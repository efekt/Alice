package it.efekt.alice.modules;

import it.efekt.alice.core.AliceBootstrap;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;

public class SpamLevelManager {
    private double step = 100d;
    private double multiplier = 1.05d;

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

    // Zwraca exp wymagany do wbicia levelu (wzglednie od ostatniego)
    public double getMaxExpPerLevel(double lvl){
        return  this.step*Math.pow(this.multiplier, lvl -1d);
    }
}
