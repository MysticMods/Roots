package epicsquid.roots.entity.ritual;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.mechanics.Magnetize;
import epicsquid.roots.network.fx.MessageItemGatheredFX;
import epicsquid.roots.ritual.RitualGathering;
import epicsquid.roots.ritual.RitualRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.List;

public class GatheringRitualEntity extends BaseRitualEntity {
  public static AxisAlignedBB bounding = new AxisAlignedBB(-1, -1, -1, 1, 1, 1);

  private RitualGathering ritual;

  public GatheringRitualEntity(EntityType<?> entityTypeIn, World worldIn) {
    super(entityTypeIn, worldIn);
    ritual = (RitualGathering) RitualRegistry.ritual_gathering;
  }

/*  public GatheringRitualEntity(World worldIn) {
    super(worldIn);
  }*/

  @Override
  protected void registerData() {
    this.getDataManager().register(lifetime, RitualRegistry.ritual_gathering.getDuration() + 20);
  }

  @Override
  public void tick() {
    super.tick();

    if (!world.isRemote) {
      if (this.ticksExisted % ritual.interval == 0) {
        AxisAlignedBB bounds = bounding.offset(getPosition());
        BlockPos start = new BlockPos(bounds.minX, bounds.minY, bounds.minZ);
        BlockPos stop = new BlockPos(bounds.maxX, bounds.maxY, bounds.maxZ);
        TileEntity te = null;
        boolean found = false;
        for (BlockPos pos : BlockPos.getAllInBoxMutable(start, stop)) {
          te = world.getTileEntity(pos);
          if (te != null && te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).isPresent()) {
            found = true;
            break;
          }
        }
        if (found) {
          te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent((cap) -> {
            List<BlockPos> transferredItems = Magnetize.store(world, getPosition(), cap, ritual.radius_x, ritual.radius_y, ritual.radius_z);
            if (!transferredItems.isEmpty()) {
              // TODO: When we add the packets
              //PacketHandler.sendToAllTracking(new MessageItemGatheredFX(transferredItems), this);
            }
          });
        }
      }
    }
  }
}