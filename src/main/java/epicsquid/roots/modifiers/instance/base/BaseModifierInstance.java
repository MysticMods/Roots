package epicsquid.roots.modifiers.instance.base;

import epicsquid.roots.api.Herb;
import epicsquid.roots.modifiers.*;
import epicsquid.roots.properties.Property;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.util.types.RegistryItem;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class BaseModifierInstance extends RegistryItem implements INBTSerializable<NBTTagCompound>, IModifier {
  protected Modifier modifier;
  protected boolean applied;

  protected BaseModifierInstance(Modifier modifier, boolean applied) {
    this.modifier = modifier;
    this.applied = applied;
  }

  protected BaseModifierInstance() {
    this.modifier = null;
    this.applied = false;
  }

  @Override
  public boolean isDisabled() {
    return modifier != null && modifier.isDisabled();
  }

  @Override
  public Modifier getModifier() {
    return modifier;
  }

  public boolean isApplied() {
    return applied && !isDisabled();
  }

  public void setApplied() {
    this.applied = true;
  }

  @Override
  public String getIdentifier() {
    return modifier.getIdentifier();
  }

  @Override
  public String getTranslationKey() {
    return modifier.getTranslationKey();
  }

  @Override
  public String getFormatting() {
    return modifier.getFormatting();
  }

  @SideOnly(Side.CLIENT)
  public String describe() {
    if (GuiScreen.isShiftKeyDown()) {
      return getFormatting() + I18n.format(getTranslationKey()) + TextFormatting.GRAY + ": " + I18n.format(getTranslationKey() + ".desc");
    } else {
      return getFormatting() + I18n.format(getTranslationKey()) + TextFormatting.RESET;
    }
  }

  @SideOnly(Side.CLIENT)
  public String describeName() {
    return getFormatting() + I18n.format(getTranslationKey()) + TextFormatting.GRAY + ":";
  }

  @SideOnly(Side.CLIENT)
  public String describeFunction() {
    return "- " + I18n.format(getTranslationKey() + ".desc");
  }

  @SideOnly(Side.CLIENT)
  public List<String> describeCost() {
    // TODO: Sort by type
    List<String> result = new ArrayList<>();
    for (IModifierCost cost : modifier.getCosts().values()) {
      switch (cost.getCost()) {
        case NO_COST:
          result.add(I18n.format("roots.tooltip.modifier.no_cost"));
          break;
        case ADDITIONAL_COST:
          if (cost.getHerb() == null) {
            throw new NullPointerException("Additional herb modifier cost type but no herb specified.");
          }
          result.add(I18n.format("roots.tooltip.modifier.additional_cost", I18n.format(cost.getHerb().getStack().getTranslationKey() + ".name"), String.format("%.4f", cost.getValue())));
          break;
        case ALL_COST_MULTIPLIER:
          if (cost.getValue() < 0) {
            result.add(I18n.format("roots.tooltip.modifier.decreased_cost", Math.floor(cost.getValue() * 100) + "%"));
          } else {
            result.add(I18n.format("roots.tooltip.modifier.increased_cost", Math.floor(cost.getValue() * 100) + "%"));
          }
          break;
        case SPECIFIC_COST_ADJUSTMENT:
          if (cost.getValue() < 0) {
            result.add(I18n.format("roots.tooltip.modifier.specific_decreased_cost", cost.getHerb().getName(), Math.floor(cost.getValue() * 100) + "%"));
          } else {
            result.add(I18n.format("roots.tooltip.modifier.specific_increased_cost", cost.getHerb().getName(), Math.floor(cost.getValue() * 100) + "%"));
          }
          break;
        case SPECIFIC_COST_MULTIPLIER:
          if (cost.getValue() < 0) {
            result.add(I18n.format("roots.tooltip.modifier.specific_decreased_cost", cost.getHerb().getName(), Math.floor(cost.getValue() * 100) + "%"));
          } else {
            result.add(I18n.format("roots.tooltip.modifier.specific_increased_cost", cost.getHerb().getName(), Math.floor(cost.getValue() * 100) + "%"));
          }
          break;
        default:
      }
    }
    return result;
  }

  @Override
  public IModifierCore getCore() {
    if (modifier == null) {
      return BaseModifiers.AIR;
    } else {
      return modifier.getCore();
    }
  }

  @Override
  public NBTTagCompound serializeNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    try {
      tag.setString("m", modifier.getRegistryName().toString());
    } catch (NullPointerException e) {
    }
    tag.setBoolean("a", applied);
    return tag;
  }

  @Override
  public void deserializeNBT(NBTTagCompound tag) {
    this.modifier = ModifierRegistry.get(new ResourceLocation(tag.getString("m")));
    this.applied = tag.getBoolean("a");
  }

  @Override
  public abstract Object2DoubleOpenHashMap<Herb> apply(Object2DoubleOpenHashMap<Herb> costs, CostType phase);

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    BaseModifierInstance that = (BaseModifierInstance) o;
    return applied == that.applied &&
        modifier.equals(that.modifier);
  }

  @Override
  public int hashCode() {
    return Objects.hash(modifier, applied);
  }

  @Nonnull
  @Override
  public ResourceLocation getRegistryName() {
    return modifier.getRegistryName();
  }

  @Override
  public List<Property<SpellBase.ModifierCost>> asProperties() {
    return Collections.emptyList();
  }
}
