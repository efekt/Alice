package it.efekt.alice.commands.util;

import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.db.UserStats;
import it.efekt.alice.modules.SpamLevelManager;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.entities.Message;

import java.time.format.DateTimeFormatter;

public class UserInfoCmd extends Command {

    public UserInfoCmd(String alias) {
        super(alias);
        setCategory(CommandCategory.UTILS);
        setDescription("Wyświetla informacje o użytkowniku");
        setUsageInfo(" `@user`");
    }

    @Override
    public void onCommand(MessageReceivedEvent e) {
        Message msg = e.getMessage();

        if (getArgs().length == 1 && !msg.getMentionedUsers().isEmpty()){
            User user = msg.getMentionedUsers().stream().findFirst().get();

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Informacje o użytkowniku " + user.getName());
            embedBuilder.setThumbnail(user.getEffectiveAvatarUrl());
            embedBuilder.setColor(AliceBootstrap.EMBED_COLOR);

            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");

            embedBuilder.addField("Data utworzenia konta", user.getCreationTime().format(dateFormat), false);
            UserStats userStats = AliceBootstrap.alice.getUserStatsManager().getUserStats(user, e.getGuild());
            embedBuilder.addField("Spamerski level", String.valueOf((int)new SpamLevelManager().getPlayerLevel(user, e.getGuild())), false);
            embedBuilder.addField("Wysłanych wiadomości", String.valueOf(userStats.getMessagesAmount()), false);

            e.getChannel().sendMessage(embedBuilder.build()).queue();
        }
    }

}
