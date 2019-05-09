package it.efekt.alice.web;
import com.google.gson.Gson;
import it.efekt.alice.core.Alice;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.db.GuildConfig;
import it.efekt.alice.web.model.StandardResponse;
import it.efekt.alice.web.model.Status;
import it.efekt.alice.web.model.StatusResponse;
import spark.Spark;

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
         status.setPing(alice.getJDA().getPing());
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
         alice.getGuildConfigManager().updateConfig(newConfig);

         return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(newConfig)));
      });

   }

   private String url(String url){
      return BASE_URL.concat(url);
   }


}
