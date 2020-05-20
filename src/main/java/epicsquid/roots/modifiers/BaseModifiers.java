package epicsquid.roots.modifiers;

import epicsquid.roots.Roots;
import epicsquid.roots.modifiers.modifier.Modifier;
import epicsquid.roots.modifiers.modifier.ModifierCores;
import net.minecraft.util.ResourceLocation;

public class BaseModifiers {
  public static Modifier SPEEDY = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "speedy"), ModifierType.ALL_COST_MULTIPLIER, ModifierCores.REDSTONE, 0.05)); // Decreases the cooldownTotal or interval of the spell
  public static Modifier REDUCTION = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "reduction"), ModifierType.ALL_COST_MULTIPLIER, ModifierCores.GLOWSTONE, -0.05)); // Reduces the cost of the spell.
  public static Modifier GREATER_EMPOWER = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "greater_empower"), ModifierType.ALL_COST_MULTIPLIER, ModifierCores.GUNPOWDER, 0.10)); // Additionally increases the duration or damage of the spell if applicable
  public static Modifier GREATER_REDUCTION = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "greater_reduction"), ModifierType.ALL_COST_MULTIPLIER, ModifierCores.LAPIS, -0.10)); // Additionally reduces the cost of the spell
  public static Modifier GREATER_SPEEDY = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "greater_speedy"), ModifierType.ALL_COST_MULTIPLIER, ModifierCores.RUNIC_DUST, 0.10)); // Decreases the cooldownTotal or interval of the spell again
  public static Modifier EMPOWER = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "empower"), ModifierType.ALL_COST_MULTIPLIER, ModifierCores.BLAZE_POWDER, 0.05)); // Increases duration or damage of the spell if applicable.
}
