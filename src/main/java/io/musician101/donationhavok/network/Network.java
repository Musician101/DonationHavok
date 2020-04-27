package io.musician101.donationhavok.network;

import io.musician101.donationhavok.DonationHavok;
import io.musician101.donationhavok.Reference;
import io.musician101.donationhavok.config.GeneralConfig;
import io.musician101.donationhavok.config.RewardsConfig;
import io.musician101.donationhavok.config.StreamlabsConfig;
import io.musician101.donationhavok.config.TwitchConfig;
import io.musician101.donationhavok.gui.GeneralConfigGui;
import io.musician101.donationhavok.gui.RewardTiersConfigGui;
import io.musician101.donationhavok.gui.StreamlabsConfigGui;
import io.musician101.donationhavok.gui.TwitchConfigGui;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import static io.musician101.donationhavok.util.json.Keys.GSON;

public class Network {

    public final static SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(Reference.MOD_ID, "configs"), () -> Reference.VERSION, s -> s.equals(Reference.VERSION) || s.equals(NetworkRegistry.ABSENT), s -> s.equals(Reference.VERSION));
    private static int id = 0;

    private Network() {

    }

    public static void init() {
        INSTANCE.registerMessage(id++, GeneralConfig.class, (generalConfig, packetBuffer) -> packetBuffer.writeString(GSON.toJson(generalConfig)), packetBuffer -> GSON.fromJson(packetBuffer.readString(32767), GeneralConfig.class), (generalConfig, contextSupplier) -> {
            Context context = contextSupplier.get();
            if (context.getDirection().getOriginationSide() == LogicalSide.SERVER) {
                Minecraft.getInstance().displayGuiScreen(new GeneralConfigGui(generalConfig));
            }
            else {
                DonationHavok mod = DonationHavok.getInstance();
                ServerPlayerEntity player = context.getSender();
                try {
                    mod.getConfig().setGeneralConfig(generalConfig);
                    if (player != null) {
                        player.sendMessage(new StringTextComponent("Saved the config successfully."));
                        mod.getLogger().info("General config saved by " + player.getName());
                        return;
                    }

                    mod.getLogger().info("General config saved.");
                }
                catch (Exception e) {
                    if (player != null) {
                        player.sendMessage(new StringTextComponent("An error occurred while attempting to save the General config."));
                    }

                    mod.getLogger().error("An error occurred while attempting to save the General config.", e);
                }
            }

            context.setPacketHandled(true);
        });
        INSTANCE.registerMessage(id++, RewardsConfig.class, (rewardsConfig, packetBuffer) -> packetBuffer.writeString(GSON.toJson(rewardsConfig)), packetBuffer -> GSON.fromJson(packetBuffer.readString(32767), RewardsConfig.class), (rewardsConfig, contextSupplier) -> {
            Context context = contextSupplier.get();
            if (context.getDirection().getOriginationSide() == LogicalSide.SERVER) {
                Minecraft.getInstance().displayGuiScreen(new RewardTiersConfigGui(rewardsConfig));
            }
            else {
                DonationHavok mod = DonationHavok.getInstance();
                ServerPlayerEntity player = context.getSender();
                try {
                    mod.getConfig().setRewardsConfig(rewardsConfig);
                    if (player != null) {
                        player.sendMessage(new StringTextComponent("Saved the Rewards config successfully."));
                        mod.getLogger().info("Rewards config saved by " + player.getName());
                        return;
                    }

                    mod.getLogger().info("Rewards config saved.");
                }
                catch (Exception e) {
                    if (player != null) {
                        player.sendMessage(new StringTextComponent("An error occurred while attempting to save the Rewards config."));
                    }

                    mod.getLogger().error("An error occurred while attempting to save the Rewards config.", e);
                }
            }

            context.setPacketHandled(true);
        });
        INSTANCE.registerMessage(id++, StreamlabsConfig.class, (streamlabsConfig, packetBuffer) -> packetBuffer.writeString(GSON.toJson(streamlabsConfig)), packetBuffer -> GSON.fromJson(packetBuffer.readString(32767), StreamlabsConfig.class), (streamlabsConfig, contextSupplier) -> {
            Context context = contextSupplier.get();
            if (context.getDirection().getOriginationSide() == LogicalSide.SERVER) {
                Minecraft.getInstance().displayGuiScreen(new StreamlabsConfigGui(streamlabsConfig));
            }
            else {
                DonationHavok mod = DonationHavok.getInstance();
                ServerPlayerEntity player = context.getSender();
                try {
                    mod.getConfig().setStreamlabsConfig(streamlabsConfig);
                    if (player != null) {
                        player.sendMessage(new StringTextComponent("Saved the Streamlabs config successfully."));
                        mod.getLogger().info("Streamlabs config saved by " + player.getName());
                        return;
                    }

                    mod.getLogger().info("Streamlabs config saved.");
                }
                catch (Exception e) {
                    if (player != null) {
                        player.sendMessage(new StringTextComponent("An error occurred while attempting to save the Streamlabs config."));
                    }

                    mod.getLogger().error("An error occurred while attempting to save the Streamlabs config.", e);
                }
            }

            context.setPacketHandled(true);
        });
        INSTANCE.registerMessage(id++, TwitchConfig.class, (twitchConfig, packetBuffer) -> packetBuffer.writeString(GSON.toJson(twitchConfig)), packetBuffer -> GSON.fromJson(packetBuffer.readString(32767), TwitchConfig.class), (twitchConfig, contextSupplier) -> {
            Context context = contextSupplier.get();
            if (context.getDirection().getOriginationSide() == LogicalSide.SERVER) {
                Minecraft.getInstance().displayGuiScreen(new TwitchConfigGui(twitchConfig));
            }
            else {
                DonationHavok mod = DonationHavok.getInstance();
                ServerPlayerEntity player = context.getSender();
                try {
                    mod.getConfig().setTwitchConfig(twitchConfig);
                    if (player != null) {
                        player.sendMessage(new StringTextComponent("Saved the Twitch config successfully."));
                        mod.getLogger().info("Twitch config saved by " + player.getName());
                        return;
                    }

                    mod.getLogger().info("Twitch config saved.");
                }
                catch (Exception e) {
                    if (player != null) {
                        player.sendMessage(new StringTextComponent("An error occurred while attempting to save the Twitch config."));
                    }

                    mod.getLogger().error("An error occurred while attempting to save the Twitch config.", e);
                }
            }

            context.setPacketHandled(true);
        });
    }
}
