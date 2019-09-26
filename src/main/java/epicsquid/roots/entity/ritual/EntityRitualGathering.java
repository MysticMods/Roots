package epicsquid.roots.entity.ritual;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.mechanics.Magnetize;
import epicsquid.roots.network.fx.MessageItemGatheredFX;
import epicsquid.roots.network.fx.MessageOvergrowthEffectFX;
import epicsquid.roots.ritual.RitualGathering;
import epicsquid.roots.ritual.RitualRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class EntityRitualGathering extends EntityRitualBase {
  private RitualGathering ritual;

  public EntityRitualGathering(World worldIn) {
    super(worldIn);
    this.getDataManager().register(lifetime, RitualRegistry.ritual_gathering.getDuration() + 20);
    ritual = (RitualGathering) RitualRegistry.ritual_gathering;
  }

  @Override
  public void onUpdate() {
    super.onUpdate();

    if (!world.isRemote) {
      if (this.ticksExisted % ritual.interval == 0) {
        TileEntity te = world.getTileEntity(getPosition().down()); // Hope that it's in the right position these days
        IItemHandler cap = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if (cap != null) {
          List<BlockPos> transferredItems = Magnetize.store(world, getPosition(), cap, ritual.radius_x, ritual.radius_y, ritual.radius_z);
          if (!transferredItems.isEmpty()) {
            PacketHandler.sendToAllTracking(new MessageItemGatheredFX(transferredItems), this);
          }
        }
      }
    }
  }
}