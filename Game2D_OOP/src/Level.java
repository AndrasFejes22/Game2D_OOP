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
        //TODO fal ne ker�lj�n a j�t�kosokra
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
    
    //rajzolja-e a csillagok terjed�s�t
    public boolean isPassable() {
        return isPassable(false);

    }

    //Csak olyan p�ly�t rajzoljon amiben nincsenek z�rt terek:


     public boolean isPassable(boolean drawAsterisks) {
        // p�lya lem�sol�sa
        String[][] levelCopy = copy(level);// 2D array lem�sol�sa
        // els� sz�k�z megkeres�se �s *-al t�rt�n� helyettes�t�se
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
        // nem tud alul/fel�lindexel�dni mert sz�ls�s�ges esetben 1,1-r�l vagy
        // max-1,max-1 r�l indul

        while(spreadAsterisks(levelCopy)) {
            if(drawAsterisks) {
                draw2DArray(levelCopy);
            }
        }
        //draw2DArray(levelCopy);
        //p�lyam�solat vizsg�lata: maradt-e sz�k�z valahol

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

                // a k�r�l�tte l�v� helyekre *-ot rak (am�g tud)
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


    //2D array lem�sol�sa
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
        // p�lya lem�sol�sa
        String[][] levelCopy = copy(level);// 2D array(p�lya) lem�sol�sa
        // els� csillag lehelyez�se
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
        Coordinates farthestCorner = new Coordinates(farthestRow, farthestColumn);//k�s�bb refactor/inline...
        //farthestCorner.setRow(farthestRow);
        //farthestCorner.setColumn(farthestColumn);
        return farthestCorner;
    }
	
	private boolean spreadAsterisksWithCheck(String[][] levelCopy) {
        boolean[][] mask = new boolean [height][width]; //alap�rtelmezetten csupa false-val van tele
        //v�gigmegyek a levelCopy-n, �s ha tal�lok valahol csillagot, ott a mask-ot true-ra �ll�tom:
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

                // a k�r�l�tte l�v� helyekre *-ot rak (am�g tud)
                if ("*".equals(levelCopy[row][column]) && mask[row][column]) {// * van valahol �S a mask az true
                    if (" ".equals(levelCopy[row - 1][column])) {
                        levelCopy[row - 1][column] = "*";
                        changed = true;//ez �gy mindig csak 1-et l�p
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
        // p�lya lem�sol�sa:
        String[][] levelCopy = copy(level);// 2D array lem�sol�sa
        // els� csillag lehelyez�se
        levelCopy[to.getRow()][to.getColumn()] = "*";
        while(spreadAsterisksWithCheck(levelCopy)) {
            if("*".equals(levelCopy[from.getRow() -1][from.getColumn()])) {//ha a visszafel� terjed� csillagok k�z�l az els� fel�lr�l
                //jelent meg akkor felfele megy�nk*
                return Direction.UP;
            }
            if("*".equals(levelCopy[from.getRow() +1][from.getColumn()])) {//ha a visszafel� terjed� csillagok k�z�l az els� alulr�l
                //jelent meg akkor lefele megy�nk*
                return Direction.DOWN;
            }
            if("*".equals(levelCopy[from.getRow()][from.getColumn() -1])) {//ha a visszafel� terjed� csillagok k�z�l az els� balr�l
                //jelent meg akkor balra megy�nk*
                return Direction.LEFT;
            }
            if("*".equals(levelCopy[from.getRow()][from.getColumn()+1])) {//ha a visszafel� terjed� csillagok k�z�l az els� jobbra
                //jelent meg akkor jobbra megy�nk*
                return Direction.RIGHT;
            }
        }
        return defaultDirection;

    }

	public String getCell(Coordinates coordinates) {
		
		return level[coordinates.getRow()][coordinates.getColumn()];
	}
	
	
	
}