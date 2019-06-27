package epicsquid.roots.ritual.natural;

import epicsquid.roots.entity.ritual.EntityRitualBase;
import epicsquid.roots.entity.ritual.EntityRitualFlowerGrowth;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.recipe.conditions.ConditionItems;
import epicsquid.roots.recipe.conditions.ConditionWorldTime;
import epicsquid.roots.ritual.RitualBase;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockRedFlower;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class RitualFlowerGrowth extends RitualBase {

    public RitualFlowerGrowth(String name, int duration)
    {
        super(name, duration);
        addCondition(new ConditionItems(
                new ItemStack(ModItems.cloud_berry),
                new ItemStack(ModItems.wildroot),
                new ItemStack(ModItems.petals),
                new ItemStack(ModItems.petals),
                new ItemStack(Blocks.RED_FLOWER, 1, BlockRedFlower.EnumFlowerType.POPPY.getMeta())
        ));

        addCondition(new ConditionWorldTime(0, 13000));
        setIcon(ModItems.ritual_flower_growth);
        setColor(TextFormatting.LIGHT_PURPLE);
    }

    @Override
    public EntityRitualBase doEffect(World world, BlockPos pos)
    {
        return this.spawnEntity(world, pos, EntityRitualFlowerGrowth.class);
    }
}
