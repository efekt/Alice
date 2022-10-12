package it.efekt.alice.commands.util;

import com.udojava.evalex.Expression;
import it.efekt.alice.commands.core.CombinedCommandEvent;
import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.lang.AMessage;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class CalcCmd extends Command {
    public CalcCmd(String alias) {
        super(alias);
        setCategory(CommandCategory.UTILS);
        setDescription(AMessage.CMD_CALC_DESC);
        setShortUsageInfo(AMessage.CMD_CALC_SHORT_USAGE_INFO);

        optionData.add(new OptionData(OptionType.STRING, "query", "query", true));
        setSlashCommand();
    }

    @Override
    public boolean onCommand(CombinedCommandEvent e) {
        if (getArgs().length < 1){
            return false;
        }

        String expressionString = String.join(" ", getArgs());

        try {
            e.sendMessageToChannel(expressionString + "=" + new Expression(expressionString).eval().toString());
        } catch (Expression.ExpressionException | ArithmeticException exc){
            e.sendMessageToChannel(exc.getLocalizedMessage());
        }
        return true;
    }
}
