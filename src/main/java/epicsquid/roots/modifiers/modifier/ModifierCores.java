package epicsquid.roots.modifiers.modifier;

import epicsquid.roots.Roots;
import epicsquid.roots.api.Herb;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModItems;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public enum ModifierCores implements IModifierCore {
  GUNPOWDER(Items.GUNPOWDER),
  GLOWSTONE(Items.GLOWSTONE_DUST),
  REDSTONE(Items.REDSTONE),
  LAPIS(new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage())),
  RUNIC_DUST(() -> ModItems.runic_dust),
  BLAZE_POWDER(Items.BLAZE_POWDER),

  PERESKIA(new ResourceLocation(Roots.MODID, "pereskia")),
  WILDEWHEET(new ResourceLocation(Roots.MODID, "wildewheet")),
  WILDROOT(new ResourceLocation(Roots.MODID, "wildroot")),
  MOONGLOW_LEAF(new ResourceLocation(Roots.MODID, "moonglow_leaf")),
  SPIRIT_HERB(new ResourceLocation(Roots.MODID, "spirit_herb")),
  TERRA_MOSS(new ResourceLocation(Roots.MODID, "terra_moss")),
  BAFFLE_CAP(new ResourceLocation(Roots.MODID, "baffle_cap")),
  CLOUD_BERRY(new ResourceLocation(Roots.MODID, "cloud_berry")),
  INFERNAL_BULB(new ResourceLocation(Roots.MODID, "infernal_bulb")),
  STALICRIPE(new ResourceLocation(Roots.MODID, "stalicripe")),
  DEWGONIA(new ResourceLocation(Roots.MODID, "dewgonia"));

  private final Supplier<ItemStack> stack;
  private final Supplier<Herb> herb;

  ModifierCores(ResourceLocation herb) {
    this.herb = () -> HerbRegistry.getHerb(herb);
    this.stack = () -> ItemStack.EMPTY;
  }

  ModifierCores(ItemStack item) {
    this.stack = () -> item;
    this.herb = null;
  }

  ModifierCores(Item item) {
    this(new ItemStack(item));
  }

  ModifierCores(Supplier<Item> item) {
    this.stack = () -> new ItemStack(item.get());
    this.herb = null;
  }

  @Override
  public boolean isHerb() {
    return this.herb != null;
  }

  @Override
  @Nullable
  public Herb getHerb() {
    return this.herb.get();
  }

  @Override
  public ItemStack getStack() {
    ItemStack sup = stack.get();
    if (this.herb != null) {
      if (sup.isEmpty()) {
        sup = new ItemStack(this.herb.get().getItem());
      }
      return sup;
    }
    return sup;
  }
}
