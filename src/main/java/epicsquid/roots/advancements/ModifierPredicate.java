package epicsquid.roots.advancements;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import epicsquid.mysticallib.advancement.IGenericPredicate;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.modifiers.IModifierCore;
import epicsquid.roots.modifiers.Modifier;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstance;
import epicsquid.roots.recipe.FeyCraftingRecipe;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.JsonUtils;
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
  public boolean test(EntityPlayerMP player, IModifierCore condition) {
    if (this.registryName == null) {
      return false;
    }
    return condition.getRegistryName().equals(this.registryName);
  }

  @Override
  public IGenericPredicate<IModifierCore> deserialize(@Nullable JsonElement element) {
    if (element != null) {
      JsonObject object = element.getAsJsonObject();
      if (JsonUtils.isString(object, "modifier")) {
        ResourceLocation modifier = new ResourceLocation(JsonUtils.getString(object, "modifier"));
        return new ModifierPredicate(modifier);
      }
    }
    return new ModifierPredicate();
  }
}
