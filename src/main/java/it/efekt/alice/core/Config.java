package it.efekt.alice.core;

import static java.lang.String.format;

public class Config {
    private String token;
    private String imgurClientId;

    public void setImgurClientId(String imgurClientId){
        this.imgurClientId = imgurClientId;
    }

    public String getImgurClientId(){
        return this.imgurClientId;
    }

    public void setToken(String token){
        this.token = token;
    }

    public String getToken(){
        return this.token;
    }

    @Override
    public String toString(){
        return new StringBuilder().append(format("token: %s\n", token)).toString();
    }
}
