package it.efekt.alice.rest.model;

public class Status {
    private long ping;

    public Status(){

    }

    public Status(long ping){
        this.ping = ping;
    }

    public long getPing() {
        return ping;
    }

    public void setPing(long ping) {
        this.ping = ping;
    }
}
