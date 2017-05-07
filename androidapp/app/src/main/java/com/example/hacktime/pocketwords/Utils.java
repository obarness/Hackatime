package com.example.hacktime.pocketwords;

import android.speech.tts.TextToSpeech;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Utils {

    public static HashMap<String, List<String>> words = new HashMap<>();


    public static void getHttpQuery(String url, RequestQueue queue,final VolleyCallback callback) {

        // Request a string response from the provided URL.
        JsonArrayRequest jsObjRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        callback.onSuccess(response.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onSuccess(error.getMessage());
                    }
                });
        // Add the request to the RequestQueue.
        queue.add(jsObjRequest);
    }
    public static interface VolleyCallback{
        void onSuccess(String result);
    }

    public static String getHttpAdress()
    {
        return ("http://10.124.184.72/");
    }

    public static void jsonStringToArrays(String str){
        try {
            JSONArray jsonArr = new JSONArray(str);
            for(int i=0;i<jsonArr.length();i++){
                List<String> list = new ArrayList<>();
                JSONObject e = jsonArr.getJSONObject(i);
                list.add(e.getString("imageSource"));
                list.add(e.getString("wordId"));
                words.put(e.getString("text"),list);
            }
        } catch (final JSONException e) {
            System.out.println("Json parsing error: " + e.getMessage());
        }
    }
}
