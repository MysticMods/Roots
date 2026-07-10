package mysticmods.roots.util;

import com.mojang.authlib.GameProfile;
import mysticmods.roots.api.RootsAPI;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Unbreakable;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.providers.EnchantmentProvider;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.common.util.FakePlayerFactory;

import java.util.UUID;

public class FakePlayerUtil {
  private static boolean initialized = false;

  public static final ResourceKey<EnchantmentProvider> LOOTING_I = ResourceKey.create(Registries.ENCHANTMENT_PROVIDER, RootsAPI.rl("looting_i"));
  public static final ResourceKey<EnchantmentProvider> LOOTING_II = ResourceKey.create(Registries.ENCHANTMENT_PROVIDER, RootsAPI.rl("looting_ii"));
  public static final ResourceKey<EnchantmentProvider> LOOTING_III = ResourceKey.create(Registries.ENCHANTMENT_PROVIDER, RootsAPI.rl("looting_iii"));
  public static final ResourceKey<EnchantmentProvider> SILK_TOUCH = ResourceKey.create(Registries.ENCHANTMENT_PROVIDER, RootsAPI.rl("silk_touch"));
  public static final ResourceKey<EnchantmentProvider> FORTUNE_I = ResourceKey.create(Registries.ENCHANTMENT_PROVIDER, RootsAPI.rl("fortune_i"));
  public static final ResourceKey<EnchantmentProvider> FORTUNE_II = ResourceKey.create(Registries.ENCHANTMENT_PROVIDER, RootsAPI.rl("fortune_ii"));
  public static final ResourceKey<EnchantmentProvider> FORTUNE_III = ResourceKey.create(Registries.ENCHANTMENT_PROVIDER, RootsAPI.rl("fortune_iii"));

  public static ItemStack LOOTING_I_ITEM = ItemStack.EMPTY;
  public static ItemStack LOOTING_II_ITEM = ItemStack.EMPTY;
  public static ItemStack LOOTING_III_ITEM = ItemStack.EMPTY;

  public static ItemStack FORTUNE_I_ITEM = ItemStack.EMPTY;
  public static ItemStack FORTUNE_II_ITEM = ItemStack.EMPTY;
  public static ItemStack FORTUNE_III_ITEM = ItemStack.EMPTY;
  public static ItemStack SILK_TOUCH_ITEM = ItemStack.EMPTY;

  public static final UUID ROOTS_UUID = UUID.fromString("d16d208c-3636-4341-ae0b-bc89e8866e95");
  public static final GameProfile ROOTS = new GameProfile(ROOTS_UUID, "[roots]");

  public static void buildItems(Level pLevel, RandomSource randomSource) {
    if (initialized) {
      return;
    }
    FakePlayerUtil.LOOTING_I_ITEM = new ItemStack(Items.DIAMOND_SWORD);
    FakePlayerUtil.LOOTING_I_ITEM.set(DataComponents.UNBREAKABLE, new Unbreakable(false));
    EnchantmentHelper.enchantItemFromProvider(FakePlayerUtil.LOOTING_I_ITEM, pLevel.registryAccess(), FakePlayerUtil.LOOTING_I, pLevel.getCurrentDifficultyAt(BlockPos.ZERO), randomSource);
    FakePlayerUtil.LOOTING_II_ITEM = new ItemStack(Items.DIAMOND_SWORD);
    FakePlayerUtil.LOOTING_II_ITEM.set(DataComponents.UNBREAKABLE, new Unbreakable(false));
    EnchantmentHelper.enchantItemFromProvider(FakePlayerUtil.LOOTING_II_ITEM, pLevel.registryAccess(), FakePlayerUtil.LOOTING_II, pLevel.getCurrentDifficultyAt(BlockPos.ZERO), randomSource);
    FakePlayerUtil.LOOTING_III_ITEM = new ItemStack(Items.DIAMOND_SWORD);
    FakePlayerUtil.LOOTING_III_ITEM.set(DataComponents.UNBREAKABLE, new Unbreakable(false));
    EnchantmentHelper.enchantItemFromProvider(FakePlayerUtil.LOOTING_III_ITEM, pLevel.registryAccess(), FakePlayerUtil.LOOTING_III, pLevel.getCurrentDifficultyAt(BlockPos.ZERO), randomSource);
    FakePlayerUtil.SILK_TOUCH_ITEM = new ItemStack(Items.DIAMOND_PICKAXE);
    FakePlayerUtil.SILK_TOUCH_ITEM.set(DataComponents.UNBREAKABLE, new Unbreakable(false));
    EnchantmentHelper.enchantItemFromProvider(FakePlayerUtil.SILK_TOUCH_ITEM, pLevel.registryAccess(), FakePlayerUtil.SILK_TOUCH, pLevel.getCurrentDifficultyAt(BlockPos.ZERO), randomSource);
    FakePlayerUtil.FORTUNE_I_ITEM = new ItemStack(Items.DIAMOND_PICKAXE);
    FakePlayerUtil.FORTUNE_I_ITEM.set(DataComponents.UNBREAKABLE, new Unbreakable(false));
    EnchantmentHelper.enchantItemFromProvider(FakePlayerUtil.FORTUNE_I_ITEM, pLevel.registryAccess(), FakePlayerUtil.FORTUNE_I, pLevel.getCurrentDifficultyAt(BlockPos.ZERO), randomSource);
    FakePlayerUtil.FORTUNE_II_ITEM = new ItemStack(Items.DIAMOND_PICKAXE);
    FakePlayerUtil.FORTUNE_II_ITEM.set(DataComponents.UNBREAKABLE, new Unbreakable(false));
    EnchantmentHelper.enchantItemFromProvider(FakePlayerUtil.FORTUNE_II_ITEM, pLevel.registryAccess(), FakePlayerUtil.FORTUNE_II, pLevel.getCurrentDifficultyAt(BlockPos.ZERO), randomSource);
    FakePlayerUtil.FORTUNE_III_ITEM = new ItemStack(Items.DIAMOND_PICKAXE);
    FakePlayerUtil.FORTUNE_III_ITEM.set(DataComponents.UNBREAKABLE, new Unbreakable(false));
    EnchantmentHelper.enchantItemFromProvider(FakePlayerUtil.FORTUNE_III_ITEM, pLevel.registryAccess(), FakePlayerUtil.FORTUNE_III, pLevel.getCurrentDifficultyAt(BlockPos.ZERO), randomSource);
    initialized = true;
  }

  public static Player wielding(ServerLevel level, ItemStack stack) {
    FakePlayer player = FakePlayerFactory.get(level, ROOTS);
    player.setItemInHand(InteractionHand.MAIN_HAND, stack);
    return player;
  }

  public static Player looting(ServerLevel level, int lootingLevel) {
    lootingLevel = Mth.clamp(lootingLevel, 0, 3);
    return wielding(level, switch (lootingLevel) {
      case 1 -> LOOTING_I_ITEM;
      case 2 -> LOOTING_II_ITEM;
      case 3 -> LOOTING_III_ITEM;
      default -> ItemStack.EMPTY;
    });
  }

  public static Player fortune(ServerLevel level, int lootingLevel) {
    lootingLevel = Mth.clamp(lootingLevel, 0, 3);
    return wielding(level, switch (lootingLevel) {
      case 1 -> FORTUNE_I_ITEM;
      case 2 -> FORTUNE_II_ITEM;
      case 3 -> FORTUNE_III_ITEM;
      default -> ItemStack.EMPTY;
    });
  }

  public static Player silkTouch(ServerLevel level) {
    return wielding(level, SILK_TOUCH_ITEM);
  }

  public static void reset() {
    initialized = false;
  }
}
