package gameClient;

import javax.swing.*;
import java.awt.*;

public class MyFrame extends JFrame {

    public MyFrame(Arena ar){
        this.setDefaultCloseOperation(MyFrame.EXIT_ON_CLOSE);
        this.setSize(500,800);
        this.setLayout(new BorderLayout());

        MyPanel panel = new MyPanel();
        panel.update(ar);
        panel.setPreferredSize(new Dimension(400,700));
        this.add(panel);
        pack();
        this.setVisible(true);
    }
}
