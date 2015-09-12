package danandzach.labpal;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.Entry;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

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
