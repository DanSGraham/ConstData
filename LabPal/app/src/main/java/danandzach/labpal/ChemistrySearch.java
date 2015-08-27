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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.HashSet;


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

    public RelativeLayout formatIsotopeResults(String databaseName, JSONObject databaseContent){
        //Builds a layout for a result from the Atomic Mass and Isotopes Database -D

        final int TITLE_ID = 1;
        final int UNDERLINE_ID = 2;
        final int STANDARD_WEIGHT_LABEL_ID = 3;
        final int STANDARD_WEIGHT_VALUE_ID = 4;
        final int COMMON_ISOTOPE_LABEL_ID = 5;
        final int COMMON_ISOTOPE_TABLE_ID = 6;


        //Setup data layout. -D
        RelativeLayout resultsContainer = new RelativeLayout(getActivity());
        RelativeLayout.LayoutParams resultsLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        resultsContainer.setLayoutParams(resultsLayoutParams);


        //Setup section title. -D
        TextView title = new TextView(getActivity());
        RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        titleParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        titleParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        title.setText(databaseName);
        title.setLayoutParams(titleParams);
        title.setId(TITLE_ID);

        //Setup expand section button -D
        Button expandViewButton = new Button(getActivity());
        //Needs to be on same line as title but parent align right

        //Underline -D
        View underline = new View(getActivity());
        RelativeLayout.LayoutParams underlineParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 5);
        underlineParams.addRule(RelativeLayout.BELOW, TITLE_ID);
        underline.setBackgroundColor(Color.GRAY);
        underline.setLayoutParams(underlineParams);
        underline.setId(UNDERLINE_ID);



        //Format the Standard Atomic Weight Section. -D
        String stdWeightTag = "Standard Atomic Weight Unspecified";
        String stdWeightDataStr = "";

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

        TextView stdWeightLabel = new TextView(getActivity());
        stdWeightLabel.setText(stdWeightTag);
        RelativeLayout.LayoutParams stdWeightLabelLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        stdWeightLabelLayoutParams.addRule(RelativeLayout.BELOW, UNDERLINE_ID);
        stdWeightLabelLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        stdWeightLabel.setLayoutParams(stdWeightLabelLayoutParams);
        stdWeightLabel.setId(STANDARD_WEIGHT_LABEL_ID);

        TextView stdWeightData = new TextView(getActivity());
        stdWeightData.setText(stdWeightDataStr);
        RelativeLayout.LayoutParams stdWeightDataLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        stdWeightDataLayoutParams.addRule(RelativeLayout.BELOW, UNDERLINE_ID);
        stdWeightDataLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        stdWeightData.setLayoutParams(stdWeightDataLayoutParams);
        stdWeightData.setId(STANDARD_WEIGHT_VALUE_ID);


        //Format the Common Isotopes table -D
        String comIsotopes = "Common Isotopes";

        TableLayout isotopeTable = generateIsotopeTable(databaseContent);
        RelativeLayout.LayoutParams isotopeTableLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        isotopeTableLayoutParams.addRule(RelativeLayout.BELOW, STANDARD_WEIGHT_LABEL_ID);
        isotopeTable.setLayoutParams(isotopeTableLayoutParams);
        isotopeTable.setId(COMMON_ISOTOPE_TABLE_ID);

        //Add all components to the layout -D
        resultsContainer.addView(title);
        resultsContainer.addView(underline);
        resultsContainer.addView(stdWeightLabel);
        resultsContainer.addView(stdWeightData);
        resultsContainer.addView(isotopeTable);
        return resultsContainer;
    }

    public TableLayout generateIsotopeTable(JSONObject databaseContent){
        //Returns the table with isotopes formatted.

        final float COLUMN_ONE_WEIGHT = 0.2f;
        final float COLUMN_TWO_WEIGHT = 0.4f;
        final float COLUMN_THREE_WEIGHT = 0.4f;
        TableLayout isotopeTable = new TableLayout(getActivity());
        TableLayout.LayoutParams tableParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        isotopeTable.setLayoutParams(tableParams);

        TableRow tableLabels = new TableRow(getActivity());
        tableLabels.setLayoutParams(tableParams);

        TextView elementLabel = new TextView(getActivity());
        TableRow.LayoutParams firstColumnLayoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, COLUMN_ONE_WEIGHT);
        elementLabel.setText(" ");
        elementLabel.setLayoutParams(firstColumnLayoutParams);

        TextView compositionLabel = new TextView(getActivity());
        TableRow.LayoutParams secondColumnLayoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, COLUMN_TWO_WEIGHT);
        compositionLabel.setText("Isotopoic Composition");
        compositionLabel.setLayoutParams(secondColumnLayoutParams);

        TextView relativeMassLabel = new TextView(getActivity());
        TableRow.LayoutParams thirdColumnLayoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, COLUMN_THREE_WEIGHT);
        relativeMassLabel.setText("Relative Mass");
        relativeMassLabel.setLayoutParams(thirdColumnLayoutParams);

        tableLabels.addView(elementLabel);
        tableLabels.addView(relativeMassLabel);
        tableLabels.addView(compositionLabel);
        isotopeTable.addView(tableLabels);

        try {
            String atomicSymbol = databaseContent.getString("Atomic Symbol").trim();
            int atomicNumber = Integer.parseInt(databaseContent.getString("Atomic Number"));
            TableRow isotopeRow;

            boolean symbolInTable = false;

            TextView atomicSymbolText;
            TextView relativeMass;
            TextView composition;

            JSONArray isotopeArray = databaseContent.getJSONArray("isotopes");

            //Elements above 95 display all isotopes.
            if(atomicNumber >= 95){
                //Iterate through and add all to table.
                for(int i = 0; i < isotopeArray.length(); i++){
                    isotopeRow = new TableRow(getActivity());
                    isotopeRow.setLayoutParams(tableParams);

                    atomicSymbolText = new TextView(getActivity());
                    relativeMass = new TextView(getActivity());
                    composition = new TextView(getActivity());

                    atomicSymbolText.setText("");
                    if(!symbolInTable){
                        atomicSymbolText.setText(atomicSymbol);
                        symbolInTable = true;
                    }
                    atomicSymbolText.setLayoutParams(firstColumnLayoutParams);

                    relativeMass.setText(isotopeArray.getJSONObject(i).optString("Relative Atomic Mass"));
                    relativeMass.setLayoutParams(secondColumnLayoutParams);

                    composition.setText(isotopeArray.getJSONObject(i).optString("Isotopic Composition"));
                    composition.setLayoutParams(thirdColumnLayoutParams);

                    isotopeRow.addView(atomicSymbolText);
                    isotopeRow.addView(relativeMass);
                    isotopeRow.addView(composition);
                    isotopeTable.addView(isotopeRow);
                }
            }

            else{
                String standardWeightString = databaseContent.getString("Standard Atomic Weight").toString();

                //Check if is a radioactive element.
                if((standardWeightString.indexOf("[") != -1 && standardWeightString.indexOf(",") == -1) || atomicSymbol.equalsIgnoreCase("U") || atomicSymbol.equalsIgnoreCase("C")) {
                    HashSet<Integer> commonIsotopeWeights = new HashSet<Integer>();
                    //Common isotope weights are derived from the NIST isotope database.
                    switch (atomicSymbol) {
                        case "C":
                            commonIsotopeWeights.add(12);
                            commonIsotopeWeights.add(13);
                            commonIsotopeWeights.add(14);
                            break;

                        case "Tc":
                            commonIsotopeWeights.add(97);
                            commonIsotopeWeights.add(98);
                            commonIsotopeWeights.add(99);
                            break;

                        case "Pm":
                            commonIsotopeWeights.add(145);
                            commonIsotopeWeights.add(147);
                            break;

                        case "Po":
                            commonIsotopeWeights.add(209);
                            commonIsotopeWeights.add(210);
                            break;

                        case "At":
                            commonIsotopeWeights.add(210);
                            commonIsotopeWeights.add(211);
                            break;

                        case "Rn":
                            commonIsotopeWeights.add(211);
                            commonIsotopeWeights.add(220);
                            commonIsotopeWeights.add(222);
                            break;

                        case "Fr":
                            commonIsotopeWeights.add(223);
                            break;

                        case "Ra":
                            commonIsotopeWeights.add(223);
                            commonIsotopeWeights.add(224);
                            commonIsotopeWeights.add(226);
                            commonIsotopeWeights.add(228);
                            break;

                        case "Ac":
                            commonIsotopeWeights.add(227);
                            break;

                        case "U":
                            commonIsotopeWeights.add(233);
                            commonIsotopeWeights.add(234);
                            commonIsotopeWeights.add(235);
                            commonIsotopeWeights.add(236);
                            commonIsotopeWeights.add(238);

                        case "Np":
                            commonIsotopeWeights.add(236);
                            commonIsotopeWeights.add(237);
                            break;

                        case "Pu":
                            commonIsotopeWeights.add(238);
                            commonIsotopeWeights.add(239);
                            commonIsotopeWeights.add(240);
                            commonIsotopeWeights.add(241);
                            commonIsotopeWeights.add(242);
                            commonIsotopeWeights.add(244);
                            break;

                        default:
                            break;
                    }

                    for (int i = 0; i < isotopeArray.length(); i++) {

                        String relAtomicMassString = isotopeArray.getJSONObject(i).optString("Relative Atomic Mass");
                        int approxAtomicMass = Math.round(Float.parseFloat(relAtomicMassString.split("\\(")[0]));

                        if (commonIsotopeWeights.contains((Integer) approxAtomicMass)) {
                            isotopeRow = new TableRow(getActivity());
                            isotopeRow.setLayoutParams(tableParams);

                            atomicSymbolText = new TextView(getActivity());
                            relativeMass = new TextView(getActivity());
                            composition = new TextView(getActivity());

                            atomicSymbolText.setText("");
                            if(!symbolInTable) {
                                atomicSymbolText.setText(atomicSymbol);
                                symbolInTable = true;
                            }
                            atomicSymbolText.setLayoutParams(firstColumnLayoutParams);

                            relativeMass.setText(isotopeArray.getJSONObject(i).optString("Relative Atomic Mass"));
                            relativeMass.setLayoutParams(secondColumnLayoutParams);

                            composition.setText("");
                            if (isotopeArray.getJSONObject(i).has("Isotopic Composition")) {
                                composition.setText(isotopeArray.getJSONObject(i).optString("Isotopic Composition"));
                            }
                            composition.setLayoutParams(thirdColumnLayoutParams);

                            isotopeRow.addView(atomicSymbolText);
                            isotopeRow.addView(relativeMass);
                            isotopeRow.addView(composition);
                            isotopeTable.addView(isotopeRow);
                        }
                    }
                }
                else if(atomicSymbol.equalsIgnoreCase("H")) {
                    //Display D and T
                    for (int i = 0; i < isotopeArray.length(); i++) {

                        String relAtomicMassString = isotopeArray.getJSONObject(i).optString("Relative Atomic Mass");
                        int approxAtomicMass = Math.round(Float.parseFloat(relAtomicMassString.split("\\(")[0]));

                        if (approxAtomicMass == 1 || approxAtomicMass == 2 || approxAtomicMass == 3) {
                            isotopeRow = new TableRow(getActivity());
                            isotopeRow.setLayoutParams(tableParams);

                            atomicSymbolText = new TextView(getActivity());
                            relativeMass = new TextView(getActivity());
                            composition = new TextView(getActivity());

                            atomicSymbol = isotopeArray.getJSONObject(i).optString("Atomic Symbol");
                            atomicSymbolText.setText(atomicSymbol);
                            atomicSymbolText.setLayoutParams(firstColumnLayoutParams);

                            relativeMass.setText(isotopeArray.getJSONObject(i).optString("Relative Atomic Mass"));
                            relativeMass.setLayoutParams(secondColumnLayoutParams);

                            composition.setText("");
                            if (isotopeArray.getJSONObject(i).has("Isotopic Composition")) {
                                composition.setText(isotopeArray.getJSONObject(i).optString("Isotopic Composition"));
                            }
                            composition.setLayoutParams(thirdColumnLayoutParams);

                            isotopeRow.addView(atomicSymbolText);
                            isotopeRow.addView(relativeMass);
                            isotopeRow.addView(composition);
                            isotopeTable.addView(isotopeRow);
                        }
                    }
                }
                else{
                    //Display only those isotopes with composition elements.
                    for (int i = 0; i < isotopeArray.length(); i++) {
                        if (isotopeArray.getJSONObject(i).has("Isotopic Composition")) {
                            isotopeRow = new TableRow(getActivity());
                            isotopeRow.setLayoutParams(tableParams);

                            atomicSymbolText = new TextView(getActivity());
                            relativeMass = new TextView(getActivity());
                            composition = new TextView(getActivity());

                            atomicSymbolText.setText("");
                            if(!symbolInTable){
                                atomicSymbolText.setText(atomicSymbol);
                                symbolInTable = true;
                            }
                            atomicSymbolText.setLayoutParams(firstColumnLayoutParams);

                            relativeMass.setText(isotopeArray.getJSONObject(i).optString("Relative Atomic Mass"));
                            relativeMass.setLayoutParams(secondColumnLayoutParams);

                            composition.setText(isotopeArray.getJSONObject(i).optString("Isotopic Composition"));
                            composition.setLayoutParams(thirdColumnLayoutParams);

                            isotopeRow.addView(atomicSymbolText);
                            isotopeRow.addView(relativeMass);
                            isotopeRow.addView(composition);
                            isotopeTable.addView(isotopeRow);
                        }
                    }
                }

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
            results.put("Isotope Data", testArray.getJSONObject(100));
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
