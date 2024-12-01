package polynomials;

import java.util.concurrent.*;

public class ParallelKaratsubaPolynomialMultiplication implements PolynomialMultiplicationStrategy {
    private final ForkJoinPool pool;

    public ParallelKaratsubaPolynomialMultiplication(int threadPoolSize) {
        this.pool = new ForkJoinPool();
    }

    public ParallelKaratsubaPolynomialMultiplication() {
        this(Runtime.getRuntime().availableProcessors());
    }

    protected Polynomial karatsuba(Polynomial lhs, Polynomial rhs) throws ExecutionException, InterruptedException {
        final var rank = Math.max(lhs.rank(), rhs.rank());

        if (rank <= 1) {
            return new Polynomial(lhs.first() * rhs.first());
        }


        final var halfRank = rank / 2;

        final var lowLhs = lhs.slice(0, halfRank);
        final var highLhs = lhs.slice(halfRank, rank);
        final var lowRhs = rhs.slice(0, halfRank);
        final var highRhs = rhs.slice(halfRank, rank);

        final var lowProductFuture = pool.submit(() -> karatsuba(lowLhs, lowRhs));
        final var highProductFuture = pool.submit(() -> karatsuba(highLhs, highRhs));
        final var sumProductFuture = pool.submit(() -> karatsuba(lowLhs.add(highLhs), lowRhs.add(highRhs)));

        final var lowProduct = lowProductFuture.get();
        final var highProduct = highProductFuture.get();
        final var middleProduct = sumProductFuture.get()
                .sub(lowProduct)
                .sub(highProduct);

        return Polynomial.merge(lowProduct, middleProduct, highProduct, rank);
    }

    @Override
    public Polynomial mul(Polynomial lhs, Polynomial rhs) {
        final Polynomial result;
        try {
            result = karatsuba(lhs, rhs);
            pool.shutdown();
            // Just crash if we get serious issues :)
            assert pool.awaitTermination(1, TimeUnit.MINUTES);
            return result;
        } catch (ExecutionException | InterruptedException e) {
            System.out.println(e.getMessage());
            assert false;
            return null;
        } finally {
            if (!pool.isShutdown()) {
                pool.shutdown();
            }
        }
    }

}
