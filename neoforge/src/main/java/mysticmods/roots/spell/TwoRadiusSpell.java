package mysticmods.roots.spell;

import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.property.SpellProperty;
import mysticmods.roots.api.spell.Spell;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;

import java.util.List;

public abstract class TwoRadiusSpell extends Spell {
  protected int radiusZX, radiusY;
  protected BoundingBox boundingBox;
  protected AABB aabb;

  public TwoRadiusSpell(Type type, ChatFormatting color, List<Cost> costs, int color1, int color2) {
    super(type, color, costs, color1, color2);
  }

  @Override
  protected void initializeProperties() {
    super.initializeProperties();
    SpellProperty<Integer> prop = getRadiusYProperty();
    if (prop != null) {
      this.radiusY = prop.getValue();
    } else {
      throw new IllegalStateException("Spell " + this + " has no radiusY property!");
    }
    prop = getRadiusZXProperty();
    if (prop != null) {
      this.radiusZX = prop.getValue();
    } else {
      throw new IllegalStateException("Spell " + this + " has no radiusZX property!");
    }
  }

  @Override
  public abstract SpellProperty<Integer> getCooldownProperty();

  public abstract SpellProperty<Integer> getRadiusYProperty ();

  public abstract SpellProperty<Integer> getRadiusZXProperty ();

  public int getRadiusX () {
    return radiusZX;
  }

  public int getRadiusZ () {
    return radiusZX;
  }

  public int getRadiusY () {
    return radiusY;
  }

  public int getRadiusZX () {
    return radiusZX;
  }

  public BoundingBox getBoundingBox () {
    if (boundingBox == null) {
      boundingBox = new BoundingBox(-getRadiusX(), -getRadiusY(), -getRadiusZ(), getRadiusX(), getRadiusY(), getRadiusZ());
    }
    return boundingBox;
  }

  public AABB getAABB () {
    if (aabb == null) {
      aabb = AABB.of(getBoundingBox());
    }
    return aabb;
  }
}
