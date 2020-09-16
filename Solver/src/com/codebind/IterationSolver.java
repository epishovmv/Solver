package com.codebind;

/**
 * Базовый абстрактный класс решателя СЛАУ итерационными методами
 * @autor Epishov
 * @version 1.0
 */
public abstract class IterationSolver extends AbstractSolver {

    /**
     * Ограничение на максимальное количество итераций
     */
    public int maxIterations;

    /**
     * Требуемая минимальная погрешность решения
     */
    public float eps;

    /**
     * Конструктор класса
     * @param coeff Матрица коэффициентов при неизвестных
     * @param freeCoeff Вектор свободных членов
     */
    public IterationSolver(float[][] coeff, float[] freeCoeff) {
        super(coeff, freeCoeff);
        maxIterations = 1000000;
        eps = 0.000001f;
    }
}
