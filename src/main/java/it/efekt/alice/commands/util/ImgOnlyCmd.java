package it.efekt.alice.commands.util;

import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.db.model.TextChannelConfig;
import it.efekt.alice.lang.AMessage;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import java.util.concurrent.TimeUnit;

public class ImgOnlyCmd extends Command {

    public ImgOnlyCmd(String alias) {
        super(alias);
        addPermission(Permission.ADMINISTRATOR);
        setCategory(CommandCategory.DISCORD_ADMIN_UTILS);
        setDescription(AMessage.CMD_IMGONLY_DESC);
        setFullUsageInfo(AMessage.CMD_IMGONLY_FULL_USAGE_INFO);
    }

    @Override
    public boolean onCommand(MessageReceivedEvent e) {
        TextChannelConfig textChannelConfig = AliceBootstrap.alice.getTextChannelConfigManager().get(e.getTextChannel());

        if (textChannelConfig.isImgOnly()){
            textChannelConfig.setImgOnly(false);
            e.getTextChannel().sendMessage("disabled image only channel (this message will delete itself)").complete().delete().completeAfter(1, TimeUnit.SECONDS);
        } else {
            textChannelConfig.setImgOnly(true);
            e.getTextChannel().sendMessage("enabled image only channel (this message will delete itself)").complete().delete().completeAfter(5, TimeUnit.SECONDS);
        }
        return true;
    }
}
