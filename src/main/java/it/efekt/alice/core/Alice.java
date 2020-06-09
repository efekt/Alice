package it.efekt.alice.core;

import it.efekt.alice.commands.HelpCmd;
import it.efekt.alice.commands.admin.*;
import it.efekt.alice.commands.core.CommandListener;
import it.efekt.alice.commands.core.CommandManager;
import it.efekt.alice.commands.fun.*;
import it.efekt.alice.commands.games.ApexStatsCmd;
import it.efekt.alice.commands.games.GameStatsCmd;
import it.efekt.alice.commands.games.MinecraftStatusCmd;
import it.efekt.alice.commands.nsfw.AnimeCharacterCmd;
import it.efekt.alice.commands.nsfw.HentaiCmd;
import it.efekt.alice.commands.nsfw.NekoCmd;
import it.efekt.alice.commands.util.*;
import it.efekt.alice.commands.voice.*;
import it.efekt.alice.config.Config;
import it.efekt.alice.db.GameStatsManager;
import it.efekt.alice.db.GuildConfigManager;
import it.efekt.alice.db.TextChannelConfigManager;
import it.efekt.alice.db.UserStatsManager;
import it.efekt.alice.lang.LanguageManager;
import it.efekt.alice.listeners.AnalyticsListener;
import it.efekt.alice.listeners.GameListener;
import it.efekt.alice.listeners.MessageListener;
import it.efekt.alice.listeners.ReadyListener;
import it.efekt.alice.modules.AliceAudioManager;
import it.efekt.alice.modules.GuildLogger;
import it.efekt.alice.modules.mentions.Greetings;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Alice {
    public final static Logger logger = LoggerFactory.getLogger(Alice.class);
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ShardManager shardManager;
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

    public void registerListeners(){
        this.shardManager.addEventListener(new AnalyticsListener());
        this.shardManager.addEventListener(new Greetings());
        this.shardManager.addEventListener(new MessageListener());
        this.guildLogger = new GuildLogger(this);
        this.shardManager.addEventListener(guildLogger);
        this.shardManager.addEventListener(new GameListener());
        this.shardManager.addEventListener(new CommandListener(this.cmdManager, this));
    }

    public void registerManagers(){
        this.guildConfigManager = new GuildConfigManager();
        this.cmdManager = new CommandManager(this);
        this.userStatsManager = new UserStatsManager();
        this.gameStatsManager = new GameStatsManager();
        this.languageManager = new LanguageManager();
        this.aliceAudioManager = new AliceAudioManager(getConfig());
        this.textChannelConfigManager = new TextChannelConfigManager();
    }

    public void registerCommands(){
        getCmdManager().addCommand(new PingCmd("ping"));
        getCmdManager().addCommand(new HelpCmd("help"));
        getCmdManager().addCommand(new PrefixCmd("prefix"));
        getCmdManager().addCommand(new TomekCmd("tomek"));
        getCmdManager().addCommand(new TomaszCmd("tomasz"));
        getCmdManager().addCommand(new AsunaCmd("asuna"));
        getCmdManager().addCommand(new NekoCmd("neko"));
        getCmdManager().addCommand(new KojimaCmd("kojima"));
        getCmdManager().addCommand(new HentaiCmd("h"));
        getCmdManager().addCommand(new StopCmd("stop"));
        getCmdManager().addCommand(new StatusCmd("status"));
        getCmdManager().addCommand(new HistoryDeletionCmd("clean"));
        getCmdManager().addCommand(new GuildLoggerCmd("logger"));
        getCmdManager().addCommand(new UserInfoCmd("info"));
        getCmdManager().addCommand(new TopCmd("top"));
        getCmdManager().addCommand(new ApexStatsCmd("apex"));
        getCmdManager().addCommand(new MinecraftStatusCmd("mc"));
        getCmdManager().addCommand(new FeaturesCmd("cmd"));
        getCmdManager().addCommand(new LangCmd("lang"));
        getCmdManager().addCommand(new RandomWaifuCmd("random-waifu"));
        getCmdManager().addCommand(new GameStatsCmd("topgames"));
        getCmdManager().addCommand(new LoliCmd("loli"));
        getCmdManager().addCommand(new WikiCmd("wiki"));
        getCmdManager().addCommand(new JoinCmd("join"));
        getCmdManager().addCommand(new LeaveCmd("leave"));
        getCmdManager().addCommand(new PlayCmd("play"));
        getCmdManager().addCommand(new NowPlayingCmd("np"));
        getCmdManager().addCommand(new PauseCmd("pause"));
        getCmdManager().addCommand(new CalcCmd("calc"));
        //getCmdManager().setExecutor(new RecordCmd("rec"));
        getCmdManager().addCommand(new ImgOnlyCmd("img-only"));
        getCmdManager().addCommand(new StatsCmd("stats"));
        getCmdManager().addCommand(new BlacklistReload("topgames-blacklist"));
        getCmdManager().addCommand(new ServersCmd("servers"));
        getCmdManager().addCommand(new PlayAgainCmd("playa"));
        getCmdManager().addCommand(new ReplyCmd("reply"));
        getCmdManager().addCommand(new TimezoneCmd("timezone"));
        getCmdManager().addCommand(new AnimeCharacterCmd("a"));
        getCmdManager().addCommand(new VoteCmd("vote"));
        getCmdManager().addCommand(new SkipCmd("skip"));
        getCmdManager().addCommand(new ChooseCommand("choose"));
        getCmdManager().addCommand(new OptCommand("opt"));
    }

    public void startSchedulers(){
        this.scheduler.scheduleAtFixedRate(new BotStatusRefresher(this), 10, 60, TimeUnit.SECONDS);
    }

    public ShardManager getShardManager(){
        return this.shardManager;
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

    public void setActivity(Activity activity){
        for (JDA jda : getShardManager().getShards()){
            jda.getPresence().setActivity(activity);
        }
    }

    private void init(){
        Runtime.getRuntime().addShutdownHook(new ShutdownThread(this));
        try {
            DefaultShardManagerBuilder builder = new DefaultShardManagerBuilder();
                builder.setToken(this.getConfig().getToken());
                builder.setShardsTotal(getConfig().getShardsTotal());
                builder.setActivity(Activity.playing("breaking the seal of the right eye..."));
                builder.addEventListeners(new ReadyListener());
                this.shardManager = builder.build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }
}


