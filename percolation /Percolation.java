public class Percolation {
    // creates n-by-n grid, with all sites initially blocked
    // -1 stands for block, 0 stands for open, 1 stands for full
    private int[][] grid;
    private int gridSize;
    private QuickUnion qu;


    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("n less than or equals 0");
        grid = new int[n][n];
        gridSize = n;
        qu = new QuickUnion(n*n+2);

        for  (int i = 0; i < n; i++) {
            for  (int j = 0; j < n; j++) {
                grid[i][j] = -1;
            }
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        checkValid(row, col);
        if (grid[row - 1][col - 1] < 0) grid[row - 1][col - 1] = 0;
        if (row == 1) qu.union(0, col);
        int surrondedGrids[][]={{row, col - 1},{row, col + 1}, {row - 1, col}, {row + 1, col}};
        for (int i = 0; i < 4; i++) {
            try {
                if (isOpen(surrondedGrids[i][0], surrondedGrids[i][1])) {
                    // StdOut.println(row + " " + col + "|" + surrondedGrids[i][0]  + " " + surrondedGrids[i][1]);
                    qu.union( (surrondedGrids[i][0] - 1)*gridSize + surrondedGrids[i][1], (row - 1)*gridSize + col);
                }
            }
            catch (IllegalArgumentException e) {continue;}
        }
        if (row == gridSize) qu.union( gridSize*gridSize + 1, (row - 1)*gridSize + col);
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        checkValid(row, col);
        return (grid[row - 1][col - 1] >= 0);
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        checkValid(row, col);
        return (grid[row - 1][col - 1] == 1);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        int openCount = 0;
        for  (int i = 0; i < gridSize; i++) {
            for  (int j = 0; j < gridSize; j++) {
                if (grid[i][j] >= 0) openCount++;
            }
        }
        return openCount;
    }

    // does the system percolate?
    public boolean percolates() {
        return qu.connected(0, gridSize*gridSize + 1);
    }

    private void checkValid(int row, int col) {
        if (!(row > 0 && row <= gridSize && col > 0 && col <= gridSize)) {
            throw new IllegalArgumentException("invalid row or col");
        }
    }

    // test client (optional)
    public static void main(String[] args) {}
}

class QuickUnion {
    int[] id;
    int[] weight;

    public QuickUnion(int n) {
        id = new int[n];
        weight = new int[n];
        for (int i = 0; i < n; i++) {
            weight[i] = 1;
            id[i] = i;
        }
    }

    public void union(int p, int q) {
        int i = root(p);
        int j = root(q);

        if (i == j) return;
        if  (weight[i] < weight[j]) { id[i] = j; weight[j] += weight[i]; }
        else { id[j] = i; weight[i] += weight[j]; }
    }

    public boolean connected(int p, int q) {
        return root(p) == root(q);
    }

    // return the root of p
    public int root(int p) {
        while (id[p] != p) {
            id[p] = id[id[p]];
            p = id[p];
        }
        return p;
    }
}