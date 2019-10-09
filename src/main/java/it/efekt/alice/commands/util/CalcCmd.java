package it.efekt.alice.commands.util;

import com.udojava.evalex.Expression;
import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.lang.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CalcCmd extends Command {
    public CalcCmd(String alias) {
        super(alias);
        setCategory(CommandCategory.UTILS);
        setDescription(Message.CMD_CALC_DESC);
        setShortUsageInfo(Message.CMD_CALC_SHORT_USAGE_INFO);
    }

    @Override
    public boolean onCommand(MessageReceivedEvent e) {
        if (getArgs().length < 1){
            return false;
        }

        String expressionString = String.join(" ", getArgs());

        try {
            e.getChannel().sendMessage(new Expression(expressionString).eval().toString()).complete();
        } catch (Expression.ExpressionException exc){
            e.getChannel().sendMessage(exc.getLocalizedMessage()).complete();
        }
        return true;
    }
}
