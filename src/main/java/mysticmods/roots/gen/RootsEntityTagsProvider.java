package mysticmods.roots.gen;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.init.ModEntities;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class RootsEntityTagsProvider extends EntityTypeTagsProvider {
  public RootsEntityTagsProvider(PackOutput arg, CompletableFuture<HolderLookup.Provider> completableFuture, @Nullable ExistingFileHelper existingFileHelper) {
    super(arg, completableFuture, RootsAPI.MODID, existingFileHelper);
  }

  @Override
  protected void addTags(HolderLookup.Provider provider) {
    this.tag(RootsTags.Entities.SQUID).add(EntityType.SQUID, EntityType.GLOW_SQUID);
    this.tag(RootsTags.Entities.PACIFIST)
        .add(
            EntityType.ALLAY,
            EntityType.AXOLOTL,
            EntityType.BEE,
            EntityType.CAT,
            EntityType.CHICKEN,
            EntityType.COD,
            EntityType.COW,
            EntityType.DOLPHIN,
            EntityType.DONKEY,
            EntityType.FOX,
            EntityType.FROG,
            EntityType.GLOW_SQUID,
            EntityType.GOAT,
            EntityType.HORSE,
            EntityType.IRON_GOLEM,
            EntityType.LLAMA,
            EntityType.MULE,
            EntityType.MOOSHROOM,
            EntityType.OCELOT,
            EntityType.PANDA,
            EntityType.PARROT,
            EntityType.PIG,
            EntityType.POLAR_BEAR,
            EntityType.PUFFERFISH,
            EntityType.RABBIT, // Specific exclusion for killer bunnies
            EntityType.SALMON,
            EntityType.SHEEP,
            EntityType.SNOW_GOLEM,
            EntityType.SQUID,
            EntityType.STRIDER,
            EntityType.TADPOLE,
            EntityType.TRADER_LLAMA,
            EntityType.TROPICAL_FISH,
            EntityType.TURTLE,
            EntityType.VILLAGER,
            EntityType.WANDERING_TRADER,
            EntityType.WOLF,
            ModEntities.DUCK.get(),
            ModEntities.OWL.get(),
            ModEntities.DEER.get(),
            ModEntities.BEETLE.get(),
            ModEntities.FENNEC.get(),
            ModEntities.GREEN_SPROUT.get(),
            ModEntities.PURPLE_SPROUT.get(),
            ModEntities.RED_SPROUT.get(),
            ModEntities.TAN_SPROUT.get()
        );
    this.tag(RootsTags.Entities.ANIMAL_HARVEST).addTag(RootsTags.Entities.PACIFIST).add(EntityType.BAT);
    this.tag(RootsTags.Entities.BOATS).add(EntityType.BOAT, EntityType.CHEST_BOAT);
    this.tag(RootsTags.Entities.FEY_LEATHER)
        .add(
            EntityType.COW,
            EntityType.DONKEY,
            EntityType.HORSE,
            EntityType.LLAMA,
            EntityType.MULE,
            EntityType.MOOSHROOM,
            EntityType.TRADER_LLAMA,
            ModEntities.DEER.get()
        );
    this.tag(RootsTags.Entities.FORCE_HOSTILE);
    this.tag(RootsTags.Entities.FORCE_FRIENDLY);
    this.tag(RootsTags.Entities.DISABLE_DISARM);
  }

  @Override
  public String getName() {
    return "Roots Entity Type Tags";
  }
}
