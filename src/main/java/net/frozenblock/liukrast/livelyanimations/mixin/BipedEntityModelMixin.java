package net.frozenblock.liukrast.livelyanimations.mixin;

import net.frozenblock.liukrast.livelyanimations.LivelyPlayerAnimationFunc;
import net.frozenblock.liukrast.livelyanimations.data.PlayerData;
import net.frozenblock.liukrast.livelyanimations.data.PlayerDataContainer;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.frozenblock.liukrast.livelyanimations.LivelyPlayerAnimationFunc.interpolate;
import static net.frozenblock.liukrast.livelyanimations.LivelyPlayerAnimationFunc.smoothTo;

@Mixin(BipedEntityModel.class)
public class BipedEntityModelMixin {

    @Shadow
    private ModelPart head;
    @Shadow
    public ModelPart hat;
    @Shadow
    public ModelPart body;
    @Shadow
    public ModelPart rightArm;
    @Shadow
    public ModelPart leftArm;
    @Shadow
    public ModelPart rightLeg;
    @Shadow
    public ModelPart leftLeg;

    @Inject(at = @At("TAIL"), method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V")
    private <T extends LivingEntity> void injected(T livingEntity, float limbSwing, float limbAmount, float age, float netHeadYaw, float headPitch, CallbackInfo ci) {
        final PlayerData data = livingEntity instanceof PlayerEntity ? ((PlayerDataContainer)livingEntity).getPlayerData() : PlayerData.generate();
        final float walk_speed = limbAmount*2;
        final float animation_speed = 0.5f;
        final float delta = limbSwing - data.getLimbSwingOld();
        if(livingEntity.isOnGround()) data.setWalkTime(data.getWalkTime() + delta);
        data.setLimbSwingOld(limbSwing);
        final float time = data.getWalkTime() * animation_speed;

        float constantY = -(float) (Math.abs(Math.cos(time)) * Math.sqrt(walk_speed)) * 2;

        final float movY = smoothTo(data.getMovY(), 0.1f, (float) livingEntity.getVelocity().y);
        final float x = -movY/2f;
        data.setMovY(movY);

        final float sneakX = data.getSneakX();

        final float swimX = data.getSwimTime();
        final float sp = 0.5f * age;

        constantY = interpolate(interpolate(constantY, 0, x), (float) -(Math.cos(sp/2)*4), swimX);

        final float cos_const = (float) Math.cos(sp);
        final float sin_const = (float) Math.sin(sp);

        final float jumpValue = - 2f * Math.clamp(movY * 3, 0, 1);

        head.pivotY = constantY + 4.2f * sneakX + jumpValue;
        head.pivotZ = interpolate(-walk_speed, 0, x);
        head.pitch += interpolate((float)Math.toRadians(Math.abs(Math.cos(time))*10/(walk_speed+1)*walk_speed), (float) Math.toRadians(cos_const*10f), x);
        head.yaw += interpolate(0, (float) Math.toRadians(sin_const*5f), x);
        double radians = Math.toRadians(cos_const*2f);
        head.roll = interpolate((float)Math.toRadians(Math.sin(time)*2/(walk_speed+1)*walk_speed), (float) radians, x);

        body.pitch = interpolate(interpolate((float) Math.toRadians(walk_speed * 5), (float) Math.toRadians(sin_const*2f), x) + 0.5f * sneakX, (float) Math.toRadians(Math.sin(sp)*5-5), swimX);
        body.yaw = interpolate((float) Math.toRadians(-Math.cos(time)*4*walk_speed), (float) Math.toRadians(-cos_const*2f), x);
        body.roll = interpolate(0, (float) radians, x);
        body.pivotY = constantY + 3.2f * sneakX + jumpValue;
        body.pivotZ = interpolate(-walk_speed, 0, x);

        final float armPitch = (float) Math.toRadians(-Math.cos(time)*walk_speed*20);
        final float armZ = -walk_speed;

        double radians2 = Math.toRadians(Math.cos(sp / 2) * 90 - 90);
        rightArm.pitch = interpolate(interpolate(armPitch, 0, x) + 0.4f * sneakX, (float) radians2, swimX);
        final float configAngle = (float) -Math.sin(sp/2);
        rightArm.yaw = interpolate(0, (float) Math.toRadians(configAngle * 45 + 45), swimX);
        rightArm.roll = interpolate((float) Math.toRadians((Math.cos(time*2)*4+4)*walk_speed), (float) Math.toRadians(cos_const*10+75 - movY*20), x);
        rightArm.pivotY = constantY + 3.2f * sneakX + 2 + jumpValue;
        rightArm.pivotZ = interpolate(armZ, 0, x);

        leftArm.pitch = interpolate(interpolate(-armPitch, 0, x) + 0.4f * sneakX, (float) radians2, swimX);
        leftArm.roll = interpolate((float) Math.toRadians((Math.cos(time*2)*4-4)*walk_speed), (float) Math.toRadians(sin_const*10-75 + movY*20), x);
        leftArm.yaw = interpolate(0, (float) Math.toRadians(-configAngle * 45 - 45), swimX);
        leftArm.pivotY = constantY+ 3.2f * sneakX + 2 + jumpValue;
        leftArm.pivotZ = interpolate(armZ, 0, x);

        final float legZ = (float) (-Math.cos(time)*Math.sqrt(walk_speed)*2);

        double radians1 = Math.toRadians(-Math.sin(sp) * 10);
        rightLeg.pitch = interpolate(
                interpolate(
                        (float) Math.toRadians(LivelyPlayerAnimationFunc.legAnimation(time)*20*walk_speed + 4*walk_speed),
                        (float) Math.toRadians(cos_const*5),
                        x),
                (float) radians1,
                swimX);
        rightLeg.roll = interpolate(interpolate(0, (float) Math.toRadians(sin_const*5+10), x), (float) Math.toRadians(Math.cos(sp)*10+10), swimX);
        rightLeg.pivotY+=constantY - (Math.sin(time)/2+1)*Math.sqrt(walk_speed);
        rightLeg.pivotZ=interpolate(interpolate(-legZ, 0, x) + 4f * sneakX, (float) (Math.sin(sp)-2), swimX);

        leftLeg.pitch= interpolate(
                interpolate(
                        (float) Math.toRadians(LivelyPlayerAnimationFunc.legAnimation(time + Math.PI)*20*walk_speed + 4*walk_speed),
                        (float) Math.toRadians(-cos_const*5),
                        x),
                (float) radians1,
                swimX
        );
        leftLeg.roll = interpolate(interpolate(0, (float) Math.toRadians(-sin_const*5-10), x), (float) Math.toRadians(-Math.cos(sp)*10-10), swimX);
        leftLeg.pivotY+=constantY - (-Math.sin(time)/2+1)*Math.sqrt(walk_speed);
        leftLeg.pivotZ=interpolate(interpolate(legZ, 0, x) + 4f * sneakX, (float) (Math.sin(sp)-2), swimX);

        this.hat.copyTransform(this.head);
    }

}
