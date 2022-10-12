package it.efekt.alice.commands.fun;

import it.efekt.alice.commands.core.CombinedCommandEvent;
import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.lang.AMessage;
import net.dv8tion.jda.api.utils.FileUpload;

public class TomekCmd extends Command {

    public TomekCmd(String alias) {
        super(alias);
        setDescription(AMessage.CMD_TOMEK_DESC);
        setCategory(CommandCategory.FUN);
    }

    @Override
    public boolean onCommand(CombinedCommandEvent e) {
        try {
            e.sendFilesMessageToChannel(FileUpload.fromData(AliceBootstrap.class.getClassLoader().getResourceAsStream("assets/images/tomek.png"), "tomek.png"));
            return true;
        } catch (NullPointerException exc) {
            e.sendMessageToChannel(AMessage.FILE_NOT_FOUND.get(e));
            return true;
        }
    }
}
