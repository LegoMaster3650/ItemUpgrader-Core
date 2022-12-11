package io._3650.itemupgrader.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.mojang.blaze3d.vertex.PoseStack;

import io._3650.itemupgrader.client.renderer.UpgradeOverlayRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
	
	@Shadow
	public float blitOffset;
	
	@Inject(method = "renderGuiItem(Lnet/minecraft/world/item/ItemStack;IILnet/minecraft/client/resources/model/BakedModel;)V", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILHARD)
	private void itemupgrader_renderUpgrade(ItemStack stack, int x, int y, BakedModel model, CallbackInfo ci, PoseStack pose1, PoseStack pose2) {
		UpgradeOverlayRenderer.render(stack, pose2, x, y, this.blitOffset);
	}
	
}