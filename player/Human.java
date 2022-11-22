package player;

import core.Action;
import game.Go;
import game.Input;

public class Human extends Player {
    Input input;

    public Human(Input input) {
        this.input = input;
    }

    @Override
    public void update(Go go) {
        if (input.selectedX != -1 || input.selectedY != -1) {

            action = new Action(input.selectedX, input.selectedY);
            input.selectedX = -1;
            input.selectedY = -1;
            return;
        }
        if (input.pass) {
            action = new Action(true);
            input.pass = false;
        }
    }

}
