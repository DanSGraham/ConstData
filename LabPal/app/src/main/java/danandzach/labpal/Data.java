package danandzach.labpal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

//Handles all our static data...

/*
    Might be a good idea to put the Element table / Element symbols mapping in this class somewhere...

 */

public class Data {

    private static JSONObject ionization_data;
    private static JSONObject atomic_mass_data;
    private static JSONObject constants_data;
    private static JSONObject ccc_data;

    private static boolean network_connection = false;

    //I'm using these as a means of accessing raw resources while outside of an activity
    public static InputStream constants_is;
    public static InputStream ionization_is;
    public static InputStream atomic_mass_is;
    public static InputStream ccc_is;

    private static String atomic_mass_array_name = "data";
    private static String ionization_array_name = "ionization energies data";
    private static String constants_array_name = "constant";
    private static String ccc_array_name = "ExpVibrations";

    private static PeriodicTable periodicTable;

    private static String url_atomic_mass = "http://www.nist.gov/srd/srd_data/srd144_Atomic_Weights_and_Isotopic_Compositions_for_All_Elements.json";
    private static String url_ionization = "http://www.nist.gov/srd/srd_data/srd111_NIST_Atomic_Ionization_Energies_Output.json";
    private static String url_constants = "http://www.nist.gov/srd/srd_data/srd121_allascii_2014.json";
    private static String url_ccc = "http://www.nist.gov/srd/srd_data/srd101_ExpVibrations.json";

    public static void initPeriodicTable(){
        periodicTable = new PeriodicTable();
    }

    public PeriodicTable getPeriodicTable(){ return periodicTable; }

    public static JSONObject getIonization_data(){
        return ionization_data;
    }

    public static JSONObject getAtomic_mass_data(){
        return atomic_mass_data;
    }

    public static JSONObject getConstants_data(){
        return constants_data;
    }

    public static JSONObject getCcc_data(){
        return ccc_data;
    }

    public static void setIonization_data(JSONObject j){
        ionization_data = j;
    }

    public static void setAtomic_mass_data(JSONObject j){
        atomic_mass_data = j;
    }

    public static void setConstants_data(JSONObject j){
        constants_data = j;
    }

    public static void setCcc_data(JSONObject j){
        ccc_data = j;
    }

    public static String getUrl_ionization(){
        return url_ionization;
    }

    public static String getUrl_atomic_mass(){
        return url_atomic_mass;
    }

    public static String getUrl_constants(){
        return url_constants;
    }

    public static String getUrl_ccc(){
        return url_ccc;
    }

    public static JSONArray get_array(JSONObject database, String identifier){
        try {
            return database.getJSONArray(identifier);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getAtomic_mass_array_name(){
        return atomic_mass_array_name;
    }

    public static String getIonization_array_name(){
        return ionization_array_name;
    }

    public static String getConstants_array_name(){
        return constants_array_name;
    }

    public static String getCcc_array_name(){
        return ccc_array_name;
    }

    public static void setNetwork_connection(boolean b){
        network_connection = b;
    }

    public static boolean getNetwork_connection(){
        return network_connection;
    }
}