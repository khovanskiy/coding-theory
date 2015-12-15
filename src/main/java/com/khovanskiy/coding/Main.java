package com.khovanskiy.coding;

import java.util.Arrays;
import java.util.Scanner;

/**
 * @author victor
 */
public class Main {

    private static int distance(int[] a, int[] b) {
        assert a.length == b.length;
        int count = 0;
        for (int i = 0; i < a.length; ++i) {
            if (a[i] != b[i]) {
                ++count;
            }
        }
        return count;
    }

    private static int minDistance(int[][] a) {
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < a.length; ++i) {
            for (int j = i + 1; j < a.length; ++j) {
                int d = distance(a[i], a[j]);
                if (d < min) {
                    min = d;
                }
            }
        }
        return min;
    }

    private static int[][] swap(int[][] a, int i, int j) {
        assert i >= 0 && i < a.length;
        assert j >= 0 && j < a.length;
        int[] temp = a[i];
        a[i] = a[j];
        a[j] = temp;
        return a;
    }

    private static int[][] sub(int[][] a, int i, int j, int q) {
        assert i >= 0 && i < a.length;
        assert j >= 0 && j < a.length;
        for (int k = 0; k < a[i].length; ++k) {
            a[i][k] = (q + a[i][k] - a[j][k]) % q;
        }
        return a;
    }

    private static boolean checkIP(int[][] a) {
        assert a.length > 0;
        int k = a.length;
        for (int i = 0; i < k; ++i) {
            for (int j = 0; j < k; ++j) {
                if (i == j && a[i][j] != 1 || i != j && a[i][j] != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean checkPI(int[][] a) {
        assert a.length > 0;
        int k = a.length;
        int n = a[0].length;
        for (int i = 0; i < k; ++i) {
            for (int j = n - k; j < n; ++j) {
                if (i == (j - n + k) && a[i][j] != 1 || i != (j - n + k) && a[i][j] != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private static int[][] convert(int[][] a) {
        if (checkIP(a)) {
            int k = a.length;
            int n = a[0].length;
            assert k <= n;
            int r = n - k;
            int[][] b = new int[r][n];
            for (int i = 0; i < k; ++i) {
                for (int j = 0; j < r; ++j) {
                    b[j][i] = a[i][j + k];
                }
            }
            for (int i = 0; i < r; ++i) {
                b[i][i + k] = 1;
            }
            return b;
        } else if (checkPI(a)) {
            int r = a.length;
            int n = a[0].length;
            int k = n - r;
            int[][] b = new int[k][n];
            for (int i = 0; i < r; ++i) {
                for (int j = 0; j < k; ++j) {
                    b[j][i + k] = a[i][j];
                }
            }
            for (int i = 0; i < k; ++i) {
                b[i][i] = 1;
            }
            return b;
        }
        throw new IllegalStateException("Matrix must be in form");
    }

    private static void print(int[][] a) {
        for (int i = 0; i < a.length; ++i) {
            System.out.println(Arrays.toString(a[i]));
        }
    }

    public static void main(String[] arg) {
        int[][] a = {
                {1, 1, 0, 1, 0, 0, 0, 1, 0, 0},
                {1, 1, 0, 0, 1, 1, 0, 1, 1, 1},
                {0, 1, 1, 0, 1, 1, 1, 1, 0, 0},
                {0, 1, 0, 1, 0, 1, 1, 0, 0, 1}
        };
        print(a);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String command = scanner.next();
            try {
                switch (command) {
                    case "swap": {
                        int i = Integer.parseInt(scanner.next());
                        int j = Integer.parseInt(scanner.next());
                        a = swap(a, i - 1, j - 1);
                        break;
                    }
                    case "sub": {
                        int i = Integer.parseInt(scanner.next());
                        int j = Integer.parseInt(scanner.next());
                        a = sub(a, i - 1, j - 1, 2);
                        break;
                    }
                    case "speed": {
                        System.out.println("R = " + ((float)a.length / (float)a[0].length));
                        break;
                    }
                    case "min": {
                        System.out.println("Min distance = " + minDistance(a));
                        break;
                    }
                    case "convert": {
                        a = convert(a);
                        break;
                    }
                    case "check": {
                        System.out.println("G = (I P) " + checkIP(a));
                        System.out.println("G = (P^T I) " + checkPI(a));
                        break;
                    }
                    case "exit": {

                        break;
                    }
                }

                print(a);
            } catch (Exception e) {
                System.out.println("Wrong command");
                e.printStackTrace();
            }
        }
    }
}
