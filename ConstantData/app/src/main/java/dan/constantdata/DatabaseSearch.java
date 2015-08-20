package dan.constantdata;

import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

//ToDo:
//Display search results without clicking a button.
//Suggestions list
//Resolve conflicts
//Display from multiple databases
//Add icon images

public class DatabaseSearch extends AppCompatActivity {


    //Updates which databases are searched based on what toggles are selected
    Set<String> selected_databases = new HashSet<String>();

    Hashtable<String, JSONObject> loaded_databases = new Hashtable<String, JSONObject>();


    public boolean updateContent(LinearLayout content_page, String content){
        //This method will update the content based on the search results.
        TextView content_text = (TextView) findViewById(R.id.content);
        if(content != content_text.getText().toString()) {
            content_text.setText(content);
        }
        return true;
    }

    public boolean loadDatabase(String dbToLoad, JSON_Adapter json_adapter){

        JSONObject json_data = null;
        boolean returnVal = true;
        //If new databases are added, they must be loaded here
        if (!(loaded_databases.containsKey(dbToLoad))) {
            returnVal = false;
            switch (dbToLoad) {
                case "Atomic Weights and Isotopic Compositions Database":
                    try {
                        json_data = json_adapter.get_JSON_object(new URL("http://www.nist.gov/srd/srd_data/srd144_Atomic_Weights_and_Isotopic_Compositions_for_All_Elements.json"));
                        returnVal = true;
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    break;

                case "Ground Levels and Ionization Energies for Neutral Atoms Database":
                    try {
                        json_data = json_adapter.get_JSON_object(new URL("http://www.nist.gov/srd/srd_data/srd111_NIST_Atomic_Ionization_Energies_Output.json"));
                        returnVal = true;
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    break;

                case "CODATA Fundamental Physical Constants Database":
                    try {
                        json_data = json_adapter.get_JSON_object(new URL("http://www.nist.gov/srd/srd_data/srd121_allascii_2014.json"));
                        returnVal = true;
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    break;

                default:
                    //Need to handle exceptions
                    json_data = null;
                    break;
            }

            loaded_databases.put(dbToLoad, json_data);
        }
        return returnVal;
    }

    public boolean setupToggleButtons(LinearLayout dbContainer, final JSON_Adapter currAdapter){
        for (int i = 0; i < dbContainer.getChildCount(); i++){
            ToggleButton currTog = (ToggleButton) dbContainer.getChildAt(i);
            currTog.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked){
                        selected_databases.add(buttonView.getText().toString());
                        loadDatabase(buttonView.getText().toString(),currAdapter);

                    }
                    else {
                        selected_databases.remove(buttonView.getText().toString());
                    }
                    //Refresh the query
                    //System.out.println(selected_databases.toString());

                }
            });
        }
        return true;
    }


    public boolean setupSearchBar(EditText search_bar, final LinearLayout page_display){
        search_bar.addTextChangedListener(new TextWatcher(){


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String content_string = "";
                for (String sel_db: selected_databases){
                    JSONObject curr_db = (JSONObject) loaded_databases.get(sel_db);
                    content_string += searchDatabases(sel_db, s.toString());
                }
                updateContent(page_display, content_string);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return true;
    };

    public String searchDatabases(String database_to_search, String search_query){
        switch (database_to_search){
            case "Atomic Weights and Isotopic Compositions Database":
                try {
                    JSONArray jsonArray = ((JSONObject) loaded_databases.get(database_to_search)).getJSONArray("data");
                    StringBuilder sb = new StringBuilder("");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        if (jsonArray.getJSONObject(i).getString("Atomic Symbol").trim().equalsIgnoreCase(search_query.trim())) {
                            sb.append("Atomic Symbol: ").append(jsonArray.getJSONObject(i).getString("Atomic Symbol")).append("\n").append("Atomic Number: ")
                                    .append(jsonArray.getJSONObject(i).getString("Atomic Number")).append("\n\n").append("Isotopes: ").append("\n\n");

                            for (int z = 0; z < jsonArray.getJSONObject(i).getJSONArray("isotopes").length(); z++) {
                                sb.append("Atomic Symbol: ").append(jsonArray.getJSONObject(i).getJSONArray("isotopes").getJSONObject(z).optString("Atomic Symbol")).append("\n")
                                        .append("Mass Number: ").append(jsonArray.getJSONObject(i).getJSONArray("isotopes").getJSONObject(z).optString("Mass Number")).append("\n")
                                        .append("Isotopic Composition: ").append(jsonArray.getJSONObject(i).getJSONArray("isotopes").getJSONObject(z).optString("Isotopic Composition"))
                                        .append("Relative Atomic Mass: ").append(jsonArray.getJSONObject(i).getJSONArray("isotopes").getJSONObject(z).optString("Relative Atomic Mass"))
                                        .append("\n\n\n");
                            }

                            sb.append("Notes: ").append(jsonArray.getJSONObject(i).optString("Notes")).append("\n")
                                    .append("Standard Atomic Weight: ").append(jsonArray.getJSONObject(i).optString("Standard Atomic Weight")).append("\n\n");
                            return sb.toString();
                        }
                    }
                    return sb.toString();
                }
                catch (JSONException e) {
                    e.printStackTrace();
                    return "There was a JSON Error0!";
                }

            case "Ground Levels and Ionization Energies for Neutral Atoms Database":
                try {
                    JSONArray jsonArray = ((JSONObject) loaded_databases.get(database_to_search)).getJSONArray("ionization energies data");
                    StringBuilder sb = new StringBuilder("");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        System.out.println(jsonArray.getJSONObject(i).getString("Element Name"));
                        if (jsonArray.getJSONObject(i).getString("Element Name").toLowerCase().equals(search_query.toLowerCase())) {


                            sb.append(jsonArray.getJSONObject(i).getString("Element Name")
                                    + "\n\n" + "Atomic Number: " + jsonArray.getJSONObject(i).getString("Atomic Number") + "\n" +
                                    "Ground Shells: " + jsonArray.getJSONObject(i).getString("Ground Shells") + "\n" +
                                    "Ground Level: " + jsonArray.getJSONObject(i).getString("Ground Level") + "\n" +
                                    "Ionization Energy (eV): " + jsonArray.getJSONObject(i).getString("Ionization Energy (eV)") + "\n" +
                                    "References: " + jsonArray.getJSONObject(i).getString("References") + "\n" +
                                    "Reference URL: " + jsonArray.getJSONObject(i).getString("ReferencesURL") + "\n\n");
                        }
                    }
                    return sb.toString();
                }
                catch (JSONException e) {
                    e.printStackTrace();
                    return "THERE WAS A JSON ERROR1!";
                }

            case "CODATA Fundamental Physical Constants Database":
                try {
                    JSONArray jsonArray = ((JSONObject) loaded_databases.get(database_to_search)).getJSONArray("constant");
                    StringBuilder sb = new StringBuilder("");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        if (jsonArray.getJSONObject(i).getString("Quantity ").equals(search_query.toLowerCase())) {
                            sb.append(jsonArray.getJSONObject(i).getString("Quantity ")
                                    + "\n\n" + "Value: " + jsonArray.getJSONObject(i).getString("Value") + "\n" +
                                    "Uncertainty: " + jsonArray.getJSONObject(i).getString("Uncertainty") + "\n" +
                                    "Units: " + jsonArray.getJSONObject(i).getString("Unit") + "\n\n\n");
                        }
                    }
                    return sb.toString();
                }
                catch (JSONException e) {
                    e.printStackTrace();
                    return "THERE WAS A JSON ERROR2!";
                }
            default:
                return "ERROR";

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_search);

        LinearLayout page_container = (LinearLayout) findViewById(R.id.page_layout);
        LinearLayout database_toggle_container = (LinearLayout) findViewById(R.id.db_container);
        EditText search_bar = (EditText) findViewById(R.id.search_bar);
        TextView content_text = (TextView) findViewById(R.id.content);
        JSON_Adapter json_adapter = new JSON_Adapter();
        boolean test = setupToggleButtons(database_toggle_container, json_adapter);
        boolean test2 = setupSearchBar(search_bar, page_container);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_database_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
