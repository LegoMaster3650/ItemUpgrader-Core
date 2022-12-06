package io._3650.itemupgrader.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io._3650.itemupgrader.api.ItemUpgraderApi;
import io._3650.itemupgrader.api.data.UpgradeEntry;
import io._3650.itemupgrader.api.data.UpgradeEventData;
import io._3650.itemupgrader.events.ModSpecialEvents;
import io._3650.itemupgrader.registry.ModUpgradeActions;
import io._3650.itemupgrader.registry.types.UpgradeHitLimited;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

@Mixin(ThrownTrident.class)
public abstract class ThrownTridentMixin extends AbstractArrow implements UpgradeHitLimited {
	
	protected ThrownTridentMixin(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}
	
	@Shadow
	@Final
	static EntityDataAccessor<Byte> ID_LOYALTY;
	
	@Unique
	private static final EntityDataAccessor<Byte> UPGRADE_HITS = SynchedEntityData.defineId(ThrownTrident.class, EntityDataSerializers.BYTE);
	
	@Shadow
	boolean dealtDamage;
	
	@Inject(method = "<init>(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;)V", at = @At("TAIL"))
	private void itemupgrader_newThrownTrident(Level level, LivingEntity shooter, ItemStack stack, CallbackInfo ci) {
		this.entityData.set(UPGRADE_HITS, (byte)3);
		byte loyalty = this.entityData.get(ID_LOYALTY);
		if (loyalty > 0) this.entityData.set(ID_LOYALTY, ModSpecialEvents.loyaltyBonus(stack, shooter, loyalty));
		ItemUpgraderApi.runActions(ModUpgradeActions.TRIDENT_THROW, new UpgradeEventData.Builder(shooter)
				.entry(UpgradeEntry.ITEM, stack)
				.entry(UpgradeEntry.PROJECTILE, (ThrownTrident)(Object)this));
	}
	
	@Inject(method = "defineSynchedData", at = @At("TAIL"))
	private void itemupgrader_defineSynchedData(CallbackInfo ci) {
		this.entityData.define(UPGRADE_HITS, (byte)0);
	}
	
	@Unique
	@Override
	public void itemupgrader_setHits(byte hits) {
		this.entityData.set(UPGRADE_HITS, hits);
	}
	
	@Unique
	@Override
	public byte itemupgrader_getHits() {
		return this.entityData.get(UPGRADE_HITS);
	}
	
	@Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
	private void itemupgrader_addAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
		tag.putByte("UpgradeHitsRemaining", this.itemupgrader_getHits());
	}
	
	@Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
	private void itemupgrader_readAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
		this.itemupgrader_setHits(tag.getByte("UpgradeHitsRemaining"));
	}
	
}