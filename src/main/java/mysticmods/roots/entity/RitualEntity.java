package mysticmods.roots.entity;

import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.init.ModRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

// TODO: lifetime
public class RitualEntity extends Entity {
  private static final EntityDataAccessor<Integer> LIFETIME = SynchedEntityData.defineId(RitualEntity.class, EntityDataSerializers.INT);

  private Ritual ritual;

  public RitualEntity(EntityType<?> pEntityType, Level pLevel) {
    super(pEntityType, pLevel);
  }

  @Override
  public void tick() {
    super.tick();
    int lifetime = this.entityData.get(LIFETIME);
    if (lifetime < 0) {
      this.remove(RemovalReason.DISCARDED);
    } else {
      this.entityData.set(LIFETIME, lifetime - 1);
      if (ritual != null) {
        ritual.ritualTick(this);
        ritual.animateTick(this);
      }
    }
  }


  @Override
  protected void defineSynchedData() {
    this.entityData.define(LIFETIME, 0);
  }

  @Override
  protected void readAdditionalSaveData(CompoundTag pCompound) {
    if (pCompound.contains("ritual")) {
      ResourceLocation rl = new ResourceLocation(pCompound.getString("ritual"));
      this.ritual = ModRegistries.RITUAL_REGISTRY.get().getValue(rl);
      if (this.ritual == null) {
        // TODO
      }
    }
    this.entityData.set(LIFETIME, pCompound.getInt("lifetime"));
  }

  @Override
  protected void addAdditionalSaveData(CompoundTag pCompound) {
    if (ritual != null) {
      pCompound.putString("ritual", ModRegistries.RITUAL_REGISTRY.get().getKey(this.ritual).toString());
    }
    pCompound.putInt("lifetime", this.entityData.get(LIFETIME));
  }

  @Override
  public Packet<?> getAddEntityPacket() {
    return NetworkHooks.getEntitySpawningPacket(this);
  }
}
