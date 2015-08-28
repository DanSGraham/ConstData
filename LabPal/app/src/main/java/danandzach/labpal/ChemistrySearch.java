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
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ActionMenuView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

import java.lang.reflect.Type;
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


    private static HashMap<String, JSONObject> search_results = new HashMap<String, JSONObject>();

    private final String ISOTOPE_DATABASE_NAME = "Atomic Weights and Isotopes";
    private final String IONIZATION_ENERGY_DATABASE_NAME = "Ground Levels and Ionization Energy";

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

        final int HEADER_TEXT_SIZE = 18;

        final int TITLE_ID = 1;
        final int UNDERLINE_ID = 2;
        final int STANDARD_WEIGHT_LABEL_ID = 3;
        final int STANDARD_WEIGHT_VALUE_ID = 4;
        final int COMMON_ISOTOPE_LABEL_ID = 5;
        final int COMMON_ISOTOPE_TABLE_ID = 6;
        final int CONTENT_CONTAINER_ID = 7;
        final int EXPAND_VIEW_BUTTON_ID = 8;


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
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, HEADER_TEXT_SIZE);
        title.setLayoutParams(titleParams);
        title.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TextView expandButton = (TextView) getActivity().findViewById(EXPAND_VIEW_BUTTON_ID);
                final int status = (Integer) expandButton.getTag();
                if (status == 1) {
                    RelativeLayout contentShow = (RelativeLayout) getActivity().findViewById(CONTENT_CONTAINER_ID);
                    contentShow.setVisibility(View.VISIBLE);
                    expandButton.setText("-");
                    expandButton.setTag(0);
                } else {
                    RelativeLayout contentHide = (RelativeLayout) getActivity().findViewById(CONTENT_CONTAINER_ID);
                    contentHide.setVisibility(View.GONE);
                    expandButton.setText("+");
                    expandButton.setTag(1);
                }
            }
        });

        title.setId(TITLE_ID);

        //Setup expand section button -D
        final TextView expandViewButton = new TextView(getActivity());
        expandViewButton.setText("+");
        expandViewButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, HEADER_TEXT_SIZE);
        expandViewButton.setTag(1);
        expandViewButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final int status = (Integer) v.getTag();
                if (status == 1) {
                    RelativeLayout contentShow = (RelativeLayout) getActivity().findViewById(CONTENT_CONTAINER_ID);
                    contentShow.setVisibility(View.VISIBLE);
                    expandViewButton.setText("-");
                    expandViewButton.setTag(0);
                } else {
                    RelativeLayout contentHide = (RelativeLayout) getActivity().findViewById(CONTENT_CONTAINER_ID);
                    contentHide.setVisibility(View.GONE);
                    expandViewButton.setText("+");
                    expandViewButton.setTag(1);
                }
            }
        });

        RelativeLayout.LayoutParams expandButtonParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        expandButtonParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        expandButtonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        expandViewButton.setLayoutParams(expandButtonParams);
        expandViewButton.setPadding(0, 0, 10, 0);
        expandViewButton.setId(EXPAND_VIEW_BUTTON_ID);



        //Underline -D
        View underline = new View(getActivity());
        RelativeLayout.LayoutParams underlineParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 5);
        underlineParams.addRule(RelativeLayout.BELOW, TITLE_ID);
        underline.setBackgroundColor(Color.GRAY);
        underline.setLayoutParams(underlineParams);
        underline.setId(UNDERLINE_ID);


        //Visibility section -D
        RelativeLayout contentContainer = new RelativeLayout(getActivity());
        RelativeLayout.LayoutParams contentLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        contentLayoutParams.addRule(RelativeLayout.BELOW, UNDERLINE_ID);
        contentContainer.setLayoutParams(contentLayoutParams);
        contentContainer.setVisibility(View.GONE);
        contentContainer.setId(CONTENT_CONTAINER_ID);

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
        stdWeightLabelLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
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
        resultsContainer.addView(expandViewButton);
        resultsContainer.addView(contentContainer);
        contentContainer.addView(stdWeightLabel);
        contentContainer.addView(stdWeightData);
        contentContainer.addView(isotopeTable);
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

    public RelativeLayout formatIonizationEnergyResults(String databaseName, JSONObject databaseContent){

        //Builds a layout for a result from the Atomic Mass and Isotopes Database -D

        final int HEADER_TEXT_SIZE = 18;

        final int TITLE_ID = 101;
        final int UNDERLINE_ID = 102;
        final int ION_ENERGY_LABEL_ID = 103;
        final int ION_ENERGY_VALUE_ID = 104;
        final int CONTENT_CONTAINER_ID = 107;
        final int EXPAND_VIEW_BUTTON_ID = 108;


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
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, HEADER_TEXT_SIZE);
        title.setLayoutParams(titleParams);
        title.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TextView expandButton = (TextView) getActivity().findViewById(EXPAND_VIEW_BUTTON_ID);
                final int status = (Integer) expandButton.getTag();
                if (status == 1) {
                    RelativeLayout contentShow = (RelativeLayout) getActivity().findViewById(CONTENT_CONTAINER_ID);
                    contentShow.setVisibility(View.VISIBLE);
                    expandButton.setText("-");
                    expandButton.setTag(0);
                } else {
                    RelativeLayout contentHide = (RelativeLayout) getActivity().findViewById(CONTENT_CONTAINER_ID);
                    contentHide.setVisibility(View.GONE);
                    expandButton.setText("+");
                    expandButton.setTag(1);
                }
            }
        });

        title.setId(TITLE_ID);

        //Setup expand section button -D
        final TextView expandViewButton = new TextView(getActivity());
        expandViewButton.setText("+");
        expandViewButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, HEADER_TEXT_SIZE);
        expandViewButton.setTag(1);
        expandViewButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final int status = (Integer) v.getTag();
                if (status == 1) {
                    RelativeLayout contentShow = (RelativeLayout) getActivity().findViewById(CONTENT_CONTAINER_ID);
                    contentShow.setVisibility(View.VISIBLE);
                    expandViewButton.setText("-");
                    expandViewButton.setTag(0);
                } else {
                    RelativeLayout contentHide = (RelativeLayout) getActivity().findViewById(CONTENT_CONTAINER_ID);
                    contentHide.setVisibility(View.GONE);
                    expandViewButton.setText("+");
                    expandViewButton.setTag(1);
                }
            }
        });

        RelativeLayout.LayoutParams expandButtonParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        expandButtonParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        expandButtonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        expandViewButton.setLayoutParams(expandButtonParams);
        expandViewButton.setPadding(0, 0, 10, 0);
        expandViewButton.setId(EXPAND_VIEW_BUTTON_ID);



        //Underline -D
        View underline = new View(getActivity());
        RelativeLayout.LayoutParams underlineParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 5);
        underlineParams.addRule(RelativeLayout.BELOW, TITLE_ID);
        underline.setBackgroundColor(Color.GRAY);
        underline.setLayoutParams(underlineParams);
        underline.setId(UNDERLINE_ID);


        //Visibility section -D
        RelativeLayout contentContainer = new RelativeLayout(getActivity());
        RelativeLayout.LayoutParams contentLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        contentLayoutParams.addRule(RelativeLayout.BELOW, UNDERLINE_ID);
        contentContainer.setLayoutParams(contentLayoutParams);
        contentContainer.setVisibility(View.GONE);
        contentContainer.setId(CONTENT_CONTAINER_ID);

        //Add ionizationTable to content container. -D
        contentContainer.addView(generateIonizationTable(databaseContent));

        //Add all components to the layout -D
        resultsContainer.addView(title);
        resultsContainer.addView(underline);
        resultsContainer.addView(expandViewButton);
        resultsContainer.addView(contentContainer);

        return resultsContainer;
    }

    public TableLayout generateIonizationTable(JSONObject dbContent){

        final float COLUMN_ONE_WEIGHT = 0.5f;
        final float COLUMN_TWO_WEIGHT = 0.5f;
        final int NUM_DATA_TYPES = 4;

        TableRow.LayoutParams firstColumnLayoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, COLUMN_ONE_WEIGHT);
        TableRow.LayoutParams secondColumnLayoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, COLUMN_TWO_WEIGHT);

        TableLayout ionizationTable = new TableLayout(getActivity());
        TableLayout.LayoutParams tableParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        ionizationTable.setLayoutParams(tableParams);


        //Add ground shell row. -D
        TextView groundShellLabel = new TextView(getActivity());
        groundShellLabel.setText("Ground Shells:");
        groundShellLabel.setLayoutParams(firstColumnLayoutParams);

        TextView groundShellData = new TextView(getActivity());
        groundShellData.setText(dbContent.optString("Ground Shells"));
        groundShellData.setLayoutParams(secondColumnLayoutParams);

        TableRow groundShellRow = new TableRow(getActivity());
        groundShellRow.setLayoutParams(tableParams);
        groundShellRow.addView(groundShellLabel);
        groundShellRow.addView(groundShellData);
        ionizationTable.addView(groundShellRow);

        //Add ground quantum level row. -D
        TextView groundLevelLabel = new TextView(getActivity());
        groundLevelLabel.setText("Ground Level:");
        groundLevelLabel.setLayoutParams(firstColumnLayoutParams);

        TextView groundLevelData = new TextView(getActivity());
        groundLevelData.setText(dbContent.optString("Ground Level"));
        groundLevelData.setLayoutParams(secondColumnLayoutParams);

        TableRow groundLevelRow = new TableRow(getActivity());
        groundLevelRow.setLayoutParams(tableParams);
        groundLevelRow.addView(groundLevelLabel);
        groundLevelRow.addView(groundLevelData);
        ionizationTable.addView(groundLevelRow);

        //Add ionization energy row. -D
        TextView ionizationEnergyLabel = new TextView(getActivity());
        ionizationEnergyLabel.setText("Ionization Energy:");
        ionizationEnergyLabel.setLayoutParams(firstColumnLayoutParams);

        TextView ionizationEnergyData = new TextView(getActivity());
        ionizationEnergyData.setText(dbContent.optString("Ionization Energy (eV)") + " eV");
        ionizationEnergyData.setLayoutParams(secondColumnLayoutParams);

        TableRow ionizationEnergyRow = new TableRow(getActivity());
        ionizationEnergyRow.setLayoutParams(tableParams);
        ionizationEnergyRow.addView(ionizationEnergyLabel);
        ionizationEnergyRow.addView(ionizationEnergyData);
        ionizationTable.addView(ionizationEnergyRow);

        //Add references row. -D
        TextView refrencesLabel = new TextView(getActivity());
        refrencesLabel.setText("References:");
        refrencesLabel.setLayoutParams(firstColumnLayoutParams);


        return ionizationTable;
    }

    public HashMap<String, JSONObject> queryDatabases(String query){
        //This method will return the results of a search on all databases associated with the search.
        HashMap<String, JSONObject> results = new HashMap<String, JSONObject>();

        //The following is a dummy method just used for testing layout formatting. -D
        try{
            JSONArray testArray = (Data.getAtomic_mass_data()).getJSONArray(Data.getAtomic_mass_array_name());
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
        String titleText = ((AutoCompleteTextView) getActivity().findViewById(R.id.search_field)).getText().toString();
        queryTitle.setText(titleText);

        View horLine = new View(getActivity());
        horLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 5));
        horLine.setBackgroundColor(Color.DKGRAY);

        layoutToModify.addView(queryTitle);
        layoutToModify.addView(horLine);
        for (HashMap.Entry<String, JSONObject> db: searchResults.entrySet()){
            switch (db.getKey()){
                case ISOTOPE_DATABASE_NAME:
                    layoutToModify.addView(formatIsotopeResults(db.getKey(), db.getValue()));
                    break;

                case IONIZATION_ENERGY_DATABASE_NAME:
                    layoutToModify.addView(formatIonizationEnergyResults(db.getKey(), db.getValue()));
                    break;

                default:
                    break;
            }
        }


        return true;
    }

    HashMap<String, JSONObject> resultMap = new HashMap<String, JSONObject>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chemistry_search, container, false);
        final LinearLayout contentArea = (LinearLayout) v.findViewById(R.id.mol_search_content_area);
        final AutoCompleteTextView search_bar = (AutoCompleteTextView) v.findViewById(R.id.search_field);

        /*
        Zach

        This is all the code neccessary for Autocomplete to work. Right now autocomplete works only by molecule name.
        We can map the element symbol to the molecule name later if we have time, but this should suffice for now.
         */

        JSONArray jsonArray = Data.get_array(Data.getIonization_data(), Data.getIonization_array_name());
        String[] adapter_list = new String[jsonArray.length()];

        for(int i = 0; i < jsonArray.length(); i++){
            try {
                adapter_list[i] = jsonArray.getJSONObject(i).optString("Element Name");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        final ArrayAdapter<String> auto_complete = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, adapter_list);
        search_bar.setAdapter(auto_complete);
        search_bar.setThreshold(1);

        search_bar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                HashMap<String, JSONObject> query_results = new HashMap<String, JSONObject>();
                PeriodicTable pt = new PeriodicTable();
                String symbol = pt.getElementSymbol(search_bar.getText().toString());
                String element_name = search_bar.getText().toString();
                Log.v("Element Name: ", element_name);
                Log.v("SYMBOL: ", symbol);

                try {
                    for (int z = 0; z < Data.get_array(Data.getAtomic_mass_data(), Data.getAtomic_mass_array_name()).length(); z++) {
                        if (Data.getAtomic_mass_data().getJSONArray(Data.getAtomic_mass_array_name()).getJSONObject(z).optString("Atomic Symbol").equalsIgnoreCase(symbol)) {
                            query_results.put(ISOTOPE_DATABASE_NAME, Data.getAtomic_mass_data().getJSONArray(Data.getAtomic_mass_array_name()).getJSONObject(z));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    for (int i = 0; i < Data.getIonization_data().getJSONArray(Data.getIonization_array_name()).length(); i++) {
                        try {
                            if (Data.getIonization_data().getJSONArray(Data.getIonization_array_name())
                                    .getJSONObject(i).optString("Element Name").trim().equalsIgnoreCase(element_name.trim())) {
                                query_results.put(IONIZATION_ENERGY_DATABASE_NAME, Data.getIonization_data().getJSONArray(
                                                Data.getIonization_array_name()).getJSONObject(i)
                                );
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                set_search_results(query_results);
                if (!query_results.isEmpty()) {
                    modifyContent(query_results, contentArea);
                }

            }
        });

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

    public void set_search_results(HashMap<String, JSONObject> h){
        search_results = h;
    }

    /*
    Use this method to get the Hashmap you need. This is what happens:
        1.) The user selects an autocomplete item.
        2.) The JSONObject from the atomic mass database is saved with the key ISOTOPE_DATABASE_NAME
        3.) The JSONObject from the ionization database is saved with the key IONIZATION_ENERGY_DATABASE_NAME
        4.) Call this method to get these objects as a hashmap and display the data how you please.

        -Zach
     */
    public HashMap<String, JSONObject> get_search_results(){
        return search_results;
    }


}
