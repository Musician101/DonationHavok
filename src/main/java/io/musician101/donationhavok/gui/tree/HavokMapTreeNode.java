package io.musician101.donationhavok.gui.tree;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.musician101.donationhavok.DonationHavok;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.JOptionPane;

import static io.musician101.donationhavok.util.json.Keys.BYTE;
import static io.musician101.donationhavok.util.json.Keys.INTEGER;

public class HavokMapTreeNode extends HavokBranchingTreeNode<JsonObject> {

    public HavokMapTreeNode() {
        this(new JsonObject());
    }

    public HavokMapTreeNode(@Nullable String key) {
        this(key, new JsonObject());
    }

    public HavokMapTreeNode(@Nonnull JsonObject json) {
        this(null, json);
    }

    private HavokMapTreeNode(@Nullable String key, @Nonnull JsonObject json) {
        super(key, json);
    }

    @Override
    protected void parseJson(JsonObject json) {
        json.entrySet().forEach(entry -> {
            String k = entry.getKey();
            JsonElement v = entry.getValue();
            if (v.isJsonArray()) {
                add(new HavokArrayTreeNode(k, v.getAsJsonArray()));
            }
            else if (v.isJsonObject()) {
                JsonObject jsonObject = v.getAsJsonObject();
                if (jsonObject.has(BYTE)) {
                    add(new HavokArrayTreeNode(k, jsonObject.getAsJsonArray(BYTE), true, false));
                }
                else if (jsonObject.has(INTEGER)) {
                    add(new HavokArrayTreeNode(k, jsonObject.getAsJsonArray(INTEGER), false, true));
                }
                else {
                    add(new HavokMapTreeNode(k, jsonObject));
                }
            }
            else if (v.isJsonPrimitive()) {
                JsonPrimitive jsonPrimitive = v.getAsJsonPrimitive();
                if (jsonPrimitive.isBoolean()) {
                    add(new HavokBooleanTreeNode(k, jsonPrimitive.getAsBoolean()));
                }
                else if (jsonPrimitive.isNumber()) {
                    add(new HavokNumberTreeNode(k, jsonPrimitive.getAsNumber()));
                }
                else if (jsonPrimitive.isString()) {
                    add(new HavokStringTreeNode(k, jsonPrimitive.getAsString()));
                }
                else {
                    JOptionPane.showConfirmDialog(null, "Invalid JSON detected. Check log for more info.", "Invalid JSON!", JOptionPane.DEFAULT_OPTION);
                    DonationHavok.INSTANCE.getLogger().warn("Invalid Json detected: " + jsonPrimitive.toString());
                }
            }
            else {
                JOptionPane.showConfirmDialog(null, "Invalid JSON detected. Check log for more info.", "Invalid JSON!", JOptionPane.DEFAULT_OPTION);
                DonationHavok.INSTANCE.getLogger().warn("Invalid Json detected: " + v.toString());
            }
        });
    }

    @Nonnull
    @Override
    public JsonObject serialize() {
        JsonObject jsonObject = new JsonObject();
        children.stream().filter(HavokTreeNode.class::isInstance).map(HavokTreeNode.class::cast).filter(child -> child.getKey() != null).forEach(child -> jsonObject.add(child.getKey(), child.serialize()));
        return jsonObject;
    }
}
