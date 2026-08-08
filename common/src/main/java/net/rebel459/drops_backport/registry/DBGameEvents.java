package net.rebel459.drops_backport.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.gameevent.GameEvent;
import net.rebel459.drops_backport.DropsBackport;
import net.rebel459.unified.platform.UnifiedRegistries;

public class DBGameEvents {

   public static UnifiedRegistries.DeferredRegistry<GameEvent> EVENTS = UnifiedRegistries.DeferredRegistry.create(DropsBackport.VANILLA_ID, BuiltInRegistries.GAME_EVENT);

   public static final Holder<GameEvent> BOUNCE = register("bounce", 16);

   public static void init() {}

   private static Holder<GameEvent> register(final String name, final int notificationRadius) {
      return EVENTS.registerForHolder(name, () -> new GameEvent(notificationRadius));
   }
}