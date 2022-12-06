package io._3650.itemupgrader.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import io._3650.itemupgrader.api.event.LivingUseTotemPostEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
	
	@Inject(method = "checkTotemDeathProtection(Lnet/minecraft/world/damagesource/DamageSource;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;broadcastEntityEvent(Lnet/minecraft/world/entity/Entity;B)V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
	private void itemupgrader_checkTotemDeathProtectionPost(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir, ItemStack itemstack) {
		LivingEntity thisLiving = (LivingEntity) (Object) this; //a moderate amount of trolling
		LivingUseTotemPostEvent event = new LivingUseTotemPostEvent(thisLiving, itemstack, damageSource);
		MinecraftForge.EVENT_BUS.post(event);
	}
	
}