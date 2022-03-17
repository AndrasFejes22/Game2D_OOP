
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
	
	//maga a mozgás
    public void makeMove(){
        Coordinates newCoordinates = new Coordinates(getCoordinates());// new Coordinate object
        //newCoordinates.setRow(oldCoordinates.getRow());//setting old coordinates
        //newCoordinates.setColumn(oldCoordinates.getColumn());
        switch (direction) {
            case UP:
                if (level.isEmpty(new Coordinates(getCoordinates().getRow() - 1, getCoordinates().getColumn()))) { //can go UP
                    newCoordinates.setRow(getCoordinates().getRow() - 1); //set newCoordinates
                }
                break;
            case DOWN:
                if (level.isEmpty(new Coordinates(getCoordinates().getRow() + 1, getCoordinates().getColumn()))) { //can go DOWN
                    newCoordinates.setRow(getCoordinates().getRow() + 1);
                }
                break;
            case LEFT:
                if (level.isEmpty(new Coordinates(getCoordinates().getRow(), getCoordinates().getColumn() - 1))) { //can go LEFT
                    newCoordinates.setColumn(getCoordinates().getColumn() - 1);
                }
                break;
            case RIGHT:
                if (level.isEmpty(new Coordinates(getCoordinates().getRow(), getCoordinates().getColumn() + 1))) { //can go RIGHT
                    newCoordinates.setColumn(getCoordinates().getColumn() + 1);
                }
                break;
        }
       setCoordinates(newCoordinates);
    }
	
	

}	
