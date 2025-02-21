package mysticmods.roots.api;

import mysticmods.roots.api.attachment.Unlock;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.storage.loot.LootTable;
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

  public static final ResourceKey<LootTable> HUT = ResourceKey.create(Registries.LOOT_TABLE, RootsAPI.rl("hut"));
  public static final ResourceKey<LootTable> BARROW = ResourceKey.create(Registries.LOOT_TABLE, RootsAPI.rl("barrow"));
  public static final ResourceKey<LootTable> STANDING_STONES = ResourceKey.create(Registries.LOOT_TABLE, RootsAPI.rl("standing_stones"));

  public static ResourceLocation rl(String path) {
    return ResourceLocation.fromNamespaceAndPath(RootsAPI.MODID, path);
  }

  public static ResourceLocation parse(String path) {
    return ResourceLocation.parse(path);
  }

  // Tool Actions (Forge-specific)
  public static ItemAbility RUNIC_SHEARS_HARVEST = ItemAbility.get("runic_shears_harvest");
  public static ItemAbility RUNIC_SHEARS_DIG = ItemAbility.get("runic_shears_dig");
  public static ItemAbility KNIFE_STRIP = ItemAbility.get("knife_strip");
  public static ItemAbility KNIFE_DIG = ItemAbility.get("knife_dig");

  public static ItemAbility FORAGE = ItemAbility.get("forage");

  public static Set<ItemAbility> RUNIC_SHEARS_DEFAULTS = Set.of(RUNIC_SHEARS_HARVEST, RUNIC_SHEARS_DIG);
  public static Set<ItemAbility> KNIFE_DEFAULTS = Set.of(KNIFE_STRIP, KNIFE_DIG, FORAGE);

  public static RootsAPI getInstance() {
    return INSTANCE;
  }

  // Identifiers & Logs
  public static final String MODID = "roots";

  public static ResourceLocation LIVING_TOOL_TIER_ID = rl("living_tool");
  public static final String MOD_IDENTIFIER = "Roots";
  public static Logger LOG = LogManager.getLogger();

  public static MutableComponent holdShift() {
    return Component.translatable("roots.tooltip.hold_shift", Component.translatable("roots.tooltip.shift")
        .setStyle(Style.EMPTY.withBold(true).withUnderlined(true).withColor(ChatFormatting.DARK_GRAY)));
  }

  public abstract void unlock(ServerPlayer player, Unlock<?> unlock);

  public abstract boolean canUnlock(ServerPlayer player, Unlock<?> unlock);

  public abstract Player getPlayer();

  public abstract boolean isShiftKeyDown();

  public abstract RecipeManager getRecipeManager();
}
