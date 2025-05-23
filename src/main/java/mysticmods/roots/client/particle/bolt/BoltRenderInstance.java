package mysticmods.roots.client.particle.bolt;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

public interface BoltRenderInstance {
  void render(Matrix4f matrix, VertexConsumer buffer, Timestamp timestamp, @Nullable Vec3 cameraPos, float partialTicks);

  boolean tick(Timestamp timestamp);

  IBoltEffect getBolt ();

  default SpawnFunction getSpawnFunction() {
    return getBolt().getSpawnFunction();
  }
}
