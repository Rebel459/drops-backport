package net.rebel459.drops_backport.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.rebel459.drops_backport.DropsBackport;
import net.rebel459.drops_backport.entity.PotentSulfurBlockEntity;
import net.rebel459.unified.api.core.Supplied;
import net.rebel459.unified.api.core.UnifiedRegistries;

import java.util.Set;

public class DBBlockEntityTypes {
    public static UnifiedRegistries.DeferredRegistry<BlockEntityType<?>> BLOCK_ENTITY_TYPES = UnifiedRegistries.DeferredRegistry.create(DropsBackport.VANILLA_ID, BuiltInRegistries.BLOCK_ENTITY_TYPE);

    public static final Supplied<BlockEntityType<PotentSulfurBlockEntity>> POTENT_SULFUR = BLOCK_ENTITY_TYPES.register(
            "potent_sulfur", () -> new BlockEntityType<>(PotentSulfurBlockEntity::new, Set.of())
    );

    public static void init() {}
}
