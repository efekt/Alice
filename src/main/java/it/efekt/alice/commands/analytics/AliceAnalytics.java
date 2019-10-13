package it.efekt.alice.commands.analytics;

import com.brsanthu.googleanalytics.GoogleAnalytics;
import it.efekt.alice.config.Config;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

public class AliceAnalytics {
    private GoogleAnalytics ga;

    public AliceAnalytics(Config config){
        this.ga = GoogleAnalytics.builder().withTrackingId(config.getGoogleAnalyticsId()).build();
    }

    public void reportCmdUsage(String alias, String args, Guild guild, User user) {
        this.ga.screenView().sessionControl("start").sendAsync();
        this.ga.pageView()
                .documentTitle(args.toLowerCase())
                .documentPath("/commands/" + alias.toLowerCase())
                .campaignSource(guild.getId())
                .campaignMedium("Discord Server")
                .clientId(user.getId())
                .userId(user.getId())
                .sendAsync();

        this.ga.event().clientId(user.getId()).userId(user.getId()).eventCategory("command").eventAction(alias.toLowerCase()).eventLabel(args.toLowerCase()).sendAsync();
        this.ga.screenView().sessionControl("end").sendAsync();
    }

    public void reportGuildJoin(Guild guild){
        this.ga.event().eventCategory("guild").eventAction("join").campaignSource(guild.getId()).eventLabel(guild.getName()).eventValue(guild.getMembers().size()).sendAsync();
    }

    public void reportGuildLeave(Guild guild){
        this.ga.event().eventCategory("guild").eventAction("leave").campaignSource(guild.getId()).eventLabel(guild.getName()).eventValue(-guild.getMembers().size()).sendAsync();
    }
}
