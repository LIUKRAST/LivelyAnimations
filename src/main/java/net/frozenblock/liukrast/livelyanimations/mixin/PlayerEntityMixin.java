package net.frozenblock.liukrast.livelyanimations.mixin;

import net.frozenblock.liukrast.livelyanimations.data.PlayerData;
import net.frozenblock.liukrast.livelyanimations.data.PlayerDataContainer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.stat.StatHandler;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import static net.frozenblock.liukrast.livelyanimations.LivelyPlayerAnimationFunc.smoothTo;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends LivingEntity implements PlayerDataContainer {

    @Unique
    private final PlayerData data = PlayerData.generate();

    protected ClientPlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }


    @Override
    public PlayerData getPlayerData() {
        return data;
    }

    @Inject(at = @At("HEAD"), method = "tick()V")
    private void injectTick() {
        final float sneakX = smoothTo(data.getSneakX(), 0.01f, this.isSneaking() ? 1 : 0);
        data.setSneakX(sneakX);

        final float swimX = smoothTo(data.getSwimTime(), 0.01f, this.isInSwimmingPose() ? 1 : 0);
        data.setSwimTime(swimX);
    }
}
