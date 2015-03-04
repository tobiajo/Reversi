package xyz.johansson.reversi.controller;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import xyz.johansson.reversi.model.Model;
import xyz.johansson.reversi.view.View;

/**
 * Listner class for the menu.
 *
 * @author Tobias Johansson
 * @version 1.03, 3 Mar 2015
 */
public class MenuListener implements ActionListener {

    private Model model;
    private View view;

    /**
     * Construct a MenuListener.
     *
     * @param m the Model
     * @param v the View
     */
    MenuListener(Model m, View v) {
        this.model = m;
        this.view = v;
    }

    /**
     * Invoked when a menu button is pressed. Runtime is specified by the events
     * ActionCommand.
     *
     * @param e triggering ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String alternative = e.getActionCommand();
        switch (alternative) {
            case "newPvP":
                newPvP();
                break;
            case "newPvC":
                newPvC();
                break;
            case "save":
                save();
                break;
            case "open":
                open();
                break;
            case "exit":
                System.exit(0);
                break;
            case "about":
                about();
                break;
            default:
                throw new IllegalArgumentException(
                        "Invalid menu alternative: " + alternative);
        }
    }

    /**
     * Start new Player vs Player game session.
     */
    private void newPvP() {
        String msg = "Good luck!";
        JOptionPane.showMessageDialog(view, msg,
                "Player vs. Player", JOptionPane.PLAIN_MESSAGE);
        model.newPvP();
    }

    /**
     * Start new Player vs Computer game session.
     */
    private void newPvC() {
        String msg = "Player is white, "
                + "Computer is black.\n\nGood luck!";
        JOptionPane.showMessageDialog(view, msg, "Player vs. Computer",
                JOptionPane.PLAIN_MESSAGE);
        model.newPvC();
    }

    /**
     * Save a game session to a file.
     */
    private void save() {
        if (!model.getGameStarted()) {
            String msg = "No game session started.";
            JOptionPane.showMessageDialog(view, msg, "Save game failed",
                    JOptionPane.PLAIN_MESSAGE);
        } else {
            JFileChooser chooser = getJFileChooser();
            chooser.setSelectedFile(new File("game.data"));
            int returnVal = chooser.showSaveDialog(view);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                String path = chooser.getSelectedFile().getPath();
                if (!path.toLowerCase().endsWith(".data")) {
                    path += ".data";
                }
                try {
                    ObjectOutputStream output = new ObjectOutputStream(
                            new FileOutputStream(path));
                    output.writeObject(model.getPrivateDataField());
                } catch (IOException ex) {
                    Logger.getLogger(
                            Controller.class.getName()).log(
                                    Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(view, "Unable to write"
                            + " file.\n\nERROR - " + ex.getMessage(),
                            "Save game failed", JOptionPane.PLAIN_MESSAGE);
                }
            }
        }
    }

    /**
     * Open a saved game session from a file.
     */
    private void open() {
        JFileChooser chooser = getJFileChooser();
        int returnVal = chooser.showOpenDialog(view);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String path = chooser.getSelectedFile().getPath();
            try {
                ObjectInputStream input = new ObjectInputStream(
                        new FileInputStream(path));
                model.setPrivateDataField((Object[]) input.readObject());
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(
                        Controller.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(view, "Unable to read file."
                        + "\n\nERROR - " + ex.getMessage(),
                        "Open game failed", JOptionPane.PLAIN_MESSAGE);
            }
        }
    }

    /**
     * Gives information about the game in a popup box.
     */
    private void about() {
        String msg = "Reversi\n \n"
                + "Reversi is a strategy board game\n"
                + "with free and open source code.\n \n"
                + "Version: 1.03, 3 Mar 2015\n"
                + "Author: Tobias Johansson\n"
                + "Source code: http://reversi.johansson.xyz";
        JOptionPane.showMessageDialog(view, msg, "About Reversi",
                JOptionPane.INFORMATION_MESSAGE, new ImageIcon(
                        view.getJFrameIconImage().getScaledInstance(
                                150, 150, Image.SCALE_AREA_AVERAGING)));
    }

    /**
     * Create a JFileChooser with set current directory and create the
     * directory.
     *
     * @return a JFileChooser with set current directory
     */
    private JFileChooser getJFileChooser() {
        JFileChooser chooser = new JFileChooser();
        File dir = new File("sessions");
        dir.mkdir();
        chooser.setCurrentDirectory(dir);
        return chooser;
    }
}
