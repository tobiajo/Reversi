package xyz.johansson.reversi.controller;

import xyz.johansson.reversi.model.Model;
import xyz.johansson.reversi.view.View;

/**
 * Controller class.
 *
 * @author Tobias Johansson
 * @version 1.03, 3 Mar 2015
 */
public class Controller {

    private Model model;
    private View view;
    private BoardListener boardListener;
    private MenuListener menuListener;

    /**
     * Construct a Controller, create a BoardListener and a MenuListener.
     *
     * @param m the model
     * @param v the view
     */
    public Controller(Model m, View v) {
        this.model = m;
        this.view = v;
        boardListener = new BoardListener(model, view);
        menuListener = new MenuListener(model, view);
    }

    //--------------------------------------------------------------------------
    // Public methods
    /**
     * Get boardListener.
     *
     * @return boardListener
     */
    public BoardListener getBoardListener() {
        return boardListener;
    }

    /**
     * Get menuListener.
     *
     * @return menuListener
     */
    public MenuListener getMenuListener() {
        return menuListener;
    }
}
