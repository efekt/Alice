package it.efekt.alice.core;

import org.apache.commons.io.IOUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.lang.String.format;

public class Config {
    private String token;
    private String prefix;

    public String getPrefix(){
        return this.prefix;
    }

    public void setPrefix(String prefix){
        this.prefix = prefix;
    }

    public String getToken(){
        return this.token;
    }

    public void setToken(String token){
        this.token = token;
    }

    @Override
    public String toString(){
        return new StringBuilder()
                .append(format("token: %s\n", token))
                .append(format("prefix: %s\n", prefix)).toString();
    }
}
