package xyz.johansson.reversi.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.Timer;
import xyz.johansson.reversi.model.Model;

/**
 * Class represnting a visual token on the game board.
 *
 * @author Tobias Johansson
 * @version 1.03, 3 Mar 2015
 */
class Token extends JPanel {

    /**
     * Color status value.
     */
    private int color;

    /**
     * Construct an empty Token (with background color).
     */
    Token() {
        color = Model.EMPTY;
        setBackground(View.BACKGROUND_COLOR); // repaints
    }

    /**
     * Paints each of the components in this container.
     *
     * @param g the graphics context
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Get the appropriate size for the figure
        int width = getWidth();
        int height = getHeight();

        g.setColor(intToColor());
        g.fillOval((int) (0.1 * width), (int) (0.1 * height),
                (int) (0.8 * width), (int) (0.8 * height));
    }

    /**
     * Get color status of Token.
     *
     * @return color status
     */
    int getColor() {
        return color;
    }

    /**
     * Set color status of Token. Highlight with backbackground if the new color
     * status value is not representing an empty Token.
     *
     * @param c value representing color status
     */
    void setColor(int c) {
        if (c != Model.EMPTY) {
            highlight();
        }
        color = c;
        repaint();
    }

    /**
     * Convert Tokens color status value to Color.
     *
     * @return Color representing the Tokens color status value
     */
    private Color intToColor() {
        switch (color) {
            case Model.EMPTY:
                return View.BACKGROUND_COLOR;
            case Model.WHITE:
                return Color.WHITE;
            case Model.BLACK:
                return Color.BLACK;
            default:
                throw new RuntimeException("Invalid color value: " + color);
        }
    }

    /**
     * Animation that highlights the background of a Token.
     */
    private void highlight() {
        setBackground(new Color(50, 75, 25)); // darker background color
        int delay = 1000; // milliseconds
        new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                setBackground(View.BACKGROUND_COLOR); // repaints
            }
        }).start();
    }
}
