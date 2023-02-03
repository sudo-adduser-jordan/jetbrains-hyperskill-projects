package hypermetro;


import java.util.*;

public class HMetro {
    Map<String, List<Station>> hyperMetro;
    Map<Integer, Station> ids = new LinkedHashMap<>();


    public void run(String filePath) {
        hyperMetro = Utils.loadFromJson(filePath);
        indexStation();
        menu();
    }

    public void indexStation() {
        int startId = 0;
        ids = new LinkedHashMap<>();
        for (Map.Entry<String, List<Station>> entry : hyperMetro.entrySet()) {
            for (Station station : entry.getValue()) {
                station.setGraph_id(startId);
                station.line = entry.getKey();
                ids.put(startId, station);
                startId++;
            }
        }
    }

    public void menu() {
        Scanner sc = new Scanner(System.in);
        boolean isRun = true;

        while (isRun) {
            String command = sc.nextLine();
            switch (command.split(" ")[0]) {
                case "/route":
                  //  indexStation();
                    String[] arrayr = command.split(" \"");
                    String line1r = arrayr[1].replace("\"", "");
                    String station1r = arrayr[2].replace("\"", "");
                    String line2r = arrayr[3].replace("\"", "");
                    String station2r = arrayr[4].replace("\"", "");
                    int idSrc = 0;
                    int idDst = 0;
                    for (Map.Entry<String, List<Station>> entry : hyperMetro.entrySet()) {
                        for (Station s : entry.getValue()) {
                            if (s.line.equals(line1r) && s.name.equals(station1r)) idSrc = s.graph_id;
                            if (s.line.equals(line2r) && s.name.equals(station2r)) idDst = s.graph_id;
                        }
                    }
                    Dijkstra d = new Dijkstra();
                    d.createGraph2(idSrc, idDst, ids, hyperMetro);
                    break;
                case "/fastest-route":
                    //indexStation();
                    String[] arrayf = command.split(" \"");
                    String line1f = arrayf[1].replace("\"", "");
                    String station1f = arrayf[2].replace("\"", "");
                    String line2f = arrayf[3].replace("\"", "");
                    String station2f = arrayf[4].replace("\"", "");
                    idSrc = 0;
                    idDst = 0;
                    for (Map.Entry<String, List<Station>> entry : hyperMetro.entrySet()) {
                        for (Station s : entry.getValue()) {
                            if (s.line.equals(line1f) && s.name.equals(station1f)) idSrc = s.graph_id;
                            if (s.line.equals(line2f) && s.name.equals(station2f)) idDst = s.graph_id;
                        }
                    }
                    d = new Dijkstra();
                    d.createGraph(idSrc, idDst, ids, hyperMetro);
                    break;
                case "/exit":
                    isRun = false;
                    break;
            }
        }

    }
}
