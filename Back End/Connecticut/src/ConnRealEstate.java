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
        connecticutRealEstate.getResults(connecticutRealEstate.inferredRelation);

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
        runQuery(" select DISTINCT ?name ?total_houses ?occupied_houses ?vacant_houses" +
                " ?total_pop_2015 ?total_male_2015 ?total_female_2015 " +
                " ?address ?list_year ?assessed_value ?sales_price ?sales_ratio where { " +
                " ?ns1 ds:municipality ?name ;" +
                "      ds:total_housing_units ?total_houses ;" +
                "      ds:occupied_housing_units ?occupied_houses ;" +
                "      ds:vacant_housing_units ?vacant_houses ." +
                " ?ns2 ds:town ?name ;" +
                "     ds:_2015_total ?total_pop_2015 ;" +
                "     ds:_2015_male_total ?total_male_2015 ;" +
                "     ds:_2015_female_total ?total_female_2015 ." +
                " ?ns3 ds:name ?name ;" +
                "      ds:address ?address ;" +
                "      ds:listyear ?list_year ;" +
                "      ds:assessedvalue ?assessed_value ;" +
                "      ds:saleprice ?sales_price ;" +
                "      ds:salesratio ?sales_ratio ." +
                "}", model);  //add the query string
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

        //Dont do both, you can do only one operation at a time. Comment one of them which you dont want
        ResultSetFormatter.outputAsJSON(jsonp, response);
        //ResultSetFormatter.outputAsCSV(csvp, response);
    }
}