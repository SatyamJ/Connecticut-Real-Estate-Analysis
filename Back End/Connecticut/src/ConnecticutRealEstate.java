/**
 * Created by Sunil on 18-Oct-16.
 */
import java.io.IOException;
import java.io.InputStream;

import org.apache.jena.query.*;
import org.apache.jena.reasoner.*;
import org.apache.jena.reasoner.rulesys.*;
import org.apache.jena.util.FileManager;
import org.apache.jena.rdf.model.*;


public class ConnecticutRealEstate {
    static String defaultNameSpace = "http://www.semanticweb.org/connecticut/real-estate#";

    Model _friends = null;
    Model schema = null;
    InfModel inferredFriends = null;

    public static void main(String[] args) throws IOException
    {
        ConnecticutRealEstate connecticutRealEstate = new ConnecticutRealEstate();

        //Load my FOAF friends
        System.out.println("Load my FOAF Friends");
        connecticutRealEstate.populateFOAFFriends();

        // Say Hello to myself
        System.out.println("\nSay Hello to Myself");
        connecticutRealEstate.mySelf(connecticutRealEstate._friends);


    }

    private void populateFOAFFriends()
    {
        _friends = ModelFactory.createOntologyModel();
        InputStream inFoafInstance =
                FileManager.get().open("data/Population_and_Housing.rdf");
        _friends.read(inFoafInstance,defaultNameSpace);
        try {
            inFoafInstance.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private void mySelf(Model model)
    {
//Hello to Me - focused search
        runQuery(" select DISTINCT ?name where { ?x ds:municipality ?name}", model);  //add the query string

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
                if (name != null)
                    System.out.println("Hello to " + name.toString());
                else
                    System.out.println("No Friends found!");

            }
        }
	    finally { qexec.close();}
        }

}
