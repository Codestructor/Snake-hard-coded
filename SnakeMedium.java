package snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class SnakeMedium extends JPanel implements KeyListener, ActionListener {

    private int dim;
    private int x0, y0;
    private boolean started;

    private ImageIcon rightmouth;
    private ImageIcon leftmouth;
    private ImageIcon upmouth;
    private ImageIcon downmouth;
    private ImageIcon snakeimage;
    private ImageIcon foodimage;

    private int delayIncrease;
    private Timer timer;

    private Snake snakey;
    private Food food;

    MapEngine mapEngine;
    private int[][] environmentMap;

    private StatusControl statusControl;

    public SnakeMedium (StatusControl statusControl){
        dim = 25;
        x0 = 0;
        y0 = 0;

        started = false;

        rightmouth = new ImageIcon("rightmouth.png");
        downmouth = new ImageIcon("downmouth.png");
        leftmouth = new ImageIcon("leftmouth.png");
        upmouth = new ImageIcon("upmouth.png");
        snakeimage = new ImageIcon( "snakeimage.png" );
        foodimage = new ImageIcon( "food.png" );

        snakey = new Snake( 3, 1 );

        food = new Food();

        mapEngine = new MapEngine( snakey, food );

        this.statusControl = statusControl;
        statusControl.setMapEngine( mapEngine );
        statusControl.setMedium( this );

        delayIncrease = 0;
        timer = new Timer ( statusControl.getSpeed().getValue(), this);

        addKeyListener (this);
        setFocusable( true );
        setFocusTraversalKeysEnabled( false );
    }

    public Timer getTimer() {
        return timer;
    }

    @Override
    protected void paintComponent (Graphics g) {
        requestFocus( true ); // GamePlay loses focus

        g.setColor( Color.darkGray );
        g.fillRect( 0, 0, this.getWidth(), this.getHeight() );

        g.setColor( Color.WHITE );
        g.drawRect( 24, 24, 876, 876 );

        paintBoard( g );

        paintSnake( snakey, g );

        paintFood( food, g );

        g.dispose();
    }

    private void paintBoard (Graphics g){
        environmentMap = mapEngine.getEnvironmentMap();

        float colorIndex = -1;

        for (int i = 1; i < mapEngine.getMapSize(); i++)
            for (int j = 1; j < mapEngine.getMapSize(); j++) {
                    colorIndex = (float) (environmentMap[i][j] - mapEngine.getMin()) / (float) mapEngine.getSpan();
                    colorIndex = (colorIndex < 0) ? 0 : colorIndex;
                    g.setColor( new Color( colorIndex, colorIndex, colorIndex ) );
                    g.fillRect( x0 + j * dim, y0 + i * dim, dim, dim );
            }
    }

    private void paintSnake(Snake snakey, Graphics g){

        switch (snakey.getDirection()){
            case 1: rightmouth.paintIcon( this, g, x0 + snakey.getXPosition( 0 ) * dim,  y0 + snakey.getYPosition( 0 ) * dim ); break;
            case 2: downmouth.paintIcon( this, g, x0 + snakey.getXPosition( 0 ) * dim, y0 + snakey.getYPosition( 0 ) * dim ); break;
            case 3: leftmouth.paintIcon( this, g, x0 + snakey.getXPosition( 0 ) * dim, y0 + snakey.getYPosition( 0 ) * dim ); break;
            case 4: upmouth.paintIcon( this, g, x0 + snakey.getXPosition( 0 ) * dim, y0 + snakey.getYPosition( 0 ) * dim ); break;
            default: break;
        }

        for (int i = 1; i < snakey.getLength(); i++){
            if(snakey.getXPosition( i ) > 0 && snakey.getXPosition( i ) <36 && snakey.getYPosition( i ) > 0 && snakey.getYPosition( i ) < 36)
                snakeimage.paintIcon( this, g, x0 + snakey.getXPosition( i ) * dim, y0 + snakey.getYPosition( i ) * dim );
        }

        statusControl.setLengthStatusLabelText( snakey.getLength() );
    }

    private void paintFood(Food food, Graphics g){
        foodimage.paintIcon( this, g, x0 + food.getFoodX() * dim, y0 + food.getFoodY() * dim );
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        snakey.move();
        if(snakey.bitesItself()){
            statusControl.setAverageLengthLabelText( snakey.getAverage() );
            repaint();
            statusControl.repaint();
            return;
        }
        if(snakey.found(food)){
            food.spawnFood();
            timer.setDelay( timer.getDelay() + delayIncrease );
        }
        repaint();
        statusControl.repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {

        int newDelay;

        switch (e.getKeyCode()) {
            case KeyEvent.VK_SPACE:
                attemptStartStop();
                break;
            case KeyEvent.VK_OPEN_BRACKET:
                newDelay = timer.getDelay() - 10;
                if (newDelay >= 1) {
                    timer.setDelay( newDelay );
                    statusControl.getSpeed().setValue( newDelay );
                }
                break;
            case KeyEvent.VK_CLOSE_BRACKET:
                newDelay = timer.getDelay() + 10;
                if (newDelay <= 500) {
                    timer.setDelay( newDelay );
                    statusControl.getSpeed().setValue( newDelay );
                }
                break;
            default:
                break;
        }
    }

    public void attemptStartStop (){
        if (!started) {
            started = true;
            statusControl.setGameStatusLabelText( "<html><div text-align:center>Snake ACTIVE.<br/>Press Space to set it to sleep!<div/></html>" );
            statusControl.setButtonText( "STOP" );
            timer.start();
        } else {
            started = false;
            statusControl.setGameStatusLabelText( "<html><div text-align:center>Snake SLEEPING.<br/>Press Space to wake it up!<div/></html>" );
            statusControl.setButtonText( "START" );
            timer.stop();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

}
