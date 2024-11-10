package org.example;

public class StrassenMatrixMultiplication {

    public StrassenMatrixMultiplication(){}

    public double[][] multiply(double[][] A, double[][] B) {
        int n = A.length;
        int newSize = nextPowerOfTwo(n);
        double[][] APrep = padMatrix(A, newSize);
        double[][] BPrep = padMatrix(B, newSize);

        double[][] result = strassen(APrep, BPrep);
        return trimMatrix(result, n);
    }

    private static double[][] strassen(double[][] A, double[][] B) {
        int n = A.length;

        if (n == 1) {
            double[][] baseResult = { { A[0][0] * B[0][0] } };
            return baseResult;
        }

        int newSize = n / 2;
        // Sub-Matrix division
        double[][] A11 = new double[newSize][newSize];
        double[][] A12 = new double[newSize][newSize];
        double[][] A21 = new double[newSize][newSize];
        double[][] A22 = new double[newSize][newSize];

        double[][] B11 = new double[newSize][newSize];
        double[][] B12 = new double[newSize][newSize];
        double[][] B21 = new double[newSize][newSize];
        double[][] B22 = new double[newSize][newSize];

        splitMatrix(A, A11, A12, A21, A22);
        splitMatrix(B, B11, B12, B21, B22);

        double[][] M1 = strassen(add(A11, A22), add(B11, B22));
        double[][] M2 = strassen(add(A21, A22), B11);
        double[][] M3 = strassen(A11, subtract(B12, B22));
        double[][] M4 = strassen(A22, subtract(B21, B11));
        double[][] M5 = strassen(add(A11, A12), B22);
        double[][] M6 = strassen(subtract(A21, A11), add(B11, B12));
        double[][] M7 = strassen(subtract(A12, A22), add(B21, B22));

        // Sub-Matrix calc
        double[][] C11 = add(subtract(add(M1, M4), M5), M7);
        double[][] C12 = add(M3, M5);
        double[][] C21 = add(M2, M4);
        double[][] C22 = add(subtract(add(M1, M3), M2), M6);

        // Matrix union
        return joinMatrix(C11, C12, C21, C22);
    }

    private static int nextPowerOfTwo(int n) {
        int power = 1;
        while (power < n) {
            power <<= 1;
        }
        return power;
    }

    private static double[][] padMatrix(double[][] matrix, int size) {
        double[][] padded = new double[size][size];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                padded[i][j] = matrix[i][j];
            }
        }
        return padded;
    }

    private static double[][] trimMatrix(double[][] matrix, int size) {
        double[][] trimmed = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                trimmed[i][j] = matrix[i][j];
            }
        }
        return trimmed;
    }

    private static void splitMatrix(double[][] P, double[][] P11, double[][] P12, double[][] P21, double[][] P22) {
        int n = P11.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                P11[i][j] = P[i][j];
                P12[i][j] = P[i][j + n];
                P21[i][j] = P[i + n][j];
                P22[i][j] = P[i + n][j + n];
            }
        }
    }

    private static double[][] joinMatrix(double[][] C11, double[][] C12, double[][] C21, double[][] C22) {
        int n = C11.length;
        double[][] C = new double[n * 2][n * 2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                C[i][j] = C11[i][j];
                C[i][j + n] = C12[i][j];
                C[i + n][j] = C21[i][j];
                C[i + n][j + n] = C22[i][j];
            }
        }
        return C;
    }

    private static double[][] add(double[][] A, double[][] B) {
        int n = A.length;
        double[][] C = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                C[i][j] = A[i][j] + B[i][j];
            }
        }
        return C;
    }

    private static double[][] subtract(double[][] A, double[][] B) {
        int n = A.length;
        double[][] C = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                C[i][j] = A[i][j] - B[i][j];
            }
        }
        return C;
    }
}
