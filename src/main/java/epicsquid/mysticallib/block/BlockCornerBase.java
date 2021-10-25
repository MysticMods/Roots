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
import epicsquid.mysticallib.model.block.BakedModelInnerCorner;
import epicsquid.mysticallib.model.block.BakedModelOuterCorner;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("deprecation")
public class BlockCornerBase extends BlockBase {
  public static Map<Integer, List<AxisAlignedBB>> boxes = new HashMap<>();

  public static final PropertyBool UP = PropertyBool.create("up");
  public static final PropertyInteger DIR = PropertyInteger.create("dir", 0, 3); //NXNZ -> PXNZ -> PXPZ -> NXPZ corner
  public static final PropertyBool INNER = PropertyBool.create("inner");

  public boolean inner;
  protected @Nullable IBlockState parent = null;

  public BlockCornerBase(@Nonnull Material mat, @Nonnull SoundType type, float hardness, @Nonnull String name, boolean inner) {
    super(mat, type, hardness, name);
    setLightOpacity(0);
    setOpacity(false);
    this.fullBlock = false;
    setModelCustom(true);
    this.inner = inner;
  }

  public BlockCornerBase(@Nonnull IBlockState parent, @Nonnull SoundType type, float hardness, @Nonnull String name, boolean inner) {
    this(parent.getMaterial(), type, hardness, name, inner);
    this.parent = parent;
  }

  @Override
  @Nonnull
  public BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, INNER, UP, DIR);
  }

  @Override
  @Nonnull
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty(INNER, inner).withProperty(UP, (meta / 4) > 0).withProperty(DIR, meta % 4);
  }

  @Override
  public int getMetaFromState(@Nonnull IBlockState state) {
    return (state.getValue(UP) ? 1 : 0) * 4 + state.getValue(DIR);
  }

  @Override
  @Nonnull
  public IBlockState withRotation(@Nonnull IBlockState state, @Nonnull Rotation rot) {
    int newDir = (state.getValue(DIR) + rot.ordinal()) % 4;
    return state.withProperty(DIR, newDir);
  }

  @Override
  public void addCollisionBoxToList(@Nonnull IBlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull AxisAlignedBB entityBox,
      @Nonnull List<AxisAlignedBB> collidingBoxes, @Nullable Entity entity, boolean advanced) {
    float box_precision = BlockSlantBase.box_precision;
    List<AxisAlignedBB> temp = new ArrayList<>();
    boolean up = state.getValue(UP);
    int dir = state.getValue(DIR);
    if (inner) {
      if (!up) {
        switch (dir) {
        case 0:
          for (float i = 0; i < 1; i += box_precision) {
            temp.add(new AxisAlignedBB(0, i, i, 1.0, i + box_precision, 1.0));
          }
          for (float i = 0; i < 1; i += box_precision) {
            temp.add(new AxisAlignedBB(i, i, 0, 1.0, i + box_precision, 1.0));
          }
          break;
        case 1:
          for (float i = 0; i < 1; i += box_precision) {
            temp.add(new AxisAlignedBB(0, i, i, 1.0, i + box_precision, 1.0));
          }
          for (float i = 0; i < 1; i += box_precision) {
            temp.add(new AxisAlignedBB(0, i, 0, 1.0 - i, i + box_precision, 1.0));
          }
          break;
        case 2:
          for (float i = 0; i < 1; i += box_precision) {
            temp.add(new AxisAlignedBB(0, i, 0, 1.0, i + box_precision, 1.0 - i));
          }
          for (float i = 0; i < 1; i += box_precision) {
            temp.add(new AxisAlignedBB(0, i, 0, 1.0 - i, i + box_precision, 1.0));
          }
          break;
        case 3:
          for (float i = 0; i < 1; i += box_precision) {
            temp.add(new AxisAlignedBB(0, i, 0, 1.0, i + box_precision, 1.0 - i));
          }
          for (float i = 0; i < 1; i += box_precision) {
            temp.add(new AxisAlignedBB(i, i, 0, 1.0, i + box_precision, 1.0));
          }
          break;
        }
      } else {
        switch (dir) {
        case 0:
          for (float i = 0; i < 1; i += box_precision) {
            temp.add(new AxisAlignedBB(0, 1.0 - i, i, 1.0, (1.0 - box_precision) - i, 1.0));
          }
          for (float i = 0; i < 1; i += box_precision) {
            temp.add(new AxisAlignedBB(i, 1.0 - i, 0, 1.0, (1.0 - box_precision) - i, 1.0));
          }
          break;
        case 1:
          for (float i = 0; i < 1; i += box_precision) {
            temp.add(new AxisAlignedBB(0, 1.0 - i, i, 1.0, (1.0 - box_precision) - i, 1.0));
          }
          for (float i = 0; i < 1; i += box_precision) {
            temp.add(new AxisAlignedBB(0, 1.0 - i, 0, 1.0 - i, (1.0 - box_precision) - i, 1.0));
          }
          break;
        case 2:
          for (float i = 0; i < 1; i += box_precision) {
            temp.add(new AxisAlignedBB(0, 1.0 - i, 0, 1.0, (1.0 - box_precision) - i, 1.0 - i));
          }
          for (float i = 0; i < 1; i += box_precision) {
            temp.add(new AxisAlignedBB(0, 1.0 - i, 0, 1.0 - i, (1.0 - box_precision) - i, 1.0));
          }
          break;
        case 3:
          for (float i = 0; i < 1; i += box_precision) {
            temp.add(new AxisAlignedBB(0, 1.0 - i, 0, 1.0, (1.0 - box_precision) - i, 1.0 - i));
          }
          for (float i = 0; i < 1; i += box_precision) {
            temp.add(new AxisAlignedBB(i, 1.0 - i, 0, 1.0, (1.0 - box_precision) - i, 1.0));
          }
          break;
        }
      }
    } else {
      if (!up) {
        switch (dir) {
        case 0:
          for (float i = 0; i < 1; i += box_precision) {
            temp.add(new AxisAlignedBB(0, i, 0, 1.0 - i, i + box_precision, 1.0 - i));
          }
          break;
        case 1:
          for (float i = 0; i < 1; i += box_precision) {
            temp.add(new AxisAlignedBB(1.0, i, 0, i, i + box_precision, 1.0 - i));
          }
          break;
        case 2:
          for (float i = 0; i < 1; i += box_precision) {
            temp.add(new AxisAlignedBB(1.0, i, 1.0, i, i + box_precision, i));
          }
          break;
        case 3:
          for (float i = 0; i < 1; i += box_precision) {
            temp.add(new AxisAlignedBB(0, i, 1.0, 1.0 - i, i + box_precision, i));
          }
          break;
        }
      } else {
        switch (dir) {
        case 0:
          for (float i = 0; i < 1; i += box_precision) {
            temp.add(new AxisAlignedBB(0, 1.0 - i, 0, 1.0 - i, (1.0 - i) - box_precision, 1.0 - i));
          }
          break;
        case 1:
          for (float i = 0; i < 1; i += box_precision) {
            temp.add(new AxisAlignedBB(1.0, 1.0 - i, 0, i, (1.0 - i) - box_precision, 1.0 - i));
          }
          break;
        case 2:
          for (float i = 0; i < 1; i += box_precision) {
            temp.add(new AxisAlignedBB(1.0, 1.0 - i, 1.0, i, (1.0 - i) - box_precision, i));
          }
          break;
        case 3:
          for (float i = 0; i < 1; i += box_precision) {
            temp.add(new AxisAlignedBB(0, 1.0 - i, 1.0, 1.0 - i, (1.0 - i) - box_precision, i));
          }
          break;
        }
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
    boolean up = (hitY > 0.5f);
    if (hitY == 1) {
      up = false;
    }
    if (hitY == 0) {
      up = true;
    }
    int dir = 0;
    if (hitX > 0 && hitX < 1 && hitZ > 0 && hitZ < 1) {
      if (hitX < 0.5 && hitZ < 0.5) {
        dir = 0;
      }
      if (hitX >= 0.5 && hitZ < 0.5) {
        dir = 1;
      }
      if (hitX >= 0.5 && hitZ >= 0.5) {
        dir = 2;
      }
      if (hitX < 0.5 && hitZ >= 0.5) {
        dir = 3;
      }
    }
    if (hitX == 0) {
      if (hitZ < 0.5) {
        dir = 1;
      }
      if (hitZ >= 0.5) {
        dir = 2;
      }
    }
    if (hitZ == 0) {
      if (hitX < 0.5) {
        dir = 3;
      }
      if (hitX >= 0.5) {
        dir = 2;
      }
    }
    if (hitX == 1) {
      if (hitZ < 0.5) {
        dir = 0;
      }
      if (hitZ >= 0.5) {
        dir = 3;
      }
    }
    if (hitZ == 1) {
      if (hitX < 0.5) {
        dir = 0;
      }
      if (hitX > 0.5) {
        dir = 1;
      }
    }
    return getDefaultState().withProperty(UP, up).withProperty(DIR, dir);
  }

  @Override
  public boolean shouldSideBeRendered(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull EnumFacing side) {
    return true;
  }

  @Override
  public boolean doesSideBlockRendering(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull EnumFacing side) {
    return false;
  }

  // TODO: Look for a better way of doing this
  @Override
  @SideOnly(Side.CLIENT)
  public void initCustomModel() {
    if (hasCustomModel()) {
      ResourceLocation defaultTex = new ResourceLocation(getRegistryName().getNamespace() + ":blocks/" + getRegistryName().getPath());
      if (parent != null) {
        defaultTex = new ResourceLocation(parent.getBlock().getRegistryName().getNamespace() + ":blocks/" + parent.getBlock().getRegistryName().getPath());
      }
      if (inner) {
        CustomModelLoader.blockmodels.put(new ResourceLocation(getRegistryName().getNamespace() + ":models/block/" + name),
            new CustomModelBlock(getModelClass(0), defaultTex, defaultTex));
        CustomModelLoader.itemmodels.put(new ResourceLocation(getRegistryName().getNamespace() + ":" + name + "#handlers"),
            new CustomModelBlock(getModelClass(0), defaultTex, defaultTex));
      } else {
        CustomModelLoader.blockmodels.put(new ResourceLocation(getRegistryName().getNamespace() + ":models/block/" + name),
            new CustomModelBlock(getModelClass(1), defaultTex, defaultTex));
        CustomModelLoader.itemmodels.put(new ResourceLocation(getRegistryName().getNamespace() + ":" + name + "#handlers"),
            new CustomModelBlock(getModelClass(1), defaultTex, defaultTex));
      }
    }
  }

  @Nonnull
  @Override
  protected Class<? extends BakedModelBlock> getModelClass(int type) {
    if (type == 1) {
      return BakedModelOuterCorner.class;
    } else {
      return BakedModelInnerCorner.class;
    }
  }
}
