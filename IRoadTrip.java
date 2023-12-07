import java.util.List; 
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Set;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Collections;
import java.util.Map;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.TreeMap;

public class IRoadTrip {

    private HashMap<String, HashMap<String, Integer>> bordersMap;  //Country Name <Neigbor, Int> Int is the distance
    private TreeMap<String, String> countryIdMap; //Country Name, Country ID
    private HashMap<String, HashMap<String, Integer>> distancesMap; //CountryID <NeighborId, distance>

    public IRoadTrip (String [] args) {
        if (args.length != 3) {
            System.err.println("Use:java IRoadTrip borders.txt capdist.csv state_name.tsv");
            System.exit(1);
        }

        bordersMap = new HashMap<>(); //Bordersmap is what represents my graph
        countryIdMap = new TreeMap<>();
        distancesMap = new HashMap<>();

        readCountryIdFile(args[2]);
        readCapDistFile(args[1]);
        readBordersFile(args[0]);
        //buildGraph();

        //System.out.println(bordersMap);
        //System.out.println(countryIdMap);
        //System.out.println(distancesMap);
    }


    public int getDistance(String country1, String country2) { //used to set distances inside graph i.e. inside the bordersMap 
    
            // Check if both countries exist in the countryIdMap
            String countryCode1 = countryIdMap.get(country1);
            String countryCode2 = countryIdMap.get(country2);

            if (countryCode1 == null || countryCode2 == null) {
                //System.out.println("Invalid country names. Please enter valid country names.");
                return -1; 
            }
        
            // Retrieve distances using country IDs
            Integer distance = distancesMap.getOrDefault(countryCode1, new HashMap<>()).get(countryCode2);
        
            if (distance == null) {
                //System.out.println("No distance information available between " + country1 + " and " + country2);
                return -1; 
            }
        
            return distance;
        }
        
    
    public List<String> findPath (String country1, String country2) {
        // Replace with your code
            // Check if both countries exist in the countryIdMap
            String countryCode1 = countryIdMap.get(country1);
            String countryCode2 = countryIdMap.get(country2);
    
            if (countryCode1 == null || countryCode2 == null) {
                System.out.println("Invalid country names. Please enter valid country names.");
                return Collections.emptyList();
            }
    
            // Run Dijkstra's algorithm to find the shortest path
            Map<String, Integer> distanceMap = new HashMap<>();
            Map<String, String> parentMap = new HashMap<>();
            Set<String> visited = new HashSet<>();
    
            PriorityQueue<String> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(distanceMap::get));
            priorityQueue.add(countryCode1);
            distanceMap.put(countryCode1, 0);
    
            while (!priorityQueue.isEmpty()) {
                String currentCountry = priorityQueue.poll();
                if (visited.contains(currentCountry)) continue;
    
                visited.add(currentCountry);
    
                for (Map.Entry<String, Integer> neighborEntry : distancesMap.getOrDefault(currentCountry, new HashMap<>()).entrySet()) {
                    String neighbor = neighborEntry.getKey();
                    int newDistance = distanceMap.get(currentCountry) + neighborEntry.getValue();
    
                    if (!distanceMap.containsKey(neighbor) || newDistance < distanceMap.get(neighbor)) {
                        distanceMap.put(neighbor, newDistance);
                        parentMap.put(neighbor, currentCountry);
                        priorityQueue.add(neighbor);
                    }
                }
            }
    
            // Reconstruct the path
            List<String> path = new ArrayList<>();
            String current = countryCode2;
            while (current != null) {
                final String finalCurrent = current;
                String countryName = countryIdMap.entrySet().stream()
                .filter(entry -> entry.getValue().equals(finalCurrent))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);

            if (countryName != null) {
            path.add(countryName);
        }

            current = parentMap.get(current);
            }
            
            Collections.reverse(path);
            return path;
            //return null;
    }


    public void acceptUserInput() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter the name of the first country (type EXIT to quit): ");
            String input1 = scanner.nextLine().trim();
    
            if (input1.equalsIgnoreCase("EXIT")) {
                break; // Exit the loop if the user enters "EXIT"
            }
    
            String countryCode1 = countryIdMap.get(input1);
    
            if (countryCode1 == null) {
                System.out.println("Invalid country name. Please enter a valid country name.");
                continue; // Restart the loop if the country name is invalid
            }
    
            System.out.print("Enter the name of the second country (type EXIT to quit): ");
            String input2 = scanner.nextLine().trim();
            String countryCode2 = countryIdMap.get(input2);
    
            if (countryCode2 == null) {
                System.out.println("Invalid country name. Please enter a valid country name.");
                continue; // Restart the loop if the country name is invalid
            }
    
            // Call findPath and print the result
            List<String> path = findPath(countryCode1, countryCode2);
    
            if (path.isEmpty()) {
                System.out.println("No path found between " + input1 + " and " + input2);
            } else {
                System.out.println("Route from " + input1 + " to " + input2 + ":");
                for (int i = 0; i < path.size() - 1; i++) {
                    String currentCountry = path.get(i);
                    String nextCountry = path.get(i + 1);
                    int distance = getDistance(currentCountry, nextCountry);
                    System.out.println("* " + currentCountry + " --> " + nextCountry + " (" + distance + " km.)");
                }
            }
        }
        scanner.close();
    }
    
    

    public static void main(String[] args) {
        IRoadTrip a3 = new IRoadTrip(args);
        a3.acceptUserInput();
    }

//NEW METHODS
private void readBordersFile(String filePath) {
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("=");
            String country = parts[0].trim();
            HashMap<String, Integer> neighbors = new HashMap<>();

            if (parts.length > 1) { // if there is info after the "=" sign
                String[] borderInfo = parts[1].split(";");
                for (String info : borderInfo) {
                    String[] neighborParts = info.trim().split(" ");
                    String neighbor = neighborParts[0].trim();
                    int distance = getDistance(country, neighbor);
                    neighbors.put(neighbor, distance);
                }
            }
            bordersMap.put(country, neighbors);
        }
    } catch (IOException e) {
        System.exit(1);
    }
}


private void readCountryIdFile(String filePath) {
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\t"); // split at tabs because it's a tsv file
            String countryName = parts[2].trim();
            String countryCode = parts[1].trim();
            String endDate = parts[4].trim();

            if (endDate.equals("2020-12-31")) { // only uses countries that are valid/still exist
                countryIdMap.put(countryName, countryCode);
            }
        }
    } catch (IOException e) {
        System.exit(1);
    }
}

private void readCapDistFile(String filePath) {
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
        String line;
        boolean isFirstLine = true; // Added variable to track the first line
        while ((line = reader.readLine()) != null) {
            if (isFirstLine) {
                isFirstLine = false;
                continue; // Skip the first line
            }

            String[] parts = line.split(",");
            String countryIdA = parts[1].trim();
            String countryIdB = parts[3].trim();
            try {
                int distance = Integer.parseInt(parts[4].trim());

                // Update distancesMap with distance information
                distancesMap.computeIfAbsent(countryIdA, k -> new HashMap<>()).put(countryIdB, distance);
                distancesMap.computeIfAbsent(countryIdB, k -> new HashMap<>()).put(countryIdA, distance);
            } catch (NumberFormatException e) {
                // Print lines with invalid distance values
                System.out.println("Invalid distance value on line: " + line);
            }
        }
    } catch (IOException e) {
        System.exit(1);
    }
}

}