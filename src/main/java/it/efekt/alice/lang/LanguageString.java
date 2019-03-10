package it.efekt.alice.lang;

public class LanguageString {
    private String text;

    public LanguageString(String text){
        this.text = text;
    }

    public String var(String... vars){
        replaceVars(vars);
        return this.text;
    }

    @Override
    public String toString(){
        return this.text;
    }

    private void replaceVars(String... vars){
        int i = 1;
        for (String var : vars){
            this.text = text.replace("{"+i+"}", var);
                    i++;
        }
    }
}
