package danandzach.labpal;





import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.Entry;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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
    public double STD_DEV = 55;

    public ArrayList<Entry> currEntries;
    private HashMap<String, ArrayList<JSONObject>> chosen_molecules;
    public HashMap<String, HashMap<Integer, Double>> chosen_molecules_chart_data;
    public HashMap<String, Double> molecule_intensity;

    public ArrayList<Entry> currEntriesReversed;

    public boolean xAxisReversed;
    public boolean yAxisReversed;


    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }


    public ArrayList<String> getGraphXAxis() {
        ArrayList<String> xAxis = new ArrayList<String>();
        double scaledX = GRAPH_MAX_X / DELTA_X;
        for (int i = GRAPH_MIN_X; i <= scaledX; i++) {
            xAxis.add(i, String.valueOf(i * DELTA_X));

        }
        return xAxis;
    }

    public ArrayList<String> getGraphXAxisRev() {
        ArrayList<String> xAxis = new ArrayList<String>();
        double scaledX = GRAPH_MAX_X / DELTA_X;
        int scaledXInt = (int) Math.ceil(scaledX);
        for (int i = scaledXInt; i >= GRAPH_MIN_X; i--) {
            xAxis.add((scaledXInt - i), String.valueOf(i * DELTA_X));
        }
        return xAxis;
    }

    public void updateReverseEntries() {

        double scaledX = GRAPH_MAX_X / DELTA_X;
        int scaledXInt = (int) Math.floor(scaledX);
        for (Entry entry : currEntries) {
            Entry tempEntry = new Entry(entry.getVal(), scaledXInt - entry.getXIndex());
            currEntriesReversed.set(scaledXInt - entry.getXIndex(), tempEntry);
        }
    }

    public void resetCurrLine() {
        currEntries.clear();
        currEntriesReversed.clear();
        double scaledX = GRAPH_MAX_X / DELTA_X;
        for (int i = GRAPH_MIN_X; i <= scaledX; i++) {
            currEntries.add(new Entry(.05f, i));
            currEntriesReversed.add(new Entry(0.5f, i));
        }
    }


    public void addGaussianToCurrEntries(String moleculeName, int mean, double stdDev, double intenseVal) {
        //Mean and stdDev must be in units of index not actual units. To get units of index must
        //divide the current units by deltaX and floor the value so it is an integer.

        double gaussYVal = 0;
        double currEntryVal = 0;
        for (Entry dataPoint : currEntries) {
            gaussYVal = intenseVal * ((1 / (stdDev * Math.sqrt(2.0 * Math.PI))) * Math.exp(-((Math.pow((dataPoint.getXIndex() - mean), 2)) / (2 * Math.pow(stdDev, 2))))) * (molecule_intensity.get(moleculeName) / 100.0);
            currEntryVal = dataPoint.getVal();
            dataPoint.setVal((float) (currEntryVal + gaussYVal));
        }
    }

    public void updateMoleculeList() {
        //Converts from frequency values to index values here. -D

        chosen_molecules_chart_data = new HashMap<>();
        String molecule_name;
        HashMap<Integer, Double> freqAndIntense;
        for (HashMap.Entry<String, ArrayList<JSONObject>> entry : chosen_molecules.entrySet()) {
            molecule_name = entry.getKey();
            chosen_molecules_chart_data.put(molecule_name, new HashMap<Integer, Double>());
            for (JSONObject freq : entry.getValue()) {
                freqAndIntense = chosen_molecules_chart_data.get(molecule_name);
                try {
                    int freqVal = convertFreqToIndex(Double.valueOf(freq.optString("Frequency")));
                    double intenseVal = Double.valueOf(freq.optString("Intensity"));
                    freqAndIntense.put(freqVal, intenseVal);
                } catch (NumberFormatException e) {
                    Log.v("Can't convert Error", molecule_name);
                    Log.v("Error Val", freq.optString("Intensity"));
                    //e.printStackTrace();
                }
            }

        }
    }


    public void updateEntries() {
        //Updates the entries based on the chosen_molecules_chart_data -D

        for (HashMap.Entry<String, HashMap<Integer, Double>> entry : chosen_molecules_chart_data.entrySet()) {
            for (HashMap.Entry<Integer, Double> molecule_entry : entry.getValue().entrySet()) {
                addGaussianToCurrEntries(entry.getKey(), molecule_entry.getKey(), STD_DEV, molecule_entry.getValue());
            }
        }
        updateReverseEntries();
    }


    public void updateDisplayIntense(){
        resetCurrLine();
        updateMoleculeList();
        updateEntries();


        LineChart display_chart = ((LineChart) getActivity().findViewById(R.id.ir_chart));

        LineDataSet dataSet;
        ArrayList<String> xVals;

        if (xAxisReversed) {
            dataSet = new LineDataSet(currEntriesReversed, "IR Data");
            xVals = getGraphXAxisRev();
        } else {
            dataSet = new LineDataSet(currEntries, "IR Data");
            xVals = getGraphXAxis();
        }

        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        dataSet.setColor(Color.parseColor("#19440c"));
        LineData data = new LineData(xVals, dataSet);

        if (yAxisReversed) {
            display_chart.getAxisRight().setInverted(true);
            display_chart.getAxisLeft().setInverted(true);
        } else {
            display_chart.getAxisRight().setInverted(false);
            display_chart.getAxisLeft().setInverted(false);
        }

        display_chart.setData(data);
        display_chart.invalidate();

    }


    public void updateDisplay() {
        resetCurrLine();
        updateMoleculeList();
        updateEntries();

        TableLayout molecule_display = (TableLayout) getActivity().findViewById(R.id.ir_molecule_selection_table);
        molecule_display.removeAllViews();
        for (HashMap.Entry<String, ArrayList<JSONObject>> molecule_name : chosen_molecules.entrySet()) {
            addMoleculeToView(molecule_name.getKey());
        }

        LineChart display_chart = ((LineChart) getActivity().findViewById(R.id.ir_chart));

        LineDataSet dataSet;
        ArrayList<String> xVals;

        if (xAxisReversed) {
            dataSet = new LineDataSet(currEntriesReversed, "IR Data");
            xVals = getGraphXAxisRev();
        } else {
            dataSet = new LineDataSet(currEntries, "IR Data");
            xVals = getGraphXAxis();
        }

        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        dataSet.setColor(Color.parseColor("#19440c"));
        LineData data = new LineData(xVals, dataSet);

        if (yAxisReversed) {
            display_chart.getAxisRight().setInverted(true);
            display_chart.getAxisLeft().setInverted(true);
        } else {
            display_chart.getAxisRight().setInverted(false);
            display_chart.getAxisLeft().setInverted(false);
        }

        display_chart.setData(data);
        display_chart.invalidate();

    }

    public void updateDisplay(View v) {
        resetCurrLine();
        updateMoleculeList();
        updateEntries();

        TableLayout molecule_display = (TableLayout) v.findViewById(R.id.ir_molecule_selection_table);
        molecule_display.removeAllViews();
        for (HashMap.Entry<String, ArrayList<JSONObject>> molecule_name : chosen_molecules.entrySet()) {
            addMoleculeToView(molecule_name.getKey());
        }

        LineChart display_chart = ((LineChart) v.findViewById(R.id.ir_chart));

        LineDataSet dataSet;
        ArrayList<String> xVals;

        if (xAxisReversed) {
            dataSet = new LineDataSet(currEntriesReversed, "IR Data");
            xVals = getGraphXAxisRev();
        } else {
            dataSet = new LineDataSet(currEntries, "IR Data");
            xVals = getGraphXAxis();
        }

        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        dataSet.setColor(Color.parseColor("#19440c"));
        LineData data = new LineData(xVals, dataSet);

        if (yAxisReversed) {
            display_chart.getAxisRight().setInverted(true);
            display_chart.getAxisLeft().setInverted(true);
        } else {
            display_chart.getAxisRight().setInverted(false);
            display_chart.getAxisLeft().setInverted(false);
        }

        display_chart.setData(data);
        display_chart.invalidate();

    }

    public int convertFreqToIndex(double freq) {
        return Math.round((float) Math.floor(freq / DELTA_X));
    }

    public void addMoleculeToView(final String molecule_name) {
        //When the user selects a molecule to view, this will format the lower display to show wihch
        //molecules and what relative inteisities they will be present on the graph -D


        int SP_SIZE = 16;

        TableLayout.LayoutParams rowParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        TableRow rowToAdd = new TableRow(getActivity());

        RelativeLayout buttonLayout = new RelativeLayout(getActivity());
        TableRow.LayoutParams buttonParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

        //Convert pixels to dp -D
        int dps = 7;
        final float scale = getActivity().getResources().getDisplayMetrics().density;
        int pixels = (int) (dps * scale + 0.5f);

        buttonLayout.setLayoutParams(buttonParams);
        buttonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosen_molecules.remove(molecule_name);
                molecule_intensity.remove(molecule_name);
                updateDisplay();
                if (chosen_molecules.isEmpty()) {
                    TextView ss = (TextView) getView().findViewById(R.id.signal_strength);
                    ss.setText("");
                }
            }
        });
        buttonLayout.setGravity(Gravity.CENTER_VERTICAL);

        TextView removeMoleculeButton = new TextView(getActivity());
        removeMoleculeButton.setPadding(0, pixels, pixels, 0);
        removeMoleculeButton.setText("x");
        removeMoleculeButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, SP_SIZE + 2);
        removeMoleculeButton.setTextColor(Color.BLACK);

        RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        textParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        removeMoleculeButton.setGravity(Gravity.CENTER_VERTICAL);
        buttonLayout.addView(removeMoleculeButton);


        TextView moleculeName = new TextView(getActivity());
        moleculeName.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
        moleculeName.setText(molecule_name);
        moleculeName.setTextSize(TypedValue.COMPLEX_UNIT_SP, SP_SIZE);


        final EditText relativeIntensity = new EditText(getActivity());
        relativeIntensity.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        relativeIntensity.setText(String.valueOf(molecule_intensity.get(molecule_name)));

        relativeIntensity.setTextSize(TypedValue.COMPLEX_UNIT_SP, SP_SIZE);

        relativeIntensity.setInputType(InputType.TYPE_CLASS_NUMBER);



        relativeIntensity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    try {
                        InputMethodManager inputMethodManager = (InputMethodManager)  getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                    catch(NullPointerException e){
                    }
                }
            }
        });

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
                    try{
                        double intensityVal = Double.valueOf(s.toString());
                        if(intensityVal >= 0 && intensityVal <= 100){
                            molecule_intensity.put(molecule_name, intensityVal);
                        }
                    }
                    catch(TypeNotPresentException e){
                        e.printStackTrace();
                    }
                    updateDisplayIntense();
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
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_irview, container, false);

        //Initialize class variables here. -D
        chosen_molecules = new HashMap<String, ArrayList<JSONObject>>();
        currEntries = new ArrayList<Entry>();
        currEntriesReversed = new ArrayList<Entry>();
        molecule_intensity = new HashMap<String, Double>();


        xAxisReversed = false;
        yAxisReversed = false;




        /*
        Zach

        All this is necessary to map the CAS number to the molecule name. It is slightly slow now.
        We can consider moving this process off of the UI thread once we get it functioning how we want.
         */

        final AutoCompleteTextView search_field = (AutoCompleteTextView) v.findViewById(R.id.ir_search_field);

        search_field.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    try {
                        InputMethodManager inputMethodManager = (InputMethodManager)  getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                    catch(NullPointerException e){
                    }
                }
            }
        });

        final ArrayList<String> casno_mapping = new ArrayList<>();
        try {
            for (int i = 0; i < Data.getCcc_data().getJSONArray(Data.getCcc_array_name()).length(); i++) {
                if (!casno_mapping.contains(Data.getCcc_data().getJSONArray(Data.getCcc_array_name()).getJSONObject(i).getString("Name")))
                    casno_mapping.add(Data.getCcc_data().getJSONArray(Data.getCcc_array_name()).getJSONObject(i).getString("Name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String[] adapter_list = new String[casno_mapping.size()];

        for (int i = 0; i < adapter_list.length; i++) {
            adapter_list[i] = casno_mapping.get(i);
        }
        final ArrayAdapter<String> auto_complete = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, adapter_list);
        search_field.setAdapter(auto_complete);
        search_field.setThreshold(1);
        search_field.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_field.setText("");
            }
        });
        search_field.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    for (int i = 0; i < Data.getCcc_data().getJSONArray(Data.getCcc_array_name()).length(); i++) {
                        if (search_field.getText().toString().equalsIgnoreCase(Data.getCcc_data().getJSONArray(
                                Data.getCcc_array_name()).getJSONObject(i).optString("Name"))
                                ) {
                            if (!chosen_molecules.containsKey(search_field.getText().toString())) {
                                chosen_molecules.put(search_field.getText().toString(), new ArrayList<JSONObject>());
                                molecule_intensity.put(search_field.getText().toString(), 100.0);
                            }
                            chosen_molecules.get(search_field.getText().toString()).add(Data.getCcc_data().getJSONArray(
                                    Data.getCcc_array_name()).getJSONObject(i));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                search_field.setText("");
                updateDisplay();
                if(!chosen_molecules.isEmpty()){
                    TextView ss = (TextView) getView().findViewById(R.id.signal_strength);
                    ss.setText("Signal Strength");
                }
                hideSoftKeyboard(getActivity());
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
                if (yAxisReversed) {
                    yAxisReversed = false;
                } else {
                    yAxisReversed = true;
                }
                updateDisplay();
            }
        });

        TextView invertXAxisButton = (TextView) v.findViewById(R.id.invert_x);
        invertXAxisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (xAxisReversed) {
                    xAxisReversed = false;
                } else {
                    xAxisReversed = true;
                }
                updateDisplay();
            }
        });

        //Setup seek bar -D

        SeekBar noise_bar = (SeekBar) v.findViewById(R.id.noise_bar);
        noise_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progressVal, boolean fromUser) {
                progress = progressVal + 35;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                STD_DEV = (double) progress;
                updateDisplay();
            }
        });
        updateDisplay(v);
        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        Data.xAxisReversed = xAxisReversed;
        Data.yAxisReversed = yAxisReversed;
        Data.chosen_molecules = chosen_molecules;
        Data.intensity_percentages = molecule_intensity;
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
    public void onResume() {
        super.onResume();
        TextView ss = (TextView)getView().findViewById(R.id.signal_strength);
        if(Data.chosen_molecules == null){
            ss.setText("");
        }
        if(Data.chosen_molecules!=null) {
            chosen_molecules = Data.chosen_molecules;
            xAxisReversed = Data.xAxisReversed;
            yAxisReversed = Data.yAxisReversed;
            molecule_intensity = Data.intensity_percentages;

            /*if(Data.intensity_percentages != null){
                for(String s:Data.intensity_percentages.keySet()){
                    changeIntensity(s, Data.intensity_percentages.get(s));
                }
            }*/

            updateDisplay();
        }
        if(((MainActivity) getActivity()).getSupportActionBar().getTitle() != "IR Viewer") {
            ((MainActivity) getActivity()).getSupportActionBar().setTitle("IR Viewer");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_home, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

         switch (item.getItemId()) {
            case R.id.action_info:
                new AlertDialog.Builder(getActivity())
                        .setTitle(Html.fromHtml("<font color='#19440c'>Special Thanks</font>"))
                        .setMessage(R.string.MPChartShoutout)
                        .setPositiveButton("Website", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String url = "https://github.com/PhilJay/MPAndroidChart";
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                startActivity(i);
                            }
                        })
                        .show();
                return true;
        }

        return super.onOptionsItemSelected(item);

    }
}
