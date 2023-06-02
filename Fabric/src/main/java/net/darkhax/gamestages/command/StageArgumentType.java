package net.darkhax.gamestages.command;

import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.darkhax.bookshelf.api.serialization.Serializers;
import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.FriendlyByteBuf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * A command argument type that represents a stage string. Has built in suggestions for all the known stages.
 */
public class StageArgumentType implements ArgumentType<String> {

    /**
     * Serializer for this argument type.
     */
    public static final ArgumentTypeInfo<StageArgumentType, ?> SERIALIZERS = new Serializer();

    /**
     * A list of examples used for this type.
     */
    private static final List<String> examples = Arrays.asList("stage", "stage_name", "example:stage");

    private final Set<String> knownStages;

    public StageArgumentType() {

        this(GameStageHelper.getKnownStages());
    }

    public StageArgumentType(Set<String> knownStages) {

        this.knownStages = knownStages;
    }

    /**
     * Gets a stage name from a command context.
     *
     * @param context The context.
     * @param name    The name of the parameter/argument.
     * @return The value for this argument.
     */
    public static String getStage(final CommandContext<?> context, final String name) {

        return context.getArgument(name, String.class);
    }

    @Override
    public String parse(final StringReader reader) throws CommandSyntaxException {

        return reader.readUnquotedString();
    }

    @Override
    public String toString() {

        return "stagename";
    }

    @Override
    public Collection<String> getExamples() {

        return examples;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {

        return SharedSuggestionProvider.suggest(this.knownStages, builder);
    }

    public static StringArgumentType createVanillaFallback(StageArgumentType type) {
        return StringArgumentType.string();
    }

    static class Template implements ArgumentTypeInfo.Template<StageArgumentType> {

        private final Set<String> knownStages;

        Template(Set<String> knownStages) {

            this.knownStages = knownStages;
        }

        @Override
        public StageArgumentType instantiate(CommandBuildContext context) {

            return new StageArgumentType(this.knownStages);
        }

        @Override
        public ArgumentTypeInfo<StageArgumentType, ?> type() {

            return SERIALIZERS;
        }
    }

    static class Serializer implements ArgumentTypeInfo<StageArgumentType, Template> {


        @Override
        public void serializeToNetwork(StageArgumentType.Template template, FriendlyByteBuf buffer) {

            Serializers.STRING.toByteBufList(buffer, new ArrayList<>(template.knownStages));
        }

        @Override
        public StageArgumentType.Template deserializeFromNetwork(FriendlyByteBuf buffer) {

            return new StageArgumentType.Template(new HashSet<>(Serializers.STRING.fromByteBufList(buffer)));
        }

        @Override
        public void serializeToJson(StageArgumentType.Template template, JsonObject json) {

            json.add("known_stages", Serializers.STRING.toJSONSet(template.knownStages));
        }

        @Override
        public StageArgumentType.Template unpack(StageArgumentType arg) {

            return new StageArgumentType.Template(arg.knownStages);
        }
    }
}