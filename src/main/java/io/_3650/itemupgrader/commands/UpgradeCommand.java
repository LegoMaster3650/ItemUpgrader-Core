package io._3650.itemupgrader.commands;

import java.util.Collection;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import io._3650.itemupgrader.api.ItemUpgrade;
import io._3650.itemupgrader.api.ItemUpgraderApi;
import io._3650.itemupgrader.commands.arguments.ItemUpgradeArgument;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class UpgradeCommand {
	
	private static final DynamicCommandExceptionType ERROR_NOT_LIVING_ENTITY = new DynamicCommandExceptionType(obj -> {
		return Component.translatable("commands.enchant.failed.entity", obj);
	});
	private static final DynamicCommandExceptionType ERROR_NO_ITEM = new DynamicCommandExceptionType(obj -> {
		return Component.translatable("commands.enchant.failed.itemless", obj);
	});
	private static final DynamicCommandExceptionType ERROR_INCOMPATIBLE = new DynamicCommandExceptionType(obj -> {
		return Component.translatable("commands.upgrade.failed.incompatible", obj);
	});
	private static final SimpleCommandExceptionType ERROR_NOTHING_HAPPENED = new SimpleCommandExceptionType(Component.translatable("commands.upgrade.failed"));
	
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(Commands.literal("upgrade")
				.requires(source -> source.hasPermission(2))
				.then(Commands.argument("targets", EntityArgument.entities())
				.then(Commands.argument("upgrade", ItemUpgradeArgument.upgrade())
				.executes(ctx -> {
					return upgrade(ctx.getSource(), EntityArgument.getEntities(ctx, "targets"), ItemUpgradeArgument.getUpgrade(ctx, "upgrade"), 1);
				}).then(Commands.argument("count", IntegerArgumentType.integer(1, 255))
				.executes(ctx -> {
					return upgrade(ctx.getSource(), EntityArgument.getEntities(ctx, "targets"), ItemUpgradeArgument.getUpgrade(ctx, "upgrade"), IntegerArgumentType.getInteger(ctx, "count"));
				})))));
	}
	
	private static int upgrade(CommandSourceStack source, Collection<? extends Entity> targets, ItemUpgrade upgrade, int count) throws CommandSyntaxException {
		int i = 0;
		
		for (Entity entity : targets) {
			if (entity instanceof LivingEntity living) {
				ItemStack stack = living.getMainHandItem();
				if (!stack.isEmpty()) {
					if (upgrade.isValidItem(stack)) {
						for (int j = 0; j < count; j++) ItemUpgraderApi.applyUpgrade(stack, upgrade.getId());
						++i;
					} else if (targets.size() == 1) {
						throw ERROR_INCOMPATIBLE.create(stack.getItem().getName(stack));
					}
				} else if (targets.size() == 1) {
					throw ERROR_NO_ITEM.create(living.getName());
				}
			} else if (targets.size() == 1) {
				throw ERROR_NOT_LIVING_ENTITY.create(entity.getName());
			}
		}
		
		if (i == 0) {
			throw ERROR_NOTHING_HAPPENED.create();
		} else {
			if (targets.size() == 1) {
				source.sendSuccess(Component.translatable("commands.upgrade.success.single", Component.translatable(upgrade.getDescriptionId()), targets.iterator().next().getDisplayName()), true);
			} else {
				source.sendSuccess(Component.translatable("commands.upgrade.success.multiple", Component.translatable(upgrade.getDescriptionId()), targets.size()), true);
			}
			
			return i;
		}
	}
	
}