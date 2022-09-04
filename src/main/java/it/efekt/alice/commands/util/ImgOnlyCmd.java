package it.efekt.alice.commands.util;

import it.efekt.alice.commands.core.CombinedCommandEvent;
import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.db.model.TextChannelConfig;
import it.efekt.alice.lang.AMessage;
import net.dv8tion.jda.api.Permission;
import java.util.concurrent.TimeUnit;

public class ImgOnlyCmd extends Command {

    public ImgOnlyCmd(String alias) {
        super(alias);
        addPermission(Permission.ADMINISTRATOR);
        setCategory(CommandCategory.DISCORD_ADMIN_UTILS);
        setDescription(AMessage.CMD_IMGONLY_DESC);
        setFullUsageInfo(AMessage.CMD_IMGONLY_FULL_USAGE_INFO);

        setSlashCommand();
    }

    @Override
    public boolean onCommand(CombinedCommandEvent e) {
        TextChannelConfig textChannelConfig = AliceBootstrap.alice.getTextChannelConfigManager().get(e.getChannel().asTextChannel());

        if (textChannelConfig.isImgOnly()){
            textChannelConfig.setImgOnly(false);
            e.sendMessageToChannel("disabled image only channel (this message will delete itself)").delete().completeAfter(1, TimeUnit.SECONDS);
        } else {
            textChannelConfig.setImgOnly(true);
            e.sendMessageToChannel("enabled image only channel (this message will delete itself)").delete().completeAfter(5, TimeUnit.SECONDS);
        }
        return true;
    }
}
