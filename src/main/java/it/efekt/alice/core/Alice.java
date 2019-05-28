package it.efekt.alice.core;

import it.efekt.alice.commands.HelpCmd;
import it.efekt.alice.commands.admin.BlacklistReload;
import it.efekt.alice.commands.admin.StatsCmd;
import it.efekt.alice.commands.fun.*;
import it.efekt.alice.commands.games.ApexStatsCmd;
import it.efekt.alice.commands.games.GameStatsCmd;
import it.efekt.alice.commands.games.MinecraftStatusCmd;
import it.efekt.alice.commands.util.*;
import it.efekt.alice.commands.admin.StatusCmd;
import it.efekt.alice.commands.admin.StopCmd;
import it.efekt.alice.commands.core.CommandManager;
import it.efekt.alice.commands.nsfw.HentaiCmd;
import it.efekt.alice.commands.nsfw.NekoCmd;
import it.efekt.alice.commands.voice.*;
import it.efekt.alice.listeners.GameListener;
import it.efekt.alice.modules.*;
import it.efekt.alice.modules.mentions.Greetings;
import it.efekt.alice.config.Config;
import it.efekt.alice.config.GuildConfigManager;
import it.efekt.alice.lang.LanguageManager;
import it.efekt.alice.listeners.JoinQuitListener;
import it.efekt.alice.listeners.MessageListener;
import it.efekt.alice.listeners.ReadyListener;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import javax.security.auth.login.LoginException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Alice {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private JDA jda;
    private Config config;
    private CommandManager cmdManager;
    private GuildConfigManager guildConfigManager;
    private GuildLogger guildLogger;
    private UserStatsManager userStatsManager;
    private GameStatsManager gameStatsManager;
    private LanguageManager languageManager;
    private AliceAudioManager aliceAudioManager;
    private TextChannelConfigManager textChannelConfigManager;

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
        this.jda.addEventListener(new GameListener());
    }

    private void registerCommands(){
        getCmdManager().setExecutor(new PingCmd("ping"));
        getCmdManager().setExecutor(new HelpCmd("help"));
        getCmdManager().setExecutor(new PrefixCmd("prefix"));
        getCmdManager().setExecutor(new TomekCmd("tomek"));
        getCmdManager().setExecutor(new TomaszCmd("tomasz"));
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
        getCmdManager().setExecutor(new ApexStatsCmd("apex"));
        getCmdManager().setExecutor(new MinecraftStatusCmd("mc"));
        getCmdManager().setExecutor(new FeaturesCmd("cmd"));
        getCmdManager().setExecutor(new LangCmd("lang"));
        getCmdManager().setExecutor(new RandomWaifuCmd("random-waifu"));
        getCmdManager().setExecutor(new GameStatsCmd("topgames"));
        getCmdManager().setExecutor(new LoliCmd("loli"));
        getCmdManager().setExecutor(new WikiCmd("wiki"));
        getCmdManager().setExecutor(new JoinCmd("join"));
        getCmdManager().setExecutor(new LeaveCmd("leave"));
        getCmdManager().setExecutor(new PlayCmd("play"));
        getCmdManager().setExecutor(new NowPlayingCmd("np"));
        getCmdManager().setExecutor(new PauseCmd("pause"));
        getCmdManager().setExecutor(new EvalCmd("eval"));
        getCmdManager().setExecutor(new RecordCmd("rec"));
        getCmdManager().setExecutor(new ImgOnlyCmd("img-only"));
        getCmdManager().setExecutor(new StatsCmd("stats"));
        getCmdManager().setExecutor(new BlacklistReload("topgames-blacklist"));
    }

    private void startSchedulers(){
        this.scheduler.scheduleAtFixedRate(new BotStatusRefresher(this), 10, 60, TimeUnit.SECONDS);
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
        return this.userStatsManager;
    }

    public GameStatsManager getGameStatsManager(){
        return this.gameStatsManager;
    }

    public LanguageManager getLanguageManager() {
        return languageManager;
    }

    public AliceAudioManager getAliceAudioManager() {
        return aliceAudioManager;
    }

    public TextChannelConfigManager getTextChannelConfigManager() {
        return textChannelConfigManager;
    }

    private void init(){
        Runtime.getRuntime().addShutdownHook(new ShutdownThread(this));
        try {
            this.jda = new JDABuilder(AccountType.BOT).setToken(this.getConfig().getToken()).addEventListener(new ReadyListener()).build();
            this.getJDA().getPresence().setGame(Game.playing("gathering info..."));
            this.guildConfigManager = new GuildConfigManager(this);
            this.cmdManager = new CommandManager(this);
            this.userStatsManager = new UserStatsManager(this);
            this.gameStatsManager = new GameStatsManager(this);
            this.languageManager = new LanguageManager();
            this.aliceAudioManager = new AliceAudioManager();
            this.textChannelConfigManager = new TextChannelConfigManager();
            registerCommands();
            registerListeners();
            startSchedulers();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }
}


