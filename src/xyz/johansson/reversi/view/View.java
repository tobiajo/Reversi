package xyz.johansson.reversi.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.Observable;
import java.util.Observer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import xyz.johansson.reversi.controller.Controller;
import xyz.johansson.reversi.model.Model;

/**
 * View class.
 *
 * @author Tobias Johansson
 * @version 1.03, 3 Mar 2015
 */
public class View extends JPanel implements Observer {

    /**
     * Background color for the game board.
     */
    static final Color BACKGROUND_COLOR = new Color(100, 150, 50);

    private Model model;
    private Token[][] tokens;
    private JLabel whitePointsLabel;
    private JLabel blackPointsLabel;

    /**
     * Construct and initiate the View and the Controller.
     *
     * @param m the Model
     */
    public View(Model m) {
        super(new BorderLayout()); // BorderLayout in JFrame
        this.model = m;

        Controller controller = new Controller(model, this); // init Controller

        /* NORTH panel: Menu bar */
        JPanel northPanel = new JPanel(new GridLayout());
        add(northPanel, BorderLayout.NORTH);

        JMenuBar menuBar = new JMenuBar(); // create menu row
        northPanel.add(menuBar);

        JMenu gameMenu = new JMenu("Game"); // create Game menu
        menuBar.add(gameMenu);

        JMenu newGameMenu = new JMenu("New game"); // sub menu: New game
        gameMenu.add(newGameMenu);
        JMenuItem newPvPItem = new JMenuItem("Player vs. Player");
        newGameMenu.add(newPvPItem);
        newPvPItem.setActionCommand("newPvP");
        newPvPItem.addActionListener(controller.getMenuListener());
        JMenuItem newPvCItem = new JMenuItem("Player vs. Computer");
        newGameMenu.add(newPvCItem);
        newPvCItem.setActionCommand("newPvC");
        newPvCItem.addActionListener(controller.getMenuListener());
        gameMenu.addSeparator();

        JMenuItem saveItem = new JMenuItem("Save"); // Save
        gameMenu.add(saveItem);
        saveItem.setActionCommand("save");
        saveItem.addActionListener(controller.getMenuListener());
        JMenuItem openItem = new JMenuItem("Open"); // Open
        gameMenu.add(openItem);
        openItem.setActionCommand("open");
        openItem.addActionListener(controller.getMenuListener());
        gameMenu.addSeparator();

        JMenuItem exitItem = new JMenuItem("Exit"); // Exit
        gameMenu.add(exitItem);
        exitItem.setActionCommand("exit");
        exitItem.addActionListener(controller.getMenuListener());

        JMenu helpMenu = new JMenu("Help"); // create Help menu
        menuBar.add(helpMenu);
        JMenuItem aboutItem = new JMenuItem("About Reversi"); // About
        helpMenu.add(aboutItem);
        aboutItem.setActionCommand("about");
        aboutItem.addActionListener(controller.getMenuListener());

        /* CENTER panel: Game board */
        JPanel centerPanel = new JPanel(new GridLayout(Model.ROWS, Model.COLS));
        add(centerPanel, BorderLayout.CENTER);

        // Add a button for each cell on the board and a Token on each button
        tokens = new Token[Model.ROWS][Model.COLS];
        for (int i = 0; i < Model.ROWS; i++) {
            for (int j = 0; j < Model.COLS; j++) {
                JButton cell = new JButton();
                centerPanel.add(cell);
                tokens[i][j] = new Token();
                cell.add(tokens[i][j]);
                cell.setPreferredSize(new Dimension(60, 60));
                cell.setBorder(BorderFactory.createLineBorder(Color.black));
                cell.setActionCommand(Integer.toString(i * Model.COLS + j));
                cell.addActionListener(controller.getBoardListener());
            }
        }

        /* SOUTH panel: Points display */
        JPanel southPanel = new JPanel(new GridLayout());
        add(southPanel, BorderLayout.SOUTH);
        whitePointsLabel = new JLabel();
        southPanel.add(whitePointsLabel);
        blackPointsLabel = new JLabel();
        southPanel.add(blackPointsLabel);
    }

    //--------------------------------------------------------------------------
    // Public methods
    /**
     * This method is called whenever the observed object is changed. An
     * application calls an Observable object's notifyObservers method to have
     * all the object's observers notified of the change.
     *
     * @param o the observable object
     * @param arg an argument passed to the notifyObservers method
     */
    @Override
    public void update(Observable o, Object arg) {
        for (int i = 0; i < Model.ROWS; i++) {
            for (int j = 0; j < Model.COLS; j++) {
                if (tokens[i][j].getColor() != model.getBoard(i, j)) {
                    tokens[i][j].setColor(model.getBoard(i, j));
                }
            }
        }
        if (model.somebodyWon()) {
            whitePointsLabel.setText("White: " + model.getWhitePoints());
            blackPointsLabel.setText("Black: " + model.getBlackPoints());
            if (model.getPvP()) {
                String winner;
                if (model.getWhitePoints() > model.getBlackPoints()) {
                    winner = "White";
                } else if (model.getWhitePoints() == model.getBlackPoints()) {
                    winner = "nobody (draw)";
                } else {
                    winner = "Black";
                }
                JOptionPane.showMessageDialog(this, "Congratulations, " + winner
                        + " won!", "Game finished", JOptionPane.PLAIN_MESSAGE);
            } else {
                String msg;
                if (model.getWhitePoints() > model.getBlackPoints()) {
                    msg = "Congratulations, you won!";
                } else if (model.getWhitePoints() == model.getBlackPoints()) {
                    msg = "Congratulations, nobody won (draw)!";
                } else {
                    msg = "Sorry, you lost. Better luck next time!";
                }
                JOptionPane.showMessageDialog(this, msg, "Game finished",
                        JOptionPane.PLAIN_MESSAGE);
            }
        } else {

            String whiteTurn = "", blackTurn = "";
            if (model.getWhitesTurn()) {
                whiteTurn = " (Your turn)";
            } else {
                blackTurn = " (Your turn)";
            }
            whitePointsLabel.setText("White: " + model.getWhitePoints()
                    + whiteTurn);
            blackPointsLabel.setText("Black: " + model.getBlackPoints()
                    + blackTurn);
        }
    }

    /**
     * Returns the image to be displayed as the icon for this window's ancestor
     * frame.
     *
     * @return the icon image for this window's ancestors frame, or null if this
     * frame doesn't have an icon image
     */
    public Image getJFrameIconImage() {
        return ((JFrame) SwingUtilities.getWindowAncestor(this)).getIconImage();
    }
}
