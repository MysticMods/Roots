package mysticmods.roots.api.recipe.output;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

  @Nonnull
  public ItemStack getResult(RandomSource random) {
    if (random.nextFloat() < this.chance) {
      return this.output.copy();
    }

    return ItemStack.EMPTY;
  }

  public ItemStack getOutput() {
    return output;
  }

  public float getChance() {
    return chance;
  }

  public JsonElement toJson() {
    JsonObject result = new JsonObject();
    result.addProperty("chance", this.chance);
    JsonObject item = new JsonObject();
    item.addProperty("item", ForgeRegistries.ITEMS.getKey(this.output.getItem()).toString());
    item.addProperty("count", this.output.getCount());
    if (this.output.hasTag()) {
      item.addProperty("nbt", this.output.getTag().toString());
    }
    result.add("item", item);
    return result;
  }

  public void toNetwork(FriendlyByteBuf pBuffer) {
    pBuffer.writeItem(getOutput());
    pBuffer.writeFloat(getChance());
  }

  public static ConditionalOutput fromJson(JsonElement pJson) {
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

  public static ConditionalOutput fromNetwork(FriendlyByteBuf pBuffer) {
    return new ConditionalOutput(pBuffer.readItem(), pBuffer.readFloat());
  }

  public static List<ItemStack> getOutputs(List<ConditionalOutput> conditionalOutputs, RandomSource random) {
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
