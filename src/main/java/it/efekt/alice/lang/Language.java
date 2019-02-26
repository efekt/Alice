package it.efekt.alice.lang;

import com.google.gson.Gson;
import it.efekt.alice.core.AliceBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;

public class Language {
    private Logger logger = LoggerFactory.getLogger(Language.class);

    // Localized text
    private LangCode langCode;
    private HashMap<String, String> langStrings =  new HashMap<>();

    public Language(LangCode langCode){
        this.langCode = langCode;
        load();
    }

    public String getLangCode(){
        return this.langCode.name();
    }

    public LanguageString getLanguageString(String key){
        if (this.langStrings.containsKey(key)){
            return new LanguageString(this.langStrings.get(key));
        }
        logger.info("Language key " + key + " not found, displaying default value");
        return new LanguageString(Message.getDefaultMessage(key).getDefaultValue());
    }

    public String get(String key){
        return getLanguageString(key).toString();
    }

    public String get(String key, String... vars){
        return getLanguageString(key).var(vars);
    }


    private void load() {
        String path = "assets/languages/"+getLangCode()+".json";
        InputStream in = AliceBootstrap.class.getClassLoader().getResourceAsStream(path);
        if (in != null){
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            Gson gson = new Gson();
            this.langStrings = gson.fromJson(reader, HashMap.class);
            logger.info("Loaded language: " + getLangCode());
            return;
        }
        logger.error("Couldn't load language: " + getLangCode() + " File not found.");
    }
}
