package snake;

public class MapEngine {

    private int[][] environmentMap, foodMap, bodyMap;
    private int[][] senseMap;
    private int mapSize = 36;

    private int min, span;

    private Snake snakey;
    private Food food;

    public MapEngine(Snake snakey, Food food) {

        foodMap = new int[50][50];
        senseMap = new int[5][5];

        this.snakey = snakey;
        this.food = food;

        snakey.setMapEngine( this );
        food.setMapEngine( this );

        generateSenseMap();

    }

    public int getMapSize() {
        return mapSize;
    }

    public int getMin() {
        return min;
    }

    public int getSpan() {
        return span;
    }

    public int[][] getSenseMap() {
        generateSenseMap();
        return senseMap;
    }

    public int[][] getEnvironmentMap() {
        return environmentMap;
    }

    public Snake getSnake() {
        return snakey;
    }

    private void generateMap() {

        environmentMap = new int[50][50];
        sumMaps( environmentMap, foodMap );

        for (int i = 4; i < snakey.getLength(); i++) {
            bodyMap = new int[50][50];
            generateBodyMap( snakey.getXPosition( i ), snakey.getYPosition( i ) );
            sumMaps( environmentMap, bodyMap );
        }

        computeMinSpan();

    }

    public void generateFoodMap(int x, int y) {

        int value = 300, i, j;

        for (i = x; i >= 0; i--)
            foodMap[y][i] = value - (x - i);
        for (i = x + 1; i <= mapSize; i++)
            foodMap[y][i] = value - (i - x);

        for (i = 0; i <= mapSize; i++) {
            for (j = y - 1; j >= 0; j--)
                foodMap[j][i] = foodMap[y][i] - (y - j);
            for (j = y + 1; j <= mapSize; j++)
                foodMap[j][i] = foodMap[y][i] - (j - y);
        }

        generateSenseMap();
    }

    private void generateBodyMap(int x, int y) {

        int max = -5, value, i, j;

        snakey.cleanSnake();

        for (i = x, value = max; i >= 0 && value < 0; i--) {
            bodyMap[y][i] = value++;
        }

        for (i = x + 1, value = max + 1; i <= mapSize && value < 0; i++)
            bodyMap[y][i] = value++;

        i = x + max + 1;
        if (i < 0)
            i = 0;
        for (; i <= mapSize && i <= x - max - 1; i++) {
            for (j = y - 1; j >= 0 && bodyMap[j + 1][i] < 0; j--)
                bodyMap[j][i] = bodyMap[j + 1][i] + 1;

            for (j = y + 1; j <= mapSize && bodyMap[j - 1][i] < 0; j++)
                bodyMap[j][i] = bodyMap[j - 1][i] + 1;
        }
    }

    private void sumMaps(int[][] a, int[][] b) { // result stored in a[][]
        int i, j;

        for (i = 0; i <= mapSize; i++)
            for (j = 0; j <= mapSize; j++)
                a[i][j] += b[i][j];
    }

    private void generateSenseMap() { // creates senseMap

        generateMap();

        int i, j;

        for (i = 1; i < 4; i++)
            for (j = 1; j < 4; j++)
                senseMap[i][j] = environmentMap[snakey.getYPosition( 0 ) - (2 - i)][snakey.getXPosition( 0 ) - (2 - j)];
        senseMap[2][2] = -100;

        switch (snakey.getDirection()) {
            case 1:
                senseMap[2][1] = -99;
                break;
            case 2:
                senseMap[1][2] = -99;
                break;
            case 3:
                senseMap[2][3] = -99;
                break;
            case 4:
                senseMap[3][2] = -99;
                break;
            default:
                break;
        }

        int xDif = food.getFoodX() - snakey.getXPosition( 0 );
        int yDif = food.getFoodY() - snakey.getYPosition( 0 );

        if (Math.abs( xDif ) < 2 && Math.abs( yDif ) < 2)
            senseMap[2 + yDif][2 + xDif] = 1000;

    }

    private void computeMinSpan (){

        int max = -1000;
        min = 1000;

        for (int i = 0; i <= mapSize; i++)
            for (int j = 0; j <= mapSize; j++) {
                if (environmentMap[i][j] > max)
                    max = environmentMap[i][j];
                if (environmentMap[i][j] < min)
                    min = environmentMap[i][j];
            }

        span = (int) (1.10 * (float) (max - min));

    }
}
