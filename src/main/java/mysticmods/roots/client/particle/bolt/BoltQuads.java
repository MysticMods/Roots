package mysticmods.roots.client.particle.bolt;

import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BoltQuads {

  private final List<Vec3> vecs = new ArrayList<>();

  protected void addQuad(Vec3... quadVecs) {
    Collections.addAll(vecs, quadVecs);
  }

  public List<Vec3> getVecs() {
    return vecs;
  }
}
