package io.musician101.donationhavok.configurator.gui.tree;

import com.google.gson.JsonPrimitive;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class HavokNumberTreeNode extends HavokValueTreeNode<Number> {

    public HavokNumberTreeNode(Number value) {
        this(null, value);
    }

    public HavokNumberTreeNode(@Nullable String key, Number value) {
        super(key, value);
    }

    @Nonnull
    @Override
    public JsonPrimitive serialize() {
        return new JsonPrimitive(getValue());
    }
}
