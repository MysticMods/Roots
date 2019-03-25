package epicsquid.roots.entity.grove;

import java.util.ArrayList;
import java.util.List;

import epicsquid.roots.capability.grove.IPlayerGroveCapability;
import epicsquid.roots.capability.grove.PlayerGroveCapabilityProvider;
import epicsquid.roots.grove.GroveType;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.tileentity.TileEntityOffertoryPlate;
import epicsquid.roots.util.OfferingUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class EntityGrove extends Entity {

  private float r, g, b;
  private GroveType type;

  private List<TileEntityOffertoryPlate> offertoryPlateList = new ArrayList<>();

  public EntityGrove(World worldIn) {
    super(worldIn);
    this.r = 255;
    this.b = 255;
    this.g = 255;
    this.type = GroveType.NATURAL;
  }

  public EntityGrove(World worldIn, float r, float g, float b, GroveType type) {
    super(worldIn);
    this.r = r;
    this.b = b;
    this.g = g;
    this.type = type;
  }

  public void addActiveOffering(TileEntityOffertoryPlate plate) {
    if (!offertoryPlateList.contains(plate)) {
      offertoryPlateList.add(plate);
    }
  }

  @Override
  protected void entityInit() {

  }

  @Override
  protected void readEntityFromNBT(NBTTagCompound compound) {

  }

  @Override
  protected void writeEntityToNBT(NBTTagCompound compound) {

  }

  @Override
  public void onUpdate() {
    super.onUpdate();
    if (world.isRemote) {
      ParticleUtil.spawnParticleGlow(world, (float) posX, (float) posY, (float) posZ, 0, 0, 0, r, g, b, 0.2f, 20.0f, 40);

    }

    if (this.ticksExisted % 10 == 0) {
      if (world.isRemote) {
        for (TileEntityOffertoryPlate tile : this.offertoryPlateList) {
          ParticleUtil
              .spawnParticleLineGlowSteady(world, tile.getPos().getX() + 0.5f, tile.getPos().getY() + 0.5f, tile.getPos().getZ() + 0.5f, (float) this.posX,
                  (float) this.posY, (float) this.posZ, r, g, b, 1, 5, 100);
        }
      }
      List<TileEntityOffertoryPlate> toRemove = new ArrayList<>();
      for (TileEntityOffertoryPlate tile : this.offertoryPlateList) {
        ItemStack stack = tile.getHeldItem();
        if (stack.isEmpty()) {
          toRemove.add(tile);
          continue;
        }

        float itemValue = OfferingUtil.getValue(stack);
        tile.removeItem();
        if (!world.isRemote) {
          EntityPlayer player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(tile.getLastPlayer());
          IPlayerGroveCapability capability = player.getCapability(PlayerGroveCapabilityProvider.PLAYER_GROVE_CAPABILITY, null);
          capability.addTrust(this.type, itemValue);
        }
      }
      for (TileEntityOffertoryPlate tile2 : toRemove) {
        this.offertoryPlateList.remove(tile2);
      }
    }
  }

  public GroveType getType() {
    return type;
  }
}
