package com.codebind;

/**
 * Базовый абстрактный класс решателя СЛАУ
 * @autor Epishov
 * @version 1.0
 */
public abstract class AbstractSolver {

    /**
     * Матрица коэффициентов при неизвестных
     */
    protected float[][] coeff;

    /**
     * Вектор свободных членов
     */
    protected float[] freeCoeff;

    /**
     * Конструктор класса
     * @param coeff Матрица коэффициентов при неизвестных
     * @param freeCoeff Вектор свободных членов
     */
    public AbstractSolver(float[][] coeff, float[] freeCoeff){
        this.coeff = coeff;
        this.freeCoeff = freeCoeff;
    }

    /**
     * Получение размерности матрицы коэффициентов при неизвестных (количества неизвестных)
     * @return
     */
    public int getRank(){
        return coeff.length;
    }

    /**
     * Поиск решения СЛАУ
     * @return Вектор со значениями найденных неизвестных - решение СЛАУ
     */
    public abstract float[] solve();
}
