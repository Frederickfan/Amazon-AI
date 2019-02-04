package amazons;

import org.junit.Test;

import static amazons.Piece.*;
import static org.junit.Assert.*;
import ucb.junit.textui;

/** The suite of all JUnit tests for the amazons package.
 *  @author Frederick Fan
 */
public class UnitTest {

    /** Run the JUnit tests in this package. Add xxxTest.class entries to
     *  the arguments of runClasses to run other JUnit tests. */
    public static void main(String[] ignored) {
        textui.runClasses(UnitTest.class);
    }

    /** Tests basic correctness of put and get on the initialized board. */
    @Test
    public void testBasicPutGet() {
        Board b = new Board();
        b.put(BLACK, Square.sq(3, 5));
        assertEquals(b.get(3, 5), BLACK);
        b.put(WHITE, Square.sq(9, 9));
        assertEquals(b.get(9, 9), WHITE);
        b.put(EMPTY, Square.sq(3, 5));
        assertEquals(b.get(3, 5), EMPTY);
    }

    /** Test legal move. */
    @Test
    public void testLegalMove() {
        Board b = new Board();
        b.makeMove(Square.sq("j", "4"), Square.sq("e", "9"),
                Square.sq("a", "9"));
        b.makeMove(Square.sq("g", "10"), Square.sq("e", "10"),
                Square.sq("a", "6"));
        b.makeMove(Square.sq("e", "9"), Square.sq("b", "9"),
                Square.sq("a", "10"));
        b.makeMove(Square.sq("e", "10"), Square.sq("b", "7"),
                Square.sq("a", "8"));
        b.makeMove(Square.sq("b", "9"), Square.sq("b", "8"),
                Square.sq("i", "1"));
        b.makeMove(Square.sq("d", "10"), Square.sq("b", "10"),
                Square.sq("b", "9"));
        b.makeMove(Square.sq("b", "8"), Square.sq("h", "2"),
                Square.sq("b", "8"));
        b.makeMove(Square.sq("b", "10"), Square.sq("j", "2"),
                Square.sq("b", "10"));
        b.makeMove(Square.sq("a", "4"), Square.sq("a", "1"),
                Square.sq("c", "1"));
        b.makeMove(Square.sq("j", "7"), Square.sq("g", "10"),
                Square.sq("b", "10"));

    }


    /** isUnblockedSquare test */
    @Test
    public void testUnblockedSquare() {
        Board b = new Board();
        boolean a = b.isUnblockedMove(Square.sq("j", "4"),
                Square.sq("e", "9"), Square.sq("a", "9"));
        assertTrue(a);
        b.makeMove(Square.sq("j", "4"), Square.sq("e", "9"),
                Square.sq("a", "9"));

        boolean a1 = b.isUnblockedMove(Square.sq("g", "10"),
                Square.sq("e", "10"), Square.sq("a", "6"));
        assertTrue(a1);
        b.makeMove(Square.sq("g", "10"), Square.sq("e", "10"),
                Square.sq("a", "6"));

        boolean a2 = b.isUnblockedMove(Square.sq("e", "9"),
                Square.sq("b", "9"), Square.sq("a", "10"));
        assertTrue(a2);
        b.makeMove(Square.sq("e", "9"), Square.sq("b", "9"),
                Square.sq("a", "10"));

        boolean a3 = b.isUnblockedMove(Square.sq("e", "10"),
                Square.sq("b", "7"), Square.sq("a", "8"));
        assertTrue(a3);
        b.makeMove(Square.sq("e", "10"), Square.sq("b", "7"),
                Square.sq("a", "8"));

        boolean a4 = b.isUnblockedMove(Square.sq("b", "9"),
                Square.sq("b", "8"), Square.sq("i", "1"));
        assertTrue(a4);
        b.makeMove(Square.sq("b", "9"), Square.sq("b", "8"),
                Square.sq("i", "1"));

        boolean a5 = b.isUnblockedMove(Square.sq("d", "10"),
                Square.sq("b", "10"), Square.sq("b", "9"));
        assertTrue(a5);
        b.makeMove(Square.sq("d", "10"), Square.sq("b", "10"),
                Square.sq("b", "9"));

        boolean a6 = b.isUnblockedMove(Square.sq("b", "8"),
                Square.sq("h", "2"), Square.sq("b", "8"));
        assertTrue(a6);
        b.makeMove(Square.sq("b", "8"), Square.sq("h", "2"),
                Square.sq("b", "8"));

        boolean a7 = b.isUnblockedMove(Square.sq("b", "10"),
                Square.sq("j", "2"), Square.sq("b", "10"));
        assertTrue(a7);
        b.makeMove(Square.sq("b", "10"), Square.sq("j", "2"),
                Square.sq("b", "10"));

        boolean a8 = b.isUnblockedMove(Square.sq("a", "4"),
                Square.sq("a", "1"), Square.sq("c", "1"));
        assertTrue(a8);
        b.makeMove(Square.sq("a", "4"), Square.sq("a", "1"),
                Square.sq("c", "1"));

    }


    /** Tests proper identification of legal/illegal queen moves. */
    @Test
    public void testIsQueenMove() {
        assertFalse(Square.sq(1, 5).isQueenMove(Square.sq(1, 5)));
        assertFalse(Square.sq(1, 5).isQueenMove(Square.sq(2, 7)));
        assertFalse(Square.sq(0, 0).isQueenMove(Square.sq(5, 1)));
        assertTrue(Square.sq(1, 1).isQueenMove(Square.sq(9, 9)));
        assertTrue(Square.sq(2, 7).isQueenMove(Square.sq(8, 7)));
        assertTrue(Square.sq(3, 0).isQueenMove(Square.sq(3, 4)));
        assertTrue(Square.sq(7, 9).isQueenMove(Square.sq(0, 2)));
    }

    /** Tests toString for initial board state and a smiling board state. :) */
    @Test
    public void testToString() {
        Board b = new Board();
        assertEquals(INIT_BOARD_STATE, b.toString());
        makeSmile(b);
        assertEquals(SMILE, b.toString());
    }


    @Test
    public void testSQ1() {
        Square d1 = Square.sq("d", "1");
        Square d1string = Square.sq("d1");
        Square d1s = Square.sq(3, 0);
        Square dis2 = Square.sq(3);
        assertEquals(d1s, dis2);
        assertEquals(d1s, d1string);
        assertEquals(d1s, d1s);
        assertEquals(d1s, d1);
    }

    private void makeSmile(Board b) {
        b.put(EMPTY, Square.sq(0, 3));
        b.put(EMPTY, Square.sq(0, 6));
        b.put(EMPTY, Square.sq(9, 3));
        b.put(EMPTY, Square.sq(9, 6));
        b.put(EMPTY, Square.sq(3, 0));
        b.put(EMPTY, Square.sq(3, 9));
        b.put(EMPTY, Square.sq(6, 0));
        b.put(EMPTY, Square.sq(6, 9));
        for (int col = 1; col < 4; col += 1) {
            for (int row = 6; row < 9; row += 1) {
                b.put(SPEAR, Square.sq(col, row));
            }
        }
        b.put(EMPTY, Square.sq(2, 7));
        for (int col = 6; col < 9; col += 1) {
            for (int row = 6; row < 9; row += 1) {
                b.put(SPEAR, Square.sq(col, row));
            }
        }
        b.put(EMPTY, Square.sq(7, 7));
        for (int lip = 3; lip < 7; lip += 1) {
            b.put(WHITE, Square.sq(lip, 2));
        }
        b.put(WHITE, Square.sq(2, 3));
        b.put(WHITE, Square.sq(7, 3));
    }

    static final String INIT_BOARD_STATE =
            "   - - - B - - B - - -\n"
                    +
            "   - - - - - - - - - -\n"
                    +
            "   - - - - - - - - - -\n"
                    +
            "   B - - - - - - - - B\n"
                    +
            "   - - - - - - - - - -\n"
                    +
            "   - - - - - - - - - -\n"
                    +
            "   W - - - - - - - - W\n"
                    +
            "   - - - - - - - - - -\n"
                    +
            "   - - - - - - - - - -\n"
                    +
            "   - - - W - - W - - -\n";

    static final String SMILE =
            "   - - - - - - - - - -\n"
                    +
            "   - S S S - - S S S -\n"
                    +
            "   - S - S - - S - S -\n"
                    +
            "   - S S S - - S S S -\n"
                    +
            "   - - - - - - - - - -\n"
                    +
            "   - - - - - - - - - -\n"
                    +
            "   - - W - - - - W - -\n"
                    +
            "   - - - W W W W - - -\n"
                    +
            "   - - - - - - - - - -\n"
                    +
            "   - - - - - - - - - -\n";

}

