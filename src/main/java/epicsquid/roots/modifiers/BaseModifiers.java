package epicsquid.roots.modifiers;

import com.google.common.collect.Lists;
import epicsquid.roots.Roots;
import epicsquid.roots.api.Herb;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class BaseModifiers {
  public static IModifierCore AIR = new IModifierCore() {
    @Override
    public boolean isHerb() {
      return false;
    }

    @Nullable
    @Override
    public Herb getHerb() {
      return null;
    }

    @Override
    public ItemStack getStack() {
      return ItemStack.EMPTY;
    }

    @Override
    public String getTranslationKey() {
      return "roots.modifiers.cores.air";
    }

    @Override
    public String getFormatting() {
      return "";
    }

    @Override
    public boolean isBasic() {
      return true;
    }
  };

  public static Modifier NULL = new Modifier(new ResourceLocation(Roots.MODID, "null"), AIR, ModifierCost.noCost());

  public static Modifier SPEEDY = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "speedy"), ModifierCores.REDSTONE, ModifierCost.of(CostType.ALL_COST_MULTIPLIER, 0.05))); // Decreases the cooldownTotal or interval of the spell
  public static Modifier EMPOWER = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "empower"), ModifierCores.GUNPOWDER, ModifierCost.of(CostType.ALL_COST_MULTIPLIER, 0.10))); // Additionally increases the duration or damage of the spell if applicable
  public static Modifier REDUCTION = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "reduction"), ModifierCores.GLOWSTONE, ModifierCost.of(CostType.ALL_COST_MULTIPLIER, -0.05))); // Reduces the cost of the spell.

  public static Modifier GREATER_SPEEDY = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "greater_speedy"), ModifierCores.NETHER_WART, ModifierCost.of(CostType.ALL_COST_MULTIPLIER, 0.10))); // Decreases the cooldownTotal or interval of the spell again
  public static Modifier GREATER_EMPOWER = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "greater_empower"), ModifierCores.BLAZE_POWDER, ModifierCost.of(CostType.ALL_COST_MULTIPLIER, 0.05))); // Increases duration or damage of the spell if applicable.
  public static Modifier GREATER_REDUCTION = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "greater_reduction"), ModifierCores.RUNIC_DUST, ModifierCost.of(CostType.ALL_COST_MULTIPLIER, -0.10))); // Additionally reduces the cost of the spell
}
