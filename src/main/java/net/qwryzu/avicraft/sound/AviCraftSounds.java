package net.qwryzu.avicraft.sound;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.qwryzu.avicraft.AviCraft;

public class AviCraftSounds {
    // Warbler Songs and Calls
    public static final SoundEvent AUDUBONS_WARBLER_SONG = registerSoundEvent("audubons_warbler_song");
    public static final SoundEvent BLACK_THROATED_BLUE_WARBLER_SONG = registerSoundEvent("black_throated_blue_warbler_song");
    public static final SoundEvent YELLOW_WARBLER_SONG = registerSoundEvent("yellow_warbler_song");

    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = Identifier.of(AviCraft.MOD_ID, name);
        AviCraft.LOGGER.info("Registering sound event with ID: " + id);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void registerSounds() {
        AviCraft.LOGGER.info("Registering Sounds for " + AviCraft.MOD_ID);
    }
}
