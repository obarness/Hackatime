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

import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final String words[] = {
            "כסא",
            "חתול",
            "בית",
            "כסא",
            "חתול",
            "בית",
            "כסא",
            "חתול",
            "בית",
            "כסא",
            "חתול",
            "בית",
    };

    private final String words_image_urls[] = {
            "http://www.asha-gabay.co.il/_Uploads/dbsArticles/11500(2).jpg",
            "http://www.best-friends.co.il/f-users/user_105106/website_105116/images/thumbs/W_960_6824-25.jpg",
            "http://www.psagotmerkaz.org/wp-content/uploads/2015/01/p17hjafm6p1r1c1pt3d4rr6ddmp1.jpg",
            "http://www.asha-gabay.co.il/_Uploads/dbsArticles/11500(2).jpg",
            "http://www.best-friends.co.il/f-users/user_105106/website_105116/images/thumbs/W_960_6824-25.jpg",
            "http://www.psagotmerkaz.org/wp-content/uploads/2015/01/p17hjafm6p1r1c1pt3d4rr6ddmp1.jpg",
            "http://www.asha-gabay.co.il/_Uploads/dbsArticles/11500(2).jpg",
            "http://www.best-friends.co.il/f-users/user_105106/website_105116/images/thumbs/W_960_6824-25.jpg",
            "http://www.psagotmerkaz.org/wp-content/uploads/2015/01/p17hjafm6p1r1c1pt3d4rr6ddmp1.jpg",
            "http://www.asha-gabay.co.il/_Uploads/dbsArticles/11500(2).jpg",
            "http://www.best-friends.co.il/f-users/user_105106/website_105116/images/thumbs/W_960_6824-25.jpg",
            "http://www.psagotmerkaz.org/wp-content/uploads/2015/01/p17hjafm6p1r1c1pt3d4rr6ddmp1.jpg",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        // example of how to pull data from server
        // result will hold a string of the json data
        getHttpQuery("http://172.22.6.60:5000/test",new VolleyCallback(){
           @Override
            public void onSuccess(String result){
                System.out.println(result);
            }
        });
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
        for(int i=0;i<words.length;i++){
            WordCard wordcard = new WordCard();
            wordcard.setWord_text(words[i]);
            wordcard.setWord_image_url(words_image_urls[i]);
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

}