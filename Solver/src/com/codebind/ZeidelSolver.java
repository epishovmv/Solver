package com.codebind;

/**
 * Реализация решения СЛАУ методом Зейделя
 * @autor Epishov
 * @version 1.0
 */
public class ZeidelSolver extends IterationSolver {

    /**
     * Конструктор класса
     * @param coeff Матрица коэффициентов при неизвестных
     * @param freeCoeff Вектор свободных членов
     */
    public ZeidelSolver(float[][] coeff, float[] freeCoeff){
        super(coeff, freeCoeff);
    }

    /**
     * Поиск решения СЛАУ
     * @return Вектор со значениями найденных неизвестных - решение СЛАУ
     */
    @Override
    public float[] solve() {
        float[][] a = coeff;
        float[] b = freeCoeff;
        int n = getRank();
        float[] x = new float[n];
        for (int i = 0; i < n; i++)
            x[i] = 1;

        float[] p = new float[n];
        int m = 0;

        do {
            for (int i = 0; i < n; i++)
                p[i] = x[i];
            for (int i = 0; i < n; i++) {
                float var = 0;
                for (int j = 0; j < i; j++)
                    var += (a[i][j] * x[j]);
                for (int j = i + 1; j < n; j++)
                    var += (a[i][j] * p[j]);
                x[i] = (b[i] - var) / a[i][i];
            }
            m++;
        } while (!converge(x, p, n) && m < maxIterations);

        if (m == maxIterations) {
            return null;
        }
        return x;
    }

    /**
     * Проверка выполнения условия окончания итерационного алгоритма
     * @return true, если итерационный алгоритм может быть завершен, иначе false
     */
    boolean converge(float[] xk, float[] xkp, int n)
    {
        double norm = 0;
        for (int i = 0; i < n; i++)
            norm += (xk[i] - xkp[i]) * (xk[i] - xkp[i]);
        return (Math.sqrt(norm) < eps);
    }
    
}
