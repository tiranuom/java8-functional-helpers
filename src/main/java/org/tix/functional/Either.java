package org.tix.functional;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by tiran on 4/10/15.
 */
public abstract class Either<L, R> {

    private Either() {
    }

    public boolean isLeft(){
        return false;
    }

    public boolean isRight(){
        return false;
    }

    protected L getLeft() {
        throw new UnsupportedOperationException();
    }

    protected R getRight() {
        throw new UnsupportedOperationException();
    }

    protected L getOrElseLeft(L defaultValue) {
        return isLeft() ? getLeft() : defaultValue;
    }

    protected R getOrElseRight(R defaultValue) {
        return isRight() ? getRight() : defaultValue;
    }

    protected Optional<Either<L, R>> filterLeft(Predicate<L> predicate) {
        return Optional.of(this).filter(either -> either.existsLeft(predicate));
    }

    protected Optional<Either<L, R>> filterRight(Predicate<R> predicate) {
        return Optional.of(this).filter(either -> either.existsRight(predicate));
    }

    protected <T> Either<T, R> mapLeft(Function<L, T> function) {
        return isLeft() ? left(function.apply(getLeft())) : right(getRight());
    }

    protected <T> Either<L, T> mapRight(Function<R, T> function) {
        return isRight() ? right(function.apply(getRight())) : left(getLeft());
    }

    protected <T> Either<T, R> flatMapLeft(Function<L, Either<T, R>> function) {
        return isLeft() ? function.apply(getLeft()) : right(getRight());
    }

    protected <T> Either<L, T> flatMapRight(Function<R, Either<L, T>> function) {
        return isRight() ? function.apply(getRight()) : left(getLeft());
    }

    protected Either<L, R> peekLeft(Consumer<L> consumer) {
        if (isLeft()) consumer.accept(getLeft());
        return this;
    }

    protected Either<L, R> peekRight(Consumer<R> consumer) {
        if (isRight()) consumer.accept(getRight());
        return this;
    }

    protected void forEachLeft(Consumer<L> consumer) {
        if (isLeft()) {
            consumer.accept(getLeft());
        }
    }

    protected void forEachRight(Consumer<R> consumer) {
        if (isRight()) {
            consumer.accept(getRight());
        }
    }

    protected boolean existsLeft(Predicate<L> predicate) {
        return isLeft() && predicate.test(getLeft());
    }

    protected boolean existsRight(Predicate<R> predicate) {
        return isRight() && predicate.test(getRight());
    }

    protected Optional<L> toOptionalLeft() {
        return isLeft() ? Optional.of(getLeft()) : Optional.empty();
    }

    protected Optional<R> toOptionalRight() {
        return isRight() ? Optional.of(getRight()) : Optional.empty();
    }

    public <T> T fold(Function<L, T> ltFunction, Function<R, T> rtFunction) {
        return isLeft() ? ltFunction.apply(getLeft()) : rtFunction.apply(getRight());
    }

    public Either<R, L> swap() {
        return isLeft() ? right(getLeft()) : left(getRight());
    }

    public RightProjection<L, R> right() {
        return new RightProjection<>(this);
    }

    public LeftProjection<L, R> left() {
        return new LeftProjection<>(this);
    }

    public static <L, R> Either<L, R> left(L value) {
        return new Left<>(value);
    }

    public static <L, R> Either<L, R> right(R value) {
        return new Right<>(value);
    }

    public static <L, R> Either<L, R> left(L value, Class<R> clz) {
        return new Left<>(value);
    }

    public static <L, R> Either<L, R> right(R value, Class<L> clz) {
        return new Right<>(value);
    }

    public static <L, R> Either<L, R> toLeft(Optional<L> left, R rightDefault) {
        return left.isPresent() ? left(left.get()) : right(rightDefault);
    }

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

        public L get() {
            return that.getLeft();
        }

        public L getOrElse(L defaultValue) {
            return that.getOrElseLeft(defaultValue);
        }

        public <T> Either<T, R> map(Function<L, T> function) {
            return that.mapLeft(function);
        }

        public <T> Either<T, R> flatMap(Function<L, Either<T, R>> function) {
            return that.flatMapLeft(function);
        }

        public Either<L, R> peek(Consumer<L> consumer) {
            that.peekLeft(consumer);
            return that;
        }

        public void forEach(Consumer<L> consumer) {
            that.forEachLeft(consumer);
        }

        public boolean exists(Predicate<L> predicate) {
            return that.existsLeft(predicate);
        }

        public Optional<L> toOptional() {
            return that.toOptionalLeft();
        }

        public Optional<Either<L, R>> filter(Predicate<L> predicate) {
            return that.filterLeft(predicate);
        }
    }

    public static final class RightProjection<L, R> {

        private final Either<L, R> that;

        private RightProjection(Either<L, R> that) {
            this.that = that;
        }

        public R get() {
            return that.getRight();
        }

        public R getOrElse(R defaultValue) {
            return that.getOrElseRight(defaultValue);
        }

        public <T> Either<L, T> map(Function<R, T> function) {
            return that.mapRight(function);
        }

        public <T> Either<L, T> flatMap(Function<R, Either<L, T>> function) {
            return that.flatMapRight(function);
        }

        public Either<L, R> peek(Consumer<R> consumer) {
            that.peekRight(consumer);
            return that;
        }

        public void forEach(Consumer<R> consumer) {
            that.forEachRight(consumer);
        }

        public boolean exists(Predicate<R> predicate) {
            return that.existsRight(predicate);
        }

        public Optional<R> toOptional() {
            return that.toOptionalRight();
        }

        public Optional<Either<L, R>> filter(Predicate<R> predicate) {
            return that.filterRight(predicate);
        }
    }
}
