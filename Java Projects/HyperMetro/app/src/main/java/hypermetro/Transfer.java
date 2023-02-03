package hypermetro;



public class Transfer {
    String line;
    String station;

    public Transfer (String line, String station) {
        this.line = line;
        this.station = station;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getStation() {
        return station;
    }

    public String getLine() {
        return line;
    }
}
