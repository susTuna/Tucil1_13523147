import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShapeStorage {
    public static void main(String[] args) {
        // Check if a file path is provided as an argument
        if (args.length != 1) {
            System.out.println("Usage: java ShapeStorage <input-file>");
            return;
        }

        // File path from command line argument
        String filePath = args[0];

        // Create a map to store the shapes by letter
        Map<Character, List<String>> shapes = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()) {  // Skip empty lines
                    char letter = line.charAt(0); // Get the first character of the string
                    shapes.putIfAbsent(letter, new ArrayList<>()); // If the letter isn't in the map, add it with an empty list
                    shapes.get(letter).add(line); // Add the string to the list corresponding to the letter
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Print the stored shapes
        for (Map.Entry<Character, List<String>> entry : shapes.entrySet()) {
            System.out.println("Letter: " + entry.getKey());
            for (String shape : entry.getValue()) {
                System.out.println("  " + shape);
            }
        }
    }
}
