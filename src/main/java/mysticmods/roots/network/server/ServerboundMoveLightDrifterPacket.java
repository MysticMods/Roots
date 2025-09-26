package mysticmods.roots.network.server;

import io.netty.buffer.ByteBuf;
import mysticmods.roots.api.ExtraStreamCodecs;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.network.IRootsPacket;
import mysticmods.roots.entity.other.LightDrifterEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public abstract class ServerboundMoveLightDrifterPacket implements IRootsPacket {
  protected final int entityId;
  protected final double x;
  protected final double y;
  protected final double z;
  protected final float yRot;
  protected final float xRot;
  protected final boolean onGround;
  protected final boolean hasPos;
  protected final boolean hasRot;

  protected ServerboundMoveLightDrifterPacket(int entityId, double x, double y, double z, float yRot, float xRot, boolean onGround, boolean hasPos, boolean hasRot) {
    this.entityId = entityId;
    this.x = x;
    this.y = y;
    this.z = z;
    this.yRot = yRot;
    this.xRot = xRot;
    this.onGround = onGround;
    this.hasPos = hasPos;
    this.hasRot = hasRot;
  }

  public double getX(double defaultValue) {
    return this.hasPos ? this.x : defaultValue;
  }

  public double getY(double defaultValue) {
    return this.hasPos ? this.y : defaultValue;
  }

  public double getZ(double defaultValue) {
    return this.hasPos ? this.z : defaultValue;
  }

  public float getYRot(float defaultValue) {
    return this.hasRot ? this.yRot : defaultValue;
  }

  public float getXRot(float defaultValue) {
    return this.hasRot ? this.xRot : defaultValue;
  }

  public boolean isOnGround() {
    return this.onGround;
  }

  @Override
  public void handle(IPayloadContext context) {
    Entity e = context.player().level().getEntity(this.entityId);
    if (e instanceof LightDrifterEntity drifter) {
      drifter.handleMovePlayer(this);
    }
  }

  public static class Pos extends ServerboundMoveLightDrifterPacket {
    public static final StreamCodec<FriendlyByteBuf, Pos> CODEC = StreamCodec.composite(
        ByteBufCodecs.VAR_INT, o -> o.entityId,
        ByteBufCodecs.DOUBLE, o -> o.x,
        ByteBufCodecs.DOUBLE, o -> o.y,
        ByteBufCodecs.DOUBLE, o -> o.z,
        ByteBufCodecs.BOOL, o -> o.onGround,
        Pos::new
    );
    public static final Type<Pos> TYPE = new Type<>(RootsAPI.rl("server_bound_move_light_drifter_pos_packet"));

    public Pos(int entityId, double x, double y, double z, boolean onGround) {
      super(entityId, x, y, z, 0.0F, 0.0F, onGround, true, false);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
      return TYPE;
    }
  }

  public static class PosRot extends ServerboundMoveLightDrifterPacket {
    public static final StreamCodec<ByteBuf, PosRot> CODEC = ExtraStreamCodecs.composite(
        ByteBufCodecs.VAR_INT, o -> o.entityId,
        ByteBufCodecs.DOUBLE, o -> o.x,
        ByteBufCodecs.DOUBLE, o -> o.y,
        ByteBufCodecs.DOUBLE, o -> o.z,
        ByteBufCodecs.FLOAT, o -> o.yRot,
        ByteBufCodecs.FLOAT, o -> o.xRot,
        ByteBufCodecs.BOOL, o -> o.onGround,
        PosRot::new
    );
    public static final Type<PosRot> TYPE = new Type<>(RootsAPI.rl("server_bound_move_light_drifter_pos_rot_packet"));

    public PosRot(int entityId, double x, double y, double z, float yRot, float xRot, boolean onGround) {
      super(entityId, x, y, z, yRot, xRot, onGround, true, true);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
      return TYPE;
    }
  }

  public static class Rot extends ServerboundMoveLightDrifterPacket {
    public static final StreamCodec<ByteBuf, Rot> CODEC = StreamCodec.composite(
        ByteBufCodecs.VAR_INT, o -> o.entityId,
        ByteBufCodecs.FLOAT, o -> o.yRot,
        ByteBufCodecs.FLOAT, o -> o.xRot,
        ByteBufCodecs.BOOL, o -> o.onGround,
        Rot::new
    );
    public static final Type<Rot> TYPE = new Type<>(RootsAPI.rl("server_bound_move_light_drifter_rot_packet"));

    public Rot(int entityId, float yRot, float xRot, boolean onGround) {
      super(entityId, 0.0, 0.0, 0.0, yRot, xRot, onGround, false, true);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
      return TYPE;
    }
  }
}
