import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.sun.org.apache.xml.internal.utils.NameSpace;
import org.jdom.*;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class ModifyXMLFileNew {
    public static void main(String[] args) {

        try {

            SAXBuilder builder = new SAXBuilder();
            File xmlFile = new File("Connecticut_Town_Population_Projections_2015-2025.rdf");

            Document doc = (Document) builder.build(xmlFile);
            Element rootNode = doc.getRootElement();

            Namespace dsbase = Namespace.getNamespace("dsbase","http://data.ct.gov/resource/");
            Namespace ds = Namespace.getNamespace("ds","http://data.ct.gov/resource/mze8-865g/");

            // update staff id attribute
            List list = rootNode.getChildren("mze8-865g", dsbase);
            System.out.println(list.size());
            for (int i = 0; i < list.size(); i++) {
                int sum=0;
                Element node = (Element) list.get(i);

                String string = node.getChildText("_2025_female_20_24", ds);
                int value1  = Integer.valueOf(string);

                string = node.getChildText("_2025_female_25_29", ds);
                int value2  = Integer.valueOf(string);

                string = node.getChildText("_2025_female_30_34", ds);
                int value3  = Integer.valueOf(string);

                string = node.getChildText("_2025_female_35_39", ds);
                int value4  = Integer.valueOf(string);


                sum  = value1 + value2 +value3 + value4 ;

                System.out.println(sum);

                // remove nodes elements
                node.removeChild("_2025_female_20_24", ds);
                node.removeChild("_2025_female_25_29", ds);
                node.removeChild("_2025_female_30_34", ds);
                node.removeChild("_2025_female_35_39", ds);

                // add new age element
                Element population = new Element("_2025_female_20_40", ds).setText(String.valueOf(sum));
                node.addContent(population);

                XMLOutputter xmlOutput = new XMLOutputter();

                // display nice nice
                xmlOutput.setFormat(Format.getPrettyFormat());
                xmlOutput.output(doc, new FileWriter("Connecticut_Town_Population_Projections_2015-2025.rdf"));
            }
            System.out.println("File updated!");
        } catch (IOException io) {
            io.printStackTrace();
        } catch (JDOMException e) {
            e.printStackTrace();
        }
    }
}