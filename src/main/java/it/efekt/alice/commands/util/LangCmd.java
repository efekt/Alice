package it.efekt.alice.commands.util;

import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.lang.LangCode;
import it.efekt.alice.lang.Message;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LangCmd extends Command {

    public LangCmd(String alias) {
        super(alias);
        addPermission(Permission.ADMINISTRATOR);
        setCategory(CommandCategory.DISCORD_ADMIN_UTILS);
        setDescription(Message.CMD_LANG_DESC);
        setShortUsageInfo(Message.CMD_LANG_SHORT_USAGE_INFO);
    }

    @Override
    public boolean onCommand(MessageReceivedEvent e) {
        if (getArgs().length == 0 || (getArgs().length == 1 && !isLang(getArgs()[0]))){
            showAvailableLanguages(e);
            return true;
        }

        if (getArgs().length == 1 && isLang(getArgs()[0])){
            LangCode langCode = LangCode.valueOf(getArgs()[0]);
            setGuildLanguage(e.getGuild(), langCode);
            e.getChannel().sendMessage(Message.CMD_LANG_LANGUAGE_CHANGED.get(e, langCode.name())).complete();
            return true;
        }
        return false;
    }

    private List<String> getLangValues(){
        List<LangCode> values = Arrays.asList(LangCode.values());
        List<String> langCodes = new ArrayList<>();
        values.forEach(langCode -> langCodes.add(langCode.name()));
        return langCodes;
     }

     private boolean isLang(String langCode){
        return getLangValues().contains(langCode);
     }

     private void showAvailableLanguages(MessageReceivedEvent e){
         e.getChannel().sendMessage(Message.CMD_LANG_AVAILABLE_LANGS.get(e, String.join(" ", getLangValues()))).queue();
     }

     private void setGuildLanguage(Guild guild, LangCode langCode){
        getGuildConfig(guild).setLocaleAndSave(langCode.name());
     }
}
