package it.efekt.alice.commands.games;

import it.efekt.alice.commands.core.CombinedCommandEvent;
import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.exceptions.MinecraftServerNotFoundException;
import it.efekt.alice.lang.AMessage;
import it.efekt.alice.modules.MinecraftServerStatus;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class MinecraftStatusCmd extends Command {
    String[] chatColorCodes = {"§4", "§c", "§6", "§e","§2","§a","§b","§3","§1","§9","§d", "§5","§f","§7","§8","§0", "§r", "§l","§o", "§n", "§m", "§k"};

    public MinecraftStatusCmd(String alias) {
        super(alias);
        setDescription(AMessage.CMD_MC_DESC);
        setShortUsageInfo(AMessage.CMD_MC_SHORT_USAGE_INFO);
        setFullUsageInfo(AMessage.CMD_MC_FULL_USAGE_INFO);
        setCategory(CommandCategory.GAMES);
    }

    @Override
    public boolean onCommand(CombinedCommandEvent e) {
        if (getArgs().length == 1){
            MinecraftServerStatus status = new MinecraftServerStatus();
            try {
                 status.loadServer(getArgs()[0]);
            } catch(MinecraftServerNotFoundException exc){
                e.getChannel().sendMessage(AMessage.CMD_MC_SERVER_NOT_FOUND.get(e)).complete();
                return true;
            }

            if (status.isOnline()){
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle(AMessage.CMD_MC_SERVER_STATUS.get(e) + " " + status.getName());
                embedBuilder.setDescription(getCleanMotd(status.getMotd()));
                embedBuilder.setColor(AliceBootstrap.EMBED_COLOR);
                embedBuilder.setThumbnail(status.getFaviconUrl());
                embedBuilder.addField(AMessage.CMD_MC_SERVER_PLAYER_COUNT.get(e), status.getCurrentPlayers() + "/" + status.getMaxPlayers(), false);

                e.getChannel().sendMessageEmbeds(embedBuilder.build()).complete();
                return true;
            } else {
                e.getChannel().sendMessage(AMessage.CMD_MC_SERVER_OFFLINE.get(e)).complete();
                return true;
            }
        }
        return false;
    }

    private String getCleanMotd(String motd){
        String cleanMotd = motd;
        for (String colorCode : this.chatColorCodes){
            cleanMotd = cleanMotd.replace(colorCode, "");
        }
        return cleanMotd;
    }
}
