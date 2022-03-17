
public class Coordinates {
	private int row;
	private int column;
    /**
	 * @param row
	 * @param column
	 */
	public Coordinates(int row, int column) {
		super();
		this.row = row;
		this.column = column;
	}
	
	public Coordinates(Coordinates other) {
		this(other.getRow(), other.getColumn());
	}

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public boolean isSame(Coordinates other){
        return this.row == other.row && this.column == other.column;
    }
    
    public int distanceFrom(Coordinates other) {//pl randomCoordinates.calculateDistance(playerStartingCoordinates);

        int rowDifference = Math.abs(this.row - other.getRow());
        int columnDifference = Math.abs(this.column - other.getColumn());
        return rowDifference + columnDifference;
        
    }
}
