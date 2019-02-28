package it.efekt.alice.modules;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.efekt.alice.exceptions.MinecraftServerNotFoundException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MinecraftServerStatus {
    private final String API_URL = "https://mcapi.us/server/status?ip=";
    private final String FAVICON_URL = "https://eu.mc-api.net/v3/server/favicon/";
    private int currentPlayers = 0;
    private int maxPlayers = 0;
    private boolean isOnline = false;
    private String motd;
    private String favicon;
    private String name;

    public int getCurrentPlayers() {
        return currentPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public String getName(){
        return this.name;
    }
    public String getFaviconUrl(){
        return this.favicon;
    }

    public String getMotd(){
        return this.motd;
    }

    public void loadServer(String serverAddress) throws MinecraftServerNotFoundException{
        JsonObject jsonObject = getServerStatus(serverAddress);

        if (jsonObject == null || !jsonObject.get("status").getAsString().equalsIgnoreCase("success")){
            throw new MinecraftServerNotFoundException("Minecraft server not found");
        }

        this.maxPlayers = jsonObject.get("players").getAsJsonObject().get("max").getAsInt();
        this.currentPlayers = jsonObject.get("players").getAsJsonObject().get("now").getAsInt();
        this.isOnline = jsonObject.get("online").getAsBoolean();
        this.motd = jsonObject.get("motd").getAsString();
        this.favicon = this.FAVICON_URL + serverAddress;
        this.name = serverAddress;
    }

    private JsonObject getServerStatus(String serverAddress) {
        try {
            URL url = new URL(API_URL + serverAddress);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");

            if (connection.getResponseCode() != 200){
                throw new IOException(connection.getResponseMessage());
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;

            while((line = reader.readLine()) != null){
                sb.append(line);
            }

            reader.close();
            connection.disconnect();

            return new JsonParser().parse(sb.toString()).getAsJsonObject();

        } catch (IOException exc){
            exc.printStackTrace();
        }


        return null;
    }

}
