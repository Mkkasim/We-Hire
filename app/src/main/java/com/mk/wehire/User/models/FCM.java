package com.mk.wehire.User.models;

import android.app.Activity;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.Request;
import com.android.volley.Response;
import com.mk.wehire.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FCM {

    String userFcmToken;
    String title;
    String body;
    String action;
    Context mContext;
    Activity mActivity;

    private RequestQueue requestQueue;
    private final String postUrl = "https://fcm.googleapis.com/fcm/send";
    private final String fcmServerKey ="AAAA7KOMeaA:APA91bFDnOxvzkCEnicg32G2EwpshI9XjXGJXsk1WLYuI7X2PLanilQRZuVTlL4btsjQ0-3moeLaBxBody8y1k2dYnqCnwPnfNJmuwyiUGRQXSe2FSinYlLhLZ5MTdRruFsn3kZpA9UD";


    public FCM(String userFcmToken, String title, String body, String action, Context mContext, Activity mActivity) {
        this.userFcmToken = userFcmToken;
        this.title = title;
        this.body = body;
        this.action = action;
        this.mContext = mContext;
        this.mActivity = mActivity;
    }


    public void SendNotifications() {

        requestQueue = Volley.newRequestQueue(mActivity);

        JSONObject mainObj = new JSONObject();
        try {
            mainObj.put("to", userFcmToken);
            mainObj.put("priority","high");
            JSONObject notiObject = new JSONObject();
            notiObject.put("title", title);
            notiObject.put("body", body);
            notiObject.put("icon", R.drawable.wehire_logo);
            notiObject.put("click_action",""+action);
            mainObj.put("data", notiObject);


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, postUrl, mainObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    // code run is got response

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // code run is got error

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {


                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=" + fcmServerKey);
                    return header;


                }
            };
            requestQueue.add(request);


        } catch (JSONException e) {
            e.printStackTrace();
        }




    }

}
