package danandzach.labpal;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.net.Uri;
import android.os.Bundle;

import  android.support.v7.app.ActionBar;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ActionMenuView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.awt.font.TextAttribute;
import java.lang.reflect.Type;
import java.util.Calendar;
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

//TODO Immediate: TEST ON DIFFERENT DISPLAYS AND WITH ALL ELEMENTS. -D
//TODO Add show more to isotope and allow citation popup to be selectable. -D
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

    private static String saved_search_term;



    private final int SECTION_TITLE_TOP_MARGIN_DIP = 10;
    private final int SECTION_TITLE_BOTTOM_MARGIN_DIP = 0;
    private final int CONTENT_LINE_MARGIN_DIP = 2;


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

        final SharedPreferences prefs = getActivity().getSharedPreferences("expansions",Context.MODE_PRIVATE);
        final SharedPreferences.Editor prefs_edit = prefs.edit();


        Calendar citCalendar = Calendar.getInstance();
        String monthAccessed = "";
        switch(citCalendar.get(Calendar.MONTH)){
            case 0:
                monthAccessed = "January";
                break;
            case 1:
                monthAccessed = "February";
                break;
            case 2:
                monthAccessed = "March";
                break;
            case 3:
                monthAccessed = "April";
                break;
            case 4:
                monthAccessed = "May";
                break;
            case 5:
                monthAccessed = "June";
                break;
            case 6:
                monthAccessed = "July";
                break;
            case 7:
                monthAccessed = "August";
                break;
            case 8:
                monthAccessed = "September";
                break;
            case 9:
                monthAccessed = "October";
                break;
            case 10:
                monthAccessed = "November";
                break;
            case 11:
                monthAccessed = "December";
                break;
            default:
                break;
        }
        Spanned ISOTOPE_DATABASE_CITATION = Html.fromHtml("J. S. Coursey, D. J. Schwab, J. J. Tsai, and R. A. Dragoset (2015). <i>Atomic Weights and Isotopic Compositions Database</i>, [Online]. Available: http://nist.gov/pml/data/comp.cfm [" + String.valueOf(citCalendar.get(Calendar.YEAR)) +
                ", " + monthAccessed + " " + String.valueOf(citCalendar.get(Calendar.DATE)) + "]. National Institute of Standards and Technology, Gaithersburg, MD.");

        //Set Margin value -D
        Resources r = getActivity().getResources();
        int topMarginPx = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                SECTION_TITLE_TOP_MARGIN_DIP,
                r.getDisplayMetrics()
        );

        int bottomMarginPx = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                SECTION_TITLE_BOTTOM_MARGIN_DIP,
                r.getDisplayMetrics()
        );

        int contentMarginPx = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                CONTENT_LINE_MARGIN_DIP,
                r.getDisplayMetrics()
        );


        //Setup data layout. -D
        RelativeLayout resultsContainer = new RelativeLayout(getActivity());
        RelativeLayout.LayoutParams resultsLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        resultsLayoutParams.setMargins(0, topMarginPx, 0, bottomMarginPx);
        resultsContainer.setLayoutParams(resultsLayoutParams);
        resultsContainer.setPadding(0, topMarginPx, 0, bottomMarginPx);


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
                    prefs_edit.putBoolean(ISOTOPE_DATABASE_NAME, true);
                    prefs_edit.commit();
                } else {
                    RelativeLayout contentHide = (RelativeLayout) getActivity().findViewById(CONTENT_CONTAINER_ID);
                    contentHide.setVisibility(View.GONE);
                    expandButton.setText("+");
                    expandButton.setTag(1);
                    prefs_edit.putBoolean(ISOTOPE_DATABASE_NAME, false);
                    prefs_edit.commit();
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
                    prefs_edit.putBoolean(ISOTOPE_DATABASE_NAME, true);
                    prefs_edit.commit();
                } else {
                    RelativeLayout contentHide = (RelativeLayout) getActivity().findViewById(CONTENT_CONTAINER_ID);
                    contentHide.setVisibility(View.GONE);
                    expandViewButton.setText("+");
                    expandViewButton.setTag(1);
                    prefs_edit.putBoolean(ISOTOPE_DATABASE_NAME, false);
                    prefs_edit.commit();
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
        contentLayoutParams.setMargins(0, contentMarginPx, 0, contentMarginPx);
        contentContainer.setLayoutParams(contentLayoutParams);
        contentContainer.setPadding(0, contentMarginPx, 0, contentMarginPx);
        if(prefs.getBoolean(ISOTOPE_DATABASE_NAME, false) == true){
            contentContainer.setVisibility(View.VISIBLE);
            expandViewButton.setText("-");
        }else{
            contentContainer.setVisibility(View.GONE);
            expandViewButton.setText("+");
        }

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
            stdWeightDataStr += " u";
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

        RelativeLayout isotopeTable = generateIsotopeTable(databaseContent);
        RelativeLayout.LayoutParams isotopeTableLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        isotopeTableLayoutParams.setMargins(0, contentMarginPx, 0, contentMarginPx);
        isotopeTableLayoutParams.addRule(RelativeLayout.BELOW, STANDARD_WEIGHT_LABEL_ID);
        isotopeTable.setLayoutParams(isotopeTableLayoutParams);
        isotopeTable.setPadding(0, contentMarginPx, 0, contentMarginPx);
        isotopeTable.setId(COMMON_ISOTOPE_TABLE_ID);


        //Format the Citation data -D
        TextView citationLabel = new TextView(getActivity());
        citationLabel.setText("Citation (NIST SRD 144):");

        RelativeLayout.LayoutParams citationLabelLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        citationLabelLayoutParams.addRule(RelativeLayout.BELOW, COMMON_ISOTOPE_TABLE_ID);
        citationLabelLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        citationLabel.setLayoutParams(citationLabelLayoutParams);


        //To make the button frame work had to use a framelayout. -D
        FrameLayout buttonContainer = new FrameLayout(getActivity());
        RelativeLayout.LayoutParams citationButtonLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        citationButtonLayoutParams.addRule(RelativeLayout.BELOW, COMMON_ISOTOPE_TABLE_ID);
        citationButtonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        citationButtonLayoutParams.setMargins(0, 0, 10, 0);
        buttonContainer.setLayoutParams(citationButtonLayoutParams);


        TextView citationButton = new TextView(getActivity());
        citationButton.setText("Show Citation");

        //Text border -D
        citationButton.setPadding(15, 5, 15, 5);
        ShapeDrawable border = new ShapeDrawable();
        border.setShape(new RectShape());
        border.getPaint().setColor(citationButton.getCurrentTextColor());
        border.getPaint().setStrokeWidth(5f);
        border.getPaint().setStyle(Paint.Style.STROKE);
        citationButton.setBackgroundDrawable(border);
        buttonContainer.addView(citationButton);


        //Citation popup -D
        final PopupWindow citationPopUp = new PopupWindow(getActivity());
        FrameLayout citationLayout = new FrameLayout(getActivity());
        FrameLayout.LayoutParams citationPopUpLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        citationPopUp.setFocusable(true);
        citationLayout.setLayoutParams(citationPopUpLayoutParams);
        citationLayout.setBackgroundColor(Color.WHITE);
        TextView citationText = new TextView(getActivity());
        citationText.setText(ISOTOPE_DATABASE_CITATION);
        citationLayout.addView(citationText);
        citationPopUp.setContentView(citationLayout);
        citationPopUp.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        citationPopUp.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        citationText.setTextIsSelectable(true);
        citationText.setFocusable(true);
        citationText.setFocusableInTouchMode(true);

        citationPopUp.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ((FrameLayout) getActivity().findViewById(R.id.mainscreen)).getForeground().setAlpha(0);
            }
        });

        buttonContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                citationPopUp.setBackgroundDrawable(new ColorDrawable());
                citationPopUp.showAtLocation(getActivity().findViewById(R.id.mol_search_content_area), Gravity.CENTER, 0, 0);
                //citationPopUp.update();
                ((FrameLayout) getActivity().findViewById(R.id.mainscreen)).getForeground().setAlpha(220);

            }
        });



        //Add all components to the layout -D
        contentContainer.addView(stdWeightLabel);
        contentContainer.addView(stdWeightData);
        contentContainer.addView(isotopeTable);
        contentContainer.addView(citationLabel);
        contentContainer.addView(buttonContainer);

        resultsContainer.addView(title);
        resultsContainer.addView(underline);
        resultsContainer.addView(expandViewButton);
        resultsContainer.addView(contentContainer);

        return resultsContainer;
    }

    public RelativeLayout generateIsotopeTable(JSONObject databaseContent){
        //Returns the table with isotopes formatted.

        Resources r = getActivity().getResources();

        int contentMarginPx = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                CONTENT_LINE_MARGIN_DIP,
                r.getDisplayMetrics()
        );

        final int tableId = 301;
        final int exceptionId = 302;

        boolean hashtag = false;

        RelativeLayout fullLayout = new RelativeLayout(getActivity());
        RelativeLayout.LayoutParams fullLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        final float COLUMN_ONE_WEIGHT = 0.2f;
        final float COLUMN_TWO_WEIGHT = 0.4f;
        final float COLUMN_THREE_WEIGHT = 0.4f;
        TableLayout isotopeTable = new TableLayout(getActivity());
        isotopeTable.setId(tableId);

        RelativeLayout.LayoutParams tableParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        tableParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
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
        compositionLabel.setPaintFlags(compositionLabel.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        compositionLabel.setLayoutParams(secondColumnLayoutParams);

        TextView relativeMassLabel = new TextView(getActivity());
        TableRow.LayoutParams thirdColumnLayoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, COLUMN_THREE_WEIGHT);
        relativeMassLabel.setText("Relative Mass");
        relativeMassLabel.setPaintFlags(relativeMassLabel.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
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

                    String relativeMassString = isotopeArray.getJSONObject(i).optString("Relative Atomic Mass");
                    if(relativeMassString.indexOf('#') != -1){
                        hashtag = true;
                    }
                    relativeMass.setText(relativeMassString);
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

                            String relativeMassString = isotopeArray.getJSONObject(i).optString("Relative Atomic Mass");
                            if(relativeMassString.indexOf('#') != -1){
                                hashtag = true;
                            }
                            relativeMass.setText(relativeMassString);
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

                            String relativeMassString = isotopeArray.getJSONObject(i).optString("Relative Atomic Mass");
                            if(relativeMassString.indexOf('#') != -1){
                                hashtag = true;
                            }
                            relativeMass.setText(relativeMassString);
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


                            String relativeMassString = isotopeArray.getJSONObject(i).optString("Relative Atomic Mass");
                            if(relativeMassString.indexOf('#') != -1){
                                hashtag = true;
                            }
                            relativeMass.setText(relativeMassString);
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

        fullLayout.addView(isotopeTable);

        if(hashtag){
            TextView exception = new TextView(getActivity());
            RelativeLayout.LayoutParams exceptionParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            exceptionParams.addRule(RelativeLayout.BELOW, tableId);
            exceptionParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            exception.setPadding(0, contentMarginPx, 0, contentMarginPx);
            exception.setText("#Value derived partly from systematic trends");
            exception.setLayoutParams(exceptionParams);
            fullLayout.addView(exception);
        }

        return fullLayout;
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
        final int ION_TABLE_ID = 109;

        SharedPreferences prefs = getActivity().getSharedPreferences("expansions", Context.MODE_PRIVATE);
        final SharedPreferences.Editor prefs_edit = prefs.edit();

        Calendar citCalendar = Calendar.getInstance();
        String monthAccessed = "";
        String dayAccessed = "";
        String yearAccessed = "";
        switch(citCalendar.get(Calendar.MONTH)){
            case 0:
                monthAccessed = "January";
                break;
            case 1:
                monthAccessed = "February";
                break;
            case 2:
                monthAccessed = "March";
                break;
            case 3:
                monthAccessed = "April";
                break;
            case 4:
                monthAccessed = "May";
                break;
            case 5:
                monthAccessed = "June";
                break;
            case 6:
                monthAccessed = "July";
                break;
            case 7:
                monthAccessed = "August";
                break;
            case 8:
                monthAccessed = "September";
                break;
            case 9:
                monthAccessed = "October";
                break;
            case 10:
                monthAccessed = "November";
                break;
            case 11:
                monthAccessed = "December";
                break;
            default:
                break;
        }
        Spanned ION_DATABASE_CITATION = Html.fromHtml("Kramida, A., Ralchenko, Yu., Reader, J., and NIST ASD Team (2014). <i>NIST Atomic Spectra Database</i> (ver. 5.2), [Online]. Available: http://physics.nist.gov/asd [" + String.valueOf(citCalendar.get(Calendar.YEAR)) +
                ", " + monthAccessed + " " + String.valueOf(citCalendar.get(Calendar.DATE)) + "]. National Institute of Standards and Technology, Gaithersburg, MD.");

        Resources r = getActivity().getResources();
        int topMarginPx = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                SECTION_TITLE_TOP_MARGIN_DIP,
                r.getDisplayMetrics()
        );

        int bottomMarginPx = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                SECTION_TITLE_BOTTOM_MARGIN_DIP,
                r.getDisplayMetrics()
        );

        //Setup data layout. -D
        RelativeLayout resultsContainer = new RelativeLayout(getActivity());
        RelativeLayout.LayoutParams resultsLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        resultsLayoutParams.setMargins(0, topMarginPx, 0, bottomMarginPx);
        resultsContainer.setLayoutParams(resultsLayoutParams);
        resultsContainer.setPadding(0, topMarginPx, 0, bottomMarginPx);


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
                    prefs_edit.putBoolean(IONIZATION_ENERGY_DATABASE_NAME, true);
                    prefs_edit.commit();
                } else {
                    RelativeLayout contentHide = (RelativeLayout) getActivity().findViewById(CONTENT_CONTAINER_ID);
                    contentHide.setVisibility(View.GONE);
                    expandButton.setText("+");
                    expandButton.setTag(1);
                    prefs_edit.putBoolean(IONIZATION_ENERGY_DATABASE_NAME, false);
                    prefs_edit.commit();
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
                    prefs_edit.putBoolean(IONIZATION_ENERGY_DATABASE_NAME, true);
                    prefs_edit.commit();
                } else {
                    RelativeLayout contentHide = (RelativeLayout) getActivity().findViewById(CONTENT_CONTAINER_ID);
                    contentHide.setVisibility(View.GONE);
                    expandViewButton.setText("+");
                    expandViewButton.setTag(1);
                    prefs_edit.putBoolean(IONIZATION_ENERGY_DATABASE_NAME, false);
                    prefs_edit.commit();
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
        if(prefs.getBoolean(IONIZATION_ENERGY_DATABASE_NAME, false) == true){
            contentContainer.setVisibility(View.VISIBLE);
            expandViewButton.setText("-");
        }else{
            contentContainer.setVisibility(View.GONE);
            expandViewButton.setText("+");
        }
        contentContainer.setId(CONTENT_CONTAINER_ID);

        //Add ionizationTable to content container. -D
        RelativeLayout ionTable = generateIonizationTable(databaseContent);
        ionTable.setId(ION_TABLE_ID);
        RelativeLayout.LayoutParams ionTableParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        ionTableParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        ionTable.setLayoutParams(ionTableParams);
        contentContainer.addView(ionTable);


        //Format the Citation data -D
        TextView citationLabel = new TextView(getActivity());
        citationLabel.setText("Citation (NIST SRD 111):");

        RelativeLayout.LayoutParams citationLabelLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        citationLabelLayoutParams.addRule(RelativeLayout.BELOW, ION_TABLE_ID);
        citationLabelLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        citationLabel.setLayoutParams(citationLabelLayoutParams);

        contentContainer.addView(citationLabel);


        //To make the button frame work had to use a framelayout. -D
        FrameLayout buttonContainer = new FrameLayout(getActivity());
        RelativeLayout.LayoutParams citationButtonLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        citationButtonLayoutParams.addRule(RelativeLayout.BELOW, ION_TABLE_ID);
        citationButtonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        citationButtonLayoutParams.setMargins(0, 0, 10, 0);
        buttonContainer.setLayoutParams(citationButtonLayoutParams);


        TextView citationButton = new TextView(getActivity());
        citationButton.setText("Show Citation");

        //Text border -D
        citationButton.setPadding(15, 5, 15, 5);
        ShapeDrawable border = new ShapeDrawable();
        border.setShape(new RectShape());
        border.getPaint().setColor(citationButton.getCurrentTextColor());
        border.getPaint().setStrokeWidth(5f);
        border.getPaint().setStyle(Paint.Style.STROKE);
        citationButton.setBackgroundDrawable(border);
        buttonContainer.addView(citationButton);


        //Citation popup -D
        final PopupWindow citationPopUp = new PopupWindow(getActivity());
        FrameLayout citationLayout = new FrameLayout(getActivity());
        FrameLayout.LayoutParams citationPopUpLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        citationPopUp.setFocusable(true);
        citationLayout.setLayoutParams(citationPopUpLayoutParams);
        citationLayout.setBackgroundColor(Color.WHITE);
        TextView citationText = new TextView(getActivity());
        citationText.setText(ION_DATABASE_CITATION);
        citationLayout.addView(citationText);
        citationPopUp.setContentView(citationLayout);
        citationPopUp.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        citationPopUp.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        citationText.setTextIsSelectable(true);
        citationText.setFocusable(true);
        citationText.setFocusableInTouchMode(true);

        citationPopUp.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ((FrameLayout) getActivity().findViewById(R.id.mainscreen)).getForeground().setAlpha(0);
            }
        });

        buttonContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                citationPopUp.setBackgroundDrawable(new ColorDrawable());
                citationPopUp.showAtLocation(getActivity().findViewById(R.id.mol_search_content_area), Gravity.CENTER, 0, 0);
                //citationPopUp.update();
                ((FrameLayout) getActivity().findViewById(R.id.mainscreen)).getForeground().setAlpha(220);

            }
        });


        contentContainer.addView(buttonContainer);

        //Add all components to the layout -D
        resultsContainer.addView(title);
        resultsContainer.addView(underline);
        resultsContainer.addView(expandViewButton);
        resultsContainer.addView(contentContainer);

        return resultsContainer;
    }

    public RelativeLayout generateIonizationTable(JSONObject dbContent){

        final float COLUMN_ONE_WEIGHT = 0.5f;
        final float COLUMN_TWO_WEIGHT = 0.5f;


        final int TABLE_ID = 200;
        final int EXCEPTION_ID = 201;

        RelativeLayout.LayoutParams tableParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        tableParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        RelativeLayout.LayoutParams exceptionParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        exceptionParams.addRule(RelativeLayout.BELOW, TABLE_ID);
        exceptionParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        RelativeLayout fullLayout = new RelativeLayout(getActivity());



        TableRow.LayoutParams firstColumnLayoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, COLUMN_ONE_WEIGHT);

        //Set Margin value -D
        Resources r = getActivity().getResources();
        int marginPx = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                CONTENT_LINE_MARGIN_DIP,
                r.getDisplayMetrics()
        );
        firstColumnLayoutParams.setMargins(0, marginPx, 0, marginPx);
        TableRow.LayoutParams secondColumnLayoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, COLUMN_TWO_WEIGHT);
        secondColumnLayoutParams.setMargins(0, marginPx, 0, marginPx);

        TableLayout ionizationTable = new TableLayout(getActivity());
        TableRow.LayoutParams rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        ionizationTable.setLayoutParams(tableParams);


        //Add ground shell row. -D
        TextView groundShellLabel = new TextView(getActivity());
        groundShellLabel.setText("Ground Shells:");
        groundShellLabel.setPadding(0, marginPx, 0, marginPx);
        groundShellLabel.setLayoutParams(firstColumnLayoutParams);

        TextView groundShellData = new TextView(getActivity());
        groundShellData.setText(formatHTMLStyle(dbContent.optString("Ground Shells")));
        groundShellData.setPadding(0, marginPx, 0, marginPx);
        groundShellData.setLayoutParams(secondColumnLayoutParams);

        TableRow groundShellRow = new TableRow(getActivity());
        groundShellRow.setLayoutParams(tableParams);
        groundShellRow.addView(groundShellLabel);
        groundShellRow.addView(groundShellData);
        ionizationTable.addView(groundShellRow);

        //Add ground quantum level row. -D
        TextView groundLevelLabel = new TextView(getActivity());
        groundLevelLabel.setText("Ground Level:");
        groundLevelLabel.setPadding(0, marginPx, 0, marginPx);
        groundLevelLabel.setLayoutParams(firstColumnLayoutParams);

        TextView groundLevelData = new TextView(getActivity());
        groundLevelData.setText(formatHTMLStyle(dbContent.optString("Ground Level")));
        groundLevelData.setPadding(0, marginPx, 0, marginPx);
        groundLevelData.setLayoutParams(secondColumnLayoutParams);

        TableRow groundLevelRow = new TableRow(getActivity());
        groundLevelRow.setLayoutParams(tableParams);
        groundLevelRow.addView(groundLevelLabel);
        groundLevelRow.addView(groundLevelData);
        ionizationTable.addView(groundLevelRow);

        //Add ionization energy row. -D

        boolean theoreticalEnergy = false;
        boolean approxFromExperiment = false;
        TextView ionizationEnergyLabel = new TextView(getActivity());
        ionizationEnergyLabel.setText("Ionization Energy:");
        ionizationEnergyLabel.setPadding(0, marginPx, 0, marginPx);
        ionizationEnergyLabel.setLayoutParams(firstColumnLayoutParams);

        TextView ionizationEnergyData = new TextView(getActivity());
        String ionizationEnergyString = dbContent.optString("Ionization Energy (eV)");
        ionizationEnergyString = ionizationEnergyString.replace("_", "");
        if (ionizationEnergyString.charAt(0) == '('){
            theoreticalEnergy = true;
            ionizationEnergyString = ionizationEnergyString.substring(1,(ionizationEnergyString.length() - 1));
            ionizationEnergyString += " eV*";
        }
        else if (ionizationEnergyString.charAt(0) == '['){
            approxFromExperiment = true;
            ionizationEnergyString = ionizationEnergyString.substring(1,(ionizationEnergyString.length() - 1));
            ionizationEnergyString += " eV*";
        }
        else{
            ionizationEnergyString += " eV";
        }

        ionizationEnergyData.setText(ionizationEnergyString);
        ionizationEnergyData.setPadding(0, marginPx, 0, marginPx);
        ionizationEnergyData.setLayoutParams(secondColumnLayoutParams);

        TableRow ionizationEnergyRow = new TableRow(getActivity());
        ionizationEnergyRow.setLayoutParams(tableParams);
        ionizationEnergyRow.addView(ionizationEnergyLabel);
        ionizationEnergyRow.addView(ionizationEnergyData);
        ionizationTable.addView(ionizationEnergyRow);

        //Add references row. -D
        TextView refrencesLabel = new TextView(getActivity());
        refrencesLabel.setText("References:");
        refrencesLabel.setPadding(0, marginPx, 0, marginPx);
        refrencesLabel.setLayoutParams(firstColumnLayoutParams);

        TextView referencesData = new TextView(getActivity());
        String htmlString = "";
        try {
            JSONArray refStringArray = dbContent.getJSONArray("References");
            JSONArray refStringURLArray = dbContent.getJSONArray("ReferencesURL");


            for (int i = 0; i < refStringArray.length(); i++) {
                String urlString = refStringURLArray.getString(i);
                urlString = urlString.replace("<", "");
                urlString = urlString.replace(">", "");
                htmlString += "<a href=" + urlString + ">" + refStringArray.getString(i);

                htmlString += "</a>";

                if (i != (refStringArray.length() - 1)) {
                    htmlString += ", ";
                }
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        referencesData.setText(Html.fromHtml(htmlString));
        referencesData.setPadding(0, marginPx, 0, marginPx);
        referencesData.setLayoutParams(firstColumnLayoutParams);
        referencesData.setMovementMethod(LinkMovementMethod.getInstance());

        TableRow referencesRow = new TableRow(getActivity());
        referencesRow.setLayoutParams(tableParams);
        referencesRow.addView(refrencesLabel);
        referencesRow.addView(referencesData);
        ionizationTable.addView(referencesRow);
        ionizationTable.setId(TABLE_ID);

        fullLayout.addView(ionizationTable);

        if(theoreticalEnergy){

            TextView exceptText = new TextView(getActivity());
            exceptText.setText("*Theoretical value");
            exceptText.setLayoutParams(exceptionParams);
            fullLayout.addView(exceptText);
        }
        if(approxFromExperiment){
            TextView exceptText = new TextView(getActivity());
            exceptText.setText("*Energy determined by interpolation or extrapolation of experimental values or by semiemperical calculation");
            exceptText.setLayoutParams(exceptionParams);
            fullLayout.addView(exceptText);
        }

        return fullLayout;
    }

    public Spanned formatHTMLStyle(String stringToFormat){
        String htmlString = "";

        boolean openSuperScript = false;
        boolean openSubScript = false;
        boolean openBracket = false;

        for (int i = 0; i < stringToFormat.length(); i++){
            if(stringToFormat.charAt(i) == '^'){
                htmlString += "<sup>";
                openSuperScript = true;
            }
            else if (stringToFormat.charAt(i) == '{'){
                openBracket = true;
            }
            else if (stringToFormat.charAt(i) == '}' && openSuperScript){
                htmlString += "</sup>";
                openBracket = false;
                openSuperScript = false;
            }
            else if (stringToFormat.charAt(i) == '}' && openSubScript){
                htmlString += "</sub>";
                openBracket = false;
                openSubScript = false;
            }
            else if (stringToFormat.charAt(i) == '_'){
                htmlString += "<sub>";
                openSubScript = true;
            }
            else if(!openBracket && openSuperScript){
                htmlString += stringToFormat.charAt(i);
                htmlString += "</sup>";
                openSuperScript = false;
            }
            else if(openBracket && openSuperScript){
                htmlString += stringToFormat.charAt(i);
            }
            else if(!openBracket && openSubScript){
                htmlString += stringToFormat.charAt(i);
                htmlString += "</sub>";
                openSubScript = false;
            }
            else if(openBracket && openSubScript){
                htmlString += stringToFormat.charAt(i);
            }
            else{
                htmlString += stringToFormat.charAt(i);
            }
        }
        if (openSuperScript){
            htmlString += "</sup>";
            openSuperScript = false;
        }
        if(openSubScript){
            htmlString += "</sub>";
            openSubScript = false;
        }

        return Html.fromHtml(htmlString);
    }


    public boolean modifyContent(HashMap<String, JSONObject> searchResults, LinearLayout layoutToModify){
        //A method to modify a LinearLayout to display search results. Results must be
        //passed as key value pairs because the database associated with the content is not
        //in the JSONObject and the modifier needs the name of the database. -D
        layoutToModify.removeAllViews();

        TextView queryTitle = new TextView(getActivity());
        queryTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        queryTitle.setTextColor(Color.BLACK);
        String titleText = saved_search_term;

        queryTitle.setText(titleText);

        View horLine = new View(getActivity());
        horLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 5));
        horLine.setBackgroundColor(Color.DKGRAY);

        layoutToModify.addView(queryTitle);
        layoutToModify.addView(horLine);

        JSONObject objectToAdd;
        objectToAdd = searchResults.get(ISOTOPE_DATABASE_NAME);
        if (objectToAdd != null){
            layoutToModify.addView(formatIsotopeResults(ISOTOPE_DATABASE_NAME, objectToAdd));
        }

        objectToAdd = searchResults.get(IONIZATION_ENERGY_DATABASE_NAME);
        if (objectToAdd != null){
            layoutToModify.addView(formatIonizationEnergyResults(IONIZATION_ENERGY_DATABASE_NAME, objectToAdd));
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


        if(!get_search_results().isEmpty()){
            modifyContent(get_search_results(), contentArea);
        }

        /*
        Zach

        This is all the code neccessary for Autocomplete to work. Right now autocomplete works only by molecule name.
        We can map the element symbol to the molecule name later if we have time, but this should suffice for now.
         */

        //This is for when the static variables get popped from memory on long application resting periods.
        Data.initPeriodicTable();

        String[] adapter_list = new String[PeriodicTable.periodic_table_size];
        Log.v("SIZE: ", String.valueOf(PeriodicTable.periodic_table_size));

        int temp = 0;
        for(Element e: PeriodicTable.getPeriodicTableSet()){
            adapter_list[temp] = e.getElementName();
            temp++;
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
                saved_search_term = element_name;

                try {
                    for (int z = 0; z < Data.get_array(Data.getAtomic_mass_data(), Data.getAtomic_mass_array_name()).length(); z++) {
                        if (Data.getAtomic_mass_data().getJSONArray(Data.getAtomic_mass_array_name()).getJSONObject(z).optString("Atomic Symbol").equalsIgnoreCase(symbol)) {
                            query_results.put(ISOTOPE_DATABASE_NAME, Data.getAtomic_mass_data().getJSONArray(Data.getAtomic_mass_array_name()).getJSONObject(z));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                boolean found = false;
                try {
                    for (int i = 0; i < Data.getIonization_data().getJSONArray(Data.getIonization_array_name()).length(); i++) {
                        try {
                            if (Data.getIonization_data().getJSONArray(Data.getIonization_array_name())
                                    .getJSONObject(i).optString("Element Name").trim().equalsIgnoreCase(element_name.trim())) {
                                query_results.put(IONIZATION_ENERGY_DATABASE_NAME, Data.getIonization_data().getJSONArray(
                                                Data.getIonization_array_name()).getJSONObject(i)
                                );
                                found = true;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if(found == false){
                        query_results.put(IONIZATION_ENERGY_DATABASE_NAME, null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                set_search_results(query_results);
                if (!get_search_results().isEmpty()) {
                    modifyContent(get_search_results(), contentArea);
                }

            }
        });


        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ISOTOPE_DATABASE_NAME, Data.getAtomic_mass_data().toString());
        outState.putString(IONIZATION_ENERGY_DATABASE_NAME, Data.getIonization_data().toString());
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume(){
        super.onResume();
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Element Search");
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
