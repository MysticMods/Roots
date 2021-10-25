package epicsquid.roots.ritual.conditions;

import epicsquid.roots.tileentity.TileEntityPyre;
import epicsquid.roots.util.RitualUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public class ConditionRunedPillars implements ICondition {

  private final int height;
  private final int amount;
  private final RitualUtil.RunedWoodType type;

  public ConditionRunedPillars(RitualUtil.RunedWoodType type, int height, int amount) {
    this.height = height;
    this.amount = amount;
    this.type = type;
  }

  public int getHeight() {
    return height;
  }

  public int getAmount() {
    return amount;
  }

  public RitualUtil.RunedWoodType getType() {
    return type;
  }

  @Override
  public boolean checkCondition(TileEntityPyre tile, PlayerEntity player) {
    return RitualUtil.getNearbyPillar(type, tile.getWorld(), tile.getPos(), this.height) >= this.amount;
  }

  @Nullable
  @Override
  public ITextComponent failMessage() {
    return new TranslationTextComponent("roots.ritual.condition.pillar", new TranslationTextComponent("roots.ritual.condition.pillar.types", getAmount(), getHeight(), getType().toString()));
  }
}
