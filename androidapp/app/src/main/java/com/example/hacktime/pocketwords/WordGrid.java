package com.example.hacktime.pocketwords;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class WordGrid extends RecyclerView.Adapter<WordGrid.ViewHolder> {
    private ArrayList<WordCard> word_cards;
    private Context context;

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
    public void onBindViewHolder(WordGrid.ViewHolder viewHolder, int i) {

        viewHolder.text_word.setText(word_cards.get(i).getWord_text());
        Picasso.with(context).load(word_cards.get(i).getWord_image_url()).resize(240, 240).into(viewHolder.img_word);
    }

    @Override
    public int getItemCount() {
        return word_cards.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView text_word;
        private ImageView img_word;
        public ViewHolder(View view) {
            super(view);

            text_word = (TextView)view.findViewById(R.id.grid_text);
            img_word = (ImageView) view.findViewById(R.id.grid_image);
        }
    }

}
