package com.codebind;

/**
 * Реализация решения СЛАУ методом Гаусса
 * @autor Epishov
 * @version 1.0
 */
public class GaussSolver extends AbstractSolver {

    /**
     * Конструктор класса
     * @param coeff Матрица коэффициентов при неизвестных
     * @param freeCoeff Вектор свободных членов
     */
    public GaussSolver(float[][] coeff, float[] freeCoeff){
        super(coeff, freeCoeff);
    }

    /**
     * Поиск решения СЛАУ
     * @return Вектор со значениями найденных неизвестных - решение СЛАУ
     */
    @Override
    public float[] solve() {
        int n = getRank();
        float[][] matrix = new float[n][n + 1];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = coeff[i][j];
            }
            matrix[i][n] = freeCoeff[i];
        }
        float[][] Matrix_Clone = new float[n][n + 1];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n + 1; j++)
                Matrix_Clone[i][j] = matrix[i][j];

        //Прямой ход (Зануление нижнего левого угла)
        for (int k = 0; k < n; k++) //k-номер строки
        {
            for (int i = 0; i < n + 1; i++) //i-номер столбца
                Matrix_Clone[k][i] = Matrix_Clone[k][i] / matrix[k][k]; //Деление k-строки на первый член !=0 для преобразования его в единицу
            for (int i = k + 1; i < n; i++) //i-номер следующей строки после k
            {
                float K = Matrix_Clone[i][k] / Matrix_Clone[k][k]; //Коэффициент
                for (int j = 0; j < n + 1; j++) //j-номер столбца следующей строки после k
                    Matrix_Clone[i][j] = Matrix_Clone[i][j] - Matrix_Clone[k][j] * K; //Зануление элементов матрицы ниже первого члена, преобразованного в единицу
            }
            for (int i = 0; i < n; i++) //Обновление, внесение изменений в начальную матрицу
                for (int j = 0; j < n + 1; j++)
                    matrix[i][j] = Matrix_Clone[i][j];
        }

        //Обратный ход (Зануление верхнего правого угла)
        for (int k = n - 1; k > -1; k--) //k-номер строки
        {
            for (int i = n; i > -1; i--) //i-номер столбца
                Matrix_Clone[k][i] = Matrix_Clone[k][i] / matrix[k][k];
            for (int i = k - 1; i > -1; i--) //i-номер следующей строки после k
            {
                float K = Matrix_Clone[i][k] / Matrix_Clone[k][k];
                for (int j = n; j > -1; j--) //j-номер столбца следующей строки после k
                    Matrix_Clone[i][j] = Matrix_Clone[i][j] - Matrix_Clone[k][j] * K;
            }
        }

        //Отделяем от общей матрицы ответы
        float[] Answer = new float[n];
        for (int i = 0; i < n; i++)
            Answer[i] = Matrix_Clone[i][n];

        return Answer;
    }
}
