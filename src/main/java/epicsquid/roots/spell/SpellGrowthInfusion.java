package epicsquid.roots.spell;

import java.util.List;
import java.util.Random;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.network.fx.MessageLifeInfusionFX;
import epicsquid.roots.spell.modules.SpellModule;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;

public class SpellGrowthInfusion extends SpellBase {
  public static String spellName = "spell_growth_infusion";
  public static SpellGrowthInfusion instance = new SpellGrowthInfusion(spellName);

  public SpellGrowthInfusion(String name) {
    super(name, TextFormatting.GREEN, 48f / 255f, 255f / 255f, 48f / 255f, 192f / 255f, 255f / 255f, 192f / 255f);
    this.castType = SpellBase.EnumCastType.CONTINUOUS;
    this.cooldown = 16;

    addCost(HerbRegistry.getHerbByName("spirit_herb"), 0.25f);
    addCost(HerbRegistry.getHerbByName("pereskia"), 0.125f);
    addIngredients(
        new ItemStack(Blocks.SAPLING, 1, 2),
        new ItemStack(ModItems.aubergine_seed),
        new ItemStack(Blocks.SAPLING, 1, 1),
        new ItemStack(ModItems.spirit_herb),
        new ItemStack(ModItems.pereskia)
    );
  }

  @Override
  public boolean cast(EntityPlayer player, List<SpellModule> modules) {
    if (!player.world.isRemote) {
      RayTraceResult result = player.world.rayTraceBlocks(player.getPositionVector().addVector(0, player.getEyeHeight(), 0),
          player.getLookVec().scale(8.0f).add(player.getPositionVector().addVector(0, player.getEyeHeight(), 0)));
      if (result != null) {
        if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
          BlockPos pos = result.getBlockPos();
          IBlockState state = player.world.getBlockState(pos);
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
