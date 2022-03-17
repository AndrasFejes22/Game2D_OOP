
public class Powerup extends Entity {
	
	boolean presentOnLevel = false;
	//boolean presentOnLevel = true; //élbõl is jelen LEHET egy régebbi powerUp update hiba miatt*****
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
			incrementPrescenceCounter();// EZZEL INDUL A POWERUP////minden iterációban növeljük eggyel a számlálót
		}

		if (presenceCounter >= refresh) {// ez*
			if (presentOnLevel) {// jelen van, és fog kapni random koordinátákat
				setCoordinates(level.getRandomCoordinates());// frissül

			}
			// powerUpPresentOnLevel = !powerUpPresentOnLevel;
			// powerup.hideOnLevel();//****** eze így eredetileg hibás a flette lévõ sor nem
			// váltja ki
			// ez jobb: (kiváltja)

			if (presentOnLevel) {
				hideOnLevel();
			} else {
				showOnLevel(); // vagy a pályán van, vagy nincs, és mindig///?????
			} // kiváltjuk x (most 20) körönként ennek az
				// ellenkezõjét
			resetPrescenceCounter(); // *meg ez csinálja hogy mindig elõlrõl kezdõdhessen a számlálás és 20
										// körig van pUp, 20 körig nincs
			// és így tovább
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
