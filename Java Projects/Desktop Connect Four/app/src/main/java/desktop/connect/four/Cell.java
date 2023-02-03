package desktop.connect.four;


public class Cell {

    private int row;
    private int column;
    private String text;

    public Cell(int row, int column) {
        this.row = row;
        this.column = column;
        this.text = "";
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}