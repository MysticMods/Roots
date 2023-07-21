package mysticmods.roots.effect;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.stats.Stats;
import net.minecraft.world.effect.InstantenousMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class WakefulEffect extends InstantenousMobEffect {
  public WakefulEffect() {
    super(MobEffectCategory.BENEFICIAL, 0xffffff);
  }

  @Override
  public void applyEffectTick(LivingEntity entity, int amplifier) {
    if (entity instanceof Player player) {
      player.resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_REST));
      player.sendSystemMessage(Component.translatable("message.dandelion_cordial").setStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.YELLOW))));
    }
  }
}
