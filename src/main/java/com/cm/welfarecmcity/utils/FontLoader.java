package com.cm.welfarecmcity.utils;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class FontLoader {
    public static void loadFont() {
        try {
            // Specify the path to the font file
            String fontPath = "resources/fonts/TH SarabunPSK.ttf";

            // Load the font
            File fontFile = new File(fontPath);
            if (fontFile.exists()) {
                Font font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(font);
                System.out.println("Font registered: " + font.getFontName());
            } else {
                System.err.println("Font file not found at: " + fontPath);
            }
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
    }
}