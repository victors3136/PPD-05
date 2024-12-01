package polynomials;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public record Polynomial(int[] coefficients, int rank) {
    public Polynomial {
        assert coefficients.length == rank;
    }

    public Polynomial(List<Integer> coefficients) {
        this(coefficients.stream().mapToInt(Integer::intValue).toArray(), coefficients.size());
    }

    public Polynomial(int... coefficients) {
        this(coefficients, coefficients.length);
    }

    public Integer get(int index) {
        assert index >= 0;
        return rank > index ? coefficients[index] : 0;
    }

    public Integer first() {
        return rank > 0 ? coefficients[0] : 0;
    }

    public Polynomial add(Polynomial other) {
        final var resultRank = Math.max(this.rank, other.rank);
        final var resultCoefficients = new int[resultRank];
        for (int index = 0; index < resultRank; ++index) {
            var partialSum = this.get(index) + other.get(index);
            resultCoefficients[index] = partialSum;
        }
        return new Polynomial(resultCoefficients);
    }

    public Polynomial slice(int start, int end) {
        assert start >= 0;
        assert end >= start;

        final var size = end - start;
        final var dest = new int[size];

        final var truncEnd = Math.min(end, rank);
        final var copySize = Math.max(0, truncEnd - start);
        System.arraycopy(coefficients, start, dest, 0, copySize);
        return new Polynomial(dest);
    }

    public Polynomial sub(Polynomial other) {
        final var resultRank = Math.max(this.rank, other.rank);
        final var resultCoefficients = new int[resultRank];
        for (int index = 0; index < resultRank; ++index) {
            var partialSum = this.get(index) - other.get(index);
            resultCoefficients[index] = partialSum;
        }
        return new Polynomial(resultCoefficients);
    }

    public Polynomial mul(Polynomial other, PolynomialMultiplicationStrategy strategy) {
        return strategy.mul(this, other);
    }

    @Override
    public String toString() {
        var str = IntStream.range(0, rank)
                .filter(i -> coefficients[i] != 0)
                .mapToObj(index -> (index > 0 ? "+ " : "")
                        + coefficients[index]
                        + (index == 0 ? "" : " X" + (index != 1 ? "^" + (index + 1) : "")) + " ")
                .reduce("", (s1, s2) -> s1 + s2);
        return !str.isEmpty() ? str : "0 ";
    }

    static Polynomial merge(Polynomial low, Polynomial middle, Polynomial high, int rank) {
        final var result = new ArrayList<>(Collections.nCopies(2 * rank - 1, 0));
        for (var index = 0; index < low.rank(); ++index) {
            result.set(index, result.get(index) + low.get(index));
            result.set(index + rank / 2, result.get(index + rank / 2) + middle.get(index));
            result.set(index + rank, result.get(index + rank) + high.get(index));
        }
        return new Polynomial(result);
    }

    public static Polynomial getRandom(int size) {
        assert size > 0;
        final var base = new int[size];
        final var rand = new Random();
        for (var i = 0; i < size; i++) {
            base[i] = rand.nextInt(-100, 101);
        }
        return new Polynomial(base);
    }
}
