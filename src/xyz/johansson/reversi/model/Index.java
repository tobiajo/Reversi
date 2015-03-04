package xyz.johansson.reversi.model;

/**
 * Index class, representing coordinates on the board.
 *
 * @author Tobias Johansson
 * @version 1.03, 3 Mar 2015
 */
class Index {

    private int row, col;

    /**
     * Construct an Index from row and column coordinates.
     *
     * @param r row on the board
     * @param c column on the board
     */
    Index(int r, int c) {
        this.row = r;
        this.col = c;
    }

    /**
     * Get row coordinate.
     *
     * @return row
     */
    int getRow() {
        return row;
    }

    /**
     * Get column coordinate.
     *
     * @return col
     */
    int getCol() {
        return col;
    }

    /**
     * Check if Index is valid.
     *
     * @return true if Index is valid, false otherwise
     */
    boolean isValid() {
        return (row >= 0 && row < Model.ROWS && col >= 0 && col < Model.COLS);
    }
}
