package mysticmods.roots.api.capability;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import mysticmods.roots.api.RootsAPI;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import noobanidus.libs.noobutil.util.EnumUtil;

import java.util.Locale;
import java.util.function.Consumer;

public class Grant implements Consumer<ServerPlayer> {
  private final Type type;
  private final ResourceLocation id;

  public Grant(Type type, ResourceLocation id) {
    this.type = type;
    this.id = id;
  }

  public Type getType() {
    return type;
  }

  public ResourceLocation getId() {
    return id;
  }

  public JsonElement toJson() {
    JsonObject result = new JsonObject();
    result.addProperty("id", id.toString());
    result.addProperty("type", type.toString().toLowerCase(Locale.ROOT));
    return result;
  }

  public void toNetwork(FriendlyByteBuf pBuffer) {
    pBuffer.writeResourceLocation(id);
    pBuffer.writeVarInt(type.ordinal());
  }

  @Override
  public void accept(ServerPlayer serverPlayer) {
    RootsAPI.getInstance().grant(serverPlayer, this);
  }

  public static Grant fromJson(JsonElement pJson) {
    if (pJson == null || pJson.isJsonNull()) {
      throw new JsonSyntaxException("Grant cannot be null");
    } else if (!pJson.isJsonObject()) {
      throw new JsonSyntaxException("Grant must be an object");
    } else {
      JsonObject pJsonObject = pJson.getAsJsonObject();
      if (pJsonObject.get("type").isJsonNull()) {
        throw new JsonSyntaxException("Grant must have a type");
      }
      if (pJsonObject.get("id").isJsonNull()) {
        throw new JsonSyntaxException("Grant must have an id");
      }
      Type type = EnumUtil.fromString(Type.class, GsonHelper.getAsString(pJsonObject, "type"));
      return new Grant(type, new ResourceLocation(GsonHelper.getAsString(pJsonObject, "id")));
    }
  }

  public static Grant fromNetwork(FriendlyByteBuf pBuffer) {
    return new Grant(EnumUtil.fromOrdinal(Type.class, pBuffer.readVarInt()), pBuffer.readResourceLocation());
  }

  public enum Type {
    SPELL,
    MODIFIER
  }
}
