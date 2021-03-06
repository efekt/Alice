package it.efekt.alice.commands.util;

import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.db.model.UserStats;
import it.efekt.alice.lang.AMessage;
import it.efekt.alice.modules.SpamLevelManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.time.format.DateTimeFormatter;

public class UserInfoCmd extends Command {

    public UserInfoCmd(String alias) {
        super(alias);
        setCategory(CommandCategory.UTILS);
        setDescription(AMessage.CMD_USERINFO_DESC);
        setShortUsageInfo(AMessage.CMD_USERINFO_USAGE_INFO);
    }

    @Override
    public boolean onCommand(MessageReceivedEvent e) {
        Message msg = e.getMessage();

        if (getArgs().length >= 1 && !msg.getMentionedUsers().isEmpty()){
            User user = msg.getMentionedUsers().stream().findFirst().get();
            showInfo(e, user);
            return true;
        } else {
            showInfo(e, e.getAuthor());
            return true;
        }
    }

    private void showInfo(MessageReceivedEvent e, User user){
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(AMessage.CMD_USERINFO_TITLE.get(e,  user.getName()));
        embedBuilder.setThumbnail(user.getEffectiveAvatarUrl());
        embedBuilder.setColor(AliceBootstrap.EMBED_COLOR);

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");

        embedBuilder.addField(AMessage.CMD_USERINFO_ACCOUNT_CREATED.get(e), user.getTimeCreated().format(dateFormat), false);
        UserStats userStats = AliceBootstrap.alice.getUserStatsManager().getUserStats(user, e.getGuild());
        if (!user.isBot()) {
            embedBuilder.addField(AMessage.CMD_USERINFO_SPAM_LVL.get(e), String.valueOf((int) new SpamLevelManager().getPlayerLevel(user, e.getGuild())), false);
            embedBuilder.addField(AMessage.CMD_USERINFO_MSGS_SENT.get(e), String.valueOf(userStats.getMessagesAmount()), false);
        }
        e.getChannel().sendMessage(embedBuilder.build()).complete();
    }

}
