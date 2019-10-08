package it.efekt.alice.web;
import com.google.gson.Gson;
import it.efekt.alice.core.Alice;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.db.model.GuildConfig;
import it.efekt.alice.db.model.UserStats;
import it.efekt.alice.web.model.StandardResponse;
import it.efekt.alice.web.model.Status;
import it.efekt.alice.web.model.StatusResponse;
import spark.Spark;
import java.util.List;
import static spark.Spark.*;

public class WebServer {
   private Alice alice = AliceBootstrap.alice;
   private final int PORT = 3214;
   private final String BASE_URL = "/api/v1";

   public WebServer(){
      Spark.port(this.PORT);
      init();
   }

   public void init(){
      before((req, res) ->{
         String method = req.requestMethod();
         String authentication = req.headers(AliceBootstrap.alice.getConfig().getRestUser());
            if (!AliceBootstrap.alice.getConfig().getRestPassword().equals(authentication)){
               Spark.halt(401, "User Unauthorized");
            }
      });

      // BOT STATUS
      get(url("/status"), (req, res) -> {
         res.type("application/json");
         Status status = new Status();
         status.setPing(alice.getJDA().getGatewayPing());
         status.setServerCount(alice.getJDA().getGuilds().size());
         status.setUserCount(alice.getJDA().getUsers().size());
         return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(status)));
      });

      // GET GUILD CONFIG
      get(url("/guild/:id/config/"), (req, res) ->{
         res.type("application/json");
         String guildId = req.params(":id");

         if (!alice.getGuildConfigManager().hasConfig(guildId)){
            return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, "Guild not found"));
         }

         return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(alice.getGuildConfigManager().getGuildConfig(alice.getJDA().getGuildById(guildId)))));
      });

      // REPLACE CONFIG
      post(url("/guild/:id/config/"), (req, res) ->{
         res.type("application/json");
         String guildId = req.params(":id");

         if (!alice.getGuildConfigManager().hasConfig(guildId)){
            return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, "Guild not found"));
         }
         GuildConfig newConfig = new Gson().fromJson(req.body(), GuildConfig.class);
         newConfig.save();

         return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(newConfig)));
      });

      get(url("/userstats/:guildId"), (req, res) ->{
         System.out.println("spam");
         res.type("application/json");
         String guildId = req.params(":guildId");
         List<UserStats> userStats = alice.getUserStatsManager().getUserStats(alice.getJDA().getGuildById(guildId));

         return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(userStats)));
      });

      get(url("/userstats/:guildId/:userId"), (req, res) ->{
         System.out.println("spam");
         res.type("application/json");
         String guildId = req.params(":guildId");
         String userId = req.params(":userId");

         UserStats userStats = alice.getUserStatsManager().getUserStats(alice.getJDA().getUserById(userId), alice.getJDA().getGuildById(guildId));

         return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(userStats)));
      });

   }

   private String url(String url){
      return BASE_URL.concat(url);
   }


}
