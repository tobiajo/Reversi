package xyz.johansson.reversi.model;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Artificial intelligence (AI).
 *
 * @author Tobias Johansson
 * @version 1.03, 3 Mar 2015
 */
class AI {

    private Model model;

    /**
     * Constructs an AI.
     *
     * @param m the Model
     */
    AI(Model m) {
        this.model = m;
    }

    /**
     * Make the move that generates the most point.
     */
    void move() {
        // Key: points, Value: Index
        TreeMap<Integer, Index> moves = new TreeMap();

        // Check points for all possible moves and add them to the TreeMap
        for (int i = 0; i < Model.ROWS; i++) {
            for (int j = 0; j < Model.COLS; j++) {
                if (model.getBoard(i, j) == Model.EMPTY) {
                    // Check points for specific move and put it in a TreeMap
                    Index index = new Index(i, j);
                    moves.put(points(index, Model.BLACK), index);
                }
            }
        }

        // Retrive a move that gives the highest points.
        // lastKey(): Returns the last (highest) key currently in this map
        Index move = moves.get(moves.lastKey());

        // Make the move
        model.setBoard(move.getRow(), move.getCol());
    }

    /**
     * Return points after placement on specified Index.
     *
     * @param i Index
     * @param color color of token
     * @return points after placement on i for color
     */
    private int points(Index i, int color) {
        // Retrive a list of tokens that should be flipped if a move to the
        // specified index
        ArrayList<Index> flipList = model.flipList(i, color);

        // Clone the original board
        int[][] boardClone = new int[Model.ROWS][Model.COLS];
        for (int row = 0; row < Model.ROWS; row++) {
            for (int col = 0; col < Model.COLS; col++) {
                // Works because int is a primitive data type, not an object
                boardClone[row][col] = model.getBoard(row, col);
            }
        }

        // Flip tokens on the clone
        for (Index flip : flipList) {
            boardClone[flip.getRow()][flip.getCol()] = color;
        }

        // Calculate and return points
        int points = 0;
        for (int row = 0; row < Model.ROWS; row++) {
            for (int col = 0; col < Model.COLS; col++) {
                if (boardClone[row][col] == color) {
                    points++;
                }
            }
        }
        return points;
    }
}
