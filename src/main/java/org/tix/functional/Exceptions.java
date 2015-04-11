package org.tix.functional;

import java.util.Optional;
import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;
import java.util.function.LongSupplier;
import java.util.function.Supplier;

/**
 * Created by tiran on 4/11/15.
 */
public class Exceptions {

    public static <T extends Throwable> EitherGenerator<T> catching(Class<T> throwable) {
        return new EitherGenerator<>(throwable);
    }

    public static class EitherGenerator<T extends Throwable> {
        private Class<T> throwable;

        private EitherGenerator(Class<T> throwable) {
            this.throwable = throwable;
        }

        public <R> Either<T, R> either(Supplier<R> supplier) {
            try {
                return Either.right(supplier.get());
            } catch (Throwable t) {
                if (t.getClass().equals(throwable)) {
                    return Either.left((T) t);
                }
                throw t;
            }
        }

        public Either<T, Integer> either(IntSupplier supplier) {
            try {
                return Either.right(supplier.getAsInt());
            } catch (Throwable t) {
                if (t.getClass().equals(throwable)) {
                    return Either.left((T) t);
                }
                throw t;
            }
        }
        public Either<T, Long> either(LongSupplier supplier) {
            try {
                return Either.right(supplier.getAsLong());
            } catch (Throwable t) {
                if (t.getClass().equals(throwable)) {
                    return Either.left((T) t);
                }
                throw t;
            }
        }

        public Either<T, Double> either(DoubleSupplier supplier) {
            try {
                return Either.right(supplier.getAsDouble());
            } catch (Throwable t) {
                if (t.getClass().equals(throwable)) {
                    return Either.left((T) t);
                }
                throw t;
            }
        }

        public <R> Optional<R> optional(Supplier<R> supplier) {
            try {
                return Optional.of(supplier.get());
            } catch (Throwable t) {
                if (t.getClass().equals(throwable)) {
                    return Optional.empty();
                }
                throw t;
            }
        }
    }
}
