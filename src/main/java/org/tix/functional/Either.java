package org.tix.functional;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Created by tiran on 4/10/15.
 */
public abstract class Either<L, R> {

    private Either() {
    }

    /**
     * Checks whether current Either is left
     *
     * @return
     */
    public boolean isLeft(){
        return false;
    }

    /**
     * Checks whether current Either is right
     *
     * @return
     */
    public boolean isRight(){
        return false;
    }

    protected L getLeft() {
        throw new UnsupportedOperationException();
    }

    protected R getRight() {
        throw new UnsupportedOperationException();
    }

    /**
     * Applies the leftFunction if current Either is Left, or rightFunction if current Either is Right.
     *
     * @param leftFunction function to be applied if the value is Left.
     * @param rightFunction function to be applied if the value is Right.
     * @param <T> Return type.
     * @return Result of function application.
     */
    public <T> T fold(Function<L, T> leftFunction, Function<R, T> rightFunction) {
        return isLeft() ? leftFunction.apply(getLeft()) : rightFunction.apply(getRight());
    }

    /**
     * If current Either is Left returns a Right wrapping current Left value,
     * else return a Left wrapping current right value.
     *
     * @return swapped Either.
     */
    public Either<R, L> swap() {
        return isLeft() ? right(getLeft()) : left(getRight());
    }

    /**
     * Returns the right projection of the Either.
     *
     * @return right projection.
     */
    public RightProjection<L, R> right() {
        return new RightProjection<>(this);
    }

    /**
     * Returns the left projection of the Either.
     *
     * @return left projection.
     */
    public LeftProjection<L, R> left() {
        return new LeftProjection<>(this);
    }

    /**
     * Returns a Left Either wrapping value.
     *
     * @param value value to be wrapped.
     * @param <L> Left type.
     * @param <R> Right type.
     * @return new Either.
     */
    public static <L, R> Either<L, R> left(L value) {
        return new Left<>(value);
    }

    /**
     * Returns a Right Either wrapping value.
     *
     * @param value value to be wrapped.
     * @param <L> Left type.
     * @param <R> Right type.
     * @return new Either
     */
    public static <L, R> Either<L, R> right(R value) {
        return new Right<>(value);
    }

    /**
     * Returns a transformer function from left value to Left Either,
     * while setting the Right type as the type of clz.
     *
     * Useful in transforming values in streams.
     *
     * @param clz Class of right value
     * @param <L> Left type
     * @param <R> Right type
     * @return Either generator function.
     */
    public static <L, R> Function<L, Either<L, R>> left(Class<R> clz) {
        return Left::new;
    }

    /**
     * Returns a transformer function from right value to Right Either,
     * while setting the Left type as the type of clz.
     *
     * Useful in transforming values in streams.
     *
     * @param clz Class of left value
     * @param <L> Left type
     * @param <R> Right type
     * @return Either generator function.
     */
    public static <L, R> Function<R, Either<L, R>> right(Class<L> clz) {
        return Right::new;
    }

    /**
     * Transforms Optional values to Left Either.
     * Returns a Right Either wrapping rightDefault if the optional is empty
     *
     * @param left optional value.
     * @param rightDefault default value for right.
     * @param <L> Left type
     * @param <R> Right type
     * @return new Either.
     */
    public static <L, R> Either<L, R> toLeft(Optional<L> left, R rightDefault) {
        return left.isPresent() ? left(left.get()) : right(rightDefault);
    }

    /**
     * Transforms Optional values to Right Either.
     * Returns a Left Either wrapping leftDefault if the optional is empty
     *
     * @param right optional value.
     * @param leftDefault default value for left.
     * @param <L> Left type
     * @param <R> Right type
     * @return new Either.
     */
    public static <L, R> Either<L, R> toRight(Optional<R> right, L leftDefault) {
        return right.isPresent() ? right(right.get()) : left(leftDefault);
    }

    private static final class Left<L, R> extends Either<L, R> {

        private L value;

        public Left(L value) {
            this.value = value;
        }

        @Override
        public boolean isLeft() {
            return true;
        }

        @Override
        public L getLeft() {
            return value;
        }
    }

    private static final class Right<L, R> extends Either<L, R> {

        private R value;

        public Right(R value) {
            this.value = value;
        }

        @Override
        public boolean isRight() {
            return true;
        }

        @Override
        public R getRight() {
            return value;
        }
    }

    public static final class LeftProjection<L, R> {

        private final Either<L, R> that;

        private LeftProjection(Either<L, R> that) {
            this.that = that;
        }

        /**
         * Returns the left value of the Either.
         *
         * @return Left value.
         * @throws UnsupportedOperationException
         */
        public L get() {
            return that.getLeft();
        }

        /**
         * Returns the left value if Either is left, or defaultValue if Either is Right.
         *
         * @param defaultValue
         * @return Current left value or default value
         */
        public L getOrElse(L defaultValue) {
            return that.isLeft() ? get() : defaultValue;
        }

        /**
         * Transforms the left value to result of function if Either is left,
         * or return current value wrapped if the Either is Right
         *
         * @param function left transformation function
         * @param <T> result type.
         * @return new Either.
         */
        public <T> Either<T, R> map(Function<L, T> function) {
            return that.isLeft() ? left(function.apply(get())) : right(that.getRight());
        }

        /**
         * Replaces the Either if the current Either is left,
         * else return the current value wrapped iw the Either is Right
         *
         * @param function Left to Either transformation function.
         * @param <T> New left type
         * @return new Either.
         */
        public <T> Either<T, R> flatMap(Function<L, Either<T, R>> function) {
            return that.isLeft() ? function.apply(get()) : right(that.getRight());
        }

        /**
         * Consumes the left value if Either is left. Will not do any effect otherwise.
         * Then returns this Either to be chained.
         *
         * @param consumer left consumer function.
         * @return current Either.
         */
        public Either<L, R> peek(Consumer<L> consumer) {
            if (that.isLeft()) consumer.accept(get());
            return that;
        }

        /**
         * Consumes the left value if Either is left. Will not do any effect otherwise
         * @param consumer
         */
        public void forEach(Consumer<L> consumer) {
            if (that.isLeft()) {
                consumer.accept(get());
            }
        }

        /**
         * Returns true if this Either is Left and the value adhere to predicate.
         *
         * @param predicate predicate to check on value
         * @return Predicate result if left.
         */
        public boolean exists(Predicate<L> predicate) {
            return that.isLeft() && predicate.test(get());
        }

        /**
         * Returns filled Optional if current Either is a Left,
         * returns empty otherwise.
         *
         * @return Optional value.
         */
        public Optional<L> toOptional() {
            return that.isLeft() ? Optional.of(get()) : Optional.empty();
        }

        /**
         * Returns an Optional Either.
         * If current Either is Left and value is adhere to predicate, returns Filled Optional,
         * Returns empty otherwise.
         *
         * @param predicate predicate to check on value.
         * @return Optional containing current Either.
         */
        public Optional<Either<L, R>> filter(Predicate<L> predicate) {
            return Optional.of(that).filter(either -> either.isLeft() && predicate.test(either.getLeft()));
        }

        /**
         * Converts this Either to an Stream.
         * Stream will be filled if current value is left.
         *
         * @return Stream of left value.
         */
        public Stream<L> stream() {
            return that.isLeft() ? Stream.of(get()) : Stream.empty();
        }
    }

    public static final class RightProjection<L, R> {

        private final Either<L, R> that;

        private RightProjection(Either<L, R> that) {
            this.that = that;
        }

        /**
         * Returns the right value of the Either.
         *
         * @return Left value.
         * @throws UnsupportedOperationException
         */

        public R get() {
            return that.getRight();
        }

        /**
         * Returns the right value if Either is right, or defaultValue if Either is Right.
         *
         * @param defaultValue
         * @return Current right value or default value
         */

        public R getOrElse(R defaultValue) {
            return that.isRight() ? get() : defaultValue;
        }

                /**
         * Transforms the right value to result of function if Either is right,
         * or return current value wrapped if the Either is Right
         *
         * @param function right transformation function
         * @param <T> result type.
         * @return new Either.
         */

        public <T> Either<L, T> map(Function<R, T> function) {
            return that.isRight() ? right(function.apply(get())) : left(that.getLeft());
        }

                /**
         * Replaces the Either if the current Either is right,
         * else return the current value wrapped iw the Either is Right
         *
         * @param function Left to Either transformation function.
         * @param <T> New right type
         * @return new Either.
         */

        public <T> Either<L, T> flatMap(Function<R, Either<L, T>> function) {
            return that.isRight() ? function.apply(get()) : left(that.getLeft());
        }

                /**
         * Consumes the right value if Either is right. Will not do any effect otherwise.
         * Then returns this Either to be chained.
         *
         * @param consumer right consumer function.
         * @return current Either.
         */

        public Either<L, R> peek(Consumer<R> consumer) {
            if (that.isRight()) consumer.accept(get());
            return that;
        }

                /**
         * Consumes the right value if Either is right. Will not do any effect otherwise
         * @param consumer
         */

        public void forEach(Consumer<R> consumer) {
            if (that.isRight()) {
                consumer.accept(get());
            }
        }

                /**
         * Returns true if this Either is Left and the value adhere to predicate.
         *
         * @param predicate predicate to check on value
         * @return Predicate result if right.
         */

        public boolean exists(Predicate<R> predicate) {
            return that.isRight() && predicate.test(get());
        }

                /**
         * Returns filled Optional if current Either is a Left,
         * returns empty otherwise.
         *
         * @return Optional value.
         */

        public Optional<R> toOptional() {
            return that.isRight() ? Optional.of(get()) : Optional.empty();
        }

                /**
         * Returns an Optional Either.
         * If current Either is Left and value is adhere to predicate, returns Filled Optional,
         * Returns empty otherwise.
         *
         * @param predicate predicate to check on value.
         * @return Optional containing current Either.
         */

        public Optional<Either<L, R>> filter(Predicate<R> predicate) {
            return Optional.of(that).filter(either -> either.isRight() && predicate.test(either.getRight()));
        }

                /**
         * Converts this Either to an Stream.
         * Stream will be filled if current value is right.
         *
         * @return Stream of right value.
         */

        public Stream<R> stream() {
            return that.isRight() ? Stream.of(get()) : Stream.empty();
        }
    }
}
