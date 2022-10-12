package it.efekt.alice.commands.core;

import it.efekt.alice.commands.util.VoteCmd;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.lang.AMessage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class SlashCommandListener extends ListenerAdapter
{
    private final Logger logger = LoggerFactory.getLogger(SlashCommandListener.class);
    private final CommandManager commandManager;

    public SlashCommandListener(CommandManager commandManager){
        this.commandManager = commandManager;
        this.logger.info("Command Listener started.");
    }

    protected boolean isMentioningSelf(List<OptionMapping> args){
        return args.stream().noneMatch(optionMapping -> optionMapping.getMentions().isMentioned(AliceBootstrap.alice.getShardManager().getShards().get(0).getSelfUser()));
    }

    protected String getGuildPrefix(Guild guild){
        return AliceBootstrap.alice.getGuildConfigManager().getGuildConfig(guild).getPrefix();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent e){
        long commandStartTime = System.currentTimeMillis();

        CombinedCommandEvent combinedCommandEvent = new CombinedCommandEvent(e);

        try {
            e.deferReply().queue();

            List<OptionMapping> allArgs = e.getOptions();
            // getting alias and cmd args accordingly to prefix (mention vs standard prefix)
            if (isMentioningSelf(allArgs) || (e.isFromGuild() )) {
                String cmdAlias = e.getName();
                List<OptionMapping> args;
                if (isMentioningSelf(allArgs) && allArgs.size() >= 2)
                {
                    args = allArgs.subList(1, allArgs.size());
                } else {
                    args = allArgs;
                }

                if (this.commandManager.getCommand(cmdAlias) == null){
                    return;
                }

                Command cmd = this.commandManager.getCommand(cmdAlias);

                if (e.getChannelType().equals(ChannelType.PRIVATE)) {
                    if (!cmd.isPrivateChannelCmd) {
                        return;
                    }
                } else {
                    if (cmd.isPrivateChannelCmd) {
                        combinedCommandEvent.sendMessageToChannel("This command can be used on private channel only.");
                        return;
                    }
                }

                if (!cmd.isPrivateChannelCmd) {
                    this.logger.debug("User: " + e.getUser().getName() + " id:" + e.getUser().getId() + " is requesting cmd: " + cmdAlias + " with args: " + allArgs);
                }
                // Prevent bots from using commands
                if (e.getUser().isBot()) {
                    return;
                }

                if (!cmd.isPrivateChannelCmd && cmd.getGuildConfig(e.getGuild()).isCmdDisabled(cmdAlias)) {
                    return;
                }

                // Only for debug purposes, change it later //todo implement better admin commands
                String BOT_AUTHOR_ID = "128146616094818304";
                if (cmd.isAdminCommand() && !e.getUser().getId().equalsIgnoreCase(BOT_AUTHOR_ID)) {
                    return;
                }
                // checking for author is important to filter private message commands, that are for admin only
                if (e.getUser().getId().equalsIgnoreCase(BOT_AUTHOR_ID) || cmd.canUseCmd(e.getMember())) {
                    if (cmd.isNsfw() && !e.getChannel().asTextChannel().isNSFW()) {
                        combinedCommandEvent.sendEmbeddedMessageToChannel(new EmbedBuilder()
                                .setThumbnail("https://i.imgur.com/L3o8Xq0.jpg")
                                .setTitle(AMessage.CMD_THIS_IS_NSFW_CMD.get(combinedCommandEvent))
                                .setColor(AliceBootstrap.EMBED_COLOR)
                                .setDescription(AMessage.CMD_NSFW_ALLOWED_ONLY.get(combinedCommandEvent))
                                .build());
                        return; // nsfw content on not-nsfw channel
                    }

                    // check if user-vote is required in order to execute this command
                    if (cmd.isVoteRequired && !cmd.hasVoted(e.getUser().getId())){
                        combinedCommandEvent.sendEmbeddedMessageToChannel(new EmbedBuilder()
                                .setTitle(AMessage.VOTE_REQUIRED_TITLE.get(combinedCommandEvent))
                                .setColor(AliceBootstrap.EMBED_COLOR)
                                .setDescription(AMessage.VOTE_REQUIRED_INFO.get(combinedCommandEvent) + VoteCmd.VOTE_URL)
                                .build());
                    }
                    cmd.args = String.join(" ",args.stream().map(OptionMapping::getAsString).toArray(String[]::new)).split(" ");
                    if(cmd.args.length == 1 && cmd.args[0].length() == 0) cmd.args = new String[0];

                    cmd.execute(combinedCommandEvent);
                    this.logger.info("User: " + e.getUser().getName() + " id:" + e.getUser().getId() + " is requesting cmd: " + cmdAlias + " args: "+ Arrays.toString(cmd.args) +" took: " + (System.currentTimeMillis() - commandStartTime) + "ms");
                }

            }
        } catch (Exception exc){
            exc.printStackTrace();
        }
    }
}
