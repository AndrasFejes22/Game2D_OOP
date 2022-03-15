
public class Entity {
	
	private String mark;
	
	private Coordinates coordinates; 
    
    private Coordinates escapeCoordinates;
    
    private Direction direction; 

    
    /**
	 * @param mark
	 * @param coordinates
	 * @param escapeCoordinates
	 * @param direction
	 */
	public Entity(String mark, Coordinates coordinates, Coordinates escapeCoordinates, Direction direction) {
		super();
		this.mark = mark;
		this.coordinates = coordinates;
		this.escapeCoordinates = escapeCoordinates;
		this.direction = direction;
	}


	public String getMark() {
		return mark;
	}


	public Coordinates getCoordinates() {
		return coordinates;
	}


	public Coordinates getEscapeCoordinates() {
		return escapeCoordinates;
	}


	public Direction getDirection() {
		return direction;
	}


	public void setMark(String mark) {
		//ellenõrzés
		this.mark = mark;
	}


	public void setCoordinates(Coordinates coordinates) {
		this.coordinates = coordinates;
	}


	public void setEscapeCoordinates(Coordinates escapeCoordinates) {
		this.escapeCoordinates = escapeCoordinates;
	}


	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	

}	
