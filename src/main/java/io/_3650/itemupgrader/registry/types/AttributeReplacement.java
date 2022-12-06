package io._3650.itemupgrader.registry.types;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public record AttributeReplacement(Attribute target, AttributeModifier oldAttribute, AttributeModifier newAttribute) {
	
}