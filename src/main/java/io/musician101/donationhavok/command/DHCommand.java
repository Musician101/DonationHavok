package io.musician101.donationhavok.command;

import io.musician101.donationhavok.DonationHavok;
import io.musician101.donationhavok.network.Network;
import io.musician101.donationhavok.network.message.JsonMessage;
import io.musician101.donationhavok.streamlabs.StreamLabsTracker.Donation;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import static io.musician101.donationhavok.util.json.JsonUtils.GSON;

public class DHCommand extends CommandBase {

    @Nonnull
    @Override
    public String getName() {
        return "dh";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender) {
        return "/dh <config | reload | test <amount> [name]>";
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
        if (args.length > 0) {
            String subCommand = args[0];
            EntityPlayerMP player = getCommandSenderAsPlayer(sender);
            if (subCommand.equalsIgnoreCase("config")) {
                Network.INSTANCE.sendTo(new JsonMessage(GSON.toJsonTree(DonationHavok.INSTANCE.getStreamLabsTracker()).getAsJsonObject()), (EntityPlayerMP) sender);
                player.sendMessage(new TextComponentString("The config is being sent to you now. If the GUI doesn't open within a few seconds, you might not have the mod installed."));
                return;
            }
            else if (subCommand.equalsIgnoreCase("reload")) {
                try {
                    DonationHavok.INSTANCE.reload();
                    sender.sendMessage(new TextComponentString("Config reloaded."));
                }
                catch (IOException e) {
                    sender.sendMessage(new TextComponentString("An error occurred while reloading the config!"));
                    DonationHavok.INSTANCE.getLogger().error("An error occurred while reloading the config!" + e.getMessage());
                }

                return;
            }
            else if (subCommand.equalsIgnoreCase("test")) {
                if (args.length > 1) {
                    double amount = parseDouble(args[1]);
                    String name = sender.getName();
                    if (args.length > 2) {
                        name = args[2];
                    }

                    DonationHavok.INSTANCE.getStreamLabsTracker().runDonation(player, new Donation(name, amount, "$" + amount, "This is a test donation."));
                    return;
                }
                // Debug code left in to bulk but commented out to prevent accidental usage.
                /*else {
                    DonationHavok.INSTANCE.getStreamLabsTracker().test();
                    return;
                }*/
            }
        }

        throw new WrongUsageException(getUsage(sender));
    }

    @Nonnull
    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        List<String> subCommands = Arrays.asList("config", "reload", "test");
        if (args.length == 0) {
            return subCommands;
        }
        else if (args.length == 1) {
            return subCommands.stream().filter(s -> s.startsWith(args[0])).collect(Collectors.toList());
        }
        else if (args.length == 2 && args[0].equalsIgnoreCase("test")) {
            return DonationHavok.INSTANCE.getStreamLabsTracker().getRewards().keySet().stream().map(Object::toString).filter(s -> s.startsWith(args[1])).collect(Collectors.toList());
        }

        return super.getTabCompletions(server, sender, args, targetPos);
    }
}
