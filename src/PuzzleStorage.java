import java.io.*;
import java.util.*;

class PuzzlePiece {
    char id;
    List<String> shape;

    public PuzzlePiece(char id, List<String> shape) {
        this.id = id;
        this.shape = shape;
    }

    public void printPiece() {
        System.out.println("Piece " + id + ":");
        for (String row : shape) {
            System.out.println(row);
        }
        System.out.println();
    }
}

public class PuzzleStorage {
    private int boardWidth;
    private int boardHeight;
    private int puzzleCount;
    private String caseType;
    private List<PuzzlePiece> puzzlePieces = new ArrayList<>();

    public void readFromFile(String filename) throws IOException {
        try (Scanner sc = new Scanner(new File(filename))) {
            // Read board dimensions and case type
            boardHeight = sc.nextInt();
            boardWidth = sc.nextInt();
            puzzleCount = sc.nextInt();
            sc.nextLine(); //pass line
            caseType = sc.nextLine();
            // System.out.println(boardHeight + boardWidth + puzzleCount);
            // System.out.println(caseType);

            String line;
            while(sc.hasNextLine()){
                line = sc.nextLine();
                System.out.println(line);
            }
    //         while ((line = sc.nextLine()) != null) {
    //             line = line.trim();
    //             if (line.isEmpty()) continue; // Skip empty lines

    //             // New puzzle piece starts with a single letter
    //             if (line.length() == 1 && Character.isLetter(line.charAt(0))) {
    //                 char pieceId = line.charAt(0);
    //                 List<String> shape = new ArrayList<>();

    //                 // Read shape until next letter or end of file
    //                 while ((line = br.readLine()) != null && 
    //                        (line.isEmpty() || !Character.isLetter(line.trim().charAt(0)) || line.trim().length() > 1)) {
    //                     shape.add(line.trim());
    //                 }

    //                 // Store the piece
    //                 puzzlePieces.add(new PuzzlePiece(pieceId, shape));

    //                 // If the next line is a letter, go back one line to process it
    //                 if (line != null && !line.trim().isEmpty()) {
    //                     br.mark(1000); // Mark current position
    //                     br.reset();    // Reset to process the letter in next loop
    //                 }
    //             }
    //         }
        }
    }


    public void printAllPieces() {
        System.out.println("Board: " + boardWidth + "x" + boardHeight);
        System.out.println("Case Type: " + caseType);
        System.out.println("Puzzle Pieces:");
        for (PuzzlePiece piece : puzzlePieces) {
            piece.printPiece();
        }
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Masukkan nama file test case: ");
        String filename = scan.nextLine().trim();

        PuzzleStorage storage = new PuzzleStorage();
        try {
            storage.readFromFile(filename);
            //storage.printAllPieces();
        } catch (IOException e) {
            System.err.println("Error membaca file: " + e.getMessage());
        }
    }
}