package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Input implements MouseListener, KeyListener {
    Go game;
    public boolean pass = false;
    public int selectedX = -1;
    public int selectedY = -1;

    public Input(Go game) {
        this.game = game;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = (e.getX() - Go.UNIT / 2) / Go.UNIT;
        int y = (e.getY() - Go.UNIT / 2) / Go.UNIT;
        selectedX = x;
        selectedY = y;
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_P) {
            pass = true;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
