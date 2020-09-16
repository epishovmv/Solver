package com.codebind;

/**
 * Реализация решения СЛАУ методом релаксации
 * @autor Epishov
 * @version 1.0
 */
public class RelaxationSolver extends IterationSolver {

    /**
     * Конструктор класса
     * @param coeff Матрица коэффициентов при неизвестных
     * @param freeCoeff Вектор свободных членов
     */
    public RelaxationSolver(float[][] coeff, float[] freeCoeff){
        super(coeff, freeCoeff);
    }

    /**
     * Поиск решения СЛАУ
     * @return Вектор со значениями найденных неизвестных - решение СЛАУ
     */
    @Override
    public float[] solve() {
        float[][] A = coeff;
        float[] B = freeCoeff;
        float w = 0.5f; //коэффициент релаксации
        int n = A.length;
        int i, j;
        int k = 0;
        float norma = 0;
        float[] x = new float[n];
        float[] xn = new float[n];
        for (i = 0; i < n; i++) {
            xn[i] = 0;
            x[i] = xn[i];
        }
        do {
            k++;
            norma = 0;
            for (i = 0; i < n; i++) {
                x[i] = B[i];
                for (j = 0; j < n; j++) {
                    if (i != j)
                        x[i] = x[i] - A[i][j] * x[j];
                }
                x[i] /= A[i][i];

                x[i] = w * x[i] + (1 - w) * xn[i];

                if (Math.abs(x[i] - xn[i]) > norma)
                    norma = Math.abs(x[i] - xn[i]);
                xn[i] = x[i];
            }
        }
        while (norma > eps && k < maxIterations);
        return x;
    }
}
