package it.efekt.alice.lang;

import com.google.gson.stream.JsonWriter;
import it.efekt.alice.core.AliceBootstrap;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public enum Message {
    FILE_NOT_FOUND("Nie znaleziono pliku :("),
    CMD_HELP_TITLE("Alice - Centrum Pomocy"),
    CMD_UNKNOWN_COMMAND("Nieznana komenda"),
    CMD_COMMAND_NOT_FOUND("Nie znaleziono komendy" ),
    CMD_CHECK_IF_IS_CORRECT("Sprawdź czy wpisałeś komendę prawidłowo\n{1}"),
    CMD_THIS_IS_NSFW_CMD("Ta komenda jest NSFW!"),
    CMD_NSFW_ALLOWED_ONLY("Dozwolona tylko na kanałach z włączoną obsługą treści NSFW"),
    CMD_NSFW_NOTIFICATION("(nsfw)"),
    CMD_HELP_CATEGORY("Kategoria"),
    CMD_HELP_REQUIRED_PERMS("Wymagane uprawnienia"),
    CMD_HELP_USAGE_INFO("<komenda> - wyświetla pomoc podanej komendy"),
    CMD_HELP_DESCRIPTION("Wyświetla ekran pomocy"),
    CMD_HELP_SHORT_USAGE_INFO("`|cmd|`"),
    CMD_HELP_COMMAND_UNKNOWN("Nieznana komenda"),
    CMD_HELP_NOT_FOUND_TRY_AGAIN("Nie znaleziono komendy, spróbuj jeszcze raz"),
    CMD_HELP_FOOTER("<komenda> - wyświetla pomoc podanej komendy"),
    CMD_HENTAI_DESC("Uważaj! Niezbadane wody!\n Dostępne typy: \n `neko` `gifneko` `random`(na własną odpowiedzialność)"),
    CMD_HENTAI_SHORT_USAGE_INFO("`typ`"),
    CMD_HENTAI_CATEGORY_UNKNOWN("Nie znam takiej kategori. Wybierz jedną z nich:\n {1}"),
    CMD_HENTAI_NOT_FOUND("Nie znaleziono"),
    CMD_HENTAI_CHECK_COMMAND("Zwróć uwagę na to jak wpisujesz komendę:"),
    CMD_HENTAI_ERROR_NOT_FOUND("Wystąpił problem, nie znaleziono nic :("),
    CMD_NEKO_DESC("Nyaaaaaa!"),
    CMD_APEX_SHORT_USAGE_INFO("`pc/psn/xbl` `nick`"),
    CMD_APEX_FULL_USAGE_INFO("`pc/psn/xbl` - platforma, wybierz jedną\n `nick` - nazwa użytkownika Apex"),
    CMD_APEX_DESC("Wyświetla statystyki z gry Apex (Eksperymentalne)"),
    CMD_APEX_PLAYER_NOT_FOUND("Nie znaleziono takiego gracza"),
    CMD_APEX_EMBED_TITLE("APEX - Statystyki (Eksperymentalne)"),
    CMD_APEX_EMBED_DESC("Niektóre wartości mogą się nie zgadzać lub zupełnie brakować"),
    CMD_APEX_PLAYERNAME("Nazwa gracza"),
    CMD_APEX_LEVEL("Poziom"),
    CMD_APEX_KILLS("Zabójstwa"),
    CMD_APEX_HEADSHOTS("Strzały w głowę"),
    CMD_APEX_MATCHES("Mecze"),
    CMD_APEX_LEGEND_KILLS("Zabójstwa: "),
    CMD_APEX_LEGEND_HEADSHOTS("Headshoty: "),
    CMD_ASUNA_DESC("Wyswietla losowe zdjecie Asuny"),
    CMD_KOJIMA_DESC("Kojumbo"),
    CMD_MC_DESC("Wyświetla aktualne informacje o serwerze Minecraft"),
    CMD_MC_SHORT_USAGE_INFO("`ip`"),
    CMD_MC_FULL_USAGE_INFO("`ip` - adres serwera Minecraft"),
    CMD_MC_SERVER_NOT_FOUND("Nie znaleziono serwera"),
    CMD_MC_SERVER_STATUS("Status serwera"),
    CMD_MC_SERVER_PLAYER_COUNT("Ilość graczy"),
    CMD_MC_SERVER_OFFLINE("Serwer jest offline"),
    CMD_TOMASZ_DESC("Tomasz"),
    CMD_TOMEK_DESC("Tomek"),
    CMD_FEATURES_DESC("Zadządzanie dostępnością komend Alice na tym serwerze \n `disable/enable` `nazwa komendy` - wyłącza/włącza komendę"),
    CMD_FEATURES_WRONG_FEATURE_GIVEN("Podano nieprawidłową nazwę funkcji"),
    CMD_FEATURES_CANNOT_DISABLE("Nie możesz wyłączyć tej funkcji"),
    CMD_FEATURES_HAS_BEEN_DISABLED("Funkcjonalność {1} została wyłączona"),
    CMD_FEATURES_HAS_BEEN_ENABLED("Funkcjonalność {1} została włączona"),
    CMD_FEATURES_LIST("Lista funkcji bota"),
    CMD_LOGGER_DESC("Ustawia kanał na którym mają zapisywać się logi serwerowe.\n `disable` - wyłącza logi"),
    CMD_LOGGER_SHORT_USAGE_INFO("`#kanał` lub `disable`"),
    CMD_LOGGER_FULL_USAGE_INFO("`#kanał` - kanał na którym mają pojawiać się logi\n`disable` - wyłącza logowanie"),
    CMD_LOGGER_NOT_SET("Nie ustawiono kanału logów"),
    CMD_LOGGER_CURRENTLY_USED("Aktualnie używany kanał logów: {1}"),
    CMD_LOGGER_SET("Ustawiono kanał logów na: {1}"),
    CMD_LOGGER_DISABLED("Wyłączono logi na tym serwerze"),
    CMD_HISTORYDEL_DESC("Usuwa n ostatnich wiadomości na kanale"),
    CMD_PING_DESC("Testuje refleks Alice"),
    CMD_PING_DESCRIPTION("Wyświetla ekran pomocy"),
    CMD_PING_RESPONSE("Pong: {1}ms"),
    CMD_HISTORYDEL_SHORT_USAGE_INFO("`liczba wiadomości`"),
    CMD_PREFIX_DESC("Ustawia prefix, który będzie używany na tym serwerze"),
    CMD_PREFIX_SHORT_USAGE_INFO("`prefix`"),
    CMD_PREFIX_FULL_USAGE_INFO("`prefix` - twój nowy wymarzony prefix"),
    CMD_PREFIX_REQUIREMENTS("Prefix musi składać się z jednego znaku"),
    CMD_NEW_PREFIX_SET("Nowy prefix dla tego serwera: {1}"),
    CMD_CHANGED_PREFIX_LOG("{1} zmienił prefix na: {2}"),
    CMD_TOP_DESC("Wyświetla listę największych spamerów"),
    CMD_TOP_USAGE_INFO("`liczba użytkowników` lub `loadAll`"),
    CMD_TOP_MINUS_TOP("oszalałeś...?"),
    CMD_TOP_NUMBER_TOO_LARGE("Nie znaleziono tylu użytkowników, maksymalnie możesz podać: {1}"),
    CMD_TOP_LOADALL_FORBIDDEN("Tylko administrator może wczytywać wszystkie wiadomości"),
    CMD_TOP_FULL_USAGE_INFO("`liczba użytkowników` - opcjonalne\n`loadAll` - zlicza całą historię serwera"),
    CMD_TOP_LOADALL_WARNING("Próbuję zliczyć wszystkie wiadomości.\nMoże to potrwać nawet do kilku/kilkunastu minut w zależności od wielkości serwera :fearful:"),
    CMD_TOP_LOADALL_FOUND("Znalazłam {1} wiadomości, rozpoczynam zliczanie..."),
    CMD_TOP_LOADALL_FINISHED("Zakończyłam zliczanie wiadomości, zapisuję wyniki do bazy danych..."),
    CMD_TO_LOADALL_SAVED("Wszystkie wyniki zostały zapisane :heart_eyes:"),
    CMD_TOP_LIST_TITLE("Lista największych spamerów serwera {1}"),
    CMD_USERINFO_DESC("Wyświetla informacje o użytkowniku"),
    CMD_USERINFO_USAGE_INFO("`@user`"),
    CMD_USERINFO_TITLE("Informacje o użytkowniku {1}"),
    CMD_USERINFO_ACCOUNT_CREATED("Data utworzenia konta"),
    CMD_USERINFO_SPAM_LVL("Spamerski level"),
    CMD_USERINFO_MSGS_SENT("Wysłanych wiadomości"),






    ALICE_GOODBYE_RESPONSE("Dobrej nocy! {1}"),
    ALICE_GOODBYE_REQUIRED("dobranoc"),
    ALICE_MORNING_RESPONSE("Dzień Dobry! {1}"),
    ALICE_MORNING_REQUIRED("dzień dobry"),
    BLANK(""); // leave it alone ;)

    private String defaultValue;

    Message(String defaultText){
        this.defaultValue = defaultText;
    }

    public String getKey(){
        return this.name().replaceAll("_", "-").toLowerCase();
    }

    public String getDefaultValue(){
        return this.defaultValue;
    }

    public String get(Language language, String... vars){
        return language.getLanguageString(getKey()).var(vars);
    }

    public String get(MessageReceivedEvent e, String... vars){
       return getLanguage(e).getLanguageString(getKey()).var(vars);
    }

    public Language getLanguage(MessageReceivedEvent e){
        String locale = AliceBootstrap.alice.getGuildConfigManager().getGuildConfig(e.getGuild()).getLocale();
        return AliceBootstrap.alice.getLanguageManager().getLang(LangCode.valueOf(locale));
    }

    public static Message getDefaultMessage(String key){
        return Arrays.stream(Message.values()).filter(msg -> msg.getKey().equals(key)).findFirst().get();
    }

    public static void main(String[] args){
        // Generate json file with default values from this enum
        System.out.println("Generating all messages to json file");
        try {
            JsonWriter jsonWriter = new JsonWriter(new FileWriter("./en_US.json"));
            jsonWriter.beginObject();
            jsonWriter.setIndent("  ");
            for (Message message : Message.values()){
                jsonWriter.name(message.getKey()).value(message.getDefaultValue());
            }
            jsonWriter.endObject();
            jsonWriter.close();
            System.out.println("Saved all messages!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
