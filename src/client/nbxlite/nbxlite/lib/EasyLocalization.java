package net.minecraft.src.nbxlite.lib;

import java.io.*;
import java.security.InvalidParameterException;
import java.util.Properties;
import net.minecraft.src.StringTranslate;

/**
* Simple mod localization class.
*
* @author Jimeo Wan
* @license Public domain
*
*/
public class EasyLocalization {

    private static final String DEFAULT_LANGUAGE = "en_US";

    private String modName = null;
    private String loadedLanguage = null;
    private Properties defaultMappings = new Properties();
    private Properties mappings = new Properties();

    /**
     * Loads the mod's localization files. All language files must be stored in
     * "[modname]/lang/", in .properties files. (ex: for the mod 'invtweaks',
     * the french translation is in: "invtweaks/lang/fr_FR.properties")
     *
     * @param modName The mod name
     */
    public EasyLocalization(String modName) {
        if (modName == null) {
            throw new InvalidParameterException("Mod name can't be null");
        }
        this.modName = modName;
        load(getCurrentLanguage());
    }

    /**
     * Get a string for the given key, in the currently active translation.
     *
     * @param key
     * @return
     */
    public synchronized String get(String key) {
        String currentLanguage = getCurrentLanguage();
        if (!currentLanguage.equals(loadedLanguage)) {
            load(currentLanguage);
        }
        return mappings.getProperty(key, defaultMappings.getProperty(key, key));
    }

    private void load(String newLanguage) {
        defaultMappings.clear();
        mappings.clear();
        try {
            BufferedReader langStream = new BufferedReader(new InputStreamReader((EasyLocalization.class).getResourceAsStream(
                            "/" + modName + "/lang/" + newLanguage + ".lang"), "UTF-8"));
            BufferedReader defaultLangStream = new BufferedReader(new InputStreamReader((EasyLocalization.class).getResourceAsStream(
                            "/" + modName + "/lang/" + DEFAULT_LANGUAGE + ".lang"), "UTF-8"));
            mappings.load((langStream == null) ? defaultLangStream : langStream);
            defaultMappings.load(defaultLangStream);
            if (langStream != null) {
                langStream.close();
            }
            defaultLangStream.close();
        } catch (Exception e) {
            load(DEFAULT_LANGUAGE);
        }
        loadedLanguage = newLanguage;
    }

    private static String getCurrentLanguage() {
        return StringTranslate.getInstance().getCurrentLanguage();
    }
}