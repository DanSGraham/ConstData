package dan.constantdata;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MoleculeSearch.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MoleculeSearch#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoleculeSearch extends Fragment {



/**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MoleculeSearch.*/

    // TODO: Rename and change types and number of parameters
    public static MoleculeSearch newInstance() {
        MoleculeSearch fragment = new MoleculeSearch();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public MoleculeSearch() {
        // Required empty public constructor
    }


    public HashMap<String, JSONObject> queryDatabaases(String queryString){
        //This is the method that will return an array of JSON objects associated with each database
        //searched. I actually need a hashmap with the key being the name of teh database searched
        //and the value being the json object. This will make formatting way easier.

        //The following is for designing the layout only

        HashMap<String, JSONObject> query_results = new HashMap<String, JSONObject>();

        JSON_Adapter json_adapter = new JSON_Adapter();
        try{
            JSONObject json_data = json_adapter.get_JSON_object(new URL("http://www.nist.gov/srd/srd_data/srd144_Atomic_Weights_and_Isotopic_Compositions_for_All_Elements.json"));
            query_results.put("Atomic Weights and Isotopic Compositions", json_data);
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }

        return query_results;
    }

    public LinearLayout isotopeDBView(JSONObject isotopeObject){
        LinearLayout ll = new LinearLayout(this.getActivity());
        ll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ll.setOrientation(LinearLayout.VERTICAL);

        TextView title = new TextView(this.getActivity());
        title.setText("TITLE HERE");
        ll.addView(title);
        return ll;

    }

    public void updateContentView(ViewGroup contentArea, HashMap<String, JSONObject> searchResults){
        for (String key: searchResults.keySet()){
            if (key == "Atomic Weights and Isotopic Compositions"){
                contentArea.addView(isotopeDBView(searchResults.get(key)));
            }
        }

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_molecule_search, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState){
        //HashMap <String, JSONObject> results = queryDatabaases("TESTSTRING");
        //LinearLayout content_section = (LinearLayout) getView().findViewById(R.id.content_layout);
        //updateContentView(content_section, results);
    }

}
