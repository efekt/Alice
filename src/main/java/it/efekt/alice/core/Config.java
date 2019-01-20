package it.efekt.alice.core;

import static java.lang.String.format;

public class Config {
    private String token;

    public String getToken(){
        return this.token;
    }

    public void setToken(String token){
        this.token = token;
    }

    @Override
    public String toString(){
        return new StringBuilder()
                .append(format("token: %s\n", token)).toString();
    }
}
