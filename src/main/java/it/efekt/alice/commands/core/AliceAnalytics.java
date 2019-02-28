package it.efekt.alice.commands.core;

import com.brsanthu.googleanalytics.GoogleAnalytics;
import it.efekt.alice.core.AliceBootstrap;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;


public class AliceAnalytics {
    public GoogleAnalytics ga;

    public AliceAnalytics(){
        ga = GoogleAnalytics.builder()
                .withTrackingId(AliceBootstrap.alice.getConfig().getGoogleAnalyticsId())
                .build();

    }

    public void reportCmdUsage(String alias, String args, Guild guild, User user) {
        ga.screenView().sessionControl("start").sendAsync();
        ga.pageView()
                .documentTitle(args.toLowerCase())
                .documentPath("/commands/" + alias.toLowerCase())
                .campaignSource(guild.getId())
                .campaignMedium("Discord Server")
                .clientId(user.getId())
                .userId(user.getId())
                .sendAsync();

        ga.event().clientId(user.getId()).userId(user.getId()).eventCategory("command").eventAction(alias.toLowerCase()).eventLabel(args.toLowerCase()).sendAsync();

        ga.screenView().sessionControl("end").sendAsync();
    }
}
