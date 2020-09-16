package com.codebind;

/**
 * Реализация решения СЛАУ методом Якоби
 * @autor Epishov
 * @version 1.0
 */
public class JacobiSolver extends IterationSolver {

    /**
     * Конструктор класса
     * @param coeff Матрица коэффициентов при неизвестных
     * @param freeCoeff Вектор свободных членов
     */
    public JacobiSolver(float[][] coeff, float[] freeCoeff){
        super(coeff, freeCoeff);
    }

    /**
     * Поиск решения СЛАУ
     * @return Вектор со значениями найденных неизвестных - решение СЛАУ
     */
    @Override
    public float[] solve() {
        int n = getRank();
        float[] X = new float[n];
        for (int i = 0; i < n; i++) {
            X[i] = i;
        }
        float[][] A = coeff;
        float[] F = freeCoeff;
        int iterations = 0;
        float[] TempX = new float[n];
        float norm = 0;
        do {
            for (int i = 0; i < n; i++) {
                TempX[i] = F[i];
                for (int g = 0; g < n; g++) {
                    if (i != g)
                        TempX[i] -= A[i][g] * X[g];
                }
                TempX[i] /= A[i][i];
            }
            norm = Math.abs(X[0] - TempX[0]);
            for (int h = 0; h < n; h++) {
                if (Math.abs(X[h] - TempX[h]) > norm)
                    norm = Math.abs(X[h] - TempX[h]);
                X[h] = (float) TempX[h];
            }
            iterations++;
        } while (norm > eps && iterations < maxIterations);
        if (iterations <= maxIterations) {
            return X;
        } else {
            return null;
        }
    }
}
