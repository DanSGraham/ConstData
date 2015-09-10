package danandzach.labpal;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
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
                    if(Data.getNetwork_connection() == true){
                        URL nist_data = new URL(params[0].toString());
                        final HttpURLConnection connection = (HttpURLConnection) nist_data.openConnection();

                        connection.setReadTimeout(4000);
                        connection.setConnectTimeout(1000);
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
                    }else{
                        InputStream is = null;
                        Log.v("NO INTERNET:", "READING JSON FILE");
                        if(mUrl.equalsIgnoreCase(Data.getUrl_constants())){
                            is = Data.constants_is;
                        }else if(mUrl.equalsIgnoreCase(Data.getUrl_ionization())){
                            is = Data.ionization_is;
                        }else if(mUrl.equalsIgnoreCase(Data.getUrl_atomic_mass())){
                            is = Data.atomic_mass_is;
                        }else if(mUrl.equalsIgnoreCase(Data.getUrl_ccc())){
                            is = Data.ccc_is;
                        }
                        BufferedReader br = new BufferedReader(new InputStreamReader(is));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while((line = br.readLine()) != null){
                            sb.append(line + "\n");
                        }
                        is.close();
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
            if(jsonObject == null){
                InputStream is = null;
                Log.v("NO INTERNET:", "READING JSON FILE");
                if(mUrl.equalsIgnoreCase(Data.getUrl_constants())){
                    is = Data.constants_is;
                }else if(mUrl.equalsIgnoreCase(Data.getUrl_ionization())){
                    is = Data.ionization_is;
                }else if(mUrl.equalsIgnoreCase(Data.getUrl_atomic_mass())){
                    is = Data.atomic_mass_is;
                }else if(mUrl.equalsIgnoreCase(Data.getUrl_ccc())){
                    is = Data.ccc_is;
                }
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                String line;
                try {
                    while((line = br.readLine()) != null){
                        sb.append(line + "\n");
                    }
                    is.close();
                    br.close();
                    JSONObject json_data = new JSONObject(sb.toString());

                    if(mUrl.equalsIgnoreCase(Data.getUrl_constants())){
                        Data.setConstants_data(json_data);
                    }else if(mUrl.equalsIgnoreCase(Data.getUrl_ionization())){
                        Data.setIonization_data(json_data);
                    }else if(mUrl.equalsIgnoreCase(Data.getUrl_atomic_mass())){
                        Data.setAtomic_mass_data(json_data);
                    }else if(mUrl.equalsIgnoreCase(Data.getUrl_ccc())){
                        Data.setCcc_data(json_data);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else{
                if(mUrl.equalsIgnoreCase(Data.getUrl_atomic_mass())){
                    Data.setAtomic_mass_data(jsonObject);
                    Log.v("DB_LOAD", "Atomic Mass Database Loaded");
                }else if(mUrl.equalsIgnoreCase(Data.getUrl_ionization())){
                    Data.setIonization_data(jsonObject);
                    Log.v("DB_LOAD", "Ionization Database Loaded");
                }else if(mUrl.equalsIgnoreCase(Data.getUrl_constants())){
                    Data.setConstants_data(jsonObject);
                    Log.v("DB_LOAD", "Constants Database Loaded");
                }else if(mUrl.equalsIgnoreCase(Data.getUrl_ccc())){
                    Data.setCcc_data(jsonObject);
                    Log.v("DB_LOAD", "Computational Chemistry Database Loaded");
                }
            }

        }
    }

}
