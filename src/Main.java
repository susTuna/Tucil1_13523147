import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToolBar;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class Main extends Application {
    private TextArea boardDisplay;
    private Board board;
    private List<Piece> pieces;
    private Canvas canvas;
    private static File lastDirectory = null;
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("IQ Puzzle Pro Solver");

        BorderPane root = new BorderPane();
        canvas = new Canvas(640, 340);
        canvas.setVisible(false);
        
        boardDisplay = new TextArea();
        boardDisplay.setEditable(false);
        boardDisplay.setStyle("-fx-control-inner-background: white; -fx-text-fill: black; -fx-font-family: 'Arial';");
        boardDisplay.setPrefHeight(100);
        boardDisplay.setMaxHeight(100);
        boardDisplay.setMinHeight(100);
        root.setCenter(canvas);
        root.setBottom(boardDisplay);

        Button loadFileButton = new Button("Load Puzzle File");
        Button saveButton = new Button("Export Solution");

        loadFileButton.setOnAction(e -> loadPuzzleFile(primaryStage));
        saveButton.setOnAction(e -> saveFile(primaryStage, canvas));
        loadFileButton.setStyle("-fx-text-fill: black; -fx-font-family: 'Arial';");
        saveButton.setStyle("-fx-text-fill: black; -fx-font-family: 'Arial';");

        ToolBar toolBar = new ToolBar(loadFileButton, saveButton);
        root.setTop(toolBar);

        Scene scene = new Scene(root, 640, 480, Color.WHITE);
        primaryStage.setScene(scene);
        primaryStage.show();

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }


    private void loadPuzzleFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Puzzle File");

        if (lastDirectory != null && lastDirectory.exists()) {
            fileChooser.setInitialDirectory(lastDirectory);
        }

        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        File file = fileChooser.showOpenDialog(stage);
        
        if (file != null) {
            clearBoard();
            lastDirectory = file.getParentFile();
            try (Scanner scanner = new Scanner(file)) {
                int height = scanner.nextInt();
                int width = scanner.nextInt();
                int P = scanner.nextInt();
                scanner.nextLine();
                scanner.nextLine();

                board = new Board();
                board.genBoard(height, width);
                
                List<String> inputLines = new ArrayList<>();
                while (scanner.hasNextLine()) {
                    inputLines.add(scanner.nextLine());
                }
                
                pieces = Puzzle.parsePieces(inputLines);
                if (!Puzzle.isValid(pieces, P)) {
                    boardDisplay.setText(Puzzle.errorMsg(pieces, P));
                    return;
                }
                solvePuzzle();
            } catch (FileNotFoundException e) {
                boardDisplay.setText("File not found!");
            }
        }
    }

    private void saveFile(Stage stage, Canvas canvas) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Solution File");

        if (lastDirectory != null && lastDirectory.exists()) {
            fileChooser.setInitialDirectory(lastDirectory);
        }

        FileChooser.ExtensionFilter pngFilter = new FileChooser.ExtensionFilter("PNG Image (*.png)", "*.png");
        FileChooser.ExtensionFilter txtFilter = new FileChooser.ExtensionFilter("Text File (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().addAll(pngFilter, txtFilter);

        File file = fileChooser.showSaveDialog(stage);
        
        if (file != null) {
            lastDirectory = file.getParentFile();
            String extension = getFileExtension(file);

            if ("png".equalsIgnoreCase(extension)) {
                saveToPng(canvas, file);
            }
            else if ("txt".equalsIgnoreCase(extension)) {
                saveToText(board, file);
            }
            else {
                showError("Unsupported file type! Please choose PNG or TXT.");
            }
        }
    }

    private void saveToPng(Canvas canvas, File file) {
        WritableImage image = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
        canvas.snapshot(null, image);
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void saveToText(Board board, File file) {
        String content = formatBoard(board);
        try {
            Files.write(Paths.get(file.toURI()), content.getBytes());
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }

    private String getFileExtension(File file) {
        String name = file.getName();
        int lastDot = name.lastIndexOf('.');
        return (lastDot > 0) ? name.substring(lastDot + 1) : "";
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void solvePuzzle() {
        BruteForceAlgorithm solver = new BruteForceAlgorithm(board, pieces);
        long startTime = System.currentTimeMillis();
        boolean solutionFound = solver.solve(0);
        long endTime = System.currentTimeMillis();
        long elapsedTimeMs = (endTime - startTime);

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

    private String formatBoard(Board board) {
        StringBuilder sb = new StringBuilder();
        for (char[] row : board.getGrid()) {
            for (char cell : row) {
                sb.append(cell).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private void clearBoard(){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void drawBoard() {
        canvas.setVisible(true);
        int cellSize = 40;
        int width = board.getWidth();
        int height = board.getHeight();

        canvas.setWidth(width * cellSize);
        canvas.setHeight(height * cellSize);
        
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        
        char[][] grid = board.getGrid();

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                char piece = grid[row][col];
                gc.setFill(getPieceColor(piece));
                gc.fillRect(col * cellSize, row * cellSize, cellSize, cellSize);
                
                gc.setStroke(Color.BLACK);
                gc.strokeRect(col * cellSize, row * cellSize, cellSize, cellSize);
                
                gc.setFill(Color.BLACK);
                gc.setFont(javafx.scene.text.Font.font("Arial", 16));
                gc.fillText(String.valueOf(piece), col * cellSize + cellSize / 3.5, row * cellSize + cellSize / 1.7);
            }
        }
    }

    private void drawNoSolutionBoard() {
        canvas.setVisible(true);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.RED);
        gc.setFont(javafx.scene.text.Font.font("Arial", 72));
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
