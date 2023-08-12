package mysticmods.roots.api.capability;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.modifier.Modifier;
import mysticmods.roots.api.registry.Registries;
import mysticmods.roots.api.spell.Spell;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import noobanidus.libs.noobutil.util.EnumUtil;

import java.util.Locale;

public class Grant {
  private final Type type;
  private final ResourceLocation id;
  private final boolean repeatable;

  public Grant(Type type, ResourceLocation id, boolean repeatable) {
    this.type = type;
    this.id = id;
    this.repeatable = repeatable;
  }

  public boolean isRepeatable() {
    return repeatable;
  }

  public Component getFailed() {
    if (getType() == Type.SPELL) {
      return Component.translatable("roots.message.spell.already_learned", Registries.SPELL_REGISTRY.get().getValue(getId()).getStyledName());
    } else if (getType() == Type.MODIFIER) {
      return Component.translatable("roots.message.modifier.already_learned", Registries.MODIFIER_REGISTRY.get().getValue(getId()).getName());
    } else {
      throw new IllegalStateException("Unknown grant type: " + getType());
    }
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
    result.addProperty("repeatable", repeatable);
    return result;
  }

  public void toNetwork(FriendlyByteBuf pBuffer) {
    pBuffer.writeResourceLocation(id);
    pBuffer.writeVarInt(type.ordinal());
    pBuffer.writeBoolean(repeatable);
  }

  public void grant(ServerPlayer serverPlayer) {
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
      boolean repeatable = false;
      if (!pJsonObject.get("repeatable").isJsonNull()) {
        repeatable = GsonHelper.getAsBoolean(pJsonObject, "repeatable");
      }
      Type type = EnumUtil.fromString(Type.class, GsonHelper.getAsString(pJsonObject, "type"));
      return new Grant(type, new ResourceLocation(GsonHelper.getAsString(pJsonObject, "id")), repeatable);
    }
  }

  public static Grant fromNetwork(FriendlyByteBuf pBuffer) {
    return new Grant(EnumUtil.fromOrdinal(Type.class, pBuffer.readVarInt()), pBuffer.readResourceLocation(), pBuffer.readBoolean());
  }

  public static Grant spell(Spell spell) {
    return new Grant(Type.SPELL, Registries.SPELL_REGISTRY.get().getKey(spell), false);
  }

  public static Grant modifier(Modifier modifier) {
    return new Grant(Type.MODIFIER, Registries.MODIFIER_REGISTRY.get().getKey(modifier), false);
  }

  public enum Type {
    SPELL,
    MODIFIER
  }
}
