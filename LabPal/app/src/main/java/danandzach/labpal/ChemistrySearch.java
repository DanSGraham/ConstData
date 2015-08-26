package danandzach.labpal;

import android.app.ActionBar;
import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ActionMenuView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChemistrySearch.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChemistrySearch#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChemistrySearch extends Fragment {

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ChemistrySearch.
     */
    // TODO: Rename and change types and number of parameters
    public static ChemistrySearch newInstance() {
        ChemistrySearch fragment = new ChemistrySearch();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ChemistrySearch() {
        // Required empty public constructor
    }

    public LinearLayout formatIsotopeResults(String databaseName, JSONObject databaseContent){
        //Builds a layout for a result from the Atomic Mass and Isotopes Database -D
        LinearLayout resultsContainer = new LinearLayout(getActivity());
        resultsContainer.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams resultsLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        resultsContainer.setLayoutParams(resultsLayoutParams);

        TextView title = new TextView(getActivity());
        title.setText(databaseName);

        View horLine = new View(getActivity());
        horLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 5));
        horLine.setBackgroundColor(Color.GRAY);

        //Format the Standard Atomic Weight Section. -D
        String stdWeightTag = "Standard Atomic Weight Unspecified";
        String stdWeightDataStr = "";
        String comIsotopeTag= "Common Isotopes";

        if(databaseContent.has("Standard Atomic Weight")){
            stdWeightDataStr = databaseContent.optString("Standard Atomic Weight");
            stdWeightTag = "Standard Atomic Weight:";
            if(stdWeightDataStr.indexOf("[") != -1){
                stdWeightDataStr = stdWeightDataStr.replace("[", "");
                stdWeightDataStr = stdWeightDataStr.replace("]", "");
                //If there is a range of values.
                if(stdWeightDataStr.indexOf(",") != -1){
                    stdWeightDataStr = stdWeightDataStr.replace(",", " - ");
                }
                else{
                    stdWeightTag = "Standard Atomic Weight of Stable Isotope: ";
                }
            }
        }

        //Format the Common Isotopes table -D
        TableLayout isotopetable = generateIsotopeTable(databaseContent);

        TextView stdWeightText = new TextView(getActivity());
        stdWeightText.setText(stdWeightTag + stdWeightDataStr);

        resultsContainer.addView(title);
        resultsContainer.addView(horLine);
        resultsContainer.addView(stdWeightText);
        resultsContainer.addView(isotopetable);
        return resultsContainer;
    }

    public TableLayout generateIsotopeTable(JSONObject databaseContent){
        //Returns the table with isotopes formatted.
        TableLayout isotopeTable = new TableLayout(getActivity());
        TableLayout.LayoutParams tableParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        isotopeTable.setLayoutParams(tableParams);
        TableRow tableLabels = new TableRow(getActivity());
        tableLabels.setLayoutParams(tableParams);
        TextView elementLabel = new TextView(getActivity());
        elementLabel.setText("Atom Symbol");

        TextView compositionLabel = new TextView(getActivity());
        compositionLabel.setText("Isotopoic Composition");

        TextView relativeMassLabel = new TextView(getActivity());
        relativeMassLabel.setText("Relative Mass");

        tableLabels.addView(elementLabel);
        tableLabels.addView(relativeMassLabel);
        tableLabels.addView(compositionLabel);
        isotopeTable.addView(tableLabels);

        try {
            String atomicSymbol = databaseContent.getString("Atomic Symbol").trim();
            int atomicNumber = Integer.parseInt(databaseContent.getString("Atomic Number"));
            TableRow isotopeRow;

            TextView atomicSymbolText;
            TextView relativeMass;
            TextView composition;
            //Elements above 95 display all isotopes.
            if(atomicNumber >= 0){
                JSONArray isotopeArray = databaseContent.getJSONArray("isotopes");
                //Iterate through and add all to table.
                for(int i = 0; i < isotopeArray.length(); i++){
                    isotopeRow = new TableRow(getActivity());
                    isotopeRow.setLayoutParams(tableParams);

                    atomicSymbolText = new TextView(getActivity());
                    relativeMass = new TextView(getActivity());
                    composition = new TextView(getActivity());

                    atomicSymbolText.setText(atomicSymbol);
                    relativeMass.setText(isotopeArray.getJSONObject(i).optString("Relative Atomic Mass"));
                    composition.setText(isotopeArray.getJSONObject(i).optString("Isotopic Composition"));

                    isotopeRow.addView(atomicSymbolText);
                    isotopeRow.addView(relativeMass);
                    isotopeRow.addView(composition);
                    isotopeTable.addView(isotopeRow);
                }
            }
            //Check if above 94

            //Check if unstable radioactive isotope with [ but no ,


            //If is not a radioactive isotope and below 93 then only display those with abundances.
            if (atomicSymbol.equalsIgnoreCase("H")){
                //Add T to normal output
            }
            else if(atomicSymbol.equalsIgnoreCase("C")){
                //Add C14 to normal output
            }

        }

        catch (JSONException e){
            e.printStackTrace();
        }

        return isotopeTable;
    }

    public LinearLayout formatIonizationEnergyResults(String databaseName, JSONObject databaseContent){
        //Builds a layout for a result from the Ionization Energy Database -D
        LinearLayout resultsContainer = new LinearLayout(getActivity());
        resultsContainer.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams resultsLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        resultsContainer.setLayoutParams(resultsLayoutParams);



        TextView title = new TextView(getActivity());
        title.setText(databaseName);


        resultsContainer.addView(title);


        return resultsContainer;
    }

    public HashMap<String, JSONObject> queryDatabases(String query){
        //This method will return the results of a search on all databases associated with the search.
        HashMap<String, JSONObject> results = new HashMap<String, JSONObject>();

        //The following is a dummy method just used for testing layout formatting. -D
        try{
            JSONArray testArray = (Data.getAtomic_mass_data()).getJSONArray("data");
            results.put("Isotope Data", testArray.getJSONObject(0));
        }

        catch (JSONException e){
            System.out.println("THERE WAS AN ISSUE!!\n\n\n\n\n");
            e.printStackTrace();
        }

        return results;
    }

    public boolean modifyContent(HashMap<String, JSONObject> searchResults, LinearLayout layoutToModify){
        //A method to modify a LinearLayout to display search results. Results must be
        //passed as key value pairs because the database associated with the content is not
        //in the JSONObject and the modifier needs the name of the database. -D
        layoutToModify.removeAllViews();

        TextView queryTitle = new TextView(getActivity());
        queryTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        queryTitle.setTextColor(Color.BLACK);
        queryTitle.setText("Hydrogen");

        View horLine = new View(getActivity());
        horLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 5));
        horLine.setBackgroundColor(Color.DKGRAY);

        layoutToModify.addView(queryTitle);
        layoutToModify.addView(horLine);
        layoutToModify.addView(formatIsotopeResults("Isotope Database", searchResults.get("Isotope Data")));
        layoutToModify.addView(formatIonizationEnergyResults("Ionization Energy", null));


        return true;
    }

    HashMap<String, JSONObject> resultMap = new HashMap<String, JSONObject>();


    public boolean setupSearchBar(EditText searchBar, final ViewGroup content_display){
        //Currently a test method. The result is hard coded in and in the actual app it will be variable.
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.toString().equalsIgnoreCase("hydrogen")){
                    resultMap = queryDatabases("TEST");
                    modifyContent(resultMap, (LinearLayout) content_display);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chemistry_search, container, false);
        LinearLayout contentArea = (LinearLayout) v.findViewById(R.id.mol_search_content_area);
        EditText search_bar = (EditText) v.findViewById(R.id.search_field);

        setupSearchBar(search_bar, contentArea);

        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }



}
