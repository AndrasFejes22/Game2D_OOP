
public class Entity {
	
	private String mark;
	
	private Coordinates coordinates; 
	
	protected Level level;
    
    

    
    /**
	 * @param mark
	 * @param coordinates
	 * @param escapeCoordinates
	 * @param direction
	 */
	public Entity(String mark, Coordinates coordinates, Level level) {
		super();
		this.mark = mark;
		this.coordinates = coordinates;
		this.level = level;
		
	}


	public String getMark() {
		return mark;
	}


	public Coordinates getCoordinates() {
		return coordinates;
	}



	public void setMark(String mark) {
		//ellenõrzés
		this.mark = mark;
	}


	public void setCoordinates(Coordinates coordinates) {
		this.coordinates = coordinates;
	}


	
	
	

}	
