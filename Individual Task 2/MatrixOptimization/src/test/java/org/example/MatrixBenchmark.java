package org.example;
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
public class MatrixBenchmark {

    // n Parametrization
    @Param({"256", "512", "1000"})
    public int n;
    MatrixMultiplication matrixMultiplier = new MatrixMultiplication();
    StrassenMatrixMultiplication strassenMultiplier = new StrassenMatrixMultiplication();
    double[][] a;
    double[][] b;

    @Setup(Level.Trial)
    public void setup() {
        a = new double[n][n];
        b = new double[n][n];

        Random random = new Random();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                a[i][j] = random.nextDouble();
                b[i][j] = random.nextDouble();
            }
        }
    }

    @Benchmark
    public double[][] testMatrixMultiplication(){
        return matrixMultiplier.multiply(a, b);
    }

    @Benchmark
    public double[][] testHoistedMultiply(){
        return matrixMultiplier.hoistedMultiply(a, b);
    }

    @Benchmark
    public double[][] testUnrolledMultiply(){
        return matrixMultiplier.unrolledMultiply(a, b);
    }

    @Benchmark
    public double[][] testStrassenMultiplication(){
        return strassenMultiplier.multiply(a, b);
    }

    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
                .include(MatrixBenchmark.class.getSimpleName())
                .forks(5)
                .warmupIterations(10)
                .measurementIterations(10)
                .measurementTime(TimeValue.seconds(1))
                .warmupTime(TimeValue.seconds(1))
                .output("console_results.log")
                .result("benchmark_results.json")
                .resultFormat(org.openjdk.jmh.results.format.ResultFormatType.JSON)
                .build();

        new Runner(opt).run();
    }
}
