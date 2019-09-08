package snake;

import com.sun.scenario.effect.impl.sw.java.JSWBlend_COLOR_BURNPeer;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

public class StatusControl extends JPanel {

    private int x0 = 112;
    private int y0 = 250;
    private int dim = 25;

    private int[][] senseMap;

    private JLabel gameStatusLabel;
    private JLabel senseMapJLabel;
    private JLabel lengthStatus;
    private JLabel averageLength;

    private JButton startButton;

    private MapEngine mapEngine;

    private SnakeMedium medium;

    private JSlider speed;

    private ImageIcon rightmouth;
    private ImageIcon leftmouth;
    private ImageIcon upmouth;
    private ImageIcon downmouth;
    private ImageIcon snakeimage;
    private ImageIcon foodimage;

    public StatusControl() {

        setLayout( new BoxLayout(this, BoxLayout.Y_AXIS) );

        gameStatusLabel = new JLabel( "<html><div text-align:center>Snake SLEEPING.<br/>Press Space to wake it up!<div/></html>",  JLabel.CENTER);
        gameStatusLabel.setMaximumSize( new Dimension(  Integer.MAX_VALUE, 220 ) );
        add( gameStatusLabel );

        senseMapJLabel = new JLabel( "Snake's sensing field:", JLabel.CENTER );
        senseMapJLabel.setVerticalAlignment( JLabel.TOP );
        senseMapJLabel.setMaximumSize( new Dimension( Integer.MAX_VALUE, 200 ) );
        add( senseMapJLabel );

        lengthStatus = new JLabel( "", JLabel.CENTER );
        lengthStatus.setVerticalAlignment( JLabel.TOP );
        lengthStatus.setMaximumSize( new Dimension( Integer.MAX_VALUE, 20 ) );
        add( lengthStatus );

        averageLength = new JLabel( "Average length: not applicable yet", JLabel.CENTER );
        averageLength.setVerticalAlignment( JLabel.TOP );
        averageLength.setMaximumSize( new Dimension( Integer.MAX_VALUE, 100 ) );
        add( averageLength );

        JLabel speedLabel = new JLabel( "Speed adjustment (ms delay)", JLabel.CENTER );
        speedLabel.setVerticalAlignment( JLabel.CENTER );
        speedLabel.setMaximumSize( new Dimension( Integer.MAX_VALUE, 50 ) );
        add( speedLabel );

        speed = new JSlider( );
        speed.setAlignmentX( JSlider.CENTER );
        speed.setBackground( Color.lightGray );
        speed.setMaximum( 500 );
        speed.setMinimum( 1 );
        speed.setMajorTickSpacing( 50 );
        speed.setMinorTickSpacing( 10 );
        speed.setPaintTicks( true );
        Hashtable speedLabels = new Hashtable(  );
        speedLabels.put(1, new JLabel( "<html><div text-align:center>1<br/>(fastest)") );
        speedLabels.put(100, new JLabel( "100") );
        speedLabels.put(200, new JLabel( "200") );
        speedLabels.put(300, new JLabel( "300") );
        speedLabels.put(400, new JLabel( "400") );
        speedLabels.put(500, new JLabel( "<html><div text-align:center>500<br/>(slowest)") );
        speed.setPaintLabels( true );
        speed.setLabelTable( speedLabels );
        speed.setValue( 25 );
        add( speed );

        JLabel infoSpeed = new JLabel( "You can also use brackets [ ] to change speed.", JLabel.CENTER );
        infoSpeed.setVerticalAlignment( JLabel.CENTER );
        infoSpeed.setMaximumSize( new Dimension( Integer.MAX_VALUE, 100 ) );
        add( infoSpeed );

        startButton = new JButton( "START" );
        startButton.setMaximumSize( new Dimension(Integer.MAX_VALUE, 50 ) );
        startButton.setVisible( true );
        add( startButton );

        rightmouth = new ImageIcon( "rightmouth.png" );
        downmouth = new ImageIcon( "downmouth.png" );
        leftmouth = new ImageIcon( "leftmouth.png" );
        upmouth = new ImageIcon( "upmouth.png" );
        snakeimage = new ImageIcon( "snakeimage.png" );
        foodimage = new ImageIcon( "food.png" );
    }

    public void setMapEngine(MapEngine mapEngine) {
        this.mapEngine = mapEngine;
    }

    public void setGameStatusLabelText(String status) {
        gameStatusLabel.setText( status );
    }

    public void setLengthStatusLabelText(int length) {
        lengthStatus.setText( "Current length: " + length);
    }

    public void setAverageLengthLabelText(int average) {
        averageLength.setText( "Average length: " + average);
    }

    public void setButtonText(String status){
        startButton.setText( status );
    }

    public void setMedium (SnakeMedium snakeMedium){
        medium = snakeMedium;

        speed.addChangeListener( new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                medium.getTimer().setDelay( speed.getValue() );
                medium.requestFocus();
            }
        } );

        startButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                medium.attemptStartStop();
            }
        } );
    }

    public JSlider getSpeed() {
        return speed;
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor( Color.lightGray );
        g.fillRect( 0, 0, this.getWidth(), this.getHeight() );

        g.setColor( Color.black );
        g.drawRect( x0, y0, 3 * dim, 3 * dim );

        paintSenseMap( g );
    }

    private void paintSenseMap(Graphics g) {

        senseMap = mapEngine.getSenseMap();

        int i, j;
        float colorIndex;

        for (i = 1; i < 4; i++) {
            for (j = 1; j < 4; j++) {
                switch (senseMap[i][j]) {
                    case -100:
                        switch (mapEngine.getSnake().getDirection()) {
                            case 1:
                                rightmouth.paintIcon( this, g, x0 + (j - 1) * dim, y0 + (i - 1) * dim );
                                break;
                            case 2:
                                downmouth.paintIcon( this, g, x0 + (j - 1) * dim, y0 + (i - 1) * dim );
                                break;
                            case 3:
                                leftmouth.paintIcon( this, g, x0 + (j - 1) * dim, y0 + (i - 1) * dim );
                                break;
                            case 4:
                                upmouth.paintIcon( this, g, x0 + (j - 1) * dim, y0 + (i - 1) * dim );
                                break;
                            default:
                                break;
                        }
                        break;
                    case -99:
                        snakeimage.paintIcon( this, g, x0 + (j - 1) * dim, y0 + (i - 1) * dim );
                        break;
                    case 1000:
                        foodimage.paintIcon( this, g, x0 + (j - 1) * dim, y0 + (i - 1) * dim );
                        break;
                    default:
                        colorIndex = (float) (senseMap[i][j] - mapEngine.getMin()) / (float) mapEngine.getSpan();
                        g.setColor( new Color( colorIndex, colorIndex, colorIndex ) );
                        g.fillRect( x0 + (j - 1) * dim, y0 + (i - 1) * dim, dim, dim );
                        break;
                }
            }
        }
    }

}
