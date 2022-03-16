
public class Entity {
	
	private String mark;
	
	private Coordinates coordinates; 
	
	//private Level level;
    
    

    
    /**
	 * @param mark
	 * @param coordinates
	 * @param escapeCoordinates
	 * @param direction
	 */
	public Entity(String mark, Coordinates coordinates)  {
		super();
		this.mark = mark;
		this.coordinates = coordinates;
		
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
