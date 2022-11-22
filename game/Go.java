package game;

import player.AI;
import util.Util;
import core.Action;
import core.State;
import player.Human;
import player.Player;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;


public class Go extends JPanel {
    public static final int BOARD_SIZE = 4;
    public static final int UNIT = Window.width / (BOARD_SIZE + 1);
    public int pieceWidth = UNIT * 80 / 100;
    public State currentState = State.getStartState(BOARD_SIZE);
    Input input = new Input(this);
    Player currentPLayer;
    Player human = new Human(input);
    Player AI = new AI();
    public static boolean endGame = false;


    public Go() {
        currentPLayer = AI;
    }

    public void update() {
        if (endGame) return;
        currentPLayer.update(this);
        if (currentPLayer.action != null) {
            currentPLayer.generateAllPossibleMoves(this);
            // Invalid move
            if (!currentPLayer.action.pass && currentPLayer.possibleMoves.stream().noneMatch(action ->
                    action.getX() == currentPLayer.action.getX() && action.getY() == currentPLayer.action.getY())) {
                currentPLayer.action = null;
                return;
            }
            applyMove(currentPLayer.action);
            currentPLayer.action = null;
            currentPLayer = currentPLayer == human ? AI : human;
        }
    }

    public void render() {
        repaint();
    }

    public void applyMove(Action action) {
        currentState = Util.result(action, currentState);
        if (currentState.isTerminal) {
            endGame = true;
            System.out.println((currentState.turn == 'b' ? "White" : "Black") + " wins!");
            System.out.println("-----------------------------------------------------");
            System.out.println("-----------------------------------------------------");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Paint background
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.white);
        for (int i = 0; i < getWidth() - UNIT; i += UNIT) {
            g.drawLine(i + UNIT, UNIT, i + UNIT, getHeight() - UNIT);
            g.drawLine(UNIT, i + UNIT, getWidth() - UNIT, i + UNIT);
        }

        // Paint pieces
        char[][] board = currentState.board;
        for (int x = 0; x < BOARD_SIZE; x++) {
            for (int y = 0; y < BOARD_SIZE; y++) {
                char piece = board[x][y];
                if (piece == '\u0000') continue;
                g.setColor(piece == 'b' ? Color.black : Color.white);
                g.fillOval(x * UNIT + UNIT - pieceWidth / 2, y * UNIT + UNIT - pieceWidth / 2, pieceWidth, pieceWidth);
            }
        }
    }


}



