package polynomials;

public class SequentialKaratsubaPolynomialMultiplication implements PolynomialMultiplicationStrategy {

    private static int getSmallestPowOf2LargerThan(int num) {
        int result = 1;
        while (result < num) {
            result <<= 1;
        }
        return result;
    }

    protected Polynomial karatsuba(Polynomial lhs, Polynomial rhs) {
        final var realRank = Math.max(lhs.rank(), rhs.rank());
        if (realRank <= 1) {
            return new Polynomial(lhs.first() * rhs.first());
        }
        final var rank = getSmallestPowOf2LargerThan(realRank);
        final var halfRank = rank / 2;

        final var lowLhs = lhs.slice(0, halfRank);
        final var highLhs = lhs.slice(halfRank, rank);
        final var lowRhs = rhs.slice(0, halfRank);
        final var highRhs = rhs.slice(halfRank, rank);

        final var lowProduct = karatsuba(lowLhs, lowRhs);
        final var highProduct = karatsuba(highLhs, highRhs);

        final var lhsSum = lowLhs.add(highLhs);
        final var rhsSum = lowRhs.add(highRhs);

        final var sumProduct = karatsuba(lhsSum, rhsSum);

        final var middleProduct = sumProduct.sub(lowProduct).sub(highProduct);

        return Polynomial.merge(lowProduct, middleProduct, highProduct, rank);
    }

    @Override
    public Polynomial mul(Polynomial lhs, Polynomial rhs) {
        return karatsuba(lhs, rhs);
    }
}
