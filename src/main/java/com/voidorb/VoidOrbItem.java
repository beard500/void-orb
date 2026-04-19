package com.voidorb;

import eu.pb4.polymer.core.api.item.PolymerItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import xyz.nucleoid.packettweaker.PacketContext;

/**
 * Throwable void_orb.
 *
 * <p>Polymer polymorphs this item on the client side so vanilla clients see a
 * regular ender pearl ItemStack. The custom 3D model is swapped in via the
 * shipped resource pack, keyed off the item_model identifier returned here.
 */
public final class VoidOrbItem extends Item implements PolymerItem {

    private static final Identifier CLIENT_MODEL = Identifier.of(VoidOrbMod.MOD_ID, "void_orb");

    public VoidOrbItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        var stack = user.getStackInHand(hand);

        world.playSound(
                null,
                user.getX(), user.getY(), user.getZ(),
                SoundEvents.BLOCK_PORTAL_TRIGGER,
                SoundCategory.PLAYERS,
                0.5f,
                1.6f + world.getRandom().nextFloat() * 0.2f
        );

        user.getItemCooldownManager().set(this, 10);

        if (!world.isClient) {
            var orb = new VoidOrbEntity(world, user);
            orb.setItem(stack);
            orb.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, 1.5f, 1.0f);
            world.spawnEntity(orb);
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.getAbilities().creativeMode) {
            stack.decrement(1);
        }

        return ActionResult.SUCCESS;
    }

    @Override
    public Item getPolymerItem(net.minecraft.item.ItemStack stack, PacketContext context) {
        return Items.ENDER_PEARL;
    }

    @Override
    public Identifier getPolymerItemModel(net.minecraft.item.ItemStack stack, PacketContext context) {
        return CLIENT_MODEL;
    }
}
