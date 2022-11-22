package core;

import util.Util;

public class State {
   public char[][] board;
   public int[] koPos;
   public char turn;
   public boolean isTerminal;

   public boolean lastPass;

    public State(char[][] board, int[] koPos, char turn) {
        this.board = board;
        this.koPos = koPos;
        this.turn = turn;
    }

    public static State copyOf(State state) {
        char[][] cloneBoard = new char[state.board.length][state.board.length];
        for (int i = 0; i < cloneBoard.length; i++) {
            for (int j = 0; j < cloneBoard.length; j++) {
                cloneBoard[i][j] = state.board[i][j];
            }
        }
        return new State(cloneBoard, state.koPos == null ? null : new int[] {state.koPos[0],state.koPos[1]}, state.turn);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int x = 0; x < board.length; x++) {
            builder.append('[');
            for (int y = 0; y < board.length; y++) {
                builder.append(board[y][x]).append(", ");
            }
            builder.deleteCharAt(builder.length()-1).deleteCharAt(builder.length()-1).append("]\n");
        }
        return builder.toString();
    }

    public static State getTest() {
        char[][] board = new char[3][3];
        board[0][1] = 'w';
        board[1][0] = 'w';
//        board[0][0] ='b';
        return new State(board,null,'b');
    }

    public static State getStartState(int boardSize) {
        return new State(new char[boardSize][boardSize], null, 'b');
    }


    public static boolean isTerminalState(State state) {
        if (Util.getAllPossibleMoves(state).size() == 0) return true;
        return false;
    }

    public int getEvaluation() {
        int score = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                char piece = board[i][j];
                if (piece == 'b') score++;
                if (piece == 'w') score--;
            }
        }
        return score;
    }
}
