package epicsquid.mysticallib.recipe;

import javax.annotation.Nonnull;

import epicsquid.mysticallib.LibRegistry;
import epicsquid.mysticallib.event.RegisterModRecipesEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;

public class RecipeRegistry {

  public static ResourceLocation getRL(@Nonnull String s) {
    return new ResourceLocation(LibRegistry.getActiveModid() + ":" + s);
  }

  public static void registerShaped(@Nonnull IForgeRegistry<IRecipe> registry, @Nonnull String name, @Nonnull ItemStack result,
      @Nonnull Object... ingredients) {
    registry.register(new ShapedOreRecipe(getRL(name), result, ingredients).setRegistryName(getRL(name)));
  }

  public static void registerShapedMirrored(@Nonnull IForgeRegistry<IRecipe> registry, @Nonnull String name, @Nonnull ItemStack result,
      @Nonnull Object... ingredients) {
    registry.register(new ShapedOreRecipe(getRL(name), result, ingredients).setMirrored(true).setRegistryName(getRL(name)));
  }

  public static void registerShapeless(@Nonnull IForgeRegistry<IRecipe> registry, @Nonnull String name, @Nonnull ItemStack result,
      @Nonnull Object... ingredients) {
    registry.register(new ShapelessOreRecipe(getRL(name), result, ingredients).setRegistryName(getRL(name)));
  }

  @SubscribeEvent
  public void onRegisterRecipes(@Nonnull RegistryEvent.Register<IRecipe> event) {
    MinecraftForge.EVENT_BUS.post(new RegisterModRecipesEvent(event.getRegistry()));
  }
}
