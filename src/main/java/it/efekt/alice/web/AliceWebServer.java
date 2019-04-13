package it.efekt.alice.web;

import io.undertow.Undertow;
import io.undertow.util.Headers;

public class AliceWebServer {
    private final int PORT = 8080;
    private Undertow server;

    public AliceWebServer(){
        init();
    }

    private void init(){
        this.server = Undertow.builder().addHttpListener(PORT,
                "localhost").setHandler(exchange -> {
            exchange.getResponseHeaders()
                    .put(Headers.CONTENT_TYPE, "text/plain");
            exchange.getResponseSender().send("Hello Alice");
        }).build();
        server.start();
    }

}
