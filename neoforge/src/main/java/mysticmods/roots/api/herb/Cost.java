package mysticmods.roots.api.herb;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.util.EnumUtil;
import net.minecraft.core.Holder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Cost {
  private final CostType type;
  private final Holder<Herb> herb;
  private final double value;

  protected Cost(CostType type, Holder<Herb> herb, double value) {
    this.type = type;
    this.herb = herb;
    this.value = value;
  }

  protected Cost(FriendlyByteBuf buf) {
    this.type = CostType.values()[buf.readVarInt()];
    final int id = buf.readVarInt();
    this.herb = RootsRegistries.HERBS.byIdOrThrow(id).builtInRegistryHolder();
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
      this.herb = RootsRegistries.HERBS.get(ResourceLocation.parse(GsonHelper.getAsString(pJsonObject, "herb"))).builtInRegistryHolder();
      this.value = GsonHelper.getAsDouble(pJsonObject, "value");
    }
  }

  public void toNetwork(FriendlyByteBuf buf) {
    buf.writeVarInt(this.type.ordinal());
    buf.writeVarInt(RootsRegistries.HERBS.getId(this.herb.value()));
    buf.writeDouble(this.value);
  }

  public JsonElement toJson() {
    JsonObject result = new JsonObject();
    result.addProperty("herb", herb.getKey().toString());
    result.addProperty("value", this.value);
    result.addProperty("type", this.type.toString().toLowerCase(Locale.ROOT));
    return result;
  }

  public CostType getType() {
    return type;
  }

  public Herb getHerb() {
    return herb.value();
  }

  public double getValue() {
    return value;
  }

  public static Cost add(Holder<Herb> herb, double value) {
    return new Cost(CostType.ADDITIVE, herb, value);
  }

  public static Cost mult(Holder<Herb> herb, double value) {
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
