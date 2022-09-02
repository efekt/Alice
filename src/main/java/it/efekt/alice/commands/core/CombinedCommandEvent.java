package it.efekt.alice.commands.core;


import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.FileUpload;

public class CombinedCommandEvent
{
    private SlashCommandInteractionEvent slashCommandInteractionEvent;
    private MessageReceivedEvent messageReceivedEvent;

    private final boolean isSlash;

    public CombinedCommandEvent(MessageReceivedEvent messageReceivedEvent)
    {
        this.messageReceivedEvent = messageReceivedEvent;
        this.isSlash = false;
    }

    public CombinedCommandEvent(SlashCommandInteractionEvent slashCommandInteractionEvent)
    {
        this.slashCommandInteractionEvent = slashCommandInteractionEvent;
        this.isSlash = true;
    }

    public Guild getGuild()
    {
        return isSlash ? slashCommandInteractionEvent.getGuild() : messageReceivedEvent.getGuild();
    }

    public User getUser()
    {
        return isSlash ? slashCommandInteractionEvent.getUser() : messageReceivedEvent.getAuthor();
    }

    public MessageChannelUnion getChannel()
    {
        return isSlash ? slashCommandInteractionEvent.getChannel() : messageReceivedEvent.getChannel();
    }

    public Member getMember()
    {
        return isSlash ? slashCommandInteractionEvent.getMember() : messageReceivedEvent.getMember();
    }

    public JDA getJDA()
    {
        return isSlash ? slashCommandInteractionEvent.getJDA() : messageReceivedEvent.getJDA();
    }

    public String getMessageString()
    {
        return isSlash ? slashCommandInteractionEvent.getName() : messageReceivedEvent.getMessage().getContentDisplay();
    }

    public Message getMessage()
    {
        return isSlash ? null : messageReceivedEvent.getMessage();
    }

    public void sendMessageToChannel(String message)
    {
        if(isSlash)
        {
            slashCommandInteractionEvent.reply(message).complete();
        }
        else
        {
            messageReceivedEvent.getChannel().sendMessage(message).complete();
        }
    }

    public void sendEmbeddedMessageToChannel(MessageEmbed message)
    {
        if(isSlash)
        {
            slashCommandInteractionEvent.replyEmbeds(message).complete();
        }
        else
        {
            messageReceivedEvent.getChannel().sendMessageEmbeds(message).complete();
        }
    }

    public void sendFilesMessageToChannel(FileUpload file)
    {
        if(isSlash)
        {
            slashCommandInteractionEvent.replyFiles(file).complete();
        }
        else
        {
            messageReceivedEvent.getChannel().sendFiles(file).complete();
        }
    }
}
