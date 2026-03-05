package net.frozenblock.liukrast.livelyanimations.mixin;

import net.frozenblock.liukrast.livelyanimations.data.PlayerData;
import net.frozenblock.liukrast.livelyanimations.data.PlayerDataContainer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.frozenblock.liukrast.livelyanimations.LivelyPlayerAnimationFunc.smoothTo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements PlayerDataContainer {

    @Unique
    private final PlayerData data = PlayerData.generate();

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }


    @Override
    public PlayerData getPlayerData() {
        return data;
    }

    @Inject(at = @At("HEAD"), method = "tick()V")
    private void injectTick(CallbackInfo ci) {
        final float sneakX = smoothTo(data.getSneakX(), 0.1f, this.isSneaking() ? 1 : 0);
        data.setSneakX(sneakX);

        final float swimX = smoothTo(data.getSwimTime(), 0.1f, this.isInSwimmingPose() ? 1 : 0);
        data.setSwimTime(swimX);

    }
}
