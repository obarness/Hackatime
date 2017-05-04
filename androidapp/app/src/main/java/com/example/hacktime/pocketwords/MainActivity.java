package com.example.hacktime.pocketwords;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private final String jsonTestString = "[{" +
            "\t\"text\": \"kitchen\"," +
            "\t\"imageSource\": \"http://travisperkins.scene7.com/is/image/travisperkins/ready-to-fit-kitchen-Orlando-White-IMG-Main?wid=710\"," +
            "\t\"wordId\": 1," +
            "\t\"children\": \",2,4\"," +
            "\t\"isRoot\": \"true\"" +
            "}, {" +
            "\t\"text\": \"garden\"," +
            "\t\"imageSource\": \"http://theunboundedspirit.com/wp-content/uploads/2013/11/flower-garden.jpg\"," +
            "\t\"wordId\": 3," +
            "\t\"children\": \"\"," +
            "\t\"isRoot\": \"true\"" +
            "}]";
    //private String words[] = {};
    //List<String> words = new ArrayList<>();
     HashMap<String, String> words = new HashMap<>();


    //private String words_image_urls[] = {};
    //List<String> words_image_urls = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        jsonStringToArrays();
        initViews();

        // example of how to pull data from server
        // result will hold a string of the json data
       // getHttpQuery("http://172.22.6.60:5000/test",new VolleyCallback(){
       //    @Override
        //    public void onSuccess(String result){
         //       System.out.println(result);
          //  }
        //});
    }
    private void initViews(){
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<WordCard> wordcards = prepareData();
        WordGrid adapter = new WordGrid(getApplicationContext(),wordcards);
        recyclerView.setAdapter(adapter);

    }
    private ArrayList<WordCard> prepareData(){
        ArrayList<WordCard> word_cards = new ArrayList<>();
        Set set = words.entrySet();
        Iterator iterator = set.iterator();
        while(iterator.hasNext()) {
            Map.Entry mentry = (Map.Entry)iterator.next();
            WordCard wordcard = new WordCard();
            wordcard.setWord_text(mentry.getKey().toString());
            wordcard.setWord_image_url(mentry.getValue().toString());
            word_cards.add(wordcard);
        }
        return word_cards;
    }

    private void getHttpQuery(String url,final VolleyCallback callback) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
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
    public interface VolleyCallback{
        void onSuccess(String result);
    }

    private void jsonStringToArrays(){
        try {
            JSONArray jsonArr = new JSONArray(jsonTestString);
            for(int i=0;i<jsonArr.length();i++){
                JSONObject e = jsonArr.getJSONObject(i);
                words.put(e.getString("text"),e.getString("imageSource"));
            }
        } catch (final JSONException e) {
            System.out.println("Json parsing error: " + e.getMessage());
        }
    }
}