package io._3650.itemupgrader.api.event;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;

/**
 * Turns out forge added a totem event but theres still no post-activation event which I kinda need for my potions so yeah
 * Fired after a totem of undying (no support for modded totems I do that myself anyways) is consumed<br>
 * This event IS NOT cancellable
 * @author LegoMaster3650
 */
public class LivingUseTotemPostEvent extends Event {
	
	/**The {@linkplain LivingEntity} that triggered the totem*/
	public final LivingEntity living;
	/**The {@linkplain ItemStack} of the totem triggered*/
	public final ItemStack totem;
	/**The {@linkplain DamageSource} that triggered the totem*/
	public final DamageSource damageSource;
	
	public LivingUseTotemPostEvent(LivingEntity living, ItemStack totem, DamageSource damageSource) {
		this.living = living;
		this.totem = totem;
		this.damageSource = damageSource;
	}
	
}