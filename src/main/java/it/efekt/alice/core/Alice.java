package it.efekt.alice.core;

import it.efekt.alice.listeners.JoinQuitListener;
import it.efekt.alice.listeners.MessageListener;
import it.efekt.alice.listeners.ReadyListener;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

public class Alice {
    private String authToken = "Mzc4MzM4NDU5ODUwMzc1MTY4.DOaFjQ.WMWNouRe3czjUGCY3dbSTBbSz8w"; // just temporary placeholded @TODO remove it later
    private JDA jda;

    public Alice(){
        try {
            this.init();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    private void init() throws Exception{
        Runtime.getRuntime().addShutdownHook(new ShutdownThread(this));
        this.jda = new JDABuilder(AccountType.BOT).setToken(authToken).addEventListener(new ReadyListener()).buildBlocking();
        this.registerListeners();
    }

    private void registerListeners(){
        this.jda.addEventListener(new MessageListener());
        this.jda.addEventListener(new JoinQuitListener());
    }

    public JDA getJDA(){
        return this.jda;
    }
}


