package mysticmods.roots.api.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import mysticmods.roots.api.RootsAPI;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import noobanidus.libs.noobutil.util.EnumUtil;

import java.util.function.Consumer;

public class Grant implements Consumer<ServerPlayer> {
  private final GrantType type;
  private final ResourceLocation id;

  public Grant(GrantType type, ResourceLocation id) {
    this.type = type;
    this.id = id;
  }

  public GrantType getType() {
    return type;
  }

  public ResourceLocation getId() {
    return id;
  }

  public JsonElement toJson() {
    JsonObject result = new JsonObject();
    result.addProperty("id", id.toString());
    result.addProperty("type", type.toString());
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
      GrantType type = EnumUtil.fromString(GrantType.class, GsonHelper.getAsString(pJsonObject, "type"));
      return new Grant(type, new ResourceLocation(GsonHelper.getAsString(pJsonObject, "id")));
    }
  }

  public static Grant fromNetwork(FriendlyByteBuf pBuffer) {
    return new Grant(EnumUtil.fromOrdinal(GrantType.class, pBuffer.readVarInt()), pBuffer.readResourceLocation());
  }

  public enum GrantType {
    SPELL,
    MODIFIER
  }
}
