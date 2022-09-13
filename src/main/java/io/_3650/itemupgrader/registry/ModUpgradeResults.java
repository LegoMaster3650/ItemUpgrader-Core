package io._3650.itemupgrader.registry;

import io._3650.itemupgrader.ItemUpgrader;
import io._3650.itemupgrader.api.registry.ItemUpgraderRegistry;
import io._3650.itemupgrader.api.serializer.UpgradeResultSerializer;
import io._3650.itemupgrader.upgrades.results.EffectUpgradeResult;
import io._3650.itemupgrader.upgrades.results.ParticleUpgradeResult;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModUpgradeResults {
	
	public static final DeferredRegister<UpgradeResultSerializer<?>> RESULTS = DeferredRegister.create(ItemUpgraderRegistry.RESULTS, ItemUpgrader.MOD_ID);
	
	public static final RegistryObject<EffectUpgradeResult.Serializer> EFFECT = RESULTS.register("effect", () -> new EffectUpgradeResult.Serializer());
	public static final RegistryObject<ParticleUpgradeResult.Serializer> PARTICLE = RESULTS.register("particle", () -> new ParticleUpgradeResult.Serializer());
	
}