package net.qwryzu.avicraft.item;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.qwryzu.avicraft.AviCraft;

public class AviCraftItems {
    //public static final Item PINK_GARNET = registerItem("pink_garnet", new Item(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(AviCraft.MOD_ID,"pink_garnet")))));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(AviCraft.MOD_ID, name), item);
    }

    public static void registerModItems() {
        AviCraft.LOGGER.info("Registering Mod Items for " + AviCraft.MOD_ID);

        //ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
        //    entries.add(PINK_GARNET);
    };
}
