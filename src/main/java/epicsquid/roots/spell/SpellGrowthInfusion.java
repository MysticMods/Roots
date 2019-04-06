package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.network.fx.MessageLifeInfusionFX;
import epicsquid.roots.spell.modules.SpellModule;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreIngredient;

import java.util.List;
import java.util.Random;

public class SpellGrowthInfusion extends SpellBase {
  public static String spellName = "spell_growth_infusion";
  public static SpellGrowthInfusion instance = new SpellGrowthInfusion(spellName);

  public SpellGrowthInfusion(String name) {
    super(name, TextFormatting.GREEN, 48f / 255f, 255f / 255f, 48f / 255f, 192f / 255f, 255f / 255f, 192f / 255f);
    this.castType = SpellBase.EnumCastType.CONTINUOUS;
    this.cooldown = 16;

    addCost(HerbRegistry.getHerbByName("terra_moss"), 0.08f);
    addIngredients(
        new OreIngredient("treeSapling"),
        new OreIngredient("treeSapling"),
        new ItemStack(ModItems.terra_moss),
        new ItemStack(ModItems.aubergine),
        new ItemStack(Items.WHEAT)
    );
  }

  @Override
  public boolean cast(EntityPlayer player, List<SpellModule> modules) {
    RayTraceResult result = player.world.rayTraceBlocks(player.getPositionVector().addVector(0, player.getEyeHeight(), 0), player.getLookVec().scale(8.0f).add(player.getPositionVector().addVector(0, player.getEyeHeight(), 0)));
      if (result != null) {
        if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
          BlockPos pos = result.getBlockPos();
          IBlockState state = player.world.getBlockState(pos);
          Block block = state.getBlock();
          if (block instanceof IGrowable && !((IGrowable) block).canGrow(player.world, pos, state, player.world.isRemote)) return false;
          if (!player.world.isRemote) {
            for (int i = 0; i < 1; i++) {
              state.getBlock().randomTick(player.world, pos, state, new Random());
            }
            PacketHandler.sendToAllTracking(new MessageLifeInfusionFX(pos.getX(), pos.getY(), pos.getZ()), player);
          }
        }
      }
    return true;
  }
}
