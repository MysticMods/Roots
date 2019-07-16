package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.network.fx.MessageScatterPlantFX;
import epicsquid.roots.spell.modules.SpellModule;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

import java.util.List;

public class SpellScatter extends SpellBase {

  public static String spellName = "spell_scatter";
  public static SpellScatter instance = new SpellScatter(spellName);

  public SpellScatter(String name) {
    super(name, TextFormatting.DARK_GREEN, 188F/255F, 244F/255F, 151F/255F, 71F/255F, 132F/255F, 30F/255F);

    this.castType = EnumCastType.INSTANTANEOUS;
    this.cooldown = 60;

    addCost(HerbRegistry.getHerbByName("wildroot"), 0.25F);
    addIngredients(
            new ItemStack(ModItems.terra_spores),
            new ItemStack(ModItems.wildroot),
            new ItemStack(epicsquid.mysticalworld.init.ModItems.aubergine),
            new ItemStack(ModItems.terra_moss)
    );
  }

  //Something tells me this method is completely broken :c
  @Override
  public boolean cast(EntityPlayer caster, List<SpellModule> modules) {

    for (int i = -7; i < 8; i++)
    {
      for (int j = -7; j < 8; j++)
      {
        BlockPos pos = new BlockPos(caster.posX + i, caster.posY - 1, caster.posZ + j);

        if (caster.getHeldItemOffhand() != ItemStack.EMPTY && caster.getHeldItemOffhand().getItem() instanceof ItemSeeds)
        {
          ItemSeeds  seeds = (ItemSeeds) caster.getHeldItemOffhand().getItem();
          IBlockState plant = seeds.getPlant(caster.world, pos);

          if(canPlacePlant(caster.world, plant, pos, seeds)/* && caster.world.getBlockState(pos.up()) == Blocks.AIR*/)
          {
            caster.world.setBlockState(pos.up(), plant);
            caster.getHeldItemOffhand().shrink(1);

            //FIXME FX is gone immediately after the seeds are planted
            MessageScatterPlantFX fx = new MessageScatterPlantFX(pos.getX(), pos.getY() + 1, pos.getZ());
            PacketHandler.sendToAllTracking(fx, caster);
          }
        }
        else
          return false;
      }
    }

    return true;
  }

  private boolean canPlacePlant(World world, IBlockState plant, BlockPos pos, ItemSeeds seeds)
  {
      return world.getBlockState(pos).getBlock().canSustainPlant(plant, world, pos, EnumFacing.UP,
              new IPlantable() {
      @Override
      public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
        return seeds.getPlantType(world, pos);
      }

      @Override
      public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
        return seeds.getPlant(world, pos);
      }
      });
  }
}
