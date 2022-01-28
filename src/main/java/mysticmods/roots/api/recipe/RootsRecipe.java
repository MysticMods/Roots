package mysticmods.roots.api.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;
import noobanidus.libs.noobutil.block.entities.IReferentialBlockEntity;
import noobanidus.libs.noobutil.crafting.Crafting;
import noobanidus.libs.noobutil.ingredient.IngredientStack;
import noobanidus.libs.noobutil.processor.Processor;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class RootsRecipe<H extends IItemHandler, T extends TileEntity & IReferentialBlockEntity, W extends Crafting<H, T>> implements IRootsRecipe<H, T, W> {
  protected final NonNullList<IngredientStack> ingredients;
  protected final ItemStack result;
  protected final List<Processor<W>> processors = new ArrayList<>();
  protected final ResourceLocation recipeId;

  @FunctionalInterface
  public interface RootsRecipeBuilder<R extends RootsRecipe<?, ?, ?>> {
    R create(NonNullList<IngredientStack> ingredients, ItemStack result, ResourceLocation recipeId);
  }

  public RootsRecipe(NonNullList<IngredientStack> ingredients, ItemStack result, ResourceLocation recipeId) {
    this.ingredients = ingredients;
    this.result = result;
    this.recipeId = recipeId;
  }

  @Override
  public NonNullList<IngredientStack> getIngredientStacks() {
    return ingredients;
  }

  @Override
  public ItemStack getResultItem() {
    return result.copy();
  }

  @Override
  public ResourceLocation getId() {
    return recipeId;
  }

  @Override
  public List<Processor<W>> getProcessors() {
    return processors;
  }

  @Override
  public void addProcessor(Processor<W> processor) {
    this.processors.add(processor);
  }

  public abstract static class Serializer<H extends IItemHandler, T extends TileEntity & IReferentialBlockEntity, W extends Crafting<H, T>, R extends RootsRecipe<H, T, W>> extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<R> {

    private final RootsRecipeBuilder<R> builder;

    public Serializer(RootsRecipeBuilder<R> builder) {
      this.builder = builder;
    }

    public abstract List<Processor<W>> parseProcessors(JsonArray processors);

    public abstract Processor<W> getProcessor(ResourceLocation rl);

    @Override
    public R fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
      JsonArray incoming = JSONUtils.getAsJsonArray(pJson, "ingredients");
      NonNullList<IngredientStack> ingredients = NonNullList.withSize(incoming.size(), IngredientStack.EMPTY);
      for (int i = 0; i < incoming.size(); i++) {
        JsonElement element = incoming.get(i);
        if (element.isJsonObject()) {
          ingredients.set(i, IngredientStack.deserialize(element.getAsJsonObject()));
        } else {
          throw new JsonSyntaxException("Invalid ingredient at index " + i + " in recipe " + pRecipeId + ", ingredient " + element + " is not a JsonObject");
        }
      }
      ItemStack result;
      if (pJson.has("result")) {
        result = ShapedRecipe.itemFromJson(pJson.getAsJsonObject("result"));
      } else {
        ResourceLocation id = new ResourceLocation(JSONUtils.getAsString(pJson, "result"));
        Item item = ForgeRegistries.ITEMS.getValue(id);
        if (item == null) {
          throw new JsonSyntaxException("Unknown item '" + id + "'");
        }
        int count;
        if (!pJson.has("count")) {
          count = 1;
        } else {
          count = JSONUtils.getAsInt(pJson, "count");
        }
        result = new ItemStack(item, count);
      }


      R recipe = builder.create(ingredients, result, pRecipeId);

      if (pJson.has("processors")) {
        JsonArray jsonProcessors = JSONUtils.getAsJsonArray(pJson, "processors");
        for (Processor<W> proc : parseProcessors(jsonProcessors)) {
          recipe.addProcessor(proc);
        }
      }

      return recipe;
    }

    @Nullable
    @Override
    public R fromNetwork(ResourceLocation pRecipeId, PacketBuffer pBuffer) {
      int ingCount = pBuffer.readVarInt();
      NonNullList<IngredientStack> ingredients = NonNullList.withSize(ingCount, IngredientStack.EMPTY);
      for (int i = 0; i < ingCount; i++) {
        ingredients.set(i, IngredientStack.read(pBuffer));
      }

      ItemStack result = pBuffer.readItem();
      R recipe = builder.create(ingredients, result, pRecipeId);

      int procCount = pBuffer.readVarInt();
      for (int i = 0; i < procCount; i++) {
        ResourceLocation rl = pBuffer.readResourceLocation();
        Processor<W> processor = getProcessor(rl);
        if (processor != null) {
          recipe.addProcessor(processor);
        }
      }

      return recipe;
    }

    @Override
    public void toNetwork(PacketBuffer pBuffer, R recipe) {
      pBuffer.writeVarInt(recipe.getIngredientStacks().size());
      for (IngredientStack ingredient : recipe.getIngredientStacks()) {
        ingredient.write(pBuffer);
      }
      pBuffer.writeItem(recipe.getResultItem());
      pBuffer.writeVarInt(recipe.getProcessors().size());
      for (Processor<W> processor : recipe.getProcessors()) {
        pBuffer.writeResourceLocation(Objects.requireNonNull(processor.getRegistryName()));
      }
    }
  }
}
