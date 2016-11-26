import org.jdom.*;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * Created by jaiswalhome on 11/26/16.
 */
public class CrimeArrestsRDFTransform {
    public static void main(String[] args) {

        String[] townArr = { "andover", "ansonia", "ashford", "avon", "barkhamsted", "beacon_falls", "berlin",
                "bethany", "bethel", "bethlehem", "bloomfield", "bolton", "bozrah", "branford", "bridgeport",
                "bridgewater", "bristol", "brookfield", "brooklyn", "burlington", "canaan", "canterbury", "canton",
                "chaplin", "cheshire", "chester", "clinton", "colchester", "colebrook", "columbia", "cornwall", "coventry",
                "cromwell", "danbury", "darien", "deep_river", "derby", "durham", "eastford", "east_granby", "east_haddam",
                "east_hampton", "east_hartford", "east_haven", "east_lyme", "easton", "east_windsor", "ellington", "enfield",
                "essex", "fairfield", "farmington", "franklin", "glastonbury", "goshen", "granby", "greenwich", "griswold",
                "groton_city", "guilford", "haddam", "hamden", "hampton", "hartford", "hartland", "harwinton", "hebron",
                "kent", "killingly", "killingworth", "lebanon", "ledyard", "lisbon", "litchfield", "lyme", "madison",
                "manchester", "mansfield", "marlborough", "meriden", "middlebury", "middlefield", "middletown", "milford",
                "monroe", "montville", "morris", "naugatuck", "new_britain", "new_canaan", "new_fairfield", "new_hartford",
                "new_haven", "newington", "new_london", "new_milford", "newtown", "norfolk", "north_branford", "north_canaan",
                "north_haven", "north_stonington", "norwalk", "norwich", "old_lyme", "old_saybrook", "orange", "oxford",
                "plainfield", "plainville", "plymouth", "pomfret", "portland", "preston", "prospect", "putnam_municipal",
                "putnam", "redding", "ridgefield", "rocky_hill", "roxbury", "salem", "salisbury", "scotland", "seymour",
                "sharon", "shelton", "sherman", "simsbury", "somers", "southbury", "southington", "south_windsor", "sprague",
                "stafford", "stamford", "sterling", "stonington", "stratford", "suffield", "thomaston", "thompson",
                "tolland", "torrington", "trumbull", "union", "vernon", "voluntown", "wallingford", "warren", "washington",
                "waterbury", "waterford", "watertown", "westbrook", "west_hartford", "west_haven", "weston", "westport",
                "wethersfield", "willington", "wilton", "winchester", "willimantic", "windham", "windsor", "windsor_locks",
                "wolcott", "woodbridge", "woodbury", "woodstock", "yale_university", "uconn_storrs", "foxwoods_casino",
                "mohegan_sun_casino", "mta", "c_c_s_u", "e_c_s_u", "uconn_health_ctr", "w_c_s_u", "s_c_s_u", "state_capitol",
                "groton_town", "groton_long_pt", "state_police_misc", "mashantucket_tribal_pd", "mohegan_tribal_pd",
                "fairfield_county", "hartford_county", "litchfield_county", "middlesex_county", "new_haven_county",
                "new_london_county", "tolland_county", "windham_county", "new_york"};

        HashMap<String, Integer> townMap = new HashMap<String, Integer>();

        for(int i=0; i<townArr.length; i++)
            townMap.put(townArr[i], new Integer(0));


        try {

            SAXBuilder builder = new SAXBuilder();
            File xmlFile = new File("Uniform Crime Reporting System Arrests 2015.rdf");

            Document doc = (Document) builder.build(xmlFile);
            Element rootNode = doc.getRootElement();

            Namespace dsbase = Namespace.getNamespace("dsbase","http://data.ct.gov/resource/");
            Namespace ds = Namespace.getNamespace("ds","http://data.ct.gov/resource/r6vz-twt4/");
            Namespace socrata = Namespace.getNamespace("socrata","http://www.socrata.com/rdf/terms#");
            Namespace rdf = Namespace.getNamespace("rdf","http://www.w3.org/1999/02/22-rdf-syntax-ns#");
            Namespace rdfs = Namespace.getNamespace("rdfs","http://www.w3.org/2000/01/rdf-schema#");

            // update staff id attribute
            List list = rootNode.getChildren("r6vz-twt4", dsbase);
//            System.out.println(list.size());
            for (int i = 0; i < list.size(); i++) {
                Element node = (Element) list.get(i);

                Iterator<String> itr = townMap.keySet().iterator();

                while(itr.hasNext()){
                    String focus = itr.next();
                    String subtag = node.getChildText(focus, ds);
                    int value  = Integer.valueOf(subtag);
                    townMap.put(focus, new Integer(townMap.get(focus).intValue() + value));
                }
            }

            rootNode.removeContent();

            for (int i = 0; i < townArr.length; i++) {
                int index = i + 1;
                Element uri = new Element("r6vz-twt4", dsbase);
                Attribute about = new Attribute("about", ds.getURI()+index, rdf);
                uri.setAttribute(about);

                Element rowID = new Element("rowID", socrata);
                rowID.setText(""+index);
                uri.addContent(rowID);

                Element member = new Element("member", rdfs);
                Attribute resource = new Attribute("resource", ds.getURI(), rdf);
                member.setAttribute(resource);
                uri.addContent(member);

                Element statIndex = new Element("stat_index", ds);
                statIndex.setText(""+index);
                uri.addContent(statIndex);

                Element town = new Element(townArr[i], ds);
                town.setText(townMap.get(townArr[i]).toString());
                uri.addContent(town);

                rootNode.addContent(uri);
            }

            XMLOutputter xmlOutput = new XMLOutputter();

            // display nice nice
            xmlOutput.setFormat(Format.getPrettyFormat());
            xmlOutput.output(doc, new FileWriter("Transformed_Uniform Crime Reporting System Arrests 2015.rdf"));

//            System.out.println("File updated!");
        } catch (IOException io) {
            io.printStackTrace();
        } catch (JDOMException e) {
            e.printStackTrace();
        }
    }
}
