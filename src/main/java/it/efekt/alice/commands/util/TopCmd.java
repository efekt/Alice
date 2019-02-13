package it.efekt.alice.commands.util;

import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.db.UserStats;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Comparator;
import java.util.List;

public class TopCmd extends Command {
    public TopCmd(String alias) {
        super(alias);
        setCategory(CommandCategory.UTILS);
        setDescription("Wyświetla listę największych spamerów \n `liczba użytowników` - opcjonalne");
        setUsageInfo( " `liczba użytkowników`");
    }

    @Override
    public void onCommand(MessageReceivedEvent e) {
        Guild guild = e.getGuild();

       List<UserStats> userStatsList = AliceBootstrap.alice.getUserStatsManager().getUserStats(guild);
       userStatsList.sort(Comparator.comparing(UserStats::getMessagesAmount).reversed());

       if (getArgs().length == 1){
           if (getArgs()[0].matches("-?\\d+")) {
               int listLength = Integer.parseInt(getArgs()[0]);

               if (listLength <= 0){
                   e.getChannel().sendMessage("oszalałeś...?").queue();
                   return;
               }

               if (userStatsList.size() < listLength){
                   e.getChannel().sendMessage("Nie znaleziono tylu użytkowników, maksymalnie możesz podać: " + userStatsList.size()).queue();
                   return;
               }
               printTop(listLength, userStatsList, e);

           }
       } else if (getArgs().length == 0){
           int listLength = userStatsList.size();

           if (listLength > 5){
               listLength = 5;
           }

           printTop(listLength, userStatsList, e);


       }

    }

    private void printTop(int maxUserAmount, List<UserStats> userStatsList, MessageReceivedEvent e){
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Lista największych spamerów serwera " + e.getGuild().getName());
        embedBuilder.setColor(AliceBootstrap.EMBED_COLOR);

        String list = "";
        for (int i=0; i < maxUserAmount; i++){
            int index = i+1;
            list = list.concat("`"+index+"` "+ AliceBootstrap.alice.getJDA().getUserById(userStatsList.get(i).getUserId()).getName()+ " - "+ userStatsList.get(i).getMessagesAmount() +"\n");
        }
        embedBuilder.addField("TOP-"+maxUserAmount, list, false);

        e.getChannel().sendMessage(embedBuilder.build()).queue();
    }
}
