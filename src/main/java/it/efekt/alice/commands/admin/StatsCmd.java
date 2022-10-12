package it.efekt.alice.commands.admin;

import it.efekt.alice.commands.core.CombinedCommandEvent;
import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.core.Alice;
import it.efekt.alice.core.AliceBootstrap;
import net.dv8tion.jda.api.EmbedBuilder;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class StatsCmd extends Command {
    public StatsCmd(String alias) {
        super(alias);
        setCategory(CommandCategory.BOT_ADMIN);
        setIsAdminCommand(true);
        setIsVoteRequired(true);

        //setSlashCommand();
    }

    @Override
    public boolean onCommand(CombinedCommandEvent e) {
        Alice alice = AliceBootstrap.alice;
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Alice - Memory Usage Stats");

        Runtime runtime = Runtime.getRuntime();

        NumberFormat format = NumberFormat.getInstance();

        StringBuilder sb = new StringBuilder();
        long maxMemory = runtime.maxMemory() / 1024 / 1024;
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long currentUsage = (totalMemory / 1024 / 1024) - (freeMemory / 1024 / 1024);

        sb.append("Current Usage: " + format.format(currentUsage)  + " / " + format.format(maxMemory) + " MB\n");
        sb.append("Connected audio channels: " + alice.getAliceAudioManager().getCurrentAudioChannelJoinedCount() + "\n");
        sb.append("Currently playing: " + alice.getAliceAudioManager().getCurrentPlaybackCount() + " (" + alice.getAliceAudioManager().getCurrentPausedCount() + " paused)");

        builder.addField("Memory usage", sb.toString(), false);

        StringBuilder globalBuilder = new StringBuilder();

        Date startupTime = new Date(AliceBootstrap.STARTUP_TIME);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

        long secondsTotal = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - AliceBootstrap.STARTUP_TIME);

        int days = (int) TimeUnit.SECONDS.toDays(secondsTotal);
        long hours = TimeUnit.SECONDS.toHours(secondsTotal) - TimeUnit.DAYS.toHours(days);
        long minutes = TimeUnit.SECONDS.toMinutes(secondsTotal) -
                TimeUnit.DAYS.toMinutes(days) -
                TimeUnit.HOURS.toMinutes(hours);
        long seconds = TimeUnit.SECONDS.toSeconds(secondsTotal) -
                TimeUnit.DAYS.toSeconds(days) -
                TimeUnit.HOURS.toSeconds(hours) -
                TimeUnit.MINUTES.toSeconds(minutes);

        builder.addField("Current threads", String.valueOf(Thread.activeCount()), false);
        builder.addField("Uptime", days + "d " + hours + "h " + minutes + "m " + seconds + "s ", true);
        builder.addField("Startup time", df.format(startupTime), true);
        e.sendEmbeddedMessageToChannel(builder.build());
        return true;
    }
}
