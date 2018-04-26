package net.darkhax.gamestages.data;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

import org.apache.logging.log4j.Level;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.Gson;

import net.darkhax.gamestages.GameStages;
import net.minecraft.nbt.NBTTagCompound;

public class FakePlayerData implements IStageData {
    private static final Map<String, FakePlayerData> fakePlayerData = new HashMap<>();

    public static void reloadFromFile () {

        GameStages.LOG.getLogger().log(Level.INFO, "Clearing gamestages fakeplayers for reload");
        fakePlayerData.clear();
        if (!GameStages.fakePlayerDataFile.exists()) {
            return;
        }
        final Gson gson = new Gson();

        try {
            final FakePlayerData[] fakePlayers = gson.fromJson(Files.newReader(GameStages.fakePlayerDataFile, Charsets.UTF_8), FakePlayerData[].class);
            Arrays.stream(fakePlayers).forEach(FakePlayerData::addFakePlayer);
        }
        catch (final FileNotFoundException e) {
            // how did this happen?
        }
    }

    public static void addFakePlayer (final String fakePlayerName, final Set<String> stages) {

        fakePlayerData.put(fakePlayerName, new FakePlayerData(fakePlayerName, stages));
    }

    public static void addFakePlayer (final FakePlayerData data) {

        fakePlayerData.put(data.fakePlayerName, data);
        GameStages.LOG.getLogger().log(Level.INFO, "Adding fakeplayer {} with gamestages {}", data.fakePlayerName, data.stages);
    }

    public static IStageData getDataFor (final String fakePlayerName) {

        return fakePlayerData.getOrDefault(fakePlayerName, DEFAULT);
    }

    private static final FakePlayerData DEFAULT = new FakePlayerData("DEFAULT", Collections.emptySet());

    private final Set<String> stages = new HashSet<>();
    private final String fakePlayerName;

    private FakePlayerData (final String fakePlayerName, final Set<String> stages) {

        this.stages.addAll(stages);
        this.fakePlayerName = fakePlayerName;
    }

    @Override
    public Collection<String> getStages () {

        return this.stages;
    }

    @Override
    public boolean hasStage (@Nonnull final String stage) {

        return this.stages.contains(stage);
    }

    @Override
    public void addStage (@Nonnull final String stage) {

        // no op
    }

    @Override
    public void removeStage (@Nonnull final String stage) {

        // no op
    }

    @Override
    public void clear () {

        // no op
    }

    @Override
    public void readFromNBT (NBTTagCompound tag) {

    }

    @Override
    public NBTTagCompound writeToNBT () {

        return null;
    }
}
