package io.musician101.donationhavok.gui.tree;

import com.google.gson.JsonPrimitive;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class HavokBooleanTreeNode extends HavokValueTreeNode<Boolean> {

    public HavokBooleanTreeNode(boolean value) {
        this(null, value);
    }

    public HavokBooleanTreeNode(@Nullable String key, boolean value) {
        super(key, value);
    }

    @Nonnull
    @Override
    public JsonPrimitive serialize() {
        return new JsonPrimitive(getValue());
    }
}
