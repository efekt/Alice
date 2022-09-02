package it.efekt.alice.commands.core;

import it.efekt.alice.commands.util.VoteCmd;
import it.efekt.alice.core.Alice;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.lang.AMessage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.regex.Pattern;

public class CommandListener extends ListenerAdapter {
    private String BOT_AUTHOR_ID = "128146616094818304";
    private Logger logger = LoggerFactory.getLogger(CommandListener.class);
    private CommandManager commandManager;

    public CommandListener(CommandManager commandManager, Alice alice){
        this.commandManager = commandManager;
        this.logger.info("Command Listener started.");
    }

    protected boolean isMentioningSelf(String[] args){
        //removing "!" from mention (it occurs when mentioned user has it's nickname changed)
        return args[0].replace("!", "").equalsIgnoreCase(AliceBootstrap.alice.getShardManager().getShards().get(0).getSelfUser().getAsMention()) && args.length>=2;
    }

    protected String getGuildPrefix(Guild guild){
        return AliceBootstrap.alice.getGuildConfigManager().getGuildConfig(guild).getPrefix();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e){
        long commandStartTime = System.currentTimeMillis();

        CombinedCommandEvent combinedCommandEvent = new CombinedCommandEvent(e);

        try {
            String[] allArgs = e.getMessage().getContentRaw().split("\\s+");
            // getting alias and cmd args accordingly to prefix (mention vs standard prefix)
            if (isMentioningSelf(allArgs) || (e.isFromGuild() && allArgs[0].startsWith(getGuildPrefix(e.getGuild())))) {
                String cmdAlias;
                String[] args;
                if (!isMentioningSelf(allArgs)) {
                    cmdAlias = allArgs[0].replaceFirst(Pattern.quote(getGuildPrefix(e.getGuild())), "");
                    args = Arrays.copyOfRange(allArgs, 1, allArgs.length);
                } else {
                    cmdAlias = allArgs[1];
                    args = Arrays.copyOfRange(allArgs, 2, allArgs.length);
                }

                if (this.commandManager.getCommand(cmdAlias) == null){
                    return;
                }

                Command cmd = this.commandManager.getCommand(cmdAlias);

                    if (e.isFromType(ChannelType.PRIVATE)) {
                        if (!cmd.isPrivateChannelCmd) {
                            return;
                        }
                    } else {
                        if (cmd.isPrivateChannelCmd) {
                            e.getChannel().asTextChannel().sendMessage("This command can be used on private channel only.").complete();
                            return;
                        }
                    }

                    if (!cmd.isPrivateChannelCmd) {
                        this.logger.debug("User: " + e.getAuthor().getName() + " id:" + e.getAuthor().getId() + " is requesting cmd: " + cmdAlias + " with msg: " + e.getMessage().getContentDisplay());
                    }
                    // Prevent bots from using commands
                    if (e.getAuthor().isBot()) {
                        return;
                    }

                    if (!cmd.isPrivateChannelCmd && cmd.getGuildConfig(e.getGuild()).isCmdDisabled(cmdAlias)) {
                        return;
                    }

                    // Only for debug purposes, change it later //todo implement better admin commands
                    if (cmd.isAdminCommand() && !e.getAuthor().getId().equalsIgnoreCase(BOT_AUTHOR_ID)) {
                        return;
                    }
                    // checking for author is important to filter private message commands, that are for admin only
                    if (e.getAuthor().getId().equalsIgnoreCase(BOT_AUTHOR_ID) || cmd.canUseCmd(e.getMember())) {
                        if (cmd.isNsfw() && !e.getChannel().asTextChannel().isNSFW()) {
                            e.getChannel().sendMessageEmbeds( new EmbedBuilder()
                                    .setThumbnail("https://i.imgur.com/L3o8Xq0.jpg")
                                    .setTitle(AMessage.CMD_THIS_IS_NSFW_CMD.get(combinedCommandEvent))
                                    .setColor(AliceBootstrap.EMBED_COLOR)
                                    .setDescription(AMessage.CMD_NSFW_ALLOWED_ONLY.get(combinedCommandEvent))
                                    .build()).queue();
                            return; // nsfw content on not-nsfw channel
                        }

                        // check if user-vote is required in order to execute this command
                        if (cmd.isVoteRequired && !cmd.hasVoted(e.getAuthor().getId())){
                            e.getChannel().sendMessageEmbeds(new EmbedBuilder()
                                    .setTitle(AMessage.VOTE_REQUIRED_TITLE.get(combinedCommandEvent))
                                    .setColor(AliceBootstrap.EMBED_COLOR)
                                    .setDescription(AMessage.VOTE_REQUIRED_INFO.get(combinedCommandEvent) + VoteCmd.VOTE_URL)
                                    .build()).queue();
                        }
                        cmd.args = args;
                        e.getChannel().sendTyping().queue();
                        cmd.execute(combinedCommandEvent);
                        this.logger.info("User: " + e.getAuthor().getName() + " id:" + e.getAuthor().getId() + " executed cmd: " + cmdAlias + " with msg: " + e.getMessage().getContentDisplay() +" took: " + (System.currentTimeMillis() - commandStartTime) + "ms");
                    }

            }
        } catch (Exception exc){
            exc.printStackTrace();
        }
    }
}
