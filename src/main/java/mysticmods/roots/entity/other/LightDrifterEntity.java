package mysticmods.roots.entity.other;

import com.google.common.primitives.Floats;
import mysticmods.roots.init.ModEffects;
import mysticmods.roots.init.ModSerializers;
import mysticmods.roots.network.client.ClientboundLightDrifterSyncPacket;
import mysticmods.roots.network.server.ServerboundMoveLightDrifterPacket;
import mysticmods.roots.snapshot.LightDrifterSnapshot;
import mysticmods.roots.snapshot.SnapshotHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class LightDrifterEntity extends LivingEntity implements TraceableEntity {
  protected static final EntityDataAccessor<Optional<UUID>> OWNER = SynchedEntityData.defineId(LightDrifterEntity.class, EntityDataSerializers.OPTIONAL_UUID);

  @Nullable
  private UUID ownerUUID;
  @Nullable
  private Entity cachedOwner;

  // These values are only used on the client.
  public double xLast;
  public double yLast1;
  public double zLast;
  public float yRotLast;
  public float xRotLast;
  public int positionReminder;
  public float yBob;
  public float xBob;
  public float yBobO;
  public float xBobO;

  // These values are only used on the server.
  private double firstGoodX;
  private double firstGoodY;
  private double firstGoodZ;
  private double lastGoodX;
  private double lastGoodY;
  private double lastGoodZ;

  private int moveTickCount = 0;

  public Vec3 lastKnownClientMovement;

  public LightDrifterEntity(EntityType<LightDrifterEntity> type, Level level, Player owner) {
    this(type, level);
    setOwner(owner);
  }

  public LightDrifterEntity(EntityType<LightDrifterEntity> entityEntityType, Level level) {
    super(entityEntityType, level);
    this.noPhysics = true;
    this.setNoGravity(true);
  }

  @Override
  public boolean onGround() {
    return false;
  }

  @Override
  public void travel(Vec3 travelVector) {
    double d2 = this.getDeltaMovement().y;
    super.travel(travelVector);
    Vec3 vec31 = this.getDeltaMovement();
    this.setDeltaMovement(vec31.x, d2 * 0.6, vec31.z);
    this.resetFallDistance();
  }

  @Override
  public void move(MoverType type, Vec3 pos) {
    // On the client, prevent moving further than `maxDistance` away from the player
    if (this.level().isClientSide()) {
      LightDrifterSnapshot snapshot = SnapshotHelper.getSnapshot(this, ModSerializers.LIGHT_DRIFTER.get());
      if (snapshot == null) {
        return;
      }

      Player player = this.level().getPlayerByUUID(snapshot.getPlayer());
      if (player == null) {
        return;
      }

      Vec3 current = player.position();
      Vec3 newCurrent = position().add(pos);
      if (current.distanceToSqr(newCurrent) >= snapshot.getMaxDistance()) {
        return;
      }
    }

    super.move(type, pos);
  }

  // Server
  @Override
  public Vec3 getKnownMovement() {
    return this.lastKnownClientMovement;
  }

  @Override
  public boolean isControlledByLocalInstance() {
    Entity owner = getOwner();
    if (owner instanceof Player player) {
      return player.isLocalPlayer();
    }
    return isEffectiveAi();
  }

  @Override
  public boolean isEffectiveAi() {
    Entity owner = getOwner();
    if (owner instanceof Player player && player.isLocalPlayer()) {
      return true;
    }

    return !level().isClientSide();
  }

  @Override
  public void baseTick() {
    super.baseTick();
    if (!this.level().isClientSide() && !this.isRemoved()) {
      Entity owner = getOwner();
      if (owner instanceof Player player) {
        if (!player.hasEffect(ModEffects.LIGHT_DRIFTER)) {
          this.remove(RemovalReason.DISCARDED);
          return;
        }
        PacketDistributor.sendToPlayer((ServerPlayer) player, new ClientboundLightDrifterSyncPacket(this.getId()));
        ((ServerPlayer) player).setCamera(null);
      } else {
        this.remove(RemovalReason.DISCARDED);
      }
    }
  }

  @Override
  public void tick() {
    if (this.level().hasChunkAt(this.getBlockX(), this.getBlockZ())) {
      super.tick();
      if (!this.level().isClientSide() && !this.isRemoved()) {
        this.resetPosition();
        this.xo = this.getX();
        this.yo = this.getY();
        this.zo = this.getZ();
        this.absMoveTo(this.firstGoodX, this.firstGoodY, this.firstGoodZ, this.getYRot(), this.getXRot());
        this.moveTickCount++;
      }
    }
  }

  @Override
  public HumanoidArm getMainArm() {
    return HumanoidArm.LEFT;
  }

  public void handleMovePlayer(ServerboundMoveLightDrifterPacket packet) {
    if (this.isRemoved()) {
      return;
    }
    LightDrifterSnapshot snapshot = SnapshotHelper.getSnapshot(this, ModSerializers.LIGHT_DRIFTER.get());
    if (snapshot == null) {
      return;
    }
    if (containsInvalidValues(packet.getX(0.0), packet.getY(0.0), packet.getZ(0.0), packet.getYRot(0.0F), packet.getXRot(0.0F))) {
      return;
    } else {
      if (this.moveTickCount == 0) {
        this.resetPosition();
      }

      Player player = level().getPlayerByUUID(snapshot.getPlayer());
      if (player == null) {
        return;
      }

      double d0 = clampHorizontal(packet.getX(this.getX()));
      double d1 = clampVertical(packet.getY(this.getY()));
      double d2 = clampHorizontal(packet.getZ(this.getZ()));
      float f = Mth.wrapDegrees(packet.getYRot(this.getYRot()));
      float f1 = Mth.wrapDegrees(packet.getXRot(this.getXRot()));
      double d3 = this.getX();
      double d4 = this.getY();
      double d5 = this.getZ();
      if (player.distanceToSqr(d0, d1, d2) > snapshot.getMaxDistance()) {
        this.absMoveTo(this.firstGoodX, this.firstGoodY, this.firstGoodZ, f, f1);
        return;
      }

      double d6 = d0 - this.lastGoodX;
      double d7 = d1 - this.lastGoodY;
      double d8 = d2 - this.lastGoodZ;
      this.move(MoverType.PLAYER, new Vec3(d6, d7, d8));

      this.absMoveTo(d0, d1, d2, f, f1);
      // We need to set a maximum distance that the drifter can travel from the player before being reset
      Vec3 vec3 = new Vec3(this.getX() - d3, this.getY() - d4, this.getZ() - d5);
      this.setOnGroundWithMovement(packet.isOnGround(), vec3);
      this.setKnownMovement(vec3);
      this.lastGoodX = this.getX();
      this.lastGoodY = this.getY();
      this.lastGoodZ = this.getZ();
      this.hasImpulse = true;
    }
  }

  private void resetPosition() {
    this.firstGoodX = this.getX();
    this.firstGoodY = this.getY();
    this.firstGoodZ = this.getZ();
    this.lastGoodX = this.getX();
    this.lastGoodY = this.getY();
    this.lastGoodZ = this.getZ();
  }

  // Server
  public void setKnownMovement(Vec3 knownMovement) {
    this.lastKnownClientMovement = knownMovement;
  }

  @Override
  protected void defineSynchedData(SynchedEntityData.Builder builder) {
    super.defineSynchedData(builder);
    builder.define(OWNER, Optional.empty());
  }

  @Override
  public void addAdditionalSaveData(CompoundTag compound) {
    if (this.ownerUUID != null) {
      compound.putUUID("Owner", this.ownerUUID);
    }
  }

  @Override
  public void readAdditionalSaveData(CompoundTag compound) {
    if (compound.hasUUID("Owner")) {
      this.ownerUUID = compound.getUUID("Owner");
      this.cachedOwner = null;
    }
  }

  @Override
  public Iterable<ItemStack> getArmorSlots() {
    return List.of();
  }

  @Override
  public ItemStack getItemBySlot(EquipmentSlot slot) {
    return ItemStack.EMPTY;
  }

  @Override
  public void setItemSlot(EquipmentSlot slot, ItemStack stack) {

  }

  @Override
  public void restoreFrom(Entity entity) {
    super.restoreFrom(entity);
    if (entity instanceof LightDrifterEntity projectile) {
      this.cachedOwner = projectile.cachedOwner;
    }
  }

  public void setOwner(@Nullable Entity owner) {
    if (owner != null) {
      this.entityData.set(OWNER, Optional.of(owner.getUUID()));
      this.ownerUUID = owner.getUUID();
      this.cachedOwner = owner;
    }
  }

  @Nullable
  public Entity getOwner() {
    Optional<UUID> opt = this.entityData.get(OWNER);
    if (this.cachedOwner != null && !this.cachedOwner.isRemoved()) {
      return this.cachedOwner;
    } else if (this.ownerUUID != null && this.level() instanceof ServerLevel serverlevel) {
      this.cachedOwner = serverlevel.getEntity(this.ownerUUID);
      return this.cachedOwner;
    } else if (opt.isPresent()) {
      this.cachedOwner = level().getPlayerByUUID(opt.get());
      return this.cachedOwner;
    } else {
      return null;
    }
  }

  @Override
  public float getViewXRot(float partialTick) {
    return super.getViewXRot(partialTick);
  }

  @Override
  public float getViewYRot(float partialTick) {
    return partialTick == 1.0F ? this.getYRot() : Mth.lerp(partialTick, this.yRotO, this.getYRot());
  }

  @Override
  public boolean onClimbable() {
    return false;
  }

  private static boolean containsInvalidValues(double x, double y, double z, float yRot, float xRot) {
    return Double.isNaN(x) || Double.isNaN(y) || Double.isNaN(z) || !Floats.isFinite(xRot) || !Floats.isFinite(yRot);
  }

  private static double clampHorizontal(double value) {
    return Mth.clamp(value, -3.0E7, 3.0E7);
  }

  private static double clampVertical(double value) {
    return Mth.clamp(value, -2.0E7, 2.0E7);
  }
}
