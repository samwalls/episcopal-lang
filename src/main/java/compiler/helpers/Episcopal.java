package compiler.helpers;

import java.util.Random;

/**
 * This class represents a set of static, built-in functions for use within the episcopal compiler. All episcopal programs
 * will depend on the class file generated for this.
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

    public static float[] builtin_dist_binomial(float[] probability) {
        return new float[] {
                new Random().nextFloat() < probability[0] ? 1f : 0f,
                Math.min(Math.max(0, probability[0] * probability[1]), 1) // multiply the probability value, by the probability with which it takes that value
        };
    }

    public static float[] builtin_dist_normal(float[] mean, float[] standardDeviation) {
        float v = (float)new Random().nextGaussian() * standardDeviation[0] + mean[0];
        float variance = (float) Math.pow(standardDeviation[0], 2);
        float probability = (float) (Math.exp(-Math.pow((v - mean[0]), 2)/(2f* variance)) / Math.sqrt(2f * Math.PI * variance));
        return new float[] {
                v,
                Math.min(Math.max(0, probability * mean[1] * mean[1]), 1) //multiply by the input probabilities
        };
    }

    public static float[] builtin_dist_beta(float[] a, float[] b) {
        // uniform random between 0 and 1
        float x = new Random().nextFloat();
        // calculate the probability with which this value could occur in the distribution
        float probability = (float) (gamma(a[0] + b[0]) / (gamma(a[0]) + gamma(b[0])) * Math.pow(1 - x, b[0] - 1) * Math.pow(x, a[0] - 1));
        return new float[] {
                x,
                Math.min(Math.max(0, probability * a[1] * b[1]), 1) // multiply the input probabilities
        };
    }

    public static void builtin_observe(float[] a) {
        // if the value is not "true" i.e. 1
        if (a[0] < 1f)
            throw new RuntimeException("program is takes on invalid value " + a[0] + " with probability " + a[1]);
    }

    public static float builtin_lt(float a, float b) {
        return a < b ? 1f : 0f;
    }

    public static float builtin_gt(float a, float b) {
        return a > b ? 1f : 0f;
    }

    public static float builtin_eq(float a, float b) {
        return a == b ? 1f : 0f;
    }

    public static float builtin_or(float a, float b) {
        return a == 1f || b == 1f ? 1f : 0f;
    }

    public static float builtin_and(float a, float b) {
        return a == 1f && b == 1f ? 1f : 0f;
    }

    public static float[] builtin_finish(float[] a) {
        System.out.println("deducted value " + a[0] + " with probability " + (a[1] * 100) + "%");
        return a;
    }
}
