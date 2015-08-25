package danandzach.labpal;

import android.app.ActionBar;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChemistrySearch.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChemistrySearch#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChemistrySearch extends Fragment {

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ChemistrySearch.
     */
    // TODO: Rename and change types and number of parameters
    public static ChemistrySearch newInstance() {
        ChemistrySearch fragment = new ChemistrySearch();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ChemistrySearch() {
        // Required empty public constructor
    }

    /*public LinearLayout formatIsotopeResults(String databaseName, JSONObject databaseContent){
        //Builds a layout for a result from the Atomic Mass and Isotopes Database -D
        return new LinearLayout();
    }*/

    public boolean modifyContent(HashMap<String, JSONObject> searchResults, LinearLayout layoutToModify){
        //A method to modify a LinearLayout to display search results. Results must be
        //passed as key value pairs because the database associated with the content is not
        //in the JSONObject and the modifier needs the name of the database. -D
        layoutToModify.removeAllViews();
        TextView test = new TextView(getActivity());
        test.setText("It Works!");
        layoutToModify.addView(test);


        return true;
    }

    HashMap<String, JSONObject> resultMap = new HashMap<String, JSONObject>();


    public boolean setupSearchBar(EditText searchBar, final ViewGroup content_display){
        //Currently a test method. The result is hard coded in and in the actual app it will be variable.
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if( s.toString().equalsIgnoreCase("a")){
                    modifyContent(resultMap, (LinearLayout) content_display);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chemistry_search, container, false);
        LinearLayout contentArea = (LinearLayout) v.findViewById(R.id.mol_search_content_area);
        EditText search_bar = (EditText) v.findViewById(R.id.search_field);

        setupSearchBar(search_bar, contentArea);

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



}
