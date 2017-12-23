package io.musician101.donationhavok.gui.tree;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.musician101.donationhavok.DonationHavok;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.JOptionPane;

import static io.musician101.donationhavok.util.json.JsonUtils.BYTE;
import static io.musician101.donationhavok.util.json.JsonUtils.INTEGER;

public class HavokArrayTreeNode extends HavokBranchingTreeNode<JsonArray> {

    private boolean byteArray;
    private boolean intArray;

    public HavokArrayTreeNode() {
        this(new JsonArray());
    }

    public HavokArrayTreeNode(@Nonnull JsonArray json) {
        this(null, json);
    }

    public HavokArrayTreeNode(@Nullable String key) {
        this(key, new JsonArray());
    }

    public HavokArrayTreeNode(@Nullable String key, @Nonnull JsonArray json) {
        this(key, json, false, false);
    }

    public HavokArrayTreeNode(boolean byteArray, boolean intArray) {
        this(new JsonArray(), byteArray, intArray);
    }

    public HavokArrayTreeNode(@Nonnull String key, boolean byteArray, boolean intArray) {
        this(key, new JsonArray(), byteArray, intArray);
    }

    public HavokArrayTreeNode(@Nonnull JsonArray json, boolean byteArray, boolean intArray) {
        this(null, json, byteArray, intArray);
    }

    public HavokArrayTreeNode(@Nullable String key, @Nonnull JsonArray json, boolean byteArray, boolean intArray) {
        super(key, json);
        if (byteArray && intArray) {
            throw new IllegalArgumentException("HavokArrayTreeNode declared as both a byte array and an integer array.");
        }

        this.byteArray = byteArray;
        this.intArray = intArray;
    }

    public boolean isByteArray() {
        return byteArray;
    }

    public boolean isIntArray() {
        return intArray;
    }

    @Override
    protected void parseJson(JsonArray json) {
        json.forEach(jsonElement -> {
            if (jsonElement.isJsonArray()) {
                add(new HavokArrayTreeNode(jsonElement.getAsJsonArray()));
            }
            else if (jsonElement.isJsonObject()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                if (jsonObject.has(BYTE)) {
                    add(new HavokArrayTreeNode(jsonObject.getAsJsonArray(BYTE), true, false));
                }
                else if (jsonObject.has(INTEGER)) {
                    add(new HavokArrayTreeNode(jsonObject.getAsJsonArray(INTEGER), false, true));
                }
                else {
                    add(new HavokMapTreeNode(jsonObject));
                }
            }
            else if (jsonElement.isJsonPrimitive()) {
                JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
                if (jsonPrimitive.isBoolean()) {
                    add(new HavokBooleanTreeNode(jsonPrimitive.getAsBoolean()));
                }
                else if (jsonPrimitive.isNumber()) {
                    add(new HavokNumberTreeNode(jsonPrimitive.getAsNumber()));
                }
                else if (jsonPrimitive.isString()) {
                    add(new HavokStringTreeNode(jsonPrimitive.getAsString()));
                }
                else {
                    JOptionPane.showConfirmDialog(null, "Invalid JSON detected. Check log for more info.", "Invalid JSON!", JOptionPane.DEFAULT_OPTION);
                    DonationHavok.INSTANCE.getLogger().warn("Invalid Json detected: " + jsonPrimitive.toString());
                }
            }
            else {
                JOptionPane.showConfirmDialog(null, "Invalid JSON detected. Check log for more info.", "Invalid JSON!", JOptionPane.DEFAULT_OPTION);
                DonationHavok.INSTANCE.getLogger().warn("Invalid Json detected: " + jsonElement.toString());
            }
        });
    }

    @Nonnull
    @Override
    public JsonArray serialize() {
        JsonArray jsonObject = new JsonArray();
        children.stream().filter(HavokTreeNode.class::isInstance).map(HavokTreeNode.class::cast).forEach(child -> jsonObject.add(child.serialize()));
        return jsonObject;
    }

    public void setByteArray(boolean byteArray) {
        this.byteArray = byteArray;
    }

    public void setIntArray(boolean intArray) {
        this.intArray = intArray;
    }
}
