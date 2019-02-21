package it.efekt.alice.commands.fun;

import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.exceptions.MinecraftServerNotFoundException;
import it.efekt.alice.modules.MinecraftServerStatus;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class MinecraftStatusCmd extends Command {
    String[] chatColorCodes = {"§4", "§c", "§6", "§e","§2","§a","§b","§3","§1","§9","§d", "§5","§f","§7","§8","§0", "§r", "§l","§o", "§n", "§m", "§k"};

    public MinecraftStatusCmd(String alias) {
        super(alias);
        setDescription("Wyświetla aktualne informacje o serwerze Minecraft");
        setUsageInfo(" `adres serwera`");
        setCategory(CommandCategory.FUN);
    }

    @Override
    public void onCommand(MessageReceivedEvent e) {
        if (getArgs().length == 1){
            MinecraftServerStatus status = new MinecraftServerStatus();
            try {
                 status.loadServer(getArgs()[0]);
            } catch(MinecraftServerNotFoundException exc){
                e.getChannel().sendMessage("Nie znaleziono serwera").queue();
                return;
            }

            if (status.isOnline()){
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle("Status serwera " + status.getName());
                embedBuilder.setDescription(getCleanMotd(status.getMotd()));
                embedBuilder.setColor(AliceBootstrap.EMBED_COLOR);
                embedBuilder.setThumbnail(status.getFaviconUrl());
                embedBuilder.addField("Ilość graczy", status.getCurrentPlayers() + "/" + status.getMaxPlayers(), false);

                e.getChannel().sendMessage(embedBuilder.build()).queue();
            } else {
                e.getChannel().sendMessage("Serwer jest offline").queue();
            }
        }
    }

    private String getCleanMotd(String motd){
        String cleanMotd = motd;
        for (String colorCode : this.chatColorCodes){
            cleanMotd = cleanMotd.replaceAll(colorCode, "");
        }
        return cleanMotd;
    }
}
