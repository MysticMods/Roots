package mysticmods.roots.spell;

import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.datamap.PropertyDataMap;
import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.spell.Spell;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;

import java.util.List;

public abstract class TwoRadiusSpell extends Spell {
  protected int radiusZX, radiusY;
  protected BoundingBox boundingBox;
  protected AABB aabb;

  public TwoRadiusSpell(Type type, ChatFormatting color, CostInstance costs, int color1, int color2) {
    super(type, color, costs, color1, color2);
  }

  @Override
  public void buildProperties (List<PropertyHolder<?>> properties) {
    super.buildProperties(properties);
    properties.add(getRadiusZXProperty());
    properties.add(getRadiusYProperty());
  }

  @Override
  protected void initializeProperties(Holder<Spell> holder) {
    super.initializeProperties(holder);
    PropertyDataMap properties = holder.getData(DataMaps.SPELL_PROPERTY_DATA);
    this.radiusY = properties.get(getRadiusYProperty());
    this.radiusZX = properties.get(getRadiusZXProperty());
  }

  @Override
  public abstract PropertyHolder<Property.IntegerProperty> getCooldownProperty();

  public abstract PropertyHolder<Property.IntegerProperty> getRadiusYProperty();

  public abstract PropertyHolder<Property.IntegerProperty> getRadiusZXProperty();

  public int getRadiusX() {
    return radiusZX;
  }

  public int getRadiusZ() {
    return radiusZX;
  }

  public int getRadiusY() {
    return radiusY;
  }

  public int getRadiusZX() {
    return radiusZX;
  }

  public BoundingBox getBoundingBox() {
    if (boundingBox == null) {
      boundingBox = new BoundingBox(-getRadiusX(), -getRadiusY(), -getRadiusZ(), getRadiusX(), getRadiusY(), getRadiusZ());
    }
    return boundingBox;
  }

  public AABB getAABB() {
    if (aabb == null) {
      aabb = AABB.of(getBoundingBox());
    }
    return aabb;
  }
}
