package danandzach.labpal;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

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
    private static ArrayList<JSONObject> chosen_molecule;


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
            currEntries.add(new Entry(0f, i));
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

    public void generateEntriesTest(){
        resetCurrLine();
        addGaussianToCurrEntries(1167 * 2, STD_DEV, 6.49);
        addGaussianToCurrEntries(1249 * 2, STD_DEV, 9.94);
        addGaussianToCurrEntries(1500 * 2, STD_DEV, 11.15);
        addGaussianToCurrEntries(1746 * 2, STD_DEV, 73.99);
        addGaussianToCurrEntries(2782 * 2, STD_DEV, 75.5);
        addGaussianToCurrEntries(2843 * 2, STD_DEV, 87.6);
    }





    /*public LineDataSet formatLineData(int inputValues){
        //This will take the JSON object and format it into a dataset.
        return 0;
    }*/

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
        chosen_molecule = new ArrayList<>();
        currEntries = new ArrayList<Entry>();

        /*
        Zach

        All this is necessary to map the CAS number to the molecule name. It is slightly slow now.
        We can consider moving this process off of the UI thread once we get it functioning how we want.
         */

        final AutoCompleteTextView search_field = (AutoCompleteTextView)v.findViewById(R.id.ir_search_field);

        final ArrayList<String> casno_mapping = new ArrayList<>();
        try {
            for(int i = 0; i < Data.getNames_data().getJSONArray(Data.getNames_array_name()).length(); i++){
                String temp_casno = Data.getNames_data().getJSONArray(Data.getNames_array_name()).getJSONObject(i).optString("casno");
                boolean has_intensity = false;

                    for (int q = 0; q < Data.getCcc_data().getJSONArray(Data.getCcc_array_name()).length(); q++) {
                        if (Data.getCcc_data().getJSONArray(Data.getCcc_array_name()).getJSONObject(q).optString("casno").equalsIgnoreCase(temp_casno)) {
                            if (Data.getCcc_data().getJSONArray(Data.getCcc_array_name()).getJSONObject(q).optString("Intensity") == null) {
                                break;
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
                            chosen_molecule.add(Data.getCcc_data().getJSONArray(Data.getCcc_array_name()).getJSONObject(i));
                            Log.v("CHOSEN", chosen_molecule.get(i).optString("Intensity"));
                        }
                        else{
                            if(temp_count == 1){
                                break;
                            }else
                                continue;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        /*
        This is the end of the non-sense of the mapping of CAS number to molecule name.
        Below sets up the chart.
         */

        LineChart display_chart = (LineChart) v.findViewById(R.id.ir_chart);

        XAxis xAxisObject = display_chart.getXAxis();
        YAxis yAxisObjectLeft = display_chart.getAxisLeft();
        YAxis yAxisObjectRight = display_chart.getAxisRight();
        Legend legendObject = display_chart.getLegend();


        xAxisObject.setPosition(XAxis.XAxisPosition.BOTTOM);
        yAxisObjectLeft.setSpaceBottom(0f);
        yAxisObjectRight.setSpaceBottom(0f);
        yAxisObjectRight.setDrawLabels(false);
        legendObject.setEnabled(false);


        generateEntriesTest();


        LineDataSet dataSet = new LineDataSet(currEntries, "JUST A TEST");
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        dataSet.setColor(Color.parseColor("#19440c"));

        ArrayList<String> xVals = getGraphXAxis();


        LineData data = new LineData(xVals, dataSet);
        display_chart.setData(data);


        display_chart.setDescription("");
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
