package epicsquid.roots.ritual.conditions;

import epicsquid.roots.tileentity.TileEntityPyre;
import epicsquid.roots.util.RitualUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public class ConditionStandingStones implements ICondition {

  private final int height;
  private final int amount;

  public ConditionStandingStones(int height, int amount) {
    this.height = height;
    this.amount = amount;
  }

  public int getHeight() {
    return height;
  }

  public int getAmount() {
    return amount;
  }

  @Override
  public boolean checkCondition(TileEntityPyre tile, PlayerEntity player) {
    return RitualUtil.getNearbyStandingStones(tile.getWorld(), tile.getPos(), this.height) >= this.amount;
  }

  @Nullable
  @Override
  public ITextComponent failMessage() {
    return new TranslationTextComponent("roots.ritual.condition.standing_stones", new TranslationTextComponent("roots.ritual.condition.standing_stones.types", getAmount(), getHeight()));
  }
}
