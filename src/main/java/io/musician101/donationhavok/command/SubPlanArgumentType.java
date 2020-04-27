package io.musician101.donationhavok.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.musician101.donationhavok.handler.twitch.event.SubPlan;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.util.text.StringTextComponent;

public class SubPlanArgumentType implements ArgumentType<SubPlan> {

    private SubPlanArgumentType() {

    }

    public static SubPlanArgumentType subPlan() {
        return new SubPlanArgumentType();
    }

    public static SubPlan getSubPlan(CommandContext<?> context, String name) {
        return context.getArgument(name, SubPlan.class);
    }

    @Override
    public SubPlan parse(StringReader reader) throws CommandSyntaxException {
        return SubPlan.fromString(reader.readString()).orElseThrow(() -> new SimpleCommandExceptionType(new StringTextComponent("Invalid Subscription Plan name!")).create());
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        getSubPlansKeys().filter(subPlan -> subPlan.toLowerCase().startsWith(builder.getRemaining().toLowerCase())).forEach(builder::suggest);
        return builder.buildFuture();
    }

    @Override
    public Collection<String> getExamples() {
        return getSubPlansKeys().collect(Collectors.toList());
    }

    private Stream<String> getSubPlansKeys() {
        return Stream.of(SubPlan.values()).map(SubPlan::getKey);
    }
}
