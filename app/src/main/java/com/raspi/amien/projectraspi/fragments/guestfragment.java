package com.raspi.amien.projectraspi.fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.raspi.amien.projectraspi.MainActivity;
import com.raspi.amien.projectraspi.R;
import com.raspi.amien.projectraspi.data.rest.ApiClient;
import com.raspi.amien.projectraspi.data.rest.ApiInterface;
import com.raspi.amien.projectraspi.data.retrofit.GetGuest;
import com.raspi.amien.projectraspi.data.retrofit.GetTools;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;


import java.io.InputStream;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class guestfragment extends Fragment {
    View view;
    public ImageView img;
    Button btnok;
    ViewPager viewPager;
    public guestfragment() {
        // Required empty public constructor
    }

    public  guestfragment (ViewPager viewPager){
        this.viewPager=viewPager;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_guestfragment, container, false);
        guest_img(view);
        btnok = (Button) view.findViewById(R.id.okbutton);
        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RequestParams params = new RequestParams();
                params.put("id", "1");
                params.put("status", "1");
                Picasso.with(getActivity()).invalidate(ApiClient.BASE_URL + "upload/guest.jpg" );
                AsyncHttpClient client = new AsyncHttpClient();
                client.post(ApiClient.BASE_URL + "index.php/Guest_api", params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        viewPager.setCurrentItem(0);
//                        Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        Timer timerObj = new Timer();
        TimerTask timerTaskObj = new TimerTask() {
            public void run() {
                guest_img(view);

            }
        };
        timerObj.schedule(timerTaskObj, 1000, 1000);

        super.onResume();
    }

    public void guest_img(View view){
        ApiInterface mApiInterface = ApiClient.GetGuests().create(ApiInterface.class);
        img = (ImageView) view.findViewById(R.id.guest_img);
        final Call<GetGuest> userCall = mApiInterface.getGuest();
        userCall.enqueue(new Callback<GetGuest>() {

            @Override
            public void onResponse(Call<GetGuest> call, Response<GetGuest> response) {
//                Log.d("dfdf", ApiClient.BASE_URL + "upload/" + response.body().getGuest().get(0).getLocation());
//                Toast.makeText(getContext(), ApiClient.BASE_URL + "upload/" + response.body().getGuest().get(0).getLocation(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                if(response.body().getGuest().get(0).getStatus().toString().equals("0")) {

                    Picasso.with(getActivity().getApplicationContext())
                            .load(ApiClient.BASE_URL + "upload/" + response.body().getGuest().get(0).getLocation())
                            .into(img);
//                    Picasso.with(getActivity()).invalidate(ApiClient.BASE_URL + "upload/" + response.body().getGuest().get(0).getLocation());
                }else{
                    img.setImageBitmap(null);
                    viewPager.setCurrentItem(0);
                }

            }

            @Override
            public void onFailure(Call<GetGuest> call, Throwable t) {
               // Toast.makeText(getContext(), "Failed "+t.getCause(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
