package it.efekt.alice.core;

import it.efekt.alice.commands.HelpCmd;
import it.efekt.alice.commands.PingCmd;
import it.efekt.alice.commands.PrefixCmd;
import it.efekt.alice.commands.TomekCmd;
import it.efekt.alice.commands.core.CommandManager;
import it.efekt.alice.listeners.JoinQuitListener;
import it.efekt.alice.listeners.ReadyListener;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

import javax.security.auth.login.LoginException;

public class Alice {
    private JDA jda;
    private Config config;
    private CommandManager cmdManager;
    private GuildConfigManager guildConfigManager;

    public Alice(Config config){
            this.config = config;
            this.init();
    }

    private void registerListeners(){
        this.jda.addEventListener(new JoinQuitListener());
    }

    private void registerCommands(){
        this.getCmdManager().setExecutor(new PingCmd("ping"));
        this.getCmdManager().setExecutor(new HelpCmd("help"));
        this.getCmdManager().setExecutor(new PrefixCmd("prefix"));
        this.getCmdManager().setExecutor(new TomekCmd("tomek"));
    }


    public JDA getJDA(){
        return this.jda;
    }

    public Config getConfig(){
        return this.config;
    }

    public CommandManager getCmdManager(){
        return this.cmdManager;
    }

    public GuildConfigManager getGuildConfigManager(){
        return this.guildConfigManager;
    }

    private void init(){
        //Runtime.getRuntime().addShutdownHook(new ShutdownThread(this));
        try {
            this.jda = new JDABuilder(AccountType.BOT).setToken(this.getConfig().getToken()).addEventListener(new ReadyListener()).build();
            this.guildConfigManager = new GuildConfigManager(this);
            this.registerListeners();
            this.cmdManager = new CommandManager(this);
            this.registerCommands();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }
}


