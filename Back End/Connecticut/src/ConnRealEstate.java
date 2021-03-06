/**
 * Created by Sunil on 18-Oct-16.
 */
import java.io.*;
import java.util.Scanner;

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

    static Model healthconnt = null;
    static Model healthschema = null;
    InfModel healthInferredRelation = null;

    public static void main(String[] args) throws IOException
    {
        ConnRealEstate connecticutRealEstate = new ConnRealEstate();

        //Load my housing
        System.out.println("Loading RDF data");
        connecticutRealEstate.readRDF();

        System.out.println("Loading RDF data");
        connecticutRealEstate.readHealthRDF();


        // Add the ontologies
        System.out.println("Adding the Ontologies");
        schema = connt;

        System.out.println("Adding the Ontologies");
        healthschema = healthconnt;

        //Align the ontologies
        System.out.println("Adding alignment");
        connecticutRealEstate.addAlignment();

        System.out.println("Adding alignment");
        connecticutRealEstate.addHealthAlignment();


        //Run reasoner to  align the instances
        System.out.println("Running Reasoner");
        connecticutRealEstate.bindReasoner();

        System.out.println("Running Reasoner");
        connecticutRealEstate.bindHealthReasoner();

        System.out.println("Running Query on final combined rdf");
        connecticutRealEstate.getResults(connecticutRealEstate.inferredRelation);

        System.out.println("Running Query on final combined rdf");
        connecticutRealEstate.getHealthResults(connecticutRealEstate.healthInferredRelation);
        
        //Fuseki part
        System.out.println("Fuseki test");
        connecticutRealEstate.runFuseki();

        // System.out.println("Fuseki test");
        // connecticutRealEstate.runHealthFuseki();

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

    private void readHealthRDF () throws IOException
    {
        healthconnt = ModelFactory.createOntologyModel();
        InputStream inFoafInstance =
                FileManager.get().open("data/nursingFacilityRegistry.rdf");
        connt.read(inFoafInstance,defaultNameSpace);
        inFoafInstance =
                FileManager.get().open("data/familyDayCare.rdf");
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

    private void addHealthAlignment() {
        
        Resource resource = healthschema.createResource
                (defaultNameSpace + "town");
        Property prop = healthschema.createProperty
                ("http://www.w3.org/2002/07/owl#equivalentClass");
        Resource obj = schema.createResource
                (defaultNameSpace + "city");
        healthschema.add(resource,prop,obj);
    }

    private void bindReasoner()
    {
        Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
        reasoner = reasoner.bindSchema(schema);
        inferredRelation = ModelFactory.createInfModel(reasoner, connt);
    }

    private void bindHealthReasoner()
    {
        Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
        reasoner = reasoner.bindSchema(healthschema);
        healthInferredRelation = ModelFactory.createInfModel(reasoner, connt);
    }

    private void getResults(Model model){
        runQuery(" select DISTINCT ?name ?total_houses ?occupied_houses ?vacant_houses" +
                " ?total_pop_2015 ?total_male_2015 ?total_female_2015 " +
                " ?address ?list_year ?assessed_value ?sales_price ?sales_ratio ?lat ?long where { " +
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
                "      ds:salesratio ?sales_ratio ;" +
                "       ds:location  ?ns4 . " +
                " ?ns4  geo:lat ?lat ;" +
                "      geo:long ?long" +

                "}", model);  //add the query string
    }

    private void getHealthResults(Model model){
        runHealthQuery("select DISTINCT ?name ?town ?avgCapacity ?avgRate ?deliveryAddress where { ?facility ds:facility_name ?name ." +
        "?facility ds:avgCapacity ?avgCapacity ." +
        "?facilty usps:deliveryAddress ?deliveryAddress ." +
        "?facility ds:town ?town . } ", model);  //add the query string
    }

    private void runQuery(String queryRequest, Model model) {
        StringBuffer queryStr = new StringBuffer();
        // Establish Prefixes
        queryStr.append("PREFIX ds:<http://data.ct.gov/resource/igy9-udjm/> ");
        queryStr.append(" PREFIX geo:<http://www.w3.org/2003/01/geo/wgs84_pos#> ");
        //Now add query
        queryStr.append(queryRequest);
        Query query = QueryFactory.create(queryStr.toString());
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        ResultSet response = qexec.execSelect();
        writeToFile(response);
    }

    private void runHealthQuery(String queryRequest, Model model) {
       StringBuffer queryStr = new StringBuffer();
        
      // Establish Prefixes
      //Set default Name space first
      queryStr.append("PREFIX rdfs" + ": <" +  
                      "http://www.w3.org/2000/01/rdf-schema#" + "> ");
      queryStr.append("PREFIX rdf" + ": <" + "http://www.w3.org/1999/02/22-rdf-syntax-ns#" + "> ");
      queryStr.append("PREFIX ds" + ": <" + "http://data.ct.gov/resource/rm6f-b9qj/" 
                       + "> ");
      queryStr.append("PREFIX usps" + ": <" + "http://www.w3.org/2000/10/swap/pim/usps#" 
              + "> ");
            
      queryStr.append(queryRequest);
      Query query = QueryFactory.create(queryStr.toString());
      QueryExecution qexec = QueryExecutionFactory.create(query, model);
            
      try 
      {
        ResultSet response = qexec.execSelect();
        writeToFile(response);
      }
      finally { 
        qexec.close();
      }             

    }


    public void writeToFile(ResultSet response){
        System.out.println("Writing output to file");
        File jsonfile = new File("output/jsonOutput.json");
        File csvfile = new File("output/csvOutput.csv");
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
        //ResultSetFormatter.outputAsJSON(jsonp, response);
        ResultSetFormatter.outputAsCSV(csvp, response);
    }

    public void runFuseki(){
        Scanner reader = new Scanner(System.in);  // Reading from System.in
        System.out.println("Enter name of a town in connecticut, to get addresses in that town (case sensitive): ");
        String name = reader.nextLine(); // Scans the next token of the input as an int.

        QueryExecution qe = QueryExecutionFactory.sparqlService(
                "http://localhost:3030/ds/query", "PREFIX ds:<http://data.ct.gov/resource/igy9-udjm/>\n" +
                        "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                        "select DISTINCT ?name ?total_pop_2015 ?address ?list_year where {\n" +
                        "  ?ns1 ds:municipality ?name .\n" +
                        "  ?ns2 ds:town ?name ;\n" +
                        "       ds:_2015_total ?total_pop_2015 .\n" +
                        "  ?ns3 ds:name ?name ;\n" +
                        "       ds:address ?address ;\n" +
                        "       ds:listyear ?list_year .\n" +
                        "  Filter(?name = \""+name+"\" ) \n" +
                        "}");
        ResultSet results = qe.execSelect();
        //ResultSetFormatter.out(System.out, results);
        ResultSetFormatter.outputAsJSON(System.out, results);
        qe.close();
    }

    public void runHealthFuseki(){
        Scanner reader = new Scanner(System.in);  // Reading from System.in
        System.out.println("Enter name of a town in connecticut, to get addresses in that town (case sensitive): ");
        String name = reader.nextLine(); // Scans the next token of the input as an int.

        int n= 139;
        int m = 150;
        QueryExecution qe = QueryExecutionFactory.sparqlService(
                "http://localhost:3030/ds/query", "PREFIX ds:<http://data.ct.gov/resource/rm6f-b9qj/>\n" +
                        "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                        "PREFIX usps: <http://www.w3.org/2000/10/swap/pim/usps#>\n" +
                        "select DISTINCT ?name ?town ?avgCapacity ?avgRate ?deliveryAddress where { ?facility ds:facility_name ?name ."
        +"?facility ds:avgCapacity ?avgCapacity ."
        +"?facilty usps:deliveryAddress ?deliveryAddress ."
        +"?facility ds:town ?town ."
        +" Filter(xsd:integer(?avgCapacity) >= "+n+" && xsd:integer(?avgCapacity) <=" + m+")} ");
        ResultSet results = qe.execSelect();
        //ResultSetFormatter.out(System.out, results);
        ResultSetFormatter.outputAsJSON(System.out, results);
        qe.close();
    }
}