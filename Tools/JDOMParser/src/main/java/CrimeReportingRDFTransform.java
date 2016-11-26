import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by jaiswalhome on 11/25/16.
 */
public class CrimeReportingRDFTransform {

    public static void main(String[] args) {

        try {

            SAXBuilder builder = new SAXBuilder();
            File xmlFile = new File("Uniform Crime Reporting System Offenses By Department 2015.rdf");

            Document doc = (Document) builder.build(xmlFile);
            Element rootNode = doc.getRootElement();

            Namespace dsbase = Namespace.getNamespace("dsbase","http://data.ct.gov/resource/");
            Namespace ds = Namespace.getNamespace("ds","http://data.ct.gov/resource/_6ntu-dndh/");

            // update staff id attribute
            List list = rootNode.getChildren("_6ntu-dndh", dsbase);

            System.out.println(list.size());
            for (int i = 0; i < list.size(); i++) {
                int sum=0;
                Element node = (Element) list.get(i);


                String tag = "murder_offenses";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                node.removeChild("murder_clearances", ds);

                node.removeChild("murder_value_stolen", ds);

                tag = "ngmansl_offenses";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                node.removeChild("ngmansl_clearances", ds);

                node.removeChild("rape_value_stolen", ds);

                tag = "rape_committed_offenses";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                node.removeChild("rape_committed_clearances", ds);

                tag = "rape_attempted_offenses";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                node.removeChild("rape_attempted_clearances", ds);

                tag = "robbery_highway_offenses";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                node.removeChild("robbery_highway_value_stolen", ds);

                tag = "robbery_commercial_house_offenses";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                node.removeChild("robbery_commercial_house_value_stolen", ds);

                tag = "robbery_gas_service_station_offenses";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                node.removeChild("robbery_gas_service_station_value_stolen", ds);

                tag = "robbery_chain_store_offenses";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                node.removeChild("robbery_chain_store_value_stolen", ds);

                tag = "robbery_residence_offenses";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                node.removeChild("robbery_residence_value_stolen", ds);

                tag = "robbery_bank_offenses";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                node.removeChild("robbery_bank_value_stolen", ds);

                tag = "robbery_miscellaneous_offenses";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                node.removeChild("robbery_miscellaneous_value_stolen", ds);

                tag = "robbery_firearm_offenses";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                node.removeChild("robbery_firearm_clearances", ds);

                tag = "robbery_knife_cutting_instrument_offenses";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                node.removeChild("robbery_knife_cutting_instrument_clearances", ds);

                tag = "robbery_other_dangerous_weapon_offenses";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                node.removeChild("robbery_other_dangerous_weapon_clearances", ds);

                tag = "robbery_hands_fists_feet_etc_offenses";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                node.removeChild("robbery_hands_fists_feet_etc_clearances", ds);

                tag = "agasslt_firearm_offenses";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                node.removeChild("agasslt_firearm_clearances", ds);

                tag = "agasslt_knife_cutting_instrument_offenses";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                node.removeChild("agasslt_knife_cutting_instrument_clearances", ds);

                tag = "agasslt_other_dangerous_weapon_offenses";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                node.removeChild("agasslt_other_dangerous_weapon_clearances", ds);

                tag = "agasslt_hands_fists_feet_etc_offenses";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                node.removeChild("agasslt_hands_fists_feet_etc_clearances", ds);

                tag = "burglary_residence_night_offenses";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                node.removeChild("burglary_residence_night_value_stolen", ds);

                tag = "burglary_residence_day_offenses";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                node.removeChild("burglary_residence_day_value_stolen", ds);

                tag = "burglary_residence_unknown_offenses";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                node.removeChild("burglary_residence_unknown_value_stolen", ds);

                tag = "burglary_non_residence_night_offenses";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                node.removeChild("burglary_non_residence_night_value_stolen", ds);

                tag = "burglary_non_residence_day_offenses";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                node.removeChild("burglary_non_residence_day_value_stolen", ds);

                tag = "burglary_non_residence_unknown_offenses";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                node.removeChild("burglary_non_residence_unknown_value_stolen", ds);

                tag = "burglary_forcible_entry_offenses";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                node.removeChild("burglary_forcible_entry_clearances", ds);

                tag = "burglary_unlawful_entry_no_force_offenses";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                node.removeChild("burglary_unlawful_entry_no_force_clearances", ds);

                tag = "burglary_attempted_forcible_entry_offenses";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                node.removeChild("burglary_attempted_forcible_entry_clearances", ds);

                tag = "larceny_pocket_picking_offenses";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                node.removeChild("larceny_pocket_picking_value_stolen", ds);

                tag = "larceny_shoplifting_offenses";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                node.removeChild("larceny_shoplifting_value_stolen", ds);

                tag = "larceny_from_motor_vehicles_offenses";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                node.removeChild("larceny_from_motor_vehicles_value_stolen", ds);

                tag = "larceny_mv_parts__accessories_offenses";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                node.removeChild("larceny_mv_parts__accessories_value_stolen", ds);

                tag = "larceny_bicycles_offenses";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                node.removeChild("larceny_bicycles_value_stolen", ds);

                tag = "larceny_from_buildings_offenses";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                node.removeChild("larceny_from_buildings_value_stolen", ds);

                tag = "larceny_coin_operated_machines_offenses";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                node.removeChild("larceny_coin_operated_machines_value_stolen", ds);

                tag = "larceny_all_other_offenses";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                node.removeChild("larceny_all_other_value_stolen", ds);

                tag = "larceny_200_and_over_offenses";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                node.removeChild("larceny_200_and_over_value_stolen", ds);

                tag = "larceny_50_to_200_offenses";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                node.removeChild("larceny_50_to_200_value_stolen", ds);

                tag = "larceny_under_50_offenses";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                node.removeChild("larceny_under_50_value_stolen", ds);

                node.removeChild("larceny_200_and_over_value_stolen", ds);

                node.removeChild("larceny_clearances", ds);

                tag = "mvtheft_auto_offenses";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                node.removeChild("mvtheft_auto_clearances", ds);

                tag = "mvtheft_trucks__buses_offenses";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                node.removeChild("mvtheft_trucks__buses_clearances", ds);

                tag = "mvtheft_other_vehicles_offenses";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                node.removeChild("mvtheft_other_vehicles_clearances", ds);

                node.removeChild("mvtheft_value_stolen", ds);

                tag = "arson_structural_offenses";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                node.removeChild("arson_structural_clearances", ds);
                node.removeChild("arson_structural_value_damaged", ds);

                tag = "arson_mobile_offenses";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                node.removeChild("arson_mobile_clearances", ds);
                node.removeChild("arson_mobile_value_damaged", ds);

                tag = "arson_other_offenses";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                node.removeChild("arson_other_clearances", ds);
                node.removeChild("arson_other_value_damaged", ds);

                tag = "smasslt_offenses";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                node.removeChild("smasslt_clearances", ds);
                node.removeChild("total_value_recovered", ds);

                tag = "murder_weapon_firearm";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_weapon_handgun";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_weapon_rifle";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_weapon_shotgun";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_weapon_other_gun";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_weapon_knife";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_weapon_blunt_object";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_weapon_motor_vehicle";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_weapon_personal_weap";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_weapon_poison";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_weapon_explosives";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_weapon_fire";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_weapon_narcotics";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_weapon_asphyxiation";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_weapon_other";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_weapon_unknown";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_relationship_spouse_female";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_relationship_spouse_male";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_relationship_live_in_female";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_relationship_live_in_male";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_relationship_ex_live_in_female";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_relationship_ex_live_in_male";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_relationship_co_procreator_female";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_relationship_co_procreator_male";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_relationship_parent_female";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_relationship_parent_male";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_relationship_child_female";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_relationship_child_male";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_relationship_sibling_female";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_relationship_sibling_male";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_relationship_grandparent_female";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_relationship_grandparent_male";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_relationship_grandchild_female";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_relationship_grandchild_male";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_relationship_in_law_female";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_relationship_in_law_male";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_relationship_stepparent_female";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_relationship_stepparent_male";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_relationship_stepchild_female";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_relationship_stepchild_male";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_relationship_other_family_female";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_relationship_other_family_male";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_relationship_neighbor_female";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_relationship_neighbor_male";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_relationship_acquaintance_female";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_relationship_acquaintance_male";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_relationship_boy_girlfriend_female";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_relationship_boy_girlfriend_male";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_relationship_babysittee_female";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_relationship_babysittee_male";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_relationship_child_of_partner_female";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_relationship_child_of_partner_male";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_relationship_ex_spouse_female";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_relationship_ex_spouse_male";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_relationship_employee_female";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_relationship_employee_male";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_relationship_employer_female";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_relationship_employer_male";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_relationship_friend_female";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_relationship_friend_male";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_relationship_same_sex_relat_female";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_relationship_same_sex_relat_male";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_relationship_oth_known_pers_female";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_relationship_oth_known_pers_male";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_relationship_stranger_female";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_relationship_stranger_male";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_relationship_unknown_female";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_relationship_unknown_male";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_circumstance_argument";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_circumstance_leoka";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_circumstance_drug_dealing";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_circumstance_gangland";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_circumstance_juvenile_gang";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_circumstance_lovers_quarrel";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_circumstance_mercy_killing";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_circumstance_other_felony";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_circumstance_other";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "murder_circumstance_unknown";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "leoka_homicides";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "leoka_assault_offenses";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                tag = "larceny_purse_snatching_offenses";
                sum += getTagValue(node, tag, ds);
                node.removeChild(tag, ds);

                node.removeChild("larceny_purse_snatching_value_stolen", ds);
                node.removeChild("leoka_assault_clearances", ds);


                // add new age element
                Element population = new Element("crime_reported", ds).setText(String.valueOf(sum));
                node.addContent(population);

                XMLOutputter xmlOutput = new XMLOutputter();

                // display nice nice
                xmlOutput.setFormat(Format.getPrettyFormat());
                xmlOutput.output(doc, new FileWriter("Uniform Crime Reporting System Offenses By Department 2015.rdf"));
            }
            System.out.println("File updated!");
        } catch (IOException io) {
            io.printStackTrace();
        } catch (JDOMException e) {
            e.printStackTrace();
        }
    }

    public static int getTagValue(Element node, String tag, Namespace ns){
//        System.out.println(tag);
        int value;
        String nodeText = node.getChildText(tag, ns);
        value  = Integer.valueOf(nodeText);
        node.removeChild(tag, ns);
        return value;
    }
}
