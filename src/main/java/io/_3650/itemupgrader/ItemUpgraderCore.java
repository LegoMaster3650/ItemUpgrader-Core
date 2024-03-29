package io._3650.itemupgrader;

import com.mojang.logging.LogUtils;

import io._3650.itemupgrader.api.data.EntryCategory;
import io._3650.itemupgrader.api.data.UpgradeEntry;
import io._3650.itemupgrader.api.ingredient.TypedCriteria;
import io._3650.itemupgrader.api.ingredient.TypedIngredient;
import io._3650.itemupgrader.api.ingredient.UpgradeIngredient;
import io._3650.itemupgrader.api.registry.ItemUpgraderRegistry;
import io._3650.itemupgrader.api.serializer.UpgradeActionSerializer;
import io._3650.itemupgrader.api.serializer.UpgradeConditionSerializer;
import io._3650.itemupgrader.api.serializer.UpgradeResultSerializer;
import io._3650.itemupgrader.api.slot.InventorySlot;
import io._3650.itemupgrader.api.type.UpgradeAction;
import io._3650.itemupgrader.api.type.UpgradeCondition;
import io._3650.itemupgrader.api.type.UpgradeResult;
import io._3650.itemupgrader.client.ItemUpgraderClient;
import io._3650.itemupgrader.network.NetworkHandler;
import io._3650.itemupgrader.registry.ModArgumentTypes;
import io._3650.itemupgrader.registry.ModInventorySlots;
import io._3650.itemupgrader.registry.ModRecipes;
import io._3650.itemupgrader.registry.ModTypedCriteria;
import io._3650.itemupgrader.registry.ModUpgradeActions;
import io._3650.itemupgrader.registry.ModUpgradeConditions;
import io._3650.itemupgrader.registry.ModUpgradeResults;
import io._3650.itemupgrader.registry.config.Config;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;
import org.slf4j.Logger;

@Mod(ItemUpgraderCore.MOD_ID)
public class ItemUpgraderCore {
	
	private static final Logger LOGGER = LogUtils.getLogger();
	
	//I know this is item upgrader core not item upgrader but the mod really called Item Upgrader wont have many things meant to be exposed for external use ;P
	public static final String MOD_ID = "itemupgrader";
	
	//ok it works now cool
	public static final Supplier<IForgeRegistry<UpgradeActionSerializer<? extends UpgradeAction>>> ACTION_REGISTRY = ModUpgradeActions.ACTIONS.makeRegistry(() ->
			new RegistryBuilder<UpgradeActionSerializer<?>>().disableSaving());
	public static final Supplier<IForgeRegistry<UpgradeConditionSerializer<? extends UpgradeCondition>>> CONDITION_REGISTRY = ModUpgradeConditions.CONDITIONS.makeRegistry(() ->
			new RegistryBuilder<UpgradeConditionSerializer<?>>().disableSaving());
	public static final Supplier<IForgeRegistry<UpgradeResultSerializer<? extends UpgradeResult>>> RESULT_REGISTRY = ModUpgradeResults.RESULTS.makeRegistry(() ->
			new RegistryBuilder<UpgradeResultSerializer<?>>().disableSaving());
	
	public static final Supplier<IForgeRegistry<TypedCriteria>> TYPED_CRITERIA_REGISTRY = ModTypedCriteria.CRITERIA.makeRegistry(() -> new RegistryBuilder<TypedCriteria>().disableSync());
	
	public static final Supplier<IForgeRegistry<InventorySlot>> INVENTORY_SLOT_REGISTRY = ModInventorySlots.SLOTS.makeRegistry(() -> new RegistryBuilder<InventorySlot>());
	
	public ItemUpgraderCore() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		
		ModRecipes.SERIALIZERS.register(bus);
		ModUpgradeActions.ACTIONS.register(bus);
		ModUpgradeConditions.CONDITIONS.register(bus);
		ModUpgradeResults.RESULTS.register(bus);
		ModTypedCriteria.CRITERIA.register(bus);
		ModInventorySlots.SLOTS.register(bus);
		ModArgumentTypes.ARGUMENT_TYPES.register(bus);
		
		bus.addListener(this::setup);
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ItemUpgraderClient::new);
		
//		ModLoadingContext.get().registerConfig(Type.SERVER, Config.SERVER_SPEC, MOD_ID + "-server.toml");
		ModLoadingContext.get().registerConfig(Type.COMMON, Config.COMMON_SPEC, MOD_ID + "-common.toml");
		ModLoadingContext.get().registerConfig(Type.CLIENT, Config.CLIENT_SPEC, MOD_ID + "-client.toml");
		
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	private void setup(final FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			LOGGER.debug("Item Upgrader common setup");
			//doing this now just because, and because I want to make sure they load in this order just to be safe
			EntryCategory.init();
			UpgradeEntry.init();
			//actual common stuff
			CraftingHelper.register(ItemUpgraderRegistry.ugRes("typed"), TypedIngredient.Serializer.INSTANCE);
			CraftingHelper.register(ItemUpgraderRegistry.ugRes("upgrade"), UpgradeIngredient.Serializer.INSTANCE);
			NetworkHandler.init();
		});
	}
}