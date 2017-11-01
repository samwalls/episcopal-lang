package compiler.helpers;

import java.util.Random;

/**
 * Disassemble this class to aid assembler instruction selection for episcopal programs.
 */
public class Episcopal {

    /**
     * This approximation of the log_gamma function uses source code from
     * https://introcs.cs.princeton.edu/java/91float/Gamma.java.html
     * @param x the input
     * @return log_e(gamma(x))
     */
    public static double logGamma(double x) {
        double tmp = (x - 0.5) * Math.log(x + 4.5) - (x + 4.5);
        double ser = 1.0 + 76.18009173    / (x + 0)   - 86.50532033    / (x + 1)
                + 24.01409822    / (x + 2)   -  1.231739516   / (x + 3)
                +  0.00120858003 / (x + 4)   -  0.00000536382 / (x + 5);
        return tmp + Math.log(ser * Math.sqrt(2 * Math.PI));
    }

    public static float gamma(double x) {
        return (float)Math.exp(logGamma(x));
    }

    public static float[] builtin_dist_binomial(float probability) {
        return new float[] {
                new Random().nextFloat() < probability ? 1f : 0f,
                probability
        };
    }

    public static float[] builtin_dist_normal(float mean, float standardDeviation) {
        float v = (float)new Random().nextGaussian() * standardDeviation + mean;
        float variance = (float) Math.pow(standardDeviation, 2);
        float probability = (float) (Math.exp(-Math.pow((v - mean), 2)/(2f* variance)) / Math.sqrt(2f * Math.PI * variance));
        return new float[] {
                v,
                probability
        };
    }

    public static float[] builtin_dist_beta(float a, float b) {
        // uniform random between 0 and 1
        float x = new Random().nextFloat();
        // calculate the probability with which this value could occur in the distribution
        float probability = (float) (gamma(a + b) / (gamma(a) + gamma(b)) * Math.pow(1 - x, b - 1) * Math.pow(x, a - 1));
        return new float[] {
                x,
                probability
        };
    }

    public static void builtin_observe(float[] a) {
        if (a[0] < 1f)
            throw new RuntimeException("program is takes on invalid value " + a[0] + " with probability " + a[1]);
    }

    // 1 betthefarm = bet365 12 31 where
    // 2 query bet365 x y =         ; create a function called bet365, which takes x, y
    // // create a .episcopal_bet365 method in the output class file with two parameters
    // // i.e.: ABS("bet365", SEQ(...
    // 3 let d = Beta x y in        ; d is a beta distribution, with alpha = x, and beta = y
    // // MEM(VAR("d")) <- new Beta(x, y);
    // 4 let bet = sample d in      ; bet = a sample from the beta distribution
    // // MEM(VAR("bet")) <- INVOKE("sample")
    // 5 observe (Flip d) = True in ; if it is not the case that Flip d = true, this function is invalid
    // 6 bet > 70%                  ; return true or false based on "bet > 70%"
    // // ... )) end SEQ and ABS

    /*
     * RUN(
     *    INIT(
     *       QUERY("bet365", ARGS(x, y), SEQ(
     *
     *       ))
     *    ),
     *    SEQ(CALL("bet365", ARGS(12, 31))
     * )
     */

    // e.g. something equivalent to the following would be constructed by compiler...

    public static void main(String[] args) {
        float[] result = bet365(12, 31);
        System.out.println(result[0]);
        System.out.println(result[1]);
    }

    public static float[] bet365(float x, float y) { // .method public episcopal_bet365(FF)F[]
        return builtin_dist_beta(x, y);
    } // .end method
}
