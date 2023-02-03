package hypermetro;

import java.util.ArrayList;
import java.util.List;

public class Station {

    public String name;
    public String line = "";
    public int graph_id;
    public List<Transfer> transfer;
    public List<String> prev;
    public List<String> next;
    public int time;

    public void setPrev(List<String> prev) {
        this.prev = prev;
    }

    public void setNext(List<String> next) {
        this.next = next;
    }

    public List<String> getNext() {
        return next;
    }

    public List<String> getPrev() {
        return prev;
    }

    public void setGraph_id(int graph_id) {
        this.graph_id = graph_id;
    }

    public int getGraph_id() {
        return graph_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTransfer(List<Transfer> transfer) {
        this.transfer = transfer;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    public List<Transfer> getTransfer() {
        return transfer;
    }

}
