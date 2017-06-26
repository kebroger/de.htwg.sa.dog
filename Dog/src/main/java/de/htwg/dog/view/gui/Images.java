/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.htwg.dog.view.gui;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
//import org.apache.log4j.Logger;

/**
 *
 * @author kev
 */
public class Images {

    public static final Map<String, BufferedImage> imageList = new HashMap<>();
    //private static final Logger LOGGER = Logger.getLogger("de.htwg.dog.View.Gui.Images");

    private Images() {
    }

    /*static {
        String path = "resources/";

        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        for (File f : listOfFiles) {
            if (f.isFile()) {
                String name = f.getName().substring(0, f.getName().indexOf("."));
                try {
                    imageList.put(name, ImageIO.read(f));
                } catch (IOException ex) {
                    //LOGGER.info(ex);
                }
            }
        }
    }*/

    /**
     * scale image
     *
     * @param sbi image to scale
     * @param imageType type of image
     * @param dWidth width of destination image
     * @param dHeight height of destination image
     * @return scaled image
     */
    public static BufferedImage scale(BufferedImage sbi, int imageType, int dWidth, int dHeight) {
        BufferedImage dbi = null;
        if (sbi != null) {
            dbi = new BufferedImage(dWidth, dHeight, imageType);
            Graphics2D g = dbi.createGraphics();
            double scale = dWidth / (double) sbi.getWidth();
            AffineTransform at = AffineTransform.getScaleInstance(scale, scale);
            g.drawRenderedImage(sbi, at);
        }
        return dbi;
    }
}
