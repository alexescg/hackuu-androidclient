package com.islas.jonathan.burlamultas;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.TimerTask;

/**
 * Created by Jonathan on 27/09/2015.
 */
public class TareaAsync extends TimerTask {

    private LocationManager locationManager;
    public Location lastLocation;
    private Activity act;
    public MediaPlayer  mediaPlayer = new MediaPlayer();
    public NotificationManager notificationManager;


    public TareaAsync(Activity act) {
        this.act = act;
        notificationManager = (NotificationManager) act.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public void run() {
        locationManager = (LocationManager) act.getSystemService(Activity.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_MEDIUM);
        String provider = locationManager.getBestProvider(criteria, true);
        if (ContextCompat.checkSelfPermission(act, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(act, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


        }
        lastLocation = locationManager.getLastKnownLocation(provider);
        String URL = "https://multaschihuahua.herokuapp.com/matrixNearestRequest/" + lastLocation.getLatitude()
        + "/" + lastLocation.getLongitude()+ "/?";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (URL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);
                        try {
                            if(response.getJSONObject("map").getBoolean("isNear")){
                                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                    @Override
                                    public void onPrepared(MediaPlayer mp) {
                                        if (mp == mediaPlayer) {
                                            mediaPlayer.start();
                                        }
                                    }
                                });
                                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(act)
                                        .setSmallIcon(R.drawable.cast_ic_notification_0)
                                        .setContentTitle(act.getString(R.string.app_name))
                                        .setContentText("default")
                                        .setDefaults(Notification.DEFAULT_SOUND)
                                        .setAutoCancel(true);
                                notificationManager.notify(0, mBuilder.build());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ;
                        //Do sound
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        //add widget toast
                    }
                });
        System.out.println(lastLocation);
        MySingleton.getInstance(act.getApplicationContext()).addToRequestQueue(jsObjRequest);
    }

    public static class MySingleton {

        private static MySingleton mInstance;
        private RequestQueue mRequestQueue;
        private static Context mCtx;

        private MySingleton(Context context) {
            mCtx = context;
            mRequestQueue = getRequestQueue();
        }

        public static synchronized MySingleton getInstance(Context context) {
            if (mInstance == null) {
                mInstance = new MySingleton(context);
            }
            return mInstance;
        }

        public RequestQueue getRequestQueue() {
            if (mRequestQueue == null) {
                // getApplicationContext() is key, it keeps you from leaking the
                // Activity or BroadcastReceiver if someone passes one in.
                mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
            }
            return mRequestQueue;
        }

        public <T> void addToRequestQueue(Request<T> req) {
            getRequestQueue().add(req);
        }
    }
}
