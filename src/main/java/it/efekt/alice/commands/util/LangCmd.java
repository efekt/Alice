package it.efekt.alice.commands.util;

import it.efekt.alice.commands.core.CombinedCommandEvent;
import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.lang.AMessage;
import it.efekt.alice.lang.LangCode;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LangCmd extends Command {

    public LangCmd(String alias) {
        super(alias);
        addPermission(Permission.ADMINISTRATOR);
        setCategory(CommandCategory.DISCORD_ADMIN_UTILS);
        setDescription(AMessage.CMD_LANG_DESC);
        setShortUsageInfo(AMessage.CMD_LANG_SHORT_USAGE_INFO);

        optionData.add(new OptionData(OptionType.STRING, "lang", "lang", false));
        setSlashCommand();
    }

    @Override
    public boolean onCommand(CombinedCommandEvent e) {
        if (getArgs().length == 0 || (getArgs().length == 1 && !isLang(getArgs()[0]))){
            showAvailableLanguages(e);
            return true;
        }

        if (getArgs().length == 1 && isLang(getArgs()[0])){
            LangCode langCode = LangCode.valueOf(getArgs()[0]);
            setGuildLanguage(e.getGuild(), langCode);
            e.sendMessageToChannel(AMessage.CMD_LANG_LANGUAGE_CHANGED.get(e, langCode.name()));
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

     private void showAvailableLanguages(CombinedCommandEvent e){
         e.sendMessageToChannel(AMessage.CMD_LANG_AVAILABLE_LANGS.get(e, String.join(" ", getLangValues())));
     }

     private void setGuildLanguage(Guild guild, LangCode langCode){
        getGuildConfig(guild).setLocaleAndSave(langCode.name());
     }
}
