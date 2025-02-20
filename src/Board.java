public class Board {
    private int height,width;
    private char[][] grid;

    public void genBoard(int a, int b){
        this.height = a;
        this.width = b;
        this.grid = new char[height][width];

        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                this.grid[i][j] = '0';
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
    
    public void printBoard(){
        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                System.out.print(grid[i][j]);
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Board board = new Board();
        board.genBoard(5, 5);
        board.printBoard();
    }

}