package epicsquid.roots.entity.ritual;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.mechanics.Magnetize;
import epicsquid.roots.network.fx.MessageItemGatheredFX;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.ritual.RitualGathering;
import epicsquid.roots.ritual.RitualRegistry;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.List;

public class EntityRitualGathering extends EntityRitualBase {
  public static AxisAlignedBB bounding = new AxisAlignedBB(-1, -1, -1, 2, 2, 2);

  private RitualGathering ritual;

  public EntityRitualGathering(World worldIn) {
    super(worldIn);
    this.getDataManager().register(lifetime, RitualRegistry.ritual_gathering.getDuration() + 20);
    ritual = (RitualGathering) RitualRegistry.ritual_gathering;
  }

  @Override
  public void onUpdate() {
    super.onUpdate();
    float alpha = (float) Math.min(40, (RitualRegistry.ritual_summon_creatures.getDuration() + 20) - getDataManager().get(lifetime)) / 40.0f;
    if (world.isRemote && getDataManager().get(lifetime) > 0) {
      for (float i = 0; i < 360; i += rand.nextFloat() * 90.0f) {
        float vx = -(0.09f * (float) Math.sin(Math.toRadians(i)));
        float vz = -(0.09f * (float) Math.cos(Math.toRadians(i)));
        float tx = (float) posX + 2.5f * (float) Math.sin(Math.toRadians(i));
        float ty = (float) posY;
        float tz = (float) posZ + 2.5f * (float) Math.cos(Math.toRadians(i));
        ParticleUtil.spawnParticleSmoke(world, tx, ty, tz, vx, 0, vz, 120, 255, 232, 0.055f, 5.0f, 95, true);
      }
    }
    if (!world.isRemote) {
      if (this.ticksExisted % ritual.interval == 0) {
        AxisAlignedBB bounds = bounding.offset(getPosition());
        BlockPos start = new BlockPos(bounds.minX, bounds.minY, bounds.minZ);
        BlockPos stop = new BlockPos(bounds.maxX, bounds.maxY, bounds.maxZ);
        BlockPos down = getPosition().down();
        for (BlockPos.MutableBlockPos pos : BlockPos.getAllInBoxMutable(start, stop)) {
          if (pos.equals(down)) {
            continue;
          }
          TileEntity te = world.getTileEntity(pos);
          if (te != null && te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
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
  }
}