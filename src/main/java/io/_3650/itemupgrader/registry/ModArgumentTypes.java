package io._3650.itemupgrader.registry;

import io._3650.itemupgrader.ItemUpgraderCore;
import io._3650.itemupgrader.commands.arguments.ItemUpgradeArgument;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModArgumentTypes {
	
	public static final DeferredRegister<ArgumentTypeInfo<?, ?>> ARGUMENT_TYPES = DeferredRegister.create(ForgeRegistries.COMMAND_ARGUMENT_TYPES, ItemUpgraderCore.MOD_ID);
	
	public static final RegistryObject<ArgumentTypeInfo<?, ?>> ITEM_UPGRADE = ARGUMENT_TYPES.register("item_upgrade", () -> ArgumentTypeInfos.registerByClass(ItemUpgradeArgument.class, SingletonArgumentInfo.contextFree(ItemUpgradeArgument::upgrade)));
	
}