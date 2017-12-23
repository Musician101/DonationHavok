package io.musician101.donationhavok.havok;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class HavokCommand extends Havok {

    private final String command;

    public HavokCommand() {
        super(0);
        this.command = "/say Quack \\_o<";
    }

    public HavokCommand(int delay, String command) {
        super(delay);
        this.command = command;
    }

    @Override
    public void wreak(EntityPlayer player, BlockPos originalPos) {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        server.getCommandManager().executeCommand(server, command);
    }

    public String getCommand() {
        return command;
    }
}
