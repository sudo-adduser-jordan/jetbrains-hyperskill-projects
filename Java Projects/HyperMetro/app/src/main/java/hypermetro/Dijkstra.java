package hypermetro;


import java.util.*;
//-- Implementation Dijkstra from here
//-- https://stackabuse.com/graphs-in-java-dijkstras-algorithm/
//--

public class Dijkstra {
    GraphWeighted graphWeighted = new GraphWeighted(true);
    Map<Integer, NodeWeighted> nodes = new LinkedHashMap<>();
    static int pathCost = 0;
    List<Integer> pathStations = new ArrayList<>();


    public void addPathStations(Integer pathStations) {
        this.pathStations.add(pathStations);
    }

    public int findByName(String name, String line, Map<Integer, Station> ids) {
        int result = -1;
        for (Map.Entry<Integer, Station> entry : ids.entrySet()) {
            if (entry.getValue().name.equals(name) && entry.getValue().line.equals(line)) result = entry.getKey();
        }
        return result;
    }

    public void createGraph2(int idSrc, int idDst, Map<Integer, Station> ids, Map<String, List<Station>> metro) {

        //create nodes
        for (Map.Entry<String, List<Station>> entry : metro.entrySet()) {
            //  System.out.println(entry.getKey());
            for (int i = 0; i < entry.getValue().size(); i++) {
                nodes.put(entry.getValue().get(i).graph_id, new NodeWeighted(entry.getValue().get(i).graph_id, entry.getValue().get(i).name));
            }
        }

        //create edges
        NodeWeighted src;
        NodeWeighted dst;
        for (Map.Entry<String, List<Station>> entry : metro.entrySet()) {
            for (int i = 0; i < entry.getValue().size(); i++) {
                if (entry.getValue().get(i).prev != null && entry.getValue().get(i).prev.size() > 0) {
                    for (String s : entry.getValue().get(i).prev) {
                        int temp = findByName(s, entry.getKey(), ids);
                        src = nodes.get(entry.getValue().get(i).graph_id);
                        dst = nodes.get(temp);
                        if (temp != -1)
                        graphWeighted.addEdge(src, dst, 1);
                    }
                }
                if (entry.getValue().get(i).next != null && entry.getValue().get(i).next.size() > 0) {
                    for (String s : entry.getValue().get(i).next) {
                        int temp = findByName(s, entry.getKey(), ids);
                        src = nodes.get(entry.getValue().get(i).graph_id);
                        dst = nodes.get(temp);
                        if (temp != -1)
                        graphWeighted.addEdge(src, dst, 1);
                    }

                }

                if (entry.getValue().get(i).transfer != null && entry.getValue().get(i).transfer.size() > 0) {
                    for (Transfer t : entry.getValue().get(i).transfer) {
                        int temp = findByName(t.station, t.line, ids);
                        src = nodes.get(entry.getValue().get(i).graph_id);
                        dst = nodes.get(temp);
                        if (temp != -1)
                        graphWeighted.addEdge(src, dst, 1);
                    }
                }
            }
        }

        NodeWeighted start = nodes.get(idSrc);
        NodeWeighted finish = nodes.get(idDst);
        graphWeighted.DijkstraShortestPath(start, finish, this);
        Collections.reverse(pathStations);

        String prevLine = ids.get(pathStations.get(0)).line;
        for (Integer i : pathStations) {
            if (!ids.get(i).line.equals(prevLine)) {
                System.out.println("Transition to line " + ids.get(i).line);
                prevLine = ids.get(i).line;
            }
            System.out.println(ids.get(i).getName());
        }

    }

    public void createGraph(int idSrc, int idDst, Map<Integer, Station> ids, Map<String, List<Station>> metro) {

        //create nodes
        for (Map.Entry<String, List<Station>> entry : metro.entrySet()) {
            for (int i = 0; i < entry.getValue().size(); i++) {
                nodes.put(entry.getValue().get(i).graph_id, new NodeWeighted(entry.getValue().get(i).graph_id, entry.getValue().get(i).name));
            }
        }

        //create edges
        NodeWeighted src;
        NodeWeighted dst;
        for (Map.Entry<String, List<Station>> entry : metro.entrySet()) {
            for (int i = 0; i < entry.getValue().size(); i++) {
                if (entry.getValue().get(i).prev != null && entry.getValue().get(i).prev.size() > 0) {
                    for (String s : entry.getValue().get(i).prev) {
                        int temp = findByName(s, entry.getKey(), ids);
                        src = nodes.get(entry.getValue().get(i).graph_id);
                        dst = nodes.get(temp);
                        if (temp != -1)
                            graphWeighted.addEdge(src, dst, ids.get(temp).time);
                    }
                }
                if (entry.getValue().get(i).next != null && entry.getValue().get(i).next.size() > 0) {
                    for (String s : entry.getValue().get(i).next) {
                        int temp = findByName(s, entry.getKey(), ids);
                        src = nodes.get(entry.getValue().get(i).graph_id);
                        dst = nodes.get(temp);
                        if (temp != -1)
                            graphWeighted.addEdge(src, dst, entry.getValue().get(i).time);
                    }

                }

                if (entry.getValue().get(i).transfer != null && entry.getValue().get(i).transfer.size() > 0) {
                    for (Transfer t : entry.getValue().get(i).transfer) {
                        int temp = findByName(t.station, t.line, ids);
                        src = nodes.get(entry.getValue().get(i).graph_id);
                        dst = nodes.get(temp);
                        if (temp != -1)
                            graphWeighted.addEdge(src, dst, 5);
                    }
                }
            }
        }

        NodeWeighted start = nodes.get(idSrc);
        NodeWeighted finish = nodes.get(idDst);
        graphWeighted.DijkstraShortestPath(start, finish, this);
        Collections.reverse(pathStations);

        String prevLine = ids.get(pathStations.get(0)).line;
        for (Integer i : pathStations) {
            if (!ids.get(i).line.equals(prevLine)) {
                System.out.println("Transition to line " + ids.get(i).line);
                prevLine = ids.get(i).line;
            }
            System.out.println(ids.get(i).getName());
        }
        if (pathCost > 0)
            System.out.println("Total: " + (pathCost) + " minutes in the way\n");


    }
}

class EdgeWeighted implements Comparable<EdgeWeighted> {

    NodeWeighted source;
    NodeWeighted destination;
    double weight;

    EdgeWeighted(NodeWeighted s, NodeWeighted d, double w) {
        source = s;
        destination = d;
        weight = w;
    }

    public String toString() {
        return String.format("(%s -> %s, %f)", source.name, destination.name, weight);
    }

    public int compareTo(EdgeWeighted otherEdge) {

        if (this.weight > otherEdge.weight) {
            return 1;
        }
        else return -1;
    }
}


class NodeWeighted {
    int n;
    String name;
    private boolean visited;
    LinkedList<EdgeWeighted> edges;

    NodeWeighted(int n, String name) {
        this.n = n;
        this.name = name;
        visited = false;
        edges = new LinkedList<>();
    }

    boolean isVisited() {
        return visited;
    }

    void visit() {
        visited = true;
    }

    void unvisit() {
        visited = false;
    }
}

class GraphWeighted {
    private Set<NodeWeighted> nodes;
    private boolean directed;

    GraphWeighted(boolean directed) {
        this.directed = directed;
        nodes = new HashSet<>();
    }

    public void addNode(NodeWeighted... n) {

        nodes.addAll(Arrays.asList(n));
    }
    public void addEdge(NodeWeighted source, NodeWeighted destination, double weight) {

        nodes.add(source);
        nodes.add(destination);

        addEdgeHelper(source, destination, weight);

        if (!directed && source != destination) {
            addEdgeHelper(destination, source, weight);
        }
    }

    public void addEdgeHelper(NodeWeighted a, NodeWeighted b, double weight) {
        for (EdgeWeighted edge : a.edges) {
            if (edge.source == a && edge.destination == b) {
                edge.weight = weight;
                return;
            }
        }

        a.edges.add(new EdgeWeighted(a, b, weight));
    }

    public void printEdges() {
        for (NodeWeighted node : nodes) {
            LinkedList<EdgeWeighted> edges = node.edges;

            if (edges.isEmpty()) {
                System.out.println("Node " + node.name + " has no edges.");
                continue;
            }
            System.out.print("Node " + node.name + " has edges to: ");

            for (EdgeWeighted edge : edges) {
                System.out.print(edge.destination.name + "(" + edge.weight + ") ");
            }
            System.out.println();
        }
    }

    public boolean hasEdge(NodeWeighted source, NodeWeighted destination) {
        LinkedList<EdgeWeighted> edges = source.edges;
        for (EdgeWeighted edge : edges) {
            if (edge.destination == destination) {
                return true;
            }
        }
        return false;
    }

    public void resetNodesVisited() {
        for (NodeWeighted node : nodes) {
            node.unvisit();
        }
    }

    public void DijkstraShortestPath(NodeWeighted start, NodeWeighted end, Dijkstra d) {
        HashMap<NodeWeighted, NodeWeighted> changedAt = new HashMap<>();
        changedAt.put(start, null);


        HashMap<NodeWeighted, Double> shortestPathMap = new HashMap<>();

        for (NodeWeighted node : nodes) {
            if (node == start)
                shortestPathMap.put(start, 0.0);
            else shortestPathMap.put(node, Double.POSITIVE_INFINITY);
        }

        for (EdgeWeighted edge : start.edges) {
            shortestPathMap.put(edge.destination, edge.weight);
            changedAt.put(edge.destination, start);
        }

        start.visit();

        while (true) {
            NodeWeighted currentNode = closestReachableUnvisited(shortestPathMap);
            if (currentNode == null) {
                System.out.println("There isn't a path between " + start.name + " and " + end.name);
                return;
            }

            if (currentNode == end) {

                NodeWeighted child = end;

                String path = end.name;
                d.addPathStations(end.n);
                while (true) {
                    NodeWeighted parent = changedAt.get(child);
                    if (parent == null) {
                        break;
                    }

                    path = parent.name + " " + path;
                    child = parent;
                    d.addPathStations(parent.n);
                }

                d.pathCost = (int) Math.round(shortestPathMap.get(end));
                return;
            }
            currentNode.visit();

            for (EdgeWeighted edge : currentNode.edges) {
                if (edge.destination.isVisited())
                    continue;

                if (shortestPathMap.get(currentNode)
                        + edge.weight
                        < shortestPathMap.get(edge.destination)) {
                    shortestPathMap.put(edge.destination,
                            shortestPathMap.get(currentNode) + edge.weight);
                    changedAt.put(edge.destination, currentNode);
                }
            }
        }
    }

    private NodeWeighted closestReachableUnvisited(HashMap<NodeWeighted, Double> shortestPathMap) {

        double shortestDistance = Double.POSITIVE_INFINITY;
        NodeWeighted closestReachableNode = null;
        for (NodeWeighted node : nodes) {
            if (node.isVisited())
                continue;

            double currentDistance = shortestPathMap.get(node);
            if (currentDistance == Double.POSITIVE_INFINITY)
                continue;

            if (currentDistance < shortestDistance) {
                shortestDistance = currentDistance;
                closestReachableNode = node;
            }
        }
        return closestReachableNode;
    }

}
