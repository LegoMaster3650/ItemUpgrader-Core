package io._3650.itemupgrader.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import io._3650.itemupgrader.events.ModSpecialEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

@Mixin(CrossbowItem.class)
public abstract class CrossbowItemMixin {
	
	@Inject(method = "shootProjectile", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;hurtAndBreak(ILnet/minecraft/world/entity/LivingEntity;Ljava/util/function/Consumer;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
	private static void itemupgrader_shootProjectile(Level level, LivingEntity shooter, InteractionHand hand, ItemStack crossbow, ItemStack stack, float pitch, boolean isCreative, float velocity, float inaccuracy, float projectileAngle, CallbackInfo ci, boolean isFirework, Projectile projectile) {
		ModSpecialEvents.crossbowShoot(crossbow, shooter, stack, projectile);
	}
	
	@Inject(method = "getShootingPower", at = @At("RETURN"), cancellable = true)
	private static void itemupgrader_crossbowSpeed(ItemStack stack, CallbackInfoReturnable<Float> cir) {
		float originalVelocity = cir.getReturnValueF();
		float velocity = ModSpecialEvents.arrowSpeed(stack, originalVelocity);
		if (originalVelocity != velocity) cir.setReturnValue(velocity);
	}
	
	@ModifyVariable(method = "shootProjectile(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;FZFFF)V", at = @At("HEAD"), ordinal = 2, argsOnly = true)
	private static float itemupgrader_crossbowInaccuracy(float inaccuracy, Level level, LivingEntity shooter, InteractionHand hand, ItemStack crossbow, ItemStack stack) {
		return ModSpecialEvents.arrowInaccuracy(crossbow, inaccuracy);
	}
	
}