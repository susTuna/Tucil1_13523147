import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Board board = new Board();
        int P;

        try (Scanner sc = new Scanner(System.in)) {
            System.out.print("Enter file name: ");
            String fileName = "./test/" + sc.nextLine(); // test filepath
            List<String> inputLines = new ArrayList<>();

            try (Scanner filesc = new Scanner(new File(fileName))) {
                board.genBoard(filesc .nextInt(), filesc.nextInt());
                P = filesc.nextInt();
                filesc.nextLine(); //skips next line
                String S = filesc.nextLine();
                while (filesc.hasNextLine()) {
                    inputLines.add(filesc.nextLine());
                }
            } catch (FileNotFoundException e) {
                System.err.println("File not found: " + e.getMessage());
                return;
            }

            List<Piece> pieces = Puzzle.parsePieces(inputLines);
            if(!Puzzle.isValid(pieces,P)) return;
            BruteForceAlgorithm solver = new BruteForceAlgorithm(board, pieces);

            long startTime = System.nanoTime();
            
            boolean solutionFound = solver.solve(0);

            long endTime = System.nanoTime();
            double elapsedTimeMs = (endTime - startTime) / 1_000_000.0; // Convert to milliseconds

            if (solutionFound && board.isSolutionValid()) {
                System.out.println("Solution found:");
                solver.printSolution();
            } else {
                
                System.out.println("No solution found.");
            }

            System.out.println("Total possibilities visited: " + solver.visited);
            System.out.printf("Time taken: %.3f ms\n", elapsedTimeMs);
        }
    }
}