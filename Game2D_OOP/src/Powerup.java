
public class Powerup extends Entity {
	
	boolean presentOnLevel = false;
	//boolean presentOnLevel = true; //�lb�l is jelen LEHET egy r�gebbi powerUp update hiba miatt*****
    boolean active = false;
    int presenceCounter = 0;
    int activeCounter = 0;
	
    public Powerup(String mark, Coordinates coordinates, Level level) {
		super(mark, coordinates, level);
		
	}

	public int incrementPrescenceCounter() {
		return ++presenceCounter;
	}
    
    public void resetPrescenceCounter() {
    	presenceCounter = 0;
    }
    
    public int incrementActiveCounter() {
		return ++activeCounter;
	}
    
    public void resetActiveCounter() {
    	activeCounter = 0;
    }

	public boolean isPresentOnLevel() {
		return presentOnLevel;
	}

	public boolean isActive() {
		return active;
	}
	
	public void activate() {
		active = true;
	}
	
	public void deactivate() {
		active = false;
	}
	
	public void showOnLevel() {
		presentOnLevel = true;
	}
	
	public void hideOnLevel() {
		presentOnLevel = false;
	}

	public int getPresenceCounter() {
		return presenceCounter;
	}

	public int getActiveCounter() {
		return activeCounter;
	}

	public void setPresenceCounter(int presenceCounter) {
		this.presenceCounter = presenceCounter;
	}

	public void setActiveCounter(int activeCounter) {
		this.activeCounter = activeCounter;
	}
	
	public boolean update(int refresh) {
		if (active) {
			incrementActiveCounter();
		} else {
			incrementPrescenceCounter();// EZZEL INDUL A POWERUP////minden iter�ci�ban n�velj�k eggyel a sz�ml�l�t
		}

		if (presenceCounter >= refresh) {// ez*
			if (presentOnLevel) {// jelen van, �s fog kapni random koordin�t�kat
				setCoordinates(level.getRandomCoordinates());// friss�l

			}
			// powerUpPresentOnLevel = !powerUpPresentOnLevel;
			// powerup.hideOnLevel();//****** eze �gy eredetileg hib�s a flette l�v� sor nem
			// v�ltja ki
			// ez jobb: (kiv�ltja)

			if (presentOnLevel) {
				hideOnLevel();
			} else {
				showOnLevel(); // vagy a p�ly�n van, vagy nincs, �s mindig///?????
			} // kiv�ltjuk x (most 20) k�r�nk�nt ennek az
				// ellenkez�j�t
			resetPrescenceCounter(); // *meg ez csin�lja hogy mindig el�lr�l kezd�dhessen a sz�ml�l�s �s 20
										// k�rig van pUp, 20 k�rig nincs
			// �s �gy tov�bb
		}
		if (activeCounter >= refresh) {
			// powerUpActive = false;
			deactivate();
			// powerUpActiveCounter = 0;
			resetActiveCounter();
			setCoordinates(level.getRandomCoordinates());

			//player.setEscapeCoordinates(level.getFarthestCorner(player.getCoordinates()));
			return true;
		}
		return false;
	}
	

    
	

}
