package com.example.hacktime.pocketwords;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class WordGrid extends RecyclerView.Adapter<WordGrid.ViewHolder> {
    private ArrayList<WordCard> word_cards;
    private Context context;

    public static final String WORD_TEXT = "com.example.hacktime.pocketwords.TEXT";
    public static final String WORD_IMG = "com.example.hacktime.pocketwords.IMG_URL";
    public static final String WORD_ID = "com.example.hacktime.pocketwords.ID_WORD";

    public WordGrid(Context context,ArrayList<WordCard> word_cards) {
        this.word_cards = word_cards;
        this.context = context;
    }

    @Override
    public WordGrid.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.word_object, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final WordGrid.ViewHolder viewHolder, final int i) {

        viewHolder.text_word.setText(word_cards.get(i).getWord_text());
        Picasso.with(context).load(word_cards.get(i).getWord_image_url()).error(R.drawable.reut_bg).placeholder(R.drawable.reut_bg)
                .resize(240, 240).into(viewHolder.img_word);
        viewHolder.img_word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (context, WordCardActivity.class);
                intent.putExtra(WORD_TEXT, word_cards.get(i).getWord_text());
                intent.putExtra(WORD_IMG, word_cards.get(i).getWord_image_url());
                intent.putExtra(WORD_ID, word_cards.get(i).getWord_id());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }

        });
        viewHolder.id_word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (context, MainActivity.class);
                intent.putExtra(WORD_ID, word_cards.get(i).getWord_id());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return word_cards.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView text_word;
        private ImageView img_word;
        private Button id_word;
        public ViewHolder(View view) {
            super(view);

            text_word = (TextView)view.findViewById(R.id.grid_text);
            img_word = (ImageView) view.findViewById(R.id.grid_image);
            id_word = (Button) view.findViewById(R.id.grid_expand);
        }
    }

}
