package epicsquid.roots.block;

import com.google.common.collect.Lists;
import epicsquid.mysticallib.LibRegistry;
import epicsquid.mysticallib.block.BlockBase;
import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.roots.api.CustomPlantType;
import epicsquid.roots.config.GeneralConfig;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.item.itemblock.ItemBlockElementalSoil;
import epicsquid.roots.mechanics.Harvest;
import epicsquid.roots.util.EnumElementalSoilType;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.BlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockElementalSoil extends BlockBase {
  public static final PropertyInteger WATER_SPEED = PropertyInteger.create("water", 0, 4);
  public static final PropertyInteger AIR_SPEED = PropertyInteger.create("air", 0, 4);
  public static final PropertyInteger EARTH_FERTILITY = PropertyInteger.create("earth", 0, 4);
  public static final PropertyInteger FIRE_MULTIPLIER = PropertyInteger.create("fire", 0, 4);

  private final @Nonnull
  Item itemBlock;
  private EnumElementalSoilType soilType;

  public static EnumElementalSoilType SOIL_INIT = EnumElementalSoilType.BASE;

  public BlockElementalSoil(@Nonnull Material mat, @Nonnull SoundType type, @Nonnull String name, @Nonnull EnumElementalSoilType soilType) {
    super(mat, type, 0.8f, name);
    this.soilType = soilType;
    this.itemBlock = new ItemBlockElementalSoil(this).setRegistryName(LibRegistry.getActiveModid(), name);
    this.setHarvestReqs("shovel", 0);
    this.setTickRandomly(true);

    if (this.soilType != EnumElementalSoilType.BASE) {
      PropertyInteger property = this.soilType == EnumElementalSoilType.WATER ?
          WATER_SPEED :
          this.soilType == EnumElementalSoilType.EARTH ? EARTH_FERTILITY : this.soilType == EnumElementalSoilType.AIR ? AIR_SPEED : FIRE_MULTIPLIER;

      this.setDefaultState(this.blockState.getBaseState().withProperty(property, 1));
    }
  }

  public void doHarvest(BlockEvent.CropGrowEvent.Post cropGrowEvent) {
    BlockPos pos = cropGrowEvent.getPos();
    BlockState soil = cropGrowEvent.getWorld().getBlockState(pos.down());
    BlockState plant = cropGrowEvent.getWorld().getBlockState(pos);
    World world = cropGrowEvent.getWorld();
    doHarvest(world, pos, soil, plant);
    world.scheduleUpdate(pos, this, 30);
  }

  private boolean shouldHarvest(World world, BlockPos pos) {
    // Assume pos is the location of the crop
    return world.getBlockState(pos.down().down()).getBlock() != Blocks.GRAVEL;
  }

  private void doHarvest(World world, BlockPos pos, BlockState soil, BlockState plant) {
    if (soil.getBlock() != ModBlocks.elemental_soil_water) return;

    if (shouldHarvest(world, pos) && plant.getBlock() instanceof IPlantable && Harvest.isGrown(plant) && soil.getBlock().canSustainPlant(soil, world, pos.down(), Direction.UP, (IPlantable) plant.getBlock())) {
      if (soil.getBlock() == ModBlocks.elemental_soil_water) {
        int speed = soil.getValue(BlockElementalSoil.WATER_SPEED);
        if (speed > 0) {
          List<ItemStack> drops = Harvest.harvestReturnDrops(plant, pos, world, null);
          handleDrops(world, pos, drops);
        }
      }
    }
  }

  private void handleDrops(World world, BlockPos pos, List<ItemStack> drops) {
    List<ItemStack> dropsList = Lists.newArrayList(drops);
    BlockPos under = pos.down().down();
    TileEntity te = world.getTileEntity(under);
    if (te != null) {
      IItemHandler cap = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
      if (cap != null) {
        List<ItemStack> remainder = new ArrayList<>();
        for (ItemStack next : dropsList) {
          if (next.isEmpty()) continue;

          ItemStack remains = ItemHandlerHelper.insertItemStacked(cap, next, false);
          if (!remains.isEmpty()) {
            remainder.add(remains);
          }
        }
        dropsList.clear();
        if (!remainder.isEmpty()) {
          dropsList.addAll(remainder);
        }
      }
    }
    if (!dropsList.isEmpty()) {
      for (ItemStack stack : dropsList) {
        ItemUtil.spawnItem(world, pos, stack);
      }
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  @Nonnull
  public BlockState getStateFromMeta(int meta) {
    if (soilType == null) {
      soilType = SOIL_INIT;
    }
    switch (soilType) {
      case AIR:
        return getDefaultState().withProperty(AIR_SPEED, meta + 1);
      case FIRE:
        return getDefaultState().withProperty(FIRE_MULTIPLIER, meta + 1);
      case EARTH:
        return getDefaultState().withProperty(EARTH_FERTILITY, meta + 1);
      case WATER:
        return getDefaultState().withProperty(WATER_SPEED, meta + 1);
      case BASE:
      default:
        return getDefaultState();
    }
  }

  @Nonnull
  @Override
  protected BlockStateContainer createBlockState() {
    if (soilType == null) {
      soilType = SOIL_INIT;
    }
    switch (soilType) {
      case AIR:
        return new BlockStateContainer(this, AIR_SPEED);
      case FIRE:
        return new BlockStateContainer(this, FIRE_MULTIPLIER);
      case EARTH:
        return new BlockStateContainer(this, EARTH_FERTILITY);
      case WATER:
        return new BlockStateContainer(this, WATER_SPEED);
      case BASE:
      default:
        return new BlockStateContainer(this);
    }
  }

  @Override
  public int getMetaFromState(BlockState state) {
    PropertyInteger property = this.soilType == EnumElementalSoilType.WATER ?
        WATER_SPEED :
        this.soilType == EnumElementalSoilType.EARTH ? EARTH_FERTILITY : this.soilType == EnumElementalSoilType.AIR ? AIR_SPEED :
            this.soilType == EnumElementalSoilType.BASE ? null : FIRE_MULTIPLIER;

    if (property == null) return 0;

    return state.get(property) - 1 > 0 ? state.get(property) - 1 : 0;
  }

  @Override
  public boolean canSustainPlant(@Nonnull BlockState state, @Nonnull IBlockAccess world, BlockPos pos, @Nonnull Direction direction, IPlantable plantable) {
    if (soilType == EnumElementalSoilType.WATER && plantable == Blocks.REEDS) {
      return true;
    } else if (plantable == Blocks.CACTUS) {
      return soilType == EnumElementalSoilType.EARTH;
    }

    EnumPlantType plant = plantable.getPlantType(world, pos.offset(direction));
    switch (plant) {
      case Nether:
      case Cave:
      case Crop:
      case Desert:
      case Plains:
        return true;
    }
    return plant == CustomPlantType.ELEMENT_FIRE
        || plant == CustomPlantType.ELEMENT_AIR
        || plant == CustomPlantType.ELEMENT_EARTH
        || plant == CustomPlantType.ELEMENT_WATER;
  }

  @Override
  public void updateTick(World world, BlockPos pos, BlockState state, Random rand) {
    super.updateTick(world, pos, state, rand);

    if (world.isAirBlock(pos.up())) {
      return;
    }

    BlockState upState = world.getBlockState(pos.up());
    Block upBlock = upState.getBlock();
    doHarvest(world, pos.up(), world.getBlockState(pos), upState);

    if (!(upBlock instanceof IGrowable))
      return;

    // TODO: Who knows if this value is any good
    if (rand.nextInt(5) == 0) {
      upBlock.randomTick(world, pos.up(), upState, rand);
    }
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void randomDisplayTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
    if (GeneralConfig.DisableParticles) {
      return;
    }

    if (soilType == EnumElementalSoilType.FIRE && rand.nextInt(5) == 0) {
      double d8 = pos.getX() + (double) rand.nextFloat();
      double d4 = pos.getY() + stateIn.getBoundingBox(worldIn, pos).maxY - 0.2f;
      double d6 = pos.getZ() + (double) rand.nextFloat();
      worldIn.spawnParticle(EnumParticleTypes.LAVA, d8, d4, d6, 0.0D, 0.0D, 0.0D);
    }

    if (soilType == EnumElementalSoilType.WATER) {
      for (int i = 0; i < 2; i++) {
        double x_offset = Math.min(Math.max(0.05, rand.nextDouble()), 0.95);
        double y_offset = Math.min(Math.max(0.05, rand.nextDouble()), 0.95);

        double d0 = (double) pos.getX() + x_offset;
        double d1 = (double) pos.getY() + 1D;
        double d2 = (double) pos.getZ() + y_offset;
        worldIn.spawnParticle(EnumParticleTypes.WATER_SPLASH, d0, d1, d2, 0, 1.5D, 0);
      }
    }

    if (soilType == EnumElementalSoilType.AIR && rand.nextInt(13) == 0) {
      for (int i = 0; i < 2; i++) {
        double x_offset = Math.min(Math.max(0.05, rand.nextDouble()), 0.95);
        double y_offset = Math.min(Math.max(0.05, rand.nextDouble()), 0.95);

        double d0 = (double) pos.getX() + x_offset;
        double d1 = (double) pos.getY() + 0.5D;
        double d2 = (double) pos.getZ() + y_offset;

        worldIn.spawnParticle(EnumParticleTypes.CLOUD, d0, d1, d2, 0.0D, 0.05D, 0.0D);
      }
    }

    if (soilType == EnumElementalSoilType.EARTH && rand.nextInt(6) == 0) {
      for (int i = 0; i < 2 + rand.nextInt(3); i++) {
        double x_offset = Math.min(Math.max(0.05, rand.nextDouble()), 0.95);
        double y_offset = Math.min(Math.max(0.05, rand.nextDouble()), 0.95);

        double d0 = (double) pos.getX() + x_offset;
        double d1 = (double) pos.getY() + 1D;
        double d2 = (double) pos.getZ() + y_offset;
        worldIn.spawnParticle(EnumParticleTypes.BLOCK_CRACK, d0, d1, d2, 0, 1D, 0, Block.getStateId(stateIn));
      }
    }
  }

  @Override
  public Item getItemBlock() {
    return itemBlock;
  }

  @Override
  public boolean isFertile(@Nonnull World world, @Nonnull BlockPos pos) {
    return true;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
    if (stack.getItem() instanceof BlockItem) {
      Block type = ((BlockItem) stack.getItem()).getBlock();
      if (type == ModBlocks.elemental_soil_fire) {
        tooltip.add("");
        tooltip.add(TextFormatting.RED + "" + TextFormatting.BOLD + I18n.format("tile.magmatic_soil.effect"));
      } else if (type == ModBlocks.elemental_soil_air) {
        tooltip.add("");
        tooltip.add(TextFormatting.AQUA + "" + TextFormatting.BOLD + I18n.format("tile.caelic_soil.effect"));
      } else if (type == ModBlocks.elemental_soil_earth) {
        tooltip.add("");
        tooltip.add(TextFormatting.YELLOW + "" + TextFormatting.BOLD + I18n.format("tile.terran_soil.effect"));
      } else if (type == ModBlocks.elemental_soil_water) {
        tooltip.add("");
        tooltip.add(TextFormatting.BLUE + "" + TextFormatting.BOLD + I18n.format("tile.aqueous_soil.effect"));
      }
    }
  }

  @Override
  @SuppressWarnings("deprecation")
  public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
    worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
    super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
  }
}

