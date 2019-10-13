package it.efekt.alice.commands.util;

import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.db.model.UserStats;
import it.efekt.alice.modules.SpamLevelManager;
import it.efekt.alice.db.UserStatsManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TopCmd extends Command {
    private final int MAX_PER_PAGE = 15;
    public TopCmd(String alias) {
        super(alias);
        setCategory(CommandCategory.FUN);
        setDescription(it.efekt.alice.lang.Message.CMD_TOP_DESC);
        setShortUsageInfo(it.efekt.alice.lang.Message.CMD_TOP_USAGE_INFO);
        setFullUsageInfo(it.efekt.alice.lang.Message.CMD_TOP_FULL_USAGE_INFO);
    }

    @Override
    public boolean onCommand(MessageReceivedEvent e) {

       Guild guild = e.getGuild();
       UserStatsManager userStatsManager = AliceBootstrap.alice.getUserStatsManager();
       List<UserStats> userStatsList = userStatsManager.getUserStats(guild);
       userStatsList.removeIf(UserStats::isInvalidUser);
       userStatsList.removeIf(UserStats::isBot);
       userStatsList.sort(Comparator.comparing(UserStats::getMessagesAmount).reversed());

       int page = 1;

       if (getArgs().length == 1 && getArgs()[0].matches("-?\\d+")) {
           page = Integer.parseInt(getArgs()[0]);
       }

       if (userStatsList.isEmpty()){
           e.getChannel().sendMessage(it.efekt.alice.lang.Message.CMD_TOP_NOTHING_FOUND.get(e)).complete();
           return true;
       }

       int beginIndex = (page - 1) * MAX_PER_PAGE;
       int maxPages = (int) Math.ceil((float)userStatsList.size() / (float)MAX_PER_PAGE);

       if (page <= 0 || userStatsList.size() < beginIndex){
           e.getChannel().sendMessage(it.efekt.alice.lang.Message.CMD_TOP_WRONG_PAGE.get(e, String.valueOf(maxPages))).complete();
           return true;
       }

       List<UserStats> subList = userStatsList.subList(beginIndex, Math.min(beginIndex + MAX_PER_PAGE, userStatsList.size()));

       printTop(beginIndex, page, maxPages, subList, e);

    return true;
    }

    private void printTop(int startIndex, int page,int maxPages, List<UserStats> userStatsList, MessageReceivedEvent e){
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(it.efekt.alice.lang.Message.CMD_TOP_LIST_TITLE.get(e, e.getGuild().getName()));
        embedBuilder.setColor(AliceBootstrap.EMBED_COLOR);
        embedBuilder.setFooter(it.efekt.alice.lang.Message.CMD_GAMESTATS_PAGE.get(e, String.valueOf(page), String.valueOf(maxPages)), e.getJDA().getSelfUser().getEffectiveAvatarUrl());

        SpamLevelManager spamLevelManager = new SpamLevelManager();

        String list = "";
        int index = startIndex;
        for (UserStats userStats : userStatsList){
            index++;

            int level = (int) spamLevelManager.getPlayerLevel(AliceBootstrap.alice.getShardManager().getUserById(userStats.getUserId()), e.getGuild());
            list = list.concat("**"+index+".** **"+ AliceBootstrap.alice.getShardManager().getUserById(userStats.getUserId()).getName()+ "** - "+ userStats.getMessagesAmount() + " _("+level+")_" +"\n");

            if (startIndex >= MAX_PER_PAGE * page){
                break;
            }

        }
        embedBuilder.addField("TOP-"+index, list, false);

        e.getChannel().sendMessage(embedBuilder.build()).complete();
    }

    private List<Message> getAllTextMessagesOnGuild(Guild guild){
        List<Message> messages = new ArrayList<>();
        for (TextChannel channel : guild.getTextChannels()){
            for (Message message : channel.getIterableHistory().cache(false)){
                messages.add(message);
            }
        }
        return messages;
    }

}
