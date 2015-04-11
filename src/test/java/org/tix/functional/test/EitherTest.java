package org.tix.functional.test;

import org.testng.annotations.Test;
import org.tix.functional.Either;

import static org.testng.Assert.assertEquals;

/**
 * Created by tiran on 4/11/15.
 */
public class EitherTest {

    @Test
    public void testEither() throws Exception {
        Either<String, Integer> error = Either.left("Error");
        Either<String, Integer> right = Either.right(1);

        Either<String, Integer> leftFlatMapped = error.left().flatMap(t -> right);
        assertEquals(right, leftFlatMapped);
    }
}