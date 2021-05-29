package epicsquid.roots.config;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import epicsquid.mysticallib.util.ConfigUtil;
import epicsquid.roots.Roots;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Config;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Config.LangKey("config.roots.category.moss")
@Config(modid = Roots.MODID, name = "roots/moss", category = "moss")
@SuppressWarnings("unused")
public class MossConfig {
  @Config.Comment(("List of dimension IDs where terra moss harvesting shouldn't work"))
  public static String[] BlacklistDimensions = new String[]{};

  @Config.Ignore
  public static Set<Integer> blacklistDimensions = null;

  public static Set<Integer> getBlacklistDimensions() {
    if (blacklistDimensions == null) {
      blacklistDimensions = new HashSet<>();
      for (String dim : BlacklistDimensions) {
        blacklistDimensions.add(Integer.parseInt(dim));
      }
    }
    return blacklistDimensions;
  }

  @Config.Comment(("List of mod:item:meta,mod:item:meta (meta optional) of mossy thaumcraft.blocks and what to convert them into when scraping with knives [note that logs or thaumcraft.blocks with positional data are unsuited for this purpose]"))
  public static String[] MossyCobblestones = new String[]{"minecraft:mossy_cobblestone,minecraft:cobblestone", "minecraft:stonebrick:1,minecraft:stonebrick", "minecraft:monster_egg:3,minecraft:monster_egg:2"};

  @Config.Ignore
  private static Map<ItemStack, ItemStack> mossyCobblestones = null;

  @Config.Ignore
  private static BiMap<Block, Block> mossyBlocks = HashBiMap.create();

  @Config.Ignore
  private static BiMap<IBlockState, IBlockState> mossyStates = HashBiMap.create();

  @SuppressWarnings("deprecation")
  public static Map<ItemStack, ItemStack> getMossyCobblestones() {
    if (mossyCobblestones == null) {
      mossyCobblestones = ConfigUtil.parseMap(new HashMap<>(), ConfigUtil::parseItemStack, ConfigUtil::parseItemStack, ",", MossyCobblestones);

      mossyBlocks.clear();
      mossyStates.clear();
      for (Map.Entry<ItemStack, ItemStack> entry : mossyCobblestones.entrySet()) {
        ItemStack in = entry.getKey();
        ItemStack out = entry.getValue();
        if (in.getItem() instanceof ItemBlock && out.getItem() instanceof ItemBlock) {
          Block blockIn = ((ItemBlock) in.getItem()).getBlock();
          Block blockOut = ((ItemBlock) out.getItem()).getBlock();
          if (in.getMetadata() != 0 || out.getMetadata() != 0) {
            IBlockState stateIn = blockIn.getStateFromMeta(in.getMetadata());
            IBlockState stateOut = blockOut.getStateFromMeta(out.getMetadata());
            mossyStates.put(stateIn, stateOut);
          } else {
            mossyBlocks.put(blockIn, blockOut);
          }
        }
      }
    }

    return mossyCobblestones;
  }

  @Nullable
  public static IBlockState scrapeResult(IBlockState state) {
    Map<ItemStack, ItemStack> mossy = getMossyCobblestones();

    if (mossyStates.containsKey(state)) {
      return mossyStates.get(state);
    }

    if (mossyBlocks.containsKey(state.getBlock())) {
      return mossyBlocks.get(state.getBlock()).getDefaultState();
    }

    return null;
  }

  @Nullable
  public static IBlockState mossConversion(IBlockState state) {
    Map<ItemStack, ItemStack> mossy = getMossyCobblestones();

    if (mossyStates.inverse().containsKey(state)) {
      return mossyStates.inverse().get(state);
    }

    if (mossyBlocks.inverse().containsKey(state.getBlock())) {
      return mossyBlocks.inverse().get(state.getBlock()).getDefaultState();
    }

    return null;
  }
}
