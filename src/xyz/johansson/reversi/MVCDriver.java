package xyz.johansson.reversi;

import xyz.johansson.reversi.model.Model;
import xyz.johansson.reversi.view.View;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 * Model-View-Controller Driver.
 *
 * @author Tobias Johansson
 * @version 1.03, 3 Mar 2015
 */
public class MVCDriver {

    /**
     * Main method for the project; start Reversi. Create Model and View (View
     * initiates Controller), add the View to a JFrame and add an Observer.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        // Create Model and View
        Model model = new Model();
        View view = new View(model); // view initiates Controller

        // Create a JFrame and add the View
        createJFrame(view);

        // Add Observer
        model.addObserver(view);
    }

    /**
     * Create a JFrame and add the View. Set window settings and an icon.
     *
     * @param view the View
     */
    private static void createJFrame(View view) {
        JFrame frame = new JFrame("Reversi"); // create a JFrame

        BufferedImage icon = null;
        try {
            icon = ImageIO.read(MVCDriver.class.getResource("icon.png"));
        } catch (IOException ex) {
            Logger.getLogger(MVCDriver.class.getName()).log(
                    Level.SEVERE, null, ex);
        }
        frame.setIconImage(icon);
        //com.apple.eawt.Application.getApplication().setDockIconImage(icon);

        frame.add(view); // add the View to frame
        frame.pack(); // causes frame to be sized to fit the preferred size
        frame.setResizable(false);
        frame.setLocationRelativeTo(null); // center frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true); // display the frame
    }
}
