package io._3650.itemupgrader.api.registry;

import io._3650.itemupgrader.ItemUpgraderCore;
import io._3650.itemupgrader.api.ingredient.TypedCriteria;
import io._3650.itemupgrader.api.slot.InventorySlot;
import io._3650.itemupgrader.api.type.UpgradeAction;
import io._3650.itemupgrader.api.type.UpgradeCondition;
import io._3650.itemupgrader.api.type.UpgradeResult;
import net.minecraft.resources.ResourceLocation;

/**
 * Contains resource locations for the ItemUpgrader registries
 * @author LegoMaster3650
 *
 */
public class ItemUpgraderRegistry {
	
	/**The registry for {@linkplain UpgradeAction}*/
	public static final ResourceLocation ACTIONS = ugRes("upgrade_types");
	/**The registry for {@linkplain UpgradeCondition}*/
	public static final ResourceLocation CONDITIONS = ugRes("condition_types");
	/**The registry for {@linkplain UpgradeResult}*/
	public static final ResourceLocation RESULTS = ugRes("result_types");
	/**The registry for {@linkplain TypedCriteria}*/
	public static final ResourceLocation TYPED_CRITERIA = ugRes("typed_criteria");
	/**The registry for {@linkplain InventorySlot}*/
	public static final ResourceLocation INVENTORY_SLOTS = ugRes("inventory_slots");
	
	/**
	 * Mostly just public for internal use only for creating resource locations with the mod id easily<br>
	 * Please use your own Mod ID if possible instead of stealing mine with this method
	 * @param name The path to use for the resource location
	 * @return A ResourceLocation with the value itemupgrader:{@literal <name>}
	 */
	public static ResourceLocation ugRes(String name) {
		return new ResourceLocation(ItemUpgraderCore.MOD_ID, name);
	}
	
}