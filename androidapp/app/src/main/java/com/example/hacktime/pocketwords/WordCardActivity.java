package com.example.hacktime.pocketwords;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.util.Locale;

/**
 * Created by okomlosh on 5/4/2017.
 */

public class WordCardActivity extends AppCompatActivity {

    private String jsonStr;
    Utils utils = new Utils();
    TextToSpeech ttobj;
    TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final RequestQueue queue = Volley.newRequestQueue(this);

        setContentView(R.layout.activity_word_card);
        Intent intent = getIntent();
        final String word_text_m = intent.getStringExtra(WordGrid.WORD_TEXT);
        textView = (TextView) findViewById(R.id.word_text);
        textView.setText(word_text_m);
        final String word_id = intent.getStringExtra(WordGrid.WORD_ID);

        ImageView imageView = (ImageView) findViewById(R.id.word_image);
        Picasso.with(this)
                .load(intent.getStringExtra(WordGrid.WORD_IMG))
                .resize(250, 200)
                .error(R.drawable.reut_bg)
                .placeholder(R.drawable.progress_animation)
                .into(imageView);

        ImageView imageView1 = (ImageView) findViewById(R.id.add_to_practice);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                utils.getHttpQuery(Utils.getHttpAdress() + "incrementCounter_" + word_id, queue, new Utils.VolleyCallback(){
                    @Override
                    public void onSuccess(String result){
                        Toast.makeText(getApplication() ,"\"" + word_text_m + "\" Was added to Training", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    public void clickText(View view)
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

