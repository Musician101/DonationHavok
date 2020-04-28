package io.musician101.donationhavok.configurator.gui.tree;

import com.google.gson.JsonPrimitive;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class HavokValueTreeNode<V> extends HavokTreeNode<JsonPrimitive> {

    @Nonnull
    private V value;

    HavokValueTreeNode(@Nullable String key, @Nonnull V value) {
        super(key);
        this.value = value;
    }

    @Nonnull
    public V getValue() {
        return value;
    }

    public void setValue(@Nonnull V value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return (getKey() == null ? "" : getKey() + ": ") + value.toString();
    }
}
