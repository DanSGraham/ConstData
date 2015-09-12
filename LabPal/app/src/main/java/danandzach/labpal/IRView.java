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
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.Entry;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


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


    public ArrayList<String> getGraphXAxis(){
        ArrayList<String> xAxis = new ArrayList<String>();
        double scaledX = GRAPH_MAX_X / DELTA_X;
        for(int i = GRAPH_MIN_X; i <= scaledX; i++){
            xAxis.add(i, String.valueOf(i * DELTA_X));
        }
        return xAxis;
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

    private static ArrayList<JSONObject> chosen_molecule;

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

        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(100f, 0));
        entries.add(new Entry(6.49f, 1167 * 2));
        entries.add(new Entry(9.94f, 1249 * 2));
        entries.add(new Entry(11.15f, 1500 * 2));
        entries.add(new Entry(73.99f, 1746 * 2));
        entries.add(new Entry(75.5f, 2782 * 2));
        entries.add(new Entry(87.6f, 2843 * 2));
        entries.add(new Entry(100f, 4000 * 2));


        LineDataSet dataSet = new LineDataSet(entries, "JUST A TEST");
        dataSet.setDrawCubic(true);
        dataSet.setColor(Color.parseColor("#19440c"));

        ArrayList<String> xVals = getGraphXAxis();


        LineData data = new LineData(xVals, dataSet);
        display_chart.setData(data);


        display_chart.setDescription("Test for IR Viewer");
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
