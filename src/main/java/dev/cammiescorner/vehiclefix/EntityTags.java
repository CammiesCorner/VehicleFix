package dev.cammiescorner.vehiclefix;

import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

import java.util.Optional;

public class EntityTags {
	public static final TagKey<EntityType<?>> AFFECTS = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("vehiclefix", "affects"));

	public static boolean affectsIsEmpty() {
		Optional<HolderSet.Named<EntityType<?>>> tag = BuiltInRegistries.ENTITY_TYPE.getTag(EntityTags.AFFECTS);

		return tag.isEmpty() || tag.get().size() == 0;
	}
}
