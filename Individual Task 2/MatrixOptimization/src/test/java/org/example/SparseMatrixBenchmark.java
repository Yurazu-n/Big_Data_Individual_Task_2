package org.example;

import org.apache.commons.math3.linear.OpenMapRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(5)
public class SparseMatrixBenchmark {

    @Param({"256", "512", "1000"}) // Matrix sizes to benchmark
    public int n;

    SparseMatrixMultiplication sparseMultiplier = new SparseMatrixMultiplication();
    SparseMatrixMultiplication.SparseMatrix customMatrixA;
    SparseMatrixMultiplication.SparseMatrix customMatrixB;
    OpenMapRealMatrix apacheMatrixA;
    OpenMapRealMatrix apacheMatrixB;

    @Setup(Level.Trial)
    public void setup() {
        // Initialize custom sparse matrices
        customMatrixA = new SparseMatrixMultiplication.SparseMatrix(n, n);
        customMatrixB = new SparseMatrixMultiplication.SparseMatrix(n, n);

        // Initialize Apache Commons Math sparse matrices
        apacheMatrixA = new OpenMapRealMatrix(n, n);
        apacheMatrixB = new OpenMapRealMatrix(n, n);

        Random random = new Random();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // Set non-zero values with a 10% probability
                if (random.nextDouble() < 0.1) {
                    double value = random.nextDouble();
                    customMatrixA.set(i, j, value);
                    customMatrixB.set(i, j, value);
                    apacheMatrixA.setEntry(i, j, value);
                    apacheMatrixB.setEntry(i, j, value);
                }
            }
        }
    }

    @Benchmark
    public SparseMatrixMultiplication.SparseMatrix testCustomSparseMatrixMultiplication() {
        return sparseMultiplier.multiplyCustom(customMatrixA, customMatrixB);
    }

    @Benchmark
    public RealMatrix testApacheSparseMatrixMultiplication() {
        return sparseMultiplier.multiplyApache(apacheMatrixA, apacheMatrixB);
    }

    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
                .include(SparseMatrixBenchmark.class.getSimpleName())
                .forks(5)
                .warmupIterations(10)
                .measurementIterations(10)
                .measurementTime(TimeValue.seconds(1))
                .warmupTime(TimeValue.seconds(1))
                .output("sparse_console_results.log")
                .result("sparse_benchmark_results.json")
                .resultFormat(org.openjdk.jmh.results.format.ResultFormatType.JSON)
                .build();

        new Runner(opt).run();
    }
}
