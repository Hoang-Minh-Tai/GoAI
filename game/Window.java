package game;

import game.Go;
import game.Input;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {
    public static int width = 600, height = 600;
    public static int UPS = 60;

    Go game = new Go();
    Input input = game.input;

    public Window(){
        init();
        loop();
    }

    private void init() {
        setVisible(true);
        setResizable(false);
        setTitle("GoPro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game.setPreferredSize(new Dimension(width,height));
        addKeyListener(input);
        addMouseListener(input);
        add(game);
        pack();
        setLocationRelativeTo(null);

    }

    private void loop() {
        long currentUpdate, lastUpdate = System.currentTimeMillis();
        double dt = 0;
        while (true) {
            currentUpdate = System.currentTimeMillis();
            dt += (currentUpdate - lastUpdate) / 1000d;
            lastUpdate = currentUpdate;

            if (dt >= 1.0d/UPS) {
                game.update();
                game.render();
                dt -= 1.0/UPS;
            }
        }
    }
}
