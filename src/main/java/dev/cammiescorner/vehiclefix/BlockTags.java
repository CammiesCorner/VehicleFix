package dev.cammiescorner.vehiclefix;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class BlockTags {
	public static final TagKey<Block> PASSABLE = TagKey.create(Registries.BLOCK, new ResourceLocation("vehiclefix", "passable"));
}
