package com.michelle;

import javax.swing.*;
import java.awt.*;

public class RoundedPanel extends JPanel {

    private final Color backgroundColor;
    private final int cornerRadius;

    public RoundedPanel(Color backgroundColor, int cornerRadius) {
        this.backgroundColor = backgroundColor;
        this.cornerRadius = cornerRadius;

        setOpaque(false);
    }

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