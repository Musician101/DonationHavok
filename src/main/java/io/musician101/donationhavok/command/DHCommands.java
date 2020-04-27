package io.musician101.donationhavok.command;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.musician101.donationhavok.DonationHavok;
import io.musician101.donationhavok.handler.SaleHandler;
import io.musician101.donationhavok.handler.StreamlabsHandler.Donation;
import io.musician101.donationhavok.handler.twitch.TwitchBot;
import io.musician101.donationhavok.handler.twitch.event.Cheer;
import io.musician101.donationhavok.handler.twitch.event.SubPlan;
import io.musician101.donationhavok.handler.twitch.event.Subscription;
import io.musician101.donationhavok.network.Network;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkDirection;

public class DHCommands {

    public static LiteralArgumentBuilder<CommandSource> dh() {
        return Commands.literal("dh").requires(source -> source.hasPermissionLevel(3)).then(config()).then(discovery()).then(reload()).then(sale()).then(test());
    }

    //TODO separate into more commands
    @Deprecated
    private static LiteralArgumentBuilder<CommandSource> config() {
        return Commands.literal("config").then(Commands.literal("general").executes(source -> {
            ServerPlayerEntity player = source.getSource().asPlayer();
            Network.INSTANCE.sendTo(DonationHavok.getInstance().getConfig().getGeneralConfig(), player.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
            player.sendMessage(new StringTextComponent("If the GUI doesn't open within a few seconds, you might not have the mod installed."));
            return 1;
        })).then(Commands.literal("rewards").executes(source -> {
            ServerPlayerEntity player = source.getSource().asPlayer();
            Network.INSTANCE.sendTo(DonationHavok.getInstance().getConfig().getRewardsConfig(), player.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
            player.sendMessage(new StringTextComponent("If the GUI doesn't open within a few seconds, you might not have the mod installed."));
            return 1;
        })).then(Commands.literal("streamlabs").executes(source -> {
            ServerPlayerEntity player = source.getSource().asPlayer();
            Network.INSTANCE.sendTo(DonationHavok.getInstance().getConfig().getStreamlabsConfig(), player.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
            player.sendMessage(new StringTextComponent("If the GUI doesn't open within a few seconds, you might not have the mod installed."));
            return 1;
        })).then(Commands.literal("twitch").executes(source -> {
            ServerPlayerEntity player = source.getSource().asPlayer();
            Network.INSTANCE.sendTo(DonationHavok.getInstance().getConfig().getTwitchConfig(), player.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
            player.sendMessage(new StringTextComponent("If the GUI doesn't open within a few seconds, you might not have the mod installed."));
            return 1;
        }));
    }

    private static LiteralArgumentBuilder<CommandSource> discovery() {
        return Commands.literal("discovery").executes(source -> {
            /*CommonProxy proxy = DonationHavok.getInstance().getProxy();
            ServerPlayerEntity player = context.getSource().asPlayer();
            proxy.openDiscoveryGUI(player, DonationHavok.getInstance().getDiscoveryHandler(), isDedicatedServer());
            player.sendMessage(new StringTextComponent("The Discoveries are being sent to you now. If the GUI doesn't open within a few seconds, you might not have the mod installed."));*/
            return 1;
        });
    }

    private static LiteralArgumentBuilder<CommandSource> reload() {
        return Commands.literal("reload").executes(source -> {
            //TODO need to redo this command
                /*try {
                    instance.reload();
                    sender.sendMessage(new StringTextComponent("Config reloaded."));
                }
                catch (IOException e) {
                    sender.sendMessage(new StringTextComponent("An error occurred while reloading the config!"));
                    instance.getLogger().error("An error occurred while reloading the config!" + e.getMessage());
                }*/
            return 1;
        });
    }

    private static LiteralArgumentBuilder<CommandSource> sale() {
        return Commands.literal("sale").then(Commands.literal("stop").executes(source -> {
            SaleHandler.stopSale();
            String message = "The sale has been stopped.";
            try {
                source.getSource().asPlayer().sendMessage(new StringTextComponent(message));
            }
            catch (CommandSyntaxException e) {
                DonationHavok.getInstance().getLogger().info(message);
            }

            return 1;
        })).then(Commands.literal("start").then(Commands.argument("discount", DoubleArgumentType.doubleArg(0D, 1D)).then(Commands.argument("length", IntegerArgumentType.integer(1)).executes(source -> {
            double discount = DoubleArgumentType.getDouble(source, "discount");
            int length = IntegerArgumentType.getInteger(source, "length");
            SaleHandler.startSale(discount, length);
            String message = "Starting sale of " + discount + "%. It will last " + length + " seconds.";
            try {
                source.getSource().asPlayer().sendMessage(new StringTextComponent(message));
            }
            catch (CommandSyntaxException e) {
                DonationHavok.getInstance().getLogger().info(message);
            }

            return 1;
        }))));
    }

    private static LiteralArgumentBuilder<CommandSource> test() {
        DonationHavok instance = DonationHavok.getInstance();
        return Commands.literal("test").then(Commands.<CommandSource>literal("cheer").then(Commands.argument("bits", IntegerArgumentType.integer()).executes(source -> {
            ServerPlayerEntity player = source.getSource().asPlayer();
            int amount = IntegerArgumentType.getInteger(source, "bits");
            TwitchBot.runCheer(new Cheer(player.getName().getString(), player.getName().getString(), amount, "This is a test cheer."));
            return 1;
        }))).then(Commands.literal("donation").then(Commands.argument("amount", DoubleArgumentType.doubleArg()).executes(source -> {
            ServerPlayerEntity player = source.getSource().asPlayer();
            double amount = DoubleArgumentType.getDouble(source, "amount");
            instance.getStreamLabsHandler().runDonation(new Donation(player.getName().getString(), amount, "$" + amount, "This is a test donation."));
            return 1;
        }))).then(Commands.literal("subscription").then(Commands.argument("subPlan", SubPlanArgumentType.subPlan()).then(Commands.argument("streak", IntegerArgumentType.integer(1))).executes(source -> {
            String player = source.getSource().asPlayer().getName().getString();
            SubPlan subPlan = SubPlanArgumentType.getSubPlan(source, "subPlan");
            int streak = IntegerArgumentType.getInteger(source, "streak");
            TwitchBot.runSubscription(new Subscription(player, player, player, subPlan, streak == 1, streak, "This is a test subscription."));
            return 1;
        })));
    }

    private static boolean isDedicatedServer() {
        return LogicalSidedProvider.WORKQUEUE.<MinecraftServer>get(LogicalSide.SERVER).isDedicatedServer();
    }
}
