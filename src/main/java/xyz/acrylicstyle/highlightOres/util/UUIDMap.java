package xyz.acrylicstyle.highlightOres.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import util.Collection;

import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

public class UUIDMap<V> extends Collection<UUID, V> {
    private final Function<UUID, V> constructor;

    public UUIDMap(@NotNull Supplier<V> constructor) {
        this(uuid -> constructor.get());
    }

    public UUIDMap(@NotNull Function<UUID, V> constructor) {
        this.constructor = constructor;
    }

    @NotNull
    public V get(@NotNull UUID key) {
        if (!this.containsKey(key)) super.put(key, constructor.apply(key));
        return super.get(key);
    }

    @Override
    @Contract("_ -> fail")
    public V get(Object key) {
        throw new UnsupportedOperationException();
    }
}
