package epicsquid.mysticallib.model.parts;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.util.EnumFacing;

public class Cube {

  public Map<EnumFacing, BakedQuad> quads = new EnumMap<EnumFacing, BakedQuad>(EnumFacing.class);
  public List<BakedQuad> unculledQuads = new ArrayList<>();

  public Cube(@Nonnull BakedQuad west, @Nonnull BakedQuad east, @Nonnull BakedQuad down, @Nonnull BakedQuad up, @Nonnull BakedQuad north,
      @Nonnull BakedQuad south) {
    quads.put(EnumFacing.WEST, west);
    quads.put(EnumFacing.EAST, east);
    quads.put(EnumFacing.DOWN, down);
    quads.put(EnumFacing.UP, up);
    quads.put(EnumFacing.NORTH, north);
    quads.put(EnumFacing.SOUTH, south);
  }

  public void addToList(@Nonnull List<BakedQuad> list, @Nullable EnumFacing face) {
    if (face != null && quads.containsKey(face)) {
      list.add(quads.get(face));
    } else if (face == null) {
      list.addAll(unculledQuads);
    }
  }

  public Cube setNoCull() {
    unculledQuads.addAll(quads.values());
    quads.clear();
    return this;
  }

  public Cube setNoCull(@Nonnull EnumFacing face) {
    BakedQuad quad = quads.get(face);
    unculledQuads.add(quad);
    quads.remove(face);
    return this;
  }
}
