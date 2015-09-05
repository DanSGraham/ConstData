package danandzach.labpal;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import org.json.JSONException;


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

        AutoCompleteTextView constants_search = (AutoCompleteTextView)v.findViewById(R.id.calculator_search);

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
        try {
            String [] adapter_list = new String[Data.getConstants_data().getJSONArray(Data.getConstants_array_name()).length()];
            for(int i = 0; i < Data.getConstants_data().getJSONArray(Data.getConstants_array_name()).length(); i++){
                adapter_list[i] = Data.getConstants_data().getJSONArray(Data.getConstants_array_name()).getJSONObject(i).optString("Quantity ");
            }

            final ArrayAdapter<String> auto_complete = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, adapter_list);
            constants_search.setAdapter(auto_complete);
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