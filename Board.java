package amazons;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Formatter;

import static amazons.Piece.*;


/** The state of an Amazons Game.
 *  @author Frederick Fan
 */
class Board {

    /** The number of squares on a side of the board. */
    static final int SIZE = 10;

    /** Initializes a game board with SIZE squares on a side in the
     *  initial position. */
    Board() {
        init();
    }

    /** Initializes a copy of MODEL. */
    Board(Board model) {
        copy(model);
    }

    /** Copies MODEL into me. */
    void copy(Board model) {
        this.init();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                piecesSetup[i][j] = model.piecesSetup[i][j];
            }
        }
        for (int i = 0; i < arrayMoves.size(); i++) {
            arrayMoves.add(model.arrayMoves.get(i));
        }
        this._turn = model._turn;
        this._winner = model._winner;
        this._moves = model._moves;
        this.arrayMoves = model.arrayMoves;
    }

    /** Clears the board to the initial position. */
    void init() {
        piecesSetup = new Piece[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if ((i == 0 & j == 3)
                        || (i == 0 & j == 6)) {
                    piecesSetup[i][j] = WHITE;
                } else if ((i == 3 & j == 0)
                        || (i == 3 & j == 9)) {
                    piecesSetup[i][j] = WHITE;
                } else if ((i == 6 & j == 0)
                        || (i == 6 & j == 9)) {
                    piecesSetup[i][j] = BLACK;
                } else if ((i == 9 & j == 3)
                        || (i == 9 & j == 6)) {
                    piecesSetup[i][j] = BLACK;
                } else {
                    piecesSetup[i][j] = EMPTY;
                }
            }
        }
        _turn = WHITE;
        _winner = EMPTY;
        arrayMoves = new ArrayList<Move>();
    }

    /** Return the Piece whose move it is (WHITE or BLACK). */
    Piece turn() {
        return _turn;
    }

    /** Return the number of moves (that have not been undone) for this
     *  board. */
    int numMoves() {
        return arrayMoves.size(); }


    /** Return the winner in the current position, or null if the game is
     *  not yet finished. */
    Piece winner() {
        if (!legalMoves(_turn).hasNext()) {
            return _turn.opponent();
        }
        return null;
    }


    /** Return the contents the square at S. */
    final Piece get(Square s) {
        return get(s.col(), s.row());
    }

    /** Return the contents of the square at (COL, ROW), where
     *  0 <= COL, ROW <= 9. */
    final Piece get(int col, int row) {
        return piecesSetup[row][col];
    }

    /** Return the contents of the square at COL ROW. */
    final Piece get(char col, char row) {
        return get(col - 'a', row - '1');
    }

    /** Set square S to P. */
    final void put(Piece p, Square s) {
        put(p, s.col(), s.row());
    }

    /** Set square (COL, ROW) to P. */
    final void put(Piece p, int col, int row) {

        piecesSetup[row][col] = p;
        _winner = this.winner();
    }

    /** Set square COL ROW to P. */
    final void put(Piece p, char col, char row) {
        put(p, col - 'a', row - '1');
    }

    /** Return true iff FROM - TO is an unblocked queen move on the current
     *  board, ignoring the contents of ASEMPTY, if it is encountered.
     *  For this to be true, FROM-TO must be a queen move and the
     *  squares along it, other than FROM and ASEMPTY, must be
     *  empty. ASEMPTY may be null, in which case it has no effect. */
    boolean isUnblockedMove(Square from, Square to, Square asEmpty) {
        if (to == null || !from.isQueenMove(to)) {
            return false;
        } else {
            int[][] dir = Square.dir();
            int moveCol = dir[from.direction(to)][0];
            int moveRow = dir[from.direction(to)][1];
            int currRow = from.row() + moveRow;
            int currCol = from.col() + moveCol;
            while (currRow != to.row()
                    || currCol != to.col()) {
                if (get(currCol, currRow).equals(EMPTY)
                        || Square.sq(currCol, currRow).equals(asEmpty)) {
                    currRow += moveRow;
                    currCol += moveCol;
                } else {
                    return false;
                }
            }

            return (get(currCol, currRow).equals(EMPTY)
                    || Square.sq(currCol, currRow).equals(asEmpty));
        }

    }

    /** Return true iff FROM is a valid starting square for a move. */
    boolean isLegal(Square from) {
        return !(get(from).equals(EMPTY) || get(from).equals(SPEAR));

    }

    /** Return true iff FROM-TO is a valid first part of move, ignoring
     *  spear throwing. */
    boolean isLegal(Square from, Square to) {
        return isUnblockedMove(from, to, null);

    }

    /** Return true iff FROM-TO(SPEAR) is a legal move in the current
     *  position. */
    boolean isLegal(Square from, Square to, Square spear) {
        if (!isLegal(from)) {
            return false;
        }
        if (spear.equals(from)) {
            if (!isLegal(from, to)) {
                return false;
            }

            if (!isUnblockedMove(from, to, from)) {
                return false;
            }
        } else if (spear != from) {
            if (get(spear) != EMPTY) {
                return false;
            }
            if (!isUnblockedMove(from, to, spear)
                    || !isUnblockedMove(to, spear, from)) {
                return false;
            }
        }
        return true;
    }



    /** Return true iff MOVE is a legal move in the current
     *  position. */
    boolean isLegal(Move move) {
        boolean rightMatch = (turn() == get(move.from()));
        return rightMatch && isLegal(move.from(), move.to(), move.spear());
    }

    /** Move FROM-TO(SPEAR), assuming this is a legal move. */
    void makeMove(Square from, Square to, Square spear) {
        Move newMove = Move.mv(from, to, spear);
        arrayMoves.add(newMove);
        put(get(from), to);
        put(EMPTY, from);
        put(SPEAR, spear);
        _turn = _turn.opponent();
        if (!legalMoves(_turn).hasNext()) {
            _winner = _turn.opponent();
        }
    }

    /** Move according to MOVE, assuming it is a legal move. */
    void makeMove(Move move) {
        makeMove(move.from(), move.to(), move.spear());
    }

    /** Undo one move.  Has no effect on the initial board. */
    void undo() {
        Move removedOne = arrayMoves.get(arrayMoves.size() - 1);
        put(EMPTY, removedOne.spear());
        put(get(removedOne.to()), removedOne.from());
        put(EMPTY, removedOne.to());
        _turn = _turn.opponent();
        arrayMoves.remove(arrayMoves.size() - 1);
    }

    /** Return an Iterator over the Squares that are reachable by an
     *  unblocked queen move from FROM. Does not pay attention to what
     *  piece (if any) is on FROM, nor to whether the game is finished.
     *  Treats square ASEMPTY (if non-null) as if it were EMPTY.  (This
     *  feature is useful when looking for Moves, because after moving a
     *  piece, one wants to treat the Square it came from as empty for
     *  purposes of spear throwing.) */
    Iterator<Square> reachableFrom(Square from, Square asEmpty) {
        return new ReachableFromIterator(from, asEmpty);
    }

    /** Return an Iterator over all legal moves on the current board. */
    Iterator<Move> legalMoves() {
        return new LegalMoveIterator(_turn);
    }

    /** Return an Iterator over all legal moves on the current board for
     *  SIDE (regardless of whose turn it is). */
    Iterator<Move> legalMoves(Piece side) {
        return new LegalMoveIterator(side);
    }

    /** An iterator used by reachableFrom. */
    private class ReachableFromIterator implements Iterator<Square> {

        /** Iterator of all squares reachable by queen move from FROM,
         *  treating ASEMPTY as empty. */
        ReachableFromIterator(Square from, Square asEmpty) {
            _from = from;
            _dir = -1;
            _steps = 0;
            _asEmpty = asEmpty;
            toNext();
        }

        @Override
        public boolean hasNext() {
            return _dir < 8;
        }

        @Override
        public Square next() {
            Square dirMove = _from.queenMove(_dir, _steps);
            if (dirMove == null) {
                System.out.println(_dir);
                System.out.println("here");
            }
            toNext();
            return dirMove;
        }

        /** Advance _dir and _steps, so that the next valid Square is
         *  _steps steps in direction _dir from _from. */
        private void toNext() {
            while (_dir < 8) {
                _steps += 1;
                if (_from.queenMove(_dir, _steps) == null
                        || !((get(_from.queenMove(_dir, _steps)) == EMPTY)
                        || (_from.queenMove(_dir, _steps) == _asEmpty))) {
                    _dir += 1;
                    _steps = 0;
                } else {
                    return;
                }
            }
        }

        /** Starting square. */
        private Square _from;
        /** Current direction. */
        private int _dir;
        /** Current distance. */
        private int _steps;
        /** Square treated as empty. */
        private Square _asEmpty;
    }

    /** An iterator used by legalMoves. */
    private class LegalMoveIterator implements Iterator<Move> {

        /** All legal moves for SIDE (WHITE or BLACK). */
        LegalMoveIterator(Piece side) {
            _startingSquares = Square.iterator();
            _spearThrows = NO_SQUARES;
            _pieceMoves = NO_SQUARES;
            _fromPiece = side;
            toNext();
        }

        @Override
        public boolean hasNext() {
            boolean pStart = _startingSquares.hasNext();
            boolean pNext = _pieceMoves.hasNext();
            boolean pSpear = _spearThrows.hasNext();
            return pStart || pNext || pSpear;
        }

        @Override
        public Move next() {
            if (_start == null) {
                System.out.println("start is null");
            }
            if (_nextSquare == null) {
                System.out.println("nextsquaer is null");
            }
            if (_sp == null) {
                System.out.println("sp is null");
            }
            Move legalMove = Move.mv(_start, _nextSquare,  _sp);
            toNext();
            return legalMove;
        }

        /** Advance so that the next valid Move is
         *  _start-_nextSquare(sp), where sp is the next value of
         *  _spearThrows. */
        private void toNext() {
            if (!_spearThrows.hasNext()) {
                if (!_pieceMoves.hasNext()) {

                    Square checkSide = _startingSquares.next();
                    while (!get(checkSide).equals(_fromPiece)
                            && checkSide != null) {
                        if (!_startingSquares.hasNext()) {
                            return;
                        }
                        checkSide = _startingSquares.next();
                    }
                    _start = checkSide;

                    if (_start != null) {
                        _pieceMoves =
                                new Board.ReachableFromIterator(_start, null);
                        if (_pieceMoves.hasNext()) {
                            _nextSquare = _pieceMoves.next();
                            _spearThrows =
                                    new Board.ReachableFromIterator(
                                            _nextSquare, _start);
                            _sp = _spearThrows.next();
                        } else {
                            toNext();
                            return;
                        }
                        return;
                    }

                } else {
                    _nextSquare = _pieceMoves.next();
                    _spearThrows =
                            new Board.ReachableFromIterator(
                                    _nextSquare, _start);
                    _sp = _spearThrows.next();
                }
            } else {
                _sp = _spearThrows.next();
            }
        }

        /** Current Spearthrow position. **/
        private Square _sp;
        /** Color of side whose moves we are iterating. */
        private Piece _fromPiece;
        /** Current starting square. */
        private Square _start;
        /** Remaining starting squares to consider. */
        private Iterator<Square> _startingSquares;
        /** Current piece's new postion. */
        private Square _nextSquare;
        /** Remaining moves from _start to consider. */
        private Iterator<Square> _pieceMoves;
        /** Remaining spear throws from _piece to consider. */
        private Iterator<Square> _spearThrows;
    }

    @Override
    public String toString() {
        Formatter out = new Formatter();
        for (int row = 9; row >= 0; row--) {
            out.format("   ");
            for (char col = 0; col < 10; col++) {
                Piece pc = get(col, row);
                String p;
                if (pc.equals(EMPTY)) {
                    p = "-";
                } else if (pc.equals(WHITE)) {
                    p = "W";
                } else if (pc.equals(BLACK)) {
                    p = "B";
                } else {
                    p = "S";
                }
                if (col != 9) {
                    out.format("%s ", p);
                } else {
                    out.format("%s", p);
                }

            }
            out.format("%s", "\n");
        }
        return out.toString();
    }

    /** Arraylist for storing moves. */
    private ArrayList<Move> arrayMoves = new ArrayList<Move>();

    /** Number of moves. */
    private int _moves;

    /** An empty iterator for initialization. */
    private static final Iterator<Square> NO_SQUARES =
        Collections.emptyIterator();

    /** Piece whose turn it is (BLACK or WHITE). */
    private Piece _turn;
    /** Cached value of winner on this board, or EMPTY if it has not been
     *  computed. */
    private Piece _winner;

    /** Pieces initial placement. */
    private Piece[][] piecesSetup;
}
