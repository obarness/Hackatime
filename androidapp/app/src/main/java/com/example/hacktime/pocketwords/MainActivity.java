package com.example.hacktime.pocketwords;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private String jsonStr;
    Utils utils = new Utils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Button training_btn = (Button) findViewById(R.id.training_activity);

        utils.words.clear();


        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        Intent intent = getIntent();
        String url = "http://172.22.6.60/";
        if (intent.getStringExtra(WordGrid.WORD_ID)!=null)
            url = url + "getChildrenOf_" + intent.getStringExtra(WordGrid.WORD_ID);
        else
            url = url + "getRoots";

        //example of how to pull data from server
        //result will hold a string of the json data
        utils.getHttpQuery(url, queue, new Utils.VolleyCallback(){
           @Override
            public void onSuccess(String result){
               jsonStr = new String(result);
               utils.jsonStringToArrays(jsonStr);
               initViews();
            }
        });

    }

    public void startTrianing(View view)
    {
        Intent intent = new Intent (this, TrainingActivity.class);
        startActivity(intent);
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
        Set set = utils.words.entrySet();
        Iterator iterator = set.iterator();
        while(iterator.hasNext()) {
            Map.Entry mentry = (Map.Entry)iterator.next();
            WordCard wordcard = new WordCard();
            wordcard.setWord_text(mentry.getKey().toString());
            wordcard.setWord_image_url(((ArrayList)mentry.getValue()).get(0).toString());
            wordcard.setWord_id(((ArrayList)mentry.getValue()).get(1).toString());
            word_cards.add(wordcard);
        }
        return word_cards;
    }

}