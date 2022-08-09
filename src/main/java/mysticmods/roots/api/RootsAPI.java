package mysticmods.roots.api;

import mysticmods.roots.api.herbs.Herb;
import mysticmods.roots.api.modifier.Modifier;
import mysticmods.roots.api.property.ModifierProperty;
import mysticmods.roots.api.property.RitualProperty;
import mysticmods.roots.api.property.SpellProperty;
import mysticmods.roots.api.recipe.IRecipeManagerAccessor;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.spells.Spell;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.crafting.RecipeManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class RootsAPI {
  public static final String MODID = "roots";
  public static final String MOD_IDENTIFIERS = "Roots";
  public static Logger LOG = LogManager.getLogger();

  public static ResourceKey<Registry<Herb>> HERB_REGISTRY = key(new ResourceLocation(RootsAPI.MODID, "herbs"));
  public static ResourceKey<Registry<Ritual>> RITUAL_REGISTRY = key(new ResourceLocation(RootsAPI.MODID, "rituals"));
  public static ResourceKey<Registry<Spell>> SPELL_REGISTRY = key(new ResourceLocation(RootsAPI.MODID, "spells"));
  public static ResourceKey<Registry<Modifier>> MODIFIER_REGISTRY = key(new ResourceLocation(RootsAPI.MODID, "modifiers"));

  public static ResourceKey<Registry<RitualProperty<?>>> RITUAL_PROPERTY_REGISTRY = key(new ResourceLocation(RootsAPI.MODID, "ritual_properties"));
  public static ResourceKey<Registry<SpellProperty<?>>> SPELL_PROPERTY_REGISTRY = key(new ResourceLocation(RootsAPI.MODID, "spell_properties"));
  public static ResourceKey<Registry<ModifierProperty<?>>> MODIFIER_PROPERTY_REGISTRY = key(new ResourceLocation(RootsAPI.MODID, "modifier_properties"));

  public static final ResourceLocation HERB_CAPABILITY_ID = new ResourceLocation(MODID, "herb_capability");

  public static RootsAPI INSTANCE;

  public static RootsAPI getInstance() {
    return INSTANCE;
  }

  public static boolean isPresent() {
    return getInstance() != null;
  }

  public abstract IRecipeManagerAccessor getRecipeAccessor();

  public abstract void grant(ServerPlayer player, Grant grant);

  public RecipeManager getRecipeManager() {
    return getRecipeAccessor().getManager();
  }

  private static <T> ResourceKey<Registry<T>> key(ResourceLocation name) {
    return ResourceKey.createRegistryKey(name);
  }
}
