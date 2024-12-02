package polynomials;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ParallelNSquaredPolynomialMultiplication implements PolynomialMultiplicationStrategy {
    @Override
    public Polynomial mul(Polynomial lhs, Polynomial rhs) {
        return mul(lhs, rhs, Runtime.getRuntime().availableProcessors());
    }

    public Polynomial mul(Polynomial lhs, Polynomial rhs, int threadPoolSize) {
        final var executor = Executors.newFixedThreadPool(threadPoolSize);
        final List<Future<?>> futures = new ArrayList<>();
        final var resultRank = lhs.rank() + rhs.rank();
        final var resultCoefficients = PolynomialMultiplicationStrategy.setup(resultRank);
        for (var index = 0; index < resultRank; ++index) {
            final var threadId = index;
            futures.add(executor.submit(() -> {
                var total = 0;
                for (var lhsId = 0; lhsId <= threadId; ++lhsId) {
                    final var rhsId = threadId - lhsId;
                    total += lhs.get(lhsId) * rhs.get(rhsId);
                }
                resultCoefficients[threadId] = total;

            }));
        }

        executor.shutdown();
        try {
            for (var future : futures) {
                future.get();
            }
        } catch (InterruptedException | ExecutionException ignored) {
        }

        return new Polynomial(resultCoefficients);

    }
}
