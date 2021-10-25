package epicsquid.mysticallib.block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import epicsquid.mysticallib.model.CustomModelBlock;
import epicsquid.mysticallib.model.CustomModelLoader;
import epicsquid.mysticallib.model.block.BakedModelBlock;
import epicsquid.mysticallib.model.block.BakedModelSlant;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("deprecation")
public class BlockSlantBase extends BlockBase {
  public static float box_precision = 0.125f;
  public static @Nonnull Map<Integer, List<AxisAlignedBB>> boxes = new HashMap<>();
  public static final PropertyInteger VERT = PropertyInteger.create("vert", 0, 2); //LOW, MID, HIGH
  public static final PropertyInteger DIR = PropertyInteger.create("dir", 0, 3); //NSWE, for mod NXNZ -> NXPZ -> PXPZ -> PXNZ corner

  protected @Nullable IBlockState parent = null;

  public BlockSlantBase(@Nonnull Material mat, @Nonnull SoundType type, float hardness, @Nonnull String name) {
    super(mat, type, hardness, name);
    setLightOpacity(0);
    setOpacity(false);
    this.fullBlock = false;
    setModelCustom(true);
  }

  public BlockSlantBase(@Nonnull IBlockState parent, @Nonnull SoundType type, float hardness, @Nonnull String name) {
    super(parent.getMaterial(), type, hardness, name);
    setLightOpacity(0);
    setOpacity(false);
    this.fullBlock = false;
    this.parent = parent;
    setModelCustom(true);
  }

  @Override
  @Nonnull
  public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
    switch (state.getValue(VERT)) {
    case 0:
      return !(face == EnumFacing.UP) && !(face.getOpposite().getIndex() == state.getValue(DIR) + 2) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    case 1:
      switch (state.getValue(DIR)) {
      case 0:
        return !(face == EnumFacing.EAST || face == EnumFacing.SOUTH) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
      case 1:
        return !(face == EnumFacing.WEST || face == EnumFacing.SOUTH) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
      case 2:
        return !(face == EnumFacing.WEST || face == EnumFacing.NORTH) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
      case 3:
        return !(face == EnumFacing.EAST || face == EnumFacing.NORTH) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
      }
      break;
    case 2:
      return !(face == EnumFacing.DOWN) && !(face.getOpposite().getIndex() == state.getValue(DIR) + 2) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }
    return BlockFaceShape.SOLID;
  }

  @Override
  public boolean onBlockActivated(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityPlayer player, @Nonnull EnumHand hand,
      @Nonnull EnumFacing face, float hitX, float hitY, float hitZ) {
    if (!world.isRemote && hand == EnumHand.MAIN_HAND) {
      world.setBlockState(pos, state.withRotation(Rotation.COUNTERCLOCKWISE_90), 8);
      return true;
    }
    return false;
  }

  @Override
  @Nonnull
  public BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, VERT, DIR);
  }

  @Override
  @Nonnull
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty(VERT, (meta / 4)).withProperty(DIR, meta % 4);
  }

  @Override
  @Nonnull
  public IBlockState withRotation(@Nonnull IBlockState state, @Nonnull Rotation rot) {
    if (state.getValue(VERT) == 1) {
      int newDir = (state.getValue(DIR) + rot.ordinal()) % 4;
      return state.withProperty(DIR, newDir);
    } else {
      EnumFacing face = EnumFacing.byIndex(state.getValue(DIR) + 2);
      return state.withProperty(DIR, rot.rotate(face).getIndex() - 2);
    }
  }

  @Override
  public int getMetaFromState(@Nonnull IBlockState state) {
    return state.getValue(VERT) * 4 + state.getValue(DIR);
  }

  @Override
  public boolean shouldSideBeRendered(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull EnumFacing side) {
    return true;
  }

  @Override
  public boolean doesSideBlockRendering(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull EnumFacing side) {
    return false;
  }

  @Override
  public void addCollisionBoxToList(@Nonnull IBlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull AxisAlignedBB entityBox,
      @Nonnull List<AxisAlignedBB> collidingBoxes, @Nonnull Entity entity, boolean advanced) {
    List<AxisAlignedBB> temp = new ArrayList<>();
    int vert = state.getValue(VERT);
    int dir = state.getValue(DIR);
    if (vert == 0) {
      switch (dir) {
      case 0:
        for (float i = 0; i < 1; i += box_precision) {
          temp.add(new AxisAlignedBB(0, i, 0, 1.0, i + box_precision, 1.0 - i));
        }
        break;
      case 1:
        for (float i = 0; i < 1; i += box_precision) {
          temp.add(new AxisAlignedBB(0, i, i, 1.0, i + box_precision, 1.0));
        }
        break;
      case 2:
        for (float i = 0; i < 1; i += box_precision) {
          temp.add(new AxisAlignedBB(0, i, 0, 1.0 - i, i + box_precision, 1.0));
        }
        break;
      case 3:
        for (float i = 0; i < 1; i += box_precision) {
          temp.add(new AxisAlignedBB(i, i, 0, 1.0, i + box_precision, 1.0));
        }
        break;
      }
    } else if (vert == 1) {
      switch (dir) {
      case 0:
        for (float i = 0; i < 1; i += box_precision) {
          temp.add(new AxisAlignedBB(0, 0, i, 1.0 - i, 1.0, i + box_precision));
        }
        break;
      case 1:
        for (float i = 0; i < 1; i += box_precision) {
          temp.add(new AxisAlignedBB(i, 0, i, 1.0, 1.0, i + box_precision));
        }
        break;
      case 2:
        for (float i = 0; i < 1; i += box_precision) {
          temp.add(new AxisAlignedBB(i, 0, 1.0 - i, 1.0, 1.0, (1.0 - box_precision) - i));
        }
        break;
      case 3:
        for (float i = 0; i < 1; i += box_precision) {
          temp.add(new AxisAlignedBB(0, 0, 1.0 - i, 1.0 - i, 1.0, (1.0 - box_precision) - i));
        }
        break;
      }
    } else if (vert == 2) {
      switch (dir) {
      case 0:
        for (float i = 0; i < 1; i += box_precision) {
          temp.add(new AxisAlignedBB(0, 1.0 - i, 0, 1.0, (1.0 - box_precision) - i, 1.0 - i));
        }
        break;
      case 1:
        for (float i = 0; i < 1; i += box_precision) {
          temp.add(new AxisAlignedBB(0, 1.0 - i, i, 1.0, (1.0 - box_precision) - i, 1.0));
        }
        break;
      case 2:
        for (float i = 0; i < 1; i += box_precision) {
          temp.add(new AxisAlignedBB(0, 1.0 - i, 0, 1.0 - i, (1.0 - box_precision) - i, 1.0));
        }
        break;
      case 3:
        for (float i = 0; i < 1; i += box_precision) {
          temp.add(new AxisAlignedBB(i, 1.0 - i, 0, 1.0, (1.0 - box_precision) - i, 1.0));
        }
        break;
      }
    }
    for (AxisAlignedBB b : temp) {
      addCollisionBoxToList(pos, entityBox, collidingBoxes, b);
    }
  }

  @Override
  @Nonnull
  public IBlockState getStateForPlacement(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing face, float hitX, float hitY, float hitZ, int meta,
      @Nonnull EntityLivingBase placer) {
    int vert = 1;
    if (hitY > 0 && hitY < 1 && (hitX > 0 && hitX < 1 && Math.abs(hitY - 0.5) > Math.abs(hitX - 0.5) || hitZ > 0 && hitZ < 1 && Math.abs(hitY - 0.5) > Math
        .abs(hitZ - 0.5))) {
      vert = 1 + (int) Math.signum(hitY - 0.5);
    }
    if (hitY == 0) {
      vert = 2;
    }
    if (hitY == 1) {
      vert = 0;
    }
    int dir = 3;
    if (vert == 1) {
      if (hitX == 0) {
        if (hitZ < 0.5) {
          dir = 1;
        } else {
          dir = 2;
        }
      } else if (hitZ == 0) {
        if (hitX < 0.5) {
          dir = 3;
        } else {
          dir = 2;
        }
      } else if (hitX == 1) {
        if (hitZ < 0.5) {
          dir = 0;
        } else {
          dir = 3;
        }
      } else if (hitZ == 1) {
        if (hitX < 0.5) {
          dir = 0;
        } else {
          dir = 1;
        }
      }
    } else {
      dir = placer.getHorizontalFacing().getIndex() - 2;
    }
    return getDefaultState().withProperty(VERT, vert).withProperty(DIR, dir);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void initCustomModel() {
    if (hasCustomModel()) {
      ResourceLocation defaultTex = new ResourceLocation(getRegistryName().getNamespace() + ":blocks/" + getRegistryName().getPath());
      if (parent != null) {
        defaultTex = new ResourceLocation(parent.getBlock().getRegistryName().getNamespace() + ":blocks/" + parent.getBlock().getRegistryName().getPath());
      }
      CustomModelLoader.blockmodels
          .put(new ResourceLocation(getRegistryName().getNamespace() + ":models/block/" + name), new CustomModelBlock(getModelClass(), defaultTex, defaultTex));
      CustomModelLoader.itemmodels.put(new ResourceLocation(getRegistryName().getNamespace() + ":" + name + "#handlers"),
          new CustomModelBlock(getModelClass(), defaultTex, defaultTex));
    }
  }

  @Nullable
  @Override
  protected IBlockState getParentState() {
    return parent;
  }

  @Nonnull
  @Override
  protected Class<? extends BakedModelBlock> getModelClass() {
    return BakedModelSlant.class;
  }
}
