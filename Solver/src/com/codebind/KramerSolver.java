package com.codebind;

/**
 * Реализация решения СЛАУ методом Крамера
 * @autor Epishov
 * @version 1.0
 */
public class KramerSolver extends AbstractSolver {

    /**
     * Конструктор класса
     * @param coeff Матрица коэффициентов при неизвестных
     * @param freeCoeff Вектор свободных членов
     */
    public KramerSolver(float[][] coeff, float[] freeCoeff){
        super(coeff, freeCoeff);
    }

    /**
     * Поиск решения СЛАУ
     * @return Вектор со значениями найденных неизвестных - решение СЛАУ
     */
    @Override
    public float[] solve() {
        int n = getRank();
        float mainDeterminant = determinantOfMatrix(coeff, n);
        if (mainDeterminant == 0) {
            return null;
        }
        float[] result = new float[n];
        for (int i = 0; i < n; i++) {
            var determinant = cramerDeretminant(i);
            result[i] = determinant / mainDeterminant;
        }
        return result;
    }

    /**
     * Вычисление определителя матрицы коэффициентов,
     * в которой заданная колонка заменена на столбец свободных коэффициентов
     * @param column Номер колонки для замены на столбец свободных коэффициентов
     * @return Значение определителя
     */
    float cramerDeretminant(int column) {
        int n = getRank();
        float[][] mat = new float[n][n];
        for(int i=0; i<n; i++) {
            for (int j = 0; j < n; j++) {
                if (j == column) {
                    mat[i][j] = freeCoeff[i];
                } else {
                    mat[i][j] = coeff[i][j];
                }
            }
        }
        return determinantOfMatrix(mat, n);
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
}
