package it.efekt.alice.web.model;

public class Status {
    private long ping;
    private long userCount;
    private long serverCount;

    public Status() {
    }

    public Status(long ping, long userCount, long serverCount) {
        this.ping = ping;
        this.userCount = userCount;
        this.serverCount = serverCount;
    }

    public long getPing() {
        return ping;
    }

    public void setPing(long ping) {
        this.ping = ping;
    }

    public long getUserCount() {
        return userCount;
    }

    public void setUserCount(long userCount) {
        this.userCount = userCount;
    }

    public long getServerCount() {
        return serverCount;
    }

    public void setServerCount(long serverCount) {
        this.serverCount = serverCount;
    }
}
