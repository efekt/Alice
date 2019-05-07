package it.efekt.alice.rest.model;

public class Status {
    private long ping;
    private long serverCount;
    private long userCount;

    public Status(){

    }

    public Status(long ping, long serverCount, long userCount) {
        this.ping = ping;
        this.serverCount = serverCount;
        this.userCount = userCount;
    }

    public long getPing() {
        return ping;
    }

    public void setPing(long ping) {
        this.ping = ping;
    }

    public long getServerCount() {
        return serverCount;
    }

    public void setServerCount(long serverCount) {
        this.serverCount = serverCount;
    }

    public long getUserCount() {
        return userCount;
    }

    public void setUserCount(long userCount) {
        this.userCount = userCount;
    }
}
