package epicsquid.roots.advancements;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import epicsquid.mysticallib.advancement.IGenericPredicate;
import epicsquid.roots.modifiers.IModifierCore;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class ModifierPredicate implements IGenericPredicate<IModifierCore> {
  private final ResourceLocation registryName;

  public ModifierPredicate(ResourceLocation registryName) {
    this.registryName = registryName;
  }

  public ModifierPredicate() {
    this.registryName = null;
  }

  @Override
  public boolean test(ServerPlayerEntity player, IModifierCore condition) {
    if (this.registryName == null) {
      return false;
    }
    return condition.getRegistryName().equals(this.registryName);
  }

  @Override
  public IGenericPredicate<IModifierCore> deserialize(@Nullable JsonElement element) {
    if (element != null) {
      JsonObject object = element.getAsJsonObject();
      if (JSONUtils.isString(object, "modifier")) {
        ResourceLocation modifier = new ResourceLocation(JSONUtils.getString(object, "modifier"));
        return new ModifierPredicate(modifier);
      }
    }
    return new ModifierPredicate();
  }
}
