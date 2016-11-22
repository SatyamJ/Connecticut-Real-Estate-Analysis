/**
 * Created by Sunil on 18-Oct-16.
 */
import java.io.IOException;
import java.io.InputStream;

import org.apache.jena.query.*;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.util.FileManager;
import org.apache.jena.rdf.model.*;


public class ConnRealEstate {
    static String defaultNameSpace = "http://www.semanticweb.org/connecticut/real-estate#";

    Model connt = null;
    Model schema = null;
    InfModel inferredRelation = null;

    public static void main(String[] args) throws IOException
    {
        ConnRealEstate connecticutRealEstate = new ConnRealEstate();

        //Load my housing
        System.out.println("Load housing");
        connecticutRealEstate.populateHousing();

        // Basic query from one rdf
        System.out.println("\nSay Hello to Myself");
        //connecticutRealEstate.getTowns(connecticutRealEstate.connt);

        //Add my population census
        System.out.println("\nAdd population census");
        connecticutRealEstate.populateCensusProjections();

        // Add the ontologies
        System.out.println("\nAdd the Ontologies");
        connecticutRealEstate.populateHousingSchema();
        connecticutRealEstate.populateCensusProjectionSchema();

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

    private void populateHousing()
    {
        connt = ModelFactory.createOntologyModel();
        InputStream inFoafInstance =
                FileManager.get().open("data/Population_and_Housing.rdf");
        connt.read(inFoafInstance,defaultNameSpace);
        try {
            inFoafInstance.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private void getTowns(Model model)
    {
        //Hello to Me - focused search
        runQuery(" select DISTINCT ?x ?name where { ?x ds:municipality ?name}", model);  //add the query string

    }

    private void runQuery(String queryRequest, Model model)
    {

        StringBuffer queryStr = new StringBuffer();

        // Establish Prefixes
        //Set default Name space first
        queryStr.append("PREFIX people: <" + defaultNameSpace + "> ");
        queryStr.append("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> ");
        queryStr.append("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ");
        queryStr.append("PREFIX foaf: <http://xmlns.com/foaf/0.1/> ");

        queryStr.append("PREFIX socrata:<http://www.socrata.com/rdf/terms#> ");
        queryStr.append("PREFIX dcat:<http://www.w3.org/ns/dcat#> ");
        queryStr.append("PREFIX ods:<http://open-data-standards.github.com/2012/01/open-data-standards#> ");
        queryStr.append("PREFIX dcterm:<http://purl.org/dc/terms/> ");
        queryStr.append("PREFIX geo:<http://www.w3.org/2003/01/geo/wgs84_pos#> ");
        queryStr.append("PREFIX skos:<http://www.w3.org/2004/02/skos/core#> ");
        queryStr.append("PREFIX dsbase:<http://data.ct.gov/resource/> ");
        queryStr.append("PREFIX ds:<http://data.ct.gov/resource/igy9-udjm/> ");

        //Now add query
        queryStr.append(queryRequest);
        Query query = QueryFactory.create(queryStr.toString());
        QueryExecution qexec = QueryExecutionFactory.create(query, model);


        try {
            ResultSet response = qexec.execSelect();

            while (response.hasNext()) {
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

            }
        }
        finally { qexec.close();}
    }

    private void getPopulationTowns(Model model)
    {
        //Hello to just my friends - navigation
        //runQuery(" select DISTINCT ?name where{ people:me foaf:knows ?friend ." +
        //       " ?friend foaf:name ?name } ", model); //add querystring
        runQuery(" select DISTINCT ?x ?z ?name ?house ?p ?q where { ?x ds:municipality ?name ." +
                "?x ds:total_housing_units ?house ." +
                "?y ds:town ?name ." +
                "?y ds:_2015_total ?z ." +
                "?y ds:_2015_male_total ?p ." +
                "?y ds:_2015_female_total ?q .}", model);  //add the query string
    }

    private void populateCensusProjections() throws IOException
    {
        InputStream inFoafInstance =
                FileManager.get().open("data/Population_Projections.rdf");
        connt.read(inFoafInstance,defaultNameSpace);
        inFoafInstance.close();
    }

    private void populateHousingSchema() throws IOException
    {
        InputStream inFoaf = FileManager.get().open("data/Population_and_Housing.rdf");
        schema = ModelFactory.createOntologyModel();

        // Use local copy for demos without network connection
        schema.read(inFoaf, defaultNameSpace);
        inFoaf.close();
    }

    private void populateCensusProjectionSchema() throws IOException
    {
        InputStream inFoafInstance =
                FileManager.get().open("data/Population_Projections.rdf");
        schema.read(inFoafInstance,defaultNameSpace);
        inFoafInstance.close();
    }

    private void addAlignment(){
        // State that :individual is equivalentClass of foaf:Person
        Resource resource = schema.createResource
                (defaultNameSpace + "municipality");
        Property prop = schema.createProperty
                ("http://www.w3.org/2002/07/owl#equivalentClass");
        Resource obj = schema.createResource
                (defaultNameSpace + "town");
        schema.add(resource,prop,obj);
    }

    private void bindReasoner()
    {
        Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
        reasoner = reasoner.bindSchema(schema);
        inferredRelation = ModelFactory.createInfModel(reasoner, connt);
    }

}
