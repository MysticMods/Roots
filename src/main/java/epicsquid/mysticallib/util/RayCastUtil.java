package epicsquid.mysticallib.util;

import com.google.common.base.Predicates;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class RayCastUtil {

  @Nullable
  public static RayTraceResult rayTraceBlocksSight(@Nonnull World world, @Nonnull Entity entity, float scale) {
    return rayTraceBlocksSight(world, entity, scale, false);
  }

  @Nullable
  public static RayTraceResult rayTraceBlocksSight(@Nonnull World world, @Nonnull Entity entity, float scale, boolean fluid) {
    return rayTraceBlocksSight(world, entity, scale, fluid, false);
  }

  @Nullable
  public static RayTraceResult rayTraceBlocksSight(@Nonnull World world, @Nonnull Entity entity, float scale, boolean fluid, boolean bounding) {
    return rayTraceBlocksSight(world, entity, scale, fluid, bounding, false);
  }

  @Nullable
  public static RayTraceResult rayTraceBlocksSight(@Nonnull World world, @Nonnull Entity entity, float scale, boolean fluid, boolean bounding, boolean lastBlock) {
    Vec3d position = entity.getPositionVector();
    float eyeHeight = entity.getEyeHeight();
    Vec3d vec1 = position.add(0, eyeHeight, 0);
    Vec3d vec2 = entity.getLookVec().scale(scale).add(vec1);
    return world.rayTraceBlocks(vec1, vec2, fluid, bounding, lastBlock);
  }

  @Nullable
  public static RayTraceResult rayTraceBlocks(@Nonnull World world, @Nonnull Vec3d vec31, @Nonnull Vec3d vec32, boolean stopOnLiquid, boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock, boolean allowNonfullCube) {
    if (!Double.isNaN(vec31.x) && !Double.isNaN(vec31.y) && !Double.isNaN(vec31.z)) {
      if (!Double.isNaN(vec32.x) && !Double.isNaN(vec32.y) && !Double.isNaN(vec32.z)) {
        int i = MathHelper.floor(vec32.x);
        int j = MathHelper.floor(vec32.y);
        int k = MathHelper.floor(vec32.z);
        int l = MathHelper.floor(vec31.x);
        int i1 = MathHelper.floor(vec31.y);
        int j1 = MathHelper.floor(vec31.z);
        BlockPos blockpos = new BlockPos(l, i1, j1);
        IBlockState iblockstate = world.getBlockState(blockpos);
        Block block = iblockstate.getBlock();

        if (((!ignoreBlockWithoutBoundingBox || iblockstate.getCollisionBoundingBox(world, blockpos) != Block.NULL_AABB)) && block.canCollideCheck(iblockstate, stopOnLiquid) || (!block.isAir(iblockstate, world, blockpos) && !block.canCollideCheck(iblockstate, stopOnLiquid) && allowNonfullCube)) {
          RayTraceResult raytraceresult = iblockstate.collisionRayTrace(world, blockpos, vec31, vec32);

          if (raytraceresult != null) {
            return raytraceresult;
          }
        }

        RayTraceResult raytraceresult2 = null;
        int k1 = 200;

        while (k1-- >= 0) {
          if (Double.isNaN(vec31.x) || Double.isNaN(vec31.y) || Double.isNaN(vec31.z)) {
            return null;
          }

          if (l == i && i1 == j && j1 == k) {
            return returnLastUncollidableBlock ? raytraceresult2 : null;
          }

          boolean flag2 = true;
          boolean flag = true;
          boolean flag1 = true;
          double d0 = 999.0D;
          double d1 = 999.0D;
          double d2 = 999.0D;

          if (i > l) {
            d0 = (double) l + 1.0D;
          } else if (i < l) {
            d0 = (double) l + 0.0D;
          } else {
            flag2 = false;
          }

          if (j > i1) {
            d1 = (double) i1 + 1.0D;
          } else if (j < i1) {
            d1 = (double) i1 + 0.0D;
          } else {
            flag = false;
          }

          if (k > j1) {
            d2 = (double) j1 + 1.0D;
          } else if (k < j1) {
            d2 = (double) j1 + 0.0D;
          } else {
            flag1 = false;
          }

          double d3 = 999.0D;
          double d4 = 999.0D;
          double d5 = 999.0D;
          double d6 = vec32.x - vec31.x;
          double d7 = vec32.y - vec31.y;
          double d8 = vec32.z - vec31.z;

          if (flag2) {
            d3 = (d0 - vec31.x) / d6;
          }

          if (flag) {
            d4 = (d1 - vec31.y) / d7;
          }

          if (flag1) {
            d5 = (d2 - vec31.z) / d8;
          }

          if (d3 == -0.0D) {
            d3 = -1.0E-4D;
          }

          if (d4 == -0.0D) {
            d4 = -1.0E-4D;
          }

          if (d5 == -0.0D) {
            d5 = -1.0E-4D;
          }

          EnumFacing enumfacing;

          if (d3 < d4 && d3 < d5) {
            enumfacing = i > l ? EnumFacing.WEST : EnumFacing.EAST;
            vec31 = new Vec3d(d0, vec31.y + d7 * d3, vec31.z + d8 * d3);
          } else if (d4 < d5) {
            enumfacing = j > i1 ? EnumFacing.DOWN : EnumFacing.UP;
            vec31 = new Vec3d(vec31.x + d6 * d4, d1, vec31.z + d8 * d4);
          } else {
            enumfacing = k > j1 ? EnumFacing.NORTH : EnumFacing.SOUTH;
            vec31 = new Vec3d(vec31.x + d6 * d5, vec31.y + d7 * d5, d2);
          }

          l = MathHelper.floor(vec31.x) - (enumfacing == EnumFacing.EAST ? 1 : 0);
          i1 = MathHelper.floor(vec31.y) - (enumfacing == EnumFacing.UP ? 1 : 0);
          j1 = MathHelper.floor(vec31.z) - (enumfacing == EnumFacing.SOUTH ? 1 : 0);
          blockpos = new BlockPos(l, i1, j1);
          IBlockState iblockstate1 = world.getBlockState(blockpos);
          Block block1 = iblockstate1.getBlock();
          if (!ignoreBlockWithoutBoundingBox || iblockstate1.getMaterial() == Material.PORTAL || iblockstate1.getCollisionBoundingBox(world, blockpos) != Block.NULL_AABB || block.canCollideCheck(iblockstate, stopOnLiquid) && (!block.isAir(iblockstate, world, blockpos) && !block.canCollideCheck(iblockstate, stopOnLiquid) && allowNonfullCube)) {
            if (block1.canCollideCheck(iblockstate1, stopOnLiquid) || allowNonfullCube) {
              RayTraceResult raytraceresult1 = iblockstate1.collisionRayTrace(world, blockpos, vec31, vec32);

              if (raytraceresult1 != null) {
                return raytraceresult1;
              }
            } else {
              raytraceresult2 = new RayTraceResult(RayTraceResult.Type.MISS, vec31, enumfacing, blockpos);
            }
          }
        }

        return returnLastUncollidableBlock ? raytraceresult2 : null;
      } else {
        return null;
      }
    } else {
      return null;
    }
  }

  @Nullable
  public static Entity mouseOverEntity(EntityLivingBase entity) {
    return mouseOverEntity(entity, 3.0);
  }

  @Nullable
  public static Entity mouseOverEntity(EntityLivingBase entity, double maxReach) {
    RayTraceAndEntityResult result = rayTraceMouseOver(entity, maxReach);
    return result.getPointedEntity();
  }

  public static RayTraceAndEntityResult rayTraceMouseOver(EntityLivingBase traceEntity) {
    return rayTraceMouseOver(traceEntity, 3.0d);
  }

  public static RayTraceAndEntityResult rayTraceMouseOver(EntityLivingBase traceEntity, double maxEntity) {
    World world = traceEntity.world;
    Entity pointedEntity = null;
    RayTraceResult objectMouseOver;
    boolean player = traceEntity instanceof EntityPlayer;
    Vec3d eyePos = traceEntity.getPositionEyes(1);
    Vec3d lookVec = traceEntity.getLook(1.0F);
    double reach = player ? traceEntity.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue() : 5;
    Vec3d modifiedLookVec = eyePos.add(lookVec.x * reach, lookVec.y * reach, lookVec.z * reach);
    objectMouseOver = world.rayTraceBlocks(eyePos, modifiedLookVec, false, false, true);
    boolean flag = false;
    double distance = reach;

    if (player && ((EntityPlayer) traceEntity).isCreative()) {
      distance = 6.0D;
    } else {
      if (reach > maxEntity) {
        flag = true;
      }
    }

    if (objectMouseOver != null) {
      distance = objectMouseOver.hitVec.distanceTo(eyePos);
    }

    Vec3d hitVec = null;
    @SuppressWarnings("Guava") List<Entity> list = world.getEntitiesInAABBexcluding(traceEntity, traceEntity.getEntityBoundingBox().expand(modifiedLookVec.x, modifiedLookVec.y, modifiedLookVec.z).grow(1.0D, 1.0D, 1.0D), Predicates.and(EntitySelectors.NOT_SPECTATING, e -> e != null && e.canBeCollidedWith()));
    double distance2 = distance;

    for (Entity entity : list) {
      AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox().grow((double) entity.getCollisionBorderSize());
      RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(eyePos, modifiedLookVec);

      if (axisalignedbb.contains(eyePos)) {
        if (distance2 >= 0.0D) {
          pointedEntity = entity;
          hitVec = raytraceresult == null ? eyePos : raytraceresult.hitVec;
          distance2 = 0.0D;
        }
      } else if (raytraceresult != null) {
        double d3 = eyePos.distanceTo(raytraceresult.hitVec);

        if (d3 < distance2 || distance2 == 0.0D) {
          if (entity.getLowestRidingEntity() == traceEntity.getLowestRidingEntity() && !entity.canRiderInteract()) {
            if (distance2 == 0.0D) {
              pointedEntity = entity;
              hitVec = raytraceresult.hitVec;
            }
          } else {
            pointedEntity = entity;
            hitVec = raytraceresult.hitVec;
            distance2 = d3;
          }
        }
      }
    }

    if (pointedEntity != null && flag && eyePos.distanceTo(hitVec) > maxEntity) {
      pointedEntity = null;
      //noinspection ConstantConditions
      objectMouseOver = new RayTraceResult(RayTraceResult.Type.MISS, hitVec, null, new BlockPos(hitVec));
    }

    if (pointedEntity != null && (distance2 < distance || objectMouseOver == null)) {
      objectMouseOver = new RayTraceResult(pointedEntity, hitVec);
    }

    return new RayTraceAndEntityResult(objectMouseOver, pointedEntity);
  }

  public static class RayTraceAndEntityResult {
    private RayTraceResult result;
    private Entity pointedEntity;

    public RayTraceAndEntityResult(RayTraceResult result, Entity pointedEntity) {
      this.result = result;
      this.pointedEntity = pointedEntity;
    }

    @Nullable
    public RayTraceResult getResult() {
      return result;
    }

    @Nullable
    public void setResult(RayTraceResult result) {
      this.result = result;
    }

    public Entity getPointedEntity() {
      return pointedEntity;
    }

    public void setPointedEntity(Entity pointedEntity) {
      this.pointedEntity = pointedEntity;
    }
  }

  public static <T extends Entity> List<T> rayTraceEntities(Class<T> clazz, Entity traceTarget, double distance) {
    return rayTraceEntities(clazz, traceTarget, distance, 0.1);
  }

  public static List<Vec3d> rayTraceEntitiesPositions(Entity traceTarget, double distance) {
    float eyes = traceTarget.getEyeHeight();
    Vec3d pos = traceTarget.getPositionVector();
    Vec3d posEyes = pos.add(0, eyes, 0);
    Vec3d lookVec = traceTarget.getLookVec();
    double yaw = Math.toRadians(-90 - traceTarget.rotationYaw);
    double offX = 0.5 * Math.sin(yaw);
    double offZ = 0.5 * Math.cos(yaw);
    Vec3d startPosition = new Vec3d(traceTarget.posX + offX, traceTarget.posY + eyes, traceTarget.posZ + offZ);
    RayTraceResult result = traceTarget.world.rayTraceBlocks(posEyes, posEyes.add(lookVec.scale(distance)), false, true, true);
    Vec3d stopPosition;
    if (result == null) {
      stopPosition = posEyes.add(lookVec.scale(distance));
    } else {
      stopPosition = result.hitVec;
    }
    return Arrays.asList(startPosition, stopPosition);
  }

  public static <T extends Entity> List<T> rayTraceEntities(Class<T> clazz, Entity traceTarget, double distance, double width) {
    List<Vec3d> positions = rayTraceEntitiesPositions(traceTarget, distance);
    Vec3d startPosition = positions.get(0);
    Vec3d stopPosition = positions.get(1);
    Set<T> entities = new HashSet<>();
    double bx = Math.abs(stopPosition.x - startPosition.x) * width;
    double by = Math.abs(stopPosition.y - startPosition.y) * width;
    double bz = Math.abs(stopPosition.z - startPosition.z) * width;
    for (float i = 0; i < 1; i += 0.1f) {
      double x = startPosition.x * (1.0f - i) + stopPosition.x * i;
      double y = startPosition.y * (1.0f - i) + stopPosition.y * i;
      double z = startPosition.z * (1.0f - i) + stopPosition.z * i;
      entities.addAll(traceTarget.world.getEntitiesWithinAABB(clazz, new AxisAlignedBB(x - bx, y - by, z - bz, x + bx, y + by, z + bz), o -> o != traceTarget));
    }
    List<T> res = new ArrayList<>(entities);
    res.sort((o1, o2) -> Float.compare(o1.getDistance(traceTarget), o2.getDistance(traceTarget)));
    return res;
  }
}
