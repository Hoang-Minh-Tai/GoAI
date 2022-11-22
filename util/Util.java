package util;

import core.Action;
import core.State;

import java.util.LinkedList;

import static game.Go.BOARD_SIZE;

public class Util {


    /**
     * @return A list of all possible actions from state
     */
    public static LinkedList<Action> getAllPossibleMoves(State state) {
        LinkedList<Action> list = new LinkedList<>();
        char[][] board = state.board;
        int[] koPos = state.koPos;

        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board.length; y++) {
                char cell = board[x][y];
                if (cell != '\u0000') continue;
                if (koPos != null && x == koPos[0] && y == koPos[1]) continue;
                board[x][y] = state.turn;

                if (!(boolean) isCaptureMove(state, new Action(x, y))[0]) {
                    if (!hasLiberty(state, new int[]{x, y}, state.turn, new LinkedList<>())) {
                        board[x][y] = '\u0000';
                        continue;
                    }
                }
                board[x][y] = '\u0000';


                list.add(new Action(x, y));
            }
        }
        list.add(new Action(true));
        return list;
    }

    /**
     * Apply action to current state
     *
     * @param action
     * @param state
     * @return new state after action is taken in current state
     */
    public static State result(Action action, State state) {
        State cloneState = State.copyOf(state);
        cloneState.koPos = null;
        if (action.pass) {
            cloneState.turn = cloneState.turn == 'b' ? 'w' : 'b';
            if (state.lastPass) {
                cloneState.isTerminal = true;
            }
            else cloneState.lastPass = true;
            return cloneState;
        }
        cloneState.lastPass = false;
        char[][] board = cloneState.board;
        int[] move = action.pos;

        board[move[0]][move[1]] = cloneState.turn;
        Object[] isCapture = isCaptureMove(cloneState, action);
        if ((boolean) isCapture[0]) {
            boolean[] captureDirection = (boolean[]) isCapture[1];
//            System.out.println("Capture direct: " + Arrays.toString(captureDirection));
            if (captureDirection[0]) capture(cloneState, new int[]{move[0], move[1] - 1}, new LinkedList<>());
            if (captureDirection[1]) capture(cloneState, new int[]{move[0] + 1, move[1]}, new LinkedList<>());
            if (captureDirection[2]) capture(cloneState, new int[]{move[0], move[1] + 1}, new LinkedList<>());
            if (captureDirection[3]) capture(cloneState, new int[]{move[0] - 1, move[1]}, new LinkedList<>());

            int[] countLiberty = countLibertyOfSinglePiece(move[0], move[1], board);
            if (countLiberty[0] == 1) {
                cloneState.koPos = new int[]{countLiberty[1], countLiberty[2]};
            }
        }
//        System.out.println("After take action");
//        System.out.println(cloneState);
        cloneState.turn = cloneState.turn == 'b' ? 'w' : 'b';
        return cloneState;
    }

    public static void capture(State state, int[] pos, LinkedList<int[]> frontier) {
        if (pos[0] < 0 || pos[0] >= BOARD_SIZE) return;
        if (pos[1] < 0 || pos[1] >= BOARD_SIZE) return;
        char[][] board = state.board;
        char target = board[pos[0]][pos[1]];
        if (target == '\u0000') return;
        if (target == state.turn) return;
        if (listContains(frontier, pos)) return;
        frontier.add(pos);
        board[pos[0]][pos[1]] = '\u0000';
        capture(state, new int[]{pos[0], pos[1] - 1}, frontier);
        capture(state, new int[]{pos[0] + 1, pos[1]}, frontier);
        capture(state, new int[]{pos[0], pos[1] + 1}, frontier);
        capture(state, new int[]{pos[0] - 1, pos[1]}, frontier);

    }

    public static Object[] isCaptureMove(State state, Action action) {
        char[][] board = state.board;
        int[] move = action.pos;
        char turn = state.turn;
        int x = move[0], y = move[1];
        boolean N = false, E = false, S = false, W = false;
        if (y > 0)
            N = (board[x][y - 1] == (turn == 'b' ? 'b' : 'w')) ? false : !hasLiberty(state, new int[]{x, y - 1}, turn == 'b' ? 'w' : 'b', new LinkedList<>());
        if (x < BOARD_SIZE - 1)
            E = (board[x + 1][y] == (turn == 'b' ? 'b' : 'w')) ? false : !hasLiberty(state, new int[]{x + 1, y}, turn == 'b' ? 'w' : 'b', new LinkedList<>());
        if (y < BOARD_SIZE - 1)
            S = (board[x][y + 1] == (turn == 'b' ? 'b' : 'w')) ? false : !hasLiberty(state, new int[]{x, y + 1}, turn == 'b' ? 'w' : 'b', new LinkedList<>());
        if (x > 0)
            W = (board[x - 1][y] == (turn == 'b' ? 'b' : 'w')) ? false : !hasLiberty(state, new int[]{x - 1, y}, turn == 'b' ? 'w' : 'b', new LinkedList<>());
        return new Object[]{N || E || S || W, new boolean[]{N, E, S, W}};
    }


    /**
     * Use DFS to find if current piece at position pos has liberty points, and add all the linked piece to list;
     *
     * @param state core.State
     * @param pos   int[]
     * @return boolean if piece at pos has liberty
     */
    public static boolean hasLiberty(State state, int[] pos, char color, LinkedList<int[]> frontier) {
        if (pos[0] < 0 || pos[0] >= BOARD_SIZE) return false;
        if (pos[1] < 0 || pos[1] >= BOARD_SIZE) return false;
        char[][] board = state.board;
        char target = board[pos[0]][pos[1]];
        if (target == '\u0000') return true;
        if (target != color) return false;
        if (listContains(frontier, pos)) return false;
        frontier.add(pos);
        // Check liberty at 4 directions from current position

        return hasLiberty(state, new int[]{pos[0] - 1, pos[1]}, color, frontier)
               || hasLiberty(state, new int[]{pos[0], pos[1] - 1}, color, frontier)
               || hasLiberty(state, new int[]{pos[0], pos[1] + 1}, color, frontier)
               || hasLiberty(state, new int[]{pos[0] + 1, pos[1]}, color, frontier);
    }


    /**
     * Check if position is in the list
     *
     * @param list LinkedList<int[]>
     * @param pos  int[]
     * @return
     */
    public static boolean listContains(LinkedList<int[]> list, int[] pos) {
        return list.stream().anyMatch(position -> position[0] == pos[0] && position[1] == pos[1]);
    }


    public static int[] countLibertyOfSinglePiece(int x, int y, char[][] board) {
        int[] res = new int[3];
        char turn = board[x][y] == 'b' ? 'w' : 'b';
        if (y + 1 < board.length && board[x][y+1] != turn ) {
            res[0]++;
            res[1] = x;
            res[2] = y + 1;
        }
        if (y > 0 && board[x][y - 1] != turn) {
            res[0]++;
            res[1] = x;
            res[2] = y - 1;
        }
        if (x + 1 < board.length && board[x + 1][y] != turn) {
            res[0]++;
            res[1] = x + 1;
            res[2] = y;
        }
        if (x > 0 && board[x - 1][y] != turn) {
            res[0]++;
            res[1] = x - 1;
            res[2] = y;
        }
        return res;
    }


}
