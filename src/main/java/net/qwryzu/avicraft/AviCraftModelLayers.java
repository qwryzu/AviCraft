package net.qwryzu.avicraft;

import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class AviCraftModelLayers {
    public static final EntityModelLayer AUDUBONSWARBLER = new EntityModelLayer(
            Identifier.of(AviCraft.MOD_ID, "audubons_warbler"), "main");
    public static final EntityModelLayer BLACKTHROATEDBLUEWARBLER = new EntityModelLayer(
            Identifier.of(AviCraft.MOD_ID, "black_throated_blue_warbler"), "main");
    public static final EntityModelLayer MAGNOLIAWARBLER = new EntityModelLayer(
            Identifier.of(AviCraft.MOD_ID, "magnolia_warbler"), "main");
    public static final EntityModelLayer REDFACEDWARBLER = new EntityModelLayer(
            Identifier.of(AviCraft.MOD_ID, "red_faced_warbler"), "main");
}
