package mysticmods.roots.recipe.runic;

import mysticmods.roots.api.recipe.crafting.IEntityCrafting;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class RunicEntityCrafting implements IEntityCrafting {
  private final LivingEntity entity;
  private final Player player;
  private final Level level;
  private final InteractionHand hand;
  private final ItemStack stack;

  public RunicEntityCrafting(LivingEntity entity, Player player, Level level, InteractionHand hand, ItemStack stack) {
    this.entity = entity;
    this.player = player;
    this.level = level;
    this.hand = hand;
    this.stack = stack;
  }

  @Override
  public LivingEntity getEntity() {
    return entity;
  }

  @Nullable
  @Override
  public Player getPlayer() {
    return player;
  }

  @Nullable
  @Override
  public Level getLevel() {
    return level;
  }

  @Override
  public void setChanged() {

  }

  @Override
  public boolean stillValid(Player pPlayer) {
    return true;
  }

  @Override
  public void clearContent() {

  }

  public ItemStack getStack() {
    return stack;
  }

  public InteractionHand getHand() {
    return hand;
  }
}
