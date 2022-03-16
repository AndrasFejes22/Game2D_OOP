
public class Powerup extends Entity {
	
	//boolean presentOnLevel = false;
	boolean presentOnLevel = true; //élbõl is jelen LEHET egy régebbi powerUp update hiba miatt*****
    boolean active = false;
    int presenceCounter = 0;
    int activeCounter = 0;
	
    public Powerup(String mark, Coordinates coordinates) {
		super(mark, coordinates);
		
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
	
	
	
	
    
	

}
