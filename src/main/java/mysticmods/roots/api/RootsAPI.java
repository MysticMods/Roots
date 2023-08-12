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
import mysticmods.roots.api.test.entity.EntityTestType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.ToolAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;

public abstract class RootsAPI {
  public static RootsAPI INSTANCE;
  public static Tier LIVING_TOOL_TIER = new ForgeTier(2, 250, 6.0f, 2.0f, 19, BlockTags.NEEDS_STONE_TOOL, () -> Ingredient.of(RootsTags.Items.BARKS));
  public static Tier COPPER_TIER = new ForgeTier(2, 250, 4.0f, 2.0f, 2, BlockTags.NEEDS_STONE_TOOL, () -> Ingredient.of(Tags.Items.INGOTS_COPPER));

  // Tool Actions (Forge-specific)
  public static ToolAction RUNIC_SHEARS_HARVEST = ToolAction.get("runic_shears_harvest");
  public static ToolAction RUNIC_SHEARS_DIG = ToolAction.get("runic_shears_dig");
  public static ToolAction KNIFE_STRIP = ToolAction.get("knife_strip");
  public static ToolAction KNIFE_DIG = ToolAction.get("knife_dig");

  public static Set<ToolAction> RUNIC_SHEARS_DEFAULTS = Set.of(RUNIC_SHEARS_HARVEST, RUNIC_SHEARS_DIG);
  public static Set<ToolAction> KNIFE_DEFAULTS = Set.of(KNIFE_STRIP, KNIFE_DIG);

  public static RootsAPI getInstance() {
    return INSTANCE;
  }

  // Identifiers & Logs
  public static final String MODID = "roots";

  public static ResourceLocation rl (String path) {
    return new ResourceLocation(MODID, path);
  }
  public static ResourceLocation LIVING_TOOL_TIER_ID = rl("living_tool");
  public static final String MOD_IDENTIFIER = "Roots";
  public static Logger LOG = LogManager.getLogger();

  // Registry keys
  public static ResourceKey<Registry<Herb>> HERB_REGISTRY = key(RootsAPI.rl("herbs"));
  public static ResourceKey<Registry<Ritual>> RITUAL_REGISTRY = key(RootsAPI.rl("rituals"));
  public static ResourceKey<Registry<Spell>> SPELL_REGISTRY = key(RootsAPI.rl("spells"));
  public static ResourceKey<Registry<Modifier>> MODIFIER_REGISTRY = key(RootsAPI.rl("modifiers"));
  public static ResourceKey<Registry<RitualProperty<?>>> RITUAL_PROPERTY_REGISTRY = key(RootsAPI.rl("ritual_properties"));
  public static ResourceKey<Registry<SpellProperty<?>>> SPELL_PROPERTY_REGISTRY = key(RootsAPI.rl("spell_properties"));
  public static ResourceKey<Registry<LevelCondition>> LEVEL_CONDITION_REGISTRY = key(RootsAPI.rl("level_conditions"));
  public static ResourceKey<Registry<PlayerCondition>> PLAYER_CONDITION_REGISTRY = key(RootsAPI.rl("player_conditions"));
  public static ResourceKey<Registry<SnapshotSerializer<?>>>
    SNAPSHOT_SERIALIZER_REGISTRY = key(RootsAPI.rl("snapshot_serializers"));

  public static ResourceKey<Registry<EntityTestType<?>>> ENTITY_TEST_TYPE_REGISTRY = key(RootsAPI.rl("entity_test_types"));

  private static <T> ResourceKey<Registry<T>> key(ResourceLocation name) {
    return ResourceKey.createRegistryKey(name);
  }

  // Capability IDs
  public static final ResourceLocation HERB_CAPABILITY_ID = rl("herb_capability");
  public static final ResourceLocation GRANT_CAPABILITY_ID = rl("grant_capability");
  public static final ResourceLocation SNAPSHOT_CAPABILITY_ID = rl("snapshot_capability");
  public static final ResourceLocation SHOULDER_CAPABILITY_ID = rl("shoulder_capability");

  public static final ResourceLocation RUNIC_SHEARS_ENTITY_CAPABILITY_ID = rl("runic_shears_entity_capability");
  public static final ResourceLocation RUNIC_SHEARS_TOKEN_CAPABILITY = rl("runic_shears_token_capability");
  public static final ResourceLocation SQUID_MILKING_CAPABILITY = rl("squid_milking_capability");

  // Actual API methods
  public abstract IRecipeManagerAccessor getRecipeAccessor();

  public abstract void grant(ServerPlayer player, Grant grant);

  public abstract boolean canGrant (ServerPlayer player, Grant grant);

  public abstract Player getPlayer();

  public abstract boolean isShiftKeyDown ();

  public RecipeManager getRecipeManager() {
    return getRecipeAccessor().getManager();
  }

}
