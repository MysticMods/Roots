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

import java.util.ArrayList;
import java.util.List;

public class TargetingSystem {
  private static final int hitDecay = 2 * 20;
  // TODO: Consider movement

  private static List<Entity> targetedEntities = new ArrayList<>();

  private static int decayTimer = -1;

  public static ItemStack getStaff (Player player) {
    if (player.getMainHandItem().is(RootsTags.Items.CASTING_TOOLS)) {
      return player.getMainHandItem();
    } else if (player.getOffhandItem().is(RootsTags.Items.CASTING_TOOLS)) {
      return player.getOffhandItem();
    } else {
      return ItemStack.EMPTY;
    }
  }

  public static ISpellInstance getCurrentSpell (Player player) {
    return CastingItem.getCurrentSpell(player.level(), player, getStaff(player));
  }

  private static void clearTargeted () {
    for (Entity entity : targetedEntities) {
      entity.removeData(ModAttachments.TARGETED_ENTITY);
    }
    targetedEntities.clear();
  }

  public static void tick () {
    var mc = Minecraft.getInstance();
    if (mc == null || mc.player == null || mc.level == null) {
      return;
    }

    Player player = mc.player;
    decayTimer--;
    ISpellInstance spell = getCurrentSpell(player);
    if (spell == null || !spell.canMarkEntityTargets() || decayTimer == 0) {
      // If we've switched spells or its decayed
      clearTargeted();
    } else if (spell.canMarkEntityTargets()) {
      var newTargets = pickEntities(player, spell, 0.0f);
      if (!newTargets.isEmpty()) {
        clearTargeted();
        targetedEntities.addAll(newTargets);
        for (Entity entity : targetedEntities) {
          entity.setData(ModAttachments.TARGETED_ENTITY, true);
        }
      }
    }
  }

  public static boolean isTargetedEntity (@NotNull Entity entity) {
    return targetedEntities.contains(entity);
  }

  @Nullable
  public static Entity getEntityHit () {
    return null;
/*    return entityHit != null ? entityHit.getEntity() : null;*/
  }

/*  @Nullable
  public static BlockPos getBlockHit () {
    return blockHit != null ? blockHit.getBlockPos() : null;
  }

  @Nullable
  public static BlockPos getFluidHit () {
    return fluidHit != null ? fluidHit.getBlockPos() : null;
  }*/

  enum TargetType {
    BLOCK,
    ENTITY,
    SPLASH;
  }

  private static List<Entity> pickEntities(Player entity, ISpellInstance spell, float partialTick) {
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
          return null; //(EntityHitResult) entityhitresult2;
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

    return null; //entityHit;
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
