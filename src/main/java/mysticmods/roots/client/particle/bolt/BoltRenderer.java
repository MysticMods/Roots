package mysticmods.roots.client.particle.bolt;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

import mysticmods.roots.client.RootsRenderTypes;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

// TODO:
// - Add a way to specify that bolts should be unique, this means PositionProvider has to implement some equality system:
//   - For static Vec3s, just compare
//   - For enitites, compare entity id
// - Bezier smoothing
// - Textures?
public class BoltRenderer {

  /** Amount of times per tick we refresh. 3 implies 60 Hz. */
  private static final float REFRESH_TIME = 3F;
  /** We will keep track of an owner's render data for 5 seconds after there are no bolts remaining. */
  private static final double MAX_OWNER_TRACK_TIME = 5 * SharedConstants.TICKS_PER_SECOND;

  private Timestamp refreshTimestamp = new Timestamp();

  private final RandomSource random = RandomSource.create();
  private final Minecraft minecraft = Minecraft.getInstance();

  private final Map<Object, BoltOwnerData> boltOwners = new Object2ObjectOpenHashMap<>();

  public boolean hasBoltsToRender() {
    synchronized (boltOwners) {
      return boltOwners.values().stream().anyMatch(data -> !data.bolts.isEmpty());
    }
  }

  public void render(float partialTicks, PoseStack matrixStack, MultiBufferSource bufferIn) {
    render(partialTicks, matrixStack, bufferIn, null);
  }

  public void render(float partialTicks, PoseStack matrixStack, MultiBufferSource bufferIn, @Nullable Vec3 cameraPos) {
    VertexConsumer buffer = bufferIn.getBuffer(RootsRenderTypes.ROOTS_LIGHTNING);
    Matrix4f matrix = matrixStack.last().pose();
    Timestamp timestamp = new Timestamp(minecraft.level.getGameTime(), partialTicks);
    boolean refresh = timestamp.isPassed(refreshTimestamp, (1 / REFRESH_TIME));
    if (refresh) {
      refreshTimestamp = timestamp;
    }
    synchronized (boltOwners) {
      for (Iterator<Map.Entry<Object, BoltOwnerData>> iter = boltOwners.entrySet().iterator(); iter.hasNext(); ) {
        Map.Entry<Object, BoltOwnerData> entry = iter.next();
        BoltOwnerData data = entry.getValue();
        // tick our bolts based on the refresh rate, removing if they're now finished
        if (refresh) {
          tickAndRemove(data, timestamp);
        }
        // TODO: This doesn't support dynamic bolts
        if (data.bolts.isEmpty() && data.lastBolt != null && data.lastBolt.getSpawnFunction().isConsecutive()) {
          data.addBolt(new StaticBoltInstance(data.lastBolt, timestamp), timestamp, random);
        }
        for (BoltRenderInstance bolt : data.bolts) {
          bolt.render(matrix, buffer, timestamp, cameraPos, partialTicks);
        }

        if (data.bolts.isEmpty() && timestamp.isPassed(data.lastUpdateTimestamp, MAX_OWNER_TRACK_TIME)) {
          iter.remove();
        }
      }
    }
  }

  private static void tickAndRemove(BoltOwnerData data, Timestamp timestamp) {
    Iterator<BoltRenderInstance> iterator = data.bolts.iterator();
    //noinspection Java8CollectionRemoveIf: requires capture
    while (iterator.hasNext()) {
      if (iterator.next().tick(timestamp)) {
        iterator.remove();
      }
    }
  }

  public void update(Object owner, IBoltEffect newBoltData, float partialTicks) {
    if (minecraft.level == null) {
      return;
    }
    synchronized (boltOwners) {
      BoltOwnerData data = boltOwners.computeIfAbsent(owner, o -> new BoltOwnerData());
      data.lastBolt = newBoltData;
      Timestamp timestamp = new Timestamp(minecraft.level.getGameTime(), partialTicks);
      if ((!data.lastBolt.getSpawnFunction().isConsecutive() || data.bolts.isEmpty()) && timestamp.isPassed(data.lastBoltTimestamp, data.lastBoltDelay)) {
        // TODO: `IBoltEffect` should provide the constructor
        if (newBoltData instanceof DynamicBoltEffect) {
          data.addBolt(new DynamicBoltInstance((DynamicBoltEffect) newBoltData, timestamp), timestamp, random);
        } else {
          data.addBolt(new StaticBoltInstance(newBoltData, timestamp), timestamp, random);
        }
      }
      data.lastUpdateTimestamp = timestamp;
    }
  }

  public static class BoltOwnerData {

    private final Set<BoltRenderInstance> bolts = new ObjectOpenHashSet<>();
    private IBoltEffect lastBolt;
    private Timestamp lastBoltTimestamp = new Timestamp();
    private Timestamp lastUpdateTimestamp = new Timestamp();
    private BiFunction<IBoltEffect, Timestamp, BoltRenderInstance> builder;
    private double lastBoltDelay;

    private void addBolt(BoltRenderInstance instance, Timestamp timestamp, RandomSource random) {
      bolts.add(instance);
      lastBoltDelay = instance.getSpawnFunction().getSpawnDelay(random);
      lastBoltTimestamp = timestamp;
    }
  }

  public static class StaticBoltInstance implements BoltRenderInstance {
    private final IBoltEffect bolt;
    private final List<BoltQuads> renderQuads;
    private final Timestamp createdTimestamp;

    public StaticBoltInstance(IBoltEffect bolt, Timestamp timestamp) {
      this.bolt = bolt;
      this.renderQuads = bolt.generate(timestamp.partial());
      this.createdTimestamp = timestamp;
    }

    @Override
    public void render(Matrix4f matrix, VertexConsumer buffer, Timestamp timestamp, @Nullable Vec3 cameraPos, float partialTicks) {
      float lifeScale = timestamp.subtract(createdTimestamp).value() / bolt.getLifespan();
      FadeFunction.RenderBounds bounds = bolt.getFadeFunction().getRenderBounds(renderQuads.size(), lifeScale);
      for (int i = bounds.start(); i < bounds.end(); i++) {
        for (Vec3 v : renderQuads.get(i).getVecs()) {
          Vec3 shiftedVertex = cameraPos == null ? v : v.subtract(cameraPos);
          buffer.addVertex(matrix, (float) shiftedVertex.x, (float) shiftedVertex.y, (float) shiftedVertex.z)
              .setColor(bolt.getColor().r(), bolt.getColor().g(), bolt.getColor().b(), bolt.getColor().a());
        }
      }
    }

    @Override
    public boolean tick(Timestamp timestamp) {
      return timestamp.isPassed(createdTimestamp, bolt.getLifespan());
    }

    @Override
    public IBoltEffect getBolt() {
      return bolt;
    }
  }

  public static class DynamicBoltInstance implements BoltRenderInstance {
    private final DynamicBoltEffect bolt;
    private final Timestamp createdTimestamp;

    public DynamicBoltInstance(DynamicBoltEffect bolt, Timestamp timestamp) {
      this.bolt = bolt;
      this.createdTimestamp = timestamp;
    }

    @Override
    public void render(Matrix4f matrix, VertexConsumer buffer, Timestamp timestamp, @Nullable Vec3 cameraPos, float partialTicks) {
      float lifeScale = timestamp.subtract(createdTimestamp).value() / bolt.getLifespan();
      List<BoltQuads> quads = bolt.generate(partialTicks);
      FadeFunction.RenderBounds bounds = bolt.getFadeFunction().getRenderBounds(quads.size(), lifeScale);
      for (int i = bounds.start(); i < bounds.end(); i++) {
        for (Vec3 v : quads.get(i).getVecs()) {
          Vec3 shiftedVertex = cameraPos == null ? v : v.subtract(cameraPos);
          buffer.addVertex(matrix, (float) shiftedVertex.x, (float) shiftedVertex.y, (float) shiftedVertex.z)
              .setColor(bolt.getColor().r(), bolt.getColor().g(), bolt.getColor().b(), bolt.getColor().a());
        }
      }
    }

    @Override
    public boolean tick(Timestamp timestamp) {
      return timestamp.isPassed(createdTimestamp, bolt.getLifespan());
    }

    @Override
    public IBoltEffect getBolt() {
      return bolt;
    }
  }

}
