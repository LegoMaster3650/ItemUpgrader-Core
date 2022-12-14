package io._3650.itemupgrader.api.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

/**
 * Main class for upgrade events.<br>
 * Event order is {@linkplain Remove} -> {@linkplain Replace} -> {@linkplain Apply.Pre} -> {@linkplain Apply.Post}
 * @author LegoMaster3650
 */
public abstract class UpgradeEvent extends Event {
	
	/**
	 * The main {@linkplain ItemStack} for this event
	 */
	public final ItemStack stack;
	/**
	 * The {@linkplain ResourceLocation} id of the upgrade associated with this event
	 */
	public final ResourceLocation upgradeId;
	
	/**
	 * Constructs a new upgrade event with the two parameters
	 * @param stack {@linkplain #stack}
	 * @param upgradeId {@linkplain #upgradeId}
	 */
	private UpgradeEvent(ItemStack stack, ResourceLocation upgradeId) {
		this.stack = stack;
		this.upgradeId = upgradeId;
	}
	
	/**
	 * Base for the two upgrade apply events
	 * @see Pre
	 * @see Post
	 */
	public static abstract class Apply extends UpgradeEvent {
		
		private Apply(ItemStack stack, ResourceLocation upgradeId) {
			super(stack, upgradeId);
		}
		
		/**
		 * Event fired before an upgrade is applied to an item
		 */
		@Cancelable
		public static class Pre extends Apply {
			/**
			 * Constructs a new {@linkplain Pre}
			 * @param stack {@linkplain #stack}
			 * @param upgradeId {@linkplain #upgradeId}
			 */
			public Pre(ItemStack stack, ResourceLocation upgradeId) {
				super(stack, upgradeId);
			}
			
			@Override
			public boolean isCancelable() {
				return true;
			}
		}
		
		/**
		 * Event fired after an upgrade is applied to an item
		 */
		public static class Post extends Apply {
			/**
			 * Constructs a new {@linkplain Post}
			 * @param stack {@linkplain #stack}
			 * @param upgradeId {@linkplain #upgradeId}
			 */
			public Post(ItemStack stack, ResourceLocation upgradeId) {
				super(stack, upgradeId);
			}
		}
		
	}
	
	/**
	 * Event fired before an upgrade is removed from an item
	 */
	public static class Remove extends UpgradeEvent {
		/**
		 * Constructs a new {@linkplain Remove}
		 * @param stack {@linkplain #stack}
		 * @param upgradeId {@linkplain #upgradeId}
		 */
		public Remove(ItemStack stack, ResourceLocation upgradeId) {
			super(stack, upgradeId);
		}
		
	}
	
	/**
	 * Event fired before an existing upgrade on an item is replaced
	 */
	@Cancelable
	public static class Replace extends UpgradeEvent {
		
		/**
		 * The previous upgrade ID being replaced
		 */
		public final ResourceLocation previousUpgradeId;
		
		/**
		 * Constructs a new {@linkplain Replace}
		 * @param stack {@linkplain #stack}
		 * @param upgradeId {@linkplain #upgradeId}
		 * @param previousUpgradeId {@linkplain #previousUpgradeId}
		 */
		public Replace(ItemStack stack, ResourceLocation upgradeId, ResourceLocation previousUpgradeId) {
			super(stack, upgradeId);
			this.previousUpgradeId = previousUpgradeId;
		}
		
		@Override
		public boolean isCancelable() {
			return true;
		}
		
	}
	
}