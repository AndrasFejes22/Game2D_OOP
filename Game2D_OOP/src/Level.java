import java.util.Random;

public class Level {
	
	private final int height;
	
	private final int width;
	
	private String[][] level;
	
	private final Random RANDOM;
	
	

	/**
	 * @param height
	 * @param width
	 */
	public Level(Random random, int height, int width) {
		super();
		this.RANDOM = random;
		this.height = height;
		this.width = width;
		this.level = new String[height][width];
		int lastRowIndex = height - 1;
		int lastColumnIndex = width - 1;
		for (int row = 0; row < height; row++) {//initLevel();
            for (int column = 0; column < width; column++) {
                if(row == 0 ||  row == lastRowIndex || column == 0 || column == lastColumnIndex) {//walls
                    level[row][column] = "X"; //walls
                }else{
                    level[row][column] = " ";
                }
            }
        }
		
	}
	
	public void addRandomWalls(int numberOfHorizontalWalls, int numberOfWerticalWalls ) {
        //TODO fal ne kerüljön a játékosokra
        for(int i = 0; i < numberOfHorizontalWalls; i++) {
            addHorizontalWall();
        }
        for(int i = 0; i < numberOfWerticalWalls; i++) {
            addVerticalWall();
        }

    }


    private void addHorizontalWall() {

        int wallWidth = RANDOM.nextInt(width-3);
        int wallRow = RANDOM.nextInt(height-2)+1;
        int wallColumn = RANDOM.nextInt(width-2-wallWidth);
        for(int i = 0; i < wallWidth; i++ ) {
            level[wallRow][wallColumn + i] = "X";
        }
    }

    private void addVerticalWall() {

        int wallHeight = RANDOM.nextInt(height-3);
        int wallRow = RANDOM.nextInt(height-2 - wallHeight);
        int wallColumn = RANDOM.nextInt(width-2)+1;
        for(int i = 0; i < wallHeight; i++ ) {
            level[wallRow + i][wallColumn] = "X";
        }

    }
    
    //rajzolja-e a csillagok terjedését
    public boolean isPassable() {
        return isPassable(false);

    }

    //Csak olyan pályát rajzoljon amiben nincsenek zárt terek:


     public boolean isPassable(boolean drawAsterisks) {
        // pálya lemásolása
        String[][] levelCopy = copy(level);// 2D array lemásolása
        // elsõ szóköz megkeresése és *-al történõ helyettesítése
        outer: for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {
                if (levelCopy[row][column].equals(" ")) {
                    levelCopy[row][column] = "*";

                    break outer;
                    // break;
                }
            }
        }

        // stars spreading
        // nem tud alul/felülindexelõdni mert szélsõséges esetben 1,1-rõl vagy
        // max-1,max-1 rõl indul

        while(spreadAsterisks(levelCopy)) {
            if(drawAsterisks) {
                draw2DArray(levelCopy);
            }
        }
        //draw2DArray(levelCopy);
        //pályamásolat vizsgálata: maradt-e szóköz valahol

        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {
                if (levelCopy[row][column].equals(" ")) {
                    return false;
                }
            }
        }

        //System.exit(0);
        //return false;
        return true;

    }
    
    private boolean spreadAsterisks(String[][] levelCopy) {
        boolean changed = false;

        for (int row = 0; row < height; row++) {

            for (int column = 0; column < width; column++) {

                // a körülötte lévõ helyekre *-ot rak (amíg tud)
                if ("*".equals(levelCopy[row][column])) {// * van valahol
                    if (" ".equals(levelCopy[row - 1][column])) {
                        levelCopy[row - 1][column] = "*";
                        changed = true;
                    }
                    if (" ".equals(levelCopy[row + 1][column])) {
                        levelCopy[row + 1][column] = "*";
                        changed = true;
                    }
                    if (" ".equals(levelCopy[row][column - 1])) {
                        levelCopy[row][column - 1] = "*";
                        changed = true;
                    }
                    if (" ".equals(levelCopy[row][column + 1])) {// levelCopy[1][4]:" "
                        levelCopy[row][column + 1] = "*";
                        changed = true;
                    }

                }
            }
        }
        return changed;
    }


    //2D array lemásolása
    private String[][] copy(String[][] level){
        String[][] copy = new String[height][width];
        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {
                copy[row][column] = level[row][column];
            }
        }
        return copy;

    }
    
    public static void draw2DArray(String[][] arr){

        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                System.out.print(arr[i][j]);
            }
            System.out.println();
        }

    }

	public boolean isEmpty(Coordinates coordinates) {
		// TODO Auto-generated method stub
		return " ".equals(level[coordinates.getRow()][coordinates.getColumn()]);
	}
	
	public Coordinates getFarthestCorner(Coordinates from) {
        // pálya lemásolása
        String[][] levelCopy = copy(level);// 2D array(pálya) lemásolása
        // elsõ csillag lehelyezése
        levelCopy[from.getRow()][from.getColumn()] = "*";

        int farthestRow = 0;
        int farthestColumn = 0;

        while(spreadAsterisksWithCheck(levelCopy)) {
            outer: for (int row = 0; row < height; row++) {
                for (int column = 0; column < width; column++) {
                    if (levelCopy[row][column].equals(" ")) {
                        farthestRow = row;
                        farthestColumn = column;
                        break outer;
                    }
                }
            }
        }
        Coordinates farthestCorner = new Coordinates(farthestRow, farthestColumn);//késõbb refactor/inline...
        //farthestCorner.setRow(farthestRow);
        //farthestCorner.setColumn(farthestColumn);
        return farthestCorner;
    }
	
	private boolean spreadAsterisksWithCheck(String[][] levelCopy) {
        boolean[][] mask = new boolean [height][width]; //alapértelmezetten csupa false-val van tele
        //végigmegyek a levelCopy-n, és ha találok valahol csillagot, ott a mask-ot true-ra állítom:
        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {
                if ("*".equals(levelCopy[row][column])) {
                    mask[row][column] = true;

                }
            }
        }
        boolean changed = false;
        for (int row = 0; row < height; row++) {

            for (int column = 0; column < width; column++) {

                // a körülötte lévõ helyekre *-ot rak (amíg tud)
                if ("*".equals(levelCopy[row][column]) && mask[row][column]) {// * van valahol ÉS a mask az true
                    if (" ".equals(levelCopy[row - 1][column])) {
                        levelCopy[row - 1][column] = "*";
                        changed = true;//ez így mindig csak 1-et lép
                    }
                    if (" ".equals(levelCopy[row + 1][column])) {
                        levelCopy[row + 1][column] = "*";
                        changed = true;
                    }
                    if (" ".equals(levelCopy[row][column - 1])) {
                        levelCopy[row][column - 1] = "*";
                        changed = true;
                    }
                    if (" ".equals(levelCopy[row][column + 1])) {// levelCopy[1][4]:" "
                        levelCopy[row][column + 1] = "*";
                        changed = true;
                    }

                }
            }
        }
        return changed;
    }

	public Direction getShortestPath(Direction defaultDirection, Coordinates from, Coordinates to) {
        // pálya lemásolása:
        String[][] levelCopy = copy(level);// 2D array lemásolása
        // elsõ csillag lehelyezése
        levelCopy[to.getRow()][to.getColumn()] = "*";
        while(spreadAsterisksWithCheck(levelCopy)) {
            if("*".equals(levelCopy[from.getRow() -1][from.getColumn()])) {//ha a visszafelé terjedõ csillagok közül az elsõ felülrõl
                //jelent meg akkor felfele megyünk*
                return Direction.UP;
            }
            if("*".equals(levelCopy[from.getRow() +1][from.getColumn()])) {//ha a visszafelé terjedõ csillagok közül az elsõ alulról
                //jelent meg akkor lefele megyünk*
                return Direction.DOWN;
            }
            if("*".equals(levelCopy[from.getRow()][from.getColumn() -1])) {//ha a visszafelé terjedõ csillagok közül az elsõ balról
                //jelent meg akkor balra megyünk*
                return Direction.LEFT;
            }
            if("*".equals(levelCopy[from.getRow()][from.getColumn()+1])) {//ha a visszafelé terjedõ csillagok közül az elsõ jobbra
                //jelent meg akkor jobbra megyünk*
                return Direction.RIGHT;
            }
        }
        return defaultDirection;

    }

	public String getCell(Coordinates coordinates) {
		
		return level[coordinates.getRow()][coordinates.getColumn()];
	}
	
	public Coordinates getRandomCoordinates() {
		Coordinates randomCoordinates;

		do {
			randomCoordinates = new Coordinates(RANDOM.nextInt(height), RANDOM.nextInt(width));

		} while (!isEmpty(randomCoordinates));

		return randomCoordinates;
	}
	
	public Coordinates getRandomStartingCoordinatesForADistance(Coordinates coordinates, int distance) {
        Coordinates randomCoordinates;
        int counter = 0; //infinite loop kivédése //////////////EZ MÉG NEM JÓÓÓ
        do {
            randomCoordinates = getRandomCoordinates();
            //**nem lehet a két koordináta közelebb egymáshoz mint distance pályaegység
        }while(counter++ < 1000 && randomCoordinates.distanceFrom(coordinates) < distance);
                
        
        //helyi Coordinates object volt látrehozva konstruktorával-->átment inline-ba: 3 sorral kevesebb
        //és a memóriát sem foglaljuk
        //startingCoordinates.setRow(randomRow);
        //startingCoordinates.setColumn(randomColumn);

        return randomCoordinates;
    }
	
	
	
}
