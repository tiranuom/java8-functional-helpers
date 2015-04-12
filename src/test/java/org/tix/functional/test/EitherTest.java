package org.tix.functional.test;

import org.testng.annotations.Test;
import org.tix.functional.Either;
import org.tix.functional.Exceptions;

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

    @Test
    public void testException() throws Exception {

        Either<UnsupportedOperationException, Integer> either = Exceptions
                .catching(UnsupportedOperationException.class)
                .either(this::throwException);

        System.out.println(either.isLeft());
    }

    @Test
    public void testExceptionPerformance() throws Exception {

        Either<UnsupportedOperationException, Integer> either = null;
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            either = catchF();
        }
        System.out.println(System.currentTimeMillis() - startTime);
        System.out.println(either);

        startTime = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            either = Exceptions
                    .catching(UnsupportedOperationException.class)
                    .either(this::throwException);
        }
        System.out.println(System.currentTimeMillis() - startTime);
        System.out.println(either);
    }

    public Either<UnsupportedOperationException, Integer> catchF () {
        try {
            return Either.right(throwException());
        } catch (UnsupportedOperationException e) {
            return Either.left(e);
        }
    }

    public int throwException() {
        throw new UnsupportedOperationException();
    }
}