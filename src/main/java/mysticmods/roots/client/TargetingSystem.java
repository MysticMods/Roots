package mysticmods.roots.client;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.item.CastingItem;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TargetingSystem {
  private static final int hitDecay = 2 * 20;
  // TODO: Consider movement

  private static int decayTimer = -1;

  private static EntityHitResult entityHit = null;
/*  private static BlockHitResult blockHit = null;
  private static BlockHitResult fluidHit = null;*/

  public static ItemStack getStaff (Player player) {
    if (player.getMainHandItem().is(RootsTags.Items.CASTING_TOOLS)) {
      return player.getMainHandItem();
    } else if (player.getOffhandItem().is(RootsTags.Items.CASTING_TOOLS)) {
      return player.getOffhandItem();
    } else {
      return ItemStack.EMPTY;
    }
  }

  public static void tick () {
    var mc = Minecraft.getInstance();
    if (mc == null || mc.player == null || mc.level == null) {
      return;
    }

    Player player = mc.player;
    var previousEntity = getEntityHit();
    decayTimer--;
    var staff = getStaff(player);
    ISpellInstance spell = CastingItem.getCurrentSpell(mc.level, player, staff);
    if (spell == null) {
      if (previousEntity != null) {
        previousEntity.removeData(ModAttachments.TARGETED_ENTITY);
      }

      entityHit = null;
/*      blockHit = null;
      fluidHit = null;*/
    } else {
      if (spell.canMarkEntityTargets()) {
        entityHit = pickEntity(player, spell, 0.0f); //spell.getBlockRange(player), spell.getEntityRange(player), spell.canTargetThroughFluids(), 0.0f); // TODO: Check partial tick

        var entity = getEntityHit();
        if (previousEntity != entity && previousEntity != null) {
          previousEntity.removeData(ModAttachments.TARGETED_ENTITY);
        }

        if (entity != null) {
          entity.setData(ModAttachments.TARGETED_ENTITY, true);
        }
      } else {
        if (previousEntity != null) {
          previousEntity.removeData(ModAttachments.TARGETED_ENTITY);
        }
        entityHit = null;
      }
    }
  }

  public static boolean isTargetedEntity (@NotNull Entity entity) {
    return entity.equals(getEntityHit());
  }

  @Nullable
  public static Entity getEntityHit () {
    return entityHit != null ? entityHit.getEntity() : null;
  }

/*  @Nullable
  public static BlockPos getBlockHit () {
    return blockHit != null ? blockHit.getBlockPos() : null;
  }

  @Nullable
  public static BlockPos getFluidHit () {
    return fluidHit != null ? fluidHit.getBlockPos() : null;
  }*/

  private static EntityHitResult pickEntity(Player entity, ISpellInstance spell, float partialTick) {
    double blockInteractionRange = spell.getBlockRange(entity);
    double entityInteractionRange = spell.getEntityRange(entity);
    boolean hitFluids = spell.canTargetThroughFluids();

    double d0 = Math.max(blockInteractionRange, entityInteractionRange);
    double d1 = Mth.square(d0);
    Vec3 vec3 = entity.getEyePosition(partialTick);
    HitResult hitresult = entity.pick(d0, partialTick, hitFluids);
/*    HitResult hitresult2 = entity.pick(d0, partialTick, true);*/
    double d2 = hitresult.getLocation().distanceToSqr(vec3);
    if (hitresult.getType() != HitResult.Type.MISS) {
      d1 = d2;
      d0 = Math.sqrt(d2);
    }

    Vec3 vec31 = entity.getViewVector(partialTick);
    Vec3 vec32 = vec3.add(vec31.x * d0, vec31.y * d0, vec31.z * d0);
    AABB aabb = entity.getBoundingBox().expandTowards(vec31.scale(d0)).inflate(1.0, 1.0, 1.0);
    EntityHitResult entityhitresult = ProjectileUtil.getEntityHitResult(
        entity, vec3, vec32, aabb, p_234237_ -> !p_234237_.isSpectator() && p_234237_.isPickable(), d1
    );

    if (entityhitresult != null) {
      if (entityhitresult.getLocation().distanceTo(vec3) < d2) {
        var entityhitresult2 = filterHitResult(entityhitresult, vec3, entityInteractionRange);
        if (entityhitresult2.getType() == HitResult.Type.ENTITY && spell.canTargetEntity(((EntityHitResult) entityhitresult2).getEntity())) {
/*          entityHit = (EntityHitResult) entityhitresult2;*/
          decayTimer = hitDecay;
          return (EntityHitResult) entityhitresult2;
        } else {
          if (decayTimer == 0) {
            return null;
          }
        }
      }
    } else {
      if (decayTimer == 0) {
        return null;
      }
    }

    return entityHit;

/*    BlockHitResult blockhit = (BlockHitResult) filterHitResult(hitresult, vec3, blockInteractionRange);
    if (blockhit.getType() == HitResult.Type.BLOCK) {
      blockHit = blockhit;
    } else {
      blockHit = null;
    }

    blockhit = (BlockHitResult) filterHitResult(hitresult2, vec3, blockInteractionRange);
    if (blockhit.getType() == HitResult.Type.BLOCK) {
      fluidHit = blockhit;
    } else {
      fluidHit = null;
    }*/
  }

  private static HitResult filterHitResult(HitResult hitResult, Vec3 pos, double blockInteractionRange) {
    Vec3 vec3 = hitResult.getLocation();
    if (!vec3.closerThan(pos, blockInteractionRange)) {
      Vec3 vec31 = hitResult.getLocation();
      Direction direction = Direction.getNearest(vec31.x - pos.x, vec31.y - pos.y, vec31.z - pos.z);
      return BlockHitResult.miss(vec31, direction, BlockPos.containing(vec31));
    } else {
      return hitResult;
    }
  }
}
