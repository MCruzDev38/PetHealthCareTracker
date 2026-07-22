package com.michelle;

import javax.swing.*;
import java.awt.*;

/**
 * A custom JPanel that displays a rounded rectangle with
 * a colored background and border.
 *
 * This class is used to create the rounded title panel
 * in the Pet Health Care Tracker graphical user interface.
 *
 * @author Michelle Cruz
 */

public class RoundedPanel extends JPanel {

    private final Color backgroundColor;
    private final int cornerRadius;

    /**
     * Creates a rounded panel with the specified background
     * color and corner radius.
     *
     * @param backgroundColor the background color of the panel
     * @param cornerRadius the radius used for the rounded corners
     */

    public RoundedPanel(Color backgroundColor, int cornerRadius) {
        this.backgroundColor = backgroundColor;
        this.cornerRadius = cornerRadius;

        setOpaque(false);
    }

    /**
     * Paints the rounded panel with a custom background
     * and border.
     *
     * @param graphics the Graphics object used for painting
     */

    @Override
    protected void paintComponent(Graphics graphics) {

        super.paintComponent(graphics);

        Graphics2D g2 = (Graphics2D) graphics.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        g2.setColor(backgroundColor);

        g2.fillRoundRect(
                0,
                0,
                getWidth(),
                getHeight(),
                cornerRadius,
                cornerRadius
        );

        g2.setColor(new Color(33, 66, 120));

        g2.setStroke(new BasicStroke(2));

        g2.drawRoundRect(
                1,
                1,
                getWidth() - 3,
                getHeight() - 3,
                cornerRadius,
                cornerRadius
        );

        g2.dispose();
    }
}