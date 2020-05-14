import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;
import java.lang.Math;

public class PercolationStats {
    // perform independent trials on an n-by-n grid
    private double[] results;
    private double mean;
    private double stddev;
    private double confidenceLo;
    private double confidenceHi;

    public PercolationStats(int n, int trials) {
        if (n <=0 || trials <= 0) throw new IllegalArgumentException();
        Stopwatch sw = new Stopwatch();
        results = new double[trials];
        // Throw an IllegalArgumentException in the constructor if either n ≤ 0 or trials ≤ 0
        int open_site;
        for (int i=0; i<trials; i++) {
            Percolation p = new Percolation(n);
            while (!(p.percolates())) {
                // uniform(int a, int b)
                // Returns a random integer uniformly in [a, b).
                open_site = StdRandom.uniform(0, n*n);
                int row = open_site / n + 1;
                int col = open_site % n + 1;
                p.open(row, col);
            }
            results[i] =  p.numberOfOpenSites() / (n*n*1.0);
        }
        // System.out.println(sw.elapsedTime());
        mean =  StdStats.mean(results);
        stddev = StdStats.stddev(results);
        confidenceLo =  mean - 1.96 * stddev / Math.sqrt(results.length);
        confidenceHi = mean + 1.96 * stddev / Math.sqrt(results.length);
    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return confidenceLo;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return confidenceHi;
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(n, T);
        StdOut.printf("mean         =   %f\n", ps.mean());
        StdOut.printf("stddev       =   %f\n", ps.stddev());
        System.out.println("95% confidence interval = [" + Double.toString(ps.confidenceLo()) + ", " +Double.toString(ps.confidenceHi()) + "]");
    }

}