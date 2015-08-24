package danandzach.labpal;


import android.app.Activity;
import android.os.Bundle;

import java.net.MalformedURLException;
import java.net.URL;

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Thread getData = new Thread(){
            @Override
            public void run() {
                Data data = new Data();
                try {
                    long tStart = System.currentTimeMillis();

                    data.setAtomic_mass_data(new JSON_Adapter().get_JSON_object(
                            new URL("http://www.nist.gov/srd/srd_data/srd144_Atomic_Weights_and_Isotopic_Compositions_for_All_Elements.json")));

                    data.setIonization_data(new JSON_Adapter().get_JSON_object(new URL(
                            "http://www.nist.gov/srd/srd_data/srd111_NIST_Atomic_Ionization_Energies_Output.json"
                    )));

                    data.setConstants_data(new JSON_Adapter().get_JSON_object(new URL(
                            "http://www.nist.gov/srd/srd_data/srd121_allascii_2014.json"
                    )));

                    long tEnd = System.currentTimeMillis();
                    long tElapsed = tEnd - tStart;

                    //Make sure that the Splash Screen has been showing for at least 5 seconds...
                    if(tElapsed < 5000){
                        try {
                            this.sleep(5000 - tElapsed);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //Must finish here... Probably a good idea to use Asynchronous tasks in the future...
                    //Will add that later. For now, the splash screen waits as long as it takes to load the data.
                    finish();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        };
        getData.start();

    }
}
