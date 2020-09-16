package com.codebind;

/**
 * Реализация решения СЛАУ матричным методом
 * @autor Epishov
 * @version 1.0
 */
public class MatrixSolver extends AbstractSolver {

    /**
     * Конструктор класса
     * @param coeff Матрица коэффициентов при неизвестных
     * @param freeCoeff Вектор свободных членов
     */
    public MatrixSolver(float[][] coeff, float[] freeCoeff){
        super(coeff, freeCoeff);
    }

    /**
     * Поиск решения СЛАУ
     * @return Вектор со значениями найденных неизвестных - решение СЛАУ
     */
    @Override
    public float[] solve() {
        int n = getRank();
        float[][] inverse = inverse(coeff);
        if(inverse==null) {
            return null;
        }
        float[] result = new float[n];
        for(int i=0; i<n; i++) {
            result[i] = 0;
            for (int j = 0; j < n; j++) {
                result[i] += inverse[i][j] * freeCoeff[j];
            }
        }
        return result;
    }

    /**
     * Функция вычисляет алгебраическое дополнение матрицы
     * @param mat матрица
     * @param temp алгебраическое дополнение
     * @param p количество строк
     * @param q количество столбцов
     * @param n текущая размерность матрицы
     */
    void cofactor(float mat[][], float temp[][], int p, int q, int n)    {
        int i = 0, j = 0;
        for (int row = 0; row < n; row++)
        {
            for (int col = 0; col < n; col++)
            {
                if (row != p && col != q)
                {
                    temp[i][j++] = mat[row][col];
                    if (j == n - 1)
                    {
                        j = 0;
                        i++;
                    }
                }
            }
        }
    }

    /**
     * Функция рекурсивно вычисляет определитель переданной матрицы
     * @param mat матрица
     * @param n размерность матрицы
     * @return  возвращает определитель переданной матрицы
     */
    float determinantOfMatrix(float mat[][], int n)
    {
        float D = 0;
        if (n == 1)
            return mat[0][0];
        float temp[][] = new float[n][n];
        int sign = 1;
        for (int f = 0; f < n; f++)
        {
            cofactor(mat, temp, 0, f, n);
            D += sign * mat[0][f] * determinantOfMatrix(temp, n - 1);
            sign = -sign;
        }
        return D;
    }

    /**
     * Вычисление обратной матрицы
     * @return  возвращает обратную матрицу для матрицы
     */
    float[][] inverse(float[][] matrix) {
        float[][] inverse = new float[matrix.length][matrix.length];
        float det = determinantOfMatrix(matrix, matrix.length);
        if (det == 0) {
            return null;
        }
        float[][] adj = new float[matrix.length][matrix.length];
        adjoint(matrix, adj);
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix.length; j++)
                inverse[i][j] = adj[i][j] / (float) det;
        return inverse;
    }

    /**
     * Функция вычисляет присоединенную матрицу для переданной матрицы
     * @param mat матрица
     * @param adj присоединенная матрица
     */
    void adjoint(float mat[][],float [][]adj)
    {
        int N = mat.length;
        if (N == 1)
        {
            adj[0][0] = 1;
            return;
        }
        int sign = 1;
        float [][]temp = new float[N][N];

        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                cofactor(mat, temp, i, j, N);
                sign = ((i + j) % 2 == 0)? 1: -1;
                adj[j][i] = (sign)*(determinantOfMatrix(temp, N-1));
            }
        }
    }

    /**
     * Функция возвращает результат умножения 2 матриц
     * @param firstMatrix 1 умножаемая матрица
     * @param secondMatrix 2 умножаемая матрица
     * @return  возвращает результат умножения 2 матриц
     */
    float[][] multiply(float[][] firstMatrix, float[][] secondMatrix) {
        float[][] result = new float[firstMatrix.length][secondMatrix[0].length];
        for (int row = 0; row < result.length; row++) {
            for (int col = 0; col < result[row].length; col++) {
                result[row][col] = firstMatrix[row][col] * secondMatrix[row][col];
            }
        }
        return result;
    }
}
