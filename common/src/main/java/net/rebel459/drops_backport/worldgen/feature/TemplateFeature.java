package net.rebel459.drops_backport.worldgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.List;

public class TemplateFeature extends Feature<TemplateFeature.Configuration> {
    public TemplateFeature(Codec<Configuration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<Configuration> context) {
        Entry entry = context.config().templates().getRandomOrThrow(context.random());
        Rotation rotation = Util.getRandom(entry.rotations(), context.random());
        StructureTemplateManager structureManager = context.level().getLevel().getServer().getStructureManager();
        StructureTemplate template = structureManager.getOrCreate(entry.template());
        Vec3i xOffset = getRotatedOffset(rotation, Direction.Axis.X, template);
        Vec3i zOffset = getRotatedOffset(rotation, Direction.Axis.Z, template);
        BlockPos origin = context.origin().offset(xOffset).offset(zOffset);
        StructurePlaceSettings settings = new StructurePlaceSettings()
                .setRotation(rotation)
                .setRandom(context.random());
        return template.placeInWorld(context.level(), origin, origin, settings, context.random(), 3);
    }

    private static Vec3i getRotatedOffset(Rotation rotation, Direction.Axis axis, StructureTemplate template) {
        return rotation.rotate(axis.getNegative()).getUnitVec3i().multiply(template.getSize().get(axis) / 2);
    }

    public record Configuration(WeightedList<Entry> templates) implements FeatureConfiguration {
        public static final Codec<Configuration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                WeightedList.codec(Entry.CODEC).fieldOf("templates").forGetter(Configuration::templates)
        ).apply(instance, Configuration::new));
    }

    public record Entry(Identifier template, List<Rotation> rotations) {
        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Identifier.CODEC.fieldOf("id").forGetter(Entry::template),
                Rotation.CODEC.listOf().optionalFieldOf("rotations", List.of(Rotation.values())).forGetter(Entry::rotations)
        ).apply(instance, Entry::new));
    }
}
