package mysticmods.roots.entity.ai;

import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Animal;

import java.util.EnumSet;

// Swim goal copied from Untitled Duck Mod, which is MIT-liccensed and thus compatible
// Original: https://github.com/Paspartout/UntitledDuckMod/blob/1.16/common/src/main/java/net/untitledduckmod/duck/DuckSwimGoal.java
public class DuckSwimGoal extends Goal {
  private final Animal entity;

  public DuckSwimGoal(Animal entity) {
    this.entity = entity;
    this.setFlags(EnumSet.of(Flag.JUMP));
    entity.getNavigation().setCanFloat(true);
  }

  @Override
  public boolean canUse() {
    return entity.isInWater() && entity.getFluidHeight(FluidTags.WATER) > (entity.isBaby() ? 0.1d : 0.2d) || entity.isInLava();
  }

  @Override
  public void tick() {
    if (entity.getRandom().nextFloat() < 0.8f) {
      entity.getJumpControl().jump();
    }
  }
}
