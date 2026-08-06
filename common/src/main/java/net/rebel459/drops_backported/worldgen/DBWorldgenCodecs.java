package net.rebel459.drops_backported.worldgen;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.rebel459.drops_backported.DropsBackported;
import net.rebel459.drops_backported.worldgen.feature.SequenceFeature;
import net.rebel459.drops_backported.worldgen.feature.speleothem.SpeleothemClusterFeature;
import net.rebel459.drops_backported.worldgen.feature.speleothem.SpeleothemFeature;
import net.rebel459.drops_backported.worldgen.feature.TemplateFeature;
import net.rebel459.drops_backported.worldgen.feature.WeightedRandomSelectorFeature;
import net.rebel459.drops_backported.worldgen.feature.poplar.PoplarFoliagePlacer;
import net.rebel459.drops_backported.worldgen.feature.OffsetPlacement;
import net.rebel459.drops_backported.worldgen.feature.poplar.ShelfMushroomDecorator;
import net.rebel459.drops_backported.worldgen.feature.poplar.PoplarTrunkPlacer;
import net.rebel459.unified.platform.UnifiedRegistries;
import net.rebel459.unified.util.registry.Supplied;

public final class DBWorldgenCodecs {
    private static final UnifiedRegistries.DeferredRegistry<Feature<?>> FEATURES =
            UnifiedRegistries.DeferredRegistry.create(DropsBackported.VANILLA_ID, BuiltInRegistries.FEATURE);
    private static final UnifiedRegistries.DeferredRegistry<TrunkPlacerType<?>> TRUNK_PLACER_TYPES =
            UnifiedRegistries.DeferredRegistry.create(DropsBackported.VANILLA_ID, BuiltInRegistries.TRUNK_PLACER_TYPE);
    private static final UnifiedRegistries.DeferredRegistry<FoliagePlacerType<?>> FOLIAGE_PLACER_TYPES =
            UnifiedRegistries.DeferredRegistry.create(DropsBackported.VANILLA_ID, BuiltInRegistries.FOLIAGE_PLACER_TYPE);
    private static final UnifiedRegistries.DeferredRegistry<TreeDecoratorType<?>> TREE_DECORATOR_TYPES =
            UnifiedRegistries.DeferredRegistry.create(DropsBackported.VANILLA_ID, BuiltInRegistries.TREE_DECORATOR_TYPE);
    private static final UnifiedRegistries.DeferredRegistry<PlacementModifierType<?>> PLACEMENT_MODIFIER_TYPES =
            UnifiedRegistries.DeferredRegistry.create(DropsBackported.VANILLA_ID, BuiltInRegistries.PLACEMENT_MODIFIER_TYPE);

    public static final Supplied<Feature<SequenceFeature.Configuration>> SEQUENCE =
            registerFeature("sequence", new SequenceFeature(SequenceFeature.Configuration.CODEC));
    public static final Supplied<Feature<WeightedRandomSelectorFeature.Configuration>> WEIGHTED_RANDOM_SELECTOR =
            registerFeature("weighted_random_selector", new WeightedRandomSelectorFeature(WeightedRandomSelectorFeature.Configuration.CODEC));
    public static final Supplied<Feature<SpeleothemFeature.Configuration>> SPELEOTHEM =
            registerFeature("speleothem", new SpeleothemFeature(SpeleothemFeature.Configuration.CODEC));
    public static final Supplied<Feature<SpeleothemClusterFeature.Configuration>> SPELEOTHEM_CLUSTER =
            registerFeature("speleothem_cluster", new SpeleothemClusterFeature(SpeleothemClusterFeature.Configuration.CODEC));
    public static final Supplied<Feature<TemplateFeature.Configuration>> TEMPLATE =
            registerFeature("template", new TemplateFeature(TemplateFeature.Configuration.CODEC));

    public static final Supplied<TrunkPlacerType<PoplarTrunkPlacer>> POPLAR_TRUNK_PLACER =
            registerTrunkPlacer("poplar_trunk_placer", PoplarTrunkPlacer.CODEC);
    public static final Supplied<FoliagePlacerType<PoplarFoliagePlacer>> POPLAR_FOLIAGE_PLACER =
            registerFoliagePlacer("poplar_foliage_placer", PoplarFoliagePlacer.CODEC);
    public static final Supplied<TreeDecoratorType<ShelfMushroomDecorator>> SHELF_MUSHROOM_DECORATOR =
            registerTreeDecorator("shelf_mushroom", ShelfMushroomDecorator.CODEC);
    public static final Supplied<PlacementModifierType<OffsetPlacement>> OFFSET =
            registerPlacementModifier("offset", OffsetPlacement.CODEC);

    private DBWorldgenCodecs() {
    }

    private static <C extends net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration> Supplied<Feature<C>> registerFeature(String name, Feature<C> feature) {
        return FEATURES.register(name, () -> feature);
    }

    private static <P extends net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer> Supplied<TrunkPlacerType<P>> registerTrunkPlacer(String name, MapCodec<P> codec) {
        return TRUNK_PLACER_TYPES.register(name, () -> new TrunkPlacerType<>(codec));
    }

    private static <P extends net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer> Supplied<FoliagePlacerType<P>> registerFoliagePlacer(String name, MapCodec<P> codec) {
        return FOLIAGE_PLACER_TYPES.register(name, () -> new FoliagePlacerType<>(codec));
    }

    private static <P extends net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator> Supplied<TreeDecoratorType<P>> registerTreeDecorator(String name, MapCodec<P> codec) {
        return TREE_DECORATOR_TYPES.register(name, () -> new TreeDecoratorType<>(codec));
    }

    private static <P extends PlacementModifier> Supplied<PlacementModifierType<P>> registerPlacementModifier(String name, MapCodec<P> codec) {
        return PLACEMENT_MODIFIER_TYPES.register(name, () -> () -> codec);
    }

    public static void init() {
    }
}
