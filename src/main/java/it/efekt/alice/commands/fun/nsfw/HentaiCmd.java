package it.efekt.alice.commands.fun.nsfw;

import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.core.AliceBootstrap;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import pw.aru.api.nekos4j.Nekos4J;
import pw.aru.api.nekos4j.image.Image;
import pw.aru.api.nekos4j.image.ImageProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class HentaiCmd extends Command {
    private Nekos4J api = new Nekos4J.Builder().build();
    private ImageProvider imageProvider = api.getImageProvider();
    private HashMap<String, List<String>> categories = new HashMap<>();

    public HentaiCmd(String alias) {
        super(alias);
        setDescription("Uważaj! Niezbadane wody!");
        setNsfw(true);
        setUsageInfo(" `typ`");
        setCategory(CommandCategory.FUN);
        loadCategories();
    }

    @Override
    public void onCommand(MessageReceivedEvent e) {

        if (getArgs().length == 1 ){
            if (!this.categories.containsKey(getArgs()[0])){
                e.getChannel().sendMessage("Nie znam takiej kategori. Wybierz jedną z nich:\n " + categories.keySet().toString()).queue();
                return;
            }

            Random random = new Random();
            int randomPic = random.nextInt(categories.get(getArgs()[0]).size());
            Future<Image> imageFuture = imageProvider.getRandomImage(categories.get(getArgs()[0]).get(randomPic)).submit();
                try {
                    Image image = imageFuture.get();
                    String imageUrl = image.getUrl();

                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setColor(AliceBootstrap.EMBED_COLOR);
                    embedBuilder.setImage(imageUrl);
                    e.getChannel().sendMessage(embedBuilder.build()).queue();
                } catch (InterruptedException| ExecutionException e1) {
                    e.getChannel().sendMessage("Nie znaleziono").queue();
                }
        } else {
            e.getChannel().sendMessage("Zwróć uwagę na to jak wpisujesz komendę: " + getGuildPrefix(e.getGuild()) + getAlias() + getUsageInfo()).queue();
        }
    }

    private void loadCategories(){
        List<String> nekoTags = new ArrayList<>();
        nekoTags.add("lewdk");
        nekoTags.add("ngif");
        nekoTags.add("lewdkemo");
        nekoTags.add("erokemo");
        nekoTags.add("eron");
        categories.put("neko",nekoTags);
    }
}
