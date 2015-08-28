package danandzach.labpal;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //Initializes the periodic table -D
        Data.initPeriodicTable();

        JSON_Adapter atomic_adapter = new JSON_Adapter();
        JSON_Adapter ion_adapter = new JSON_Adapter();
        JSON_Adapter const_adapter = new JSON_Adapter();
        try {
            if(isNetworkConnected()){
                    Data.setNetwork_connection(true);
                    Log.v("CONNECTION STATUS: ", "CONNECTED");
                }else{
                    Log.v("CONNECTION STATUS: ", "NOT CONNECTED");
                    Data.constants_is = getResources().openRawResource(R.raw.constants);
                    Data.ionization_is = getResources().openRawResource(R.raw.ionizations);
                    Data.atomic_mass_is = getResources().openRawResource(R.raw.atomic_mass);
                }

            atomic_adapter.get_JSON_object(new URL(Data.getUrl_atomic_mass()));
            ion_adapter.get_JSON_object(new URL(Data.getUrl_ionization()));
            const_adapter.get_JSON_object(new URL(Data.getUrl_constants()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Thread splash = new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    //5 and a half seconds to load the databases
                    this.sleep(5500);
                    Intent start = new Intent(SplashScreen.this, MainActivity.class);
                    start.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(start);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        splash.start();

    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return false;
        } else
            return true;
    }

}
