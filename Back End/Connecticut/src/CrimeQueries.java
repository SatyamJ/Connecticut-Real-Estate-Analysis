import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.util.FileManager;

import java.io.*;
import java.util.Scanner;

/**
 * Created by jaiswalhome on 11/26/16.
 */
public class CrimeQueries {

    static String defaultNameSpace = "http://www.semanticweb.org/connecticut/real-estate#";
    static Model connt = null;
    static Model schema = null;
    InfModel inferredRelation = null;

    public static void main(String[] args) throws IOException
    {
        CrimeQueries cq = new CrimeQueries();

        //Load my housing
        System.out.println("Loading RDF data");
        cq.readRDF();

        // Add the ontologies
        System.out.println("Adding the Ontologies");
        schema = connt;

        //Align the ontologies
        System.out.println("Adding alignment");
        cq.addAlignment();

        //Run reasoner to  align the instances
        System.out.println("Running Reasoner");
        cq.bindReasoner();

        System.out.println("Running Query on final combined rdf");
        cq.getResults(cq.inferredRelation);

        //Fuseki part
        System.out.println("Fuseki test");
        cq.runFuseki();

    }

    private void readRDF () throws IOException
    {
        connt = ModelFactory.createOntologyModel();
        InputStream inFoafInstance =
                FileManager.get().open("data/Tranformed_Uniform Crime Reporting System Offenses By Department 2015.rdf");

        /*
        connt.read(inFoafInstance,defaultNameSpace);
        inFoafInstance =
                FileManager.get().open("data/Population_Projections.rdf");
        connt.read(inFoafInstance,defaultNameSpace);
        inFoafInstance =
                FileManager.get().open("data/Real_Estate_Sales.rdf");
        connt.read(inFoafInstance,defaultNameSpace);
        */
        inFoafInstance.close();
    }

    private void addAlignment(){
        Resource resource = schema.createResource(defaultNameSpace + "municipality");
        Property prop = schema.createProperty("http://www.w3.org/2002/07/owl#equivalentClass");
        Resource obj = schema.createResource(defaultNameSpace + "town");
        schema.add(resource,prop,obj);

        resource = schema.createResource(defaultNameSpace + "municipality");
        prop = schema.createProperty("http://www.w3.org/2002/07/owl#equivalentClass");
        obj = schema.createResource(defaultNameSpace + "name");
        schema.add(resource,prop,obj);
    }

    private void bindReasoner()
    {
        Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
        reasoner = reasoner.bindSchema(schema);
        inferredRelation = ModelFactory.createInfModel(reasoner, connt);
    }

    private void getResults(Model model){
        String town = "Andover";
        String request = "PREFIX ds:<http://data.ct.gov/resource/igy9-udjm/> \n" +
                "SELECT  ?n ?rows " +
                "WHERE { ?rows ds:jurisdiction ?n}";
        runQuery(request, model);

    }

    private void runQuery(String queryRequest, Model model) {
        StringBuffer queryStr = new StringBuffer();
        // Establish Prefixes
        queryStr.append("PREFIX ds:<http://data.ct.gov/resource/igy9-udjm/>");
        //Now add query
        queryStr.append(queryRequest);
        Query query = QueryFactory.create(queryStr.toString());
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        ResultSet response = qexec.execSelect();
        writeToFile(response);
    }

    public void writeToFile(ResultSet response){
        System.out.println("Writing output to file");
        File jsonfile = new File("output/crimeJsonOutput.json");
//        File csvfile = new File("output/csvOutput.csv");
        FileOutputStream jsonp =null;
        FileOutputStream csvp =null;
        try {
            jsonp = new FileOutputStream(jsonfile);
//            csvp = new FileOutputStream(csvfile);
        }
        catch (FileNotFoundException fnf){
            fnf.printStackTrace();
        }

        //Dont do both, you can do only one operation at a time. Comment one of them which you dont want
        ResultSetFormatter.outputAsJSON(jsonp, response);
        //ResultSetFormatter.outputAsCSV(csvp, response);
    }

    public void runFuseki(){
        Scanner reader = new Scanner(System.in);  // Reading from System.in
        System.out.println("Enter name of a town in connecticut: ");
        String town = reader.nextLine(); // Scans the next token of the input as an int.

//        QueryExecution qe = QueryExecutionFactory.sparqlService(
//                "http://localhost:3030/ds/query",
//                "PREFIX ds:<http://data.ct.gov/resource/igy9-udjm/> \n" +
//                        "SELECT  ?n ?rows " +
//                        "WHERE { ?rows ds:jurisdiction ?n}");
        QueryExecution qe = QueryExecutionFactory.sparqlService(
                "http://ec2-54-149-233-81.us-west-2.compute.amazonaws.com:3030/ds/query",
                "PREFIX ds:<http://data.ct.gov/resource/igy9-udjm/> \n" +
                        "SELECT  ?n ?rows " +
                        "WHERE { ?rows ds:jurisdiction ?n}");
        ResultSet results = qe.execSelect();
        //ResultSetFormatter.out(System.out, results);
        ResultSetFormatter.outputAsJSON(System.out, results);
        qe.close();
    }
}
