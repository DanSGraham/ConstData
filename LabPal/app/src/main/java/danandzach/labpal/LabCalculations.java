package danandzach.labpal;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LabCalculations.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LabCalculations#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LabCalculations extends Fragment {

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LabCalculations.
     */
    
    public EditText currModifyText;
    private static JSONObject data_constant;

    public static LabCalculations newInstance() {
        LabCalculations fragment = new LabCalculations();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public LabCalculations() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_lab_calculations, container, false);

        //Set up the buttons
        Button ac = (Button)v.findViewById(R.id.b_AC);
        Button power = (Button)v.findViewById(R.id.b_10x);
        Button b_div = (Button)v.findViewById(R.id.b_div);
        Button del = (Button)v.findViewById(R.id.b_del);
        Button b7 = (Button)v.findViewById(R.id.b7);
        Button b8 = (Button)v.findViewById(R.id.b8);
        Button b9 = (Button)v.findViewById(R.id.b9);
        Button b_star = (Button)v.findViewById(R.id.b_star);
        Button b4 = (Button)v.findViewById(R.id.b4);
        Button b5 = (Button)v.findViewById(R.id.b5);
        Button b6 = (Button)v.findViewById(R.id.b6);
        Button b_minus = (Button)v.findViewById(R.id.b_minus);
        Button b1 = (Button)v.findViewById(R.id.b1);
        Button b2 = (Button)v.findViewById(R.id.b2);
        Button b3 = (Button)v.findViewById(R.id.b3);
        Button b_plus = (Button)v.findViewById(R.id.b_plus);
        Button b0 = (Button)v.findViewById(R.id.b0);
        Button b_dot = (Button)v.findViewById(R.id.b_dot);
        Button b_err = (Button)v.findViewById(R.id.b_err);
        Button b_equals = (Button)v.findViewById(R.id.b_equals);

        //on click listeners
        ac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }

        });

        power.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        b_div.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        b_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        b_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        b_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        b_err.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        b_dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        b_equals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        b8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        b9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        b0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        /*
        This is for all the on touch events... Alot i know..
         */

        ac.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        v.setBackgroundColor(Color.GRAY);
                        return true;
                    case MotionEvent.ACTION_UP:
                        v.setBackgroundColor(Color.parseColor("#19440c"));
                        return true;

                }
                return false;
            }
        });

        power.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        v.setBackgroundColor(Color.GRAY);
                        return true;
                    case MotionEvent.ACTION_UP:
                        v.setBackgroundColor(Color.parseColor("#19440c"));
                        return true;

                }
                return false;
            }
        });

        b_div.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.setBackgroundColor(Color.GRAY);
                        return true;
                    case MotionEvent.ACTION_UP:
                        v.setBackgroundColor(Color.parseColor("#19440c"));
                        return true;

                }
                return false;
            }
        });

        del.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.setBackgroundColor(Color.GRAY);
                        return true;
                    case MotionEvent.ACTION_UP:
                        v.setBackgroundColor(Color.parseColor("#19440c"));
                        return true;

                }
                return false;
            }
        });

        b_star.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.setBackgroundColor(Color.GRAY);
                        return true;
                    case MotionEvent.ACTION_UP:
                        v.setBackgroundColor(Color.parseColor("#19440c"));
                        return true;

                }
                return false;
            }
        });

        b_minus.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.setBackgroundColor(Color.GRAY);
                        return true;
                    case MotionEvent.ACTION_UP:
                        v.setBackgroundColor(Color.parseColor("#19440c"));
                        return true;

                }
                return false;
            }
        });

        b_plus.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.setBackgroundColor(Color.GRAY);
                        return true;
                    case MotionEvent.ACTION_UP:
                        v.setBackgroundColor(Color.parseColor("#19440c"));
                        return true;

                }
                return false;
            }
        });

        b_err.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        v.setBackgroundColor(Color.GRAY);
                        return true;
                    case MotionEvent.ACTION_UP:
                        v.setBackgroundColor(Color.parseColor("#19440c"));
                        return true;

                }
                return false;
            }
        });

        b_dot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.setBackgroundColor(Color.GRAY);
                        return true;
                    case MotionEvent.ACTION_UP:
                        v.setBackgroundColor(Color.parseColor("#19440c"));
                        return true;

                }
                return false;
            }
        });

        b_equals.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.setBackgroundColor(Color.GRAY);
                        return true;
                    case MotionEvent.ACTION_UP:
                        v.setBackgroundColor(Color.parseColor("#19440c"));
                        return true;

                }
                return false;
            }
        });

        b0.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.setBackgroundColor(Color.GRAY);
                        return true;
                    case MotionEvent.ACTION_UP:
                        v.setBackgroundColor(Color.parseColor("#19440c"));
                        return true;

                }
                return false;
            }
        });

        b1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.setBackgroundColor(Color.GRAY);
                        return true;
                    case MotionEvent.ACTION_UP:
                        v.setBackgroundColor(Color.parseColor("#19440c"));
                        return true;

                }
                return false;
            }
        });

        b2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.setBackgroundColor(Color.GRAY);
                        return true;
                    case MotionEvent.ACTION_UP:
                        v.setBackgroundColor(Color.parseColor("#19440c"));
                        return true;

                }
                return false;
            }
        });

        b3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.setBackgroundColor(Color.GRAY);
                        return true;
                    case MotionEvent.ACTION_UP:
                        v.setBackgroundColor(Color.parseColor("#19440c"));
                        return true;

                }
                return false;
            }
        });

        b4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.setBackgroundColor(Color.GRAY);
                        return true;
                    case MotionEvent.ACTION_UP:
                        v.setBackgroundColor(Color.parseColor("#19440c"));
                        return true;

                }
                return false;
            }
        });

        b5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.setBackgroundColor(Color.GRAY);
                        return true;
                    case MotionEvent.ACTION_UP:
                        v.setBackgroundColor(Color.parseColor("#19440c"));
                        return true;

                }
                return false;
            }
        });

        b6.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.setBackgroundColor(Color.GRAY);
                        return true;
                    case MotionEvent.ACTION_UP:
                        v.setBackgroundColor(Color.parseColor("#19440c"));
                        return true;

                }
                return false;
            }
        });

        b7.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.setBackgroundColor(Color.GRAY);
                        return true;
                    case MotionEvent.ACTION_UP:
                        v.setBackgroundColor(Color.parseColor("#19440c"));
                        return true;

                }
                return false;
            }
        });

        b8.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.setBackgroundColor(Color.GRAY);
                        return true;
                    case MotionEvent.ACTION_UP:
                        v.setBackgroundColor(Color.parseColor("#19440c"));
                        return true;

                }
                return false;
            }
        });

        b9.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.setBackgroundColor(Color.GRAY);
                        return true;
                    case MotionEvent.ACTION_UP:
                        v.setBackgroundColor(Color.parseColor("#19440c"));
                        return true;

                }
                return false;
            }
        });



        final AutoCompleteTextView constants_search = (AutoCompleteTextView)v.findViewById(R.id.calculator_search);

        /*
        //Hide the keybaord when loses focus -D
        constants_search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    InputMethodManager imm = ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE));
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });
        */

        constants_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constants_search.setText("");
            }
        });
        try {
            String [] adapter_list = new String[Data.getConstants_data().getJSONArray(Data.getConstants_array_name()).length()];
            for(int i = 0; i < Data.getConstants_data().getJSONArray(Data.getConstants_array_name()).length(); i++){
                adapter_list[i] = Data.getConstants_data().getJSONArray(Data.getConstants_array_name()).getJSONObject(i).optString("Quantity ");
            }

            final ArrayAdapter<String> auto_complete = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, adapter_list);
            constants_search.setAdapter(auto_complete);

            constants_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        for(int i = 0; i < Data.getConstants_data().getJSONArray(Data.getConstants_array_name()).length(); i++){
                        if(Data.getConstants_data().getJSONArray(Data.getConstants_array_name()).
                                getJSONObject(i).optString("Quantity ").equalsIgnoreCase(constants_search.getText().toString())){
                            data_constant = Data.getConstants_data().getJSONArray(Data.getConstants_array_name()).getJSONObject(i);

                        }

                        }
                        InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        in.hideSoftInputFromWindow(getActivity().getWindow().getCurrentFocus().getWindowToken(), 0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


                });
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //Setup Display -D
        EditText displayValue = (EditText) v.findViewById(R.id.display_value);
        currModifyText = displayValue;

        EditText displayError = (EditText) v.findViewById(R.id.display_err);

        //Prevents keyboard from coming up. -D
        displayValue.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocus();
                return true;
            }
        });

        displayValue.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    currModifyText = (EditText) v;
                }
            }
        });

        displayError.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocus();
                return true;
            }
        });

        displayError.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    currModifyText = (EditText) v;
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
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Lab Calculator");
    }

}