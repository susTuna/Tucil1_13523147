public class Board {
    public static final String RESET = "\u001B[37m";  // reset to default (white)
    private int height,width;
    private char[][] grid;

    public void genBoard(int a, int b){
        this.height = a;
        this.width = b;
        this.grid = new char[height][width];

        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                this.grid[i][j] = 'X';
            }
        }
    }

    public int getHeight(){
        return this.height;
    }

    public int getWidth(){
        return this.width;
    }

    public char[][] getGrid(){
        return this.grid;
    }

    public boolean isSolutionValid(){
        for (int i = 0; i< height; i++){
            for (int j = 0; j < width; j++){
                if (grid[i][j] == 'X') return false;
            }
        }
        return true;
    }
    public void printBoard() {
    for (int i = 0; i < height; i++) {
        for (int j = 0; j < width; j++) {
            char piece = grid[i][j];
            String color = getColor(piece); 
            System.out.print(color + piece + RESET + "");
        }
        System.out.println();
        }
    }

    private String getColor(char piece) {
        switch (piece) {
            case 'A': return "\u001B[31m"; // Red
            case 'B': return "\u001B[32m"; // Green
            case 'C': return "\u001B[34m"; // Blue
            case 'D': return "\u001B[33m"; // Yellow
            case 'E': return "\u001B[35m"; // Purple
            case 'F': return "\u001B[36m"; // Cyan
            case 'G': return "\u001B[91m"; // Bright Red
            case 'H': return "\u001B[92m"; // Bright Green
            case 'I': return "\u001B[93m"; // Bright Yellow
            case 'J': return "\u001B[94m"; // Bright Blue
            case 'K': return "\u001B[95m"; // Bright Magenta
            case 'L': return "\u001B[96m"; // Bright Cyan
            case 'M': return "\u001B[97m"; // Bright White
            case 'N': return "\u001B[90m"; // Bright Black (Gray)
            case 'O': return "\u001B[41m\u001B[30m"; // Red Background, Black Text
            case 'P': return "\u001B[42m\u001B[30m"; // Green Background, Black Text
            case 'Q': return "\u001B[44m\u001B[30m"; // Blue Background, Black Text
            case 'R': return "\u001B[45m\u001B[30m"; // Magenta Background, Black Text
            case 'S': return "\u001B[46m\u001B[30m"; // Cyan Background, Black Text
            case 'T': return "\u001B[41m\u001B[37m"; // Red Background, White Text
            case 'U': return "\u001B[42m\u001B[37m"; // Green Background, White Text
            case 'V': return "\u001B[43m\u001B[37m"; // Yellow Background, White Text
            case 'W': return "\u001B[44m\u001B[37m"; // Blue Background, White Text
            case 'Y': return "\u001B[46m\u001B[37m"; // Cyan Background, White Text
            case 'Z': return "\u001B[47m\u001B[30m"; // White Background, Black Text
            default:  return "\u001B[37m"; // White
        }
    }
    
    public static void main(String[] args) {
        Board board = new Board();
        board.genBoard(5, 5);
        board.printBoard();
    }

}