package epicsquid.roots.block;

import epicsquid.mysticallib.block.Block;
import epicsquid.roots.Roots;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;

@SuppressWarnings("deprecation")
public class BlockReinforcedPyre extends BlockPyre {
  public BlockReinforcedPyre(@Nonnull Material mat, @Nonnull SoundType type, float hardness, @Nonnull String name, @Nonnull Class<? extends TileEntity> teClass) {
    super(mat, type, hardness, name, teClass);
    setResistance(5000f);
  }

  @Override
  public void attemptRegistry(@Nonnull Class<? extends TileEntity> c, String name) {
    if (!Block.classes.contains(c)) {
      Block.classes.add(c);
      GameRegistry.registerTileEntity(c, new ResourceLocation(Roots.MODID, "tile_entity_reinforced_pyre"));
    }
  }

  @Override
  public boolean canEntityDestroy(BlockState state, IBlockAccess world, BlockPos pos, Entity entity) {
    if (entity instanceof EnderDragonEntity || (entity instanceof WitherEntity) || (entity instanceof WitherSkullEntity)) {
      return false;
    }

    return super.canEntityDestroy(state, world, pos, entity);
  }
}
