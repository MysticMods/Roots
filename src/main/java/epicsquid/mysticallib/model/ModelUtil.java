package epicsquid.mysticallib.model;

import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import epicsquid.mysticallib.model.parts.Cube;
import epicsquid.mysticallib.model.parts.Segment;
import epicsquid.mysticallib.struct.Vec2f;
import epicsquid.mysticallib.struct.Vec4f;
import epicsquid.mysticallib.util.MathUtil;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;

public class ModelUtil {
  public static float STANDARD_SPRITE_WIDTH = 0, STANDARD_SPRITE_HEIGHT = 0;

  // UV for a texture using a full face
  public static @Nonnull Vec4f FULL_FACE_UV = new Vec4f(0, 0, 16, 16);

  // Array for each face if it has a full uv texture
  public static @Nonnull Vec4f[] FULL_FACES = new Vec4f[] { FULL_FACE_UV, FULL_FACE_UV, FULL_FACE_UV, FULL_FACE_UV, FULL_FACE_UV, FULL_FACE_UV };

  public static boolean[] NO_CULL = new boolean[] { true, true, true, true, true, true };

  @Nonnull
  public static BakedQuad makeCubeFace(@Nonnull VertexFormat format, @Nonnull EnumFacing side, double x, double y, double z, double w, double h, double l,
      float u, float v, float uw, float vh, @Nonnull TextureAtlasSprite sprite, int tintIndex) {
    switch (side) {
    case NORTH:
      return createQuad(format, x + w, y + h, z, x + w, y, z, x, y, z, x, y + h, z, u, v, uw, vh, new Vec3d(0, 0, -1), sprite, tintIndex);

    case SOUTH:
      return createQuad(format, x, y + h, z + l, x, y, z + l, x + w, y, z + l, x + w, y + h, z + l, u, v, uw, vh, new Vec3d(0, 0, 1), sprite, tintIndex);

    case WEST:
      return createQuad(format, x, y + h, z, x, y, z, x, y, z + l, x, y + h, z + l, u, v, uw, vh, new Vec3d(-1, 0, 0), sprite, tintIndex);

    case EAST:
      return createQuad(format, x + w, y + h, z + l, x + w, y, z + l, x + w, y, z, x + w, y + h, z, u, v, uw, vh, new Vec3d(1, 0, 0), sprite, tintIndex);

    case DOWN:
      return createQuad(format, x + w, y, z, x + w, y, z + l, x, y, z + l, x, y, z, u, v, uw, vh, new Vec3d(0, -1, 0), sprite, tintIndex);

    case UP:
      return createQuad(format, x, y + h, z, x, y + h, z + l, x + w, y + h, z + l, x + w, y + h, z, u, v, uw, vh, new Vec3d(0, 1, 0), sprite, tintIndex);
    }
    return null;
  }

  @Nonnull
  public static BakedQuad makeCubeFace(@Nonnull VertexFormat format, @Nonnull EnumFacing side, double x, double y, double z, double w, double h, double l,
      float u, float v, float uw, float vh, @Nonnull TextureAtlasSprite sprite, @Nonnull Function<Vec3d, Vec3d> transform, int tintIndex) {
    Vec3d[] vertices = new Vec3d[0];
    switch (side) {
    case NORTH:
      vertices = new Vec3d[] { new Vec3d(x + w, y + h, z), new Vec3d(x + w, y, z), new Vec3d(x, y, z), new Vec3d(x, y + h, z), };
      break;
    case SOUTH:
      vertices = new Vec3d[] { new Vec3d(x, y + h, z + l), new Vec3d(x, y, z + l), new Vec3d(x + w, y, z + l), new Vec3d(x + w, y + h, z + l), };
      break;
    case WEST:
      vertices = new Vec3d[] { new Vec3d(x, y + h, z), new Vec3d(x, y, z), new Vec3d(x, y, z + l), new Vec3d(x, y + h, z + l), };
      break;
    case EAST:
      vertices = new Vec3d[] { new Vec3d(x + w, y + h, z + l), new Vec3d(x + w, y, z + l), new Vec3d(x + w, y, z), new Vec3d(x + w, y + h, z), };
      break;
    case DOWN:
      vertices = new Vec3d[] { new Vec3d(x + w, y, z), new Vec3d(x + w, y, z + l), new Vec3d(x, y, z + l), new Vec3d(x, y, z), };
      break;
    case UP:
      vertices = new Vec3d[] { new Vec3d(x, y + h, z), new Vec3d(x, y + h, z + l), new Vec3d(x + w, y + h, z + l), new Vec3d(x + w, y + h, z), };
      break;
    default:
      break;
    }
    if (vertices != null && vertices.length > 0) {
      for (int i = 0; i < vertices.length; i++) {
        vertices[i] = transform.apply(vertices[i]);
      }
      return createQuad(format, vertices[0].x, vertices[0].y, vertices[0].z, vertices[1].x, vertices[1].y, vertices[1].z, vertices[2].x, vertices[2].y,
          vertices[2].z, vertices[3].x, vertices[3].y, vertices[3].z, u, v, uw, vh, sprite, tintIndex);
    }
    return null;
  }

  @Nonnull
  public static Cube makeCube(@Nonnull VertexFormat format, double x, double y, double z, double w, double h, double l, @Nullable Vec4f[] uv,
      @Nonnull TextureAtlasSprite[] sprites, @Nonnull Function<Vec3d, Vec3d> transform, int tintIndex) {
    if (uv == null) {
      uv = new Vec4f[] { new Vec4f((float) (z * 16f), (float) MathUtil.nclamp(h * 16f - y * 16f, 16f), (float) l * 16f, (float) h * 16f),
          new Vec4f((float) MathUtil.nclamp(l * 16f - z * 16f, 16f), (float) MathUtil.nclamp(h * 16f - y * 16f, 16f), (float) l * 16f, (float) h * 16f),
          new Vec4f((float) (x * 16f), (float) MathUtil.nclamp(z * 16f, 16f), (float) w * 16f, (float) l * 16f),
          new Vec4f((float) (x * 16f), (float) MathUtil.nclamp(l * 16f - z * 16f, 16f), (float) w * 16f, (float) l * 16f),
          new Vec4f((float) MathUtil.nclamp(w * 16f - x * 16f, 16f), (float) MathUtil.nclamp(h * 16f - y * 16f, 16f), (float) w * 16f, (float) h * 16f),
          new Vec4f((float) (x * 16f), (float) MathUtil.nclamp(h * 16f - y * 16f, 16f), (float) w * 16f, (float) h * 16f) };
    }
    return new Cube(makeCubeFace(format, EnumFacing.WEST, x, y, z, w, h, l, uv[0].x, uv[0].y, uv[0].z, uv[0].w, sprites[0], transform, tintIndex),
        makeCubeFace(format, EnumFacing.EAST, x, y, z, w, h, l, uv[1].x, uv[1].y, uv[1].z, uv[1].w, sprites[1], transform, tintIndex),
        makeCubeFace(format, EnumFacing.DOWN, x, y, z, w, h, l, uv[2].x, uv[2].y, uv[2].z, uv[2].w, sprites[2], transform, tintIndex),
        makeCubeFace(format, EnumFacing.UP, x, y, z, w, h, l, uv[3].x, uv[3].y, uv[3].z, uv[3].w, sprites[3], transform, tintIndex),
        makeCubeFace(format, EnumFacing.NORTH, x, y, z, w, h, l, uv[4].x, uv[4].y, uv[4].z, uv[4].w, sprites[4], transform, tintIndex),
        makeCubeFace(format, EnumFacing.SOUTH, x, y, z, w, h, l, uv[5].x, uv[5].y, uv[5].z, uv[5].w, sprites[5], transform, tintIndex));
  }

  @Nonnull
  public static Cube makeCube(@Nonnull VertexFormat format, float x, float y, float z, float w, float h, float l, @Nullable Vec4f[] uv,
      @Nonnull TextureAtlasSprite[] sprites, int tintIndex) {
    if (uv == null) {
      uv = new Vec4f[] { new Vec4f((z * 16f), (float) MathUtil.nclamp(h * 16f - y * 16f, 16f), l * 16f, h * 16f),
          new Vec4f((float) MathUtil.nclamp(l * 16f - z * 16f, 16f), (float) MathUtil.nclamp(h * 16f - y * 16f, 16f), l * 16f, h * 16f),
          new Vec4f((x * 16f), (float) MathUtil.nclamp(z * 16f, 16f), w * 16f, l * 16f),
          new Vec4f((x * 16f), (float) MathUtil.nclamp(l * 16f - z * 16f, 16f), w * 16f, l * 16f),
          new Vec4f((float) MathUtil.nclamp(w * 16f - x * 16f, 16f), (float) MathUtil.nclamp(h * 16f - y * 16f, 16f), w * 16f, h * 16f),
          new Vec4f((x * 16f), (float) MathUtil.nclamp(h * 16f - y * 16f, 16f), w * 16f, h * 16f) };
    }
    return new Cube(makeCubeFace(format, EnumFacing.WEST, x, y, z, w, h, l, uv[0].x, uv[0].y, uv[0].z, uv[0].w, sprites[0], tintIndex),
        makeCubeFace(format, EnumFacing.EAST, x, y, z, w, h, l, uv[1].x, uv[1].y, uv[1].z, uv[1].w, sprites[1], tintIndex),
        makeCubeFace(format, EnumFacing.DOWN, x, y, z, w, h, l, uv[2].x, uv[2].y, uv[2].z, uv[2].w, sprites[2], tintIndex),
        makeCubeFace(format, EnumFacing.UP, x, y, z, w, h, l, uv[3].x, uv[3].y, uv[3].z, uv[3].w, sprites[3], tintIndex),
        makeCubeFace(format, EnumFacing.NORTH, x, y, z, w, h, l, uv[4].x, uv[4].y, uv[4].z, uv[4].w, sprites[4], tintIndex),
        makeCubeFace(format, EnumFacing.SOUTH, x, y, z, w, h, l, uv[5].x, uv[5].y, uv[5].z, uv[5].w, sprites[5], tintIndex));
  }

  @Nonnull
  public static Segment makeSegm(@Nonnull VertexFormat format, double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3,
      double z3, double x4, double y4, double z4, double x5, double y5, double z5, double x6, double y6, double z6, double x7, double y7, double z7, double x8,
      double y8, double z8, boolean[] culling, @Nonnull TextureAtlasSprite[] sprites, @Nonnull Function<Vec3d, Vec3d> transform, int tintIndex) {
    Vec2f[] west = getQuadUV(x1, y1, z1, x4, y4, z4, x8, y8, z8, x5, y5, z5, EnumFacing.WEST);
    Vec2f[] east = getQuadUV(x3, y3, z3, x2, y2, z2, x6, y6, z6, x7, y7, z7, EnumFacing.EAST);
    Vec2f[] down = getQuadUV(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4, EnumFacing.DOWN);
    Vec2f[] up = getQuadUV(x6, y6, z6, x5, y5, z5, x8, y8, z8, x7, y7, z7, EnumFacing.UP);
    Vec2f[] north = getQuadUV(x4, y4, z4, x3, y3, z3, x7, y7, z7, x8, y8, z8, EnumFacing.NORTH);
    Vec2f[] south = getQuadUV(x2, y2, z2, x1, y1, z1, x5, y5, z5, x6, y6, z6, EnumFacing.SOUTH);
    Vec3d[] v = { new Vec3d(x1, y1, z1), new Vec3d(x2, y2, z2), new Vec3d(x3, y3, z3), new Vec3d(x4, y4, z4), new Vec3d(x5, y5, z5), new Vec3d(x6, y6, z6),
        new Vec3d(x7, y7, z7), new Vec3d(x8, y8, z8) };
    for (int i = 0; i < v.length; i++) {
      v[i] = transform.apply(v[i]);
    }
    x1 = v[3].x;
    y1 = v[3].y;
    z1 = v[3].z;
    x2 = v[2].x;
    y2 = v[2].y;
    z2 = v[2].z;
    x3 = v[1].x;
    y3 = v[1].y;
    z3 = v[1].z;
    x4 = v[0].x;
    y4 = v[0].y;
    z4 = v[0].z;
    x5 = v[7].x;
    y5 = v[7].y;
    z5 = v[7].z;
    x6 = v[6].x;
    y6 = v[6].y;
    z6 = v[6].z;
    x7 = v[5].x;
    y7 = v[5].y;
    z7 = v[5].z;
    x8 = v[4].x;
    y8 = v[4].y;
    z8 = v[4].z;
    return new Segment(createQuad(format, x1, y1, z1, x4, y4, z4, x8, y8, z8, x5, y5, z5, west, sprites[0], tintIndex),
        createQuad(format, x3, y3, z3, x2, y2, z2, x6, y6, z6, x7, y7, z7, east, sprites[1], tintIndex),
        createQuad(format, x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4, down, sprites[2], tintIndex),
        createQuad(format, x6, y6, z6, x5, y5, z5, x8, y8, z8, x7, y7, z7, up, sprites[3], tintIndex),
        createQuad(format, x4, y4, z4, x3, y3, z3, x7, y7, z7, x8, y8, z8, north, sprites[4], tintIndex),
        createQuad(format, x2, y2, z2, x1, y1, z1, x5, y5, z5, x6, y6, z6, south, sprites[5], tintIndex), culling);
  }

  @Nonnull
  public static Segment makeSegm(@Nonnull VertexFormat format, double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3,
      double z3, double x4, double y4, double z4, double x5, double y5, double z5, double x6, double y6, double z6, double x7, double y7, double z7, double x8,
      double y8, double z8, boolean[] culling, @Nonnull TextureAtlasSprite[] sprites, int tintIndex) {
    return new Segment(createQuad(format, x1, y1, z1, x4, y4, z4, x8, y8, z8, x5, y5, z5, EnumFacing.WEST, sprites[0], tintIndex),
        createQuad(format, x3, y3, z3, x2, y2, z2, x6, y6, z6, x7, y7, z7, EnumFacing.EAST, sprites[1], tintIndex),
        createQuad(format, x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4, EnumFacing.DOWN, sprites[2], tintIndex),
        createQuad(format, x6, y6, z6, x5, y5, z5, x8, y8, z8, x7, y7, z7, EnumFacing.UP, sprites[3], tintIndex),
        createQuad(format, x4, y4, z4, x3, y3, z3, x7, y7, z7, x8, y8, z8, EnumFacing.NORTH, sprites[4], tintIndex),
        createQuad(format, x2, y2, z2, x1, y1, z1, x5, y5, z5, x6, y6, z6, EnumFacing.SOUTH, sprites[5], tintIndex), culling);
  }

  @Nonnull
  public static Segment makeSegm(@Nonnull VertexFormat format, double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3,
      double z3, double x4, double y4, double z4, double x5, double y5, double z5, double x6, double y6, double z6, double x7, double y7, double z7, double x8,
      double y8, double z8, @Nonnull TextureAtlasSprite[] sprites, @Nonnull Function<Vec3d, Vec3d> transform, int tintIndex) {
    return ModelUtil
        .makeSegm(format, x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4, x5, y5, z5, x6, y6, z6, x7, y7, z7, x8, y8, z8, ModelUtil.NO_CULL, sprites, transform,
            tintIndex);
  }

  @Nonnull
  public static Segment makeSegm(@Nonnull VertexFormat format, double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3,
      double z3, double x4, double y4, double z4, double x5, double y5, double z5, double x6, double y6, double z6, double x7, double y7, double z7, double x8,
      double y8, double z8, @Nonnull TextureAtlasSprite[] sprites, int tintIndex) {
    return ModelUtil
        .makeSegm(format, x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4, x5, y5, z5, x6, y6, z6, x7, y7, z7, x8, y8, z8, ModelUtil.NO_CULL, sprites,
            tintIndex);
  }

  // This is a special version of the makeSegm method where every face except the downward one has its first and second coord switched with its third and fourth.
  // For some reason faces are rendered black on custom baked models if the coords have a "wrong drawing order" on newer Forge versions.
  @Nonnull
  public static Segment makeSegmUp(@Nonnull VertexFormat format, double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3,
      double z3, double x4, double y4, double z4, double x5, double y5, double z5, double x6, double y6, double z6, double x7, double y7, double z7, double x8,
      double y8, double z8, boolean[] culling, @Nonnull TextureAtlasSprite[] sprites, int tintIndex) {
    return new Segment(ModelUtil.createQuad(format, x8, y8, z8, x5, y5, z5, x1, y1, z1, x4, y4, z4, EnumFacing.WEST, sprites[0], tintIndex),
        ModelUtil.createQuad(format, x6, y6, z6, x7, y7, z7, x3, y3, z3, x2, y2, z2, EnumFacing.EAST, sprites[1], tintIndex),
        ModelUtil.createQuad(format, x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4, EnumFacing.DOWN, sprites[2], tintIndex),
        ModelUtil.createQuad(format, x8, y8, z8, x7, y7, z7, x6, y6, z6, x5, y5, z5, EnumFacing.UP, sprites[3], tintIndex),
        ModelUtil.createQuad(format, x7, y7, z7, x8, y8, z8, x4, y4, z4, x3, y3, z3, EnumFacing.NORTH, sprites[4], tintIndex),
        ModelUtil.createQuad(format, x5, y5, z5, x6, y6, z6, x2, y2, z2, x1, y1, z1, EnumFacing.SOUTH, sprites[5], tintIndex), ModelUtil.NO_CULL);
  }

  @Nonnull
  public static Segment makeSegmUp(@Nonnull VertexFormat format, double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3,
      double z3, double x4, double y4, double z4, double x5, double y5, double z5, double x6, double y6, double z6, double x7, double y7, double z7, double x8,
      double y8, double z8, @Nonnull TextureAtlasSprite[] sprites, int tintIndex) {
    return ModelUtil
        .makeSegmUp(format, x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4, x5, y5, z5, x6, y6, z6, x7, y7, z7, x8, y8, z8, ModelUtil.NO_CULL, sprites,
            tintIndex);
  }

  @Nonnull
  public static Vec2f getUVForPos(double x, double y, double z, @Nonnull EnumFacing face) {
    Vec2f uv = new Vec2f(0, 0);
    switch (face) {
    case DOWN:
      uv = new Vec2f((float) (x * 16f), (float) (z * 16f));
      break;
    case EAST:
      uv = new Vec2f((float) (16f - z * 16f), (float) (16f - y * 16f));
      break;
    case NORTH:
      uv = new Vec2f((float) (x * 16f), (float) (16f - y * 16f));
      break;
    case SOUTH:
      uv = new Vec2f((float) (16f - x * 16f), (float) (16f - y * 16f));
      break;
    case UP:
      uv = new Vec2f((float) (16f - x * 16f), (float) (z * 16f));
      break;
    case WEST:
      uv = new Vec2f((float) (z * 16f), (float) (16f - y * 16f));
      break;
    default:
      break;
    }
    return uv;
  }

  @Nonnull
  public static Vec2f[] getQuadUV(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4,
      double z4, EnumFacing face) {
    Vec2f uv1 = getUVForPos(x1, y1, z1, face);
    Vec2f uv2 = getUVForPos(x2, y2, z2, face);
    Vec2f uv3 = getUVForPos(x3, y3, z3, face);
    Vec2f uv4 = getUVForPos(x4, y4, z4, face);
    return new Vec2f[] { uv1, uv2, uv3, uv4 };
  }

  @Nonnull
  public static BakedQuad createQuad(@Nonnull VertexFormat format, double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3,
      double z3, double x4, double y4, double z4, @Nonnull Vec2f[] uv, @Nonnull TextureAtlasSprite sprite, int tintIndex) {
    Vec3d normal = getNormal(x1, y1, z1, x2, y2, z2, x4, y4, z4);
    UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(format);
    builder.setTexture(sprite);
    Vec2f uv1 = uv[0];
    Vec2f uv2 = uv[1];
    Vec2f uv3 = uv[2];
    Vec2f uv4 = uv[3];
    if (tintIndex > -1) {
      builder.setQuadTint(tintIndex);
    }
    putVertex(builder, normal, x1, y1, z1, uv1.x, uv1.y, sprite);
    putVertex(builder, normal, x2, y2, z2, uv2.x, uv2.y, sprite);
    putVertex(builder, normal, x3, y3, z3, uv3.x, uv3.y, sprite);
    putVertex(builder, normal, x4, y4, z4, uv4.x, uv4.y, sprite);
    return builder.build();
  }

  @Nonnull
  public static BakedQuad createQuad(@Nonnull VertexFormat format, double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3,
      double z3, double x4, double y4, double z4, @Nonnull EnumFacing face, @Nonnull TextureAtlasSprite sprite, int tintIndex) {
    Vec3d normal = getNormal(x1, y1, z1, x2, y2, z2, x4, y4, z4);
    UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(format);
    builder.setTexture(sprite);
    Vec2f uv1 = getUVForPos(x1, y1, z1, face);
    Vec2f uv2 = getUVForPos(x2, y2, z2, face);
    Vec2f uv3 = getUVForPos(x3, y3, z3, face);
    Vec2f uv4 = getUVForPos(x4, y4, z4, face);
    if (tintIndex > -1) {
      builder.setQuadTint(tintIndex);
    }
    putVertex(builder, normal, x1, y1, z1, uv1.x, uv1.y, sprite);
    putVertex(builder, normal, x2, y2, z2, uv2.x, uv2.y, sprite);
    putVertex(builder, normal, x3, y3, z3, uv3.x, uv3.y, sprite);
    putVertex(builder, normal, x4, y4, z4, uv4.x, uv4.y, sprite);
    return builder.build();
  }

  @Nonnull
  public static BakedQuad createQuad(@Nonnull VertexFormat format, double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3,
      double z3, double x4, double y4, double z4, float u1, float v1, float u2, float v2, float u3, float v3, float u4, float v4,
      @Nonnull TextureAtlasSprite sprite, int tintIndex) {
    Vec3d normal = getNormal(x1, y1, z1, x2, y2, z2, x4, y4, z4);
    UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(format);
    builder.setTexture(sprite);
    if (tintIndex > -1) {
      builder.setQuadTint(tintIndex);
    }
    putVertex(builder, normal, x1, y1, z1, u1, v1, sprite);
    putVertex(builder, normal, x2, y2, z2, u2, v2, sprite);
    putVertex(builder, normal, x3, y3, z3, u3, v3, sprite);
    putVertex(builder, normal, x4, y4, z4, u4, v4, sprite);
    return builder.build();
  }

  @Nonnull
  public static BakedQuad createQuad(@Nonnull VertexFormat format, double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3,
      double z3, double x4, double y4, double z4, float u, float v, float uw, float vh, @Nonnull TextureAtlasSprite sprite, int tintIndex) {
    Vec3d normal = getNormal(x1, y1, z1, x2, y2, z2, x4, y4, z4);
    UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(format);
    builder.setTexture(sprite);
    if (tintIndex > -1) {
      builder.setQuadTint(tintIndex);
    }
    putVertex(builder, normal, x1, y1, z1, u, v, sprite);
    putVertex(builder, normal, x2, y2, z2, u, v + vh, sprite);
    putVertex(builder, normal, x3, y3, z3, u + uw, v + vh, sprite);
    putVertex(builder, normal, x4, y4, z4, u + uw, v, sprite);
    return builder.build();
  }

  @Nonnull
  public static Cube makeCube(@Nonnull VertexFormat format, double x, double y, double z, double w, double h, double l, @Nullable Vec4f[] uv,
      @Nonnull TextureAtlasSprite[] sprites, int tintIndex) {
    if (uv == null) {
      uv = new Vec4f[] { new Vec4f((float) z * 16f, (float) -y * 16f + (16f - (float) (h) * 16f), (float) l * 16f, (float) h * 16f),
          new Vec4f((16f - (float) l * 16f) - (float) z * 16f, (float) -y * 16f + (16f - (float) (h) * 16f), (float) l * 16f, (float) h * 16f),
          new Vec4f((float) (x * 16f), (float) MathUtil.nclamp(z * 16f, 16f), (float) w * 16f, (float) l * 16f),
          new Vec4f((float) (x * 16f), (float) MathUtil.nclamp(z * 16f, 16f), (float) w * 16f, (float) l * 16f),
          new Vec4f((16f - (float) w * 16f) - (float) x * 16f, (float) -y * 16f + (16f - (float) (h) * 16f), (float) w * 16f, (float) h * 16f),
          new Vec4f((float) x * 16f, (float) -y * 16f + (16f - (float) (h) * 16f), (float) w * 16f, (float) h * 16f), };
    }
    return new Cube(makeCubeFace(format, EnumFacing.WEST, x, y, z, w, h, l, uv[0].x, uv[0].y, uv[0].z, uv[0].w, sprites[0], tintIndex),
        makeCubeFace(format, EnumFacing.EAST, x, y, z, w, h, l, uv[1].x, uv[1].y, uv[1].z, uv[1].w, sprites[1], tintIndex),
        makeCubeFace(format, EnumFacing.DOWN, x, y, z, w, h, l, uv[2].x, uv[2].y, uv[2].z, uv[2].w, sprites[2], tintIndex),
        makeCubeFace(format, EnumFacing.UP, x, y, z, w, h, l, uv[3].x, uv[3].y, uv[3].z, uv[3].w, sprites[3], tintIndex),
        makeCubeFace(format, EnumFacing.NORTH, x, y, z, w, h, l, uv[4].x, uv[4].y, uv[4].z, uv[4].w, sprites[4], tintIndex),
        makeCubeFace(format, EnumFacing.SOUTH, x, y, z, w, h, l, uv[5].x, uv[5].y, uv[5].z, uv[5].w, sprites[5], tintIndex));
  }

  @Nonnull
  public static Vec3d getNormal(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3) {
    return (new Vec3d(x2 - x1, y2 - y1, z2 - z1)).crossProduct(new Vec3d(x3 - x1, y3 - y1, z3 - z1)).normalize();
  }

  @Nonnull
  public static BakedQuad createQuad(@Nonnull VertexFormat format, double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3,
      double z3, double x4, double y4, double z4, float u, float v, float uw, float vh, @Nonnull Vec3d normal, @Nonnull TextureAtlasSprite sprite,
      int tintIndex) {
    UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(format);
    builder.setTexture(sprite);
    if (tintIndex > -1) {
      builder.setQuadTint(tintIndex);
    }
    putVertex(builder, normal, x1, y1, z1, u, v, sprite);
    putVertex(builder, normal, x2, y2, z2, u, v + vh, sprite);
    putVertex(builder, normal, x3, y3, z3, u + uw, v + vh, sprite);
    putVertex(builder, normal, x4, y4, z4, u + uw, v, sprite);
    return builder.build();
  }

  public static void putVertex(@Nonnull UnpackedBakedQuad.Builder builder, @Nonnull Vec3d normal, double x, double y, double z, float u, float v,
      @Nonnull TextureAtlasSprite sprite) {
    VertexFormat format = builder.getVertexFormat();
    for (int e = 0; e < format.getElementCount(); e++) {
      switch (format.getElement(e).getUsage()) {
      case POSITION:
        builder.put(e, (float) x, (float) y, (float) z, 1.0f);
        break;
      case COLOR:
        builder.put(e, 1.0f, 1.0f, 1.0f, 1.0f);
        break;
      case UV:
        if (format.getElement(e).getIndex() == 0) {
          float uScale = 1.0f;//STANDARD_SPRITE_WIDTH/Math.max(STANDARD_SPRITE_WIDTH,(sprite.getMaxU()-sprite.getMinU()));
          float vScale = 1.0f;//STANDARD_SPRITE_HEIGHT/Math.max(STANDARD_SPRITE_HEIGHT,(sprite.getMaxV()-sprite.getMinV()));
          u = sprite.getInterpolatedU(u * uScale);
          v = sprite.getInterpolatedV(v * vScale);
          builder.put(e, u, v, 0f, 1f);
          break;
        }
      case NORMAL:
        builder.put(e, (float) normal.x, (float) normal.y, (float) normal.z, 0f);
        break;
      default:
        builder.put(e);
        break;
      }
    }
  }
}
