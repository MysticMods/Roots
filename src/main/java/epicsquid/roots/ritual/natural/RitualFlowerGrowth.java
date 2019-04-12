package epicsquid.roots.ritual.natural;

import epicsquid.roots.entity.ritual.EntityRitualFlowerGrowth;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.recipe.conditions.ConditionItems;
import epicsquid.roots.recipe.conditions.ConditionWorldTime;
import epicsquid.roots.ritual.RitualBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RitualFlowerGrowth extends RitualBase {

    public RitualFlowerGrowth(String name, int duration)
    {
        super(name, duration);
        addCondition(new ConditionItems(
                new ItemStack(ModItems.pereskia),
                new ItemStack(ModItems.pereskia),
                new ItemStack(ModItems.cloud_berry),
                new ItemStack(ModItems.wildroot),
                new ItemStack(ModItems.spirit_herb)
        ));

        addCondition(new ConditionWorldTime(0, 13000));
        setIcon(ModItems.ritual_flower_growth);
    }

    @Override
    public void doEffect(World world, BlockPos pos)
    {
        this.spawnEntity(world, pos, EntityRitualFlowerGrowth.class);
    }
}
