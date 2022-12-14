package io._3650.itemupgrader.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import io._3650.itemupgrader.events.ModSpecialEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

@Mixin(BowItem.class)
public class BowItemMixin {
	
	@Inject(method = "releaseUsing", at = @At(value = "INVOKE", target = "net/minecraft/world/item/ItemStack.hurtAndBreak(ILnet/minecraft/world/entity/LivingEntity;Ljava/util/function/Consumer;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
	private void itemupgrader_releaseUsing(ItemStack bow, Level level, LivingEntity living, int timeLeft, CallbackInfo ci, Player player, boolean hasAmmo, ItemStack stack, int i, float f, boolean isInfinite, ArrowItem item, AbstractArrow arrow) {
		ModSpecialEvents.bowShoot(bow, player, stack, arrow, hasAmmo);
	}
	
	@ModifyArg(method = "releaseUsing", at = @At(value = "INVOKE", target = "net/minecraft/world/entity/projectile/AbstractArrow.shootFromRotation(Lnet/minecraft/world/entity/Entity;FFFFF)V"), index = 5)
	private float itemupgrader_bowSpeed(Entity shooter, float x, float y, float z, float velocity, float inaccuracy) {
		if (shooter instanceof LivingEntity living) {
			ItemStack stack = living.getUseItem();
			if (stack.getItem() instanceof BowItem) {
				return ModSpecialEvents.arrowSpeed(stack, velocity);
			}
		}
		return velocity;
	}
	
	@ModifyArg(method = "releaseUsing", at = @At(value = "INVOKE", target = "net/minecraft/world/entity/projectile/AbstractArrow.shootFromRotation(Lnet/minecraft/world/entity/Entity;FFFFF)V"), index = 5)
	private float itemupgrader_bowAccuracy(Entity shooter, float x, float y, float z, float velocity, float inaccuracy) {
		if (shooter instanceof LivingEntity living) {
			ItemStack stack = living.getUseItem();
			if (stack.getItem() instanceof BowItem) {
				return ModSpecialEvents.arrowInaccuracy(stack, inaccuracy);
			}
		}
		return inaccuracy;
	}
	
}