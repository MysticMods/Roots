package epicsquid.roots.recipe.conditions;

import epicsquid.roots.tileentity.TileEntityBonfire;
import net.minecraft.entity.player.EntityPlayer;

public class ConditionWorldTime implements Condition {

    private final int first, second;

    public ConditionWorldTime(int first, int second){
        this.first = first;
        this.second = second;
    }


    @Override
    public boolean checkCondition(TileEntityBonfire tile, EntityPlayer player) {
        return tile.getWorld().getWorldTime() >= first && tile.getWorld().getWorldTime() <= second;
    }
}
