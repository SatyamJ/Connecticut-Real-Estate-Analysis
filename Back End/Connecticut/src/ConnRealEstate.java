/**
 * Created by Sunil on 18-Oct-16.
 */
import java.io.*;

import org.apache.jena.query.*;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.util.FileManager;
import org.apache.jena.rdf.model.*;


public class ConnRealEstate {
    static String defaultNameSpace = "http://www.semanticweb.org/connecticut/real-estate#";

    static Model connt = null;
    static Model schema = null;
    InfModel inferredRelation = null;

    public static void main(String[] args) throws IOException
    {
        ConnRealEstate connecticutRealEstate = new ConnRealEstate();

        //Load my housing
        System.out.println("Load housing");
        connecticutRealEstate.readRDF();

        // Add the ontologies
        System.out.println("\nAdd the Ontologies");
        schema = connt;

        //Align the ontologies
        System.out.println("Adding alignment the two ontologies.");
        connecticutRealEstate.addAlignment();

        //Run reasoner to  align the instances
        System.out.println("\nRun a Reasoner");
        connecticutRealEstate.bindReasoner();

        System.out.println("\nRun on final combined rdf from 2 rdfs");
        connecticutRealEstate.getPopulationTowns(connecticutRealEstate.inferredRelation);

        //Fuseki part
        /* System.out.println("Fuseki test");
        QueryExecution qe = QueryExecutionFactory.sparqlService(
                "http://localhost:3030/pop/query", "PREFIX ds:<http://data.ct.gov/resource/igy9-udjm/>\n" +
                        "select DISTINCT ?x ?name where { ?x ds:municipality ?name}");
        ResultSet results = qe.execSelect();
        //ResultSetFormatter.out(System.out, results);
        ResultSetFormatter.outputAsJSON(System.out, results);
        qe.close();
        */

    }

    private void readRDF () throws IOException
    {
        connt = ModelFactory.createOntologyModel();
        InputStream inFoafInstance =
                FileManager.get().open("data/Population_and_Housing.rdf");
        connt.read(inFoafInstance,defaultNameSpace);
        inFoafInstance =
                FileManager.get().open("data/Population_Projections.rdf");
        connt.read(inFoafInstance,defaultNameSpace);
        inFoafInstance =
                FileManager.get().open("data/Real_Estate_Sales.rdf");
        connt.read(inFoafInstance,defaultNameSpace);
            inFoafInstance.close();
    }

    private void addAlignment(){
        Resource resource = schema.createResource(defaultNameSpace + "municipality");
        Property prop = schema.createProperty("http://www.w3.org/2002/07/owl#equivalentClass");
        Resource obj = schema.createResource(defaultNameSpace + "town");
        schema.add(resource,prop,obj);
    }

    private void bindReasoner()
    {
        Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
        reasoner = reasoner.bindSchema(schema);
        inferredRelation = ModelFactory.createInfModel(reasoner, connt);
    }



    private void getPopulationTowns(Model model)
    {
        runQuery(" select DISTINCT ?x ?z ?name ?house ?p ?q where { ?x ds:municipality ?name ." +
                "?x ds:total_housing_units ?house ." +
                "?y ds:town ?name ." +
                "?y ds:_2015_total ?z ." +
                "?y ds:_2015_male_total ?p ." +
                "?y ds:_2015_female_total ?q .}", model);  //add the query string

        //Write to file as csv or json
    }

    private void runQuery(String queryRequest, Model model) {

        StringBuffer queryStr = new StringBuffer();
        // Establish Prefixes
        queryStr.append("PREFIX ds:<http://data.ct.gov/resource/igy9-udjm/> ");

        //Now add query
        queryStr.append(queryRequest);
        Query query = QueryFactory.create(queryStr.toString());
        QueryExecution qexec = QueryExecutionFactory.create(query, model);

        ResultSet response = qexec.execSelect();
       /* while (response.hasNext()) {
            QuerySolution soln = response.nextSolution();
            RDFNode name = soln.get("?name");
            RDFNode housing = soln.get("?house");
            RDFNode population = soln.get("?z");
            RDFNode malepopulation = soln.get("?p");
            RDFNode femalepopulation = soln.get("?q");
            if (name != null)
                System.out.println(name.toString() +" has total housing of "+ housing.toString()+" and population of "
                        +population.toString()+" with total male "+malepopulation.toString()+" and total female "
                        +femalepopulation.toString() +"\n");
            else
                System.out.println("No Friends found!");
        } */

        writeToFile(response);
    }

    public void writeToFile(ResultSet response){
        File jsonfile = new File("c:/test/jsonOutput.json");
        File csvfile = new File("c:/test/csvOutput.csv");
        FileOutputStream jsonp =null;
        FileOutputStream csvp =null;
        try {
            jsonp = new FileOutputStream(jsonfile);
            csvp = new FileOutputStream(csvfile);
        }
        catch (FileNotFoundException fnf){
            fnf.printStackTrace();
        }
        ResultSetFormatter.outputAsJSON(jsonp, response);
        //ResultSetFormatter.outputAsCSV(csvp, response);
    }
}
