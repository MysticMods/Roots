package mysticmods.roots.api;

import mysticmods.roots.api.access.IRecipeManagerAccessor;
import mysticmods.roots.api.capability.Grant;
import mysticmods.roots.api.condition.LevelCondition;
import mysticmods.roots.api.condition.PlayerCondition;
import mysticmods.roots.api.herb.Herb;
import mysticmods.roots.api.modifier.Modifier;
import mysticmods.roots.api.property.RitualProperty;
import mysticmods.roots.api.property.SpellProperty;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.snapshot.SnapshotSerializer;
import mysticmods.roots.api.spell.Spell;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.Block;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class RootsAPI {
  public static RootsAPI INSTANCE;

  public static RootsAPI getInstance() {
    return INSTANCE;
  }

  // Identifiers & Logs
  public static final String MODID = "roots";
  public static final String MOD_IDENTIFIER = "Roots";
  public static Logger LOG = LogManager.getLogger();

  // Registry keys
  public static ResourceKey<Registry<Herb>> HERB_REGISTRY = key(new ResourceLocation(RootsAPI.MODID, "herbs"));
  public static ResourceKey<Registry<Ritual>> RITUAL_REGISTRY = key(new ResourceLocation(RootsAPI.MODID, "rituals"));
  public static ResourceKey<Registry<Spell>> SPELL_REGISTRY = key(new ResourceLocation(RootsAPI.MODID, "spells"));
  public static ResourceKey<Registry<Modifier>> MODIFIER_REGISTRY = key(new ResourceLocation(RootsAPI.MODID, "modifiers"));
  public static ResourceKey<Registry<RitualProperty<?>>> RITUAL_PROPERTY_REGISTRY = key(new ResourceLocation(RootsAPI.MODID, "ritual_properties"));
  public static ResourceKey<Registry<SpellProperty<?>>> SPELL_PROPERTY_REGISTRY = key(new ResourceLocation(RootsAPI.MODID, "spell_properties"));
  public static ResourceKey<Registry<LevelCondition>> LEVEL_CONDITION_REGISTRY = key(new ResourceLocation(RootsAPI.MODID, "level_conditions"));
  public static ResourceKey<Registry<PlayerCondition>> PLAYER_CONDITION_REGISTRY = key(new ResourceLocation(RootsAPI.MODID, "player_conditions"));

  public static ResourceKey<Registry<SnapshotSerializer<?>>>
    SNAPSHOT_SERIALIZER_REGISTRY = key(new ResourceLocation(RootsAPI.MODID, "snapshot_serializers"));

  private static <T> ResourceKey<Registry<T>> key(ResourceLocation name) {
    return ResourceKey.createRegistryKey(name);
  }

  // Capability IDs
  public static final ResourceLocation HERB_CAPABILITY_ID = new ResourceLocation(MODID, "herb_capability");
  public static final ResourceLocation GRANT_CAPABILITY_ID = new ResourceLocation(MODID, "grant_capability");
  public static final ResourceLocation SNAPSHOT_CAPABILITY_ID = new ResourceLocation(MODID, "snapshot_capability");

  // Actual API methods
  public abstract IRecipeManagerAccessor getRecipeAccessor();

  public abstract void grant(ServerPlayer player, Grant grant);

  public abstract Player getPlayer();

  public abstract boolean isShiftKeyDown ();

  public RecipeManager getRecipeManager() {
    return getRecipeAccessor().getManager();
  }

}
