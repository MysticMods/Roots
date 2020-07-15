package epicsquid.roots.ritual.conditions;

import epicsquid.roots.tileentity.TileEntityPyre;
import epicsquid.roots.util.RitualUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

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
  public boolean checkCondition(TileEntityPyre tile, EntityPlayer player) {
    return RitualUtil.getNearbyPillar(type, tile.getWorld(), tile.getPos(), this.height) >= this.amount;
  }

  @Nullable
  @Override
  public ITextComponent failMessage() {
    return new TextComponentTranslation("roots.ritual.condition.pillar", new TextComponentTranslation("roots.ritual.condition.pillar.types", getAmount(), getHeight(), getType().toString()));
  }
}
