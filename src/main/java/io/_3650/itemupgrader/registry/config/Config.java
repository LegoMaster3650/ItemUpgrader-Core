package io._3650.itemupgrader.registry.config;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class Config {
	
	public static class Server {
		
		Server(ForgeConfigSpec.Builder builder) {
			
			
			
		}
		
	}
	
	public static class Common {

		public final BooleanValue allowRadiusSphere;
		public final BooleanValue allowLivingTick;
		
		Common(ForgeConfigSpec.Builder builder) {
			builder.push("performance");
			
			allowRadiusSphere = builder.comment("Allows upgrades to search for items in a sphere rather than a cube if specified.", "Disabling this COULD help performance.", "Requires datapack reload to take effect.", "[Default: true]").define("allowMagneticSphere", true);
			allowLivingTick = builder.comment("Allows the mod to run per-tick actions for any living entity besides a player", "Disabling this COULD help performance.", "Also note that players will still run per-tick events.", "[Default: true]").define("allowLivingTick", true);
			
			builder.pop();
		}
		
	}
	
	public static class Client {
		
		public final BooleanValue requiresKeyHeld;
		public final BooleanValue showUpgradeID;
		public final BooleanValue useRomanNumerals;
		
		public final IntValue recipeClickAreaLeftX;
		public final IntValue recipeClickAreaTopY;
		public final IntValue recipeClickAreaWidth;
		public final IntValue recipeClickAreaHeight;
		
		public final BooleanValue renderUpgradeOverlays;
		public final IntValue overlayRenderDepth;
		
		Client(ForgeConfigSpec.Builder builder) {
			builder.push("tooltip");
			
			requiresKeyHeld = builder.comment("Does the upgrade tooltip require a key to be held to expand?", "[Default: true]").define("requiresKeyHeld", true);
			showUpgradeID = builder.comment("Show an item's upgrade id when advanced tooltips are enabled", "[Default: false]").define("showUpgradeID", false);
			useRomanNumerals = builder.comment("Uses roman numerals instead of numbers when describing enchantments.", "[Default: false]").define("useRomanNumerals", false);
			
			builder.pop();
			///////////////////////////////////////////////////////////
			builder.push("jei");
			builder.comment("Tweaking the area you can click in the smithing table gui to view upgrade recipes.", "Mainly intended if you have an incompatible gui modifying resource pack.", ">> Requires a reload for changes to apply! <<").push("recipeClickArea");
			
			recipeClickAreaLeftX = builder.comment("X of left edge of click area", "[Default (Hammer): 17]", "[Alternative (Plus Icon): 53]").defineInRange("recipeClickAreaLeftX", 17, 0, 175);
			recipeClickAreaTopY = builder.comment("Y of top edge of click area", "[Default (Hammer): 7]", "[Alternative (Plus Icon): 49]").defineInRange("recipeClickAreaTopY", 7, 0, 165);
			recipeClickAreaWidth = builder.comment("Width of click area", "[Default (Hammer): 30]", "[Alternative (Plus Icon): 13]").defineInRange("recipeClickAreaWidth", 30, 1, 176);
			recipeClickAreaHeight = builder.comment("Height of click area", "[Default (Hammer): 30]", "[Alternative (Plus Icon): 13]").defineInRange("recipeClickAreaHeight", 30, 1, 166);
			
			builder.pop();
			builder.pop();
			///////////////////////////////////////////////////////////
			builder.push("decoration");
			
			renderUpgradeOverlays = builder.comment("Should the mod render upgrade overlays on items in a GUI?", "This texture is an overlay, and will not recieve effects like enchantment glow.", "NOTE: This does NOT disable the loading of the textures, just the rendering", "[Default: true]").define("renderUpgradeOverlays", true);
			overlayRenderDepth = builder.comment(
					"Determines what depth the overlay renders at, allowing fine tuning of which elements the overlay appears above and below.",
					"Don't touch unless the overlay is appearing over things besides the item and its' enchantment glow.",
					">> Requires a resource pack reload in order to take effect!",
					"[Default: 110]").defineInRange("overlayRenderDepth", 110, 0, 500);
			
			builder.pop();
		}
		
	}
	
//	public static final ForgeConfigSpec SERVER_SPEC;
//	public static final Server SERVER;
	
	public static final ForgeConfigSpec COMMON_SPEC;
	public static final Common COMMON;
	
	public static final ForgeConfigSpec CLIENT_SPEC;
	public static final Client CLIENT;
	
	static {
//		final Pair<Server, ForgeConfigSpec> serverSpecPair = new ForgeConfigSpec.Builder().configure(Server::new);
//		SERVER_SPEC = serverSpecPair.getRight();
//		SERVER = serverSpecPair.getLeft();
		
		final Pair<Common, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(Common::new);
		COMMON_SPEC = commonSpecPair.getRight();
		COMMON = commonSpecPair.getLeft();
		
		final Pair<Client, ForgeConfigSpec> clientSpecPair = new ForgeConfigSpec.Builder().configure(Client::new);
		CLIENT_SPEC = clientSpecPair.getRight();
		CLIENT = clientSpecPair.getLeft();
	}
	
}