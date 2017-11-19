package com.raspi.amien.projectraspi;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.raspi.amien.projectraspi.data.rest.ApiClient;
import com.raspi.amien.projectraspi.data.rest.ApiInterface;
import com.raspi.amien.projectraspi.data.retrofit.GetGuest;
import com.raspi.amien.projectraspi.data.retrofit.GetTools;
import com.raspi.amien.projectraspi.fragments.guestfragment;
import com.raspi.amien.projectraspi.fragments.homefragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity  {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SpeechRecognizerManager mSpeechManager;
    private static AsyncHttpClient client = new AsyncHttpClient();
    private TextToSpeech tts;
    private int[] tabIcons = {
            R.drawable.ic_tab_favourite,
            R.drawable.ic_tab_call
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icon_text_tabs);
        ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 500);
        toneG.startTone(ToneGenerator.TONE_CDMA_ANSWER, 200);
        toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 500);
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitle("Jarvis");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

//        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
//            @Override
//            public void onInit(int status) {
//                speak("I am go to the market");
//            }
//        });

    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);

    }
    @Override
    protected void onResume() {

        if(PermissionHandler.checkPermission(this,PermissionHandler.RECORD_AUDIO)) {

            if (mSpeechManager == null) {
                SetSpeechListener();
            } else if (!mSpeechManager.ismIsListening()) {
                mSpeechManager.destroy();
                SetSpeechListener();
            }
//            result_tv.setText(getString(R.string.you_may_speak));
        }else
        {
            PermissionHandler.askForPermission(PermissionHandler.RECORD_AUDIO,this);
        }


        super.onResume();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new homefragment(viewPager), "Jarvis");
        adapter.addFrag(new guestfragment(viewPager), "Notification");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    private void speak(String text){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);

        }else{
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode)
        {
            case PermissionHandler.RECORD_AUDIO:
                if(grantResults.length>0) {
                    if(grantResults[0]== PackageManager.PERMISSION_GRANTED) {
                        onResume();
                    }
                }
                break;

        }
    }


    private void SetSpeechListener()
    {
        mSpeechManager=new SpeechRecognizerManager(this, new SpeechRecognizerManager.onResultsReady() {
            @Override
            public void onResults(ArrayList<String> results) {



                if(results!=null && results.size()>0)
                {

                    if(results.size()==1)
                    {
//                        mSpeechManager.destroy();
//                        mSpeechManager = null;
                        //result_tv.setText(results.get(0));
                        for (String result : results) {
                            Log.d("Judul","==++>>>>>"+result);

                            if(result.toString().equalsIgnoreCase("kitchen on")){
//                                hasil.setText("The lamp turn on bedroom");
                                lamp_on("1","1");
                                getar();
                            }else if(result.toString().equalsIgnoreCase("kitchen off")){
//                                hasil.setText("The lamp turn off bedroom");
                                getar();
                                lamp_on("1","0");
                            }else if(result.toString().equalsIgnoreCase("toilet on")){
//                                hasil.setText("The lamp turn on kitchen");
                                lamp_on("2","1");
                                getar();
                            }else if(result.toString().equalsIgnoreCase("toilet off")) {
//                                hasil.setText("The lamp turn off kitchen");
                                lamp_on("2","0");
                                getar();
                            }else if(result.toString().equalsIgnoreCase("living room on")){
//                                hasil.setText("The lamp turn on living room");
                                lamp_on("3","1");
                                getar();
                            }else if(result.toString().equalsIgnoreCase("living room off")) {
//                                hasil.setText("The lamp turn off living room");
                                lamp_on("3","0");
                                getar();
                            }else if(result.toString().equalsIgnoreCase("bedroom on")){
//                                hasil.setText("The lamp turn on living room");
                                lamp_on("4","1");
                                getar();
                            }else if(result.toString().equalsIgnoreCase("bedroom off")) {
//                                hasil.setText("The lamp turn off living room");
                                lamp_on("4","0");
                                getar();
                            }else if(result.toString().equalsIgnoreCase("open the door")) {
//                                hasil.setText("The lamp turn off living room");
                                lamp_on("5","1");
                                getar();
                            }else if(result.toString().equalsIgnoreCase("close the door")) {
//                                hasil.setText("The lamp turn off living room");
                                lamp_on("5","0");
                                getar();
                            }else if(result.toString().equalsIgnoreCase("help")) {
//                                hasil.setText("The lamp turn off living room");
                                helper("1","1","1");
                                getar();
                            }else if(result.toString().equalsIgnoreCase("jarvis")) {
//                                hasil.setText("Yes master");


                            }

                        }
                    }
                    else {
                        StringBuilder sb = new StringBuilder();
                        if (results.size() > 5) {
                            results = (ArrayList<String>) results.subList(0, 5);
                        }
                        for (String result : results) {
                            sb.append(result).append("\n");
                        }
                        for (String result : results) {
                            Log.d("Judul","==++>>>>>"+result);

                            if(result.toString().equalsIgnoreCase("kitchen on")){
//                                hasil.setText("The lamp turn on bedroom");
                                lamp_on("1","1");
                                getar();
                            }else if(result.toString().equalsIgnoreCase("kitchen off")){
//                                hasil.setText("The lamp turn off bedroom");
                                getar();
                                lamp_on("1","0");
                            }else if(result.toString().equalsIgnoreCase("toilet on")){
//                                hasil.setText("The lamp turn on kitchen");
                                lamp_on("2","1");
                                getar();
                            }else if(result.toString().equalsIgnoreCase("toilet off")) {
//                                hasil.setText("The lamp turn off kitchen");
                                lamp_on("2","0");
                                getar();
                            }else if(result.toString().equalsIgnoreCase("living room on")){
//                                hasil.setText("The lamp turn on living room");
                                lamp_on("3","1");
                                getar();
                            }else if(result.toString().equalsIgnoreCase("living room off")) {
//                                hasil.setText("The lamp turn off living room");
                                lamp_on("3","0");
                                getar();
                            }else if(result.toString().equalsIgnoreCase("bedroom on")){
//                                hasil.setText("The lamp turn on living room");
                                lamp_on("4","1");
                                getar();
                            }else if(result.toString().equalsIgnoreCase("bedroom off")) {
//                                hasil.setText("The lamp turn off living room");
                                lamp_on("4","0");
                                getar();
                            }else if(result.toString().equalsIgnoreCase("open the door")) {
//                                hasil.setText("The lamp turn off living room");
                                lamp_on("5","1");
                                getar();
                            }else if(result.toString().equalsIgnoreCase("close the door")) {
//                                hasil.setText("The lamp turn off living room");
                                lamp_on("5","0");
                                getar();
                            }else if(result.toString().equalsIgnoreCase("help")) {
//                                hasil.setText("The lamp turn off living room");
                                helper("1","1","1");
                                getar();
                            }else if(result.toString().equalsIgnoreCase("jarvis")) {
//                                hasil.setText("Yes master");



                            }

                        }
                        //result_tv.setText(sb.toString());
                    }
                }
                else{}
                    //result_tv.setText(getString(R.string.no_results_found));
            }
        });
    }

    void helper(String Id, String NoRoom, String Status){
        RequestParams params = new RequestParams();
        params.put("id", Id.toString());
        params.put("no_room", NoRoom.toString());
        params.put("status", Status.toString());
        client.post(ApiClient.BASE_URL + "index.php/Helper_api", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(MainActivity.this, "Succsess", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onPause() {
        if(mSpeechManager!=null) {
            mSpeechManager.destroy();
            mSpeechManager=null;
        }
        super.onPause();

    }

    protected void lamp_on(String id, String status){
        ApiInterface mApiInterface = ApiClient.GetTools().create(ApiInterface.class);
        final Call<GetTools> userCall = mApiInterface.user_insert(
                id.toString(),
                status.toString());
        userCall.enqueue(new Callback<GetTools>() {

            @Override
            public void onResponse(Call<GetTools> call, Response<GetTools> response) {

                Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<GetTools> call, Throwable t) {
//                Toast.makeText(MainActivity.this, "Failed "+t.getCause(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    void getar(){
        Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(500);
    }
}
