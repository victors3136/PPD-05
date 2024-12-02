package polynomials;

public class SequentialNSquaredPolynomialMultiplication implements PolynomialMultiplicationStrategy {
    @Override
    public Polynomial mul(Polynomial lhs, Polynomial rhs) {
        final var resultRank = lhs.rank() + rhs.rank();
        final var resultCoefficients = PolynomialMultiplicationStrategy.setup(resultRank);
        for (var index = 0; index < resultRank; ++index) {
            var total = 0;
            for (var lhsId = 0; lhsId <= index; ++lhsId) {
                final var rhsId = index - lhsId;
                total += lhs.get(lhsId) * rhs.get(rhsId);
            }
            resultCoefficients[index] = total;
        }
        return new Polynomial(resultCoefficients);
    }
}