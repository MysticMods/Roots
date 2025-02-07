package mysticmods.roots.util;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.UnaryOperator;


@SuppressWarnings({"unused", "Duplicates", "WeakerAccess"})
public class VoxelUtil {
  private static final Vec3 fromOrigin = new Vec3(-0.5, -0.5, -0.5);

  /**
   * @param base   The base voxel shape that all additional shapes will be {@link VoxelShape ::or VoxelShape::or'd} into.
   * @param shapes Additional shapes to be combined into the main shape.
   * @return The combination of base and each item contained within `shapes`.
   */
  public static VoxelShape multiOr(VoxelShape base, VoxelShape... shapes) {
    VoxelShape result = base;

    for (VoxelShape shape : shapes) {
      result = Shapes.or(result, shape);
    }

    return result;
  }

  /**
   * @param base   6 doubles defining the uppermost and lowermost points of a cube, that all additional shapes will
   *               be {@link VoxelShape ::or VoxelShape::or'd} into.
   * @param shapes Additional shapes defined as an array of 6 doubles, to be combined into the main shape.
   * @return The combination of the base and each subsequent shape.
   */
  public static VoxelShape multiOr(double[] base, double[]... shapes) {
    VoxelShape result = makeCube(base);

    for (double[] shape : shapes) {
      result = Shapes.or(result, makeCube(shape));
    }

    return result;
  }

  /*
    Following code was derived from Mekanism's VoxelShapeUtils:
    https://raw.githubusercontent.com/mekanism/Mekanism/1.15x/src/main/java/mekanism/common/util/VoxelShapeUtils.java
    Licensed under the terms of the MIT license, compatible with this module
   */

  /**
   * @param p 6 doubles consisting of the uppermost and lowermost points of a cube.
   * @return The VoxelShape describing that cube.
   */
  @SuppressWarnings("WeakerAccess")
  public static VoxelShape makeCube(double... p) {
    return Shapes.box(p[0] / 16.0D, p[1] / 16.0D, p[2] / 16.0D, p[3] / 16.0D, p[4] / 16.0D, p[5] / 16.0D);
  }

  /**
   * Rotates an {@link AABB} to a specific side, similar to how the block states rotate models.
   *
   * @param box  The {@link AABB} to rotate
   * @param side The side to rotate it to.
   * @return The rotated {@link AABB}
   */
  @SuppressWarnings("SuspiciousNameCombination")
  public static AABB rotate(AABB box, Direction side) {
    switch (side) {
      case DOWN:
        return box;
      case UP:
        return new AABB(box.minX, -box.minY, -box.minZ, box.maxX, -box.maxY, -box.maxZ);
      case NORTH:
        return new AABB(box.minX, -box.minZ, box.minY, box.maxX, -box.maxZ, box.maxY);
      case SOUTH:
        return new AABB(-box.minX, box.minZ, -box.minY, -box.maxX, box.maxZ, -box.maxY);
      case WEST:
        return new AABB(box.minY, -box.minZ, -box.minX, box.maxY, -box.maxZ, -box.maxX);
      case EAST:
        return new AABB(-box.minY, box.minZ, box.minX, -box.maxY, box.maxZ, box.maxX);
    }
    return box;
  }

  /**
   * Rotates an {@link AABB} to a according to a specific rotation.
   *
   * @param box      The {@link AABB} to rotate
   * @param rotation The rotation we are performing.
   * @return The rotated {@link AABB}
   */
  public static AABB rotate(AABB box, Rotation rotation) {
    switch (rotation) {
      case NONE:
        return box;
      case CLOCKWISE_90:
        return new AABB(-box.minZ, box.minY, box.minX, -box.maxZ, box.maxY, box.maxX);
      case CLOCKWISE_180:
        return new AABB(-box.minX, box.minY, -box.minZ, -box.maxX, box.maxY, -box.maxZ);
      case COUNTERCLOCKWISE_90:
        return new AABB(box.minZ, box.minY, -box.minX, box.maxZ, box.maxY, -box.maxX);
    }
    return box;
  }

  /**
   * Rotates an {@link AABB} to a specific side horizontally. This is a default most common rotation setup as to {@link #rotate(AABB, Rotation)}
   *
   * @param box  The {@link AABB} to rotate
   * @param side The side to rotate it to.
   * @return The rotated {@link AABB}
   */
  public static AABB rotateHorizontal(AABB box, Direction side) {
    switch (side) {
      case NORTH:
        return rotate(box, Rotation.NONE);
      case SOUTH:
        return rotate(box, Rotation.CLOCKWISE_180);
      case WEST:
        return rotate(box, Rotation.COUNTERCLOCKWISE_90);
      case EAST:
        return rotate(box, Rotation.CLOCKWISE_90);
    }
    return box;
  }

  /**
   * Rotates a {@link VoxelShape} to a specific side, similar to how the block states rotate models.
   *
   * @param shape The {@link VoxelShape} to rotate
   * @param side  The side to rotate it to.
   * @return The rotated {@link VoxelShape}
   */
  public static VoxelShape rotate(VoxelShape shape, Direction side) {
    return rotate(shape, box -> rotate(box, side));
  }

  /**
   * Rotates a {@link VoxelShape} to a according to a specific rotation.
   *
   * @param shape    The {@link VoxelShape} to rotate
   * @param rotation The rotation we are performing.
   * @return The rotated {@link VoxelShape}
   */
  public static VoxelShape rotate(VoxelShape shape, Rotation rotation) {
    return rotate(shape, box -> rotate(box, rotation));
  }

  /**
   * Rotates a {@link VoxelShape} to a specific side horizontally. This is a default most common rotation setup as to {@link #rotate(VoxelShape, Rotation)}
   *
   * @param shape The {@link VoxelShape} to rotate
   * @param side  The side to rotate it to.
   * @return The rotated {@link VoxelShape}
   */
  public static VoxelShape rotateHorizontal(VoxelShape shape, Direction side) {
    return rotate(shape, box -> rotateHorizontal(box, side));
  }

  /**
   * Rotates a {@link VoxelShape} using a specific transformation function for each {@link AABB} in the {@link VoxelShape}.
   *
   * @param shape          The {@link VoxelShape} to rotate
   * @param rotateFunction The transformation function to apply to each {@link AABB} in the {@link VoxelShape}.
   * @return The rotated {@link VoxelShape}
   */
  public static VoxelShape rotate(VoxelShape shape, UnaryOperator<AABB> rotateFunction) {
    List<VoxelShape> rotatedPieces = new ArrayList<>();
    // Explode the voxel shape into bounding boxes
    List<AABB> sourceBoundingBoxes = shape.toAabbs();
    // Rotate them and convert them each back into a voxel shape
    for (AABB sourceBoundingBox : sourceBoundingBoxes) {
      // Make the bounding box be centered around the middle, and then move it back after rotating
      rotatedPieces.add(Shapes.create(rotateFunction.apply(sourceBoundingBox.move(fromOrigin.x, fromOrigin.y, fromOrigin.z))
          .move(-fromOrigin.x, -fromOrigin.z, -fromOrigin.z)));
    }
    return combine(rotatedPieces);
  }

  /**
   * Used for mass combining shapes
   *
   * @param shapes The list of {@link VoxelShape}s to include
   * @return A simplified {@link VoxelShape} including everything that is part of any of the input shapes.
   */
  public static VoxelShape combine(VoxelShape... shapes) {
    return batchCombine(Shapes.empty(), BooleanOp.OR, true, shapes);
  }

  /**
   * Used for mass combining shapes
   *
   * @param shapes The collection of {@link VoxelShape}s to include
   * @return A simplified {@link VoxelShape} including everything that is part of any of the input shapes.
   */
  public static VoxelShape combine(Collection<VoxelShape> shapes) {
    return combine(shapes, true);
  }

  public static VoxelShape combine(Collection<VoxelShape> shapes, boolean simplify) {
    return batchCombine(Shapes.empty(), BooleanOp.OR, simplify, shapes);
  }

  /**
   * Used for cutting shapes out of a full cube
   *
   * @param shapes The list of {@link VoxelShape}s to cut out
   * @return A {@link VoxelShape} including everything that is not part of any of the input shapes.
   */
  public static VoxelShape exclude(VoxelShape... shapes) {
    return batchCombine(Shapes.block(), BooleanOp.ONLY_FIRST, true, shapes);
  }

  /**
   * Used for mass combining shapes using a specific {@link BooleanOp} and a given start shape.
   *
   * @param initial  The {@link VoxelShape} to start with
   * @param function The {@link BooleanOp} to perform
   * @param simplify True if the returned shape should run {@link VoxelShape#optimize()}, False otherwise
   * @param shapes   The collection of {@link VoxelShape}s to include
   * @return A {@link VoxelShape} based on the input parameters.
   * @implNote We do not do any simplification until after combining all the shapes, and then only if the {@code simplify} is True. This is because there is a
   * performance hit in calculating the simplified shape each time if we still have more changers we are making to it.
   */
  public static VoxelShape batchCombine(VoxelShape initial, BooleanOp function, boolean simplify, Collection<VoxelShape> shapes) {
    VoxelShape combinedShape = initial;
    for (VoxelShape shape : shapes) {
      combinedShape = Shapes.joinUnoptimized(combinedShape, shape, function);
    }
    return simplify ? combinedShape.optimize() : combinedShape;
  }

  /**
   * Used for mass combining shapes using a specific {@link BooleanOp} and a given start shape.
   *
   * @param initial  The {@link VoxelShape} to start with
   * @param function The {@link BooleanOp} to perform
   * @param simplify True if the returned shape should run {@link VoxelShape#optimize()}, False otherwise
   * @param shapes   The list of {@link VoxelShape}s to include
   * @return A {@link VoxelShape} based on the input parameters.
   * @implNote We do not do any simplification until after combining all the shapes, and then only if the {@code simplify} is True. This is because there is a
   * performance hit in calculating the simplified shape each time if we still have more changers we are making to it.
   */
  public static VoxelShape batchCombine(VoxelShape initial, BooleanOp function, boolean simplify, VoxelShape... shapes) {
    VoxelShape combinedShape = initial;
    for (VoxelShape shape : shapes) {
      combinedShape = Shapes.joinUnoptimized(combinedShape, shape, function);
    }
    return simplify ? combinedShape.optimize() : combinedShape;
  }

  /**
   * Rotates a point around the origin with a given angle in the X, Y, and Z directions.
   *
   * @param x            X coordinate of the point to rotate
   * @param y            Y coordinate of the point to rotate
   * @param z            Z coordinate of the point to rotate
   * @param rotateAngleX Angle in radians in the X direction to rotate the input point
   * @param rotateAngleY Angle in radians in the Y direction to rotate the input point
   * @param rotateAngleZ Angle in radians in the Z direction to rotate the input point
   * @return A {@link Vector3f} of the new positions for x, y, and z after rotating around the origin at the given angle.
   */
  private static Vector3f rotateVector(float x, float y, float z, float rotateAngleX, float rotateAngleY, float rotateAngleZ) {
    //TODO: It does not seem that multi angle rotations work properly, at least when one of them is y
    float xReturn = x;
    float yReturn = y;
    float zReturn = z;
    if (rotateAngleZ != 0) {
      float sinZ = (float) Math.sin(rotateAngleZ);
      float cosZ = (float) Math.cos(rotateAngleZ);
      xReturn = x * cosZ - y * sinZ;
      yReturn = x * sinZ + y * cosZ;
      x = xReturn;
      y = yReturn;
    }
    if (rotateAngleY != 0) {
      float sinY = (float) Math.sin(rotateAngleY);
      float cosY = (float) Math.cos(rotateAngleY);
      xReturn = x * cosY + z * sinY;
      zReturn = z * cosY - x * sinY;
      //x = xReturn;
      z = zReturn;
    }
    if (rotateAngleX != 0) {
      float sinX = (float) Math.sin(rotateAngleX);
      float cosX = (float) Math.cos(rotateAngleX);
      yReturn = y * cosX - z * sinX;
      zReturn = y * sinX + z * cosX;
    }
    return new Vector3f(xReturn, yReturn, zReturn);
  }

  @FunctionalInterface
  public interface ShapeCreator {
    VoxelShape createShape(float x, float y, float z);
  }

  /**
   * Float version of Vector3d
   */
  private static class Vector3f {

    public final float x;
    public final float y;
    public final float z;

    public Vector3f(float x, float y, float z) {
      this.x = x;
      this.y = y;
      this.z = z;
    }

    public Vector3f scale(float factor) {
      return mul(factor, factor, factor);
    }

    public Vector3f subtract(Vector3f vec) {
      return subtract(vec.x, vec.y, vec.z);
    }

    public Vector3f subtract(float x, float y, float z) {
      return add(-x, -y, -z);
    }

    public Vector3f add(Vector3f vec) {
      return add(vec.x, vec.y, vec.z);
    }

    public Vector3f add(float x, float y, float z) {
      return new Vector3f(this.x + x, this.y + y, this.z + z);
    }

    public Vector3f mul(float factorX, float factorY, float factorZ) {
      return new Vector3f(this.x * factorX, this.y * factorY, this.z * factorZ);
    }

    public String toString() {
      return "(" + this.x + ", " + this.y + ", " + this.z + ")";
    }
  }
}
