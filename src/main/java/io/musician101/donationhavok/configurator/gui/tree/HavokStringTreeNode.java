package io.musician101.donationhavok.configurator.gui.tree;

import com.google.gson.JsonPrimitive;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class HavokStringTreeNode extends HavokValueTreeNode<String> {

    public HavokStringTreeNode(String value) {
        this(null, value);
    }

    public HavokStringTreeNode(@Nullable String key, String value) {
        super(key, value);
    }

    @Nonnull
    @Override
    public JsonPrimitive serialize() {
        return new JsonPrimitive(getValue());
    }
}
