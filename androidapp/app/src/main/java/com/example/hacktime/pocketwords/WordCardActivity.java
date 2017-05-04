package com.example.hacktime.pocketwords;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by okomlosh on 5/4/2017.
 */

public class WordCardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("onCreate");

        setContentView(R.layout.activity_word_card);
        Intent intent = getIntent();
        String word_text_m = intent.getStringExtra(WordGrid.WORD_TEXT);
        TextView textView = (TextView) findViewById(R.id.word_text);
        textView.setText(word_text_m);

        ImageView imageView = (ImageView) findViewById(R.id.word_image);
        Picasso.with(this)
                .load(intent.getStringExtra(WordGrid.WORD_IMG))
                .resize(250, 200)
                .into(imageView);
    }

}
