package me.hugmanrique.bpr.utils;

import me.hugmanrique.bpr.BPRestarter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.config.Configuration;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Hugmanrique
 * @since 20/09/2016
 */
public class Language {
    private static final String PHRASES_KEY = "phrases";
    private static final String DEFAULT_PHRASE = ChatColor.RED + "We couldn't find this phrase. Please report this to Hugmanrique";

    private BPRestarter main;

    private Map<String, String> phrases;

    public Language(BPRestarter main) {
        this.main = main;
        this.phrases = new HashMap<>();

        loadPhrases(main.getConfig());
    }

    private void loadPhrases(Configuration config){
        Collection<String> keys = config.getSection(PHRASES_KEY).getKeys();

        for (String key : keys){
            String phrase = ChatColor.translateAlternateColorCodes('&', config.getString(PHRASES_KEY + "." + key));

            phrases.put(key, phrase);
        }
    }

    public String getPhrase(String key){
        String phrase = phrases.get(key);

        if (phrase == null){
            return DEFAULT_PHRASE;
        }

        return phrase;
    }
}
