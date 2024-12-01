import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ParallelNSquaredPolynomialMultiplication implements PolynomialMultiplicationStrategy {
    @Override
    public Polynomial mul(Polynomial lhs, Polynomial rhs) {
        return mul(lhs, rhs, Runtime.getRuntime().availableProcessors());
    }

    public Polynomial mul(Polynomial lhs, Polynomial rhs, int threadPoolSize) {
        final var resultCoefficients = PolynomialMultiplicationStrategy.setup(lhs.rank() + rhs.rank());
        final var locks = new Object[resultCoefficients.size()];
        for (var i = 0; i < locks.length; i++) {
            locks[i] = new Object();
        }

        final var executor = Executors.newFixedThreadPool(threadPoolSize);

        for (var lhsId = 0; lhsId < lhs.rank(); lhsId++) {
            for (var rhsId = 0; rhsId < rhs.rank(); rhsId++) {
                final var resultId = lhsId + rhsId;
                final var product = lhs.get(lhsId) * rhs.get(rhsId);

                executor.submit(() -> {
                    synchronized (locks[resultId]) {
                        final var prev = resultCoefficients.get(resultId);
                        resultCoefficients.set(resultId, prev + product);
                    }
                });
            }
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
