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
        Data data = new Data();
        JSON_Adapter atomic_adapter = new JSON_Adapter();
        JSON_Adapter ion_adapter = new JSON_Adapter();
        JSON_Adapter const_adapter = new JSON_Adapter();
        try {
            atomic_adapter.get_JSON_object(new URL(data.getUrl_atomic_mass()));
            ion_adapter.get_JSON_object(new URL(data.getUrl_ionization()));
            const_adapter.get_JSON_object(new URL(data.getUrl_constants()));
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
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        splash.start();

    }
}
