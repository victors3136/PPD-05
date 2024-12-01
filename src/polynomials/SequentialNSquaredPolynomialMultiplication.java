public class SequentialNSquaredPolynomialMultiplication implements PolynomialMultiplicationStrategy {
    @Override
    public Polynomial mul(Polynomial lhs, Polynomial rhs) {
        var resultCoefficients = PolynomialMultiplicationStrategy.setup(lhs.rank() + rhs.rank());
        for (var lhsId = 0; lhsId < lhs.rank(); ++lhsId) {
            for (var rhsId = 0; rhsId < rhs.rank(); ++rhsId) {
                resultCoefficients.add(lhsId + rhsId, lhs.get(lhsId) * rhs.get(rhsId));
            }
        }
        return new Polynomial(resultCoefficients);
    }
}