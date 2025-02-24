package com.phasmidsoftware.dsaipg.adt.pq;

import com.phasmidsoftware.dsaipg.util.Benchmark_Timer;
import java.util.Comparator;
import java.util.Random;
import java.util.function.Supplier;


public class PriorityQueueBenchmark {

    // Heap capacity
    private final int M;
    // Number of insert operations
    private final int numInsert;
    // Number of delete operations
    private final int numRemove;
    // Number of test runs
    private final int runs;

    /**
     * Constructor.
     *
     * @param M         the capacity of the priority queue (maximum number of elements held simultaneously)
     * @param numInsert the number of insert operations
     * @param numRemove the number of delete operations
     * @param runs      the number of test runs (used to obtain an average)
     */
    public PriorityQueueBenchmark(int M, int numInsert, int numRemove, int runs) {
        this.M = M;
        this.numInsert = numInsert;
        this.numRemove = numRemove;
        this.runs = runs;
    }

    /**
     * Executes a series of operations on the given priority queue:
     * 1. Inserts numInsert random integers;
     * 2. Performs numRemove delete operations.
     *
     * @param pq the priority queue instance
     */
    private void runPQTest(PriorityQueue<Integer> pq) {
        Random rand = new Random();
        for (int i = 0; i < numInsert; i++) {
            pq.give(rand.nextInt());
        }
        for (int j = 0; j < numRemove; j++) {
            try {
                pq.take();
            } catch (PQException e) {
                // If the queue is empty, exit the deletion loop
                break;
            }
        }
    }

    /**
     * Benchmarks the basic binary heap implementation (without using Floyd's trick).
     *
     * @return the average runtime in milliseconds
     */
    public double benchmarkBasic() {
        // Set floyd parameter to false
        Supplier<PriorityQueue<Integer>> supplier = () -> new PriorityQueue<Integer>(M, true, Comparator.naturalOrder(), false);
        Benchmark_Timer<PriorityQueue<Integer>> benchmark = new Benchmark_Timer<>(
                "PriorityQueue Basic (binary heap)",
                pq -> pq,   // Preprocessing function (does nothing)
                this::runPQTest,
                null);
        double time = benchmark.runFromSupplier(supplier, runs);
        System.out.printf("Basic binary heap (floyd = false) average time: %.3f ms%n", time);
        return time;
    }

    /**
     * Benchmarks the binary heap implementation using Floyd's trick (i.e., calling the snake method).
     *
     * @return the average runtime in milliseconds
     */
    public double benchmarkFloyd() {
        // Set floyd parameter to true
        Supplier<PriorityQueue<Integer>> supplier = () -> new PriorityQueue<Integer>(M, true, Comparator.naturalOrder(), true);
        Benchmark_Timer<PriorityQueue<Integer>> benchmark = new Benchmark_Timer<>(
                "PriorityQueue with Floyd's trick (binary heap)",
                pq -> pq,
                this::runPQTest,
                null);
        double time = benchmark.runFromSupplier(supplier, runs);
        System.out.printf("Binary heap with Floyd's trick (floyd = true) average time: %.3f ms%n", time);
        return time;
    }

    public static void main(String[] args) {
        // Example: Use capacity 4095, insert 16,000 elements, delete 4,000 elements, and run the test 200 times
        PriorityQueueBenchmark benchmark = new PriorityQueueBenchmark(4095, 16000, 4000, 400);
        benchmark.benchmarkBasic();
        benchmark.benchmarkFloyd();

        PriorityQueueBenchmark benchmark2 = new PriorityQueueBenchmark(4095, 16000, 4000, 800);
        benchmark2.benchmarkBasic();
        benchmark2.benchmarkFloyd();

        PriorityQueueBenchmark benchmark3 = new PriorityQueueBenchmark(4095, 16000, 4000, 1600);
        benchmark3.benchmarkBasic();
        benchmark3.benchmarkFloyd();

        PriorityQueueBenchmark benchmark4 = new PriorityQueueBenchmark(4095, 16000, 4000, 3200);
        benchmark4.benchmarkBasic();
        benchmark4.benchmarkFloyd();

        PriorityQueueBenchmark benchmark5 = new PriorityQueueBenchmark(4095, 16000, 4000, 6400);
        benchmark5.benchmarkBasic();
        benchmark5.benchmarkFloyd();

        PriorityQueueBenchmark benchmark6 = new PriorityQueueBenchmark(4095, 16000, 4000, 12800);
        benchmark6.benchmarkBasic();
        benchmark6.benchmarkFloyd();
    }
}
