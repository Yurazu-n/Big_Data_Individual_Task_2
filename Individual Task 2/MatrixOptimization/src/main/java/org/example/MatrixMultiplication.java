package org.example;

import java.util.Random;

public class MatrixMultiplication {
    public MatrixMultiplication(){
    }

    public double[][] multiply(double[][] matrixA, double[][] matrixB){

        int n = matrixA.length;

        double[][] result = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    result[i][j] = matrixA[i][k] * matrixB[k][j];
                }
            }
        }
        return result;
    }

    public double[][] hoistedMultiply(double[][] matrixA, double[][] matrixB){

        int n = matrixA.length;

        double[][] result = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int k = 0; k < n; k++) {
                double aTemp = matrixA[i][k];
                for (int j = 0; j < n; j++) {
                    result[i][j] += aTemp * matrixB[k][j];
                }
            }
        }
        return result;
    }

    public double[][] unrolledMultiply(double[][] A, double[][] B) {
        int n = A.length;
        double[][] result = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double sum = 0;
                int k;

                for (k = 0; k <= n - 4; k += 4) {
                    sum += A[i][k] * B[k][j];
                    sum += A[i][k + 1] * B[k + 1][j];
                    sum += A[i][k + 2] * B[k + 2][j];
                    sum += A[i][k + 3] * B[k + 3][j];
                }

                // Manages left operations
                for (; k < n; k++) {
                    sum += A[i][k] * B[k][j];
                }

                result[i][j] = sum;
            }
        }
        return result;
    }


}
