package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.network.fx.MessageScatterPlantFX;
import epicsquid.roots.spell.modules.SpellModule;
import epicsquid.roots.util.types.Property;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
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

  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(60);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("wildroot", 0.25));
  public static Property<Integer> PROP_RADIUS = new Property<>("radius", 7);

  public static String spellName = "spell_scatter";
  public static SpellScatter instance = new SpellScatter(spellName);

  private int radius;

  public SpellScatter(String name) {
    super(name, TextFormatting.DARK_GREEN, 188F/255F, 244F/255F, 151F/255F, 71F/255F, 132F/255F, 30F/255F);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_RADIUS);
  }

  @Override
  public void init() {
    addIngredients(
            new ItemStack(ModItems.terra_spores),
            new ItemStack(ModItems.wildroot),
            new ItemStack(epicsquid.mysticalworld.init.ModItems.aubergine),
            new ItemStack(ModItems.terra_moss)
    );
  }

  @Override
  public boolean cast(EntityPlayer caster, List<SpellModule> modules) {

    List<BlockPos> blocks = Util.getBlocksWithinRadius(caster.world, caster.getPosition().down(), radius, 1, radius, Blocks.FARMLAND);
    boolean hadEffect = false;

    for (BlockPos pos : blocks)
    {
      if (caster.getHeldItemOffhand() != ItemStack.EMPTY && caster.getHeldItemOffhand().getItem() instanceof ItemSeeds)
      {
        ItemSeeds  seeds = (ItemSeeds) caster.getHeldItemOffhand().getItem();
        IBlockState plant = seeds.getPlant(caster.world, pos);

        if(canPlacePlant(caster.world, plant, pos, seeds) && caster.world.isAirBlock(pos.up()))
        {
          caster.world.setBlockState(pos.up(), plant);

          if (!caster.isCreative())
            caster.getHeldItemOffhand().shrink(1);

          MessageScatterPlantFX fx = new MessageScatterPlantFX(pos.getX(), pos.getY() + 1, pos.getZ());
          PacketHandler.sendToAllTracking(fx, caster);

          hadEffect = true;
        }
      }
    }
    return hadEffect;
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

  @Override
  public void doFinalise() {
    this.castType = properties.getProperty(PROP_CAST_TYPE);
    this.cooldown = properties.getProperty(PROP_COOLDOWN);
    this.radius = properties.getProperty(PROP_RADIUS);
  }

}
