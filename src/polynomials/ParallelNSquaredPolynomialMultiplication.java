package polynomials;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ParallelNSquaredPolynomialMultiplication implements PolynomialMultiplicationStrategy {
    @Override
    public Polynomial mul(Polynomial lhs, Polynomial rhs) {
        return mul(lhs, rhs, Runtime.getRuntime().availableProcessors());
    }

    public Polynomial mul(Polynomial lhs, Polynomial rhs, int threadPoolSize) {
        final var resultCoefficients = PolynomialMultiplicationStrategy.setup(lhs.rank() + rhs.rank());
        final var executor = Executors.newFixedThreadPool(threadPoolSize);
        for (var index = 0; index < resultCoefficients.size(); ++index) {
            final var threadId = index;
            executor.submit(() -> {
                var total = 0;
                for (var lhsId = 0; lhsId < threadId; ++lhsId) {
                    total += lhs.get(lhsId) * rhs.get(threadId - lhsId);
                }
                resultCoefficients.set(threadId, total);

            });
        }

        executor.shutdown();
        try {
            assert executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            assert false;
        }

        return new Polynomial(resultCoefficients);

    }
}
