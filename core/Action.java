package core;

import java.util.Arrays;

/**
 * @Overviews core.Action represents a valid move player can make in current board state
 * @Object An core.Action is an core.Action(x,y) -> x int, y int
 * @Attribute
 *      pos int[2]
 */
public class Action {
   public int[] pos;
   public boolean pass;


    public Action(int x, int y) {
        pos = new int[2];
        pos[0] = x;
        pos[1] = y;
        this.pass = false;
    }

    public Action(boolean pass) {
        this.pass = pass;
        pos = new int[2];
        pos[0] = 200;
        pos[1] = 200;
    }

    @Override
    public String toString() {
        if (pass) return "Pass";
        return Arrays.toString(pos);
    }

    public int getX() {
        return pos[0];
    }
    public int getY() {
        return pos[1];
    }
}
