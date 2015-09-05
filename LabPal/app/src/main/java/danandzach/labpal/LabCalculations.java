package danandzach.labpal;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
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
import android.widget.TextView;

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


    /*
    Use the below values for computations
     */

    public static String operator;
    public static boolean operate;
    public static float result;
    public static float value;



    public static LabCalculations newInstance() {
        LabCalculations fragment = new LabCalculations();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public LabCalculations() {
        // Required empty public constructor
    }


    public void modifyDisplay(Button buttonPressed){
        //A class to modify the display depending on which button is pressed. -D
        String currDisplayString = currModifyText.getText().toString();
        int insertLocation = currModifyText.getSelectionStart();
        String preSelect = "";
        String postSelect = "";
        preSelect = currDisplayString.substring(0, insertLocation);
        if(insertLocation < currDisplayString.length()){
            postSelect = currDisplayString.substring(insertLocation);
        }
        switch(buttonPressed.getId()){
            case R.id.b_del:

                if(currModifyText.getText().length() > 2)
                    currModifyText.setText(preSelect.substring(0,preSelect.length() - 1));
                if((currModifyText.getText().length() == 2 || currModifyText.getText().length() == 1) && currModifyText.getText().toString().charAt(0) != '('){
                    currModifyText.setText(preSelect.substring(0, preSelect.length() - 1));
                }
                break;

            case R.id.b_AC:
                currModifyText.setText("");
                break;

            case R.id.b0:
                currModifyText.setText(preSelect + "0" + postSelect);
                break;
            case R.id.b1:
                currModifyText.setText(preSelect + "1" + postSelect);
                break;

            case R.id.b2:
                currModifyText.setText(preSelect + "2" + postSelect);
                break;

            case R.id.b3:
                currModifyText.setText(preSelect + "3" + postSelect);
                break;

            case R.id.b4:
                currModifyText.setText(preSelect + "4" + postSelect);
                break;

            case R.id.b5:
                currModifyText.setText(preSelect + "5" + postSelect);
                break;

            case R.id.b6:
                currModifyText.setText(preSelect + "6" + postSelect);
                break;

            case R.id.b7:
                currModifyText.setText(preSelect + "7" + postSelect);
                break;

            case R.id.b8:
                currModifyText.setText(preSelect + "8" + postSelect);
                break;

            case R.id.b9:
                currModifyText.setText(preSelect + "9" + postSelect);
                break;

            case R.id.b_dot:
                if(!currModifyText.getText().toString().contains("."))
                    currModifyText.setText(preSelect + "." + postSelect);
                break;
        }
        if(currModifyText.getId() == R.id.display_err){
            Selection.setSelection(currModifyText.getText(), currModifyText.getText().length() - 1);
        }
        else {
            Selection.setSelection(currModifyText.getText(), currModifyText.getText().length());
        }
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
                        modifyDisplay((Button)v);
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
                        if(currModifyText.isFocused())
                            modifyDisplay((Button) v);
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
                        modifyDisplay((Button) v);
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
                        modifyDisplay((Button) v);
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
                        modifyDisplay((Button) v);
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
                        modifyDisplay((Button) v);
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
                        modifyDisplay((Button) v);
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
                        modifyDisplay((Button) v);
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
                        modifyDisplay((Button) v);
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
                        modifyDisplay((Button) v);
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
                        modifyDisplay((Button) v);
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
                        modifyDisplay((Button) v);
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
                        modifyDisplay((Button) v);
                        return true;
                    case MotionEvent.ACTION_UP:
                        v.setBackgroundColor(Color.parseColor("#19440c"));
                        return true;

                }
                return false;
            }
        });



        final AutoCompleteTextView constants_search = (AutoCompleteTextView)v.findViewById(R.id.calculator_search);
        final TextView units = (TextView)v.findViewById(R.id.display_units);
        final EditText display_err = (EditText)v.findViewById(R.id.display_err);

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
                        for (int i = 0; i < Data.getConstants_data().getJSONArray(Data.getConstants_array_name()).length(); i++) {
                            if (Data.getConstants_data().getJSONArray(Data.getConstants_array_name()).
                                    getJSONObject(i).optString("Quantity ").equalsIgnoreCase(constants_search.getText().toString())) {
                                data_constant = Data.getConstants_data().getJSONArray(Data.getConstants_array_name()).getJSONObject(i);
                                currModifyText.setText("" + data_constant.optString("Value").replaceAll("\\s+" + "", ""));
                                currModifyText.requestFocus();
                                units.setText(data_constant.optString("Unit"));
                                display_err.setText(data_constant.optString("Uncertainty").replaceAll("\\s+" + "", ""));

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
        final EditText displayValue = (EditText) v.findViewById(R.id.display_value);

        currModifyText = displayValue;

        final EditText displayError = (EditText) v.findViewById(R.id.display_err);

        //Allow persistent () in the error display. -D
        displayError.setText("()");
        Selection.setSelection(displayError.getText(), 1);

        displayError.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String errString = s.toString();
                if(s.toString().charAt(0) != '('){
                    errString = "(" + s.toString();
                    displayError.setText(errString);
                    Selection.setSelection(displayError.getText(), errString.length() - 1);
                }
                if(!s.toString().endsWith(")")){
                    errString += ")";
                    displayError.setText(errString);
                    Selection.setSelection(displayError.getText(), errString.length() - 1);
                }
            }
        });



        //Prevents keyboard from coming up. -D
        displayValue.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocus();
                if(displayValue.getText().length() > 0){
                    Selection.setSelection(displayValue.getText(),displayValue.getText().length());
                }
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

    public float compute(float result, float value, String button){
        if(button == "+")
            return result + value;
        else if (button == "-")
            return result - value;
        else if (button == "*")
            return result * value;
        else if (button == "/")
            return result / value;

        return 0.0f;
    }

}