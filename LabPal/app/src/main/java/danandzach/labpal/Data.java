package danandzach.labpal;

import org.json.JSONObject;

//Handles all our static data...

/*
    Might be a good idea to put the Element table / Element symbols mapping in this class somewhere...
 */
public class Data {

    private static JSONObject ionization_data;
    private static JSONObject atomic_mass_data;
    private static JSONObject constants_data;

    public JSONObject getIonization_data(){
        return ionization_data;
    }

    public JSONObject getAtomic_mass_data(){
        return atomic_mass_data;
    }

    public JSONObject getConstants_data(){
        return constants_data;
    }

    public void setIonization_data(JSONObject j){
        ionization_data = j;
    }

    public void setAtomic_mass_data(JSONObject j){
        atomic_mass_data = j;
    }

    public void setConstants_data(JSONObject j){
        constants_data = j;
    }
}