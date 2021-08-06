package epicsquid.roots.block;

import epicsquid.mysticallib.LibRegistry;
import epicsquid.mysticallib.block.IBlock;
import epicsquid.mysticallib.block.INoCullBlock;
import epicsquid.mysticallib.model.ICustomModeledObject;
import epicsquid.mysticallib.model.IModeledObject;
import epicsquid.roots.init.ModDamage;
import epicsquid.roots.util.EntityUtil;
import net.minecraft.block.BlockFire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class BlockWildFire extends BlockFire implements IBlock, IModeledObject, ICustomModeledObject, INoCullBlock {
  public @Nonnull
  String name;

  public BlockWildFire(@Nonnull String name) {
    super();
    this.name = name;
    setTranslationKey(name);
    setRegistryName(LibRegistry.getActiveModid(), name);
    setLightOpacity(15);
    setLightLevel(1.0f / 4);
    this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, 0).withProperty(NORTH, false).withProperty(EAST, false).withProperty(SOUTH, false).withProperty(WEST, false).withProperty(UPPER, false));
    //itemBlock = new ItemBlock(this).setRegistryName(LibRegistry.getActiveModid(), name);
  }

  @Nullable
  @Override
  public Item getItemBlock() {
    return null; //itemBlock;
  }

  @Override
  public ItemBlock setItemBlock(ItemBlock block) {
    return block;
  }

  @Override
  public boolean noCull() {
    return false;
  }

  @Nullable
  protected IBlockState getParentState() {
    return null;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void initModel() {
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void initCustomModel() {
  }

  @Override
  public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
    // TODO: Maybe make it not affect immunity to fire; but it's FEY fire.
    /*if (entityIn.isImmuneToFire()) {
      return;
    }*/

    if (!EntityUtil.isHostile(entityIn) || EntityUtil.isFriendly(entityIn)) {
      return;
    }

    entityIn.setFire(1);

    if (worldIn.isRemote) return;

    DamageSource feyFire = ModDamage.wildfireDamage(worldIn);
    if (feyFire != null) {
      entityIn.attackEntityFrom(feyFire, 2f);
    }
  }

  @Nullable
  @Override
  @SuppressWarnings("deprecation")
  public net.minecraft.pathfinding.PathNodeType getAiPathNodeType(IBlockState state, IBlockAccess world, BlockPos pos) {
    return PathNodeType.DAMAGE_FIRE;
  }

  @Override
  public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    if (!worldIn.isAreaLoaded(pos, 2)) return; // Forge: prevent loading unloaded chunks when spreading fire
    if (!this.canPlaceBlockAt(worldIn, pos) || worldIn.isAirBlock(pos.down())) {
      worldIn.setBlockToAir(pos);
    }

    int i = state.getValue(AGE);

    if (i < 15) {
      state = state.withProperty(AGE, Math.max(15, i + rand.nextInt(6) / 2));
      worldIn.setBlockState(pos, state, 4);
    } else {
      worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
    }

    worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn) + rand.nextInt(10));
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
    if (rand.nextInt(24) == 0) {
      worldIn.playSound((double) ((float) pos.getX() + 0.5F), (double) ((float) pos.getY() + 0.5F), (double) ((float) pos.getZ() + 0.5F), SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 1.0F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.3F, false);
    }

    if (rand.nextBoolean()) {
      for (int i = 0; i < 3; ++i) {
        double d0 = (double) pos.getX() + rand.nextDouble();
        double d1 = (double) pos.getY() + rand.nextDouble() * 0.5D + 0.5D;
        double d2 = (double) pos.getZ() + rand.nextDouble();
        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.0D, 0.0D);
      }
    }
  }

  @Override
  @SideOnly(Side.CLIENT)
  @SuppressWarnings("deprecation")
  public int getPackedLightmapCoords(IBlockState state, IBlockAccess source, BlockPos pos) {
    return source.getCombinedLight(pos, 15);
  }
}
