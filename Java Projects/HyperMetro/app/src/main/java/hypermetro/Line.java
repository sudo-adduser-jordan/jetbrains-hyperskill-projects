package hypermetro;



import java.util.List;

public class Line {
    List<Station> line;
    String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setLine(List<Station> line) {
        this.line = line;
    }

    public List<Station> getLine() {
        return line;
    }
}
