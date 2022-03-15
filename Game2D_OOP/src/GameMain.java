import java.util.Random;

public class GameMain {

	
		
		/*
		Features:
		It always generates a field in which there are no enclosed spaces.
		By default, the enemy (@) is chasing the player (O).
		A power-up (*)appears at a certain number of rounds, which the player tries to pick up.
		If the player picks up the power-up, it will chase the enemy, the enemy will escape.
		The effect of the power-up disappears over time, at which point the original order is restored.
		The character that escapes goes to the farthest point of the field.
		The characters move smartly, avoid obstacles, and go the shortest way.
		 * */

	    static int height = 40;
	    static int width = 40;
	    static int powerUpInLevel = height;
	    static final int  GAME_LOOP_NUMBER = 300;
	    static Random RANDOM = new Random(); //ha ez = new Random(100L); pseudo-random numbers/field does not change
	    //static Random RANDOM = new Random(160L);
	    //static Random RANDOM = new Random(27L); //100L (1,4) 20,20
	    public static void main(String[] args) throws InterruptedException {

	        //game area
	        String[][] level = new String[height][width];
	        //draw the level: --initlevel method //drawing random walls
	        int isPassableCounter = 0;
	        do {
	            initLevel(level);
	            //addRandomWalls(level, 10, 10);
	            addRandomWalls(level, 5, 6);
	            isPassableCounter++;
	        }while(!isPassable(level));

	        System.out.println("The No " + isPassableCounter + " board is passable");

	        ////////DRAW ASTERISKS OR NOT/////////
	        //overloaded isPassable() //drawing asterisks
	        //isPassable(level, true);
	        isPassable(level, false);

	        ////////VIZSGÁLAT ELEJE///////
	        //ez a 2 sor csak azt vizsgálja, hogy hányadik generálásra kapunk átjárható pályát, a fõ programban nem kell
	        //draw2DArray(level);
	        //System.exit(0);
	        ////////VIZSGÁLAT VÉGE///////


	        //Who will win?
	        GameResult gameResult = GameResult.TIE; //default

	        //////PLAYER//////

	        String playerMark = "O"; //represents the player
	        //random first coordinates for the player
	        Coordinates playerCoordinates = getRandomStartingCoordinates(level);
	        
	        Coordinates playerEscapeCoordinates = getFarthestCorner(level, playerCoordinates);
	        
	        Direction playerDirection = Direction.RIGHT; //First, the player moving to the right

	        //////ENEMY//////

	        String enemyMark = "@"; //represents the enemy
	        //random first coordinates for the enemy
	        Coordinates enemyCoordinates = getRandomStartingCoordinatesForADistance(level, playerCoordinates, 10); 
	        
	        Coordinates enemyEscapeCoordinates = getFarthestCorner(level, enemyCoordinates);
	        
	        Direction enemyDirection = Direction.LEFT; //First, the player moving to the left


	        //////POWER-UP//////

	        String powerUpMark = "*"; //represents the power-up, egy helyben fog állni
	        //random first coordinates for the power-up
	        Coordinates powerUpCoordinates = getRandomStartingCoordinates(level);
	        
	        boolean powerUpPresentOnLevel = false;
	        boolean powerUpActive = false;
	        int powerUpPresenceCounter = 0;
	        int powerUpActiveCounter = 0;


			for (int iterationNumber = 1; iterationNumber < GAME_LOOP_NUMBER; iterationNumber++) {// lépteti a karaktert
																									// -->makeMove
																									// method
				/////// UPDATED PLAYER MOVING////////
				// player irányváltoztatása
				if (powerUpActive) {// powerup aktív = a játékos kergeti az enemyt

					playerDirection = getShortestPath(level, playerDirection, playerCoordinates, enemyCoordinates);

				} else {
					if (powerUpPresentOnLevel) {

						// megy a powerUp felé
						playerDirection = getShortestPath(level, playerDirection, playerCoordinates,
								powerUpCoordinates);

					} else {
						// player menekül:
						if (iterationNumber % 50 == 0) {// választás 50 lépésenként
							// inkább meneküljön a legtávolabbi pontba:
							playerEscapeCoordinates = getFarthestCorner(level, playerCoordinates);

						}
						playerDirection = getShortestPath(level, playerDirection, playerCoordinates,
								playerEscapeCoordinates);// minden körben derékszögû irányváltoztatás
					}
				}
				// kiszedjük a koordinátákat és átadjuk a draw()-nak
				// ez a 3 sor a léptetése a playernek
				playerCoordinates = makeMove(playerDirection, level, playerCoordinates); // új értéket kap a
																							// playerCoordinates
																							// (paraméter az elõzõ)

				/////// UPDATED ENEMY MOVING////////

				// enemy irányváltoztatása: ez nincs benne if blokkban mert leköveti a player
				// mozgását, (utánamegy), tehát a player mozgása vezérli
				if (powerUpActive) {

					if (iterationNumber % 50 == 0) {// választás 50 lépésenként
						enemyEscapeCoordinates = getFarthestCorner(level, enemyCoordinates);

					}
					enemyDirection = getShortestPath(level, enemyDirection, enemyCoordinates, enemyEscapeCoordinates);
				} else {

					// felokosítva:
					// enemy üldöz:
					enemyDirection = getShortestPath(level, enemyDirection, enemyCoordinates, playerCoordinates);
				}
				// enemy léptetése:
				// kiszedjük a koordinátákat és átadjuk a draw()-nak
				if (iterationNumber % 2 == 0) {// minden 2. körben lép csak
					enemyCoordinates = makeMove(enemyDirection, level, enemyCoordinates);// frissül

				}

				// powerUp updateing:
				if (powerUpActive) {
					powerUpActiveCounter++;
				} else {
					powerUpPresenceCounter++;// minden iterációban növeljük eggyel a számlálót
				}
				// powerUpPresenceCounter mennyi ideig van a pályán
				if (powerUpPresenceCounter >= powerUpInLevel) {// ez*
					if (powerUpPresentOnLevel) {// jelen van, és fog kapni random koordinátákat
						powerUpCoordinates = getRandomStartingCoordinates(level);// frissül

					}
					powerUpPresentOnLevel = !powerUpPresentOnLevel; // vagy a pályán van, vagy nincs, és mindig
																	// kiváltjuk x (most 20) körönként ennek az
																	// ellenkezõjét
					powerUpPresenceCounter = 0; // *meg ez csinálja hogy mindig elõlrõl kezdõdhessen a számlálás és 20
												// körig van pUp, 20 körig nincs
					// és így tovább
				}
				if (powerUpActiveCounter >= powerUpInLevel) {
					powerUpActive = false;
					powerUpActiveCounter = 0;
					powerUpCoordinates = getRandomStartingCoordinates(level);

					playerEscapeCoordinates = getFarthestCorner(level, playerCoordinates);

				}

				// player-powerUp interaction handling:
				if (powerUpPresentOnLevel && playerCoordinates.isSame(powerUpCoordinates)) {
					powerUpActive = true;
					powerUpPresentOnLevel = false;
					powerUpPresenceCounter = 0;
					enemyEscapeCoordinates = getFarthestCorner(level, enemyCoordinates);

				}

				// drawing level and a playerMark; minden körben mindenki kirajzolása = "mozi"
				draw(level, playerMark, playerCoordinates, enemyMark, enemyCoordinates, powerUpMark, powerUpCoordinates,
						powerUpPresentOnLevel, powerUpActive);

				// várakoztatás
				addSomeDelay(iterationNumber, 200L);// print the iteration number and do the delay

				// ha az enemy elkapta a playert (= a koordinátáik megegyeznek), akkor game over
				if (playerCoordinates.isSame(enemyCoordinates)) {// több élet?
					if (powerUpActive) {
						gameResult = GameResult.WIN;
					} else {
						gameResult = GameResult.LOSE;
						System.out.println("The enemy caught the player!");
					}
					break;
				}

			}
	        switch (gameResult) {
	            case WIN:
	                System.out.println("Congratulation You WIN!");
	                break;
	            case LOSE:
	                System.out.println("Sorry You LOSE!");
	                break;
	            case TIE:
	                System.out.println("Draw Game.");
	                break;
	        }
	    }//main end





	    private static Coordinates getFarthestCorner(String[][] level, Coordinates from) {
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
	        Coordinates farthestCorner = new Coordinates();
	        farthestCorner.setRow(farthestRow);
	        farthestCorner.setColumn(farthestColumn);
	        return farthestCorner;
	    }





	    //rajzolja-e a csillagok terjedését
	    static boolean isPassable(String[][] level) {
	        return isPassable(level, false);

	    }

	    //Csak olyan pályát rajzoljon amiben nincsenek zárt terek:


	    static boolean isPassable(String[][] level, boolean drawAsterisks) {
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

	    //UPDATED PLAYER MOVING
	    //Az út megtalálása a terjedõ csillagok method visszafelé
	    //A *-ok ugyanis mindig a legrövidebb irányban terjednek
	    static Direction getShortestPath(String[][]level, Direction defaultDirection, Coordinates from, Coordinates to) {
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

	    private static boolean spreadAsterisksWithCheck(String[][] levelCopy) {
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

	    private static boolean spreadAsterisks(String[][] levelCopy) {
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
	    static String[][] copy(String[][] level){
	        String[][] copy = new String[height][width];
	        for (int row = 0; row < height; row++) {
	            for (int column = 0; column < width; column++) {
	                copy[row][column] = level[row][column];
	            }
	        }
	        return copy;

	    }

	    private static Direction getEscapeDirection(String[][] level, int enemyRow, int enemyColumn, Direction directionTowardsPlayer) {

	        Direction escapeDirection = getOppositeDirection(directionTowardsPlayer);
	        switch (escapeDirection){
	            case UP://menjen erre vagy jobbra, vagy balra de mindenképpen elfele, végsõ esetben a falnak is ütközhet
	                if (level[enemyRow - 1][enemyColumn].equals(" ")) {
	                    return Direction.UP;
	                }else if (level[enemyRow][enemyColumn-1].equals(" ")) {
	                    return Direction.LEFT;
	                }else if (level[enemyRow][enemyColumn +1].equals(" ")) {
	                    return Direction.RIGHT;
	                }else {
	                    return Direction.UP;
	                }

	            case DOWN://menjen erre vagy jobbra, vagy balra de mindenképpen elfele, végsõ esetben a falnak is ütközhet
	                if (level[enemyRow + 1][enemyColumn].equals(" ")) {
	                    return Direction.DOWN;
	                }else if (level[enemyRow][enemyColumn-1].equals(" ")) {
	                    return Direction.LEFT;
	                }else if (level[enemyRow][enemyColumn +1].equals(" ")) {
	                    return Direction.RIGHT;
	                }else {
	                    return Direction.DOWN;
	                }
	            case RIGHT://menjen erre vagy jobbra, vagy balra de mindenképpen elfele, végsõ esetben a falnak is ütközhet
	                if (level[enemyRow][enemyColumn + 1].equals(" ")) {
	                    return Direction.RIGHT;
	                }else if (level[enemyRow - 1][enemyColumn].equals(" ")) {
	                    return Direction.UP;
	                }else if (level[enemyRow + 1][enemyColumn ].equals(" ")) {
	                    return Direction.DOWN;
	                }else {
	                    return Direction.RIGHT;
	                }

	            case LEFT://menjen erre vagy jobbra, vagy balra de mindenképpen elfele, végsõ esetben a falnak is ütközhet
	                if (level[enemyRow][enemyColumn - 1].equals(" ")) {
	                    return Direction.LEFT;
	                }else if (level[enemyRow - 1][enemyColumn].equals(" ")) {
	                    return Direction.UP;
	                }else if (level[enemyRow + 1][enemyColumn ].equals(" ")) {
	                    return Direction.DOWN;
	                }else {
	                    return Direction.LEFT;
	                }
	            default:
	                return escapeDirection;


	        }
	    }

	    private static Direction getOppositeDirection(Direction direction) {

	        switch (direction){
	            case RIGHT :
	                return Direction.LEFT;

	            case DOWN  :
	                return Direction.UP;

	            case LEFT :
	                return Direction.RIGHT;

	            case UP :
	                return Direction.DOWN;

	            default:
	                return direction;
	        }

	    }

	    //random kezdõ koordináták sorsolása az eneminek, de**
	    private static Coordinates getRandomStartingCoordinatesForADistance(String[][] level, Coordinates playerStartingCoordinates, int distance) {
	        int playerStartingRow = playerStartingCoordinates.getRow();
	        int playerStartingColumn = playerStartingCoordinates.getColumn();
	        int randomRow;
	        int randomColumn;
	        int counter = 0; //infinite loop kivédése //////////////EZ MÉG NEM JÓÓÓ
	        do {
	            counter++;
	            randomRow = RANDOM.nextInt(height);//nemjo ezek nullát felvehetnek!!!
	            randomColumn = RANDOM.nextInt(width);
	            //**nem lehet a két koordináta közelebb egymáshoz mint distance pályaegység
	        }while(counter < 1000
	                && (!level[randomRow][randomColumn].equals(" ") || calculateDistance(randomRow, randomColumn, playerStartingRow, playerStartingColumn) < distance));
	        Coordinates startingCoordinates = new Coordinates();
	        startingCoordinates.setRow(randomRow);
	        startingCoordinates.setColumn(randomColumn);

	        return startingCoordinates;
	    }

	    private static int calculateDistance(int row1, int column1, int row2, int column2) {

	        int rowDifference = Math.abs(row1 - row2);
	        int columnDifference = Math.abs(column1 - column2);
	        return rowDifference + columnDifference;
	    }

	    //random kezdõ koordináták sorsolása egy tetszõleges playernek:
	    static Coordinates getRandomStartingCoordinates(String[][] level) {
	        int randomRow;
	        int randomColumn;
	        do {
	            randomRow = RANDOM.nextInt(height);//nemjo ezek nullát felvehetnek, de az most nem baj mert az "X", és azt kivédi a while itt
	            randomColumn = RANDOM.nextInt(width);

	        }while(!level[randomRow][randomColumn].equals(" "));

	        Coordinates startingCoordinates = new Coordinates();
	        startingCoordinates.setRow(randomRow);
	        startingCoordinates.setColumn(randomColumn);

	        return startingCoordinates;
	    }

	    //WALLS

	    static void addRandomWalls(String [][]level, int numberOfHorizontalWalls, int numberOfWerticalWalls ) {
	        //TODO fal ne kerüljön a játékosokra
	        for(int i = 0; i < numberOfHorizontalWalls; i++) {
	            addHorizontalWall(level);
	        }
	        for(int i = 0; i < numberOfWerticalWalls; i++) {
	            addVerticalWall(level);
	        }

	    }


	    static void addHorizontalWall(String [][]level) {

	        int wallWidth = RANDOM.nextInt(width-3);
	        int wallRow = RANDOM.nextInt(height-2)+1;
	        int wallColumn = RANDOM.nextInt(width-2-wallWidth);
	        for(int i = 0; i < wallWidth; i++ ) {
	            level[wallRow][wallColumn + i] = "X";
	        }
	    }

	    static void addVerticalWall(String [][]level) {

	        int wallHeight = RANDOM.nextInt(height-3);
	        int wallRow = RANDOM.nextInt(height-2 - wallHeight);
	        int wallColumn = RANDOM.nextInt(width-2)+1;
	        for(int i = 0; i < wallHeight; i++ ) {
	            level[wallRow + i][wallColumn] = "X";
	        }

	    }

	    //késleltetés
	    private static void addSomeDelay(int loopCounter, long timeout) throws InterruptedException {
	        System.out.println("-------" + loopCounter + "------");
	        Thread.sleep(timeout);
	    }

	    //maga a mozgás
	    static Coordinates makeMove(Direction direction, String[][] level, Coordinates oldCoordinates){
	        Coordinates newCoordinates = new Coordinates();// new Coordinate object
	        newCoordinates.setRow(oldCoordinates.getRow());//setting old coordinates
	        newCoordinates.setColumn(oldCoordinates.getColumn());
	        switch (direction) {
	            case UP:
	                if (level[oldCoordinates.getRow() - 1][oldCoordinates.getColumn()].equals(" ")) { //can go UP
	                    newCoordinates.setRow(oldCoordinates.getRow() - 1); //set newCoordinates
	                }
	                break;
	            case DOWN:
	                if (level[oldCoordinates.getRow() + 1][oldCoordinates.getColumn()].equals(" ")) { //can go DOWN
	                    newCoordinates.setRow(oldCoordinates.getRow() + 1);
	                }
	                break;
	            case LEFT:
	                if (level[oldCoordinates.getRow()][oldCoordinates.getColumn() - 1].equals(" ")) { //can go LEFT
	                    newCoordinates.setColumn(oldCoordinates.getColumn() - 1);
	                }
	                break;
	            case RIGHT:
	                if (level[oldCoordinates.getRow()][oldCoordinates.getColumn() + 1].equals(" ")) { //can go RIGHT
	                    newCoordinates.setColumn(oldCoordinates.getColumn() + 1);
	                }
	                break;
	        }
	        return newCoordinates;
	    }

	    //pálya megrajzolása
	    private static void initLevel(String[][] level) {
	        for (int row = 0; row < height; row++) {
	            for (int column = 0; column < width; column++) {
	                if(row == 0 ||  row == height-1 || column == 0 || column == width-1) {//walls
	                    level[row][column] = "X"; //walls
	                }else{
	                    level[row][column] = " ";
	                }
	            }
	        }
	    }

	    //körkörös mozgás
	    private static Direction changeDirection(Direction direction) {
	        switch (direction){
	            case RIGHT :
	                direction = Direction.DOWN;
	                break;
	            case DOWN  :
	                direction = Direction.LEFT;
	                break;
	            case LEFT :
	                direction = Direction.UP;
	                break;
	            case UP :
	                direction = Direction.RIGHT;
	                break;
	        }
	        return direction; //ha egyik s-w ág sem tut le, az eredeti iránnyal térünk vissza
	        //return: vezérlésátadó utasítás, metódus szinten dolgozik
	    }

	    //követõ mozgás: ha sorok vagy oszlopok indexében delta van akkor ezt az enemy igyekszik kiegyenlíteni = utána megy
	    // && = ha mehet arra = nincs fal = .equals(" ")
	    private static Direction changeDirectionTowards(String[][] level, Direction oiginalEnemyDirection, int enemyRow, int enemyColumn, int playerRow, int playerColumn ) {
	        if(playerRow < enemyRow && level[enemyRow -1][enemyColumn].equals(" ")) {// && mehet arra
	            return Direction.UP;
	        }
	        if(playerRow > enemyRow && level[enemyRow +1][enemyColumn].equals(" ")) {
	            return Direction.DOWN;
	        }
	        if(playerColumn < enemyColumn && level[enemyRow][enemyColumn - 1].equals(" ")) {
	            return Direction.LEFT;
	        }
	        if(playerColumn > enemyColumn && level[enemyRow][enemyColumn + 1].equals(" ")) {
	            return Direction.RIGHT;
	        }
	        return oiginalEnemyDirection;
	    }

	    public static void draw2DArray(String[][] arr){

	        for (int i = 0; i < arr.length; i++) {
	            for (int j = 0; j < arr[i].length; j++) {
	                System.out.print(arr[i][j]);
	            }
	            System.out.println();
	        }

	    }

	    public static boolean leftAStringInAnArray(String[][] level, String str){

	        for (int row = 0; row < height; row++) {
	            for (int column = 0; column < width; column++) {
	                if(level[row][column].equals(str));
	                return false;
	            }
	        }
	        return true;
	    }




	    //pálya és játékosok kirajzolása
	    public static void draw(String[][] board, String playerMark, Coordinates playerCoordinates, String enemyMark, Coordinates enemyCoordinates,
	                            String powerUpMark, Coordinates powerUpCoordinates, boolean powerUpPresentOnLevel, boolean powerUpActive){

	        for (int row = 0; row < height; row++) {
	            for (int column = 0; column < width; column++) {
	                Coordinates coordinatesToDraw = new Coordinates();
	                coordinatesToDraw.setRow(row);
	                coordinatesToDraw.setColumn(column);
	                if (coordinatesToDraw.isSame(playerCoordinates)) {
	                    System.out.print(playerMark);
	                } else if(coordinatesToDraw.isSame(enemyCoordinates)) {
	                    System.out.print(enemyMark);
	                } else if(powerUpPresentOnLevel && coordinatesToDraw.isSame(powerUpCoordinates)) {
	                    System.out.print(powerUpMark);
	                }else{
	                    System.out.print(board[row][column]);
	                }
	            }
	            System.out.println();
	        }
	        if(powerUpActive) {
	            System.out.println("power-up is active!");
	        }
	        if(powerUpPresentOnLevel) {
	            System.out.println("power-up is on the board!");
	        }

	    

	}

}
