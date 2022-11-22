package player;

import game.Go;
import util.Util;
import core.Action;

import java.util.LinkedList;

public abstract class Player {
    public Action action;
    public LinkedList<Action> possibleMoves = new LinkedList<>();

    public abstract void update(Go go);

    public void generateAllPossibleMoves(Go game) {
        possibleMoves = Util.getAllPossibleMoves(game.currentState);
    }
}
