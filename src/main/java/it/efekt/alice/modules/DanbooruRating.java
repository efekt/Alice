package it.efekt.alice.modules;

public enum DanbooruRating {
    SAFE("s"),
    QUESTIONABLE("q"),
    EXPLICIT("e");


    private String name;

    DanbooruRating(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
}
