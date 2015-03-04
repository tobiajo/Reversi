package xyz.johansson.reversi.model;

import java.util.ArrayList;
import java.util.Observable;

/**
 * Model class.
 *
 * @author Tobias Johansson
 * @version 1.03, 3 Mar 2015
 */
public class Model extends Observable {

    /**
     * Number of rows.
     */
    public static final int ROWS = 8;

    /**
     * Number of columns.
     */
    public static final int COLS = 8;

    /**
     * Representing an empty cell on the logical board.
     */
    public static final int EMPTY = 0;

    /**
     * Representing a white token on the logical board.
     */
    public static final int WHITE = 1;

    /**
     * Representing a black token on the logical board.
     */
    public static final int BLACK = 2;

    private enum Direction {

        UP_LEFT, UP, UP_RIGHT, RIGHT, DOWN_RIGHT, DOWN, DOWN_LEFT, LEFT;
    }

    private int[][] board; // holds the status for each cell on the board
    private boolean gameStarted;
    private boolean boardLocked;
    private boolean whitesTurn;
    private boolean PvP; // Player vs Player
    private int whitePoints;
    private int blackPoints;

    /**
     * Construct an empty Model that is locked until a new game is created.
     */
    public Model() {
        boardLocked = true;
    }

    //--------------------------------------------------------------------------
    // Public methods
    /**
     * Start a new Player vs Player game session. Intiate private data field and
     * notify Observer.
     */
    public void newPvP() {
        board = new int[ROWS][COLS];
        board[3][3] = WHITE;
        board[3][4] = BLACK;
        board[4][3] = BLACK;
        board[4][4] = WHITE;
        gameStarted = true;
        boardLocked = false;
        whitesTurn = true;
        PvP = true;
        whitePoints = 2;
        blackPoints = 2;

        // Notify the View
        setChanged();
        notifyObservers();
    }

    /**
     * Start a new Player vs Computer game session. Intiate private data field
     * and notify Observer.
     */
    public void newPvC() {
        board = new int[ROWS][COLS];
        board[3][3] = WHITE;
        board[3][4] = BLACK;
        board[4][3] = BLACK;
        board[4][4] = WHITE;
        gameStarted = true;
        boardLocked = false;
        whitesTurn = true;
        PvP = false;
        whitePoints = 2;
        blackPoints = 2;

        // Notify the View
        setChanged();
        notifyObservers();
    }

    /**
     * Make the AI move; make the move that gives most point using the
     * setBoard(int row, int col) method.
     */
    public void computerMove() {
        new AI(this).move();
    }

    /**
     * Check if somebody won.
     *
     * @return true if somebody won, false otherwise
     */
    public boolean somebodyWon() {
        return (whitePoints == 0 || blackPoints == 0
                || whitePoints + blackPoints == ROWS * COLS);
    }

    //--------------------------------------------------------------------------
    // Getters
    /**
     * Get value of board on specified row and column. Return EMPTY for an empty
     * cell, WHITE for a white token and BLACK for a black token.
     *
     * @param row row
     * @param col column
     * @return EMPTY for an empty cell, WHITE for a white token and BLACK for a
     * black token
     */
    public int getBoard(int row, int col) {
        if (!new Index(row, col).isValid()) {
            throw new ArrayIndexOutOfBoundsException("row: " + row
                    + ", col = " + col);
        }
        return board[row][col];
    }

    /**
     * Get if a game is started.
     *
     * @return true if a game is started, false otherwise
     */
    public boolean getGameStarted() {
        return gameStarted;
    }

    /**
     * Get status for if it is whites turn.
     *
     * @return true if whites turn, false otherwise
     */
    public boolean getWhitesTurn() {
        return whitesTurn;
    }

    /**
     * Get status for if Player vs Player game is ongoing.
     *
     * @return true if Player vs Player game is running, otherwise false
     */
    public boolean getPvP() {
        return PvP;
    }

    /**
     * Get whites points.
     *
     * @return whites point
     */
    public int getWhitePoints() {
        return whitePoints;
    }

    /**
     * Get blacks points.
     *
     * @return blacks points
     */
    public int getBlackPoints() {
        return blackPoints;
    }

    /**
     * Get the private data field of this Model.
     *
     * @return the private data field of this Model
     */
    public Object[] getPrivateDataField() {
        return new Object[]{board, gameStarted, boardLocked, whitesTurn, PvP,
            whitePoints, blackPoints};
    }

    //--------------------------------------------------------------------------
    // Setters
    /**
     * Set value of board on specified row and column for whose turn it is,
     * toggle whose turn and notify Observer. Flip tokens on board and update
     * points. Lock the board if somebody won.
     *
     * @param row row
     * @param col column
     * @return true if set is succeded, otherwise false
     */
    public boolean setBoard(int row, int col) {
        if (boardLocked || getBoard(row, col) != EMPTY) {
            return false;
        }

        // Check whose turn
        int color = whitesTurn ? WHITE : BLACK;

        // Set color
        board[row][col] = color;

        // Retrive a list of tokens that should be flipped
        ArrayList<Index> flipList = flipList(new Index(row, col), color);

        // Flip tokens
        for (Index flip : flipList) {
            board[flip.getRow()][flip.getCol()] = color;
        }

        // Calculate and set points
        int white = 0, black = 0;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (board[i][j] == WHITE) {
                    white++;
                }
                if (board[i][j] == BLACK) {
                    black++;
                }
            }
        }
        whitePoints = white;
        blackPoints = black;

        // Lock the board if somebody won
        if (somebodyWon()) {
            boardLocked = true;
        }

        // Toggle whose turn
        whitesTurn = !whitesTurn;

        // Notify the View
        setChanged();
        notifyObservers();
        return true;
    }

    /**
     * Set the private data field of this Model and notify Observer.
     *
     * @param objects the new private private data field for this Model
     * @throws java.lang.ClassNotFoundException if set fail
     */
    public void setPrivateDataField(Object[] objects)
            throws ClassNotFoundException {
        try {
            board = (int[][]) objects[0];
            gameStarted = (boolean) objects[1];
            boardLocked = (boolean) objects[2];
            whitesTurn = (boolean) objects[3];
            PvP = (boolean) objects[4];
            whitePoints = (int) objects[5];
            blackPoints = (int) objects[6];
        } catch (Exception ex) {
            throw new ClassNotFoundException();
        }
        // Notify the View
        setChanged();
        notifyObservers();
    }

    //--------------------------------------------------------------------------
    // Helper methods
    /**
     * Return a Index list of tokens that should be flipped for move to a
     * specified Index and color of the token.
     *
     * @param i index
     * @param color the color
     * @return a Index list of tokens that should be flipped for i and color
     */
    ArrayList<Index> flipList(Index i, int color) {
        // Traverse all directions, one at a time. If valid placement add
        // candidates if opposite color, when encounter the same color add all
        // candidates to a fliplist i.e. tokens that will be flipped.
        ArrayList<Index> flipList = new ArrayList();
        for (Direction dir : Direction.values()) {
            Index next = new Index(i.getRow(), i.getCol());
            ArrayList<Index> candidates = new ArrayList();
            while (true) {
                next = next(next, dir);
                if (!next.isValid()) {
                    break;
                }
                if (getBoard(next.getRow(), next.getCol()) == EMPTY) {
                    break;
                }
                if (getBoard(next.getRow(), next.getCol()) == color) {
                    flipList.addAll(candidates);
                    break;
                }
                candidates.add(next);
            }
        }
        return flipList;
    }

    /**
     * Return Index (coordiates) for next cell in specified direction.
     *
     * @param i Index
     * @param dir direction
     * @return Index for next cell after i in direction dir
     */
    private Index next(Index i, Direction dir) {
        switch (dir) {
            // Up
            case UP_LEFT:
                return new Index(i.getRow() - 1, i.getCol() - 1);
            case UP:
                return new Index(i.getRow() - 1, i.getCol());
            case UP_RIGHT:
                return new Index(i.getRow() - 1, i.getCol() + 1);

            // Right
            case RIGHT:
                return new Index(i.getRow(), i.getCol() + 1);

            // Down
            case DOWN_RIGHT:
                return new Index(i.getRow() + 1, i.getCol() + 1);
            case DOWN:
                return new Index(i.getRow() + 1, i.getCol());
            case DOWN_LEFT:
                return new Index(i.getRow() + 1, i.getCol() - 1);

            // Left
            case LEFT:
                return new Index(i.getRow(), i.getCol() - 1);

            default:
                throw new IllegalArgumentException(
                        "Invalid Direction: " + dir);
        }
    }
}
