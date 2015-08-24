package danandzach.labpal;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class JSON_Adapter {

    /*
    Zach

    Adapter Class for handling JSON calls
     */
    public String mUrl;

    public void get_JSON_object(URL url){
        mUrl = url.toString();
        DownloadData downloadData = new DownloadData();
        downloadData.execute(url);
    }

    private class DownloadData extends AsyncTask<URL, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(URL... params) {
            try {
                URL nist_data = new URL(params[0].toString());
                final HttpURLConnection connection = (HttpURLConnection) nist_data.openConnection();

                connection.setReadTimeout(10000);
                connection.setConnectTimeout(10000);
                connection.setRequestMethod("GET");
                connection.setAllowUserInteraction(false);
                connection.connect();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();

                    JSONObject json_data = new JSONObject(sb.toString());

                    return json_data;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            Data data = new Data();
            if(mUrl.equalsIgnoreCase(data.getUrl_atomic_mass())){
                data.setAtomic_mass_data(jsonObject);
                Log.v("DB_LOAD", "Atomic Mass Database Loaded");
            }else if(mUrl.equalsIgnoreCase(data.getUrl_ionization())){
                data.setIonization_data(jsonObject);
                Log.v("DB_LOAD", "Ionization Database Loaded");
            }else if(mUrl.equalsIgnoreCase(data.getUrl_constants())){
                data.setConstants_data(jsonObject);
                Log.v("DB_LOAD", "Constants Database Loaded");
            }
        }
    }

}
