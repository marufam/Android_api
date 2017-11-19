package com.raspi.amien.projectraspi.fragments;

import android.content.Context;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.raspi.amien.projectraspi.MainActivity;
import com.raspi.amien.projectraspi.R;
import com.raspi.amien.projectraspi.data.rest.ApiClient;
import com.raspi.amien.projectraspi.data.rest.ApiInterface;
import com.raspi.amien.projectraspi.data.retrofit.GetGuest;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class homefragment extends Fragment {
    private ViewPager viewPager;
    public homefragment() {
        // Required empty public constructor
    }

    public  homefragment (ViewPager viewPager){
        this.viewPager=viewPager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }
    protected void guest(){
        ApiInterface mApiInterface = ApiClient.GetGuests().create(ApiInterface.class);
        final Call<GetGuest> userCall = mApiInterface.getGuest();
        userCall.enqueue(new Callback<GetGuest>() {

            @Override
            public void onResponse(Call<GetGuest> call, Response<GetGuest> response) {
//                Log.d("asas==",response.body().toString());
                if(response.body().getGuest().get(0).getStatus().toString().equals("0")) {
                   //Toast.makeText(getContext(), response.body().getGuest().get(0).getLocation(), Toast.LENGTH_SHORT).show();
//                    getFragmentManager().beginTransaction().replace(R.id.viewpager, new guestfragment()).commit();
                    viewPager.setCurrentItem(1);
                    Snackbar snackbar = Snackbar
                            .make(getView(), "Guest in front of the door", Snackbar.LENGTH_LONG);
                    ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 500);
                    toneG.startTone(ToneGenerator.TONE_CDMA_ANSWER, 200);
                    toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 500);
                    snackbar.show();

                }
            }

            @Override
            public void onFailure(Call<GetGuest> call, Throwable t) {
//                Toast.makeText(getContext(), "Failed "+t.getCause(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onResume() {
        Timer timerObj = new Timer();
        TimerTask timerTaskObj = new TimerTask() {
            public void run() {
                guest();

            }
        };
        timerObj.schedule(timerTaskObj, 2000, 2000);

        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_homefragment, container, false);
    }


}
