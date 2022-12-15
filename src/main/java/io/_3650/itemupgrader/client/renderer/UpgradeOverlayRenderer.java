package io._3650.itemupgrader.client.renderer;

import java.util.HashMap;
import java.util.HashSet;

import com.google.common.collect.ImmutableSet;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;

import io._3650.itemupgrader.api.ItemUpgraderApi;
import io._3650.itemupgrader.registry.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;

public class UpgradeOverlayRenderer {
	
	public static final String MAINDIR = "item_upgrades/";
	
	private static final org.slf4j.Logger LOGGER = com.mojang.logging.LogUtils.getLogger();
	
	private static ImmutableSet<ResourceLocation> textures = ImmutableSet.of();
	
	static ImmutableSet<ResourceLocation> getTextures() {
		return textures;
	}
	
	// Rendering Logic
	
	private static HashSet<ResourceLocation> invalidCache = new HashSet<>();
	
	public static void render(ItemStack stack, PoseStack pose, int x, int y, float blitOffset) {
		if (stack.isEmpty()) return;
		try {
			if (!Config.CLIENT.renderUpgradeOverlays.get()) return;
		} catch (IllegalStateException e) { //just in case the renderer is being dumb and somehow rendering an item before the client config exists
			LOGGER.error("Error checking config for item rendering: ", e);
			return;
		}
		ResourceLocation upgradeId = ItemUpgraderApi.getUpgradeKey(stack);
		if (upgradeId == null) return;
		if (invalidCache.contains(upgradeId)) return;
		ResourceLocation path = new ResourceLocation(upgradeId.getNamespace(), MAINDIR + upgradeId.getPath());
		if (textures.contains(path)) {
			Minecraft.getInstance().getProfiler().push("itemupgrader_overlay");
			TextureAtlasSprite sprite = getSprite(path);
			if (sprite != null) {
				pose.pushPose();
				pose.translate(0, 0, blitOffset + getBlitOffset());
				
				float x1 = x;
				float x2 = x + sprite.getWidth();
				float y1 = y;
				float y2 = y + sprite.getHeight();
				
				RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
				RenderSystem.setShader(GameRenderer::getPositionTexShader);
				RenderSystem.enableTexture();
				Matrix4f matrix = pose.last().pose();
				BufferBuilder buffer = Tesselator.getInstance().getBuilder();
				buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
				buffer.vertex(matrix, x1, y2, 0).uv(sprite.getU0(), sprite.getV1()).endVertex();
				buffer.vertex(matrix, x2, y2, 0).uv(sprite.getU1(), sprite.getV1()).endVertex();
				buffer.vertex(matrix, x2, y1, 0).uv(sprite.getU1(), sprite.getV0()).endVertex();
				buffer.vertex(matrix, x1, y1, 0).uv(sprite.getU0(), sprite.getV0()).endVertex();
				BufferUploader.drawWithShader(buffer.end());
				
				pose.popPose();
			} else invalidCache.add(path);
			Minecraft.getInstance().getProfiler().pop();
		} else invalidCache.add(upgradeId);
	}
	
	private static HashMap<ResourceLocation, TextureAtlasSprite> spriteCache = new HashMap<>();
	
	private static TextureAtlasSprite getSprite(ResourceLocation path) {
		if (spriteCache.containsKey(path)) return spriteCache.get(path);
		TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(path);
		if (sprite != null) spriteCache.put(path, sprite);
		if (sprite != null) LOGGER.debug(sprite.getName().toString());
		return sprite;
	}
	
	private static int blitOffset = -1;
	
	private static int getBlitOffset() {
		if (blitOffset == -1) blitOffset = Config.CLIENT.overlayRenderDepth.get();
		return blitOffset;
	}
	
	// Reloading Logic
	
	private static final String TEXTURES = "textures/";
	private static final int TEXTURES_LENGTH = TEXTURES.length();
	private static final String SUFFIX = ".png";
	private static final int SUFFIX_LENGTH = SUFFIX.length();
	private static final String DIR = TEXTURES + MAINDIR.substring(0, MAINDIR.length() - 1);
	
	public static void reload(ResourceManager resourceManager) {
		ImmutableSet.Builder<ResourceLocation> resources = ImmutableSet.builder();
		
		LOGGER.debug("Reloading Upgrade Textures...");
		
		for (ResourceLocation loc : resourceManager.listResources(DIR, loc -> loc.getPath().endsWith(SUFFIX)).keySet()) {
			String path = loc.getPath();
			resources.add(new ResourceLocation(loc.getNamespace(), path.substring(TEXTURES_LENGTH, path.length() - SUFFIX_LENGTH)));
		}
		
		textures = resources.build();
		
		LOGGER.debug("Loaded " + textures.size() + " textures");
		
		return;
	}
	
	public static void clearCaches() {
		invalidCache.clear();
		spriteCache.clear();
		blitOffset = -1;
	}
	
}