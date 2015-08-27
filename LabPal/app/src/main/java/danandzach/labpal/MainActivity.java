package danandzach.labpal;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.app.Fragment;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        //The following method of tabs is deprecated. In API 21 the correct way to use tab
        //navigation is TabLayout however there are not many devices on android 5 so I am
        //Using this backwards compatible method for now. -D
        actionBar.setNavigationMode(android.support.v7.app.ActionBar.NAVIGATION_MODE_TABS);

        //Add tabs for fragments. -D

        android.support.v4.app.Fragment chemSearch = new ChemistrySearch();
        android.support.v7.app.ActionBar.Tab mol_tab = actionBar.newTab();
        mol_tab.setText("Mol Search");
        mol_tab.setTabListener(new NavigationTabsListener(chemSearch));
        actionBar.addTab(mol_tab);

        android.support.v4.app.Fragment calc_frag = new LabCalculations();
        android.support.v7.app.ActionBar.Tab calc_tab = actionBar.newTab();
        calc_tab.setText("Calcs");
        calc_tab.setTabListener(new NavigationTabsListener(calc_frag));
        actionBar.addTab(calc_tab);

        android.support.v4.app.Fragment irView = new IRView();
        android.support.v7.app.ActionBar.Tab ir_tab = actionBar.newTab();
        ir_tab.setText("IR View");
        ir_tab.setTabListener(new NavigationTabsListener(irView));
        actionBar.addTab(ir_tab);

        android.support.v4.app.Fragment labNotes = new LabNotes();
        android.support.v7.app.ActionBar.Tab notes_tab = actionBar.newTab();
        notes_tab.setText("Notes");
        notes_tab.setTabListener(new NavigationTabsListener(labNotes));
        actionBar.addTab(notes_tab);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

class NavigationTabsListener implements android.support.v7.app.ActionBar.TabListener{

    //Implementation of TabListener -D

    public android.support.v4.app.Fragment fragment;

    public NavigationTabsListener(android.support.v4.app.Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public void onTabReselected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {
        //Nothing when reselected
    }

    @Override
    public void onTabSelected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {
        ft.replace(R.id.fragment_container, fragment);
    }

    @Override
    public void onTabUnselected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {
        ft.remove(fragment);
    }

}
