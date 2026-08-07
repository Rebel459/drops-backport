package net.rebel459.drops_backported.registry;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.rebel459.drops_backported.DropsBackported;
import net.rebel459.drops_backported.entity.PotentSulfurBlockEntity;
import net.rebel459.unified.platform.UnifiedRegistries;
import net.rebel459.unified.util.registry.Supplied;

public class DBBlockEntityTypes {
    public static UnifiedRegistries.BlockEntityTypes BLOCK_ENTITY_TYPES = UnifiedRegistries.BlockEntityTypes.create(DropsBackported.VANILLA_ID);

    public static final Supplied<BlockEntityType<PotentSulfurBlockEntity>> POTENT_SULFUR = BLOCK_ENTITY_TYPES.register(
            "potent_sulfur", PotentSulfurBlockEntity::new
    );

    public static void init() {
    }
}
