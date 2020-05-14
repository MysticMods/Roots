package epicsquid.roots.modifiers.modifier;

import epicsquid.roots.api.Herb;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModItems;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public enum ModifierCores implements IModifierCore {
  GUNPOWDER(Items.GUNPOWDER),
  GLOWSTONE(Items.GLOWSTONE_DUST),
  REDSTONE(Items.REDSTONE),
  LAPIS(new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage())),
  RUNIC_DUST(ModItems.runic_dust),
  BLAZE_POWDER(Items.BLAZE_POWDER),

  PERESKIA(HerbRegistry.pereskia),
  WILDEWHEET(HerbRegistry.wildewheet),
  WILDROOT(HerbRegistry.wildroot),
  MOONGLOW_LEAF(HerbRegistry.moonglow_leaf),
  SPIRIT_HERB(HerbRegistry.spirit_herb),
  TERRA_MOSS(HerbRegistry.terra_moss),
  BAFFLE_CAP(HerbRegistry.baffle_cap),
  CLOUD_BERRY(HerbRegistry.cloud_berry),
  INFERNAL_BULB(HerbRegistry.infernal_bulb),
  STALICRIPE(HerbRegistry.stalicripe),
  DEWGONIA(HerbRegistry.dewgonia);

  private ItemStack stack;
  private final Herb herb;

  ModifierCores(Herb herb) {
    this.herb = herb;
    this.stack = ItemStack.EMPTY;
  }

  ModifierCores(ItemStack item) {
    this.stack = item;
    this.herb = null;
  }

  ModifierCores(Item item) {
    this(new ItemStack(item));
  }

  @Override
  public boolean isHerb() {
    return this.herb != null;
  }

  @Override
  @Nullable
  public Herb getHerb() {
    return this.herb;
  }

  @Override
  public ItemStack getStack() {
    if (this.herb != null) {
      if (this.stack.isEmpty()) {
        this.stack = new ItemStack(this.herb.getItem());
      }
      return this.stack;
    }
    return this.stack;
  }
}
