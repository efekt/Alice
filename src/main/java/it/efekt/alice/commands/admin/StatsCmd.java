package it.efekt.alice.commands.admin;

import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.text.NumberFormat;

public class StatsCmd extends Command {
    public StatsCmd(String alias) {
        super(alias);
        setCategory(CommandCategory.BOT_ADMIN);
        setIsAdminCommand(true);
    }

    @Override
    public boolean onCommand(MessageReceivedEvent e) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Alice - Memory Usage Stats");

        Runtime runtime = Runtime.getRuntime();

        NumberFormat format = NumberFormat.getInstance();

        StringBuilder sb = new StringBuilder();
        long maxMemory = runtime.maxMemory();
        long allocatedMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();

        sb.append("Free: " + format.format(freeMemory / 1024 / 1024) + "\n");
        sb.append("Allocated: " + format.format(allocatedMemory / 1024 / 1024) + "\n");
        sb.append("Max: " + format.format(maxMemory / 1024 / 1024) + "\n");
        sb.append("Total Free: " + format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024 / 1024) + "\n");

        builder.addField("Memory usage", sb.toString(), false);

        e.getTextChannel().sendMessage(builder.build()).complete();
        return true;
    }
}
