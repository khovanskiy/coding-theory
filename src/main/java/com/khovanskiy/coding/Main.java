package com.khovanskiy.coding;

import java.util.*;

/**
 * @author victor
 */
public class Main {

    public static int distance(int[] a, int[] b) {
        assert a.length == b.length;
        int count = 0;
        for (int i = 0; i < a.length; ++i) {
            if (a[i] != b[i]) {
                ++count;
            }
        }
        return count;
    }

    public static int minDistance(int[][] a) {
        int min = Integer.MAX_VALUE;
        int k = a.length;
        int[][] m = words(k);
        for (int i = 1; i < m.length; ++i) {
            int[] c = mul(m[i], a);
            System.out.println(Arrays.toString(m[i]) + " | " + Arrays.toString(c));
            int d = w(c);
            if (d < min) {
                min = d;
            }
        }
        return min;
    }

    public static int[][] swap(int[][] a, int i, int j) {
        assert i >= 0 && i < a.length;
        assert j >= 0 && j < a.length;
        int[] temp = a[i];
        a[i] = a[j];
        a[j] = temp;
        return a;
    }

    public static int[][] sub(int[][] a, int i, int j, int q) {
        assert i >= 0 && i < a.length;
        assert j >= 0 && j < a.length;
        for (int k = 0; k < a[i].length; ++k) {
            a[i][k] = (q + a[i][k] - a[j][k]) % q;
        }
        return a;
    }

    public static int[][] add(int[][] a, int i, int j, int q) {
        assert i >= 0 && i < a.length;
        assert j >= 0 && j < a.length;
        for (int k = 0; k < a[i].length; ++k) {
            a[i][k] = (a[i][k] + a[j][k]) % q;
        }
        return a;
    }

    public static boolean checkIP(int[][] a) {
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

    public static boolean checkPI(int[][] a) {
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

    public static int[][] convert(int[][] a) {
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

    public static void print(int[][] a) {
        for (int i = 0; i < a.length; ++i) {
            System.out.println(Arrays.toString(a[i]));
        }
    }

    public static void print(Map<int[], int[]> a) {
        for (Map.Entry<int[], int[]> entry : a.entrySet()) {
            System.out.println(Arrays.toString(entry.getKey()) + "=" + Arrays.toString(entry.getValue()));
        }
    }

    public static int[][] transpose(int[][] a) {
        int k = a.length;
        assert a.length > 0;
        int n = a[0].length;
        int[][] b = new int[n][k];
        for (int i = 0; i < k; ++i) {
            for (int j = 0; j < n; ++j) {
                b[j][i] = a[i][j];
            }
        }
        return b;
    }

    public static int[] mul(int[] v, int[][] a) {
        assert v.length == a.length;
        int[] b = new int[a[0].length];
        for (int i = 0; i < a[0].length; ++i) {
            for (int j = 0; j < v.length; ++j) {
                b[i] += v[j] * a[j][i];
            }
            b[i] %= 2;
        }
        return b;
    }

    public static int[] inc(int[] v) {
        int c = 1;
        int[] b = new int[v.length];
        for (int i = v.length - 1; i >= 0; --i) {
            int t = (v[i] + c) % 2;
            int q = (v[i] + c) / 2;
            b[i] = t;
            c = q;
        }
        return b;
    }

    public static int[][] words(int r) {
        int[][] a = new int[1 << r][r];
        for (int i = 1; i < (1 << r); ++i) {
            a[i] = inc(a[i - 1]);
        }
        return a;
    }

    public static int w(int[] a) {
        int count = 0;
        for (int i = 0; i < a.length; ++i) {
            if (a[i] > 0) {
                ++count;
            }
        }
        return count;
    }

    public static Map<int[], int[]> syndroms(int[][] h) {
        System.out.println("H = " + h.length + "x" + h[0].length);
        h = transpose(h);
        print(h);
        System.out.println("H^T = " + h.length + "x" + h[0].length);
        int r = h.length;
        int n = h[0].length;
        int[][] errs = words(r);
        Map<int[], int[]> b = new HashMap<>();
        Set<String> cache = new HashSet<>();
        //int[][] b = new int[errs.length][h[0].length];
        for (int i = 0; i < errs.length; ++i) {
            int[] val = errs[i];
            int[] key = mul(val, h);
            if (!cache.contains(Arrays.toString(key))) {
                b.put(key, val);
                cache.add(Arrays.toString(key));
            }
        }
        return b;
    }

    public static int b(int[] x) {
        for (int i = 0; i < x.length; ++i) {
            if (x[i] == 1) {
                return i;
            }
        }
        return 0;
    }

    public static int e(int[] x) {
        for (int i = x.length - 1; i >= 0; i--) {
            if (x[i] == 1) {
                return i;
            }
        }
        return 0;
    }

    public static boolean isPrime(int a) {
        for (int i = 2; i < a; ++i) {
            if (a % i == 0) {
                return false;
            }
        }
        return true;
    }

    public static List<Integer> primes(int a) {
        List<Integer> array = new ArrayList<>();
        for (int i = 2; i < a; ++i) {
            if (a % i == 0 && isPrime(i)) {
                array.add(i);
            }
        }
        return array;
    }

    public static int span(int[] x) {
        return e(x) - b(x);
    }

    public static boolean checkMCF(int[][] x) {
        Set<Integer> bb = new HashSet<>();
        for (int i = 0; i < x.length; ++i) {
            int cb = b(x[i]);
            if (bb.contains(cb)) {
                return false;
            }
            bb.add(cb);
        }
        return true;
    }

    public static void main(String[] arg) {
        /*int[][] a = {
                {1, 1, 0, 1, 0, 0, 0, 1, 0, 0},
                {1, 1, 0, 0, 1, 1, 0, 1, 1, 1},
                {0, 1, 1, 0, 1, 1, 1, 1, 0, 0},
                {0, 1, 0, 1, 0, 1, 1, 0, 0, 1}
        };/**/
        /*int[][] a = {
                {0, 1, 0, 1, 1, 0},
                {1, 0, 1, 1, 0, 1},
                {1, 1, 0, 1, 0, 0}
        };*/
        int[][] a = {
                {1, 1, 0, 1, 0, 0},
                {0, 1, 0, 1, 1, 0},
                {0, 0, 1, 1, 1, 1}
        };
        /*int[][] a = {
                {1, 0, 1, 1, 0, 1},
                {1, 0, 1, 0, 1, 0},
                {1, 1, 0, 1, 0, 0}
        };/**/
        /*int[][] a = {
                {1, 1, 0, 1, 0, 0},
            {0, 1, 1, 1, 1, 0},
            {0, 0, 0, 1, 1, 1}
        };*/
        /*int[][] a = {
                {1, 1, 0, 0, 1, 1},
                {0, 0, 1, 0, 1, 1},
                {0, 1, 0, 1, 0, 1}
        };*/
        /*int[][] a = {
                {1, 0, 1, 0, 0, 1, 1, 0, 1, 1},
                {0, 0, 1, 0, 1, 0, 1, 1, 1, 0},
                {0, 0, 0, 1, 0, 1, 1, 0, 1, 0},
                {1, 1, 0, 1, 0, 0, 0, 1, 1, 0}
        };/**/
        /*int[][] a = {
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        };/**/
        //print(words(4));
        print(a);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String command = scanner.next();
            try {
                switch (command.toLowerCase()) {
                    case "swap": {
                        int i = Integer.parseInt(scanner.next());
                        int j = Integer.parseInt(scanner.next());
                        a = swap(a, i - 1, j - 1);
                        print(a);
                        break;
                    }
                    case "add": {
                        int i = Integer.parseInt(scanner.next());
                        int j = Integer.parseInt(scanner.next());
                        a = add(a, i - 1, j - 1, 2);
                        print(a);
                        break;
                    }
                    case "sub": {
                        int i = Integer.parseInt(scanner.next());
                        int j = Integer.parseInt(scanner.next());
                        a = sub(a, i - 1, j - 1, 2);
                        print(a);
                        break;
                    }
                    case "speed": {
                        System.out.println("R = " + ((float) a.length / (float) a[0].length));
                        break;
                    }
                    case "min": {
                        System.out.println("Min distance = " + minDistance(a));
                        break;
                    }
                    case "convert": {
                        a = convert(a);
                        print(a);
                        break;
                    }
                    case "check": {
                        System.out.println("G = (I P) " + checkIP(a));
                        System.out.println("G = (P^T I) " + checkPI(a));
                        break;
                    }
                    case "syndroms": {
                        print(syndroms(a));
                        break;
                    }
                    case "checkmcf": {
                        System.out.println("Is in MCF ? -> " + checkMCF(a));
                        break;
                    }
                    case "gen": {
                        int[][] x = words(a.length);
                        int[][] r = new int[x.length][a.length];
                        print(x);
                        for (int i = 0; i < x.length; ++i) {
                            r[i] = mul(x[i], a);
                        }
                        //print(r);
                        for (int i = 0; i < r.length; ++i) {
                            System.out.println(Arrays.toString(r[i]) + " " + b(r[i]) + ", " + (e(r[i]) - 1));
                        }
                    }
                    case "exit": {

                        break;
                    }
                }

                //print(a);
            } catch (Exception e) {
                System.out.println("Wrong command");
                e.printStackTrace();
            }
        }
    }
}
