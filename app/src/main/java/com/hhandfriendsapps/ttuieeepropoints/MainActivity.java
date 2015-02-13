package com.hhandfriendsapps.ttuieeepropoints;

import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import java.io.File;


public class MainActivity extends ActionBarActivity {

    // Insert desired URL here to set starting page
    String url_home = "http://www.mobile.ttuieee.org";
    String url_points = "http://www.mobile.ttuieee.org/points.html";
    String url_events = "http://www.mobile.ttuieee.org/events.html";
    String url_students = "http://www.mobile.ttuieee.org/student.html";
    String url_labs = "http://www.mobile.ttuieee.org/labs.html";

    WebView view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view = (WebView) this.findViewById(R.id.webView);
        view.getSettings().setJavaScriptEnabled(true);
        view.setWebViewClient(new WebViewClient());
        view.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        view.loadUrl(url_home);

//        File file = new File(Environment.getRootDirectory().getAbsolutePath());
//
//        AudioMonitor Audio;
//        Audio.monitor();
//
//        Thread thread = new Thread(new Runnable() {
//            public void run() {
//                record();
//            }
//        });
//        thread.start();

    }

    public boolean updateUrl(WebView newView, String url){
        newView.loadUrl(url);
        return true;
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
        if (id == R.id.action_home) {
            this.updateUrl(view, url_home);
            return true;
        }
        if(id == R.id.action_points){
            this.updateUrl(view, url_points);
            return true;
        }
        if(id == R.id.action_events){
            this.updateUrl(view, url_events);
            return true;
        }
        if(id == R.id.action_students){
            this.updateUrl(view, url_students);
            return true;
        }
        if(id == R.id.action_labs){
            this.updateUrl(view, url_labs);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
