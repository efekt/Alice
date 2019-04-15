package it.efekt.alice.web;
import static spark.Spark.*;

public class AliceWebServer {

    public AliceWebServer(){
        init();
    }

    private void init(){
        get("/alice", (req, res) -> "siemanko!");
    }



}
