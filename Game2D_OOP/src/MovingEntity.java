
public class MovingEntity extends Entity {
	
    private Coordinates escapeCoordinates;
    
    private Direction direction; 

    
    /**
	 * @param mark
	 * @param coordinates
	 * @param escapeCoordinates
	 * @param direction
	 */
	public MovingEntity(String mark, Coordinates coordinates, Coordinates escapeCoordinates, Direction direction, Level level) {
		super(mark, coordinates, level);
		this.escapeCoordinates = escapeCoordinates;
		this.direction = direction;
	}


	public Coordinates getEscapeCoordinates() {
		return escapeCoordinates;
	}


	public Direction getDirection() {
		return direction;
	}


	public void setEscapeCoordinates(Coordinates escapeCoordinates) {
		this.escapeCoordinates = escapeCoordinates;
	}


	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	

}	
