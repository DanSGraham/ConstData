package danandzach.labpal;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.Selection;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.TypedValue;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.DuplicateFormatFlagsException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LabCalculations.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LabCalculations#newInstance} factory method to
 * create an instance of this fragment.
 */

//ToDo: calculate error (for exponent only left), add icons, allow large enough area for units, handle edge cases example below.
    //Make sure doesnt crash (mag. constant). One way someone could break is by pasting into the edit texts. <- Fixed
public class LabCalculations extends Fragment {

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LabCalculations.
     */

    public EditText currModifyText;

    public TextWatcher displayErrorWatcher;
    private static JSONObject data_constant;


    /*
    Use the below values for computations
     */

    public static String operator;
    public static boolean operate;
    public BigDecimal result;
    public BigDecimal resultError;
    public BigDecimal[] resultArray = new BigDecimal[2];
    public BigDecimal value;



    public static LabCalculations newInstance() {
        LabCalculations fragment = new LabCalculations();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public LabCalculations() {
        // Required empty public constructor
    }

    public boolean properlyFormattedValue(String valueString){
        //Checks if the value in the input is valid. -D

        try{
            new BigDecimal(valueString);
            return true;
        }
        catch(NumberFormatException e){
            return false;
        }
    }

    public boolean setErrorDisplay(String toSet){
        //Set text on display_err was causing crashes so I had to add this method -D

        EditText display_err = (EditText)getView().findViewById(R.id.display_err);
        display_err.removeTextChangedListener(displayErrorWatcher);
        display_err.setText(getString(R.string.plus_minus_sign) + toSet.replace(getString(R.string.plus_minus_sign), ""));
        display_err.addTextChangedListener(displayErrorWatcher);
        return true;
    }

    public Spanned formatUnits(String stringToFormat){

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

        if(htmlString.length() <= 0 ){
            htmlString = " ";
        }
        return Html.fromHtml(htmlString);
    }

    public void modifyDisplay(Button buttonPressed){
        //A class to modify the display depending on which button is pressed. -D

        String currDisplayString = currModifyText.getText().toString();
        int insertLocation = currModifyText.getSelectionStart();

        boolean emptyValue = false;
        boolean emptyError = false;
        boolean emptyRecent = false;

        EditText display_err = (EditText)getView().findViewById(R.id.display_err);
        EditText main_display = (EditText)getView().findViewById(R.id.display_value);
        TextView units_display = (TextView)getView().findViewById(R.id.display_units);
        TextView recent_number = (TextView)getView().findViewById(R.id.recent_number);
        AutoCompleteTextView autocomplete = (AutoCompleteTextView)getView().findViewById(R.id.calculator_search);

        //Determines if this is the first calculation in a series. -D
        if(recent_number.getText().toString().length() <= 0){
            emptyRecent = true;
        }

        //Determines if the view is empty -D
        if(main_display.getText().toString().length() <= 0){
            emptyValue = true;
        }

        if(display_err.getText().toString().length() <= 1){
            emptyError = true;
        }

        //Gets view string -D
        String preSelect = "";
        String postSelect = "";
        preSelect = currDisplayString.substring(0, insertLocation);
        if(insertLocation < currDisplayString.length()){
            postSelect = currDisplayString.substring(insertLocation);
        }

        //Determine button actions -D
        switch(buttonPressed.getId()){
            case R.id.b_del:
                if(currModifyText.getId() == R.id.display_err && !emptyError){
                    setErrorDisplay(preSelect.substring(0, preSelect.length() - 1));
                }

                if (currModifyText.getId() == R.id.display_value && !emptyValue){
                    currModifyText.setText(preSelect.substring(0, preSelect.length() - 1));
                }
                break;

            case R.id.b_AC:
                    setErrorDisplay("");
                    main_display.setText("");
                    units_display.setText("");
                    autocomplete.setText("");
                    recent_number.setText("");
                    result = BigDecimal.valueOf(0.0);
                    value = BigDecimal.valueOf(0.0);
                    resultError = BigDecimal.valueOf(0.0);
                    operate = false;
                break;


            //Number pad reaction -D
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
                //Check that currModifyText doesn't already have a . -D
                if(!currModifyText.getText().toString().contains(".")) {
                    currModifyText.setText(preSelect + "." + postSelect);
                }
                break;


            //Operations -D
            //Operations still do not deal with error values.
            case R.id.power:
                if(emptyValue || !properlyFormattedValue(main_display.getText().toString()) || (!properlyFormattedValue(display_err.getText().toString().replace(getString(R.string.plus_minus_sign), "")) && !emptyError)) {
                    break;
                }
                if(emptyRecent || operator == "="){
                    resultArray[0] = new BigDecimal(main_display.getText().toString());

                    if(emptyError){
                        resultArray[1] = BigDecimal.valueOf(0.0);
                        resultError = BigDecimal.valueOf(0.0);
                        recent_number.setText(String.valueOf(resultArray[0]));
                    }
                    else{
                        resultArray[1] = new BigDecimal(display_err.getText().toString().replace(getString(R.string.plus_minus_sign), ""));
                        result = resultArray[0];
                        resultError = resultArray[1];
                        recent_number.setText(String.valueOf(resultArray[0]) + getString(R.string.plus_minus_sign) + String.valueOf(resultArray[1]));
                    }
                }else{
                    resultArray = compute(recent_number.getText().toString(),
                            main_display.getText().toString(),
                            display_err.getText().toString(),
                            operator);

                    result = resultArray[0];
                    resultError = resultArray[1];
                    recent_number.setText(String.valueOf(resultArray[0]) + getString(R.string.plus_minus_sign) + String.valueOf(resultArray[1]));
                }
                operator = "^";
                main_display.setText("");
                setErrorDisplay("");
                units_display.setText("");
                break;

            case R.id.b_div:
                if(emptyValue || !properlyFormattedValue(main_display.getText().toString()) || (!properlyFormattedValue(display_err.getText().toString().replace(getString(R.string.plus_minus_sign), "")) && !emptyError)) {
                    break;
                }
                if(emptyRecent || operator == "="){
                    resultArray[0] = new BigDecimal(main_display.getText().toString());

                    if(emptyError){
                        resultArray[1] = BigDecimal.valueOf(0.0);
                        resultError = BigDecimal.valueOf(0.0);
                        recent_number.setText(String.valueOf(resultArray[0]));
                    }
                    else{
                        resultArray[1] = new BigDecimal(display_err.getText().toString().replace(getString(R.string.plus_minus_sign), ""));
                        result = resultArray[0];
                        resultError = resultArray[1];
                        recent_number.setText(String.valueOf(resultArray[0]) + getString(R.string.plus_minus_sign) + String.valueOf(resultArray[1]));
                    }
                }else{
                    if(Integer.parseInt(main_display.getText().toString()) != 0){
                        resultArray = compute(recent_number.getText().toString(),
                                main_display.getText().toString(),
                                display_err.getText().toString(),
                                operator);

                        result = resultArray[0];
                        resultError = resultArray[1];
                        recent_number.setText(String.valueOf(resultArray[0]) + getString(R.string.plus_minus_sign) + String.valueOf(resultArray[1]));
                    }else{
                        recent_number.setText("");
                        main_display.setText("");
                        setErrorDisplay(getString(R.string.plus_minus_sign));
                        units_display.setText("DIV. BY 0 ERROR");
                        break;
                    }

                }
                operator = "/";
                main_display.setText("");
                setErrorDisplay("");
                units_display.setText("");
                break;

            case R.id.b_star:
                if(emptyValue || !properlyFormattedValue(main_display.getText().toString()) || (!properlyFormattedValue(display_err.getText().toString().replace(getString(R.string.plus_minus_sign), "")) && !emptyError)) {
                    break;
                }
                if(emptyRecent || operator == "="){
                    resultArray[0] = new BigDecimal(main_display.getText().toString());

                    if(emptyError){
                        resultArray[1] = BigDecimal.valueOf(0.0);
                        resultError = BigDecimal.valueOf(0.0);
                        recent_number.setText(String.valueOf(resultArray[0]));
                    }
                    else{
                        resultArray[1] = new BigDecimal(display_err.getText().toString().replace(getString(R.string.plus_minus_sign), ""));
                        result = resultArray[0];
                        resultError = resultArray[1];
                        recent_number.setText(String.valueOf(resultArray[0]) + getString(R.string.plus_minus_sign) + String.valueOf(resultArray[1]));
                    }
                }else{
                    resultArray = compute(recent_number.getText().toString(),
                            main_display.getText().toString(),
                            display_err.getText().toString(),
                            operator);

                    result = resultArray[0];
                    resultError = resultArray[1];
                    recent_number.setText(String.valueOf(resultArray[0]) + getString(R.string.plus_minus_sign) + String.valueOf(resultArray[1]));
                }
                operator = "*";
                main_display.setText("");
                setErrorDisplay("");
                units_display.setText("");
                break;

            case R.id.b_plus:
                if(emptyValue || !properlyFormattedValue(main_display.getText().toString()) || (!properlyFormattedValue(display_err.getText().toString().replace(getString(R.string.plus_minus_sign), "")) && !emptyError)) {
                    break;
                }
                if(emptyRecent || operator == "="){
                    resultArray[0] = new BigDecimal(main_display.getText().toString());

                    if(emptyError){
                        resultArray[1] = BigDecimal.valueOf(0.0);
                        resultError = BigDecimal.valueOf(0.0);
                        recent_number.setText(String.valueOf(resultArray[0]));
                    }
                    else{
                        resultArray[1] = new BigDecimal(display_err.getText().toString().replace(getString(R.string.plus_minus_sign), ""));
                        result = resultArray[0];
                        resultError = resultArray[1];
                        recent_number.setText(String.valueOf(resultArray[0]) + getString(R.string.plus_minus_sign) + String.valueOf(resultArray[1]));
                    }
                }else{
                    resultArray = compute(recent_number.getText().toString(),
                            main_display.getText().toString(),
                            display_err.getText().toString(),
                            operator);

                    result = resultArray[0];
                    resultError = resultArray[1];
                    recent_number.setText(String.valueOf(resultArray[0]) + getString(R.string.plus_minus_sign) + String.valueOf(resultArray[1]));
                }
                operator = "+";
                main_display.setText("");
                setErrorDisplay("");
                units_display.setText("");
                break;

            case R.id.b_minus:
                if(emptyValue || !properlyFormattedValue(main_display.getText().toString()) || (!properlyFormattedValue(display_err.getText().toString().replace(getString(R.string.plus_minus_sign), "")) && !emptyError)) {
                    break;
                }
                if(emptyRecent || operator == "="){
                    resultArray[0] = new BigDecimal(main_display.getText().toString());

                    if(emptyError){
                        resultArray[1] = BigDecimal.valueOf(0.0);
                        resultError = BigDecimal.valueOf(0.0);
                        recent_number.setText(String.valueOf(resultArray[0]));
                    }
                    else{
                        resultArray[1] = new BigDecimal(display_err.getText().toString().replace(getString(R.string.plus_minus_sign), ""));
                        result = resultArray[0];
                        resultError = resultArray[1];
                        recent_number.setText(String.valueOf(resultArray[0]) + getString(R.string.plus_minus_sign) + String.valueOf(resultArray[1]));
                    }
                }else{
                    resultArray = compute(recent_number.getText().toString(),
                            main_display.getText().toString(),
                            display_err.getText().toString(),
                            operator);

                    result = resultArray[0];
                    resultError = resultArray[1];
                    recent_number.setText(String.valueOf(resultArray[0]) + getString(R.string.plus_minus_sign) + String.valueOf(resultArray[1]));
                }
                operator = "-";
                main_display.setText("");
                setErrorDisplay("");
                units_display.setText("");
                break;

            case R.id.b_equals:

                if(emptyValue || !properlyFormattedValue(main_display.getText().toString()) || (!properlyFormattedValue(display_err.getText().toString().replace(getString(R.string.plus_minus_sign), "")) && !emptyError)) {
                    break;
                }

                if(operator.equalsIgnoreCase("/") && Integer.parseInt(main_display.getText().toString()) == 0){
                    main_display.setText("");
                    recent_number.setText("");
                    display_err.setText(getString(R.string.plus_minus_sign));
                    units_display.setText("DIV. BY 0 ERROR");
                    result = 0.0f;
                    value = 0.0f;
                    operate = false;
                    break;
                }

                if(emptyRecent || operator == "="){
                    resultArray[0] = new BigDecimal(main_display.getText().toString());
                    System.out.println(resultArray[0]);
                    result = new BigDecimal(main_display.getText().toString());
                    if(display_err.getText().toString().length() <= 1){
                        resultArray[1] = BigDecimal.valueOf(0.0);
                        resultError =  BigDecimal.valueOf(0.0);
                        recent_number.setText(String.valueOf(resultArray[0]));
                    }
                    else{
                        resultArray[1] = new BigDecimal(display_err.getText().toString().replace(getString(R.string.plus_minus_sign), ""));
                        resultError = resultArray[1];
                        recent_number.setText(String.valueOf(resultArray[0]) + getString(R.string.plus_minus_sign) + String.valueOf(resultArray[1]));
                    }

                }else{
                    resultArray = compute(recent_number.getText().toString(),
                            main_display.getText().toString(),
                            display_err.getText().toString(),
                            operator);
                    result = resultArray[0];
                    resultError = resultArray[1];
                    recent_number.setText(String.valueOf(resultArray[0]) + getString(R.string.plus_minus_sign) + String.valueOf(resultArray[1]));
                }
                operator = "=";
                main_display.setText(String.valueOf(result));
                if (!resultError.equals(0)){
                    setErrorDisplay(String.valueOf(resultError));
                }
                else{
                    setErrorDisplay("");
                }
                units_display.setText("");
                break;

            case R.id.b_neg:
                BigDecimal currDisplayVal;
                if(currModifyText.getId() == R.id.display_value){
                    if(properlyFormattedValue(main_display.getText().toString())){
                        currDisplayVal = new BigDecimal(main_display.getText().toString());
                        currDisplayVal = currDisplayVal.multiply(BigDecimal.valueOf(-1));
                        main_display.setText(String.valueOf(currDisplayVal));
                    }
                }
                break;
        }

        Selection.setSelection(currModifyText.getText(), currModifyText.getText().length());
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

        result = BigDecimal.valueOf(0.0);
        value =BigDecimal.valueOf(0.0);
        resultError = BigDecimal.valueOf(0.0);

        //Set up the buttons
        Button ac = (Button)v.findViewById(R.id.b_AC);
        Button power = (Button)v.findViewById(R.id.power);
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
        Button b_neg = (Button)v.findViewById(R.id.b_neg);
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
                        modifyDisplay((Button)v);
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
                        modifyDisplay((Button)v);
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
                        modifyDisplay((Button)v);
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
                        modifyDisplay((Button)v);
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
                        modifyDisplay((Button)v);
                        return true;

                }
                return false;
            }
        });

        b_neg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()){
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
                        modifyDisplay((Button)v);
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
        final EditText main_display = (EditText)v.findViewById(R.id.display_value);

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
                                main_display.setText("" + data_constant.optString("Value").replaceAll("\\s+" + "", "").replace("...",""));
                                if(main_display.getText().length() > 12){
                                    main_display.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);
                                }
                                units.setText(formatUnits(data_constant.optString("Unit")));
                                if(data_constant.optString("Uncertainty").equalsIgnoreCase("(exact)")){
                                    //do nothing...
                                }else
                                    display_err.setText(data_constant.optString("Uncertainty").replaceAll("\\s+" + "", "").replace("...", ""));
                                currModifyText = main_display;
                                main_display.requestFocus();
                                main_display.setSelection(currModifyText.getText().length());

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
        currModifyText.requestFocus();

        final EditText displayError = (EditText) v.findViewById(R.id.display_err);

        //Allow persistent plus minus in the error display. -D
        displayError.setText(R.string.plus_minus_sign);
        Selection.setSelection(displayError.getText(), 1);

        displayErrorWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String errString = s.toString();
                if(s.toString().charAt(0) != getString(R.string.plus_minus_sign).toCharArray()[0]){
                    errString = getString(R.string.plus_minus_sign) + s.toString();
                    displayError.setText(errString);
                    Selection.setSelection(displayError.getText(), errString.length());
                }
            }
        };
        displayError.addTextChangedListener(displayErrorWatcher);



        //Prevents keyboard from coming up. -D
        displayValue.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocus();
                if(displayValue.getText().length() > 0){
                    Selection.setSelection(displayValue.getText(), displayValue.getText().length());
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
                Selection.setSelection(displayError.getText(),displayError.getText().length());
                return true;
            }
        });

        displayError.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus) {
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

    public BigDecimal[] compute(String result, String value, String valueErr, String button){
        //Divide by 0 error still problem -D

        int roundingPrecision = 5;

        boolean resultExact = false;
        boolean valueExact = false;

        valueErr = valueErr.replace(getString(R.string.plus_minus_sign), "");
        BigDecimal[] returnArray = {BigDecimal.valueOf(0.0), BigDecimal.valueOf(0.0)};

        String[] resultArrayString = result.split(getString(R.string.plus_minus_sign));
        if(resultArrayString.length <= 1){
            resultExact = true;
        }

        if(valueErr == "" || valueErr == "exact"){
            valueExact = true;
        }

        BigDecimal[] resultsArray = {BigDecimal.valueOf(0.0), BigDecimal.valueOf(0.0)};
        for(int i = 0; i < resultArrayString.length; i++){
            resultsArray[i] = new BigDecimal(resultArrayString[i]);
        }
        BigDecimal[] valueArray = {BigDecimal.valueOf(0.0), BigDecimal.valueOf(0.0)};
        valueArray[0] = new BigDecimal(value);
        if(valueErr != ""){
            valueArray[1] = new BigDecimal(valueErr);
        }


        //Special constant Rules -D
        if(button == "+") {
            returnArray[0] = resultsArray[0].add(valueArray[0]);
            returnArray[1] = resultsArray[1].add(valueArray[1]);
        }
        else if (button == "-") {
            returnArray[0] = resultsArray[0].subtract(valueArray[0]);
            returnArray[1] = resultsArray[1].add(valueArray[1]);
        }
        else if (button == "*") {
            returnArray[0] = resultsArray[0].multiply(valueArray[0]);
            if(resultExact){
                returnArray[1] = resultsArray[0].multiply(valueArray[1]);
            }
            else if(valueExact){
                returnArray[1] = valueArray[0].multiply(resultsArray[1]);
            }
            else {
                returnArray[1] = (((resultsArray[1].divide(resultsArray[0], roundingPrecision, RoundingMode.HALF_UP)).multiply(BigDecimal.valueOf(100))).add(((valueArray[1].divide(valueArray[0], roundingPrecision, RoundingMode.HALF_UP)).multiply(BigDecimal.valueOf(100)))).divide(BigDecimal.valueOf(100), roundingPrecision, RoundingMode.HALF_UP)).multiply(returnArray[0]);
            }
        }
        else if (button == "/") {
            if (valueArray[0].equals(0.0)) {
                System.out.println("THERE IS A PROBLEM");
            }
            else {
                returnArray[0] = resultsArray[0].divide(valueArray[0], roundingPrecision, RoundingMode.HALF_UP);
                if(resultExact && valueExact){
                    returnArray[1] = BigDecimal.valueOf(0.0);
                }
                else if(resultExact){
                    returnArray[1] = valueArray[1].divide(resultsArray[0], roundingPrecision, RoundingMode.HALF_UP);
                }
                else if(valueExact){
                    returnArray[1] = resultsArray[1].divide(valueArray[0], roundingPrecision, RoundingMode.HALF_UP);
                }
                else {
                    returnArray[1] = (((resultsArray[1].divide(resultsArray[0], roundingPrecision, RoundingMode.HALF_UP)).multiply(BigDecimal.valueOf(100))).add(((valueArray[1].divide(valueArray[0], roundingPrecision, RoundingMode.HALF_UP)).multiply(BigDecimal.valueOf(100)))).divide(BigDecimal.valueOf(100), roundingPrecision, RoundingMode.HALF_UP)).multiply(returnArray[0]);
                }
            }
        }
        else if(button == "^") {
            returnArray[0] = BigDecimal.valueOf(Math.exp(valueArray[0].multiply(BigDecimal.valueOf(Math.log(valueArray[1].doubleValue()))).doubleValue()));
            returnArray[1] = (((resultsArray[1].divide(resultsArray[0])).multiply(BigDecimal.valueOf(100))).multiply(valueArray[0]).divide(BigDecimal.valueOf(100))).multiply(returnArray[0]);

        }

        else if(button == "="){
            //If the equals is pressed twice.
            returnArray[0] = valueArray[0];
            returnArray[1] = valueArray[1];
        }

        returnArray[1] = returnArray[1].abs();

        return returnArray;
    }

}