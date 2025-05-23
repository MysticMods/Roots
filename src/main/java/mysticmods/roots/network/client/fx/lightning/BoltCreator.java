package mysticmods.roots.network.client.fx.lightning;

import mysticmods.roots.client.particle.bolt.*;
import net.minecraft.world.phys.Vec3;

@FunctionalInterface
public interface BoltCreator {
  IBoltEffect create(PositionProvider provider, int segments);

  static IBoltEffect createBolt(BoltRenderInfo info, PositionProvider provider, int segments) {
    if (provider.isDynamic()) {
      return new DynamicBoltEffect(info, provider, segments);
    } else {
      return new BoltEffect(info, provider.getStart(), provider.getStop(), segments);
    }
  }
}
