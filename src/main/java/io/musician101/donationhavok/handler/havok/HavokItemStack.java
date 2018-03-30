package io.musician101.donationhavok.handler.havok;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import io.musician101.donationhavok.handler.havok.HavokItemStack.Serializer;
import io.musician101.donationhavok.util.json.Keys;
import io.musician101.donationhavok.util.json.adapter.BaseSerializer;
import io.musician101.donationhavok.util.json.adapter.TypeOf;
import java.lang.reflect.Type;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

@TypeOf(Serializer.class)
public class HavokItemStack extends Havok {

    private final ItemStack itemStack;

    public HavokItemStack() {
        super(0);
        this.itemStack = new ItemStack(Items.WHEAT_SEEDS);
        itemStack.setStackDisplayName("Duck Feed");
    }

    public HavokItemStack(int delay, ItemStack itemStack) {
        super(delay);
        this.itemStack = itemStack;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public void wreak(EntityPlayer player, BlockPos originalPos) {
        World world = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld();
        EntityItem item = new EntityItem(world);
        item.setItem(itemStack);
        item.setPositionAndRotation(originalPos.getX(), originalPos.getY() + 1, originalPos.getZ(), 0, 0);
        world.spawnEntity(item);
    }

    public static class Serializer extends BaseSerializer<HavokItemStack> {

        @Override
        public HavokItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            int delay = deserialize(jsonObject, context, Keys.DELAY, 0);
            ItemStack defaultStack = new ItemStack(Items.WHEAT_SEEDS);
            defaultStack.setStackDisplayName("Duck Feed");
            ItemStack itemStack = deserialize(jsonObject, context, Keys.ITEM_STACK, defaultStack);
            return new HavokItemStack(delay, itemStack);
        }

        @Override
        public JsonElement serialize(HavokItemStack src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            serialize(jsonObject, context, Keys.DELAY, src.getDelay());
            serialize(jsonObject, context, Keys.ITEM_STACK, src.getItemStack());
            return jsonObject;
        }
    }
}
