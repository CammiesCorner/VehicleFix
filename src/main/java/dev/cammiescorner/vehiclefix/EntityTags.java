package dev.cammiescorner.vehiclefix;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class EntityTags {
	public static final TagKey<EntityType<?>> NOT_AFFECTED = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("vehiclefix", "not_affected"));
}
