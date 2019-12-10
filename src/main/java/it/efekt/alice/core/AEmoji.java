package it.efekt.alice.core;

public enum AEmoji {
    HEART("❤️");


    private String code;

    AEmoji(String code){
        this.code = code;
    }


    public String get(){
        return this.code;
    }
}
