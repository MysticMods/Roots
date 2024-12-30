package mysticmods.roots.api;

import mysticmods.roots.api.access.IRecipeManagerAccessor;
import mysticmods.roots.api.capability.Grant;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.common.SimpleTier;
import net.neoforged.neoforge.common.Tags;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;

public abstract class RootsAPI {
  public static RootsAPI INSTANCE;
  public static Tier LIVING_TOOL_TIER = new SimpleTier(BlockTags.INCORRECT_FOR_STONE_TOOL, 300, 6.0f, 2.0f, 19, () -> Ingredient.of(RootsTags.Items.BARKS));
  public static Tier COPPER_TIER = new SimpleTier(BlockTags.INCORRECT_FOR_IRON_TOOL, 150, 4.0f, 2.0f, 2, () -> Ingredient.of(Tags.Items.INGOTS_COPPER));
  public static Tier RUNED_TIER = new SimpleTier(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 2031, 9.0f, 4.0f, 15, () -> Ingredient.of(RootsTags.Items.RUNED_OBSIDIAN));

  public static ResourceLocation rl(String path) {
    return ResourceLocation.fromNamespaceAndPath(RootsAPI.MODID, path);
  }

  // Tool Actions (Forge-specific)
  public static ItemAbility RUNIC_SHEARS_HARVEST = ItemAbility.get("runic_shears_harvest");
  public static ItemAbility RUNIC_SHEARS_DIG = ItemAbility.get("runic_shears_dig");
  public static ItemAbility KNIFE_STRIP = ItemAbility.get("knife_strip");
  public static ItemAbility KNIFE_DIG = ItemAbility.get("knife_dig");

  public static Set<ItemAbility> RUNIC_SHEARS_DEFAULTS = Set.of(RUNIC_SHEARS_HARVEST, RUNIC_SHEARS_DIG);
  public static Set<ItemAbility> KNIFE_DEFAULTS = Set.of(KNIFE_STRIP, KNIFE_DIG);

  public static RootsAPI getInstance() {
    return INSTANCE;
  }

  // Identifiers & Logs
  public static final String MODID = "roots";

  public static ResourceLocation LIVING_TOOL_TIER_ID = rl("living_tool");
  public static final String MOD_IDENTIFIER = "Roots";
  public static Logger LOG = LogManager.getLogger();

  // Capability IDs
  public static final ResourceLocation HERB_CAPABILITY_ID = rl("herb_capability");
  public static final ResourceLocation GRANT_CAPABILITY_ID = rl("grant_capability");
  public static final ResourceLocation SNAPSHOT_CAPABILITY_ID = rl("snapshot_capability");
  public static final ResourceLocation SHOULDER_CAPABILITY_ID = rl("shoulder_capability");
  public static final ResourceLocation REPUTATION_CAPABILITY_ID = rl("reputation_capability");

  public static final ResourceLocation RUNIC_SHEARS_ENTITY_CAPABILITY_ID = rl("runic_shears_entity_capability");
  public static final ResourceLocation RUNIC_SHEARS_TOKEN_CAPABILITY = rl("runic_shears_token_capability");
  public static final ResourceLocation SQUID_MILKING_CAPABILITY = rl("squid_milking_capability");

  // Actual API methods
  public abstract IRecipeManagerAccessor getRecipeAccessor();

  public abstract void grant(ServerPlayer player, Grant grant);

  public abstract boolean canGrant(ServerPlayer player, Grant grant);

  public abstract Player getPlayer();

  public abstract boolean isShiftKeyDown();

  public RecipeManager getRecipeManager() {
    return getRecipeAccessor().getManager();
  }

}
