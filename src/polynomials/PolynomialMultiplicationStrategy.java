package polynomials;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface PolynomialMultiplicationStrategy {

    static List<Integer> setup(int rank) {
        return new ArrayList<>(Collections.nCopies(rank, 0));
    }

    Polynomial mul(Polynomial lhs, Polynomial rhs);
}

