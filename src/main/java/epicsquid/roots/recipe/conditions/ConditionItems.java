package epicsquid.roots.recipe.conditions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import epicsquid.mysticallib.util.ListUtil;
import epicsquid.roots.tileentity.TileEntityBonfire;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ConditionItems implements Condition {

    final List<ItemStack> itemList;

    public ConditionItems(ItemStack... stacks){
        itemList = Arrays.asList(stacks);

    }

    @Override
    public boolean checkCondition(TileEntityBonfire tile, EntityPlayer player) {
        List<ItemStack> stacks = new ArrayList<>();
        for (int i = 0; i < tile.inventory.getSlots(); i++) {
            stacks.add(tile.inventory.getStackInSlot(i));
        }
        return ListUtil.stackListsMatch(stacks, this.itemList);
    }

    public List<ItemStack> getItemList() {
        return itemList;
    }
}
