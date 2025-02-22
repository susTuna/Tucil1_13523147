import java.util.*;

public class BruteForceAlgorithm {
    private Board board;
    private List<Piece> pieces;
    public long visited = 0; // Counter

    public BruteForceAlgorithm(Board board, List<Piece> pieces) {
        this.board = board;
        this.pieces = pieces;
    }

    public boolean solve(int pieceIndex) {
        if (pieceIndex >= pieces.size()) {
            return true; // Ini bisa
        }

        Piece piece = pieces.get(pieceIndex);

        // Flip vertical
        for (int flip = 0; flip < 2; flip++){
            // Rotasi piece (0°, 90°, 180°, 270°)
            for (int rotation = 0; rotation < 4; rotation++) {
                char[][] shape = piece.getShapeArray();

                // Nyoba masang ya ges ya
                for (int row = 0; row < board.getHeight(); row++) {
                    for (int col = 0; col < board.getWidth(); col++) {
                        visited++; // Tambah 1

                        if (canPlacePiece(shape, row, col)) {
                            placePiece(shape, row, col, piece.getId());

                            if (solve(pieceIndex + 1)) {
                                return true; // Bisa gan
                            }

                            removePiece(shape, row, col); // Backtrack
                        }
                    }
                }
                piece = piece.rotate90();
            }
            piece = piece.flipVertical();
        }
        
        return false; // Udah gabisa
    }

    private boolean canPlacePiece(char[][] shape, int startRow, int startCol) {
        int shapeHeight = shape.length;
        int shapeWidth = shape[0].length;

        if (startRow + shapeHeight > board.getHeight() || startCol + shapeWidth > board.getWidth()) {
            return false; // Nembus wak
        }

        for (int i = 0; i < shapeHeight; i++) {
            for (int j = 0; j < shapeWidth; j++) {
                if (shape[i][j] != ' ' && board.getGrid()[startRow + i][startCol + j] != 'X') {
                    return false; // Nempel wak
                }
            }
        }
        return true;
    }

    private void placePiece(char[][] shape, int startRow, int startCol, char pieceId) {
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[0].length; j++) {
                if (shape[i][j] != ' ') {
                    board.getGrid()[startRow + i][startCol + j] = pieceId;
                }
            }
        }
    }

    private void removePiece(char[][] shape, int startRow, int startCol) {
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[0].length; j++) {
                if (shape[i][j] != ' ') {
                    board.getGrid()[startRow + i][startCol + j] = 'X'; // Reset to X (empty)
                }
            }
        }
    }

    public void printSolution() {
        board.printBoard();
    }
}