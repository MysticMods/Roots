package epicsquid.roots.modifiers;

import epicsquid.roots.Roots;
import epicsquid.roots.modifiers.modifier.Modifier;
import epicsquid.roots.modifiers.modifier.ModifierCores;
import net.minecraft.util.ResourceLocation;

public class BaseModifiers {
  public static Modifier REDSTONE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "redstone"), ModifierType.ALL_COST_MULTIPLIER, ModifierCores.REDSTONE, 0.05));
  public static Modifier GLOWSTONE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "glowstone"), ModifierType.ALL_COST_MULTIPLIER, ModifierCores.GLOWSTONE, 0.05));
  public static Modifier GUNPOWDER = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "gunpowder"), ModifierType.ALL_COST_MULTIPLIER, ModifierCores.GUNPOWDER, 0.05));
  public static Modifier LAPIS = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "lapis"), ModifierType.ALL_COST_MULTIPLIER, ModifierCores.LAPIS, 0.05));
  public static Modifier RUNIC_DUST = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "runic_dust"), ModifierType.ALL_COST_MULTIPLIER, ModifierCores.RUNIC_DUST, 0.05));
  public static Modifier BLAZE_POWDER = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "blaze_powder"), ModifierType.ALL_COST_MULTIPLIER, ModifierCores.BLAZE_POWDER, 0.05));
}
