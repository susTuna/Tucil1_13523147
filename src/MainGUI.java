import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainGUI extends Application {
    private TextArea boardDisplay;
    private Board board;
    private List<Piece> pieces;
    private Canvas canvas;
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Puzzle Solver");

        BorderPane root = new BorderPane();
        canvas = new Canvas(600, 400);
        boardDisplay = new TextArea();
        boardDisplay.setEditable(false);
        boardDisplay.setStyle("-fx-control-inner-background: #222; -fx-text-fill: white; -fx-font-family: 'monospace';");
        root.setCenter(canvas);
        root.setBottom(boardDisplay);

        Button loadFileButton = new Button("Load Puzzle File");
        loadFileButton.setOnAction(e -> loadPuzzleFile(primaryStage));
        loadFileButton.setStyle("-fx-background-color: #555; -fx-text-fill: white;");
        root.setTop(loadFileButton);

        Scene scene = new Scene(root, 600, 500, Color.web("#111"));
        primaryStage.setScene(scene);
        primaryStage.show();

        // Set canvas background to dark
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.web("#111"));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void loadPuzzleFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Puzzle File");
        File file = fileChooser.showOpenDialog(stage);
        
        if (file != null) {
            try (Scanner scanner = new Scanner(file)) {
                int height = scanner.nextInt();
                int width = scanner.nextInt();
                int P = scanner.nextInt();
                scanner.nextLine(); // Skip line
                scanner.nextLine(); // Skip line

                board = new Board();
                board.genBoard(height, width);
                
                List<String> inputLines = new ArrayList<>();
                while (scanner.hasNextLine()) {
                    inputLines.add(scanner.nextLine());
                }
                
                pieces = Puzzle.parsePieces(inputLines);
                if (!Puzzle.isValid(pieces, P)) return;

                solvePuzzle();
            } catch (FileNotFoundException e) {
                boardDisplay.setText("File not found!");
            }
        }
    }

    private void solvePuzzle() {
        BruteForceAlgorithm solver = new BruteForceAlgorithm(board, pieces);
        long startTime = System.nanoTime();
        boolean solutionFound = solver.solve(0);
        long endTime = System.nanoTime();
        double elapsedTimeMs = (endTime - startTime) / 1_000_000.0;

        if (solutionFound && board.isSolutionValid()) {
            boardDisplay.setText("Solution found.");
            drawBoard();
        } else {
            boardDisplay.setText("No solution found.");
            drawNoSolutionBoard();
        }
        boardDisplay.appendText("\nTotal possibilities visited: " + solver.visited);
        boardDisplay.appendText("\nTime taken: " + elapsedTimeMs + " ms");
    }

    private String formatBoard() {
        StringBuilder sb = new StringBuilder();
        for (char[] row : board.getGrid()) {
            for (char cell : row) {
                sb.append(cell).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private void drawBoard() {
        int cellSize = 40;
        int width = board.getWidth();
        int height = board.getHeight();

        canvas.setWidth(width * cellSize);
        canvas.setHeight(height * cellSize);
        
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.web("#111")); // Dark background for canvas
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        
        char[][] grid = board.getGrid();

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                char piece = grid[row][col];
                gc.setFill(getPieceColor(piece));
                gc.fillRect(col * cellSize, row * cellSize, cellSize, cellSize);
                
                gc.setStroke(Color.web("#444"));
                gc.strokeRect(col * cellSize, row * cellSize, cellSize, cellSize);
                
                gc.setFill(Color.WHITE);
                gc.setFont(javafx.scene.text.Font.font("Arial", 16));
                gc.fillText(String.valueOf(piece), col * cellSize + cellSize / 3.5, row * cellSize + cellSize / 1.7);
            }
        }
    }

    private void drawNoSolutionBoard() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.web("#111")); // Dark background
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.RED);
        gc.setFont(javafx.scene.text.Font.font("Arial", 50));
        gc.fillText("X", canvas.getWidth() / 2 - 20, canvas.getHeight() / 2);
    }

    private Color getPieceColor(char piece) {
        int hash = (piece * 37) % 360;
        return Color.hsb(hash, 0.6, 0.7);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
