package polynomials;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface PolynomialMultiplicationStrategy {

    static int[] setup(int rank) {
        return new int[rank];
    }

    Polynomial mul(Polynomial lhs, Polynomial rhs);
}

