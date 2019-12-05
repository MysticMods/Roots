package epicsquid.roots.ritual.conditions;

import epicsquid.roots.capability.grove.IPlayerGroveCapability;
import epicsquid.roots.capability.grove.PlayerGroveCapabilityProvider;
import epicsquid.roots.grove.GroveType;
import epicsquid.roots.tileentity.TileEntityBonfire;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

@Deprecated
public class ConditionGroveFaith implements Condition {

  private final GroveType type;
  private final int amount;

  public ConditionGroveFaith(GroveType type, int amount) {
    this.type = type;
    this.amount = amount;
  }

  @Override
  public boolean checkCondition(TileEntityBonfire tile, PlayerEntity player) {
    IPlayerGroveCapability capability = player.getCapability(PlayerGroveCapabilityProvider.PLAYER_GROVE_CAPABILITY, null);
    if (capability == null) {
      return false;
    }
    return capability.getTrust(type) >= amount;
  }

  @Nullable
  @Override
  public ITextComponent failMessage() {
    return null;
  }

}
