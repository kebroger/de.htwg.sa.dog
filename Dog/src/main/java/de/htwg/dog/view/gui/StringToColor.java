package de.htwg.dog.view.gui;

import java.awt.*;
import java.lang.reflect.Field;

public class StringToColor {

    public enum Colors {
        YELLOW, BLUE, GREEN, CYAN, GREY
    }

    /**
     * Converts a given string into a color.
     *
     * @param value
     *          the string, either a name or a hex-string.
     * @return the color.
     */
    public static Color intToColor(final int number) {

        String value = Colors.values()[number].name();

        if (value == null) {
            return Color.black;
        }
        try {
            // get color by hex or octal value
            return Color.decode(value);
        } catch (NumberFormatException nfe) {
            // if we can't decode lets try to get it by name
            try {
                // try to get a color by name using reflection
                final Field f = Color.class.getField(value);

                return (Color) f.get(null);
            } catch (Exception ce) {
                // if we can't get any color return black
                return Color.black;
            }
        }
    }
}
