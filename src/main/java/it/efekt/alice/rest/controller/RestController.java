package it.efekt.alice.rest.controller;

import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.db.GuildConfig;
import it.efekt.alice.rest.model.Status;
import net.dv8tion.jda.core.JDA;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api/v1")
public class RestController {
    private JDA jda = AliceBootstrap.alice.getJDA();

    @RequestMapping("/status")
    public Status status(){
        Status status = new Status();
        status.setPing(this.jda.getPing());
        status.setServerCount(this.jda.getGuilds().size());
        status.setUserCount(this.jda.getUsers().size());

        return status;
    }

    @RequestMapping("/guild/{guildId}/config")
    @ResponseBody
    public GuildConfig guildConfig(@PathVariable("guildId") long id, HttpServletResponse res) {
        try {
            return AliceBootstrap.alice.getGuildConfigManager().getGuildConfig(jda.getGuildById(id));
        } catch(NullPointerException exc){
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
    }

    @PostMapping("/guild/{guildId}/config")
    public GuildConfig setConfig(@PathVariable("guildId") long id, @RequestParam("param") String param, @RequestParam("value") String value){
        GuildConfig config = AliceBootstrap.alice.getGuildConfigManager().getGuildConfig(jda.getGuildById(id));

        switch(param){
            case "prefix":
                config.setPrefix(value);
                break;
            case "logChannel":
                config.setLogChannel(value);
                break;
            case "locale":
                config.setLocale(value);
                break;
            case "disabledFeatures":
                config.setDisabledFeatures(value);
                break;
            default: break;
        }
        config.save();
        return config;
    }
}
