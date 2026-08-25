package mysticmods.roots.api.template;

import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

public interface TargetingTemplate {
  class Builder {
    protected double distance = -1;
    protected TargetType type = null;
    @Nullable
    protected Entity targeter = null;

    public Builder withDistance(double distance) {
      this.distance = distance;
      return this;
    }

    public Builder withTargeter(Entity entity) {
      this.targeter = entity;
      return this;
    }

    public Builder entity() {
      if (this.type != null && this.type != TargetType.ENTITY) {
        throw new IllegalStateException("TargetingTemplate builder is already set to " + this.type);
      }
      this.type = TargetType.ENTITY;
      return this;
    }

    public Builder block() {
      if (this.type != null && this.type != TargetType.BLOCK) {
        throw new IllegalStateException("TargetingTemplate builder is already set to " + this.type);
      }
      this.type = TargetType.BLOCK;
      return this;
    }

    public Builder fluid() {
      if (this.type != null && this.type != TargetType.FLUID) {
        throw new IllegalStateException("TargetingTemplate builder is already set to " + this.type);
      }
      this.type = TargetType.FLUID;
      return this;
    }

    public Builder air() {
      if (this.type != null && this.type != TargetType.AIR) {
        throw new IllegalStateException("TargetingTemplate builder is already set to " + this.type);
      }
      this.type = TargetType.AIR;
      return this;
    }

    // TODO:
    public void build() {

    }
  }

  enum TargetType {
    AIR,
    BLOCK,
    FLUID,
    ENTITY;
  }
}
