package com.example.hacktime.pocketwords;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Created by okomlosh on 5/5/2017.
 */

public class TrainingActivity extends AppCompatActivity {

    private String jsonStr;
    Utils utils = new Utils();
    ArrayList<WordCard> word_cards = new ArrayList<>();
    Set set;
    Iterator iterator;
    ImageView imageView;
    TextView textView;
    ImageView k_img;
    ImageView uk_img;
    String word_id;
    TextToSpeech ttobj;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        utils.words.clear();
        RequestQueue queue = Volley.newRequestQueue(this);

        setContentView(R.layout.activity_training);
        imageView = (ImageView) findViewById(R.id.word_image);
        textView = (TextView) findViewById(R.id.word_text);
        k_img = (ImageView) findViewById(R.id.Knew);
        uk_img = (ImageView) findViewById(R.id.didnt_knew);


        utils.getHttpQuery("http://172.22.6.60/getTopTen", queue, new Utils.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                jsonStr = new String(result);
                utils.jsonStringToArrays(jsonStr);
                set = utils.words.entrySet();
                iterator = set.iterator();
                showNext();
            }
        });
    }

    private void showNext()
    {
        if(iterator.hasNext()) {
            Map.Entry mentry = (Map.Entry)iterator.next();
            Picasso.with(this)
                    .load(((ArrayList)mentry.getValue()).get(0).toString())
                    .resize(250, 200)
                    .error(R.drawable.reut_bg)
                    .placeholder(R.drawable.progress_animation)
                    .into(imageView);
            textView.setText(mentry.getKey().toString());
            word_id = ((ArrayList)mentry.getValue()).get(1).toString();
        }
        else
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void clickImg(View view)
    {
        textView.setVisibility(view.VISIBLE);
        k_img.setVisibility(view.VISIBLE);
        uk_img.setVisibility(view.VISIBLE);
    }

    public void clickKnewOrDidnt(View view)
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        String updateUrl = "http://172.22.6.60/";
        if (view.getId() == k_img.getId())
            updateUrl = updateUrl + "incrementCounter_";
        else
            updateUrl = updateUrl + "decrementCounter_";
        System.out.println(updateUrl + word_id);

        utils.getHttpQuery(updateUrl + word_id, queue, new Utils.VolleyCallback(){
            @Override
            public void onSuccess(String result){
                textView.setVisibility(View.INVISIBLE);
                k_img.setVisibility(View.INVISIBLE);
                uk_img.setVisibility(View.INVISIBLE);
                showNext();
            }
        });

    }

    public void clickTextTraining(View view)
    {
        ttsp(textView.getText().toString());
    }

    public void ttsp(final String word){
        ttobj = new TextToSpeech(this.getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    ttobj.setLanguage(Locale.UK);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ttobj.speak(word, TextToSpeech.QUEUE_FLUSH, null, null);
                    }
                }
            }
        });
    }

}
