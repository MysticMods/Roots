package epicsquid.roots.entity.ritual;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.mechanics.Magnetize;
import epicsquid.roots.network.fx.MessageItemGatheredFX;
import epicsquid.roots.network.fx.MessageOvergrowthEffectFX;
import epicsquid.roots.ritual.RitualRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class EntityRitualGathering extends EntityRitualBase {
  protected static Random random = new Random();
  protected static final DataParameter<Integer> lifetime = EntityDataManager.createKey(EntityRitualGathering.class, DataSerializers.VARINT);
  public static AxisAlignedBB bounding = new AxisAlignedBB(-1, -1, -1, 1, 1, 1);


  public EntityRitualGathering(World worldIn) {
    super(worldIn);
    this.getDataManager().register(lifetime, RitualRegistry.ritual_gathering.getDuration() + 20);
  }

  @Override
  public void onUpdate() {
    super.onUpdate();
    int curLifetime = getDataManager().get(lifetime);
    getDataManager().set(lifetime, curLifetime - 1);
    getDataManager().setDirty(lifetime);
    if (getDataManager().get(lifetime) < 0) {
      setDead();
    }
    if (!world.isRemote) {
      if (this.ticksExisted % 80 == 0) {
        AxisAlignedBB bounds = bounding.offset(getPosition());
        BlockPos start = new BlockPos(bounds.minX, bounds.minY, bounds.minZ);
        BlockPos stop = new BlockPos(bounds.maxX, bounds.maxY, bounds.maxZ);
        TileEntity te = null;
        boolean found = false;
        for (BlockPos.MutableBlockPos pos : BlockPos.getAllInBoxMutable(start, stop)) {
          te = world.getTileEntity(pos);
          if (te != null && te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
            found = true;
            break;
          }
        }
        if (found && te != null) {
          IItemHandler cap = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
          if (cap != null) {
            List<BlockPos> transferredItems = Magnetize.store(world, getPosition(), cap, 15, 15, 15);
            if (!transferredItems.isEmpty()) {
              PacketHandler.sendToAllTracking(new MessageItemGatheredFX(transferredItems), this);
            }
          }
        }
      }
    }
  }

  @Override
  public DataParameter<Integer> getLifetime() {
    return lifetime;
  }
}