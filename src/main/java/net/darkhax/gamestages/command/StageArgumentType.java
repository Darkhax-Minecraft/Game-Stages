package net.darkhax.gamestages.command;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.darkhax.bookshelf.serialization.Serializers;
import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.ArgumentSerializer;
import net.minecraft.network.PacketBuffer;

/**
 * A command argument type that represents a stage string. Has built in suggestions for all the
 * known stages.
 */
public class StageArgumentType implements ArgumentType<String> {
    
    /**
     * Serializer for this argument type.
     */
    public static final ArgumentSerializer<StageArgumentType> SERIALIZERS = new Serializer();
    
    /**
     * A list of examples used for this type.
     */
    private static final List<String> examples = Arrays.asList("stage", "stage_name", "example:stage");
    
    private Set<String> knownStages;
    
    public StageArgumentType() {
        
        this.knownStages = GameStageHelper.getKnownStages();
    }
    
    /**
     * Gets a stage name from a command context.
     *
     * @param context The context.
     * @param name The name of the parameter/argument.
     * @return The value for this argument.
     */
    public static String getStage (final CommandContext<?> context, final String name) {
        
        return context.getArgument(name, String.class);
    }
    
    @Override
    public String parse (final StringReader reader) throws CommandSyntaxException {
        
        return reader.readUnquotedString();
    }
    
    @Override
    public String toString () {
        
        return "stagename";
    }
    
    @Override
    public Collection<String> getExamples () {
        
        return examples;
    }
    
    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions (CommandContext<S> context, SuggestionsBuilder builder) {
        
        return ISuggestionProvider.suggest(this.knownStages, builder);
    }
    
    static class Serializer extends ArgumentSerializer<StageArgumentType> {

        private Serializer() {
            
            super(StageArgumentType::new);
        }

        @Override
        public void serializeToNetwork (StageArgumentType arg, PacketBuffer buffer) {
            
            Serializers.STRING.writeSet(buffer, arg.knownStages);
        }

        @Override
        public StageArgumentType deserializeFromNetwork (PacketBuffer buffer) {
            
            final StageArgumentType argType = super.deserializeFromNetwork(buffer);
            argType.knownStages = Serializers.STRING.readSet(buffer);
            return argType;
        }     
    }
}