package amazons;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static amazons.Utils.*;

/** Represents a position on an Amazons board.  Positions are numbered
 *  from 0 (lower-left corner) to 99 (upper-right corner).  Squares
 *  are immutable and unique: there is precisely one square created for
 *  each distinct position.  Clients create squares using the factory method
 *  sq, not the constructor.  Because there is a unique Square object for each
 *  position, you can freely use the cheap == operator (rather than the
 *  .equals method) to compare Squares, and the program does not waste time
 *  creating the same square over and over again.
 *  @author Frederick Fan
 */
final class Square {
    /** Returns the constant 97.*/
    public static final int NINESEVEN = 97;

    /** Returns the constant 10.*/
    public static final int TEN = 10;

    /** The regular expression for a square designation (e.g.,
     *  a3). For convenience, it is in parentheses to make it a
     *  group.  This subpattern is intended to be incorporated into
     *  other pattern that contain square designations (such as
     *  patterns for moves). */
    static final String SQ = "([a-j](?:[1-9]|10))";

    /** Return my row position, where 0 is the bottom row. */
    int row() {
        return _row;
    }

    /** Return my column position, where 0 is the leftmost column. */
    int col() {
        return _col;
    }

    /** Return my index position (0-99).  0 represents square a1, and 99
     *  is square j10. */
    int index() {
        return _index;
    }

    /** Method for giving access to DIR. @return dir*/
    static int[][] dir() {
        return DIR;
    }

    /** Return true iff THIS - TO is a valid queen move. */
    boolean isQueenMove(Square to) {
        int moveDisX = Math.abs(to.col() - this.col());
        int moveDisY = Math.abs(to.row() - this.row());
        if (this.equals(to)) {
            return false;
        } else if (to.row() == this.row()
                || to.col() == this.col()) {
            return true;
        } else {
            return (moveDisX == moveDisY);
        }
    }

    /** Definitions of direction for queenMove.  DIR[k] = (dcol, drow)
     *  means that to going one step from (col, row) in direction k,
     *  brings us to (col + dcol, row + drow). */
    private static final int[][] DIR = {
        { 0, 1 }, { 1, 1 }, { 1, 0 }, { 1, -1 },
        { 0, -1 }, { -1, -1 }, { -1, 0 }, { -1, 1 }
    };

    /** Return the Square that is STEPS>0 squares away from me in direction
     *  DIR, or null if there is no such square.
     *  DIR = 0 for north, 1 for northeast, 2 for east, etc., up to 7 for
     *  northwest. If DIR has another value, return null. Thus, unless the
     *  result is null the resulting square is a queen move away from me. */
    Square queenMove(int dir, int steps) {
        if (dir >= DIR.length || dir < 0) {
            return null;
        } else if (this.col()  + steps * DIR[dir][0] >= Board.SIZE
                || this.row() + steps * DIR[dir][1] >= Board.SIZE
                || this.col()  + steps * DIR[dir][0] < 0
                || this.row() + steps * DIR[dir][1] < 0) {
            return null;
        } else {
            return sq(this.col()
                    + steps * DIR[dir][0], this.row() + steps * DIR[dir][1]);
        }
    }

    /** Return the direction (an int as defined in the documentation
     *  for queenMove) of the queen move THIS-TO. */
    int direction(Square to) {
        assert isQueenMove(to);
        int disVertical = to.row() - this.row();
        int disHorizontal = to.col() - this.col();
        if (this.col() == to.col()
                && disVertical > 0) {
            return 0;
        } else if (this.col() == to.col()
                && disVertical < 0) {
            return 4;
        } else if (this.row() == to.row()
                && disHorizontal > 0) {
            return 2;
        } else if (this.row() == to.row()
                && disHorizontal < 0) {
            return 6;
        } else if (disVertical > 0
                && disHorizontal > 0) {
            return 1;
        } else if (disVertical < 0
                && disHorizontal > 0) {
            return 3;
        } else if (disVertical > 0
                && disHorizontal < 0) {
            return 7;
        } else {
            return 5;
        }
    }

    @Override
    public String toString() {
        return _str;
    }

    /** Return true iff COL ROW is a legal square. */
    static boolean exists(int col, int row) {
        return row >= 0 && col >= 0 && row < Board.SIZE && col < Board.SIZE;
    }

    /** Return the (unique) Square denoting COL ROW. */
    static Square sq(int col, int row) {
        if (!exists(row, col)) {
            throw error("row or column out of bounds");
        }
        int index = 10 * row + col;
        return sq(index);
    }

    /** Return the (unique) Square denoting the position with index INDEX. */
    static Square sq(int index) {
        return SQUARES[index];
    }

    /** Return the (unique) Square denoting the position COL ROW, where
     *  COL ROW is the standard text format for a square (e.g., a4). */
    static Square sq(String col, String row) {
        int colInt = (int) col.charAt(0) - NINESEVEN;
        int rowInt = Integer.parseInt(row);
        return sq(colInt, rowInt - 1);
    }

    /** Return the (unique) Square denoting the position in POSN, in the
     *  standard text format for a square (e.g. a4). POSN must be a
     *  valid square designation. */
    static Square sq(String posn) {
        assert posn.matches(SQ);
        String col = Character.toString(posn.charAt(0));
        String row = posn.substring(1, posn.length());
        return sq(col, row);
    }

    /** Return an iterator over all Squares. */
    static Iterator<Square> iterator() {
        return SQUARE_LIST.iterator();
    }

    /** Return the Square with index INDEX. */
    private Square(int index) {
        _index = index;
        _row = index / TEN;
        _col = index - _row * TEN;
        char c = (char) (NINESEVEN + _col);
        _str = String.format("%c%d", c, _row + 1);
    }

    /** The cache of all created squares, by index. */
    private static final Square[] SQUARES =
        new Square[Board.SIZE * Board.SIZE];

    /** SQUARES viewed as a List. */
    private static final List<Square> SQUARE_LIST = Arrays.asList(SQUARES);

    static {
        for (int i = Board.SIZE * Board.SIZE - 1; i >= 0; i -= 1) {
            SQUARES[i] = new Square(i);
        }
    }

    /** My index position. */
    private final int _index;

    /** My row and column (redundant, since these are determined by _index). */
    private final int _row, _col;

    /** My String denotation. */
    private final String _str;

}
