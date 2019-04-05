package epicsquid.roots.spell;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.spell.modules.SpellModule;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockMushroom;
import net.minecraft.block.BlockYellowFlower;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.EnumPlantType;

import java.util.List;

public class SpellZephyrSlice extends SpellBase {
  public static String spellName = "spell_zephyr_slice";
  public static SpellZephyrSlice instance = new SpellZephyrSlice(spellName);

  public SpellZephyrSlice(String name) {
    super(name, TextFormatting.AQUA, 53f / 255f, 204f / 255f, 234f / 255f, 77f / 255f, 77f / 255f, 77f / 255f);
    this.castType = EnumCastType.INSTANTANEOUS;
    this.cooldown = 20;

    addCost(HerbRegistry.getHerbByName("cloud_berry"), 0.08f);
    addIngredients(
            new ItemStack(Items.GOLDEN_SWORD),
            new ItemStack(ModItems.cloud_berry),
            new ItemStack(Items.APPLE),
            new ItemStack(Items.SHEARS),
            new ItemStack(ModItems.petals)
    );
  }

  @Override
  public boolean cast(EntityPlayer player, List<SpellModule> modules) {
    if (!player.world.isRemote) {
      for (BlockPos pos : Util.getBlocksWithinRadius(player.world, player.getPosition(), 4, 3, 4, (pos) -> {
        IBlockState state = player.world.getBlockState(pos);
        if (state.getBlock() == Blocks.TALLGRASS) return true;
        if (state.getBlock() == Blocks.DOUBLE_PLANT) {
          BlockDoublePlant.EnumPlantType variant = state.getValue(BlockDoublePlant.VARIANT);
          return variant == BlockDoublePlant.EnumPlantType.FERN || variant == BlockDoublePlant.EnumPlantType.GRASS;
        }
        if (state.getBlock() instanceof BlockFlower || state.getBlock() instanceof BlockMushroom) {
          return true;
        }
        return false;
      })) {
        IBlockState state = player.world.getBlockState(pos);
        state.getBlock().harvestBlock(player.world, player, pos, state, null, player.getHeldItemMainhand());
        player.world.setBlockState(pos, Blocks.AIR.getDefaultState());
      }
    }
    return true;
  }
}
