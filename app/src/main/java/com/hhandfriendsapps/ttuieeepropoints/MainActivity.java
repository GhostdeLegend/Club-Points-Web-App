package com.hhandfriendsapps.ttuieeepropoints;

import android.app.Instrumentation;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.inputmethodservice.Keyboard;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import com.hhandfriendsapps.ttuieeepropoints.AudioMonitor;
import com.hhandfriendsapps.ttuieeepropoints.MessageType;
import com.hhandfriendsapps.ttuieeepropoints.AudioDecoder;
import com.hhandfriendsapps.ttuieeepropoints.HeadsetStateReceiver;
import com.hhandfriendsapps.ttuieeepropoints.SwipeData;

import org.w3c.dom.Text;


public class MainActivity extends ActionBarActivity implements AudioMonitorActivity {

    private Handler mUiHandler;
    private Handler mBackgroundHandler;

    private HeadsetStateReceiver mHeadsetStateReceiver;
    private IntentFilter mIntentFilter;
    private Thread mAudioMonitorThread;
    private Runnable mRunnable;
    private Handler mCallback;
    private AudioMonitor mAudioMonitor;
    private AudioDecoder mAudioDecoder;

    private Runnable mRunnableOutput;
    private Thread mOutputStringThread;

    public String rNumber;


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

        mHeadsetStateReceiver = new HeadsetStateReceiver(this);
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(Intent.ACTION_HEADSET_PLUG);

        mAudioMonitorThread = new Thread();
        mOutputStringThread = new Thread();

        mRunnable = new Runnable() {
            @Override
            public void run() {
                mAudioMonitor.monitor();
                System.out.println("At the end");

            }
        };

        mRunnableOutput = new Runnable() {
            @Override
            public void run() {
                System.out.println("Output Runnable");

                Instrumentation instrumentation = new Instrumentation();
                view.requestFocus();
                int i = 0;
                Log.d("DEBUG", "Before do: " + rNumber);
                if(rNumber != null) {
                    System.out.println(rNumber.length());
                    if (rNumber.length() == 8) {
                        System.out.println("Inside If: ");
                        do {
                            System.out.println("Inside Do");
                            System.out.println(rNumber.charAt(i));

                            //instrumentation.sendCharacterSync(KeyEvent.KEYCODE_0);
                            //System.out.println(rNumber.charAt(i));
                            switch (rNumber.charAt(i)) {
                                case '0':
                                    instrumentation.sendCharacterSync(KeyEvent.KEYCODE_0);
                                    System.out.println("Key-0");
                                    break;
                                case '1':
                                    instrumentation.sendCharacterSync(KeyEvent.KEYCODE_1);
                                    System.out.println("Key-1");
                                    break;
                                case '2':
                                    instrumentation.sendCharacterSync(KeyEvent.KEYCODE_2);
                                    System.out.println("Key-2");
                                    break;
                                case '3':
                                    instrumentation.sendCharacterSync(KeyEvent.KEYCODE_3);
                                    System.out.println("Key-3");
                                    break;
                                case '4':
                                    instrumentation.sendCharacterSync(KeyEvent.KEYCODE_4);
                                    System.out.println("Key-4");
                                    break;
                                case '5':
                                    instrumentation.sendCharacterSync(KeyEvent.KEYCODE_5);
                                    System.out.println("Key-5");
                                    break;
                                case '6':
                                    instrumentation.sendCharacterSync(KeyEvent.KEYCODE_6);
                                    System.out.println("Key-6");
                                    break;
                                case '7':
                                    instrumentation.sendCharacterSync(KeyEvent.KEYCODE_7);
                                    System.out.println("Key-7");
                                    break;
                                case '8':
                                    instrumentation.sendCharacterSync(KeyEvent.KEYCODE_8);
                                    System.out.println("Key-8");
                                    break;
                                case '9':
                                    instrumentation.sendCharacterSync(KeyEvent.KEYCODE_9);
                                    System.out.println("Key-9");
                                    break;
                                default:
                                    break;
                            }
                            i = i + 1;
                        } while (i < 8);
                        i = 0;
                    }
                }
            }
        };

        mCallback = new Handler(new Handler.Callback(){
            @Override
            public boolean handleMessage(Message msg){
                if (msg.what == MessageType.NO_DATA_PRESENT.ordinal()) {
                    //System.out.println("No data present");
                    return true;
                } else if (msg.what == MessageType.DATA_PRESENT.ordinal()) {
                    System.out.println("data present");
                    hideMessage();
                    hideTrackData();
                    return true;
                } else if (msg.what == MessageType.RECORDING_ERROR.ordinal()) {
                    showErrorMessage("Recording error");
                    return true;
                } else if (msg.what == MessageType.INVALID_SAMPLE_RATE
                        .ordinal()) {
                    showErrorMessage("Invalid sample rate");
                    return true;
                } else if (msg.what == MessageType.DATA.ordinal()) {
                    System.out.println("Data received");
                    onNewTrackData((List<Integer>) msg.obj);
                    return true;
                } else {
                    return false;
                }
            }
        });

        mAudioMonitor = new AudioMonitor(mCallback);
        mAudioDecoder = new AudioDecoder();


    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mHeadsetStateReceiver, mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mHeadsetStateReceiver);
        stopAudioMonitor();
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

    private void startAudioMonitor() {
        System.out.println("Loc. 1");

        try {
            mAudioMonitorThread.join();
        } catch (InterruptedException e) {
        }

        mAudioMonitorThread = new Thread(mRunnable);
        mAudioMonitorThread.start();
    }

    private void outputString(){
        System.out.println("Output String");

        try{
            mOutputStringThread.join();
        } catch (InterruptedException e) {
        }

        mOutputStringThread = new Thread(mRunnableOutput);
        mOutputStringThread.start();
    }

    private void stopAudioMonitor() {
        System.out.println("Loc. 3");
        if (mAudioMonitor.isRecording()) {
            mAudioMonitor.stopRecording();
        }
        try {
            mAudioMonitorThread.join();
        } catch (InterruptedException e) {
        }
    }

    @Override
    public void setDongleReady(boolean state) {
        hideMessage();
        hideTrackData();

        if (state) {
            //mDongleStateTextView.setTextColor(Color.GREEN);
            //mDongleStateTextView.setText("connected");

            startAudioMonitor();
        } else {
            //mDongleStateTextView.setText("disconnected");
            //mDongleStateTextView.setTextColor(Color.RED);

            stopAudioMonitor();
        }
    }

    private void showMessage(String msg, int color) {
        hideTrackData();
//        mMessageView.setVisibility(View.VISIBLE);
//        mMessageTextView.setText(msg);
//        mMessageTextView.setTextColor(color);
    }

    private void showStatusMessage(String msg) {
        showMessage(msg, Color.GREEN);
    }

    private void showErrorMessage(String msg) {
        showMessage(msg, Color.RED);
    }

    private void showTrackData(String data) {
        hideMessage();
//      mTrackDataView.setVisibility(View.VISIBLE);
//      mTrackDataTextView.setText(data);

        rNumber = data;
        if(rNumber.length() == 15) {
            rNumber = rNumber.substring(1, 9);
        }
        System.out.println(rNumber);
        outputString();
        System.out.println("After Output");
    }

    private void hideMessage() {
//        mMessageView.setVisibility(View.INVISIBLE);
//        mMessageTextView.setText("");
    }

    private void hideTrackData() {
//        mTrackDataView.setVisibility(View.INVISIBLE);
//        mTrackDataTextView.setText("");
    }

    private void onNewTrackData(List<Integer> samples) {
        stopAudioMonitor();
        SwipeData data = mAudioDecoder.processData(samples);
        if (data.isBadRead()) {
            showErrorMessage("Bad read");
        } else {
            showTrackData(data.content);
        }
        startAudioMonitor();
    }
}
