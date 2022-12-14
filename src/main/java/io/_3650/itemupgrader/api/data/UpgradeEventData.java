package io._3650.itemupgrader.api.data;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;

import io._3650.itemupgrader.api.slot.InventorySlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.LogicalSide;

/**
 * Main data holder for event data in ItemUpgrader<br>
 * This system was made due to the mod's datapack-oriented nature and the need to establish consistancy.<br>
 * <br>
 * There are two maps of entries present, the {@code entries} map and the {@code results} map.<br>
 * The {@code entries} map is immutable and contains the data passed into this event initially.<br>
 * The {@code results} map is meant to hold return values and thus can be modified. 
 * @author LegoMaster3650
 * 
 * @see Builder
 * @see io._3650.itemupgrader.api.ItemUpgraderApi
 */
public class UpgradeEventData {
	
	private final UpgradeEntrySet entrySet;
	private final Map<UpgradeEntry<?>, Object> entries;
	private final Set<UpgradeEntry<?>> modifiableEntries;
	private final ItemStack ownerItem;
	
	private UpgradeEventData(UpgradeEntrySet entrySet, Map<UpgradeEntry<?>, Object> entries, Set<UpgradeEntry<?>> modifiableEntries, ItemStack ownerItem) {
		this.entrySet = entrySet;
		this.entries = entries;
		this.modifiableEntries = ImmutableSet.copyOf(modifiableEntries);
		this.ownerItem = ownerItem;
	}
	
	/**
	 * Gets the entry set this event data was built with and verified against.
	 * @return This event data's entry set
	 */
	public UpgradeEntrySet getEntrySet() {
		return this.entrySet;
	}
	
	/**
	 * Gets the item that triggered this event run
	 * @return The {@linkplain ItemStack} that triggered this event
	 */
	public ItemStack getOwnerItem() {
		return this.ownerItem;
	}
	
	private boolean resultSuccess = false;
	
	/**
	 * Gets whether the last result to be run was successful (defaults to false)
	 * @return Whether the last result to be run was successful (defaults to false)
	 */
	public boolean getLastResultSuccess() {
		return this.resultSuccess;
	}
	
	/**
	 * Checks if the event data contains an entry for this type
	 * @param entry The {@linkplain UpgradeEntry} type to check for
	 * @return If the entry type is present
	 */
	public boolean hasEntry(UpgradeEntry<?> entry) {
		return entry.isNullable() ? this.entries.containsKey(entry) : this.getEntryOrNull(entry) != null;
	}
	
	/**
	 * Gets an entry value for this event, erroring if null or missing
	 * @param <T> The data type provided by this entry
	 * @param entry The {@linkplain UpgradeEntry} type to get
	 * @return The value of the entry if present
	 * @throws NoSuchElementException If the entry isn't present
	 * @see #getEntryOrNull(UpgradeEntry)
	 * @see #getOptional(UpgradeEntry)
	 */
	@Nonnull
	public <T> T getEntry(UpgradeEntry<T> entry) throws NoSuchElementException {
		T t = this.getEntryOrNull(entry);
		if (t == null) {
			throw new NoSuchElementException("Upgrade event missing nonnull entry " + entry);
		} else {
			return t;
		}
	}
	
	/**
	 * Gets an entry value for this event, returning null if missing
	 * @param <T> The data type provided by this entry
	 * @param entry The {@linkplain UpgradeEntry} type to get
	 * @return The value of the entry if present, or {@code null} if not
	 */
	@SuppressWarnings("unchecked") //Pretty sure it's type safe enough so I'll use the forbidden suppression
	@Nullable
	public <T> T getEntryOrNull(UpgradeEntry<T> entry) {
		return (T) this.entries.get(entry);
	}
	
	/**
	 * Gets an optional of this entry's value which is empty if not present
	 * @param <T> The data type provided by this entry
	 * @param entry The {@linkplain UpgradeEntry} type to get
	 * @return An {@linkplain Optional} of the entry's value if present
	 */
	public <T> Optional<T> getOptional(UpgradeEntry<T> entry) {
		return Optional.ofNullable(this.getEntryOrNull(entry));
	}
	
	/**
	 * Checks if the event allows the given entry to be modified
	 * @param entry The {@linkplain UpgradeEntry} type to check for
	 * @return If the event allows this entry to be modified
	 * @see #hasModifiableEntry(UpgradeEntry)
	 */
	public boolean isModifiableEntry(UpgradeEntry<?> entry) {
		return this.modifiableEntries.contains(entry);
	}
	
	/**
	 * Checks if the event data contains an entry for this type and if it is modifiable
	 * @param entry The {@linkplain UpgradeEntry} type to check
	 * @return Whether the result specified has a value
	 * @see #hasEntry(UpgradeEntry)
	 * @see #isModifiableEntry(UpgradeEntry)
	 */
	public boolean hasModifiableEntry(UpgradeEntry<?> entry) {
		return this.hasEntry(entry) && this.isModifiableEntry(entry);
	}
	
	/**
	 * Sets a modifiable entry for this event if allowed
	 * @param <T> The data type held by this result
	 * @param entry The {@linkplain UpgradeEntry} type to set
	 * @param value The data to store for this entry
	 * @throws IllegalArgumentException if the provided entry doesn't allow null values but a null value was given anyways
	 */
	public <T> void setModifiableEntry(UpgradeEntry<T> entry, T value) throws IllegalArgumentException {
		if (!entry.isNullable() && value == null) throw new IllegalArgumentException("Tried to set the value of non-nullable entry " + entry + " to null");
		if (!this.isModifiableEntry(entry)) throw new IllegalStateException("Tried to set the value of unmodifiable entry " + entry);
		else this.entries.put(entry, value);
	}
	
	/**
	 * A utility function to quickly check if this event can be cancelled
	 * @return If the event is cancellable
	 */
	public boolean isCancellable() {
		return this.hasModifiableEntry(UpgradeEntry.CANCELLED);
	}
	
	/**
	 * A utility function to quickly cancel the event if it is cancellable
	 */
	public void cancel() {
		this.setModifiableEntry(UpgradeEntry.CANCELLED, true);
	}
	
	/**
	 * A utility function to quickly check if the event is cancelled
	 * @return If the event is cancelled
	 */
	public boolean isCancelled() {
		return this.getOptional(UpgradeEntry.CANCELLED).orElse(false);
	}
	
	/**
	 * Consumes the event, preventing any further runs of this action on any other items
	 */
	public void consume() {
		this.setModifiableEntry(UpgradeEntry.CONSUMED, true);
	}
	
	/**
	 * Whether or not to run this action for any more items
	 * @return Whether or not to run this action for any more items
	 */
	public boolean isConsumed() {
		return this.getBoolEntry(UpgradeEntry.CONSUMED);
	}
	
	/**
	 * A utility function to safely get a boolean result or false if not present
	 * @param entry The boolean result to get
	 * @return The boolean output of the result
	 */
	public boolean getBoolEntry(UpgradeEntry<Boolean> entry) {
		return this.getOptional(entry).orElse(false);
	}
	
	/**
	 * Forcefully modifies one existing entry in this event data
	 * @param <T> The data type held by this result
	 * @param entry The {@linkplain UpgradeEntry} type to set
	 * @param value The data to store for this entry
	 * @return The modified event data (this)
	 * @throws IllegalArgumentException if the provided entry doesn't allow null values but a null value was given anyways
	 */
	public <T> UpgradeEventData forceModifyEntry(UpgradeEntry<T> entry, T value) throws IllegalArgumentException {
		if (!entry.isNullable() && value == null) throw new IllegalArgumentException("Tried to set the value of non-nullable entry " + entry + " to null");
		if (!this.entries.containsKey(entry)) throw new IllegalStateException("Tried to force-set the value of unpresent entry " + entry);
		else this.entries.put(entry, value);
		return this;
	}
	
	/**
	 * Forcefully modifies the existing contents of the event data using an EMPTY builder
	 * @param builderConsumer A {@linkplain Consumer} of the generated {@linkplain Builder}
	 * @return The modified event data (this)
	 * @throws IllegalArgumentException if the provided entry doesn't allow null values but a null value was given anyways
	 */
	public UpgradeEventData modify(Consumer<Builder> builderConsumer) throws IllegalArgumentException {
		Builder builder = new Builder();
		builderConsumer.accept(builder);
		for (var entry : builder.entries.keySet()) {
			if (this.entries.containsKey(entry)) {
				Object value = builder.entries.get(entry);
				if (!entry.isNullable() && value == null) throw new IllegalArgumentException("Tried to set the value of non-nullable entry " + entry + " to null");
				this.entries.put(entry, builder.entries.get(entry));
				if (builder.modifiableEntries.contains(entry)) this.modifiableEntries.add(entry);
			}
		}
		return this;
	}
	
	/**
	 * The builder for {@linkplain UpgradeEventData}
	 * @author LegoMaster3650
	 *
	 */
	public static class Builder {
		
		private final Map<UpgradeEntry<?>, Object> entries = Maps.newIdentityHashMap();
		private final Set<UpgradeEntry<?>> provided = Sets.newIdentityHashSet();
		private final Set<UpgradeEntry<?>> modifiableEntries = Sets.newIdentityHashSet();
		
		/**
		 * Constructs a builder with the {@link UpgradeEntry#ITEM} property set
		 * @param stack An {@linkplain ItemStack} to use for context
		 */
		public Builder(ItemStack stack) {
			this.entry(UpgradeEntry.ITEM, stack);
		}
		
		/**
		 * <b>Complaint with {@linkplain UpgradeEntrySet#PLAYER_SLOT_ITEM}</b><br>
		 * Constructs a builder with following properties automatically determined:<br>
		 * {@linkplain UpgradeEntry#ITEM}<br>
		 * {@linkplain UpgradeEntry#SLOT}<br>
		 * {@linkplain UpgradeEntry#PLAYER}<br>
		 * {@linkplain UpgradeEntry#LIVING}<br>
		 * {@linkplain UpgradeEntry#ENTITY}<br>
		 * {@linkplain UpgradeEntry#POSITION}<br>
		 * {@linkplain UpgradeEntry#LEVEL}<br>
		 * {@linkplain UpgradeEntry#SIDE}
		 * @param player A {@linkplain Player} to use for context
		 * @param slot An {@linkplain InventorySlot} to use for context
		 */
		public Builder(Player player, InventorySlot slot) {
			this.entry(UpgradeEntry.ITEM, slot.getItem(player))
				.entry(UpgradeEntry.SLOT, slot)
				.entry(UpgradeEntry.PLAYER, player)
				.entry(UpgradeEntry.LIVING, player)
				.entry(UpgradeEntry.ENTITY, player)
				.entry(UpgradeEntry.POSITION, player.position())
				.entry(UpgradeEntry.LEVEL, player.level)
				.entry(UpgradeEntry.SIDE, player.level.isClientSide ? LogicalSide.CLIENT : LogicalSide.SERVER);
		}
		
		/**
		 * <b>Complaint with {@linkplain UpgradeEntrySet#PLAYER_SLOT_ITEM}</b><br>
		 * Constructs a builder with following properties automatically determined:<br>
		 * {@linkplain UpgradeEntry#ITEM}<br>
		 * {@linkplain UpgradeEntry#SLOT}<br>
		 * {@linkplain UpgradeEntry#PLAYER}<br>
		 * {@linkplain UpgradeEntry#LIVING}<br>
		 * {@linkplain UpgradeEntry#ENTITY}<br>
		 * {@linkplain UpgradeEntry#POSITION}<br>
		 * {@linkplain UpgradeEntry#LEVEL}<br>
		 * {@linkplain UpgradeEntry#SIDE}
		 * @param player A {@linkplain Player} to use for context
		 * @param slot An {@linkplain EquipmentSlot} to use for context
		 */
		public Builder(Player player, EquipmentSlot slot) {
			InventorySlot invSlot = InventorySlot.byBase(slot);
			this.entry(UpgradeEntry.ITEM, invSlot.getItem(player))
			.entry(UpgradeEntry.SLOT, invSlot)
			.entry(UpgradeEntry.PLAYER, player)
			.entry(UpgradeEntry.LIVING, player)
			.entry(UpgradeEntry.ENTITY, player)
			.entry(UpgradeEntry.POSITION, player.position())
			.entry(UpgradeEntry.LEVEL, player.level)
			.entry(UpgradeEntry.SIDE, player.level.isClientSide ? LogicalSide.CLIENT : LogicalSide.SERVER);
		}
		
		/**
		 * <b>Complaint with {@linkplain UpgradeEntrySet#PLAYER}</b><br>
		 * Constructs a builder with following properties automatically determined:<br>
		 * {@linkplain UpgradeEntry#PLAYER}<br>
		 * {@linkplain UpgradeEntry#LIVING}<br>
		 * {@linkplain UpgradeEntry#ENTITY}<br>
		 * {@linkplain UpgradeEntry#POSITION}<br>
		 * {@linkplain UpgradeEntry#LEVEL}<br>
		 * {@linkplain UpgradeEntry#SIDE}
		 * @param player A {@linkplain Player} to use for context
		 */
		public Builder(Player player) {
			this.entry(UpgradeEntry.PLAYER, player)
				.entry(UpgradeEntry.LIVING, player)
				.entry(UpgradeEntry.ENTITY, player)
				.entry(UpgradeEntry.POSITION, player.position())
				.entry(UpgradeEntry.LEVEL, player.level)
				.entry(UpgradeEntry.SIDE, player.level.isClientSide ? LogicalSide.CLIENT : LogicalSide.SERVER);
		}
		
		/**
		 * <b>Complaint with {@linkplain UpgradeEntrySet#LIVING_SLOT_ITEM}</b><br>
		 * Constructs a builder with following properties automatically determined:<br>
		 * {@linkplain UpgradeEntry#ITEM}<br>
		 * {@linkplain UpgradeEntry#SLOT}<br>
		 * {@linkplain UpgradeEntry#LIVING}<br>
		 * {@linkplain UpgradeEntry#ENTITY}<br>
		 * {@linkplain UpgradeEntry#POSITION}<br>
		 * {@linkplain UpgradeEntry#LEVEL}<br>
		 * {@linkplain UpgradeEntry#SIDE}
		 * @param living A {@linkplain LivingEntity} to use for context
		 * @param slot An {@linkplain InventorySlot} to use for context
		 */
		public Builder(LivingEntity living, InventorySlot slot) {
			this.entry(UpgradeEntry.ITEM, slot.getItem(living))
				.entry(UpgradeEntry.SLOT, slot)
				.entry(UpgradeEntry.LIVING, living)
				.entry(UpgradeEntry.ENTITY, living)
				.entry(UpgradeEntry.POSITION, living.position())
				.entry(UpgradeEntry.LEVEL, living.level)
				.entry(UpgradeEntry.SIDE, living.level.isClientSide ? LogicalSide.CLIENT : LogicalSide.SERVER);
		}
		
		/**
		 * <b>Complaint with {@linkplain UpgradeEntrySet#LIVING_SLOT_ITEM}</b><br>
		 * Constructs a builder with following properties automatically determined:<br>
		 * {@linkplain UpgradeEntry#ITEM}<br>
		 * {@linkplain UpgradeEntry#SLOT}<br>
		 * {@linkplain UpgradeEntry#LIVING}<br>
		 * {@linkplain UpgradeEntry#ENTITY}<br>
		 * {@linkplain UpgradeEntry#POSITION}<br>
		 * {@linkplain UpgradeEntry#LEVEL}<br>
		 * {@linkplain UpgradeEntry#SIDE}
		 * @param living A {@linkplain LivingEntity} to use for context
		 * @param slot An {@linkplain EquipmentSlot} to use for context
		 */
		public Builder(LivingEntity living, EquipmentSlot slot) {
			InventorySlot invSlot = InventorySlot.byBase(slot);
			this.entry(UpgradeEntry.ITEM, invSlot.getItem(living))
			.entry(UpgradeEntry.SLOT, invSlot)
			.entry(UpgradeEntry.LIVING, living)
			.entry(UpgradeEntry.ENTITY, living)
			.entry(UpgradeEntry.POSITION, living.position())
			.entry(UpgradeEntry.LEVEL, living.level)
			.entry(UpgradeEntry.SIDE, living.level.isClientSide ? LogicalSide.CLIENT : LogicalSide.SERVER);
		}
		
		/**
		 * <b>Complaint with {@linkplain UpgradeEntrySet#LIVING}</b><br>
		 * Constructs a builder with following properties automatically determined:<br>
		 * {@linkplain UpgradeEntry#LIVING}<br>
		 * {@linkplain UpgradeEntry#ENTITY}<br>
		 * {@linkplain UpgradeEntry#POSITION}<br>
		 * {@linkplain UpgradeEntry#LEVEL}<br>
		 * {@linkplain UpgradeEntry#SIDE}
		 * @param living A {@linkplain LivingEntity} to use for context
		 */
		public Builder(LivingEntity living) {
			this.entry(UpgradeEntry.LIVING, living)
				.entry(UpgradeEntry.ENTITY, living)
				.entry(UpgradeEntry.POSITION, living.position())
				.entry(UpgradeEntry.LEVEL, living.level)
				.entry(UpgradeEntry.SIDE, living.level.isClientSide ? LogicalSide.CLIENT : LogicalSide.SERVER);
		}
		
		/**
		 * <b>Complaint with {@linkplain UpgradeEntrySet#ENTITY}</b><br>
		 * Constructs a builder with following properties automatically determined:<br>
		 * {@linkplain UpgradeEntry#ENTITY}<br>
		 * {@linkplain UpgradeEntry#POSITION}<br>
		 * {@linkplain UpgradeEntry#LEVEL}<br>
		 * {@linkplain UpgradeEntry#SIDE}
		 * @param entity An {@linkplain Entity} to use for context
		 */
		public Builder(Entity entity) {
			this.entry(UpgradeEntry.ENTITY, entity)
				.entry(UpgradeEntry.POSITION, entity.position())
				.entry(UpgradeEntry.LEVEL, entity.level)
				.entry(UpgradeEntry.SIDE, entity.level.isClientSide ? LogicalSide.CLIENT : LogicalSide.SERVER);
		}
		
		/**
		 * <b>Complaint with {@linkplain UpgradeEntrySet#LEVEL}</b><br>
		 * Constructs a builder with following properties automatically determined:<br>
		 * {@linkplain UpgradeEntry#LEVEL}<br>
		 * {@linkplain UpgradeEntry#SIDE}
		 * @param level A {@linkplain Level} to use for context
		 */
		public Builder(Level level) {
			this.entry(UpgradeEntry.LEVEL, level)
				.entry(UpgradeEntry.SIDE, level.isClientSide ? LogicalSide.CLIENT : LogicalSide.SERVER);
		}
		
		/**
		 * Constructs an empty builder
		 */
		public Builder() {}
		
		/**
		 * Adds the given entry to the builder
		 * @param <T> The data type provided by this entry
		 * @param entry The {@linkplain UpgradeEntry} type to set
		 * @param value The data to store for this entry
		 * @return This builder
		 * @throws IllegalArgumentException if the provided entry doesn't allow null values but a null value was given anyways
		 */
		public <T> Builder entry(UpgradeEntry<T> entry, T value) throws IllegalArgumentException {
			if (!entry.isNullable() && value == null) throw new IllegalArgumentException(entry + " has a null value but isn't nullable");
			else {
				this.entries.put(entry, value);
				this.provided.add(entry);
			}
			return this;
		}
		
		/**
		 * Adds the given entry to the builder if not null
		 * @param <T> The data type provided by this entry
		 * @param entry The {@linkplain UpgradeEntry} type to set
		 * @param value The data to store for this entry
		 * @return This builder
		 */
		public <T> Builder optionalEntry(UpgradeEntry<T> entry, T value) {
			if (value == null) {
				this.entries.remove(entry);
				this.provided.remove(entry);
			} else {
				this.entries.put(entry, value);
				this.provided.add(entry);
			}
			return this;
		}
		
		/**
		 * Adds the given entry to the builder and marks it as modifiable.
		 * @param <T> The data type provided by this result
		 * @param entry The {@linkplain UpgradeEntry} type to set
		 * @param defaultValue The data to store for this result
		 * @return This builder
		 * @throws IllegalArgumentException if the provided entry doesn't allow null values but a null value was given anyways
		 */
		public <T> Builder modifiableEntry(UpgradeEntry<T> entry, T defaultValue) throws IllegalArgumentException {
			if (!entry.isNullable() && defaultValue == null) throw new IllegalArgumentException(entry + " has a null value but isn't nullable");
			else {
				this.entries.put(entry, defaultValue);
				this.provided.add(entry);
				this.modifiableEntries.add(entry);
			}
			return this;
		}
		
		/**
		 * Adds the given entry to the builder and marks it as modifiable.
		 * @param <T> The data type provided by this result
		 * @param entry The {@linkplain UpgradeEntry} type to set
		 * @param defaultValue The data to store for this result
		 * @return This builder
		 */
		public <T> Builder optionalModifiableEntry(UpgradeEntry<T> entry, T defaultValue) {
			this.entries.put(entry, defaultValue);
			this.provided.add(entry);
			this.modifiableEntries.add(entry);
			return this;
		}
		
//		/**
//		 * Adds the given result to the builder if a condition is met
//		 * @param <T> The data type provided by this result
//		 * @param condition A boolean determining whether or not to add the result
//		 * @param entry The {@linkplain UpgradeEntry} type to set
//		 * @param value The data to store for this result
//		 * @return This builder
//		 */
//		public <T> Builder modifiableEntryIf(boolean condition, UpgradeEntry<T> entry, T defaultValue) {
//			if (condition) {
//				this.entries.put(entry, defaultValue);
//				this.modifiableEntries.add(entry);
//			}
//			return this;
//		}
		
		/**
		 * Utility function to quickly add the cancellable result to this event
		 * @return This builder
		 */
		public Builder cancellable() {
			return this.cancellable(false);
		}
		
		/**
		 * Utility function to quickly add the cancellable result to this event if {@code condition} is true
		 * @param condition A boolean determining whether or not to add the result
		 * @return This builder
		 */
		public Builder cancellableIf(boolean condition) {
			return this.cancellableIf(condition, false);
		}
		
		/**
		 * Utility function to quickly add the cancellable result with a default value to this event
		 * @param defaultValue Whether or not the event is cancelled by default
		 * @return This builder
		 */
		public Builder cancellable(boolean defaultValue) {
			return this.modifiableEntry(UpgradeEntry.CANCELLED, defaultValue);
		}
		
		/**
		 * Utility function to quickly add the cancellable result to this event with a default value if {@code condition} is true
		 * @param condition A boolean determining whether or not to add the result
		 * @param defaultValue Whether or not the event is cancelled by default
		 * @return This builder
		 */
		public Builder cancellableIf(boolean condition, boolean defaultValue) {
			if (condition) this.modifiableEntry(UpgradeEntry.CANCELLED, defaultValue);
			return this;
		}
		
		/**
		 * Allows this event to be consumed (should stop all future item runs for this occurence but NOT cancel the event if true)
		 * @return This builder
		 */
		public Builder consumable() {
			return this.modifiableEntry(UpgradeEntry.CONSUMED, false);
		}
		
		/**
		 * Builds the builder against the given entry set, erroring if the entry set's required parameters aren't present<br>
		 * Automatically determines the item to use from the item entry, erroring if not present
		 * @param entrySet An {@linkplain UpgradeEntrySet} of the required parameters for this entry
		 * @return The resulting {@linkplain UpgradeEventData} if no errors occur
		 * @throws NoSuchElementException If no {@linkplain UpgradeEntry#ITEM ITEM} entry was present
		 * @throws IllegalStateException If one or more required entries in the entry set are not present
		 */
		public UpgradeEventData build(UpgradeEntrySet entrySet) throws NoSuchElementException, IllegalStateException {
			ItemStack ownerItem = Optional.ofNullable((ItemStack) this.entries.get(UpgradeEntry.ITEM)).orElseThrow(() -> new NoSuchElementException("Missing Item Entry"));
			return build(entrySet, ownerItem);
		}
		
		/**
		 * Builds the builder against the given entry set, erroring if the entry set's required parameters aren't present
		 * @param entrySet An {@linkplain UpgradeEntrySet} of the required parameters for this entry
		 * @param ownerItem The {@linkplain ItemStack} that caused this event to run
		 * @return The resulting {@linkplain UpgradeEventData} if no errors occur
		 * @throws IllegalStateException If one or more required entries in the entry set are not present
		 */
		public UpgradeEventData build(UpgradeEntrySet entrySet, ItemStack ownerItem) throws IllegalStateException {
			this.provided.addAll(entrySet.getForcedProvided());
			SetView<UpgradeEntry<?>> test = Sets.difference(entrySet.getProvided(), this.provided);
			if (!test.isEmpty()) {
				throw new IllegalStateException("Missing promised provided entries: " + test.toString());
			} else {
				SetView<UpgradeEntry<?>> modTest = Sets.difference(entrySet.getModified(), this.modifiableEntries);
				if (!modTest.isEmpty()) {
					throw new IllegalStateException("Entries are not modifiable as promised: " + test.toString());
				} else {
					return new UpgradeEventData(entrySet, this.entries, this.modifiableEntries, ownerItem);
				}
			}
		}
		
	}
	
	/**
	 * Constructs an empty builder
	 * @return An empty {@linkplain Builder}
	 */
	public static Builder builder() {
		return new Builder();
	}
	
	/**
	 * This class holds more secret internal functions that are not intended to be used normally or generally break existing conventions
	 * @author LegoMaster3650
	 */
	public static final class InternalStuffIgnorePlease {
		
		/**
		 * Sets whether the last result was successful
		 * @param data The {@linkplain UpgradeEventData} to modify
		 * @param value Whether the last result was successful
		 */
		public static void setSuccess(UpgradeEventData data, boolean value) {
			data.resultSuccess = value;
		}
		
		/**
		 * This forcibly sets an entry in the given data, even if it's not present!<br>
		 * This is hidden away as the unexpected change will have to be reflected in the entry set in order to be usable<br>
		 * Note: This still has
		 * @param <T> The data type held by this result
		 * @param data The {@linkplain UpgradeEventData} to modify
		 * @param entry The {@linkplain UpgradeEntry} type to set
		 * @param value The data to store for this entry
		 * @throws IllegalArgumentException if the provided entry doesn't allow null values but a null value was given anyways
		 */
		public static <T> void forceSetEntry(UpgradeEventData data, UpgradeEntry<T> entry, T value) throws IllegalArgumentException {
			if (!entry.isNullable() && value == null) throw new IllegalArgumentException("Tried to set the value of non-nullable entry " + entry + " to null");
			data.entries.put(entry, value);
		}
		
	}
	
}