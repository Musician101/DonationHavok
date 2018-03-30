package io.musician101.donationhavok.command;

import io.musician101.donationhavok.Config;
import io.musician101.donationhavok.DonationHavok;
import io.musician101.donationhavok.handler.StreamLabsHandler.Donation;
import io.musician101.donationhavok.handler.twitch.event.SubPlan;
import io.musician101.donationhavok.network.Network;
import io.musician101.donationhavok.network.message.JsonMessage;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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

import static io.musician101.donationhavok.util.json.JsonKeyProcessor.GSON;

public class DHCommand extends CommandBase {

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
        DonationHavok instance = DonationHavok.INSTANCE;
        if (args.length > 0) {
            String subCommand = args[0];
            if (subCommand.equalsIgnoreCase("config")) {
                EntityPlayerMP player = getCommandSenderAsPlayer(sender);
                Network.INSTANCE.sendTo(new JsonMessage(GSON.toJsonTree(new Config(instance.getDiscoveryHandler(), instance.getRewardsHandler(), instance.getStreamLabsHandler(), instance.getTwitchHandler())).getAsJsonObject()), player);
                player.sendMessage(new TextComponentString("The config is being sent to you now. If the GUI doesn't open within a few seconds, you might not have the mod installed."));
                return;
            }
            else if (subCommand.equalsIgnoreCase("reload")) {
                try {
                    instance.reload();
                    sender.sendMessage(new TextComponentString("Config reloaded."));
                }
                catch (IOException e) {
                    sender.sendMessage(new TextComponentString("An error occurred while reloading the config!"));
                    instance.getLogger().error("An error occurred while reloading the config!" + e.getMessage());
                }

                return;
            }
            else if (subCommand.equalsIgnoreCase("test")) {
                if (args.length > 2) {
                    EntityPlayerMP player = getCommandSenderAsPlayer(sender);
                    String type = args[1];
                    if (type.equalsIgnoreCase("bits") || type.equalsIgnoreCase("cheer")) {
                        int amount = parseInt(args[2]);
                        DonationHavok.INSTANCE.getTwitchHandler().runCheer(amount);
                        return;
                    }
                    else if (type.equalsIgnoreCase("donation")) {
                        double amount = parseDouble(args[2], 0);
                        String name = player.getName();
                        if (args.length > 3) {
                            name = args[3];
                        }

                        DonationHavok.INSTANCE.getStreamLabsHandler().runDonation(new Donation(name, amount, "$" + amount, "This is a test donation."));
                        return;
                    }
                    else if ((type.equalsIgnoreCase("subscription") || type.equalsIgnoreCase("sub")) && args.length > 3) {
                        Optional<SubPlan> subPlan = SubPlan.fromString(args[2]);
                        if (subPlan.isPresent()) {
                            int streak = parseInt(args[3], 0);
                            DonationHavok.INSTANCE.getTwitchHandler().runSubscription(subPlan.get(), streak);
                            return;
                        }
                    }
                }
                // Debug code left in to bulk test but commented out to prevent accidental usage.
                /*else {
                    DonationHavok.INSTANCE.getStreamLabsHandler().test();
                    return;
                }*/
            }
        }

        throw new WrongUsageException(getUsage(sender));
    }

    @Nonnull
    @Override
    public String getName() {
        return "dh";
    }

    @Nonnull
    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length == 1) {
            return Stream.of("config", "reload", "test").filter(s -> s.startsWith(args[0].toLowerCase())).collect(Collectors.toList());
        }
        else if (args[0].equalsIgnoreCase("test")) {
            if (args.length == 2) {
                return Stream.of("bits", "cheer", "donation", "sub", "subscription").filter(s -> s.startsWith(args[1].toLowerCase())).collect(Collectors.toList());
            }
            else if (args[1].equalsIgnoreCase("donation")) {
                if (args.length == 3) {
                    return DonationHavok.INSTANCE.getRewardsHandler().getRewards().keySet().stream().map(Object::toString).filter(s -> s.startsWith(args[2])).collect(Collectors.toList());
                }

            }
            else if (args[1].equalsIgnoreCase("sub") || args[1].equalsIgnoreCase("subscription")) {
                if (args.length == 3) {
                    return Stream.of("1000", "2000", "3000", "Prime").filter(s -> s.startsWith(args[2])).collect(Collectors.toList());
                }
            }
        }

        return super.getTabCompletions(server, sender, args, targetPos);
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender) {
        return "/dh <config | reload | test <bits <amount> | cheer <amount> | donation <amount> [name] | sub <subPlan> <streak> | subscription <subPlan> <streak>>>";
    }
}
