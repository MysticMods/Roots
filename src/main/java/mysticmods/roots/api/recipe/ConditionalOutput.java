package mysticmods.roots.api.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.NBTIngredient;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ConditionalOutput {
  private final ItemStack output;
  private final float chance;

  public ConditionalOutput(ItemStack output, float chance) {
    this.output = output;
    this.chance = chance;
    if (this.chance > 1) {
      throw new IllegalArgumentException("Invalid chance for a conditional output: " + this.chance);
    }
  }

  @Nullable
  public ItemStack getResult (Random random) {
    if (random.nextFloat() < this.chance) {
      return this.output.copy();
    }

    // TODO: Null or empty?
    //return ItemStack.EMPTY;
    return null;
  }

  public ItemStack getOutput() {
    return output;
  }

  public float getChance() {
    return chance;
  }

  public JsonElement toJson () {
    JsonObject result = new JsonObject();
    result.addProperty("chance", this.chance);
    JsonObject item = new JsonObject();
    item.addProperty("item", this.output.getItem().getRegistryName().toString());
    item.addProperty("count", this.output.getCount());
    if (this.output.hasTag()) {
      item.addProperty("nbt", this.output.getTag().toString());
    }
    result.add("item", item);
    return result;
  }

  public void toNetwork (FriendlyByteBuf pBuffer) {
    pBuffer.writeItem(getOutput());
    pBuffer.writeFloat(getChance());
  }

  public static ConditionalOutput fromJson (JsonElement pJson) {
    if (pJson != null && !pJson.isJsonNull()) {
      if (pJson.isJsonObject()) {
        JsonObject pJsonObject = pJson.getAsJsonObject();
        if (!pJsonObject.get("item").isJsonObject()) {
          throw new JsonSyntaxException("Conditional output item must be an object");
        }
        if (pJsonObject.get("chance").isJsonNull()) {
          throw new JsonSyntaxException("Conditional output must have a chance");
        }
        return new ConditionalOutput(CraftingHelper.getItemStack(pJsonObject.getAsJsonObject("item"), true, true), pJsonObject.get("chance").getAsFloat());
      } else {
        throw new JsonSyntaxException("Expected conditional output to be object");
      }
    } else {
      throw new JsonSyntaxException("Conditional output cannot be null");
    }
  }

  public static ConditionalOutput fromNetwork (FriendlyByteBuf pBuffer) {
    return new ConditionalOutput(pBuffer.readItem(), pBuffer.readFloat());
  }

  public static List<ItemStack> getOutputs (List<ConditionalOutput> conditionalOutputs, Random random) {
    List<ItemStack> result = new ArrayList<>();
    for (ConditionalOutput output : conditionalOutputs) {
      ItemStack thisResult = output.getResult(random);
      if (thisResult != null) {
        result.add(thisResult);
      }
    }
    return result;
  }
}
