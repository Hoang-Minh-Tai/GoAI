package player;

import core.Action;
import core.State;
import game.Go;

import java.util.List;

import static util.Util.getAllPossibleMoves;
import static util.Util.result;

public class AI extends Player {
    int maxDepth = 7;

    static int prune = 0;
    static int moveCalculated = 0;

    @Override
    public void update(Go go) {
        pause(1000);

        long timeStart = System.currentTimeMillis();
        Object[] ab = go.currentState.turn == 'w' ? abmin(go.currentState, -100, 100, 0) : abmax(go.currentState, -100, 100, 0);
        long timeFinish = System.currentTimeMillis();
        action = (Action) ab[1];
        long time = timeFinish - timeStart;
        printInfo(ab, time);

        prune = 0;
        moveCalculated = 0;
        System.out.println("--------------------------------------------------");
    }

    private void printInfo(Object[] ab, long time) {
        System.out.println("AI has chosen move: " + action + " for value: " + ab[0]);
        System.out.println("Max Depth: " + maxDepth);
        System.out.println("Move calculated: " + moveCalculated);
        System.out.println("Time consumed: " + time + "ms");
        System.out.println("Prune Count: " + prune);
    }

    public void pause(int milisecond) {
        try {
            Thread.sleep(milisecond);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public Object[] abmin(State state, int alpha, int beta, int depth) {
        moveCalculated++;

        if (depth > maxDepth) return new Object[]{getEvaluation(state), null};
        if (state.isTerminal) return new Object[]{getEvaluation(state), null};
        List<Action> moves = getAllPossibleMoves(state);
        int minValue = Integer.MAX_VALUE;
        Action bestMove = null;
        for (Action move : moves) {
            State new_state = result(move, state);
            char[][] new_board = new_state.board;
            Object[] abmax = abmax(new_state, alpha, beta, depth + 1);
            int result = (int) abmax[0];
            Action m = (Action) abmax[1];
            if (result < minValue) {
                minValue = result;
                bestMove = move;
            }
            if (minValue <= alpha) {
                prune++;
                return new Object[]{minValue, bestMove};
            }
            beta = Math.min(minValue, beta);
        }
        return new Object[]{minValue, bestMove};
    }

    public Object[] abmax(State state, int alpha, int beta, int depth) {
        moveCalculated++;
        if (state.isTerminal) return new Object[]{getEvaluation(state), null};
        List<Action> moves = getAllPossibleMoves(state);
        int maxValue = Integer.MIN_VALUE;
        Action bestMove = null;
        for (Action move : moves) {
            State new_state = result(move, state);
            Object[] abmin = abmin(new_state, alpha, beta, depth + 1);
            int result = (int) abmin[0];
            Action m = (Action) abmin[1];
            if (result > maxValue) {
                maxValue = result;
                bestMove = move;
            }
            if (maxValue >= beta) {
                prune++;
                return new Object[]{maxValue, bestMove};
            }
            alpha = Math.max(maxValue, alpha);
        }
        return new Object[]{maxValue, bestMove};

    }


    public int getEvaluation(State state) {
        char[][] board = state.board;
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

