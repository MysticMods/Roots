package epicsquid.roots.modifiers.instance.base;

import epicsquid.roots.api.Herb;
import epicsquid.roots.modifiers.IModifier;
import epicsquid.roots.modifiers.ModifierRegistry;
import epicsquid.roots.modifiers.ModifierType;
import epicsquid.roots.modifiers.modifier.IModifierCore;
import epicsquid.roots.modifiers.modifier.Modifier;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Objects;

public class BaseModifierInstance implements INBTSerializable<NBTTagCompound>, IModifier {
  protected Modifier modifier;
  protected boolean applied;

  public BaseModifierInstance(Modifier modifier, boolean applied) {
    this.modifier = modifier;
    this.applied = applied;
  }

  public BaseModifierInstance() {
    this.modifier = null;
    this.applied = false;
  }

  public Modifier getModifier() {
    return modifier;
  }

  public boolean isApplied() {
    return applied;
  }

  public void setApplied() {
    this.applied = true;
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
    if (isBasic()) {
      return I18n.format(getTranslationKey());
    }
    if (GuiScreen.isShiftKeyDown()) {
      return getFormatting() + I18n.format(getTranslationKey()) + TextFormatting.GRAY + ": " + I18n.format(getTranslationKey() + ".desc");
    } else {
      return getFormatting() + I18n.format(getTranslationKey()) + TextFormatting.RESET;
    }
  }

  @SideOnly(Side.CLIENT)
  public String description() {
    return getFormatting() + I18n.format(getTranslationKey()) + TextFormatting.GRAY + ": " + I18n.format(getTranslationKey() + ".desc");
  }

  @SideOnly(Side.CLIENT)
  public String describeCost() {
    switch (modifier.getType()) {
      case NO_COST:
        return I18n.format("roots.tooltip.modifier.no_cost");
      case ADDITIONAL_COST:
        return I18n.format("roots.tooltip.modifier.additional_cost", I18n.format(getStack().getTranslationKey() + ".name"), String.format("%.4f", modifier.getValue()));
      case ALL_COST_MULTIPLIER:
        if (modifier.getValue() < 0) {
          return I18n.format("roots.tooltip.modifier.decreased_cost", Math.floor(modifier.getValue() * 100) + "%");
        } else {
          return I18n.format("roots.tooltip.modifier.increased_cost", Math.floor(modifier.getValue() * 100) + "%");
        }
      default:
        return "";
    }
  }

  @Override
  public ItemStack getStack() {
    return modifier.getStack();
  }

  @Override
  public ModifierType getType() {
    return modifier.getType();
  }

  @Override
  public double getValue() {
    return modifier.getValue();
  }

  @Override
  public IModifierCore getCore() {
    return modifier.getCore();
  }

  @Override
  public boolean isBasic() {
    return modifier.isBasic();
  }

  @Override
  public NBTTagCompound serializeNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString("m", modifier.getRegistryName().toString());
    tag.setBoolean("a", applied);
    return tag;
  }

  @Override
  public void deserializeNBT(NBTTagCompound tag) {
    this.modifier = ModifierRegistry.get(new ResourceLocation(tag.getString("m")));
    this.applied = tag.getBoolean("a");
  }

  // TODO
  public static BaseModifierInstance fromNBT(NBTTagCompound tag) {
    BaseModifierInstance result = new BaseModifierInstance();
    result.deserializeNBT(tag);
    return result;
  }

  @Override
  public Object2DoubleOpenHashMap<Herb> apply(Object2DoubleOpenHashMap<Herb> costs) {
    return costs;
  }

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
}
