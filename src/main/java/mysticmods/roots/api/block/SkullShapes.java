package mysticmods.roots.api.block;

import com.google.common.collect.Maps;
import mysticmods.roots.util.VoxelUtil;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.Map;

public final class SkullShapes {
  private static final Direction[] FLOOR_FACINGS = {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};

  private static final VoxelShape TALL_NORTH = VoxelUtil.multiOr(
      VoxelUtil.makeCube(4, 0, 4, 12, 10, 12),
      VoxelUtil.makeCube(7, -1, 2, 9, 3, 4)
  );

  private static final VoxelShape TRADER_NORTH = VoxelUtil.multiOr(
      VoxelUtil.makeCube(3.49, -0.51, 3.49, 12.51, 10.51, 12.51),
      VoxelUtil.makeCube(7, -1, 2, 9, 3, 4)
  );

  private static final VoxelShape ILLUSIONER_NORTH = VoxelUtil.multiOr(
      VoxelUtil.makeCube(3.55, -2.45, 3.55, 12.45, 10.45, 12.45),
      VoxelUtil.makeCube(7, -1, 2, 9, 3, 4)
  );

  private static final VoxelShape WITCH_NORTH = VoxelUtil.multiOr(
      TALL_NORTH,
      VoxelUtil.makeCube(3, 8, 3, 13, 10, 13),
      VoxelUtil.makeCube(4.25, 10, 5, 11.25, 14, 12),
      VoxelUtil.makeCube(5.5, 14, 7, 9.5, 18, 11),
      VoxelUtil.makeCube(6.5, 18, 8.75, 8, 20.25, 10.25)
  );

  private static final VoxelShape ENDERMAN_POWERED_NORTH = VoxelUtil.multiOr(
      VoxelUtil.makeCube(4.2, 0, 4.2, 11.8, 7.6, 11.8),
      VoxelUtil.makeCube(4, 5, 4, 12, 13, 12)
  );

  private static final VoxelShape OUTER_LAYER_NORTH = VoxelUtil.makeCube(3.8125, 0, 3.8125, 12.1875, 8.1875, 12.1875);

  private static final ShapeSet TALL = ShapeSet.of(TALL_NORTH);
  private static final ShapeSet OUTER_LAYER = ShapeSet.of(OUTER_LAYER_NORTH);
  private static final ShapeSet ENDERMAN_POWERED = ShapeSet.of(ENDERMAN_POWERED_NORTH);
  private static final ShapeSet TRADER = ShapeSet.of(TRADER_NORTH);
  private static final ShapeSet ILLUSIONER = ShapeSet.of(ILLUSIONER_NORTH);
  private static final ShapeSet WITCH = ShapeSet.of(WITCH_NORTH);
  private SkullShapes() {
  }

  @Nullable
  public static VoxelShape floor(SkullBlock.Type type, int rotation, boolean powered) {
    ShapeSet set = setFor(type, powered);
    return set == null ? null : set.floor().get(FLOOR_FACINGS[((rotation + 2) / 4) & 3]);
  }

  @Nullable
  public static VoxelShape wall(SkullBlock.Type type, Direction facing, boolean powered) {
    ShapeSet set = setFor(type, powered);
    return set == null ? null : set.wall().get(facing);
  }

  @Nullable
  private static ShapeSet setFor(SkullBlock.Type type, boolean powered) {
    if (!(type instanceof ExtraSkullTypes extra)) {
      return null;
    }

    return switch (extra) {
      case ENDERMAN -> powered ? ENDERMAN_POWERED : null;
      case VILLAGER, ZOMBIE_VILLAGER, PILLAGER, IRON_GOLEM -> TALL;
      case WANDERING_TRADER -> TRADER;
      case ILLUSIONER -> ILLUSIONER;
      case WITCH -> WITCH;
      case DROWNED, STRAY, BOGGED -> OUTER_LAYER;
      default -> null;
    };
  }

  private static VoxelShape clamp(VoxelShape shape) {
    return Shapes.join(shape, Shapes.block(), BooleanOp.AND);
  }

  private record ShapeSet(Map<Direction, VoxelShape> floor, Map<Direction, VoxelShape> wall) {
    static ShapeSet of(VoxelShape north) {
      VoxelShape wallNorth = north.move(0, 4 / 16.0, 4 / 16.0);
      Map<Direction, VoxelShape> floor = new EnumMap<>(Direction.class);
      Map<Direction, VoxelShape> wall = new EnumMap<>(Direction.class);
      for (Direction direction : FLOOR_FACINGS) {
        floor.put(direction, clamp(VoxelUtil.rotateHorizontal(north, direction)));
        wall.put(direction, clamp(VoxelUtil.rotateHorizontal(wallNorth, direction)));
      }
      return new ShapeSet(Maps.immutableEnumMap(floor), Maps.immutableEnumMap(wall));
    }
  }
}