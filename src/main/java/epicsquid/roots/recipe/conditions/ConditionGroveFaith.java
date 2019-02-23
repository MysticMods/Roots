package epicsquid.roots.recipe.conditions;

import epicsquid.roots.capability.IPlayerGroveCapability;
import epicsquid.roots.capability.PlayerGroveCapabilityProvider;
import epicsquid.roots.grove.GroveType;
import epicsquid.roots.tileentity.TileEntityBonfire;
import net.minecraft.entity.player.EntityPlayer;

public class ConditionGroveFaith implements Condition {

    private final GroveType type;
    private final int amount;

    public ConditionGroveFaith(GroveType type, int amount){
        this.type = type;
        this.amount = amount;
    }

    @Override
    public boolean checkCondition(TileEntityBonfire tile, EntityPlayer player) {
        IPlayerGroveCapability capability = player.getCapability(PlayerGroveCapabilityProvider.PLAYER_GROVE_CAPABILITY, null);
        if(capability == null){
            return false;
        }
        return capability.getTrust(type) >= amount;
    }

}
