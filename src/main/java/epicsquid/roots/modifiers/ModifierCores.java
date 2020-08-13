package epicsquid.roots.modifiers;

import epicsquid.roots.Roots;
import epicsquid.roots.api.Herb;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModItems;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public enum ModifierCores implements IModifierCore {
  GUNPOWDER(Items.GUNPOWDER, ""),
  GLOWSTONE(Items.GLOWSTONE_DUST, ""),
  REDSTONE(Items.REDSTONE, ""),
  NETHER_WART(Items.NETHER_WART, ""),
  RUNIC_DUST(() -> ModItems.runic_dust, ""),
  BLAZE_POWDER(Items.BLAZE_POWDER, ""),

  PERESKIA(new ResourceLocation(Roots.MODID, "pereskia"), TextFormatting.BOLD + "" + TextFormatting.LIGHT_PURPLE),
  WILDEWHEET(new ResourceLocation(Roots.MODID, "wildewheet"), TextFormatting.GOLD + "" + TextFormatting.BOLD),
  WILDROOT(new ResourceLocation(Roots.MODID, "wildroot"), TextFormatting.YELLOW + ""),
  MOONGLOW_LEAF(new ResourceLocation(Roots.MODID, "moonglow_leaf"), TextFormatting.DARK_PURPLE + ""),
  SPIRIT_HERB(new ResourceLocation(Roots.MODID, "spirit_herb"), TextFormatting.GREEN + "" + TextFormatting.BOLD),
  TERRA_MOSS(new ResourceLocation(Roots.MODID, "terra_moss"), TextFormatting.DARK_GREEN + "" + TextFormatting.BOLD),
  BAFFLE_CAP(new ResourceLocation(Roots.MODID, "baffle_cap"), TextFormatting.DARK_PURPLE + "" + TextFormatting.BOLD),
  CLOUD_BERRY(new ResourceLocation(Roots.MODID, "cloud_berry"), TextFormatting.AQUA + "" + TextFormatting.BOLD),
  INFERNAL_BULB(new ResourceLocation(Roots.MODID, "infernal_bulb"), TextFormatting.RED + "" + TextFormatting.BOLD),
  STALICRIPE(new ResourceLocation(Roots.MODID, "stalicripe"), TextFormatting.DARK_RED + "" + TextFormatting.BOLD),
  DEWGONIA(new ResourceLocation(Roots.MODID, "dewgonia"), TextFormatting.DARK_AQUA + "" + TextFormatting.BOLD);

  private final Supplier<ItemStack> stack;
  private final Supplier<Herb> herb;
  private final String formatting;

  ModifierCores(ResourceLocation herb, String formatting) {
    this.herb = () -> HerbRegistry.getHerb(herb);
    this.stack = () -> ItemStack.EMPTY;
    this.formatting = formatting;
  }

  ModifierCores(ItemStack item, String formatting) {
    this.stack = () -> item;
    this.herb = null;
    this.formatting = formatting;
  }

  ModifierCores(Item item, String formatting) {
    this(new ItemStack(item), formatting);
  }

  ModifierCores(Supplier<Item> item, String formatting) {
    this.stack = () -> new ItemStack(item.get());
    this.herb = null;
    this.formatting = formatting;
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

  @Override
  public String getTranslationKey() {
    return "roots.modifiers.cores." + name().toLowerCase();
  }

  @Override
  public String getFormatting() {
    return formatting;
  }

  @Override
  public boolean isBasic() {
    return this == GUNPOWDER || this == NETHER_WART || this == REDSTONE || this == BLAZE_POWDER || this == GLOWSTONE || this == RUNIC_DUST;
  }

  @Override
  public int getKey() {
    return ordinal();
  }

  @Nullable
  public static IModifierCore getByOrdinal (int ordinal) {
    if (ordinal == -1) {
      return BaseModifiers.AIR;
    }
    for (ModifierCores core : values()) {
      if (core.ordinal() == ordinal) {
        return core;
      }
    }
    return null;
  }
}
