package mysticmods.roots.api.recipe.type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.recipe.IRootsRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class ResolvingRecipeType<V, C extends RecipeInput, T extends Recipe<C> & IRootsRecipe<C>> extends SimpleJsonResourceReloadListener {
  protected static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
  protected final Supplier<RecipeType<T>> type;
  protected List<RecipeHolder<T>> cache = null;
  protected final Comparator<? super RecipeHolder<T>> comparator;
  protected final Object2IntOpenHashMap<ResourceLocation> reverseLookup = new Object2IntOpenHashMap<>();
  protected final Function<T, @org.jetbrains.annotations.Nullable V> resolver;
  private RecipeHolder<T> lastRecipe = null;

  public ResolvingRecipeType(Supplier<RecipeType<T>> type, Comparator<? super RecipeHolder<T>> comparator, Function<T, @org.jetbrains.annotations.Nullable V> resolver) {
    super(GSON, "recipes");
    this.type = type;
    this.comparator = comparator;
    this.resolver = resolver;
  }

  protected List<RecipeHolder<T>> getRecipesList() {
    RecipeManager manager = RootsAPI.getInstance().getRecipeManager();
    if (manager == null) {
      return Collections.emptyList();
    }
    return manager.getAllRecipesFor(type.get());
  }

  public List<RecipeHolder<T>> getRecipes() {
    if (cache == null) {
      cache = getRecipesList();
    }
    if (cache != null) {
      try {
        cache.sort(comparator);
      } catch (UnsupportedOperationException exception) {
        cache = new ArrayList<>(cache);
        cache.sort(comparator);
      }
      reverseLookup.clear();
      for (int i = 0; i < cache.size(); i++) {
        reverseLookup.put(cache.get(i).id(), i);
      }
      return cache;
    } else {
      return Collections.emptyList();
    }
  }

  @Nullable
  public RecipeHolder<T> getRecipe(ResourceLocation location) {
    int index = lookup(location);
    if (index == -1) {
      return null;
    }
    return getRecipe(index);
  }

  public int size() {
    return getRecipes().size();
  }

  public RecipeHolder<T> getRecipe(int index) {
    if (index < 0 || index >= getRecipes().size()) {
      throw new RuntimeException("Index " + index + " not in valid range for recipe type " + type + " [0," + getRecipes().size() + ")");
    }

    return getRecipes().get(index);
  }

  public boolean hasRecipe(int index) {
    return index < getRecipes().size();
  }

  @Override
  protected void apply(Map<ResourceLocation, JsonElement> pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
    reset();
  }

  public void reset() {
    cache = null;
    lastRecipe = null;
    reverseLookup.clear();
  }

  public int lookup(ResourceLocation recipeId) {
    getRecipes();
    return reverseLookup.getOrDefault(recipeId, -1);
  }

  @Nullable
  public RecipeHolder<T> findRecipe(C inventory, Level level) {
    if (lastRecipe != null && !lastRecipe.value().isDynamic() && lastRecipe.value().getPriority() >= 0 && lastRecipe.value().matches(inventory, level)) {
      return lastRecipe;
    }
    for (RecipeHolder<T> recipe : getRecipes()) {
      if (recipe.value().matches(inventory, level)) {
        lastRecipe = recipe;
        return recipe;
      }
    }

    return null;
  }

  @Nullable
  public RecipeHolder<T> findRecipe(V output) {
    if (resolver == null) {
      return null;
    }

    if (lastRecipe != null) {
      V lastValue = resolver.apply(lastRecipe.value());
      if (lastValue != null && lastValue.equals(output)) {
        return lastRecipe;
      }
    }

    for (RecipeHolder<T> recipe : getRecipes()) {
      V value = resolver.apply(recipe.value());
      if (value != null && value.equals(output)) {
        lastRecipe = recipe;
        return recipe;
      }
    }

    return null;
  }
}
