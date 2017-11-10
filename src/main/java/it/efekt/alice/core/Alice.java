package it.efekt.alice.core;

import it.efekt.alice.listeners.MessageListener;
import it.efekt.alice.listeners.ReadyListener;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

public class Alice {
    private String authToken = "Mzc4MzM4NDU5ODUwMzc1MTY4.DOaFjQ.WMWNouRe3czjUGCY3dbSTBbSz8w"; // just temporary placeholded @TODO remove it later

    public Alice(){
        try {
            this.init();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    private void init() throws Exception{
        JDA jda = new JDABuilder(AccountType.BOT).setToken(authToken).addEventListener(new ReadyListener()).buildBlocking();
        jda.setAutoReconnect(true);
        jda.addEventListener(new MessageListener());
    }
}


