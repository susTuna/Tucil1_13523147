import java.util.*;
import java.io.*;

class Piece {
    private char id;
    private List<List<Character>> shape;

    public Piece(char id, List<String> shapeLines) {
        this.id = id;
        this.shape = new ArrayList<>();

        for (String line : shapeLines) {
            List<Character> row = new ArrayList<>();
            for (char c : line.toCharArray()) {
                row.add(c);
            }
            this.shape.add(row); 
        }
    }

    public char getId() {
        return id;
    }

    public char[][] getShapeArray() {
        int rows = shape.size();
        int cols = 0;

        for (List<Character> row : shape) {
            cols = Math.max(cols, row.size());
        }

        char[][] shapeArray = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            List<Character> row = shape.get(i);
            for (int j = 0; j < cols; j++) {
                shapeArray[i][j] = (j < row.size()) ? row.get(j) : ' ';
            }
        }
        return shapeArray;
    }

    public Piece rotate90() {
        char[][] original = getShapeArray();
        int rows = original.length;
        int cols = original[0].length;
        char[][] rotated = new char[cols][rows];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                rotated[j][rows - 1 - i] = original[i][j];
            }
        }

        List<String> newShape = new ArrayList<>();
        for (char[] row : rotated) {
            newShape.add(new String(row));
        }
        return new Piece(this.id, newShape);
    }

    public Piece flipVertical() {
        char[][] original = getShapeArray();
        int rows = original.length;
        int cols = original[0].length;
        char[][] flipped = new char[rows][cols];

        for (int i = 0; i < rows; i++) {
            flipped[rows - 1 - i] = original[i];
        }

        List<String> newShape = new ArrayList<>();
        for (char[] row : flipped) {
            newShape.add(new String(row));
        }
        return new Piece(this.id, newShape);
    }

    public void printPiece() {
        System.out.println("Piece ID: " + id);
        char[][] shapeArray = getShapeArray();
        for (char[] row : shapeArray) {
            System.out.println(new String(row));
        }
        System.out.println();
    }
}

public class Puzzle {
    public static List<Piece> parsePieces(List<String> inputLines) {
    List<Piece> pieces = new ArrayList<>();
    Map<Character, List<String>> pieceMap = new LinkedHashMap<>();

    Character currentPieceId = null;

    for (String line : inputLines) {
        if (line.isBlank()) {
            if (currentPieceId != null) {
                pieceMap.get(currentPieceId).add(line);
            }
            continue;
        }

        for (char c : line.toCharArray()) {
            if (c != ' ') {
                currentPieceId = c;
                break;
            }
        }

        if (currentPieceId != null) {
            pieceMap.putIfAbsent(currentPieceId, new ArrayList<>());
            pieceMap.get(currentPieceId).add(line);
        }
    }

    for (Map.Entry<Character, List<String>> entry : pieceMap.entrySet()) {
        pieces.add(new Piece(entry.getKey(), entry.getValue()));
    }
    return pieces;
}

    public static boolean isValid(List<Piece> inputpiece, int P){
        int size = inputpiece.size();
        if (size == P) {
            return true;
        }
        return false;
    }

    public static String errorMsg(List<Piece> inputpiece, int P) {
        String err;
        int size = inputpiece.size();
        if (size < P){
            err = P + " pieces are required but only " + size + " is given.";
        }
        else{
            err = "Pieces out of bounds. Only " + P + " pieces required but " + size + " is given.";
        }
        return err;
    }

    public static void main(String[] args) {
        String fileName = "./test/shape.txt"; // File path
        List<String> inputLines = new ArrayList<>();

        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNextLine()) {
                inputLines.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
            return;
        }

        List<Piece> pieces = parsePieces(inputLines);

        for (Piece piece : pieces) {
            piece.printPiece();
            System.out.println("Rotated 90°:");
            piece.rotate90().printPiece();
            System.out.println("Rotated 180°:");
            piece.rotate90().rotate90().printPiece();
            System.out.println("Rotated 270°:");
            piece.rotate90().rotate90().rotate90().printPiece();
        }
    }
}
