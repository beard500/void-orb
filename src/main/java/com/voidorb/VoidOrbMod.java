package com.voidorb;

import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class VoidOrbMod implements ModInitializer {
    public static final String MOD_ID = "void_orb";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final Identifier VOID_ORB_ID = Identifier.of(MOD_ID, "void_orb");

    public static final RegistryKey<Item> VOID_ORB_ITEM_KEY =
            RegistryKey.of(RegistryKeys.ITEM, VOID_ORB_ID);

    public static final Item VOID_ORB_ITEM = new VoidOrbItem(
            new Item.Settings()
                    .maxCount(16)
                    .registryKey(VOID_ORB_ITEM_KEY)
    );

    public static final EntityType<VoidOrbEntity> VOID_ORB_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            VOID_ORB_ID,
            EntityType.Builder.<VoidOrbEntity>create(VoidOrbEntity::new, SpawnGroup.MISC)
                    .dimensions(0.25f, 0.25f)
                    .maxTrackingRange(4)
                    .trackingTickInterval(10)
                    .build(VOID_ORB_ID.toString())
    );

    @Override
    public void onInitialize() {
        Registry.register(Registries.ITEM, VOID_ORB_ID, VOID_ORB_ITEM);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
            entries.addAfter(Items.ENDER_PEARL, VOID_ORB_ITEM);
        });

        // Auto-ship the void_orb resource pack (model + textures) to every joining
        // vanilla client, and require it so clients that reject it cannot join.
        PolymerResourcePackUtils.addModAssets(MOD_ID);
        PolymerResourcePackUtils.markAsRequired();

        LOGGER.info("[void_orb] registered item, entity, and resource pack");
    }
}
