package io._3650.itemupgrader.commands.arguments;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import io._3650.itemupgrader.api.ItemUpgrade;
import io._3650.itemupgrader.upgrades.ItemUpgradeManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ItemUpgradeArgument implements ArgumentType<ItemUpgrade> {
	
	private static final Collection<String> EXAMPLES = Arrays.asList("brick");
	public static final DynamicCommandExceptionType ERROR_UNKNOWN_UPGRADE = new DynamicCommandExceptionType(obj -> {
			return Component.translatable("itemupgrade.unknown", obj);
	});
	
	public static ItemUpgradeArgument upgrade() {
		return new ItemUpgradeArgument();
	}
	
	public static ItemUpgrade getUpgrade(CommandContext<CommandSourceStack> ctx, String name) {
		return ctx.getArgument(name, ItemUpgrade.class);
	}
	
	@Override
	public ItemUpgrade parse(StringReader reader) throws CommandSyntaxException {
		ResourceLocation upgradeId = ResourceLocation.read(reader);
		return ItemUpgradeManager.INSTANCE.getOptional(upgradeId).orElseThrow(() -> {
			return ERROR_UNKNOWN_UPGRADE.create(upgradeId);
		});
	}
	
	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		return SharedSuggestionProvider.suggestResource(ItemUpgradeManager.INSTANCE.getUpgrades().keySet(), builder);
	}
	
	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}
	
}
