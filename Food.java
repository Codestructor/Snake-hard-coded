package snake;

import java.util.Random;

public class Food {

    private int x;
    private int y;
    private Random rand;
    private MapEngine mapEngine;

    public Food(){
        rand = new Random( );
    }

    public int getFoodX() { return x; }

    public int getFoodY() { return y; }

    public void setMapEngine(MapEngine mapEngine) {
        this.mapEngine = mapEngine;
        spawnFood();
    }

    public void spawnFood(){
        do {
            x = rand.nextInt( 35 ) + 1;
            y = rand.nextInt( 35 ) + 1;
        }while(checkOverlap());

        mapEngine.generateFoodMap(x, y);
        mapEngine.getSenseMap();
    }

    private boolean checkOverlap(){
        for(int i = 0; i< mapEngine.getSnake().getLength(); i++)
            if(x == mapEngine.getSnake().getXPosition( i ) && y == mapEngine.getSnake().getYPosition( i ))
                return true;
        return false;
    }
}
