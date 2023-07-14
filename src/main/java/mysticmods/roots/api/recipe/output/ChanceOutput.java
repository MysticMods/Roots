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

public class ChanceOutput {
  private final ItemStack output;
  private final float chance;

  public ChanceOutput(ItemStack output, float chance) {
    this.output = output;
    this.chance = chance;
    if (this.chance > 1) {
      throw new IllegalArgumentException("Invalid chance for a chance output: " + this.chance);
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

  public static ChanceOutput fromJson(JsonElement pJson) {
    if (pJson != null && !pJson.isJsonNull()) {
      if (pJson.isJsonObject()) {
        JsonObject pJsonObject = pJson.getAsJsonObject();
        if (!pJsonObject.get("item").isJsonObject()) {
          throw new JsonSyntaxException("Chance output item must be an object");
        }
        if (pJsonObject.get("chance").isJsonNull()) {
          throw new JsonSyntaxException("Chance output must have a chance");
        }
        return new ChanceOutput(CraftingHelper.getItemStack(pJsonObject.getAsJsonObject("item"), true, true), pJsonObject.get("chance").getAsFloat());
      } else {
        throw new JsonSyntaxException("Expected chance output to be object");
      }
    } else {
      throw new JsonSyntaxException("Chance output cannot be null");
    }
  }

  public static ChanceOutput fromNetwork(FriendlyByteBuf pBuffer) {
    return new ChanceOutput(pBuffer.readItem(), pBuffer.readFloat());
  }

  public static List<ItemStack> getOutputs(List<ChanceOutput> chanceOptions, RandomSource random) {
    List<ItemStack> result = new ArrayList<>();
    for (ChanceOutput output : chanceOptions) {
      ItemStack thisResult = output.getResult(random);
      if (thisResult != null) {
        result.add(thisResult);
      }
    }
    return result;
  }
}
