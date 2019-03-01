package it.efekt.alice.commands.util;

import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.db.UserStats;
import it.efekt.alice.modules.SpamLevelManager;
import it.efekt.alice.modules.UserStatsManager;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TopCmd extends Command {
    public TopCmd(String alias) {
        super(alias);
        setCategory(CommandCategory.UTILS);
        setDescription(it.efekt.alice.lang.Message.CMD_TOP_DESC);
        setShortUsageInfo(it.efekt.alice.lang.Message.CMD_TOP_USAGE_INFO);
        setFullUsageInfo(it.efekt.alice.lang.Message.CMD_TOP_FULL_USAGE_INFO);
    }

    @Override
    public boolean onCommand(MessageReceivedEvent e) {

       Guild guild = e.getGuild();
       UserStatsManager userStatsManager = AliceBootstrap.alice.getUserStatsManager();
       List<UserStats> userStatsList = userStatsManager.getUserStats(guild);
       System.out.println(userStatsList.size());
       userStatsList.removeIf(UserStats::isInvalidUser);
       userStatsList.removeIf(UserStats::isBot);
       userStatsList.sort(Comparator.comparing(UserStats::getMessagesAmount).reversed());

       if (getArgs().length == 1){
           if (getArgs()[0].matches("-?\\d+")) {
               int listLength = Integer.parseInt(getArgs()[0]);

               if (listLength <= 0){
                   e.getChannel().sendMessage(it.efekt.alice.lang.Message.CMD_TOP_MINUS_TOP.get(e)).queue();
                   return true;
               }

               if (userStatsList.size() < listLength){
                   e.getChannel().sendMessage(it.efekt.alice.lang.Message.CMD_TOP_NUMBER_TOO_LARGE.get(e, String.valueOf(userStatsList.size()))).queue();
                   return true;
               }
               printTop(listLength, userStatsList, e);
               return true;
           }

           if (getArgs()[0].equalsIgnoreCase("loadAll")) {
               if (!e.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                   e.getChannel().sendMessage(it.efekt.alice.lang.Message.CMD_TOP_LOADALL_FORBIDDEN.get(e));
                   return true;
               }

               userStatsManager.removeAll(e.getGuild());

                   e.getChannel().sendMessage(it.efekt.alice.lang.Message.CMD_TOP_LOADALL_WARNING.get(e)).complete();
                   List<Message> allMessages = getAllTextMessagesOnGuild(e.getGuild());

                   e.getChannel().sendMessage(it.efekt.alice.lang.Message.CMD_TOP_LOADALL_FOUND.get(e, String.valueOf(allMessages.size()))).complete();

                   allMessages.forEach(message -> userStatsManager.getUserStats(message.getAuthor(), message.getGuild()).addMessagesAmount(1));
                   e.getChannel().sendMessage(it.efekt.alice.lang.Message.CMD_TOP_LOADALL_FINISHED.get(e)).complete();

                   userStatsManager.removeAllInvalidUsers();
                   userStatsManager.saveAllUserStats();

                   e.getChannel().sendMessage(it.efekt.alice.lang.Message.CMD_TO_LOADALL_SAVED.get(e)).complete();
               return true;

               }
       } else if (getArgs().length == 0){
           int listLength = userStatsList.size();

           if (listLength > 5){
               listLength = 5;
           }

           printTop(listLength, userStatsList, e);
           return true;
       }
    return false;
    }

    private void printTop(int maxUserAmount, List<UserStats> userStatsList, MessageReceivedEvent e){
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(it.efekt.alice.lang.Message.CMD_TOP_LIST_TITLE.get(e, e.getGuild().getName()));
        embedBuilder.setColor(AliceBootstrap.EMBED_COLOR);

        SpamLevelManager spamLevelManager = new SpamLevelManager();

        String list = "";
        for (int i=0; i < maxUserAmount; i++){
            int index = i+1;
            int level = (int) spamLevelManager.getPlayerLevel(AliceBootstrap.alice.getJDA().getUserById(userStatsList.get(i).getUserId()), e.getGuild());
            list = list.concat("**"+index+".** **"+ AliceBootstrap.alice.getJDA().getUserById(userStatsList.get(i).getUserId()).getName()+ "** - "+ userStatsList.get(i).getMessagesAmount() + " _("+level+")_" +"\n");
        }
        embedBuilder.addField("TOP-"+maxUserAmount, list, false);

        e.getChannel().sendMessage(embedBuilder.build()).queue();
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
