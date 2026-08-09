package mysticmods.roots.item;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class EffectUseItem extends Item {
  private final Holder<MobEffect> effect;
  private final int amplifier;
  private final int duration;

  public EffectUseItem(Holder<MobEffect> effect, Properties properties) {
    this(effect, 0, 20 * 4, properties);
  }

  public EffectUseItem(Holder<MobEffect> effect, int amplifier, int duration, Properties properties) {
    super(properties);
    this.effect = effect;
    this.amplifier = amplifier;
    this.duration = duration;
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
    ItemStack stack = player.getItemInHand(usedHand);
    if (!level.isClientSide()) {
      MobEffectInstance instance = new MobEffectInstance(effect, duration, amplifier);
      player.addEffect(instance, player);
      stack.consume(1, player);
    }
    level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.GLASS_BREAK, SoundSource.PLAYERS);
    return InteractionResultHolder.consume(stack);
  }

  @Override
  public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    MutableComponent component = Component.translatable(effect.value().getDescriptionId());
    if (amplifier > 0) {
      component = Component.translatable("potion.withAmplifier", component, Component.translatable("potion.potency." + amplifier));
    }
    tooltipComponents.add(Component.translatable("roots.tooltip.effect", component, duration / 20));
  }
}
