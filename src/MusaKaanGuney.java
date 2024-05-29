import java.awt.*;
import java.io.File;
import java.util.*;
import java.io.FileNotFoundException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class MusaKaanGuney {
    /**
     * Creating an Arraylist to use connections between two cities.
     */
    static ArrayList<Connections> connectionsList = new ArrayList<>();
    private static final int INF = Integer.MAX_VALUE;

    /**
     * Reading a text files and putting the datas into the arraylists.
     * Taking starting city and destination city as inputs.
     * Creating a graph depends on the datas in the textfiles in order to use Djikstra algorithm.
     * Calling a dijkstra function in order to implement all codes.
     */
    public static void main(String[] args) throws FileNotFoundException {
        String fileName = "city_coordinates.txt";
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.printf("%s can not be found.", fileName);
            System.exit(1); // exit the program
        }
        Scanner inputFile = new Scanner(file);
        ArrayList<City> cities = new ArrayList<>();
        while (inputFile.hasNextLine()) {
            String[] line = inputFile.nextLine().split(",");
            String cityName = line[0];
            double xCoor = Double.parseDouble(line[1]);
            double yCoor = Double.parseDouble(line[2]);
            City city = new City(cityName, xCoor, yCoor);
            cities.add(city);
        }
        inputFile.close();


        String fileName2 = "city_connections.txt";
        File file2 = new File(fileName2);
        if (!file2.exists()) {
            System.out.printf("%s can not be found.", fileName2);
            System.exit(1); // exit the program
        }
        Scanner inputFile2 = new Scanner(file2);
        while (inputFile2.hasNextLine()) {
            String[] line2 = inputFile2.nextLine().split(",");
            String connections1 = line2[0];
            String connections2 = line2[1];
            Connections connection = new Connections(connections1, connections2);
            connectionsList.add(connection);
        }
        inputFile2.close();

        String cityStarting = "";
        String cityDestination = "";
        int desttmp = 0;
        int starttmp = 0;

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter starting city: ");
            cityStarting = scanner.nextLine();
            for (City city : cities) {
                if (Objects.equals(city.cityName, cityStarting)) {
                    starttmp++;
                }
            }
            if (starttmp == 0) {
                System.out.println("City named '" + cityStarting + "' not found. Please enter a valid city name.");
            }
            else {
                break;
            }
        }

        while (true) {
            System.out.print("Enter destination city: ");
            cityDestination = scanner.nextLine();;
            for (City city : cities) {
                if (Objects.equals(city.cityName, cityDestination)) {
                    desttmp++;
                }
            }
            if (desttmp == 0) {
                System.out.println("City named '" + cityDestination + "' not found. Please enter a valid city name.");
            } else {
                break;
            }
        }
        scanner.close();

        int src = findIndex(cities, cityStarting);


        int m = cities.size();
        int n = connectionsList.size();
        double[][] graph = new double[m][m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (cities.get(i).cityName.equals(connectionsList.get(j).connection1)) {
                    int index = findIndex(cities, connectionsList.get(j).connection2);
                    double x1 = cities.get(i).xCoordinate;
                    double y1 = cities.get(i).yCoordinate;
                    double x2 = cities.get(index).xCoordinate;
                    double y2 = cities.get(index).yCoordinate;
                    double distance = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
                    graph[i][index] = distance;
                    graph[index][i] = distance;
                }
            }
        }
        dijkstra(graph, src, cityDestination, cities);
    }

    /**
     * Find the index of the given city in the cities arraylist
     * @param cities Arraylist of City objects
     * @param name Name of the city asked
     * @return index of the city
     */
    private static int findIndex(ArrayList<City> cities, String name) {
        int index = -1;
        for (int i = 0; i < cities.size(); i++) {
            City city = cities.get(i);
            if (Objects.equals(city.cityName, name)) {
                index = i;
                break;
            }
        }
        return index;
    }

    /**
     * using dijkstra algorithm to find the shortest path.
     * @param graph using 2-D Array to implement Dijkstra algorithm.
     * @param src to declare starting city.
     * @param dest to declare destination city.
     * @param cities Arraylist of City objects
     */
    private static void dijkstra(double[][] graph, int src, String dest, ArrayList<City> cities) {
        int V = graph.length;
        double[] dist = new double[V];
        boolean[] visited = new boolean[V];

        Arrays.fill(dist, INF);
        Arrays.fill(visited, false);
        dist[src] = 0;

        for (int i = 0; i < V - 1; i++) {
            double minDist = INF;
            int minIndex = -1;

            for (int v = 0; v < V; v++) {

                if (!visited[v] && dist[v] <= minDist) {
                    minDist = dist[v];
                    minIndex = v;
                }
            }

            visited[minIndex] = true;

            for (int v = 0; v < V; v++) {
                if (!visited[v] && graph[minIndex][v] != 0 && dist[minIndex] != INF &&
                        dist[minIndex] + graph[minIndex][v] < dist[v]) {
                    cities.get(v).before = cities.get(minIndex).cityName;
                    dist[v] = dist[minIndex] + graph[minIndex][v];
                }
            }
        }
        printSolution(dist, cities, dest);
    }

    /**
     * printing the output depends on inputs and calling std function in order to implement all codes onto canva.
     * @param dist list of
     * @param cities Arraylist of City objects
     * @param dest to declare destination city.
     */
    private static void printSolution(double[] dist, ArrayList<City> cities, String dest) {
        ArrayList<String> path = new ArrayList<>();
        for (int i = 0; i < dist.length; i++) {
            if (dist[i] == INF) {
            } else {
                Double distance;
                int index = findIndex(cities, dest);
                path = new ArrayList<>();
                City city = cities.get(index);
                while (true) {
                    path.add(city.cityName);
                    if (Objects.equals(city.before, "")) {
                        break;
                    }
                    city = cities.get(findIndex(cities, cities.get(findIndex(cities, city.cityName)).before));
                }
                distance = dist[index];
                if (distance == INF) {
                    System.out.print("No path could be found");
                    System.exit(0);
                    break;
                }
                String out = String.format(Locale.US, "Total distance: %.2f. Path: ", distance);
                System.out.printf(out);
                System.out.print(path.get(path.size() - 1));
                for (int j = path.size() - 2; j >= 0; j--) {
                    System.out.print(" -> " + path.get(j));
                }
                System.out.print("\n");

                break;
            }

        }
        std(path, cities, MusaKaanGuney.connectionsList);
    }

    /**
     * Drawing all cities onto canva and connecting them by using lines.
     * Showing and coloring a path.
     * @param path cities which in path
     * @param cities Arraylist of City objects
     * @param connections Arraylist of connection between two cities.
     */
    private static void std(ArrayList<String> path, ArrayList<City> cities, ArrayList<Connections> connections) {
        StdDraw.setCanvasSize(2377 / 2, 1055 / 2);
        StdDraw.setXscale(0, 2377);
        StdDraw.setYscale(0, 1055);
        StdDraw.picture(2377.0 / 2, 1055.0 / 2, "map.png", 2377, 1055);
        for (City city : cities) {
            StdDraw.setPenRadius(0.01);
            StdDraw.setPenColor(StdDraw.GRAY);
            StdDraw.point(city.xCoordinate, city.yCoordinate);
            StdDraw.setFont(new Font("Serif", Font.PLAIN, 12));
            StdDraw.text(city.xCoordinate, city.yCoordinate + 30, city.cityName);
        }
        for (Connections conn : connections) {
            StdDraw.setPenRadius(0.002);
            StdDraw.setPenColor(StdDraw.GRAY);
            City city1 = cities.get(findIndex(cities, conn.connection1));
            City city2 = cities.get(findIndex(cities, conn.connection2));
            StdDraw.line(city1.xCoordinate, city1.yCoordinate, city2.xCoordinate, city2.yCoordinate);
        }
        if(path.size() == 1){
            StdDraw.setPenRadius(0.01);
            StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
            City city1 = cities.get(findIndex(cities, path.get(0)));
            StdDraw.point(city1.xCoordinate,city1.yCoordinate);
            StdDraw.text(city1.xCoordinate, city1.yCoordinate + 30, city1.cityName);
        }
        for (int i = 0; i < path.size() - 1; i++) {
            StdDraw.setPenRadius(0.01);
            StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
            City city1 = cities.get(findIndex(cities, path.get(i)));
            City city2 = cities.get(findIndex(cities, path.get(i+1)));
            StdDraw.line(city1.xCoordinate, city1.yCoordinate, city2.xCoordinate, city2.yCoordinate);
            StdDraw.point(city1.xCoordinate,city1.yCoordinate);
            StdDraw.text(city1.xCoordinate, city1.yCoordinate + 30, city1.cityName);
            if(i == path.size() - 2){
                StdDraw.text(city2.xCoordinate, city2.yCoordinate + 30, city2.cityName);
                StdDraw.point(city2.xCoordinate,city2.yCoordinate);
            }
        }
    }
}