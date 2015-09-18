package danandzach.labpal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private final String ISOTOPE_DATABASE_NAME = "Atomic Weights and Isotopes";
    private final String IONIZATION_ENERGY_DATABASE_NAME = "Ground Levels and Ionization Energy";
    private final String CONSTANTS_DATABASE_NAME = "Fundamental Physics Constants Database";
    private final String CCCBDB_DATABASE_NAME = "Computational Chemistry Database";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();


        //The following method of tabs is deprecated. In API 21 the correct way to use tab
        //navigation is TabLayout however there are not many devices on android 5 so I am
        //Using this backwards compatible method for now. -D
        actionBar.setNavigationMode(android.support.v7.app.ActionBar.NAVIGATION_MODE_TABS);

        //Add tabs for fragments. -D

        android.support.v4.app.Fragment chemSearch = new ChemistrySearch();
        android.support.v7.app.ActionBar.Tab mol_tab = actionBar.newTab();
        mol_tab.setIcon(R.drawable.search_icon);
        mol_tab.setTabListener(new NavigationTabsListener(chemSearch));
        actionBar.addTab(mol_tab);

        android.support.v4.app.Fragment calc_frag = new LabCalculations();
        android.support.v7.app.ActionBar.Tab calc_tab = actionBar.newTab();
        calc_tab.setIcon(R.drawable.calculator_icon);
        calc_tab.setTabListener(new NavigationTabsListener(calc_frag));
        actionBar.addTab(calc_tab);

        android.support.v4.app.Fragment irView = new IRView();
        android.support.v7.app.ActionBar.Tab ir_tab = actionBar.newTab();
        ir_tab.setIcon(R.drawable.ir_icon);
        ir_tab.setTabListener(new NavigationTabsListener(irView));
        actionBar.addTab(ir_tab);

        android.support.v4.app.Fragment labNotes = new LabNotes();
        android.support.v7.app.ActionBar.Tab notes_tab = actionBar.newTab();
        notes_tab.setIcon(R.drawable.notes_icon);
        notes_tab.setTabListener(new NavigationTabsListener(labNotes));
        actionBar.addTab(notes_tab);
        ((FrameLayout) findViewById(R.id.mainscreen)).getForeground().setAlpha(0);

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(Data.getAtomic_mass_data() != null){
            outState.putString(ISOTOPE_DATABASE_NAME, Data.getAtomic_mass_data().toString());
        }
        if(Data.getIonization_data() != null){
            outState.putString(IONIZATION_ENERGY_DATABASE_NAME, Data.getIonization_data().toString());
        }
        if(Data.getConstants_data() != null){
            outState.putString(CONSTANTS_DATABASE_NAME, Data.getConstants_data().toString());
        }
        if(Data.getCcc_data() != null){
            outState.putString(CCCBDB_DATABASE_NAME, Data.getCcc_data().toString());
        }

        if(Data.chosen_molecules != null){
            int p = 0;
            outState.putBoolean("x", Data.xAxisReversed);
            outState.putBoolean("y", Data.yAxisReversed);
            Bundle b = new Bundle();
            for(String s:Data.chosen_molecules.keySet()){
                String [] temp_arr = new String[Data.chosen_molecules.get(s).size()];
                for(int i = 0; i < Data.chosen_molecules.get(s).size(); i++){
                    temp_arr[i] = Data.chosen_molecules.get(s).get(i).toString();
                }
                b.putStringArray(String.valueOf(p), temp_arr);
                p++;
            }
            outState.putBundle("ir_data", b);

            Bundle b2 = new Bundle();
            if(Data.intensity_percentages != null){
                String[] temp_arr2 = new String[Data.intensity_percentages.size()];
                String[] temp_arr3 = new String[Data.intensity_percentages.size()];
                int t = 0;
                for(String x:Data.intensity_percentages.keySet()){
                    temp_arr2[t] = Data.intensity_percentages.get(x).toString();
                    temp_arr3[t] = x;
                }
                b2.putStringArray("ir_percentages", temp_arr2);
                b2.putStringArray("percent_keys", temp_arr3);
            }
            outState.putBundle("ir_data2",b2);
        }




    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String temp_ionization_db = savedInstanceState.getString(IONIZATION_ENERGY_DATABASE_NAME);
        String temp_atomic_db = savedInstanceState.getString(ISOTOPE_DATABASE_NAME);
        String temp_constants_db = savedInstanceState.getString(CONSTANTS_DATABASE_NAME);
        String temp_ccc_db = savedInstanceState.getString(CCCBDB_DATABASE_NAME);

        try {
            JSONObject restored_ionization_db = new JSONObject(temp_ionization_db);
            JSONObject restored_atomic_db = new JSONObject(temp_atomic_db);
            JSONObject restored_constants_db = new JSONObject(temp_constants_db);
            JSONObject restored_ccc_db = new JSONObject(temp_ccc_db);

            Data.setIonization_data(restored_ionization_db);
            Data.setAtomic_mass_data(restored_atomic_db);
            Data.setConstants_data(restored_constants_db);
            Data.setCcc_data(restored_ccc_db);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(savedInstanceState != null){

            Data.xAxisReversed = savedInstanceState.getBoolean("x");
            Data.yAxisReversed = savedInstanceState.getBoolean("y");
            Bundle b = savedInstanceState.getBundle("ir_data");
            Data.chosen_molecules = null;
            for(int i = 0; i < b.size(); i++){

                String[] temp_arr = b.getStringArray(String.valueOf(i));
                ArrayList<JSONObject> temp_json_list = new ArrayList<>();
                for(int z = 0; z < temp_arr.length; z++){
                    try {
                        JSONObject temp_j = new JSONObject(temp_arr[z]);
                        temp_json_list.add(temp_j);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                Data.chosen_molecules.put(temp_json_list.get(0).optString("Name"), temp_json_list);

            }
            Bundle b2 = savedInstanceState.getBundle("ir_data2");
            if(Data.intensity_percentages == null){
                Data.intensity_percentages = new HashMap<>();
            }
            for(int z = 0; z < b2.getStringArray("ir_percentages").length; z++){
                Data.intensity_percentages.put(b2.getStringArray("percent_keys")[z], Double.valueOf(b2.getStringArray("ir_percentages")[z]));
            }
        }
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    */
}

class NavigationTabsListener implements android.support.v7.app.ActionBar.TabListener{

    //Implementation of TabListener -D

    public android.support.v4.app.Fragment fragment;

    public NavigationTabsListener(android.support.v4.app.Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public void onTabReselected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {
        //Nothing when reselected
    }

    @Override
    public void onTabSelected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {
        ft.replace(R.id.fragment_container, fragment);
    }

    @Override
    public void onTabUnselected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {
        ft.remove(fragment);
    }

}
