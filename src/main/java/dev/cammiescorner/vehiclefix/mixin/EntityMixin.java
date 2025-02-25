package dev.cammiescorner.vehiclefix.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.cammiescorner.vehiclefix.BlockTags;
import dev.cammiescorner.vehiclefix.EntityTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(Entity.class)
public abstract class EntityMixin {
	@Shadow public abstract List<Entity> getPassengers();
	@Shadow @Nullable public abstract Entity getVehicle();
	@Shadow public abstract EntityType<?> getType();
	@Shadow private Level level;

	@Shadow public abstract InteractionResult interact(Player player, InteractionHand interactionHand);

	@WrapOperation(method = "collide", at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/Entity;getBoundingBox()Lnet/minecraft/world/phys/AABB;"
	))
	private AABB getBoundingBox(Entity instance, Operation<AABB> original) {
		AABB aabb = original.call(instance);

		if(getVehicle() != null && (!getVehicle().getType().is(EntityTags.NOT_AFFECTED)))
			return new AABB(aabb.minX, getVehicle().getBoundingBox().minY + 0.1, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ);

		return aabb;
	}

	@Inject(method = "collide", at = @At(
			value = "RETURN",
			ordinal = 1
	), cancellable = true, locals = LocalCapture.CAPTURE_FAILSOFT)
	public void handleRiderCollisions(Vec3 movement, CallbackInfoReturnable<Vec3> info, AABB box, List<VoxelShape> list, Vec3 vec3d) {
		if(!getType().is(EntityTags.NOT_AFFECTED)) {
			for(Entity passenger : getPassengers()) {
				Vec3 newPos = passenger.position().add(0, passenger.getBbHeight(), 0).add(movement.normalize());
				BlockState state = level.getBlockState(new BlockPos(new Vec3i((int) newPos.x, (int) newPos.y, (int) newPos.z)));

				if(!state.is(BlockTags.PASSABLE)) {
					Vec3 passengerCollision = passenger.collide(movement);

					info.setReturnValue(new Vec3(
							vec3d.x() > 0 ? Math.min(passengerCollision.x(), vec3d.x()) : vec3d.x() < 0 ? Math.max(passengerCollision.x(), vec3d.x()) : vec3d.x(),
							vec3d.y() > 0 ? Math.min(passengerCollision.y(), vec3d.y()) : Math.max(passengerCollision.y(), vec3d.y()),
							vec3d.z() > 0 ? Math.min(passengerCollision.z(), vec3d.z()) : vec3d.z() < 0 ? Math.max(passengerCollision.z(), vec3d.z()) : vec3d.z()
					));
				}
			}
		}
	}
}
