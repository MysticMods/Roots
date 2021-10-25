package epicsquid.mysticallib.event;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.common.eventhandler.Event;

public class PlayerServerInteractEvent extends Event {
  protected @Nonnull
  PlayerEntity player;
  protected @Nonnull
  Hand hand;
  protected @Nonnull ItemStack stack;

  public PlayerServerInteractEvent(@Nonnull PlayerEntity player, @Nonnull Hand hand, @Nonnull ItemStack stack) {
    this.player = player;
    this.hand = hand;
    this.stack = stack;
  }

  @Nonnull
  public PlayerEntity getPlayer() {
    return player;
  }

  @Nonnull
  public Hand getHand() {
    return hand;
  }

  @Nonnull
  public ItemStack getStack() {
    return stack;
  }

  public static class LeftClickAir extends PlayerServerInteractEvent {

    public LeftClickAir(@Nonnull PlayerEntity player, @Nonnull Hand hand, @Nonnull ItemStack stack) {
      super(player, hand, stack);
    }

  }

  public static class RightClickAir extends PlayerServerInteractEvent {

    public RightClickAir(@Nonnull PlayerEntity player, @Nonnull Hand hand, @Nonnull ItemStack stack) {
      super(player, hand, stack);
    }

  }
}
