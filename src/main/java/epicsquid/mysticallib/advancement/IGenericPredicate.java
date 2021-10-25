package epicsquid.mysticallib.advancement;

import javax.annotation.Nullable;

import com.google.gson.JsonElement;

import net.minecraft.entity.player.EntityPlayerMP;

public interface IGenericPredicate<T> {
  boolean test(EntityPlayerMP player, T condition);

  IGenericPredicate<T> deserialize(@Nullable JsonElement element);
}
