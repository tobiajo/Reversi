package xyz.johansson.reversi.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import xyz.johansson.reversi.model.Model;
import xyz.johansson.reversi.view.View;

/**
 * Listner class for the board.
 *
 * @author Tobias Johansson
 * @version 1.03, 3 Mar 2015
 */
public class BoardListener implements ActionListener {

    private Model model;
    private View view;

    /**
     * Construct a BoardListener.
     *
     * @param m the Model
     * @param v the View
     */
    BoardListener(Model m, View v) {
        this.model = m;
        this.view = v;
    }

    /**
     * Inovked when a cell on the board is clicked. Try to set the cell in the
     * Model. If player move are succeeded and if PvC is ongoing, after make a
     * computer move. The event is expected to have the following ActionCommand
     * (row * Model.COLS + col) for identification of clicked cell.
     *
     * @param e triggering ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        int position = Integer.valueOf(e.getActionCommand());
        int row = position / Model.COLS;
        int col = position % Model.COLS;
        if (model.setBoard(row, col) && !model.getPvP()) {
            model.computerMove(); // if player set is succeded and PvC ongoing
        }
    }
}
