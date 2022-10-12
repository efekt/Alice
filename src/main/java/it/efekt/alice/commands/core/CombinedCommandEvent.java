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

    public boolean isSlash()
    {
        return isSlash;
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

    public Message sendMessageToChannel(String message)
    {
        if(isSlash)
        {
            return slashCommandInteractionEvent.getHook().sendMessage(message).complete();
        }
        else
        {
            return messageReceivedEvent.getChannel().sendMessage(message).complete();
        }
    }

    public void sendEmbeddedMessageToChannel(MessageEmbed message)
    {
        if(isSlash)
        {
            slashCommandInteractionEvent.getHook().sendMessageEmbeds(message).queue();
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
            slashCommandInteractionEvent.getHook().sendFiles(file).queue();
        }
        else
        {
            messageReceivedEvent.getChannel().sendFiles(file).complete();
        }
    }

    public void deleteMessage()
    {
        if(!isSlash)
        {
            messageReceivedEvent.getMessage().delete().queue();
        }
    }

    public void sendSlashConfirmation(String message)
    {
        if(isSlash)
        {
            slashCommandInteractionEvent.getHook().sendMessage(message).setEphemeral(true).queue();
        }
    }

    public Mentions getMentions()
    {
        if(isSlash)
        {
            return slashCommandInteractionEvent.getOptions().get(0).getMentions(); //Usually only first option is mention
        }
        else
        {
            return messageReceivedEvent.getMessage().getMentions();
        }
    }
}
