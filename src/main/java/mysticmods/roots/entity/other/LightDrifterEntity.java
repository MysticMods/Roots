package mysticmods.roots.entity.other;

import com.google.common.primitives.Floats;
import mysticmods.roots.init.ModEffects;
import mysticmods.roots.network.server.ServerboundMoveLightDrifterPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.UUID;

public class LightDrifterEntity extends Entity implements TraceableEntity {
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
  public float xxa;
  public float zza;

  // These values are only used on the server.
  private double firstGoodX;
  private double firstGoodY;
  private double firstGoodZ;
  private double lastGoodX;
  private double lastGoodY;
  private double lastGoodZ;

  private int moveTickCount = 0;

  public Vec3 lastKnownClientMovement;

  // TODO: Setting the owner

  public LightDrifterEntity(EntityType<?> entityType, Level level) {
    super(entityType, level);
    this.noPhysics = true;
    this.setNoGravity(true);
  }

  @Override
  public boolean onGround() {
    return false;
  }

  // Server
  @Override
  public Vec3 getKnownMovement() {
    return this.lastKnownClientMovement;
  }

  @Override
  public void baseTick() {
    super.baseTick();
    if (!this.level().isClientSide() && !this.isRemoved()) {
      Entity owner = getOwner();
      if (owner instanceof Player player) {
        if (!player.hasEffect(ModEffects.LIGHT_DRIFTER)) {
          this.remove(RemovalReason.DISCARDED);
        }
      } else {
        this.remove(RemovalReason.DISCARDED);
      }
    }
  }

  @Override
  public void tick() {
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

  public void handleMovePlayer(ServerboundMoveLightDrifterPacket packet) {
    if (this.isRemoved()) {
      return;
    }
    if (containsInvalidValues(packet.getX(0.0), packet.getY(0.0), packet.getZ(0.0), packet.getYRot(0.0F), packet.getXRot(0.0F))) {
      return;
    } else {
      if (this.moveTickCount == 0) {
        this.resetPosition();
      }

      double d0 = clampHorizontal(packet.getX(this.getX()));
      double d1 = clampVertical(packet.getY(this.getY()));
      double d2 = clampHorizontal(packet.getZ(this.getZ()));
      float f = Mth.wrapDegrees(packet.getYRot(this.getYRot()));
      float f1 = Mth.wrapDegrees(packet.getXRot(this.getXRot()));
      double d3 = this.getX();
      double d4 = this.getY();
      double d5 = this.getZ();
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

  }

  @Override
  protected void addAdditionalSaveData(CompoundTag compound) {
    if (this.ownerUUID != null) {
      compound.putUUID("Owner", this.ownerUUID);
    }
  }

  @Override
  protected void readAdditionalSaveData(CompoundTag compound) {
    if (compound.hasUUID("Owner")) {
      this.ownerUUID = compound.getUUID("Owner");
      this.cachedOwner = null;
    }
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
      this.ownerUUID = owner.getUUID();
      this.cachedOwner = owner;
    }
  }

  @Nullable
  public Entity getOwner() {
    if (this.cachedOwner != null && !this.cachedOwner.isRemoved()) {
      return this.cachedOwner;
    } else if (this.ownerUUID != null && this.level() instanceof ServerLevel serverlevel) {
      this.cachedOwner = serverlevel.getEntity(this.ownerUUID);
      return this.cachedOwner;
    } else {
      return null;
    }
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
