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
	    static int playerLifes = 2;
	    static int counterPlayerLifes = 0;
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
	            addRandomWalls(level, 7, 7);
	            isPassableCounter++;
	        }while(!isPassable(level));

	        System.out.println("The No " + isPassableCounter + " board is passable");

	        ////////DRAW ASTERISKS OR NOT/////////
	        //overloaded isPassable() //drawing asterisks
	        //isPassable(level, true);
	        isPassable(level, false);

	        ////////VIZSG�LAT ELEJE///////
	        //ez a 2 sor csak azt vizsg�lja, hogy h�nyadik gener�l�sra kapunk �tj�rhat� p�ly�t, a f� programban nem kell
	        //draw2DArray(level);
	        //System.exit(0);
	        ////////VIZSG�LAT V�GE///////


	        //Who will win?
	        GameResult gameResult = GameResult.TIE; //default

	        //////PLAYER//////
	        //random first coordinates for the player
	        Coordinates playerCoordinates = getRandomStartingCoordinates(level);
	        Entity player = new Entity("O", playerCoordinates, getFarthestCorner(level, playerCoordinates), Direction.RIGHT);
	        /*
	        String playerMark = "O"; //represents the player
	        Coordinates playerEscapeCoordinates = getFarthestCorner(level, playerCoordinates);
	        Direction playerDirection = Direction.RIGHT; //First, the player moving to the right
	        */
	        //////ENEMY//////

	        
	        //random first coordinates for the enemy
	        Coordinates enemyCoordinates = getRandomStartingCoordinatesForADistance(level, player.getCoordinates(), 10); 
	        Entity enemy = new Entity("@", enemyCoordinates, getFarthestCorner(level, enemyCoordinates), Direction.LEFT);
	        /*
	        String enemyMark = "@"; //represents the enemy
	        Coordinates enemyEscapeCoordinates = getFarthestCorner(level, enemyCoordinates);
	        Direction enemyDirection = Direction.LEFT; //First, the player moving to the left
	        */

	        //////POWER-UP//////

	        String powerUpMark = "*"; //represents the power-up, egy helyben fog �llni
	        //random first coordinates for the power-up
	        Coordinates powerUpCoordinates = getRandomStartingCoordinates(level);
	        
	        boolean powerUpPresentOnLevel = false;
	        boolean powerUpActive = false;
	        int powerUpPresenceCounter = 0;
	        int powerUpActiveCounter = 0;


			for (int iterationNumber = 1; iterationNumber < GAME_LOOP_NUMBER; iterationNumber++) {// l�pteti a karaktert
																									// -->makeMove
																									// method
				/////// UPDATED PLAYER MOVING////////
				// player ir�nyv�ltoztat�sa
				if (powerUpActive) {// powerup akt�v = a j�t�kos kergeti az enemyt

					//playerDirection = getShortestPath(level, playerDirection, playerCoordinates, enemyCoordinates);
					player.setDirection(getShortestPath(level, player.getDirection(), player.getCoordinates(), enemy.getCoordinates()));

				} else {
					if (powerUpPresentOnLevel) {

						// megy a powerUp fel�
						//playerDirection = getShortestPath(level, playerDirection, playerCoordinates, powerUpCoordinates);
						player.setDirection(getShortestPath(level, player.getDirection(), player.getCoordinates(), powerUpCoordinates));
								

					} else {
						// player menek�l:
						if (iterationNumber % 50 == 0) {// v�laszt�s 50 l�p�senk�nt
							// ink�bb menek�lj�n a legt�volabbi pontba:
							//playerEscapeCoordinates = getFarthestCorner(level, playerCoordinates);
							player.setEscapeCoordinates(getFarthestCorner(level, player.getCoordinates()));

						}
						//playerDirection = getShortestPath(level, playerDirection, playerCoordinates,playerEscapeCoordinates);
						player.setDirection(getShortestPath(level, player.getDirection(), player.getCoordinates(), player.getEscapeCoordinates()));
								
					}
				}
				// kiszedj�k a koordin�t�kat �s �tadjuk a draw()-nak
				// ez a 3 sor a l�ptet�se a playernek
				//playerCoordinates = makeMove(player.getDirection(), level, playerCoordinates);//VIDE�S // �j �rt�ket kap a
				player.setCoordinates(makeMove(player.getDirection(), level, player.getCoordinates())); // �j �rt�ket kap a
																							// playerCoordinates
																							// (param�ter az el�z�)

				/////// UPDATED ENEMY MOVING////////

				// enemy ir�nyv�ltoztat�sa: ez nincs benne if blokkban mert lek�veti a player
				// mozg�s�t, (ut�namegy), teh�t a player mozg�sa vez�rli
				if (powerUpActive) {

					if (iterationNumber % 50 == 0) {// v�laszt�s 50 l�p�senk�nt
						enemy.setEscapeCoordinates(getFarthestCorner(level, enemy.getCoordinates()));

					}
					enemy.setDirection(getShortestPath(level, enemy.getDirection(), enemy.getCoordinates(), enemy.getEscapeCoordinates()));
				} else {

					// felokos�tva:
					// enemy �ld�z:
					enemy.setDirection(getShortestPath(level, enemy.getDirection(), enemy.getCoordinates(), player.getCoordinates()));
				}
				// enemy l�ptet�se:
				// kiszedj�k a koordin�t�kat �s �tadjuk a draw()-nak
				if (iterationNumber % 2 == 0) {// minden 2. k�rben l�p csak
					//enemyCoordinates = makeMove(enemyDirection, level, enemyCoordinates);// friss�l
					enemy.setCoordinates(makeMove(enemy.getDirection(), level, enemy.getCoordinates()));// friss�l
				}

				// powerUp updateing:
				if (powerUpActive) {
					powerUpActiveCounter++;
				} else {
					powerUpPresenceCounter++;// minden iter�ci�ban n�velj�k eggyel a sz�ml�l�t
				}
				// powerUpPresenceCounter mennyi ideig van a p�ly�n
				if (powerUpPresenceCounter >= powerUpInLevel) {// ez*
					if (powerUpPresentOnLevel) {// jelen van, �s fog kapni random koordin�t�kat
						powerUpCoordinates = getRandomStartingCoordinates(level);// friss�l

					}
					powerUpPresentOnLevel = !powerUpPresentOnLevel; // vagy a p�ly�n van, vagy nincs, �s mindig
																	// kiv�ltjuk x (most 20) k�r�nk�nt ennek az
																	// ellenkez�j�t
					powerUpPresenceCounter = 0; // *meg ez csin�lja hogy mindig el�lr�l kezd�dhessen a sz�ml�l�s �s 20
												// k�rig van pUp, 20 k�rig nincs
					// �s �gy tov�bb
				}
				if (powerUpActiveCounter >= powerUpInLevel) {
					powerUpActive = false;
					powerUpActiveCounter = 0;
					powerUpCoordinates = getRandomStartingCoordinates(level);

					player.setEscapeCoordinates(getFarthestCorner(level, player.getCoordinates()));

				}

				// player-powerUp interaction handling:
				if (powerUpPresentOnLevel && player.getCoordinates().isSame(powerUpCoordinates)) {
					powerUpActive = true;
					powerUpPresentOnLevel = false;
					powerUpPresenceCounter = 0;
					enemy.setEscapeCoordinates(getFarthestCorner(level, enemy.getCoordinates()));

				}

				// drawing level and a playerMark; minden k�rben mindenki kirajzol�sa = "mozi"
				draw(level, player.getMark(), player.getCoordinates(), enemy.getMark(), enemy.getCoordinates(), powerUpMark, powerUpCoordinates,
						powerUpPresentOnLevel, powerUpActive);

				// v�rakoztat�s
				addSomeDelay(iterationNumber, 150L);// print the iteration number and do the delay

				// ha az enemy elkapta a playert (= a koordin�t�ik megegyeznek), akkor game over
				if (player.getCoordinates().isSame(enemy.getCoordinates())) {
					
					if (powerUpActive) {
						gameResult = GameResult.WIN;
						break;
					} else {
						//playerLifes--;// t�bb �let?
						//if(playerLifes == 0) {
	                        gameResult = GameResult.LOSE;
	                        System.out.println("The enemy caught the player!");
	                        //break;
	                    //}
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





	    //rajzolja-e a csillagok terjed�s�t
	    static boolean isPassable(String[][] level) {
	        return isPassable(level, false);

	    }

	    //Csak olyan p�ly�t rajzoljon amiben nincsenek z�rt terek:


	    static boolean isPassable(String[][] level, boolean drawAsterisks) {
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

	    //UPDATED PLAYER MOVING
	    //Az �t megtal�l�sa a terjed� csillagok method visszafel�
	    //A *-ok ugyanis mindig a legr�videbb ir�nyban terjednek
	    static Direction getShortestPath(String[][]level, Direction defaultDirection, Coordinates from, Coordinates to) {
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

	    private static boolean spreadAsterisksWithCheck(String[][] levelCopy) {
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

	    private static boolean spreadAsterisks(String[][] levelCopy) {
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
	            case UP://menjen erre vagy jobbra, vagy balra de mindenk�ppen elfele, v�gs� esetben a falnak is �tk�zhet
	                if (level[enemyRow - 1][enemyColumn].equals(" ")) {
	                    return Direction.UP;
	                }else if (level[enemyRow][enemyColumn-1].equals(" ")) {
	                    return Direction.LEFT;
	                }else if (level[enemyRow][enemyColumn +1].equals(" ")) {
	                    return Direction.RIGHT;
	                }else {
	                    return Direction.UP;
	                }

	            case DOWN://menjen erre vagy jobbra, vagy balra de mindenk�ppen elfele, v�gs� esetben a falnak is �tk�zhet
	                if (level[enemyRow + 1][enemyColumn].equals(" ")) {
	                    return Direction.DOWN;
	                }else if (level[enemyRow][enemyColumn-1].equals(" ")) {
	                    return Direction.LEFT;
	                }else if (level[enemyRow][enemyColumn +1].equals(" ")) {
	                    return Direction.RIGHT;
	                }else {
	                    return Direction.DOWN;
	                }
	            case RIGHT://menjen erre vagy jobbra, vagy balra de mindenk�ppen elfele, v�gs� esetben a falnak is �tk�zhet
	                if (level[enemyRow][enemyColumn + 1].equals(" ")) {
	                    return Direction.RIGHT;
	                }else if (level[enemyRow - 1][enemyColumn].equals(" ")) {
	                    return Direction.UP;
	                }else if (level[enemyRow + 1][enemyColumn ].equals(" ")) {
	                    return Direction.DOWN;
	                }else {
	                    return Direction.RIGHT;
	                }

	            case LEFT://menjen erre vagy jobbra, vagy balra de mindenk�ppen elfele, v�gs� esetben a falnak is �tk�zhet
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

	    //random kezd� koordin�t�k sorsol�sa az eneminek, de**
	    private static Coordinates getRandomStartingCoordinatesForADistance(String[][] level, Coordinates playerStartingCoordinates, int distance) {
	        int playerStartingRow = playerStartingCoordinates.getRow();
	        int playerStartingColumn = playerStartingCoordinates.getColumn();
	        int randomRow;
	        int randomColumn;
	        int counter = 0; //infinite loop kiv�d�se //////////////EZ M�G NEM J���
	        do {
	            counter++;
	            randomRow = RANDOM.nextInt(height);//nemjo ezek null�t felvehetnek!!!
	            randomColumn = RANDOM.nextInt(width);
	            //**nem lehet a k�t koordin�ta k�zelebb egym�shoz mint distance p�lyaegys�g
	        }while(counter < 1000
	                && (!level[randomRow][randomColumn].equals(" ") || calculateDistance(randomRow, randomColumn, playerStartingRow, playerStartingColumn) < distance));
	        
	        //helyi Coordinates object volt l�trehozva konstruktor�val-->�tment inline-ba: 3 sorral kevesebb
	        //�s a mem�ri�t sem foglaljuk
	        //startingCoordinates.setRow(randomRow);
	        //startingCoordinates.setColumn(randomColumn);

	        return new Coordinates(randomRow, randomColumn);
	    }

	    private static int calculateDistance(int row1, int column1, int row2, int column2) {

	        int rowDifference = Math.abs(row1 - row2);
	        int columnDifference = Math.abs(column1 - column2);
	        return rowDifference + columnDifference;
	    }

	    //random kezd� koordin�t�k sorsol�sa egy tetsz�leges playernek:
	    static Coordinates getRandomStartingCoordinates(String[][] level) {
	        int randomRow;
	        int randomColumn;
	        do {
	            randomRow = RANDOM.nextInt(height);//nemjo ezek null�t felvehetnek, de az most nem baj mert az "X", �s azt kiv�di a while itt
	            randomColumn = RANDOM.nextInt(width);

	        }while(!level[randomRow][randomColumn].equals(" "));
	        /*
	         * ez volt, konstruktor �s inline el�tt:
	        Coordinates startingCoordinates = new Coordinates();
	        startingCoordinates.setRow(randomRow);
	        startingCoordinates.setColumn(randomColumn);
	        return startingCoordinates;
	        */
	        
	        return new Coordinates(randomRow, randomColumn);
	    }

	    //WALLS

	    static void addRandomWalls(String [][]level, int numberOfHorizontalWalls, int numberOfWerticalWalls ) {
	        //TODO fal ne ker�lj�n a j�t�kosokra
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

	    //k�sleltet�s
	    private static void addSomeDelay(int loopCounter, long timeout) throws InterruptedException {
	        System.out.println("-------" + loopCounter + "------");
	        Thread.sleep(timeout);
	    }

	    //maga a mozg�s
	    static Coordinates makeMove(Direction direction, String[][] level, Coordinates oldCoordinates){
	        Coordinates newCoordinates = new Coordinates(oldCoordinates.getRow(), oldCoordinates.getColumn());// new Coordinate object
	        //newCoordinates.setRow(oldCoordinates.getRow());//setting old coordinates
	        //newCoordinates.setColumn(oldCoordinates.getColumn());
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

	    //p�lya megrajzol�sa
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

	    //k�rk�r�s mozg�s
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
	        return direction; //ha egyik s-w �g sem tut le, az eredeti ir�nnyal t�r�nk vissza
	        //return: vez�rl�s�tad� utas�t�s, met�dus szinten dolgozik
	    }

	    //k�vet� mozg�s: ha sorok vagy oszlopok index�ben delta van akkor ezt az enemy igyekszik kiegyenl�teni = ut�na megy
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




	    //p�lya �s j�t�kosok kirajzol�sa
	    public static void draw(String[][] board, String playerMark, Coordinates playerCoordinates, String enemyMark, Coordinates enemyCoordinates,
	                            String powerUpMark, Coordinates powerUpCoordinates, boolean powerUpPresentOnLevel, boolean powerUpActive){

	        for (int row = 0; row < height; row++) {
	            for (int column = 0; column < width; column++) {
	                Coordinates coordinatesToDraw = new Coordinates(row, column);
	                //coordinatesToDraw.setRow(row);
	               // coordinatesToDraw.setColumn(column);
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
	        System.out.println("Player lives: " + playerLifes);
	        if(powerUpActive) {
	            System.out.println("power-up is active!");
	        }
	        if(powerUpPresentOnLevel) {
	            System.out.println("power-up is on the board!");
	        }

	    

	}

}