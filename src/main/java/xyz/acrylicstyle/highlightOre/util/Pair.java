package xyz.acrylicstyle.highlightOre.util;

import java.util.Objects;

public class Pair<A, B, C> {
    private final A a;
    private final B b;
    private final C c;

    public Pair(A a, B b, C c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public A getA() {
        return a;
    }

    public B getB() {
        return b;
    }

    public C getC() {
        return c;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?, ?> pair = (Pair<?, ?, ?>) o;
        return Objects.equals(a, pair.a) &&
                Objects.equals(b, pair.b) &&
                Objects.equals(c, pair.c);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b, c);
    }
}
