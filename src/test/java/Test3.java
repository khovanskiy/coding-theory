import com.khovanskiy.coding.Main;
import org.junit.Test;

/**
 * @author victor
 */
public class Test3 {

    public int N(int k, int d) {
        if (k == 0) {
            return d;
        }
        return d + N(k - 1, Math.round(d / 2.0f));
    }

    public double log2(double x) {
        return Math.log(x) / Math.log(2);
    }

    public double h(double x) {
        return -x * log2(x) - (1 - x) * log2(1 - x);
    }

    public double BE(int n, int d) {
        double delta = (double) d / (double) n;
        return n * (1 - h(0.5 - 0.5 * Math.sqrt(1 - 2.0 * delta)));
    }

    @Test
    public void main() {
        System.out.println(BE(27, 11));
        //System.out.println((int)Math.round(0.55));
        //System.out.println((int)Math.round(0.5));
        //System.out.println((int)Math.round(1.9));
    }
}
