package epicsquid.roots.entity.spell;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellDrizzle;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.FarmlandWaterManager;
import net.minecraftforge.common.ticket.AABBTicket;

public class EntitySpellDrizzle extends EntitySpellBase<SpellDrizzle> {
  public static AxisAlignedBB BOUNDING_BOX = null;

  private AABBTicket ticket = null;

  public EntitySpellDrizzle(World worldIn) {
    super(worldIn);
    this.instance = SpellDrizzle.instance;
    getDataManager().register(lifetime, instance.duration + 20);

    if (BOUNDING_BOX == null) {
      BOUNDING_BOX = new AxisAlignedBB(-instance.radius, -3, -instance.radius, instance.radius, 3, instance.radius);
    }
  }

  @Override
  public void onAddedToWorld() {
    super.onAddedToWorld();

    if (world != null && !world.isRemote) {
      if (ticket != null) {
        ticket.validate();
      } else {
        ticket = FarmlandWaterManager.addAABBTicket(world, BOUNDING_BOX.offset(getPosition()));
      }
    }
  }

  @Override
  public void onRemovedFromWorld() {
    if (world != null && !world.isRemote) {
      if (ticket != null) {
        ticket.invalidate();
      }
    }

    super.onRemovedFromWorld();
  }

  @Override
  public void onUpdate() {
    super.onUpdate();

    if (world != null && !world.isRemote) {
      if (ticket == null) { // Don't validate, just enable it if it doesns't exist
        ticket = FarmlandWaterManager.addAABBTicket(world, BOUNDING_BOX.offset(getPosition()));
      }
    }

    if (world != null && world.isRemote) {
      int countdown = getDataManager().get(lifetime);
      float alpha = (float) Math.min(20, countdown) / 40.0f;
      for (int j = 0; j < SpellDrizzle.instance.radius; j++) {
        for (float i = 0; i < 360; i += Util.rand.nextFloat() * 90.0f) {
          float tx = (float) posX + (j + 0.5f) * (float) Math.sin(Math.toRadians(i));
          float ty = (float) posY + 2.5f;
          float tz = (float) posZ + (j + 0.5f) * (float) Math.cos(Math.toRadians(i));
          ParticleUtil.spawnParticleSmoke(world, tx, ty, tz, 0, 0, 0, 35, 35, 90, 0.45f * alpha, 14.0f, 80, false);
        }
      }
    }
  }
}
