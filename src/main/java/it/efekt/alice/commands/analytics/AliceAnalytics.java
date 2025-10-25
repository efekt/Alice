package it.efekt.alice.commands.analytics;

import com.posthog.server.PostHog;
import com.posthog.server.PostHogCaptureOptions;
import com.posthog.server.PostHogConfig;
import com.posthog.server.PostHogInterface;
import it.efekt.alice.commands.analytics.gameAnalytics.EventCategory;
import it.efekt.alice.config.Config;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;
import java.util.zip.GZIPOutputStream;

public class AliceAnalytics {

    private Config config;
    public PostHogInterface postHog;

    public AliceAnalytics(Config config){
        this.config = config;

        String posthogHost = "https://eu.i.posthog.com";


        PostHogConfig postHogConfig = com.posthog.server.PostHogConfig
                .builder(config.getPostHogApiKey())
                .host(posthogHost)
                .build();
        postHog = PostHog.with(postHogConfig);
    }


    public void reportCmdUsage(String alias, String args, Guild guild, User user) {
        try {
            sendDesignEvent("command:" + alias, "user_" + user.getId(), UUID.randomUUID().toString());
            postHog.capture(
                    "user_" + user.getId(),
                    "user_command_used",
                    PostHogCaptureOptions.builder()
                        .property("command_alias", alias)
                        .property("command_args", args)
                        .property("guild_id", guild.getId())
                        .build()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void reportGuildJoin(Guild guild){
        try {
            sendDesignEvent("guild:join", "guild_" + guild.getId(), UUID.randomUUID().toString());
            postHog.capture(
                    "guild_" + guild.getId(),
                    "guild_joined",
                    PostHogCaptureOptions.builder()
                            .property("guild_owner_id", guild.getOwnerId())
                            .property("guild_name", guild.getName())
                            .property("guild_member_count", guild.getMemberCount())
                            .property("guild_locale", guild.getLocale().name())
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void reportGuildLeave(Guild guild){
        try {
            sendDesignEvent("guild:leave", "guild_" + guild.getId(), UUID.randomUUID().toString());
            postHog.capture(
                    "guild_" + guild.getId(),
                    "guild_left"
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void sendDesignEvent(String eventId, String userId, String sessionId) throws Exception {
        JSONObject eventJson = getDesignEvent(userId, sessionId, eventId, 1);

        JSONArray events = new JSONArray();
        events.put(eventJson);

        sendEvent(events.toString());
    }


    private void sendEvent(String jsonBody) throws Exception {
        String BASE_URL = "https://api.gameanalytics.com/v2/";

        byte[] gzippedBody = gzipCompress(jsonBody);
        String authorization = calculateHMAC(gzippedBody, config.getGameAnalyticsApiKey());
        String url = BASE_URL + config.getGameAnalyticsGameKey() + "/events";

        HttpResponse<String> response = Unirest.post(url)
                .header("Authorization", authorization)
                .header("Content-Type", "application/json")
                .header("Content-Encoding", "gzip")
                .body(gzippedBody)
                .asString();

        if (response.getStatus() >= 200 && response.getStatus() < 300) {
            System.out.println("GameAnalytics event sent successfully");
        } else {
            System.out.printf("GameAnalytics failed: {%s} - {%s}", response.getStatus(), response.getBody());
        }
    }

    private static String calculateHMAC(byte[] data, String secretKey) throws Exception {
        Mac hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        hmac.init(keySpec);
        byte[] hmacBytes = hmac.doFinal(data);
        return Base64.getEncoder().encodeToString(hmacBytes);
    }

    private static byte[] gzipCompress(String data) throws Exception {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try (GZIPOutputStream gzipStream = new GZIPOutputStream(byteStream)) {
            gzipStream.write(data.getBytes(StandardCharsets.UTF_8));
        }
        return byteStream.toByteArray();
    }

    public JSONObject getDesignEvent(String userId, String sessionId, String event_id, float amount)
    {
        JSONObject event = getBaseEventJson(userId, sessionId);
        event.put("category", EventCategory.DESIGN.toString().toLowerCase());
        event.put("event_id", event_id);
        event.put("amount", amount);
        return event;
    }

    private JSONObject getBaseEventJson(String userId, String sessionId)
    {
        JSONObject event = new JSONObject();
        event.put("platform", "server");
        event.put("os_version", "linux 1.0");
        event.put("sdk_version", "rest api v2"); // from docs: Custom solutions should ALWAYS use the string “rest api v2”
        event.put("device", "unknown");
        event.put("manufacturer", "unknown");
        event.put("build", "prod"); // Optional but it is good practice to send the build type, check in dashboard if "dev" or "prod" was saved
        event.put("user_id", userId);
        event.put("session_id", sessionId);
        event.put("session_num", 1);
        event.put("v", 2);
        event.put("client_ts",System.currentTimeMillis() / 1000L); // Optional but it is good practice to send the timestamp
        return event;
    }

}
