package epicsquid.roots.modifiers;

import epicsquid.roots.Roots;
import epicsquid.roots.api.Herb;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.util.types.IRegistryItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.NotImplementedException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ModifierCores implements IModifierCore {
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

  private static Set<Item> modifierCores = null;

  private final Supplier<ItemStack> stack;
  private final Supplier<Herb> herb;
  private final String formatting;
  private final ResourceLocation registryName;
  private String cachedName = null;

  ModifierCores(ResourceLocation herb, String formatting) {
    this.herb = () -> HerbRegistry.getHerb(herb);
    this.stack = () -> ItemStack.EMPTY;
    this.formatting = formatting;
    this.registryName = herb;
  }

  @Override
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
  public int getKey() {
    return ordinal();
  }

  @Nullable
  public static IModifierCore fromOrdinal(int ordinal) {
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

  public static boolean isModifierCore(Item item) {
    if (modifierCores == null) {
      modifierCores = Stream.of(values()).map(ModifierCores::getStack).map(ItemStack::getItem).collect(Collectors.toSet());
    }
    return modifierCores.contains(item);
  }

  public static boolean isModifierCore(ItemStack stack) {
    return isModifierCore(stack.getItem());
  }

  @Nullable
  public static ModifierCores fromHerb(Herb herb) {
    for (ModifierCores core : values()) {
      if (core.getHerb() == herb) {
        return core;
      }
    }
    return null;
  }

  @Nullable
  public static ModifierCores fromItemStack (ItemStack stack) {
    if (!isModifierCore(stack)) {
      return null;
    }
    for (ModifierCores core : values()) {
      if (core.getHerb().getItem() == stack.getItem()) {
        return core;
      }
    }
    return null;
  }

  @Override
  public void setRegistryName(ResourceLocation name) {
    throw new NotImplementedException("setRegistryName not implemented for this");
  }

  @Nonnull
  @Override
  public ResourceLocation getRegistryName() {
    return registryName;
  }

  @Nonnull
  @Override
  public String getCachedName() {
    if (cachedName == null) {
      cachedName = this.registryName.toString();
    }

    return cachedName;
  }
}
