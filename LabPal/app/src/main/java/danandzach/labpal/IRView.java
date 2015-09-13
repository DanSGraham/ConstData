package danandzach.labpal;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.Entry;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link IRView.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link IRView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IRView extends Fragment {

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment IRView.
     */

    public static final int GRAPH_MAX_X = 4000;
    public static final int GRAPH_MIN_X = 0;
    public static final double DELTA_X = 0.5;
    public static final double STD_DEV = 55;

    public ArrayList<Entry> currEntries;
    private HashMap<String, ArrayList<JSONObject>> chosen_molecules;
    public HashMap<String, HashMap<Integer, Double>> chosen_molecules_chart_data;


    public static void hideSoftKeyboard(Activity activity){
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }


    public ArrayList<String> getGraphXAxis(){
        ArrayList<String> xAxis = new ArrayList<String>();
        double scaledX = GRAPH_MAX_X / DELTA_X;
        for(int i = GRAPH_MIN_X; i <= scaledX; i++){
            xAxis.add(i, String.valueOf(i * DELTA_X));

        }
        return xAxis;
    }

    public void resetCurrLine(){
        currEntries.clear();
        double scaledX = GRAPH_MAX_X / DELTA_X;
        for(int i = GRAPH_MIN_X; i <= scaledX; i++){
            currEntries.add(new Entry(.05f, i));
        }
    }



    public void addGaussianToCurrEntries(int mean, double stdDev, double intenseVal){
        //Mean and stdDev must be in units of index not actual units. To get units of index must
        //divide the current units by deltaX and floor the value so it is an integer.

        double gaussYVal = 0;
        double currEntryVal = 0;
        for(Entry dataPoint: currEntries){
            gaussYVal = intenseVal * ((1 / (stdDev * Math.sqrt(2.0 * Math.PI))) * Math.exp(-((Math.pow((dataPoint.getXIndex() - mean), 2)) / (2 * Math.pow(stdDev, 2)))));
            currEntryVal = dataPoint.getVal();
            dataPoint.setVal((float) (currEntryVal + gaussYVal));
        }
    }

    public void updateMoleculeList(){
        //Converts from frequency values to index values here. -D

        chosen_molecules_chart_data = new HashMap<>();
        String molecule_name;
        HashMap<Integer, Double> freqAndIntense;
        for(HashMap.Entry<String, ArrayList<JSONObject>> entry : chosen_molecules.entrySet()){
            molecule_name = entry.getKey();
            chosen_molecules_chart_data.put(molecule_name, new HashMap<Integer, Double>());
            for(JSONObject freq : entry.getValue()){
                freqAndIntense = chosen_molecules_chart_data.get(molecule_name);
                try{
                    int freqVal = convertFreqToIndex(Double.valueOf(freq.optString("Frequency")));
                    double intenseVal = Double.valueOf(freq.optString("Intensity"));
                    freqAndIntense.put(freqVal, intenseVal);
                }
                catch(NumberFormatException e){
                    Log.v("Can't convert Error", molecule_name);
                    Log.v("Error Val", freq.optString("Intensity"));
                    //e.printStackTrace();
                }
            }

        }
    }


    public void updateEntries(){
        //Updates the entries based on the chosen_molecules_chart_data -D

        for(HashMap.Entry<String, HashMap<Integer, Double>> entry : chosen_molecules_chart_data.entrySet()) {
            for (HashMap.Entry<Integer, Double> molecule_entry : entry.getValue().entrySet()) {
                addGaussianToCurrEntries(molecule_entry.getKey(), STD_DEV, molecule_entry.getValue());
            }
        }
    }

    public void updateDisplayNoMoleculeReset(){
        resetCurrLine();
        updateEntries();



        LineChart display_chart = ((LineChart) getActivity().findViewById(R.id.ir_chart));

        LineDataSet dataSet = new LineDataSet(currEntries, "IR Data");
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        dataSet.setColor(Color.parseColor("#19440c"));
        ArrayList<String> xVals = getGraphXAxis();
        LineData data = new LineData(xVals, dataSet);
        display_chart.setData(data);
        display_chart.invalidate();
    }

    public void updateDisplay(){
        resetCurrLine();
        updateMoleculeList();
        updateEntries();

        TableLayout molecule_display = (TableLayout) getActivity().findViewById(R.id.ir_molecule_selection_table);
        molecule_display.removeAllViews();
        for(HashMap.Entry<String, ArrayList<JSONObject>> molecule_name: chosen_molecules.entrySet()){
            addMoleculeToView(molecule_name.getKey());
        }

        LineChart display_chart = ((LineChart) getActivity().findViewById(R.id.ir_chart));

        LineDataSet dataSet = new LineDataSet(currEntries, "IR Data");
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        dataSet.setColor(Color.parseColor("#19440c"));
        ArrayList<String> xVals = getGraphXAxis();
        LineData data = new LineData(xVals, dataSet);
        display_chart.setData(data);
        display_chart.invalidate();

    }

    public int convertFreqToIndex(double freq){
        return Math.round( (float) Math.floor(freq / DELTA_X));
    }

    public void addMoleculeToView(final String molecule_name){
        //When the user selects a molecule to view, this will format the lower display to show wihch
        //molecules and what relative inteisities they will be present on the graph -D


        int SP_SIZE = 16;

        TableLayout.LayoutParams rowParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        TableRow rowToAdd = new TableRow(getActivity());

        FrameLayout buttonLayout = new FrameLayout(getActivity());
        TableRow.LayoutParams buttonParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        buttonParams.setMargins(5,0,0,5);
        buttonParams.gravity = Gravity.CENTER_VERTICAL;
        buttonLayout.setLayoutParams(buttonParams);
        buttonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosen_molecules.remove(molecule_name);
                updateDisplay();
            }
        });


        TextView removeMoleculeButton = new TextView(getActivity());
        removeMoleculeButton.setPadding(5, 0, 5, 0);
        removeMoleculeButton.setText("x");
        removeMoleculeButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, SP_SIZE + 2);
        removeMoleculeButton.setTextColor(Color.BLACK);
        buttonLayout.addView(removeMoleculeButton);


        TextView moleculeName = new TextView(getActivity());
        moleculeName.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
        moleculeName.setText(molecule_name);
        moleculeName.setTextSize(TypedValue.COMPLEX_UNIT_SP, SP_SIZE);


        EditText relativeIntensity = new EditText(getActivity());
        relativeIntensity.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        relativeIntensity.setText("100");
        relativeIntensity.setTextSize(TypedValue.COMPLEX_UNIT_SP, SP_SIZE);

        relativeIntensity.setInputType(InputType.TYPE_CLASS_NUMBER);

        relativeIntensity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    changeIntensity(molecule_name, Double.valueOf(s.toString()));
                }
            }
        });


        relativeIntensity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    try {
                        hideSoftKeyboard(getActivity());
                    }
                    catch(NullPointerException e){

                    }
                }
            }
        });


        TextView percentSign = new TextView(getActivity());
        percentSign.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        percentSign.setText("%");
        percentSign.setTextSize(TypedValue.COMPLEX_UNIT_SP, SP_SIZE);

        rowToAdd.addView(buttonLayout);
        rowToAdd.addView(moleculeName);
        rowToAdd.addView(relativeIntensity);
        rowToAdd.addView(percentSign);

        TableLayout display_table = (TableLayout) getActivity().findViewById(R.id.ir_molecule_selection_table);
        display_table.addView(rowToAdd);

    }


    public void changeIntensity(String molecule_name, double percentIntensity){
        if(percentIntensity >= 0 && percentIntensity <= 100.0){
            chosen_molecules_chart_data.remove(molecule_name);
            ArrayList<JSONObject> moleculeJSON = chosen_molecules.get(molecule_name);
            chosen_molecules_chart_data.put(molecule_name, new HashMap<Integer, Double>());
            HashMap<Integer, Double> freqAndIntense;

            for(JSONObject freq : moleculeJSON){
                freqAndIntense = chosen_molecules_chart_data.get(molecule_name);
                try{
                    int freqVal = convertFreqToIndex(Double.valueOf(freq.optString("Frequency")));
                    double intenseVal = Double.valueOf(freq.optString("Intensity")) * (percentIntensity / 100);
                    freqAndIntense.put(freqVal, intenseVal);
                }
                catch(NumberFormatException e){
                    e.printStackTrace();
                }
            }
            updateDisplayNoMoleculeReset();
        }

    }


    public static IRView newInstance() {
        IRView fragment = new IRView();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }



    public IRView() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_irview, container, false);
        chosen_molecules = new HashMap<String, ArrayList<JSONObject>>();
        currEntries = new ArrayList<Entry>();

        /*
        Zach

        All this is necessary to map the CAS number to the molecule name. It is slightly slow now.
        We can consider moving this process off of the UI thread once we get it functioning how we want.
         */

        final AutoCompleteTextView search_field = (AutoCompleteTextView)v.findViewById(R.id.ir_search_field);

        search_field.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    try {
                        hideSoftKeyboard(getActivity());
                    }
                    catch(NullPointerException e){

                    }
                }
            }
        });

        final ArrayList<String> casno_mapping = new ArrayList<>();
        try {
            for(int i = 0; i < Data.getNames_data().getJSONArray(Data.getNames_array_name()).length(); i++){
                String temp_casno = Data.getNames_data().getJSONArray(Data.getNames_array_name()).getJSONObject(i).optString("casno");
                boolean has_intensity = false;

                    for (int q = 0; q < Data.getCcc_data().getJSONArray(Data.getCcc_array_name()).length(); q++) {
                        if (Data.getCcc_data().getJSONArray(Data.getCcc_array_name()).getJSONObject(q).optString("casno").equalsIgnoreCase(temp_casno)) {
                            if (Data.getCcc_data().getJSONArray(Data.getCcc_array_name()).getJSONObject(q).optString("Intensity").equals("")) {
                                continue;
                            } else {
                                boolean has_name = false;
                                for (int p = 0; p < casno_mapping.size(); p++) {
                                    if (casno_mapping.get(p).equalsIgnoreCase(Data.getNames_data().getJSONArray(Data.getNames_array_name()).getJSONObject(q).optString("Name"))) {
                                        has_name = true;
                                    }
                                }
                                if (has_name == false) {
                                    has_intensity = true;
                                    break;
                                } else
                                    break;
                            }
                        }
                    }
                if(has_intensity == false)
                    continue;
                else
                    casno_mapping.add(Data.getNames_data().getJSONArray(Data.getNames_array_name()).getJSONObject(i).optString("Name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String[] adapter_list = new String[casno_mapping.size()];

        for(int i = 0; i < adapter_list.length; i++){
            adapter_list[i] = casno_mapping.get(i);
        }
        final ArrayAdapter<String> auto_complete = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, adapter_list);
        search_field.setAdapter(auto_complete);
        search_field.setThreshold(1);
        search_field.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String molecule = search_field.getText().toString();
                String casno = "";
                try {
                    for(int i = 0; i < Data.getNames_data().getJSONArray(Data.getNames_array_name()).length(); i++){
                        if(molecule.equalsIgnoreCase(Data.getNames_data().getJSONArray(Data.getNames_array_name()).getJSONObject(i)
                        .optString("Name"))){
                            casno = Data.getNames_data().getJSONArray(Data.getNames_array_name()).getJSONObject(i)
                                    .optString("casno");
                            break;
                        }
                    }
                    int temp_count = 0;
                    for(int i = 0; i < Data.getCcc_data().getJSONArray(Data.getCcc_array_name()).length(); i++){
                        if(casno.equalsIgnoreCase(Data.getCcc_data().getJSONArray(Data.getCcc_array_name()).getJSONObject(i)
                        .optString("casno"))){
                            temp_count = 1;
                            if (!(chosen_molecules.containsKey(molecule))){
                                chosen_molecules.put(molecule, new ArrayList<JSONObject>());
                            }
                            chosen_molecules.get(molecule).add(Data.getCcc_data().getJSONArray(Data.getCcc_array_name()).getJSONObject(i));

                        }
                        else{
                            if(temp_count == 1){
                                break;
                            }else
                                continue;
                        }
                    }

                    updateDisplay();
                    InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(getActivity().getWindow().getCurrentFocus().getWindowToken(), 0);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        /*
        This is the end of the non-sense of the mapping of CAS number to molecule name.
        Below sets up the chart.
         */

        final LineChart display_chart = (LineChart) v.findViewById(R.id.ir_chart);

        XAxis xAxisObject = display_chart.getXAxis();
        YAxis yAxisObjectLeft = display_chart.getAxisLeft();
        YAxis yAxisObjectRight = display_chart.getAxisRight();
        Legend legendObject = display_chart.getLegend();


        xAxisObject.setPosition(XAxis.XAxisPosition.BOTTOM);
        yAxisObjectLeft.setSpaceBottom(0f);
        yAxisObjectRight.setSpaceBottom(0f);
        yAxisObjectRight.setDrawLabels(false);
        legendObject.setEnabled(false);


        LineDataSet dataSet = new LineDataSet(currEntries, "IR Dataset");
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        dataSet.setColor(Color.parseColor("#19440c"));

        ArrayList<String> xVals = getGraphXAxis();


        LineData data = new LineData(xVals, dataSet);
        display_chart.setData(data);


        display_chart.setDescription("");

        //Setup invert axis buttons -D

        TextView invertYAxisButton = (TextView) v.findViewById(R.id.invert_y);
        invertYAxisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(display_chart.getAxisLeft().isInverted())){
                    display_chart.getAxisLeft().setInverted(true);
                    display_chart.getAxisRight().setInverted(true);

                    LineDataSet dataSet = new LineDataSet(currEntries, "IR Data");
                    dataSet.setDrawCircles(false);
                    dataSet.setDrawValues(false);
                    dataSet.setColor(Color.parseColor("#19440c"));
                    ArrayList<String> xVals = getGraphXAxis();
                    LineData data = new LineData(xVals, dataSet);
                    display_chart.setData(data);
                    display_chart.invalidate();
                }
                else{
                    display_chart.getAxisLeft().setInverted(false);
                    display_chart.getAxisRight().setInverted(false);

                    LineDataSet dataSet = new LineDataSet(currEntries, "IR Data");
                    dataSet.setDrawCircles(false);
                    dataSet.setDrawValues(false);
                    dataSet.setColor(Color.parseColor("#19440c"));
                    ArrayList<String> xVals = getGraphXAxis();
                    LineData data = new LineData(xVals, dataSet);
                    display_chart.setData(data);
                    display_chart.invalidate();
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

    @Override
    public void onResume(){
        super.onResume();
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("IR Viewer");
    }

}
