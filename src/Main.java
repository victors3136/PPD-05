import polynomials.*;

import java.util.Objects;

/*
    ===========WARMUP===========
    Warming up the JIT with ranks 6891 and 7886 [ 1/41]
    Time: 1768.2863 ms
    Warming up the JIT with ranks 7601 and 6673 [ 2/41]
    Time: 995.976 ms
    Warming up the JIT with ranks 7208 and 7154 [ 3/41]
    Time: 751.5636 ms
    Warming up the JIT with ranks 7357 and 6902 [ 4/41]
    Time: 737.0528 ms
    Warming up the JIT with ranks 7602 and 6588 [ 5/41]
    Time: 693.2435 ms
    Warming up the JIT with ranks 6977 and 7475 [ 6/41]
    Time: 573.4115 ms
    Warming up the JIT with ranks 6556 and 6662 [ 7/41]
    Time: 491.2501 ms
    Warming up the JIT with ranks 7869 and 6566 [ 8/41]
    Time: 611.0257 ms
    Warming up the JIT with ranks 7140 and 7012 [ 9/41]
    Time: 539.5424 ms
    Warming up the JIT with ranks 6823 and 7575 [ 10/41]
    Time: 534.7763 ms
    Warming up the JIT with ranks 7214 and 6715 [ 11/41]
    Time: 495.517 ms
    Warming up the JIT with ranks 7079 and 7148 [ 12/41]
    Time: 499.5891 ms
    Warming up the JIT with ranks 7861 and 6822 [ 13/41]
    Time: 585.4862 ms
    Warming up the JIT with ranks 6561 and 7228 [ 14/41]
    Time: 540.2671 ms
    Warming up the JIT with ranks 6692 and 7130 [ 15/41]
    Time: 510.5087 ms
    Warming up the JIT with ranks 6874 and 6680 [ 16/41]
    Time: 509.5258 ms
    Warming up the JIT with ranks 6706 and 7012 [ 17/41]
    Time: 497.0875 ms
    Warming up the JIT with ranks 7587 and 6720 [ 18/41]
    Time: 570.14 ms
    Warming up the JIT with ranks 7816 and 6747 [ 19/41]
    Time: 541.5953 ms
    Warming up the JIT with ranks 6900 and 6824 [ 20/41]
    Time: 528.6266 ms
    Warming up the JIT with ranks 7533 and 7453 [ 21/41]
    Time: 536.4114 ms
    Warming up the JIT with ranks 6979 and 7248 [ 22/41]
    Time: 546.3791 ms
    Warming up the JIT with ranks 6993 and 6660 [ 23/41]
    Time: 498.2673 ms
    Warming up the JIT with ranks 7831 and 6617 [ 24/41]
    Time: 565.6515 ms
    Warming up the JIT with ranks 6889 and 7246 [ 25/41]
    Time: 489.8456 ms
    Warming up the JIT with ranks 7037 and 7695 [ 26/41]
    Time: 570.8878 ms
    Warming up the JIT with ranks 7900 and 7704 [ 27/41]
    Time: 561.6293 ms
    Warming up the JIT with ranks 7681 and 7371 [ 28/41]
    Time: 529.8676 ms
    Warming up the JIT with ranks 7170 and 7189 [ 29/41]
    Time: 515.0465 ms
    Warming up the JIT with ranks 7354 and 6863 [ 30/41]
    Time: 539.3003 ms
    Warming up the JIT with ranks 7747 and 7808 [ 31/41]
    Time: 565.2445 ms
    Warming up the JIT with ranks 6795 and 7272 [ 32/41]
    Time: 509.0036 ms
    Warming up the JIT with ranks 7204 and 7908 [ 33/41]
    Time: 577.6566 ms
    Warming up the JIT with ranks 7414 and 7538 [ 34/41]
    Time: 546.6125 ms
    Warming up the JIT with ranks 7865 and 7866 [ 35/41]
    Time: 591.9184 ms
    Warming up the JIT with ranks 7669 and 6627 [ 36/41]
    Time: 538.5047 ms
    Warming up the JIT with ranks 7281 and 7063 [ 37/41]
    Time: 528.9693 ms
    Warming up the JIT with ranks 7379 and 6881 [ 38/41]
    Time: 552.8226 ms
    Warming up the JIT with ranks 7291 and 6628 [ 39/41]
    Time: 520.0742 ms
    Warming up the JIT with ranks 6952 and 7062 [ 40/41]
    Time: 510.8584 ms
    Warming up the JIT with ranks 7786 and 7489 [ 41/41]
    Time: 557.2495 ms

    ===========BENCHMARK===========
    Started Standard Sequential with ranks 7883 and 7901
    Time: 119.446 ms
    Started Standard Parallel with ranks 7883 and 7901
    Time: 33.6514 ms
    Started Karatsuba Sequential with ranks 7883 and 7901
    Time: 268.9361 ms
    Started Karatsuba Parallel with ranks 7883 and 7901
    Time: 265.6716 ms

    ===========CHECK===========
    Parallel Simple algorithm produced the same result as the Sequential Simple
    Sequential Karatsuba algorithm produced the same result as the Sequential Simple
    Parallel Simple algorithm produced the same result as the Sequential Simple

 */
public class Main {
    private static Polynomial multiplyWrapper(
            Polynomial lhs,
            Polynomial rhs,
            PolynomialMultiplicationStrategy strategy,
            String type) {
        long startTime = System.nanoTime();
        System.out.println("Started " + type + " with ranks "
                + lhs.rank() + " and " + rhs.rank());
        final var sequentialSimpleResult = lhs.mul(rhs, strategy);
        long endTime = System.nanoTime();
        System.out.println("Time: " + (endTime - startTime) / 1e6 + " ms");
        return sequentialSimpleResult;
    }

    private static final int lhsRank = 7883;
    private static final int rhsRank = 7901;
    private static final int warmupUB = 41;
    private static final int warmupLhsLB = 6553;
    private static final int warmupLhsUB = 7907;
    private static final int warmupRhsLB = 6563;
    private static final int warmupRhsUB = 7919;

    public static void main(String[] args) {
        // Using prime numbers to screw up caching

        // warm up the JIT
        System.out.println("===========WARMUP===========");
        for (var index = 0; index < warmupUB; ++index) {
            long startTime = System.nanoTime();
            final var warmupLhsRank = (int) (Math.random() * (warmupLhsUB - warmupLhsLB) + warmupLhsLB);
            final var warmupRhsRank = (int) (Math.random() * (warmupRhsUB - warmupRhsLB) + warmupRhsLB);
            System.out.println("Warming up the JIT with ranks " + warmupLhsRank + " and " + warmupRhsRank + " [ "
                    + (index + 1) + "/" + warmupUB + "]");
            Polynomial.getRandom(warmupLhsRank).mul(Polynomial.getRandom(warmupRhsRank),
                    new SequentialNSquaredPolynomialMultiplication());
            Polynomial.getRandom(warmupLhsRank).mul(Polynomial.getRandom(warmupRhsRank),
                    new ParallelNSquaredPolynomialMultiplication());
            Polynomial.getRandom(warmupLhsRank).mul(Polynomial.getRandom(warmupRhsRank),
                    new SequentialKaratsubaPolynomialMultiplication());
            Polynomial.getRandom(warmupLhsRank).mul(Polynomial.getRandom(warmupRhsRank),
                    new ParallelKaratsubaPolynomialMultiplication());
            long endTime = System.nanoTime();
            System.out.println("Time: " + (endTime - startTime) / 1e6 + " ms");
        }
        // Benchmark
        System.out.println("\n===========BENCHMARK===========");
        final Polynomial sequentialSimpleResult;
        final Polynomial parallelSimpleResult;
        final Polynomial sequentialKaratsubaResult;
        final Polynomial parallelKaratsubaResult;
        {
            final var leftHandSide = Polynomial.getRandom(lhsRank);
            final var rightHandSize = Polynomial.getRandom(rhsRank);
            sequentialSimpleResult = multiplyWrapper(
                    leftHandSide,
                    rightHandSize,
                    new SequentialNSquaredPolynomialMultiplication(),
                    "Standard Sequential");
        }
        {
            final var leftHandSide = Polynomial.getRandom(lhsRank);
            final var rightHandSize = Polynomial.getRandom(rhsRank);
            parallelSimpleResult = multiplyWrapper(
                    leftHandSide,
                    rightHandSize,
                    new ParallelNSquaredPolynomialMultiplication(),
                    "Standard Parallel");
        }
        {
            final var leftHandSide = Polynomial.getRandom(lhsRank);
            final var rightHandSize = Polynomial.getRandom(rhsRank);
            sequentialKaratsubaResult = multiplyWrapper(
                    leftHandSide,
                    rightHandSize,
                    new SequentialKaratsubaPolynomialMultiplication(),
                    "Karatsuba Sequential");
        }
        {
            final var leftHandSide = Polynomial.getRandom(lhsRank);
            final var rightHandSize = Polynomial.getRandom(rhsRank);
            parallelKaratsubaResult = multiplyWrapper(
                    leftHandSide,
                    rightHandSize,
                    new ParallelKaratsubaPolynomialMultiplication(),
                    "Karatsuba Parallel");

        }
        // Verify Results :)
        System.out.println("\n===========CHECK===========");
        final var size = sequentialSimpleResult.rank();
        assert size == parallelSimpleResult.rank();
        for (var index = 0; index < size; ++index) {
            assert Objects.equals(parallelSimpleResult.get(index), sequentialSimpleResult.get(index));
        }
        System.out.println("Parallel Simple algorithm produced the same result as the Sequential Simple");
        assert size == sequentialKaratsubaResult.rank();
        for (var index = 0; index < size; ++index) {
            assert Objects.equals(sequentialKaratsubaResult.get(index), sequentialSimpleResult.get(index));
        }
        System.out.println("Sequential Karatsuba algorithm produced the same result as the Sequential Simple");
        assert size == parallelKaratsubaResult.rank();
        for (var index = 0; index < size; ++index) {
            assert Objects.equals(parallelKaratsubaResult.get(index), sequentialSimpleResult.get(index));
        }
        System.out.println("Parallel Simple algorithm produced the same result as the Sequential Simple");
    }
}