// import java.io.*;
// import java.util.*;

// public class IQPuzzlerSolver {
//     private int boardWidth;
//     private int boardHeight;
//     private int puzzleCount;
//     private String caseType;
//     private List<String> puzzleShapes = new ArrayList<>();
//     private char[][] board;
//     private long iterations = 0;
    
//     public void readTestCase(String filename) throws IOException {
//         try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
//             String[] dimensions = br.readLine().split(" ");
//             boardHeight = Integer.parseInt(dimensions[0]);
//             boardWidth = Integer.parseInt(dimensions[1]);
//             puzzleCount = Integer.parseInt(dimensions[2]);
//             caseType = br.readLine().trim();
            
//             for (int i = 0; i < puzzleCount; i++) {
//                 puzzleShapes.add(br.readLine().trim());
//             }
//         }
//     }
    
//     public void solve() {
//         board = new char[boardHeight][boardWidth];
//         for (char[] row : board) Arrays.fill(row, '.');
        
//         long startTime = System.currentTimeMillis();
//         boolean solved = bruteForceSolve();
//         long endTime = System.currentTimeMillis();
        
//         if (solved) {
//             printBoard();
//             System.out.println("Waktu pencarian: " + (endTime - startTime) + " ms");
//             System.out.println("Banyak kasus yang ditinjau: " + iterations);
//         } else {
//             System.out.println("Puzzle tidak memiliki solusi.");
//         }
        
//         promptSaveSolution();
//     }
    
//     private boolean bruteForceSolve() {
//         List<int[]> positions = new ArrayList<>();
//         for (int i = 0; i < boardHeight; i++) {
//             for (int j = 0; j < boardWidth; j++) {
//                 positions.add(new int[]{i, j});
//             }
//         }
        
//         List<String> permutations = generatePermutations(puzzleShapes);
//         for (String perm : permutations) {
//             iterations++;
//             resetBoard();
//             if (tryPlacement(perm, positions)) return true;
//         }
//         return false;
//     }
    
//     private List<String> generatePermutations(List<String> elements) {
//         List<String> results = new ArrayList<>();
//         permute(elements, 0, results);
//         return results;
//     }
    
//     private void permute(List<String> arr, int k, List<String> results) {
//         if (k == arr.size()) {
//             results.add(String.join("", arr));
//         } else {
//             for (int i = k; i < arr.size(); i++) {
//                 Collections.swap(arr, i, k);
//                 permute(arr, k + 1, results);
//                 Collections.swap(arr, i, k);
//             }
//         }
//     }
    
//     private boolean tryPlacement(String perm, List<int[]> positions) {
//         int index = 0;
//         for (String shape : perm.split("")) {
//             if (index >= positions.size()) return false;
//             int[] pos = positions.get(index);
//             if (!canPlace(shape, pos[0], pos[1])) return false;
//             placePiece(shape, pos[0], pos[1], shape.charAt(0));
//             index++;
//         }
//         return true;
//     }
    
//     private void resetBoard() {
//         for (char[] row : board) Arrays.fill(row, '.');
//     }
    
//     private boolean canPlace(String shape, int row, int col) {
//         for (int i = 0; i < shape.length(); i++) {
//             int r = row + i;
//             if (r >= boardHeight || board[r][col] != '.') return false;
//         }
//         return true;
//     }
    
//     private void placePiece(String shape, int row, int col, char piece) {
//         for (int i = 0; i < shape.length(); i++) {
//             board[row + i][col] = piece;
//         }
//     }
    
//     private void printBoard() {
//         for (char[] row : board) {
//             for (char cell : row) {
//                 System.out.print(cell + " ");
//             }
//             System.out.println();
//         }
//     }
    
//     private void promptSaveSolution() {
//         Scanner scanner = new Scanner(System.in);
//         System.out.print("Apakah anda ingin menyimpan solusi? (ya/tidak): ");
//         String response = scanner.nextLine().trim().toLowerCase();
//         if (response.equals("ya")) {
//             try (BufferedWriter writer = new BufferedWriter(new FileWriter("solution.txt"))) {
//                 for (char[] row : board) {
//                     writer.write(new String(row));
//                     writer.newLine();
//                 }
//                 System.out.println("Solusi disimpan dalam solution.txt");
//             } catch (IOException e) {
//                 System.err.println("Gagal menyimpan solusi.");
//             }
//         }
//     }
    
//     public static void main(String[] args) throws IOException {
//         Scanner scanner = new Scanner(System.in);
//         System.out.print("Masukkan nama file test case: ");
//         String filename = scanner.nextLine().trim();
        
//         IQPuzzlerSolver solver = new IQPuzzlerSolver();
//         solver.readTestCase(filename);
//         solver.solve();
//     }
// }

import java.io.*;
import java.util.*;

public class IQPuzzlerSolver {
    private int boardWidth;
    private int boardHeight;
    private int puzzleCount;
    private String caseType;
    private List<char[][]> puzzleShapes = new ArrayList<>();
    private char[][] board;
    private long iterations = 0;
    
    public void readTestCase(String filename) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String[] dimensions = br.readLine().split(" ");
            boardHeight = Integer.parseInt(dimensions[0]);
            boardWidth = Integer.parseInt(dimensions[1]);
            puzzleCount = Integer.parseInt(dimensions[2]);
            caseType = br.readLine().trim();
            
            for (int i = 0; i < puzzleCount; i++) {
                List<String> shapeLines = new ArrayList<>();
                String line;
                while ((line = br.readLine()) != null && !line.isEmpty()) {
                    shapeLines.add(line);
                }
                puzzleShapes.add(convertToMatrix(shapeLines));
            }
        }
    }
    
    private char[][] convertToMatrix(List<String> shapeLines) {
        int rows = shapeLines.size();
        int cols = shapeLines.get(0).length();
        char[][] shape = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            shape[i] = shapeLines.get(i).toCharArray();
        }
        return shape;
    }
    
    public void solve() {
        board = new char[boardHeight][boardWidth];
        for (char[] row : board) Arrays.fill(row, '.');
        
        long startTime = System.currentTimeMillis();
        boolean solved = bruteForceSolve();
        long endTime = System.currentTimeMillis();
        
        if (solved) {
            printBoard();
            System.out.println("Waktu pencarian: " + (endTime - startTime) + " ms");
            System.out.println("Banyak kasus yang ditinjau: " + iterations);
        } else {
            System.out.println("Puzzle tidak memiliki solusi.");
        }
        
        promptSaveSolution();
    }
    
    private boolean bruteForceSolve() {
        List<int[]> positions = new ArrayList<>();
        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                positions.add(new int[]{i, j});
            }
        }
        
        List<List<char[][]>> permutations = generatePermutations(puzzleShapes);
        for (List<char[][]> perm : permutations) {
            iterations++;
            resetBoard();
            if (tryPlacement(perm, positions)) return true;
        }
        return false;
    }
    
    private List<List<char[][]>> generatePermutations(List<char[][]> elements) {
        List<List<char[][]>> results = new ArrayList<>();
        permute(elements, 0, results);
        return results;
    }
    
    private void permute(List<char[][]> arr, int k, List<List<char[][]>> results) {
        if (k == arr.size()) {
            results.add(new ArrayList<>(arr));
        } else {
            for (int i = k; i < arr.size(); i++) {
                Collections.swap(arr, i, k);
                permute(arr, k + 1, results);
                Collections.swap(arr, i, k);
            }
        }
    }
    
    private boolean tryPlacement(List<char[][]> perm, List<int[]> positions) {
        for (int[] pos : positions) {
            for (char[][] shape : perm) {
                if (canPlace(shape, pos[0], pos[1])) {
                    placePiece(shape, pos[0], pos[1]);
                    return true;
                }
            }
        }
        return false;
    }
    
    private void resetBoard() {
        for (char[] row : board) Arrays.fill(row, '.');
    }
    
    private boolean canPlace(char[][] shape, int row, int col) {
        int rows = shape.length;
        int cols = shape[0].length;
        if (row + rows > boardHeight || col + cols > boardWidth) return false;
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (shape[i][j] != '.' && board[row + i][col + j] != '.') return false;
            }
        }
        return true;
    }
    
    private void placePiece(char[][] shape, int row, int col) {
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] != '.') {
                    board[row + i][col + j] = shape[i][j];
                }
            }
        }
    }
    
    private void printBoard() {
        for (char[] row : board) {
            for (char cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }
    
    private void promptSaveSolution() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Apakah anda ingin menyimpan solusi? (ya/tidak): ");
        String response = scanner.nextLine().trim().toLowerCase();
        if (response.equals("ya")) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("solution.txt"))) {
                for (char[] row : board) {
                    writer.write(new String(row));
                    writer.newLine();
                }
                System.out.println("Solusi disimpan dalam solution.txt");
            } catch (IOException e) {
                System.err.println("Gagal menyimpan solusi.");
            }
        }
    }
    
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Masukkan nama file test case: ");
        String filename = scanner.nextLine().trim();
        
        IQPuzzlerSolver solver = new IQPuzzlerSolver();
        solver.readTestCase(filename);
        solver.solve();
    }
}
