package mysticmods.roots.api.recipe.output;

import com.mojang.serialization.Codec;
import mysticmods.roots.api.RootsAPI;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public record Cooldown(int ticks) implements IOutput {
  public static final Codec<Cooldown> CODEC = Codec.INT.xmap(Cooldown::new, Cooldown::ticks);

  public MutableComponent asNumber() {
    return Component.literal(String.join("\uf801", String.valueOf(ticks)
        .split(""))).withStyle(style -> style.withFont(RootsAPI.MINI_OUTLINE_FONT));
  }

  public Component asComponent () {
    return Component.translatable("roots.text.cooldown", String.valueOf(ticks / 20));
  }
}
