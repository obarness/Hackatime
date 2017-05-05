package com.example.hacktime.pocketwords;

/**
 * Created by okomlosh on 5/4/2017.
 */

public class WordCard {

    private String word_text;
    private String word_image_url;
    private String word_id;

    public String getWord_text() {
        return word_text;
    }

    public void setWord_text(String word_string) {
        this.word_text = word_string;
    }

    public String getWord_image_url() {
        return word_image_url;
    }

    public void setWord_image_url(String word_image_url) {
        this.word_image_url = word_image_url;
    }

    public String getWord_id(){
        return word_id;
    }

    public void setWord_id(String word_id){
        this.word_id = word_id;
    }
}
