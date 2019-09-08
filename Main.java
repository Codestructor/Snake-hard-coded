package snake;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class Main {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Snake");

        JPanel mainPanel = new JPanel(  );
        mainPanel.setLayout( new BorderLayout(  ) );
        mainPanel.setBackground(Color.white);
        frame.add( mainPanel );

        StatusControl statusControl = new StatusControl();
        statusControl.setPreferredSize( new Dimension( 300, 900 ) );
        mainPanel.add(statusControl , BorderLayout.EAST );

        SnakeMedium snakeMedium = new SnakeMedium(statusControl);
        snakeMedium.setPreferredSize( new Dimension( 915, 915 ) );
        mainPanel.add(snakeMedium, BorderLayout.CENTER);

        mainPanel.validate();
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
}
