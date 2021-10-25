package epicsquid.mysticallib.model.parts;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.util.EnumFacing;

public class Segment {

  public Map<EnumFacing, BakedQuad> quads = new EnumMap<>(EnumFacing.class);

  public Segment(@Nonnull BakedQuad west, @Nonnull BakedQuad east, @Nonnull BakedQuad down, @Nonnull BakedQuad up, @Nonnull BakedQuad north,
      @Nonnull BakedQuad south, boolean[] cull) {
    if (cull[0])
      quads.put(EnumFacing.WEST, west);
    if (cull[1])
      quads.put(EnumFacing.EAST, east);
    if (cull[2])
      quads.put(EnumFacing.DOWN, down);
    if (cull[3])
      quads.put(EnumFacing.UP, up);
    if (cull[4])
      quads.put(EnumFacing.NORTH, north);
    if (cull[5])
      quads.put(EnumFacing.SOUTH, south);
  }

  public void addToList(@Nonnull List<BakedQuad> list, @Nullable EnumFacing face) {
    if (face != null && quads.containsKey(face)) {
      list.add(quads.get(face));
    }
  }
}
