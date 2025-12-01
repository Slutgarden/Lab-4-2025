import functions.TabulatedFunction;
import functions.ArrayTabulatedFunction;
import functions.TabulatedFunctions;

import functions.basic.Sin;
import functions.basic.Cos;
import functions.basic.Exp;
import functions.basic.Log;

import java.io.*;

public class Main {
    public static void main(String[] args) {

        Sin sin = new Sin();
        Cos cos = new Cos();

        System.out.println("Исходные Sin и Cos:");
        for (double x = 0; x <= Math.PI; x += 0.1) {
            double s = sin.getFunctionValue(x);
            double c = cos.getFunctionValue(x);
            System.out.printf("x=%.2f  sin=%.5f  cos=%.5f\n", x, s, c);
        }

        TabulatedFunction tabSin = TabulatedFunctions.tabulate(sin, 0, Math.PI, 10);
        TabulatedFunction tabCos = TabulatedFunctions.tabulate(cos, 0, Math.PI, 10);

        System.out.println("\nТабулированные Sin и Cos:");
        for (double x = 0; x <= Math.PI; x += 0.1) {
            System.out.printf("x=%.2f  tabSin=%.5f  tabCos=%.5f\n",
                    x, tabSin.getFunctionValue(x), tabCos.getFunctionValue(x));
        }

        int n = tabSin.getPointsCount();
        double[] xs = new double[n];
        double[] ys = new double[n];

        for (int i = 0; i < n; i++) {
            xs[i] = tabSin.getPointX(i);
            double a = tabSin.getPointY(i);
            double b = tabCos.getPointY(i);
            ys[i] = a * a + b * b;
        }

        TabulatedFunction sumSquares =
                new ArrayTabulatedFunction(xs[0], xs[xs.length - 1], ys);

        System.out.println("\nСумма квадратов tabSin и tabCos:");
        for (double x = 0; x <= Math.PI; x += 0.1) {
            System.out.printf("x=%.2f  sumSquares=%.5f\n", x, sumSquares.getFunctionValue(x));
        }

        TabulatedFunction expTab = TabulatedFunctions.tabulate(new Exp(), 0, 10, 11);

        try (Writer wr = new FileWriter("exp.txt")) {
            TabulatedFunctions.writeTabulatedFunction(expTab, wr);
        } catch (IOException e) {
            System.out.println("Ошибка записи exp: " + e.getMessage());
        }

        TabulatedFunction readExp = null;
        try (Reader rd = new FileReader("exp.txt")) {
            readExp = TabulatedFunctions.readTabulatedFunction(rd);
        } catch (IOException e) {
            System.out.println("Ошибка чтения exp: " + e.getMessage());
        }

        System.out.println("\nСравнение Exp:");
        for (int i = 0; i < expTab.getPointsCount(); i++) {
            double y1 = expTab.getPointY(i);
            double y2 = readExp.getPointY(i);
            System.out.printf("x=%d  orig=%.5f  read=%.5f\n", i, y1, y2);
        }

        TabulatedFunction logTab = TabulatedFunctions.tabulate(new Log(Math.E), 1, 10, 11);

        try (OutputStream out = new FileOutputStream("log.bin")) {
            TabulatedFunctions.outputTabulatedFunction(logTab, out);
        } catch (IOException e) {
            System.out.println("Ошибка записи log: " + e.getMessage());
        }

        TabulatedFunction readLog = null;
        try (InputStream in = new FileInputStream("log.bin")) {
            readLog = TabulatedFunctions.inputTabulatedFunction(in);
        } catch (IOException e) {
            System.out.println("Ошибка чтения log: " + e.getMessage());
        }

        System.out.println("\nСравнение Log:");
        for (int i = 0; i < logTab.getPointsCount(); i++) {
            double x = logTab.getPointX(i);
            double y1 = logTab.getPointY(i);

            double y2 = readLog.getFunctionValue(x);

            System.out.printf("x=%.2f  orig=%.5f  read=%.5f\n", x, y1, y2);
        }
    }
}
