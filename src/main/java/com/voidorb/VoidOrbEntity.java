package com.voidorb;

import eu.pb4.polymer.core.api.entity.PolymerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import xyz.nucleoid.packettweaker.PacketContext;

/**
 * Server-side projectile spawned by {@link VoidOrbItem}.
 *
 * <p>Vanilla clients see it as an ender pearl entity (via Polymer's per-instance
 * entity polymorphing), which means the ender pearl sprite renders mid-flight
 * without any client-side code. On impact we spawn portal/reverse-portal
 * particles and discard — no teleport, no damage in v1.
 */
public final class VoidOrbEntity extends ThrownItemEntity implements PolymerEntity {

    public VoidOrbEntity(EntityType<? extends VoidOrbEntity> type, World world) {
        super(type, world);
    }

    public VoidOrbEntity(World world, LivingEntity owner, ItemStack stack) {
        super(VoidOrbMod.VOID_ORB_ENTITY, owner, world, stack);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.ENDER_PEARL;
    }

    @Override
    public EntityType<?> getPolymerEntityType(PacketContext context) {
        return EntityType.ENDER_PEARL;
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);

        if (this.world instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(
                    ParticleTypes.PORTAL,
                    this.getX(), this.getY(), this.getZ(),
                    30, 0.2, 0.2, 0.2, 0.4
            );
            serverWorld.spawnParticles(
                    ParticleTypes.REVERSE_PORTAL,
                    this.getX(), this.getY(), this.getZ(),
                    15, 0.1, 0.1, 0.1, 0.15
            );
            serverWorld.playSound(
                    null,
                    this.getBlockPos(),
                    SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK,
                    SoundCategory.NEUTRAL,
                    0.8f,
                    0.6f
            );
            this.discard();
        }
    }
}
