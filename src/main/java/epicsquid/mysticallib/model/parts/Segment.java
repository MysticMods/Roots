package epicsquid.mysticallib.model.parts;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.util.Direction;

public class Segment {

  public Map<Direction, BakedQuad> quads = new EnumMap<>(Direction.class);

  public Segment(@Nonnull BakedQuad west, @Nonnull BakedQuad east, @Nonnull net.minecraft.client.renderer.model.BakedQuad down, @Nonnull BakedQuad up, @Nonnull BakedQuad north,
                 @Nonnull BakedQuad south, boolean[] cull) {
    if (cull[0])
      quads.put(Direction.WEST, west);
    if (cull[1])
      quads.put(Direction.EAST, east);
    if (cull[2])
      quads.put(Direction.DOWN, down);
    if (cull[3])
      quads.put(Direction.UP, up);
    if (cull[4])
      quads.put(Direction.NORTH, north);
    if (cull[5])
      quads.put(Direction.SOUTH, south);
  }

  public void addToList(@Nonnull List<BakedQuad> list, @Nullable Direction face) {
    if (face != null && quads.containsKey(face)) {
      list.add(quads.get(face));
    }
  }
}
