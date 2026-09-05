package mysticmods.roots.spell;

import mysticmods.roots.api.SpellType;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.datamap.PropertyDataMap;
import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.Spell;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;

import java.util.List;

public abstract class TwoRadiusSpell extends Spell {
  protected int radiusZX, radiusY;
  protected BoundingBox boundingBox;
  protected AABB aabb;

  @Deprecated
  public TwoRadiusSpell(SpellType.Cast type, ChatFormatting color, CostInstance costs, SpellType.Primary chargeType, int color1, int color2) {
    this(type, TextColor.fromLegacyFormat(color), costs, chargeType, color1, color2);
  }

  public TwoRadiusSpell(SpellType.Cast type, TextColor color, CostInstance costs, SpellType.Primary chargeType, int color1, int color2) {
    super(type, color, costs, chargeType, color1, color2);
  }

  public TwoRadiusSpell(Spell.Properties properties) {
    super(properties);
  }

  @Override
  public void buildProperties(List<PropertyHolder<?>> properties) {
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

  @Override
  public BoundingBox getBoundingBox() {
    if (boundingBox == null) {
      boundingBox = new BoundingBox(-getRadiusX() - 1, -getRadiusY() - 1, -getRadiusZ() - 1, getRadiusX(), getRadiusY(), getRadiusZ()).inflatedBy(1);
    }
    return boundingBox;
  }

  @Override
  public AABB getAABB(ISpellInstance iSpellInstance) {
    if (aabb == null) {
      BoundingBox box = getBoundingBox();
      if (box == null) {
        return null;
      }
      aabb = AABB.of(box);
    }
    return aabb;
  }
}
