package snake;

import java.util.ArrayList;

public class Snake {

    private ArrayList<Integer> xpos;
    private ArrayList<Integer> ypos;


    private double average;
    private int pastGenerations;

    private int direction;

    private int initialLength, initialxstart, initialystart;

    private int[][] senseMap;

    private int length;

    MapEngine mapEngine;

    public Snake(int xstart, int ystart) {
        this( xstart, ystart, 3 );
    }

    public Snake(int xstart, int ystart, int length) {

        xpos = new ArrayList<>();
        xpos.add( xstart );
        initialxstart = xstart;
        ypos = new ArrayList<>();
        ypos.add( ystart );
        initialystart = ystart;

        initialLength = length;
        this.length = length;
        direction = 1;

        for (int i = 1; i < length; i++) {
            xpos.add( xstart - i );
            ypos.add( ystart );
        }

        pastGenerations = 0;
        average = 0;
    }

    public int getLength() {
        return length;
    }

    public int getXPosition(int i) {
        return xpos.get( i );
    }

    public int getYPosition(int i) {
        return ypos.get( i );
    }

    public int getDirection() {
        return direction;
    }

    public int getAverage() { return (int) Math.round( average ); }

    public MapEngine getMapEngine() {
        return mapEngine;
    }

    public void setMapEngine(MapEngine mapEngine) {
        this.mapEngine = mapEngine;
    }

    public void move() {

        direction = decideDirection( direction );

        for (int i = length - 1; i > 0; i--) {
            xpos.set( i, xpos.get( i - 1 ) );
            ypos.set( i, ypos.get( i - 1 ) );
        }

        switch (direction) {
            case 1:
                xpos.set( 0, xpos.get( 0 ) + 1 );
                break;
            case 2:
                ypos.set( 0, ypos.get( 0 ) + 1 );
                break;
            case 3:
                xpos.set( 0, xpos.get( 0 ) - 1 );
                break;
            case 4:
                ypos.set( 0, ypos.get( 0 ) - 1 );
                break;
            default:
                break;
        }

        switch (xpos.get( 0 )) {
            case 36:
                xpos.set( 0, 1 );
                break;
            case 0:
                xpos.set( 0, 35 );
                break;
            default:
                break;
        }

        switch (ypos.get( 0 )) {
            case 36:
                ypos.set( 0, 1 );
                break;
            case 0:
                ypos.set( 0, 35 );
                break;
            default:
                break;
        }
    }

    public boolean found(Food food) {
        if (xpos.get( 0 ) == food.getFoodX() && ypos.get( 0 ) == food.getFoodY()) {
            xpos.add( 2 * xpos.get( length - 1 ) - xpos.get( length - 2 ) );
            ypos.add( 2 * ypos.get( length - 1 ) - ypos.get( length - 2 ) );
            length++;
            return true;
        } else
            return false;
    }

    public boolean bitesItself() {
        for (int i = 4; i < length; i++)
            if (xpos.get( 0 ) == xpos.get( i ) && ypos.get( 0 ) == ypos.get( i )) {
                restartSnake();
                return true;
            }
        return false;
    }

    private void restartSnake() {

        System.out.println( length );

        average = (average*pastGenerations + length)/(pastGenerations + 1);
        pastGenerations++;

        direction = 1;

        xpos = new ArrayList<>();
        xpos.add( initialxstart );
        ypos = new ArrayList<>();
        ypos.add( initialystart );

        length = initialLength;

        for (int i = 1; i < length; i++) {
            xpos.add( initialxstart - i );
            ypos.add( initialystart );
        }

        mapEngine.getSenseMap();
    }

    public void cleanSnake() {
        for (int i = 4; i < length; i++)
            if (xpos.get( i ) > 36 || xpos.get( i ) < 0 || ypos.get( i ) > 36 || ypos.get( i ) < 0) {
                System.out.print( "Body unit deleted: (" + xpos.get( i ) + ", " + ypos.get( i ) + ") at position: " + i + " out of the length: " + length );
                System.out.println();
                xpos.remove( i );
                ypos.remove( i );
                length--;
            }
    }

    private int decideDirection(int direction) {

        int i;

        int sumL, sumR, sumF;
        sumF = 0;
        sumL = 0;
        sumR = 0;
        
        senseMap = mapEngine.getSenseMap();

        switch (direction) {
            case 1:
                for (i = 1; i < 4; i++) {
                    sumL += senseMap[1][i];
                    sumR += senseMap[3][i];
                    sumF += senseMap[i][3];
                }
                break;
            case 2:
                for (i = 1; i < 4; i++) {
                    sumL += senseMap[i][3];
                    sumR += senseMap[i][1];
                    sumF += senseMap[3][i];
                }
                break;
            case 3:
                for (i = 1; i < 4; i++) {
                    sumL += senseMap[3][i];
                    sumR += senseMap[1][i];
                    sumF += senseMap[i][1];
                }
                break;
            case 4:
                for (i = 1; i < 4; i++) {
                    sumL += senseMap[i][1];
                    sumR += senseMap[i][3];
                    sumF += senseMap[1][i];
                }
                break;
            default:
                break;
        }

        if (sumL > sumF && sumL > sumR) {
            direction--;
            if (direction < 1)
                direction = 4;
        }
        if (sumR > sumF && sumR > sumL) {
            direction++;
            if (direction > 4)
                direction = 1;
        }

        return direction;
    }
}
