package net.rebel459.drops_backport.network;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.rebel459.drops_backport.DropsBackport;
import net.rebel459.unified.platform.UnifiedHelpers;

public record TeleportTrailPacket(BlockPos from, BlockPos to) implements CustomPacketPayload {

    public static final Type<TeleportTrailPacket> TYPE = new Type<>(DropsBackport.modId("teleport_trail"));

    public static final StreamCodec<RegistryFriendlyByteBuf, TeleportTrailPacket> CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC,
                    TeleportTrailPacket::from,
                    BlockPos.STREAM_CODEC,
                    TeleportTrailPacket::to,
                    TeleportTrailPacket::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void init() {
        UnifiedHelpers.NETWORKING.registerPlayToClient(TeleportTrailPacket.TYPE, TeleportTrailPacket.CODEC, (packet, player) -> {
            RandomSource random = player.level().getRandom();
            for (int particle = 0; particle < 128; particle++) {
                double randomDistance = random.nextDouble();
                float velocityX = (random.nextFloat() - 0.5F) * 0.2F;
                float velocityY = (random.nextFloat() - 0.5F) * 0.2F;
                float velocityZ = (random.nextFloat() - 0.5F) * 0.2F;
                double x = Mth.lerp(randomDistance, packet.to().getX(), packet.from().getX()) + (random.nextDouble() - 0.5) + 0.5;
                double y = Mth.lerp(randomDistance, packet.to().getY(), packet.from().getY()) + random.nextDouble() - 0.5;
                double z = Mth.lerp(randomDistance, packet.to().getZ(), packet.from().getZ()) + (random.nextDouble() - 0.5) + 0.5;
                player.level().addParticle(ParticleTypes.PORTAL, x, y, z, velocityX, velocityY, velocityZ);
            }
        });
    }
}