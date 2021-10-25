package epicsquid.mysticallib.event;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.Event;

public class PlayerServerInteractEvent extends Event {
  protected @Nonnull EntityPlayer player;
  protected @Nonnull EnumHand hand;
  protected @Nonnull ItemStack stack;

  public PlayerServerInteractEvent(@Nonnull EntityPlayer player, @Nonnull EnumHand hand, @Nonnull ItemStack stack) {
    this.player = player;
    this.hand = hand;
    this.stack = stack;
  }

  @Nonnull
  public EntityPlayer getPlayer() {
    return player;
  }

  @Nonnull
  public EnumHand getHand() {
    return hand;
  }

  @Nonnull
  public ItemStack getStack() {
    return stack;
  }

  public static class LeftClickAir extends PlayerServerInteractEvent {

    public LeftClickAir(@Nonnull EntityPlayer player, @Nonnull EnumHand hand, @Nonnull ItemStack stack) {
      super(player, hand, stack);
    }

  }

  public static class RightClickAir extends PlayerServerInteractEvent {

    public RightClickAir(@Nonnull EntityPlayer player, @Nonnull EnumHand hand, @Nonnull ItemStack stack) {
      super(player, hand, stack);
    }

  }
}
