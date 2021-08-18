package epicsquid.roots.block;

import epicsquid.mysticallib.block.BlockTEBase;
import epicsquid.roots.Roots;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;

@SuppressWarnings("deprecation")
public class BlockReinforcedCatalystPlate extends BlockCatalystPlate {
  public BlockReinforcedCatalystPlate(@Nonnull Material mat, @Nonnull SoundType type, float hardness, @Nonnull String name, @Nonnull Class<? extends TileEntity> teClass) {
    super(mat, type, hardness, name, teClass);
    setResistance(5000f);
  }

  @Override
  public void attemptRegistry(@Nonnull Class<? extends TileEntity> c, String name) {
    if (!BlockTEBase.classes.contains(c)) {
      BlockTEBase.classes.add(c);
      GameRegistry.registerTileEntity(c, new ResourceLocation(Roots.MODID, "tile_entity_reinforced_catalyst_plate"));
    }
  }

  @Override
  public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity) {
    if (entity instanceof net.minecraft.entity.boss.EntityDragon || (entity instanceof net.minecraft.entity.boss.EntityWither) || (entity instanceof net.minecraft.entity.projectile.EntityWitherSkull)) {
      return false;
    }

    return super.canEntityDestroy(state, world, pos, entity);
  }
}
