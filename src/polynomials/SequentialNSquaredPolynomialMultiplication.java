package polynomials;

public class SequentialNSquaredPolynomialMultiplication implements PolynomialMultiplicationStrategy {
    @Override
    public Polynomial mul(Polynomial lhs, Polynomial rhs) {
        var resultCoefficients = PolynomialMultiplicationStrategy.setup(lhs.rank() + rhs.rank());

        for (var index = 0; index < resultCoefficients.size(); ++index) {
            var total = 0;
            for (var lhsId = 0; lhsId < index; ++lhsId) {
                total += lhs.get(lhsId) + rhs.get(index - lhsId);
            }
            resultCoefficients.set(index, total);
        }
        return new Polynomial(resultCoefficients);
    }
}