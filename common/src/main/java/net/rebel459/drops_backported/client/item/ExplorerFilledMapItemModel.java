package net.rebel459.drops_backported.client.item;

import com.mojang.blaze3d.platform.Transparency;
import com.mojang.math.Quadrant;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.color.item.Constant;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.color.item.MapColor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.dispatch.BlockModelRotation;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.item.CuboidItemModelWrapper;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ResolvableModel;
import net.minecraft.client.resources.model.ResolvedModel;
import net.minecraft.client.resources.model.cuboid.CuboidFace;
import net.minecraft.client.resources.model.cuboid.FaceBakery;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.geometry.BakedQuad.MaterialInfo;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.component.MapDecorations;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.joml.Matrix4fc;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ExplorerFilledMapItemModel implements ItemModel {
    private static final Identifier FILLED_MAP_MODEL = Identifier.withDefaultNamespace("item/filled_map");
    private static final Identifier EXPLORER_FILLED_MAP_MODEL = Identifier.withDefaultNamespace("item/explorer_filled_map");
    private static final List<ItemTintSource> DEFAULT_TINTS = List.of(new Constant(-1), new MapColor(4603950));
    public static final Identifier TYPE = Identifier.fromNamespaceAndPath("drops_backported", "explorer_filled_map");

    private final ItemModel defaultBaseModel;
    private final ItemModel explorerBaseModel;
    private final ModelBaker modelBaker;
    private final SpriteGetter sprites;
    private final ModelRenderProperties properties;
    private final Matrix4fc transformation;
    private final Map<Identifier, List<BakedQuad>> overlayQuads = new ConcurrentHashMap<>();

    private ExplorerFilledMapItemModel(
            ItemModel defaultBaseModel,
            ItemModel explorerBaseModel,
            ModelBaker modelBaker,
            SpriteGetter sprites,
            ModelRenderProperties properties,
            Matrix4fc transformation
    ) {
        this.defaultBaseModel = defaultBaseModel;
        this.explorerBaseModel = explorerBaseModel;
        this.modelBaker = modelBaker;
        this.sprites = sprites;
        this.properties = properties;
        this.transformation = transformation;
    }

    @Override
    public void update(
            ItemStackRenderState renderState,
            ItemStack stack,
            ItemModelResolver resolver,
            ItemDisplayContext displayContext,
            ClientLevel level,
            ItemOwner owner,
            int seed
    ) {
        Identifier decoration = this.findExplorationMapDecoration(stack, level);
        if (decoration == null) {
            this.defaultBaseModel.update(renderState, stack, resolver, displayContext, level, owner, seed);
            return;
        }

        this.explorerBaseModel.update(renderState, stack, resolver, displayContext, level, owner, seed);
        ItemStackRenderState.LayerRenderState layer = renderState.newLayer();
        this.properties.applyToLayer(layer, displayContext);
        layer.setLocalTransform(this.transformation);
        layer.prepareQuadList().addAll(this.overlayQuads.computeIfAbsent(decoration, this::createOverlayQuads));
    }

    private Identifier findExplorationMapDecoration(ItemStack stack, ClientLevel level) {
        Identifier componentDecoration = this.findComponentDecoration(stack);
        if (componentDecoration != null) {
            return componentDecoration;
        }

        if (level == null) {
            return null;
        }

        MapItemSavedData mapData = MapItem.getSavedData(stack, level);
        if (mapData == null || !mapData.isExplorationMap()) {
            return null;
        }

        for (MapDecoration decoration : mapData.getDecorations()) {
            if (decoration.type().value().explorationMapElement()) {
                return decoration.getSpriteLocation();
            }
        }

        return null;
    }

    private Identifier findComponentDecoration(ItemStack stack) {
        MapDecorations decorations = stack.getOrDefault(DataComponents.MAP_DECORATIONS, MapDecorations.EMPTY);
        for (MapDecorations.Entry entry : decorations.decorations().values()) {
            return entry.type().value().assetId();
        }

        return null;
    }

    private List<BakedQuad> createOverlayQuads(Identifier decoration) {
        SpriteId spriteId = new SpriteId(Sheets.MAP_DECORATIONS_SHEET, decoration);
        TextureAtlasSprite sprite = this.sprites.get(spriteId);
        MaterialInfo materialInfo = new MaterialInfo(
                sprite,
                ChunkSectionLayer.byTransparency(Transparency.TRANSLUCENT),
                RenderTypes.text(sprite.atlasLocation()),
                CuboidFace.NO_TINT,
                false,
                0
        );

        BakedQuad quad = FaceBakery.bakeQuad(
                this.modelBaker.interner(),
                // do not touch z, and mirror any additions / subtractions on x + y across both vectors
                new Vector3f(6.0F, 2.0F, 8.6F),
                new Vector3f(14.0F, 10.0F, 8.6F),
                new CuboidFace.UVs(0.0F, 0.0F, 16.0F, 16.0F),
                Quadrant.R0,
                materialInfo,
                Direction.SOUTH,
                BlockModelRotation.IDENTITY,
                null
        );
        return List.of(quad);
    }

    public record Unbaked() implements ItemModel.Unbaked {
        public static final Unbaked INSTANCE = new Unbaked();
        public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(INSTANCE);

        @Override
        public MapCodec<Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public void resolveDependencies(ResolvableModel.Resolver resolver) {
            resolver.markDependency(FILLED_MAP_MODEL);
            resolver.markDependency(EXPLORER_FILLED_MAP_MODEL);
        }

        @Override
        public ItemModel bake(ItemModel.BakingContext context, Matrix4fc transformation) {
            ItemModel defaultBaseModel = new CuboidItemModelWrapper.Unbaked(FILLED_MAP_MODEL, Optional.empty(), DEFAULT_TINTS).bake(context, transformation);
            ItemModel explorerBaseModel = new CuboidItemModelWrapper.Unbaked(EXPLORER_FILLED_MAP_MODEL, Optional.empty(), List.of()).bake(context, transformation);
            ResolvedModel resolvedModel = context.blockModelBaker().getModel(EXPLORER_FILLED_MAP_MODEL);
            ModelRenderProperties properties = ModelRenderProperties.fromResolvedModel(
                    context.blockModelBaker(),
                    resolvedModel,
                    resolvedModel.getTopTextureSlots()
            );
            return new ExplorerFilledMapItemModel(defaultBaseModel, explorerBaseModel, context.blockModelBaker(), context.sprites(), properties, transformation);
        }
    }
}
