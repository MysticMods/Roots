package mysticmods.roots.api.herb;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import mysticmods.roots.api.registry.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import noobanidus.libs.noobutil.type.LazySupplier;
import noobanidus.libs.noobutil.util.EnumUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

public class Cost {
  private final CostType type;
  private final Supplier<Herb> herb;
  private final double value;

  protected Cost(CostType type, Supplier<Herb> herb, double value) {
    this.type = type;
    this.herb = new LazySupplier<>(herb);
    this.value = value;
  }

  protected Cost(FriendlyByteBuf buf) {
    this.type = CostType.values()[buf.readVarInt()];
    final int id = buf.readVarInt();
    this.herb = () -> Registries.HERB_REGISTRY.get().getValue(id);
    this.value = buf.readDouble();
  }

  protected Cost(JsonElement pJson) {
    if (pJson == null || pJson.isJsonNull()) {
      throw new JsonSyntaxException("Cost cannot be null");
    } else if (!pJson.isJsonObject()) {
      throw new JsonSyntaxException("Cost must be an object");
    } else {
      JsonObject pJsonObject = pJson.getAsJsonObject();
      if (pJsonObject.get("type").isJsonNull()) {
        throw new JsonSyntaxException("Cost must have a type");
      }
      if (pJsonObject.get("herb").isJsonNull()) {
        throw new JsonSyntaxException("Cost must have a herb");
      }
      this.type = EnumUtil.fromString(CostType.class, GsonHelper.getAsString(pJsonObject, "type"));
      this.herb = () -> Registries.HERB_REGISTRY.get().getValue(new ResourceLocation(GsonHelper.getAsString(pJsonObject, "herb")));
      this.value = GsonHelper.getAsDouble(pJsonObject, "value");
    }
  }

  public void toNetwork(FriendlyByteBuf buf) {
    buf.writeVarInt(this.type.ordinal());
    buf.writeVarInt(Registries.HERB_REGISTRY.get().getID(this.herb.get()));
    buf.writeDouble(this.value);
  }

  public JsonElement toJson() {
    JsonObject result = new JsonObject();
    result.addProperty("herb", Registries.HERB_REGISTRY.get().getKey(this.herb.get()).toString());
    result.addProperty("value", this.value);
    result.addProperty("type", this.type.toString().toLowerCase(Locale.ROOT));
    return result;
  }

  public CostType getType() {
    return type;
  }

  public Herb getHerb() {
    return herb.get();
  }

  public double getValue() {
    return value;
  }

  public static Cost add(Supplier<Herb> herb, double value) {
    return new Cost(CostType.ADDITIVE, herb, value);
  }

  public static Cost mult(Supplier<Herb> herb, double value) {
    return new Cost(CostType.MULTIPLICATIVE, herb, value);
  }

  public static Cost fromNetwork(FriendlyByteBuf buf) {
    return new Cost(buf);
  }

  public static Cost fromJson(JsonElement pJson) {
    return new Cost(pJson);
  }

  public static List<Cost> fromNetworkArray(FriendlyByteBuf buf) {
    List<Cost> costs = new ArrayList<>();
    int count = buf.readVarInt();
    for (int i = 0; i < count; i++) {
      costs.add(Cost.fromNetwork(buf));
    }
    return costs;
  }

  public static List<Cost> fromJsonArray(JsonElement pJson) {
    if (pJson.isJsonNull() || !pJson.isJsonArray() && !pJson.isJsonObject()) {
      throw new JsonSyntaxException("Costs must be an array or object");
    }
    List<Cost> result = new ArrayList<>();
    JsonArray costs;
    if (pJson.isJsonObject()) {
      costs = GsonHelper.getAsJsonArray(pJson.getAsJsonObject(), "costs");
    } else {
      costs = pJson.getAsJsonArray();
    }
    for (JsonElement element : costs) {
      result.add(Cost.fromJson(element));
    }
    return result;
  }

  public enum CostType {
    ADDITIVE,
    MULTIPLICATIVE
  }
}
