package it.efekt.alice.commands.admin;

import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.core.Alice;
import it.efekt.alice.core.AliceBootstrap;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import java.text.NumberFormat;

public class StatsCmd extends Command {
    public StatsCmd(String alias) {
        super(alias);
        setCategory(CommandCategory.BOT_ADMIN);
        setIsAdminCommand(true);
        setIsVoteRequired(true);
    }

    @Override
    public boolean onCommand(MessageReceivedEvent e) {
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
        sb.append("Currently playing: " + alice.getAliceAudioManager().getCurrentPlaybackCount());
        sb.append("\nCurrently paused: " + alice.getAliceAudioManager().getCurrentPausedCount());

        builder.addField("Memory usage", sb.toString(), false);

        e.getTextChannel().sendMessage(builder.build()).complete();
        return true;
    }
}
