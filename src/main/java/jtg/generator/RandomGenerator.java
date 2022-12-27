package jtg.generator;

import java.util.Random;

public abstract class RandomGenerator<T> {

    static private Random random = new Random();

    protected static double randomFloatFromRange(double lowerBound, double upperBound) {
        double r = random.nextDouble();
        r = r * (upperBound - lowerBound) + lowerBound;
        if (r >= upperBound)
            r = Math.nextDown(upperBound);
        return r;
    }

    protected static long randomIntegerFromRange(long lowerBound, long upperBound) {
        return Math.round(randomFloatFromRange(lowerBound, upperBound));
    }

    protected abstract Object generate();

    protected abstract T generate(Object... params);
}
