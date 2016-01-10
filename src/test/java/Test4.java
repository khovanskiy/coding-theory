import com.khovanskiy.coding.Main;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * victor
 */
public class Test4 {

    public String concat(List<?> arrs, String delimeter) {
        String temp = "";
        for (int i = 0; i < arrs.size() - 1; ++i) {
            temp += arrs.get(i) + delimeter;
        }
        temp += arrs.get(arrs.size() - 1);
        return temp;
    }

    /**
     * Максимальная длина последовательности идущих подряд элементов
     */
    public int seqLength(Group group) {
        int c = 1;
        int m = 1;
        for (int i = 1; i < group.size(); ++i) {
            if (group.get(i - 1) + 1 == group.get(i)) {
                ++c;
                if (c > m) {
                    m = c;
                }
            } else {
                c = 1;
            }
        }
        return m;
    }

    @Test
    public void posTest() {
        Assert.assertEquals(1, seqLength(new Group(0)));
        Assert.assertEquals(2, seqLength(new Group(1, 2, 4, 8)));
        Assert.assertEquals(4, seqLength(new Group(1, 2, 3, 4, 6, 9, 12)));
        Assert.assertEquals(6, seqLength(new Group(1, 2, 3, 4, 5, 6, 8, 9, 10, 12)));
        Assert.assertEquals(14, seqLength(new Group(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14)));
    }

    @Test
    @Ignore
    public void testPolynoms() {
        /*Assert.assertEquals("0", Polynomial.of(0).toString());
        Assert.assertEquals("1", Polynomial.of(1).toString());
        Assert.assertEquals("x", Polynomial.of(0, 1).toString());
        Assert.assertEquals("x<sup>2</sup>", Polynomial.of(0, 0, 1).toString());
        Assert.assertEquals("x<sup>2</sup>+x<sup>5</sup>", Polynomial.of(0, 0, 1, 0, 0, 1).toString());
        Assert.assertEquals("3+x<sup>2</sup>+x<sup>5</sup>-x<sup>7</sup>", Polynomial.of(3, 0, 1, 0, 0, 1, 0, -1).toString());
        Assert.assertEquals("x<sup>2</sup>+x<sup>3</sup>+x<sup>5</sup>", Polynomial.shiftAndMul(Polynomial.of(1, 1, 0, 1), 2, 1, 2).toString());*/
        System.out.println(Polynomial.mul(Polynomial.of(1, 1, 0, 1), Polynomial.of(1, 1), 2));
        System.out.println(Polynomial.mod(Polynomial.of(1, 0, 1, 1, 1), Polynomial.of(1, 0, 1), 2));
    }

    @Test
    public void testExample() {
        Polynomial px = Polynomial.of(1, 1, 0, 0, 1);
        Polynomial current = Polynomial.of(1);
        Polynomial x = Polynomial.of(0, 1);
        for (int i = 0; i <= 14; ++i) {
            System.out.println(i + " | " + current);
            current = Polynomial.mod(Polynomial.mul(current, x, 2), px, 2);
        }
    }

    @Test
    public void main() throws IOException {
        // основание системы счисления (2 - двоичный код)
        int p = 2;
        // модуль / длина кода
        int n = 17;

        int q = 2;
        int d = 6;

        List<Group> groups = new ArrayList<>();
        Set<Integer> used = new HashSet<>();
        for (int s = 0; s < n; ++s) {
            if (!used.contains(s)) {
                Group group = new Group();
                group.add(s);
                int b = s * p % n;
                while (!group.contains(b)) {
                    group.add(b);
                    used.add(b);
                    b = b * p % n;
                }
                groups.add(group);
            }
        }
        try (PrintWriter writer = new PrintWriter("result.html")) {
            writer.println("<html><head><meta charset=\"UTF-8\"><link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css\"/></head><body>");
            writer.println("<h2>Циклотомические классы по модулю " + n + "</h2>");
            writer.println("<ul>");
            for (Group group : groups) {
                writer.println("<li>C<sub>" + group.get(0) + "</sub> = " + group + "</li>");
            }
            writer.println("</ul>");
            writer.println("<h2>Минимальные многочлены корней</h2>");
            writer.println("<ul>");
            for (Group group : groups) {
                if (group.get(0) == 0) {
                    writer.println("<li>M<sub>0</sub>(x) = x + 1</li>");
                } else {
                    writer.println("<li>M<sub>" + group.get(0) + "</sub>(x) = " + concat(group.stream().map(i -> "(x - a<sup>" + i + "</sup>)").collect(Collectors.toList()), "*") + "</li>");
                }
            }
            writer.println("</ul>");
            int[][] map = Main.words(groups.size());
            writer.println("<h2>БЧХ-коды длины " + n + "</h2>");
            writer.println("<table class='table'>");
            writer.println("<tr><th>Показатели</th><th>Порождающий</th><th>Размерность<br/>n-deg g(x)</th><th>Расстояние</th></tr>");
            List<Code> codes = new ArrayList<>();
            for (int i = 0; i < map.length - 1; ++i) {
                Group g = new Group();
                List<Integer> polynomial = new ArrayList<>();
                for (int j = 0; j < map[i].length; ++j) {
                    if (map[i][j] == 1) {
                        g.addAll(groups.get(j));
                        polynomial.add(groups.get(j).get(0));
                    }
                }
                if (!g.isEmpty()) {
                    Collections.sort(g);
                    int distance = seqLength(g) + 1;
                    assert distance <= n;
                    codes.add(new Code(g, polynomial, n - g.size(), distance));
                }
            }

            int acc = 1;
            int foundM = 0;
            for (int m = 1; m < 32; ++m) {
                acc *= 2;
                if (acc - 1 == n) {
                    writer.println("<p>Число n = " + n + " равно числу 2<sup>m</sup> - 1 = " + (acc - 1) + " = " + concat(Main.primes(acc - 1), "*") + " при m = " + m + "</p>");
                    writer.println("<p>Можно построить примитивный БЧХ код.</p>");
                    foundM = m;
                    break;
                } else if (Main.primes(acc - 1).contains(n)) {
                    writer.println("<p>Число n = " + n + " является делителем числа 2<sup>m</sup> - 1 = " + (acc - 1) + " = " + concat(Main.primes(acc - 1), "*") + " при m = " + m + "</p>");
                    writer.println("<p>Можно построить непримитивный БЧХ код.</p>");
                    foundM = m;
                    break;
                }
            }

            codes = codes.stream().sorted((lhs, rhs) -> Integer.compare(lhs.distance, rhs.distance)).collect(Collectors.toList());
            for (Code code : codes) {
                printCode(writer, code);
            }
            writer.println("</table>");

            Code code = codes.stream().sorted((lhs, rhs) -> Integer.compare(lhs.distance, rhs.distance)).filter(c -> c.distance >= d).findFirst().get();
            if (code == null) {
                writer.println("<p>Подходящего БЧХ кода с минимальным расстояние d = " + d + " не найдено.</p>");
            } else {
                writer.println("<p>Подходящий БЧХ кода с минимальным расстоянием d = " + d + " найден:</p>");
                writer.println("<table class='table'>");
                writer.println("<tr><th>Показатели</th><th>Порождающий</th><th>Размерность<br/>n-deg g(x)</th><th>Расстояние</th></tr>");
                printCode(writer, code);
                writer.println("</table>");
                writer.println("<ul>");
                writer.println("<li>Порождающий многочлен <strong>g(x)</strong> =" + concat(code.group.stream().map(i -> "(x - a<sup>" + i + "</sup>)").collect(Collectors.toList()), "*") + "</li>");
                List<Polynomial> polynomials = code.group.stream().map(i -> {
                    int size = Math.max(1, i) + 1;
                    int[] values = new int[size];
                    values[1] = 1;
                    values[i] = 1;
                    return Polynomial.of(values);
                }).collect(Collectors.toList());
                Polynomial temp = polynomials.get(0);
                for (int i = 1; i < polynomials.size(); ++i) {
                    temp = Polynomial.mul(temp, polynomials.get(i), q);
                }
                Polynomial prim = Polynomial.kxm(1, 8).add(Polynomial.kxm(1, 4), q).add(Polynomial.kxm(1, 3), q).add(Polynomial.kxm(1, 2), q).add(Polynomial.kxm(1, 0), q);
                Polynomial p3 = Polynomial.of(1, 1, 0, 1);
                Polynomial p5 = Polynomial.of(1, 0, 1, 0, 0, 1);
                Polynomial p17 = Polynomial.kxm(1, 17).add(Polynomial.of(1, 0, 0, 1), q);
                writer.println(p3 + "<br/>");
                writer.println(p5 + "<br/>");
                writer.println(p17 + "<br/>");
                Polynomial tt = p3.mul(p5, q).mul(p17, q);
                writer.println(tt + "<br/>");

                writer.println("<li>Примитивный полином <strong>p(x)</strong> = " + prim + "</li>");
                writer.println("<li>Примитивный элемент поля <strong>a</strong> = " + Polynomial.of(0, 1) + "</li>");
                writer.println("</ul>");
                Polynomial gx = Polynomial.mod(temp, prim, q);
                writer.println("<p>Таким образом, порождающий многочлен <strong>g(x)</strong> = " + gx + "</p>");
                writer.println("<h2>Порождающая матрица G</h2>");
                writer.println("<table class='table'>");
                System.out.println("N = " + n + " |g(x)| = " + gx.numbers.length);
                int r = gx.length();
                int h = n - r;
                for (int i = 0; i <= h; ++i) {
                    writer.println("<tr>");
                    for (int j = 0; j < i; ++j) {
                        writer.println("<td>0</td>");
                    }
                    for (int j = 0; j < gx.length(); ++j) {
                        writer.print("<td class='success'>" + gx.get(j) + "</td>");
                    }
                    for (int j = 0; j < h - i; ++j) {
                        writer.println("<td>0</td>");
                    }
                    writer.println("</tr>");
                }
                writer.println("</table>");

                Polynomial xn_1 = Polynomial.kxm(1, 0).add(Polynomial.kxm(1, n), q);
                writer.println(xn_1 + "<br/>");
                writer.println("Mod = " + Polynomial.mod(xn_1, p5, q) + "<br/>");
            }

            writer.println("</body></html>");
        }
    }

    private void printCode(PrintWriter writer, Code code) {
        writer.println("<tr>");
        writer.println("<td>" + code.group + "</td>");
        String tm = "";
        for (int i : code.polynomial) {
            tm += "M<sub>" + i + "</sub>";
        }
        writer.println("<td>" + tm + "</td>");
        writer.println("<td>" + code.size + "</td>");
        writer.println("<td>" + code.distance + "</td>");
        writer.println("</tr>");
    }

    public static class Code {
        Group group;
        List<Integer> polynomial;
        int size;
        int distance;

        public Code(Group group, List<Integer> polynomial, int size, int distance) {
            this.group = group;
            this.polynomial = polynomial;
            this.size = size;
            this.distance = distance;
        }
    }

    public static class Polynomial {
        private final int[] numbers;

        private Polynomial(int... numbers) {
            this.numbers = numbers;
        }

        public static Polynomial of(int... numbers) {
            return new Polynomial(numbers);
        }

        public int length() {
            return numbers.length;
        }

        public int get(int i) {
            return numbers[i];
        }

        public static Polynomial kxm(int k, int m) {
            assert k != 0;
            int[] numbers = new int[m + 1];
            numbers[m] = k;
            numbers = normalize(numbers);
            return Polynomial.of(numbers);
        }

        public static Polynomial add(Polynomial a, Polynomial b, int q) {
            int sc = Math.max(a.numbers.length, b.numbers.length);
            int[] values = new int[sc];
            for (int i = 0; i < sc; ++i) {
                int cv = 0;
                if (i < a.numbers.length) {
                    cv += a.numbers[i];
                }
                if (i < b.numbers.length) {
                    cv += b.numbers[i];
                }
                values[i] = cv % q;
            }
            values = normalize(values);
            return Polynomial.of(values);
        }

        public Polynomial add(Polynomial other, int q) {
            return Polynomial.add(this, other, q);
        }

        public static int[] normalize(int[] a) {
            int c = a.length - 1;
            while (c > 0) {
                if (a[c] != 0) {
                    break;
                }
                --c;
            }
            if (c == a.length - 1) {
                return a;
            }
            int[] temp = new int[c + 1];
            System.arraycopy(a, 0, temp, 0, temp.length);
            return temp;
        }

        public Polynomial mul(Polynomial other, int q) {
            return Polynomial.mul(this, other, q);
        }

        public static Polynomial mul(Polynomial a, Polynomial b, int q) {
            //System.out.println("(" + a + ")*(" + b + ")");
            Polynomial temp = null;
            for (int i = 0; i < b.numbers.length; ++i) {
                if (b.numbers[i] != 0) {
                    Polynomial current = Polynomial.shiftAndMul(a, i, b.numbers[i], q);
                    //System.out.println(i + " = " + current);
                    if (temp == null) {
                        temp = current;
                    } else {
                        temp = Polynomial.add(temp, current, q);
                    }
                }
            }
            return temp;
        }

        public static Polynomial mod(Polynomial a, Polynomial b, int q) {
            Polynomial dividend = a;
            while (dividend.numbers.length >= b.numbers.length) {
                int shift = dividend.numbers.length - b.numbers.length;
                Polynomial temp;
                if (shift > 0) {
                    temp = Polynomial.shiftAndMul(b, shift, 1, q);
                } else {
                    temp = b;
                }
                dividend = Polynomial.add(dividend, temp, 2);
            }
            return dividend;
        }

        public static Polynomial shiftAndMul(Polynomial p, int shift, int k, int q) {
            int[] values = new int[p.numbers.length + shift];
            System.arraycopy(p.numbers, 0, values, 0, p.numbers.length);
            for (int i = values.length - shift - 1; i >= 0; --i) {
                if (values[i] != 0) {
                    int temp = values[i];
                    values[i] = 0;
                    values[i + shift] = temp * k % q;
                }
            }
            return Polynomial.of(values);
        }

        @Override
        public int hashCode() {
            return numbers.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Polynomial) {
                Polynomial other = (Polynomial) obj;
                return this.numbers.equals(other.numbers);
            }
            return false;
        }

        @Override
        public String toString() {
            if (numbers.length == 1 && numbers[0] == 0) {
                return "0";
            }
            StringBuilder sb = new StringBuilder();
            boolean flag = false;
            for (int i = 0; i < numbers.length; ++i) {
                if (numbers[i] != 0) {
                    if (i == 0) {
                        sb.append(numbers[i]);
                        flag = true;
                    } else {
                        if (flag && numbers[i] > 0) {
                            sb.append("+");
                        }
                        if (numbers[i] != 1) {
                            if (numbers[i] == -1) {
                                sb.append("-");
                            } else {
                                sb.append(numbers[i]);
                            }
                        }
                        if (i != 1) {
                            sb.append("x<sup>").append(i).append("</sup>");
                        } else {
                            sb.append("x");
                        }
                        flag = true;
                    }
                }
            }
            return sb.toString();
        }
    }

    public static class Group extends ArrayList<Integer> {
        public Group(Integer... numbers) {
            super(Arrays.asList(numbers));
        }
    }
}
