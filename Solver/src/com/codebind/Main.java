package com.codebind;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.Locale;

/**
 * Класс главного окна приложения
 * @autor Epishov
 * @version 1.0
 */
public class Main extends JDialog {

    JSpinner txbVariableCount;
    JButton btnSolve;
    JTextArea txbResult;
    JTable tblCoefficients;
    JTable tblFreeCoefficients;
    JPanel contentPanel;

    /**
     * Входная точка программы.
     * Создание и отображение главного окна.
     * @param args Аргументы командной строки
     */
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        Main dialog = new Main();
        dialog.pack();
        dialog.setSize(new Dimension(800,600));
        dialog.setVisible(true);
        System.exit(0);
    }

    /**
     * Конструктор класса главного окна
     */
    public Main() {
        setContentPane(contentPanel);
        setModal(true);
        setTitle("Решение СЛАУ");
        setVariableNumber(3);
        txbVariableCount.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                updateView();
            }
        });
        btnSolve.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    solve();
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(null, exception.getMessage());
                }
            }
        });
        updateView();
        setCoefficients(new float[][]{
                {5, 2, 3},
                {1.5f, 4f, 3.3f},
                {-3.1f, 0, 3.7f}
        });
        setFreeCoefficients(new float[]{1, 2, 3});
    }

    /**
     * Получение указанного количества неизвестных в СЛАУ
     * @return
     */
    public int getVariableNumber(){
        return (int)txbVariableCount.getValue();
    }

    /**
     * Установка заданного количества неизвестных в СЛАУ
     * @param value
     */
    public void setVariableNumber(int value) {
        txbVariableCount.setValue(value);
    }

    /**
     * Перестроение таблицы коэффициентов и вектора свободных членов СЛАУ
     */
    void updateView() {
        int variableNumber = getVariableNumber();
        if(variableNumber<=0) {
            setVariableNumber(1);
            return;
        }
        tblCoefficients.setModel(new DefaultTableModel(variableNumber, variableNumber));
        tblFreeCoefficients.setModel(new DefaultTableModel(variableNumber, 1));
    }

    /**
     * Преобразование строковой записи в число.
     * Если строка value пустая, то она интерпретируется как 0.
     * Десятичная точка и запятая интерпретируются как разделитель дробной части.
     * @param value Строка содержащая число
     * @return Результат преобразования строки в число
     * @throws Exception
     */
    float parseString(String value) throws Exception {
        if (value == null || value.isBlank()) {
            return 0;
        }
        try {
            return Float.parseFloat(value.trim().replace(',', '.'));
        } catch (NumberFormatException ex) {
            throw new Exception("Значение '" + value + "' не является числом.");
        }
    }

    /**
     * Получение матрицы коэффициентов при неизвестных в СЛАУ
     * @return Матрица коэффициентов (квадратная)
     * @throws Exception
     */
    float[][] getCoefficients() throws Exception {
        int n = getVariableNumber();
        TableModel model = tblCoefficients.getModel();
        float[][] result = new float[n][n];
        for(int i=0; i<n; i++) {
            for (int j = 0; j < n; j++) {
                String str = (String) model.getValueAt(i, j);
                float value = parseString(str);
                result[i][j] = (float) value;
            }
        }
        return result;
    }

    /**
     * Установка значений матрицы коэффициентов в СЛАУ
     * @param value Матрица коэффициентов (квадратная)
     */
    void setCoefficients(float[][] value) {
        TableModel model = tblCoefficients.getModel();
        for (int i = 0; i < value.length; i++) {
            for (int j = 0; j < value[i].length; j++) {
                model.setValueAt(String.valueOf(value[i][j]), i, j);
            }
        }
    }

    /**
     * Получение вектора свободных коэффициентов в СЛАУ
     * @return Вектор свободных коэффициентов
     * @throws Exception
     */
    float[] getFreeCoefficients() throws Exception {
        int n = getVariableNumber();
        TableModel model = tblFreeCoefficients.getModel();
        float[] result = new float[n];
        for (int i = 0; i < n; i++) {
            String str = (String) model.getValueAt(i, 0);
            float value = parseString(str);
            result[i] = value;
        }
        return result;
    }

    /**
     * Установка значений вектора свободных коэффициентов в СЛАУ
     * @param value
     */
    void setFreeCoefficients(float[] value) {
        TableModel model = tblFreeCoefficients.getModel();
        for (int i = 0; i < value.length; i++) {
            model.setValueAt(String.valueOf(value[i]), i, 0);
        }
    }

    /**
     * Выполнение поиска решений СЛАУ и отображение результатов
     * @throws Exception
     */
    void solve() throws Exception {
        float[][] coefficients = getCoefficients();
        float[] freeCoefficients = getFreeCoefficients();

        displayEquations(coefficients, freeCoefficients);

        AbstractSolver solver = new GaussSolver(coefficients, freeCoefficients);
        float[] result = solver.solve();
        displayResult(result, "Решение методом Гаусса");

        solver = new KramerSolver(coefficients, freeCoefficients);
        result = solver.solve();
        displayResult(result, "Решение методом Крамера");

        solver = new MatrixSolver(coefficients, freeCoefficients);
        result = solver.solve();
        displayResult(result, "Решение матричным методом");

        solver = new ZeidelSolver(coefficients, freeCoefficients);
        result = solver.solve();
        displayResult(result, "Решение методом Зейделя");

        solver = new JacobiSolver(coefficients, freeCoefficients);
        result = solver.solve();
        displayResult(result, "Решение методом Якоби");

        solver = new RelaxationSolver(coefficients, freeCoefficients);
        result = solver.solve();
        displayResult(result, "Решение методом релаксации");
    }

    /**
     * Отображение отформатированной системы уравнений
     * @param coefficients Матрица коэффициентов при неизвестных
     * @param freeCoefficients Вектор свободных членов
     */
    void displayEquations(float[][] coefficients, float[] freeCoefficients) {
        txbResult.setText("Система уравнений:\r\n\r\n");
        for (int i = 0; i < coefficients.length; i++) {
            txbResult.append("    ");
            for (int j = 0; j < coefficients[i].length; j++) {
                String c = String.valueOf(coefficients[i][j]);
                if(j>0) {
                    if (coefficients[i][j] >= 0) {
                        c = " + " + c;
                    } else if(coefficients[i][j]<0) {
                        c = " - " + String.valueOf(Math.abs(coefficients[i][j]));
                    }
                }
                txbResult.append(c + "·" + "x" + String.valueOf(j + 1));
            }
            txbResult.append(" = " + String.valueOf(freeCoefficients[i]));
            txbResult.append("\r\n");
        }
        txbResult.append("\r\n");
    }

    /**
     * Отображение результата решения СЛАУ с пояснением
     * @param result Вектор с результатом решения СЛАУ
     * @param methodName Текст пояснения
     */
    void displayResult(float[] result, String methodName) {
        txbResult.append(methodName + ":");
        txbResult.append("\r\n\r\n    ");
        if (result == null) {
            txbResult.append("Система уравнений не имеет решения.\r\n");
        } else {
            for (int i = 0; i < result.length; i++) {
                txbResult.append("x" + String.valueOf(i + 1) + " = " + String.valueOf(result[i]) + "; ");
            }
        }
        txbResult.append("\r\n\r\n");
    }
}
