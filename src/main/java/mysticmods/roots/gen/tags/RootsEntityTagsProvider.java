package mysticmods.roots.gen.tags;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.init.ModEntities;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public final class RootsEntityTagsProvider extends EntityTypeTagsProvider {
  public RootsEntityTagsProvider(PackOutput arg, CompletableFuture<HolderLookup.Provider> completableFuture, @Nullable ExistingFileHelper existingFileHelper) {
    super(arg, completableFuture, RootsAPI.MODID, existingFileHelper);
  }

  @SuppressWarnings("unchecked")
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
            ModEntities.JERBOA.get(),
            ModEntities.FENNEC.get(),
            ModEntities.GREEN_SPROUT.get(),
            ModEntities.PURPLE_SPROUT.get(),
            ModEntities.SNOW_SPROUT.get(),
            ModEntities.RED_SPROUT.get(),
            ModEntities.TAN_SPROUT.get(),
            ModEntities.MELODY_SPROUT.get()
        );
    this.tag(RootsTags.Entities.AUGMENTABLE)
        .add(EntityType.HORSE, EntityType.MULE, EntityType.DONKEY, EntityType.LLAMA, EntityType.CAMEL, EntityType.WOLF)
        .add(ModEntities.FENNEC.get());
    this.tag(RootsTags.Entities.AUGMENTABLE_EXCLUDE);
    this.tag(RootsTags.Entities.ANIMAL_HARVEST)
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
            EntityType.LLAMA,
            EntityType.MULE,
            EntityType.MOOSHROOM,
            EntityType.OCELOT,
            EntityType.PANDA,
            EntityType.PARROT,
            EntityType.PIG,
            EntityType.POLAR_BEAR,
            EntityType.PUFFERFISH,
            EntityType.RABBIT,
            EntityType.SALMON,
            EntityType.SHEEP,
            EntityType.SQUID,
            EntityType.STRIDER,
            EntityType.TADPOLE,
            EntityType.TRADER_LLAMA,
            EntityType.TROPICAL_FISH,
            EntityType.TURTLE,
            EntityType.WOLF,
            ModEntities.DUCK.get(),
            ModEntities.OWL.get(),
            ModEntities.DEER.get(),
            ModEntities.BEETLE.get(),
            ModEntities.JERBOA.get(),
            ModEntities.FENNEC.get(),
            ModEntities.GREEN_SPROUT.get(),
            ModEntities.PURPLE_SPROUT.get(),
            ModEntities.SNOW_SPROUT.get(),
            ModEntities.MELODY_SPROUT.get(),
            ModEntities.RED_SPROUT.get(),
            ModEntities.TAN_SPROUT.get(),
            EntityType.BAT);
    this.tag(RootsTags.Entities.ANIMAL_HARVEST_EXCLUDE).add(EntityType.IRON_GOLEM);
    this.tag(RootsTags.Entities.BOATS).add(EntityType.BOAT, EntityType.CHEST_BOAT);
    this.tag(RootsTags.Entities.SYLVAN_LEATHER)
        .add(
            EntityType.COW,
            EntityType.DONKEY,
            EntityType.HORSE,
            EntityType.GOAT,
            EntityType.LLAMA,
            EntityType.MULE,
            EntityType.MOOSHROOM,
            EntityType.TRADER_LLAMA,
            ModEntities.DEER.get()
        );
    this.tag(RootsTags.Entities.FORCE_HOSTILE);
    this.tag(RootsTags.Entities.FORCE_FRIENDLY);
    this.tag(RootsTags.Entities.DISABLE_DISARM);
    this.tag(RootsTags.Entities.HEALABLE_ICE_CREATURES).add(EntityType.SNOW_GOLEM);
    this.tag(RootsTags.Entities.ZOMBIE_VILLAGERS).add(EntityType.ZOMBIE_VILLAGER);
    this.tag(RootsTags.Entities.ZOMBIE_VILLAGERS_EXCLUDE);
    this.tag(RootsTags.Entities.WINDWALL_FORCE_EXCLUDE);
    this.tag(RootsTags.Entities.WINDWALL_FORCE_INCLUDE);
    this.tag(RootsTags.Entities.GEAS_EXCLUDE);
    this.tag(RootsTags.Entities.TEMPORAL_MORASS_EXCLUDE).add(EntityType.PLAYER);
    this.tag(RootsTags.Entities.ROSE_THORNS_EXCLUDE).add(EntityType.PLAYER);
    this.tag(RootsTags.Entities.ALERTNESS).add(EntityType.CREEPER);
    this.tag(RootsTags.Entities.RUNIC_SHEARS_OVERRIDE)
        .add(EntityType.HORSE, EntityType.MULE, EntityType.DONKEY, EntityType.VILLAGER, EntityType.WANDERING_TRADER, EntityType.LLAMA, EntityType.TRADER_LLAMA, EntityType.CAMEL);
    this.tag(RootsTags.Entities.ALLOW_CASTING_TOOL_RIGHT_CLICK);
    this.tag(RootsTags.Entities.ADD_TENTACLE_LOOT).add(EntityType.SQUID, EntityType.GLOW_SQUID);

    this.tag(RootsTags.Entities.WITHERS).add(EntityType.WITHER);
    this.tag(RootsTags.Entities.DRAGONS).add(EntityType.ENDER_DRAGON);
    this.tag(RootsTags.Entities.TRADERS)
        .add(EntityType.PIGLIN, EntityType.VILLAGER, EntityType.WANDERING_TRADER, EntityType.ZOMBIE_VILLAGER);
    this.tag(RootsTags.Entities.UNDEAD).addTag(EntityTypeTags.UNDEAD);
    this.tag(RootsTags.Entities.SPROUTS)
        .add(ModEntities.GREEN_SPROUT.get(), ModEntities.SNOW_SPROUT.get(), ModEntities.PURPLE_SPROUT.get(), ModEntities.TAN_SPROUT.get(), ModEntities.RED_SPROUT.get(), ModEntities.MELODY_SPROUT.get());
    this.tag(RootsTags.Entities.HELL_ANIMALS);
    this.tag(RootsTags.Entities.SNOW_ANIMALS).add(ModEntities.SNOW_SPROUT.get());
    this.tag(RootsTags.Entities.END_ANIMALS).add(ModEntities.MELODY_SPROUT.get());
    this.tag(RootsTags.Entities.MELODY_SPROUT).add(ModEntities.MELODY_SPROUT.get());
    this.tag(RootsTags.Entities.SNOW_SPROUT).add(ModEntities.SNOW_SPROUT.get());
    this.tag(RootsTags.Entities.SPECIAL_SPROUTS)
        .addTags(RootsTags.Entities.MELODY_SPROUT, RootsTags.Entities.SNOW_SPROUT);
    this.tag(RootsTags.Entities.RED_SPROUT).add(ModEntities.RED_SPROUT.get());
    this.tag(RootsTags.Entities.TAN_SPROUT).add(ModEntities.TAN_SPROUT.get());
    this.tag(RootsTags.Entities.GREEN_SPROUT).add(ModEntities.GREEN_SPROUT.get());
    this.tag(RootsTags.Entities.PURPLE_SPROUT).add(ModEntities.PURPLE_SPROUT.get());
    this.tag(RootsTags.Entities.NORMAL_SPROUTS)
        .addTags(RootsTags.Entities.GREEN_SPROUT, RootsTags.Entities.RED_SPROUT, RootsTags.Entities.TAN_SPROUT, RootsTags.Entities.PURPLE_SPROUT);
    this.tag(RootsTags.Entities.PLAYERS).add(EntityType.PLAYER);
    this.tag(RootsTags.Entities.LIGHT_DRIFTER).add(ModEntities.LIGHT_DRIFTER.value());
    this.tag(RootsTags.Entities.LIMIT_WOODEN_SHEARS_DROPS).add(EntityType.SHEEP);

    this.tag(RootsTags.Entities.SHOULD_RENDER_HUD)
        .add(EntityType.HORSE, EntityType.DONKEY, EntityType.MULE, EntityType.CAMEL, EntityType.LLAMA, EntityType.VILLAGER, EntityType.WOLF, ModEntities.FENNEC.get());
    this.tag(EntityTypeTags.ARROWS).add(ModEntities.LIVING_ARROW.get());
  }

  @Override
  public String getName() {
    return "Roots Entity Type Tags";
  }
}
