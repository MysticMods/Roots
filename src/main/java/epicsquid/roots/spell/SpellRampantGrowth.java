package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.network.fx.MessageLifeInfusionFX;
import epicsquid.roots.spell.modules.SpellModule;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SpellRampantGrowth extends SpellBase {
  public static String spellName = "spell_rampant_growth";
  public static SpellRampantGrowth instance = new SpellRampantGrowth(spellName);

  public SpellRampantGrowth(String name) {
    super(name, TextFormatting.GREEN, 48f / 255f, 255f / 255f, 48f / 255f, 192f / 255f, 255f / 255f, 192f / 255f);
    this.castType = EnumCastType.CONTINUOUS;
    this.cooldown = 16;

    addCost(HerbRegistry.getHerbByName("spirit_herb"), 0.3f);
    addCost(HerbRegistry.getHerbByName("pereskia"), 0.2f);
    addIngredients(
            new ItemStack(Blocks.SAPLING, 1, 5),
            new ItemStack(Items.GOLDEN_APPLE),
            new ItemStack(Blocks.SAPLING, 1, 4),
            new ItemStack(ModItems.spirit_herb),
            new ItemStack(ModItems.pereskia)
    );
  }

  private static List<Block> skips = Arrays.asList(Blocks.GRASS, Blocks.TALLGRASS, Blocks.DOUBLE_PLANT);

  @Override
  public boolean cast(EntityPlayer player, List<SpellModule> modules) {
    List<BlockPos> positions = Util.getBlocksWithinRadius(player.world, player.getPosition(), 6, 3, 6, (pos) -> {
      IBlockState state = player.world.getBlockState(pos);
      if (state.getBlock() instanceof IGrowable) {
        Block block = state.getBlock();
        if (skips.contains(block)) return false;
        if (state.getBlock() instanceof BlockFlower) return false;
        return ((IGrowable) state.getBlock()).canGrow(player.world, pos, state, false);
      }
      return false;
    });
    if (positions.isEmpty()) return false;
    if (!player.world.isRemote) {
      for (int i = 0; i < 2 + player.world.rand.nextInt(4); i++) {
        BlockPos pos = positions.get(player.world.rand.nextInt(positions.size()));
        IBlockState state = player.world.getBlockState(pos);
        for (int j = 0; j < 3; j++) {
          state.getBlock().randomTick(player.world, pos, state, new Random());
        }
        PacketHandler.sendToAllTracking(new MessageLifeInfusionFX(pos.getX(), pos.getY(), pos.getZ()), player);
      }
    }
    return true;
  }

}
