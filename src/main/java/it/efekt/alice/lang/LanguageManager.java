package it.efekt.alice.lang;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class LanguageManager {
    private Logger logger = LoggerFactory.getLogger(LanguageManager.class);
    private List<Language> languageList = new ArrayList<>();
    private LangCode defaultLanguage = LangCode.en_US;

    public LanguageManager(){
        loadAll();
    }

    private void loadAll(){
       for (LangCode langCode : LangCode.values()){
           this.languageList.add(new Language(langCode));
       }
    }

    public Language getLang(LangCode language){
        Language l = languageList.stream().filter(lang -> lang.getLangCode().equalsIgnoreCase(language.name())).findFirst().get();
        if (l != null){
            return l;
        } else {
            logger.warn("Couldn't find " + language.name() + " lang files, getting default instead: " + this.defaultLanguage.name());
            return languageList.stream().filter(lang -> lang.getLangCode().equalsIgnoreCase(defaultLanguage.name())).findFirst().get();
        }
    }

}
