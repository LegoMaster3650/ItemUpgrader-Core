package io._3650.itemupgrader.network;

import java.util.function.Supplier;

import io._3650.itemupgrader.event.ModEvents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraftforge.network.NetworkEvent;

public record PlayerRightClickEmptyPacket(EquipmentSlot slot) {
	
	public static void encode(PlayerRightClickEmptyPacket packet, FriendlyByteBuf buffer) {
		buffer.writeEnum(packet.slot);
	}
	
	public static PlayerRightClickEmptyPacket decode(FriendlyByteBuf buffer) {
		return new PlayerRightClickEmptyPacket(buffer.readEnum(EquipmentSlot.class));
	}
	
	public static void handle(PlayerRightClickEmptyPacket packet, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			ServerPlayer player = ctx.get().getSender();
			ModEvents.rightClickBase(packet.slot, player, true);
		});
		ctx.get().setPacketHandled(true);
	}
	
}