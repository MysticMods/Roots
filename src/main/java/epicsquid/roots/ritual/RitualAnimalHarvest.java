package epicsquid.roots.ritual;

import epicsquid.roots.capability.IPlayerGroveCapability;
import epicsquid.roots.capability.PlayerGroveCapabilityProvider;
import epicsquid.roots.grove.GroveType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RitualAnimalHarvest extends RitualBase {

  public RitualAnimalHarvest(String name, int duration, boolean doUpdateValidity) {
    super(name, duration, doUpdateValidity);
  }

  @Override
  public void doEffect(World world, BlockPos pos) {
    //todo: let all mobs drop their thingy items
  }

  @Override
  public boolean canFire(World world, BlockPos pos, EntityPlayer player) {
    if(player != null){
      IPlayerGroveCapability capability = player.getCapability(PlayerGroveCapabilityProvider.PLAYER_GROVE_CAPABILITY, null);
      return capability.getTrust(GroveType.WILD) > 10;
    }
    return false;
  }
}
