package net.rebel459.drops_backport.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.trading.TradeSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Villager.class)
public abstract class VillagerMixin {

    @Shadow
    protected abstract void updateSpecialPrices(Player player);

    @Shadow
    protected abstract void resendOffersToTradingPlayer();

    @Shadow
    protected abstract void increaseMerchantCareer(ServerLevel level);

    @WrapOperation(method = "customServerAiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/npc/villager/Villager;isTrading()Z", ordinal = 0))
    private boolean skipTradeCheck(Villager instance, Operation<Boolean> original) {
        return true;
    }

    @Inject(method = "updateTrades", at = @At(value = "TAIL"))
    private void updateVillagerTrades(ServerLevel level, CallbackInfo ci, @Local(name = "trades") ResourceKey<TradeSet> trades) {
        if (trades != null) {
            Player tradingPlayer = Villager.class.cast(this).getTradingPlayer();
            if (tradingPlayer != null) {
                this.updateSpecialPrices(tradingPlayer);
                this.resendOffersToTradingPlayer();
            }
        }
    }

    @WrapOperation(method = "rewardTradeXp", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/npc/villager/Villager;shouldIncreaseLevel()Z"))
    private boolean updateMerchantLevel(Villager villager, Operation<Boolean> original) {
        boolean result = original.call(villager);
        if (result && villager.level() instanceof ServerLevel level) {
            this.increaseMerchantCareer(level);
            villager.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 0));
        }
        return result;
    }
}
