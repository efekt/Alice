package it.efekt.alice.core;

import it.efekt.alice.commands.HelpCmd;
import it.efekt.alice.commands.admin.*;
import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandListener;
import it.efekt.alice.commands.core.CommandManager;
import it.efekt.alice.commands.core.SlashCommandListener;
import it.efekt.alice.commands.fun.AsunaCmd;
import it.efekt.alice.commands.fun.ChooseCommand;
import it.efekt.alice.commands.fun.RandomWaifuCmd;
import it.efekt.alice.commands.fun.WikiCmd;
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
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        this.shardManager.addEventListener(new SlashCommandListener(this.cmdManager));
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

    public void registerSlashCommands()
    {
//        Guild jda = shardManager.getGuildById("823611908623958046");

        for (JDA jda : shardManager.getShards())
        {
            for (Command c : cmdManager.getCommands().values())
            {
                if (c.isSlashCommand())
                {
                    jda.upsertCommand(c.getCommandData()).queue();
                }
            }
        }
    }

    public void registerCommands(){
        getCmdManager().addCommand(new PingCmd("ping"));
        getCmdManager().addCommand(new HelpCmd("help"));
        getCmdManager().addCommand(new PrefixCmd("prefix")); //excluded
//        getCmdManager().addCommand(new TomekCmd("tomek")); //excluded
//        getCmdManager().addCommand(new TomaszCmd("tomasz")); //excluded
        getCmdManager().addCommand(new AsunaCmd("asuna"));
        getCmdManager().addCommand(new NekoCmd("neko"));
//        getCmdManager().addCommand(new KojimaCmd("kojima")); //excluded
        getCmdManager().addCommand(new HentaiCmd("h"));
        getCmdManager().addCommand(new StopCmd("stop")); //excluded
        getCmdManager().addCommand(new StatusCmd("status")); //excluded
        getCmdManager().addCommand(new HistoryDeletionCmd("clean"));
        getCmdManager().addCommand(new GuildLoggerCmd("logger"));
        getCmdManager().addCommand(new UserInfoCmd("info"));
        getCmdManager().addCommand(new TopCmd("top"));
//        getCmdManager().addCommand(new ApexStatsCmd("apex"));
        getCmdManager().addCommand(new MinecraftStatusCmd("mc"));
        getCmdManager().addCommand(new FeaturesCmd("cmd")); //excluded
        getCmdManager().addCommand(new LangCmd("lang"));
        getCmdManager().addCommand(new RandomWaifuCmd("randomwaifu"));
        getCmdManager().addCommand(new GameStatsCmd("topgames"));
//        getCmdManager().addCommand(new LoliCmd("loli")); //excluded
        getCmdManager().addCommand(new WikiCmd("wiki"));
        getCmdManager().addCommand(new JoinCmd("join"));
        getCmdManager().addCommand(new LeaveCmd("leave"));
        getCmdManager().addCommand(new PlayCmd("play"));
        getCmdManager().addCommand(new NowPlayingCmd("np"));
        getCmdManager().addCommand(new PauseCmd("pause"));
        getCmdManager().addCommand(new CalcCmd("calc"));
//        getCmdManager().setExecutor(new RecordCmd("rec")); //deprecated
        getCmdManager().addCommand(new ImgOnlyCmd("img-only"));
        getCmdManager().addCommand(new StatsCmd("stats")); //excluded there is no description
        getCmdManager().addCommand(new BlacklistReload("topgames-blacklist")); //excluded
        getCmdManager().addCommand(new ServersCmd("servers")); //excluded
        getCmdManager().addCommand(new PlayAgainCmd("playa"));
        getCmdManager().addCommand(new ReplyCmd("reply")); //excluded
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
            DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(this.getConfig().getToken());
                builder.setShardsTotal(getConfig().getShardsTotal());
                builder.setActivity(Activity.playing("breaking the seal of the right eye..."));
                builder.addEventListeners(new ReadyListener());
                //builder.enableIntents(GatewayIntent.MESSAGE_CONTENT); //Intents to be verified
                builder.enableIntents(GatewayIntent.GUILD_MEMBERS);
                builder.enableIntents(GatewayIntent.GUILD_PRESENCES);
                builder.setMemberCachePolicy(MemberCachePolicy.ALL);
                builder.setChunkingFilter(ChunkingFilter.NONE);
                builder.enableCache(CacheFlag.ONLINE_STATUS, CacheFlag.ACTIVITY);

                this.shardManager = builder.build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


