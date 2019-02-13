package it.efekt.alice.core;

import it.efekt.alice.commands.HelpCmd;
import it.efekt.alice.commands.util.*;
import it.efekt.alice.commands.admin.StatusCmd;
import it.efekt.alice.commands.admin.StopCmd;
import it.efekt.alice.commands.fun.AsunaCmd;
import it.efekt.alice.commands.fun.KojimaCmd;
import it.efekt.alice.commands.fun.TomekCmd;
import it.efekt.alice.commands.core.CommandManager;
import it.efekt.alice.commands.fun.nsfw.HentaiCmd;
import it.efekt.alice.commands.fun.nsfw.NekoCmd;
import it.efekt.alice.commands.mentions.Greetings;
import it.efekt.alice.listeners.JoinQuitListener;
import it.efekt.alice.listeners.MessageListener;
import it.efekt.alice.listeners.ReadyListener;
import it.efekt.alice.modules.GuildLogger;
import it.efekt.alice.modules.UserStatsManager;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

import javax.security.auth.login.LoginException;

public class Alice {
    private JDA jda;
    private Config config;
    private CommandManager cmdManager;
    private GuildConfigManager guildConfigManager;
    private GuildLogger guildLogger;
    private UserStatsManager userStatsManager;

    public Alice(Config config){
            this.config = config;
            this.init();
    }

    private void registerListeners(){
        this.jda.addEventListener(new JoinQuitListener());
        this.jda.addEventListener(new Greetings());
        this.jda.addEventListener(new MessageListener());
        this.guildLogger = new GuildLogger(this);
        this.jda.addEventListener(guildLogger);
    }

    private void registerCommands(){
        getCmdManager().setExecutor(new PingCmd("ping"));
        getCmdManager().setExecutor(new HelpCmd("help"));
        getCmdManager().setExecutor(new PrefixCmd("prefix"));
        getCmdManager().setExecutor(new TomekCmd("tomek"));
        getCmdManager().setExecutor(new AsunaCmd("asuna"));
        getCmdManager().setExecutor(new NekoCmd("neko"));
        getCmdManager().setExecutor(new KojimaCmd("kojima"));
        getCmdManager().setExecutor(new HentaiCmd("h"));
        getCmdManager().setExecutor(new StopCmd("stop"));
        getCmdManager().setExecutor(new StatusCmd("status"));
        getCmdManager().setExecutor(new HistoryDeletionCmd("clean"));
        getCmdManager().setExecutor(new GuildLoggerCmd("logger"));
        getCmdManager().setExecutor(new UserInfoCmd("info"));
        getCmdManager().setExecutor(new TopCmd("top"));
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

    public GuildLogger getGuildLogger(){
        return this.guildLogger;
    }

    public UserStatsManager getUserStatsManager() {
        return userStatsManager;
    }

    private void init(){
        Runtime.getRuntime().addShutdownHook(new ShutdownThread(this));
        try {
            this.jda = new JDABuilder(AccountType.BOT).setToken(this.getConfig().getToken()).addEventListener(new ReadyListener()).build();
            this.guildConfigManager = new GuildConfigManager(this);
            registerListeners();
            this.cmdManager = new CommandManager(this);
            this.userStatsManager = new UserStatsManager(this);
            registerCommands();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }
}


