package com.raphydaphy.vitality.util;

import net.minecraft.util.text.translation.I18n;

public class TextHelper
{
	/*
	 * Formats text using the minecraft formatting system
	 * Allows sytax such as &3 to change colors in string
	 */
    public static String getFormattedText(String string)
    {
        return string.replaceAll("&", "\u00A7");
    }

    /*
     * Localizes text to the language set in the users settings
     */
    public static String localize(String input, Object... format)
    {
        return I18n.translateToLocalFormatted(input, format);
    }
    
    /*
     * Checks if the required lang file is avalable for translation
     * & if the lang file has the key required for translation
     */
    public static boolean canTranslate(String key)
    {
        return I18n.canTranslate(key);
    }
}