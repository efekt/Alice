package it.efekt.alice.modules;

import it.efekt.alice.core.AliceBootstrap;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;

public class SpamLevelManager {
    private final double STEP = 100d;
    private final double MULTIPLIER = 1.05d;

    public double getPlayerLevel(User user, Guild guild){
        int playerMessages = AliceBootstrap.alice.getUserStatsManager().getUserStats(user, guild).getMessagesAmount();
            double exp = playerMessages;
            double tempExp = getMaxExpPerLevel(0);
            int i = 0;
            while(tempExp<=exp){
                i++;
                tempExp = tempExp + getMaxExpPerLevel(i);
            }
            return i ;
    }

    // Returns exp required to reach certain level
    public double getMaxExpPerLevel(double lvl){
        return  this.STEP *Math.pow(this.MULTIPLIER, lvl -1d);
    }
}
