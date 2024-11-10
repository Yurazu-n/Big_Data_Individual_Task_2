package org.example;

import org.apache.commons.math3.linear.OpenMapRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.HashMap;
import java.util.Map;

public class SparseMatrixMultiplication {

    // Multiply using custom sparse matrix implementation
    public SparseMatrix multiplyCustom(SparseMatrix matrixA, SparseMatrix matrixB) {
        return matrixA.multiply(matrixB);
    }

    // Multiply using Apache Commons Math
    public RealMatrix multiplyApache(OpenMapRealMatrix matrixA, OpenMapRealMatrix matrixB) {
        return matrixA.multiply(matrixB);
    }

    // Custom sparse matrix class using a map for non-zero values
    public static class SparseMatrix {
        private final int rows;
        private final int cols;
        private final Map<String, Double> matrix;

        public SparseMatrix(int rows, int cols) {
            this.rows = rows;
            this.cols = cols;
            this.matrix = new HashMap<>();
        }

        // Set value in the matrix, removing entry if it's zero
        public void set(int row, int col, double value) {
            if (value != 0) {
                matrix.put(row + "," + col, value);
            } else {
                matrix.remove(row + "," + col);
            }
        }

        // Get value at specified position, returning zero if not present
        public double get(int row, int col) {
            return matrix.getOrDefault(row + "," + col, 0.0);
        }

        // Multiply this matrix with another sparse matrix
        public SparseMatrix multiply(SparseMatrix other) {
            SparseMatrix result = new SparseMatrix(this.rows, other.cols);

            for (String keyA : this.matrix.keySet()) {
                String[] indicesA = keyA.split(",");
                int i = Integer.parseInt(indicesA[0]);
                int k = Integer.parseInt(indicesA[1]);
                double valueA = this.matrix.get(keyA);

                for (int j = 0; j < other.cols; j++) {
                    double valueB = other.get(k, j);
                    if (valueB != 0) {
                        double newValue = result.get(i, j) + valueA * valueB;
                        result.set(i, j, newValue);
                    }
                }
            }
            return result;
        }
    }
}
