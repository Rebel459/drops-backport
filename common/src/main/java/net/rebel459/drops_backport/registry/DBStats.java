package net.rebel459.drops_backport.registry;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import net.rebel459.drops_backport.DropsBackport;
import net.rebel459.unified.platform.UnifiedRegistries;

import java.util.ArrayList;
import java.util.List;

public class DBStats {

    public static UnifiedRegistries.DeferredRegistry<Identifier> STATS = UnifiedRegistries.DeferredRegistry.create(DropsBackport.VANILLA_ID, BuiltInRegistries.CUSTOM_STAT);
    private static final List<Pair<Identifier, StatFormatter>> DEFERRED_CUSTOM_STATS = new ArrayList<>();

    public static final Identifier SLEEP_IN_STRAW_BED = makeCustomStat("sleep_in_straw_bed", StatFormatter.DEFAULT);

    public static void init() {}

    public static void initCustomStats() {
        DEFERRED_CUSTOM_STATS.forEach(pair -> Stats.CUSTOM.get(pair.getFirst(), pair.getSecond()));
    }

    private static Identifier makeCustomStat(String id, StatFormatter formatter) {
        Identifier location = Identifier.withDefaultNamespace(id);
        STATS.register(id, () -> location);
        DEFERRED_CUSTOM_STATS.add(Pair.of(location, formatter));
        return location;
    }
}
