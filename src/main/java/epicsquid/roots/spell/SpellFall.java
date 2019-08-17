package epicsquid.roots.spell;

import epicsquid.mysticallib.util.Util;
import epicsquid.mysticalworld.init.ModBlocks;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.spell.modules.SpellModule;
import jdk.nashorn.internal.ir.BlockStatement;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

/*************************************************
 * Author: Davoleo
 * Date / Hour: 17/08/2019 / 14:47
 * Class: SpellFall
 * Project: Mystic Mods
 * Copyright - © - Davoleo - 2019
 **************************************************/

public class SpellFall extends SpellBase {

    public static String spellName = "spell_fall";
    public static SpellFall instance = new SpellFall(spellName);

    public SpellFall(String name) {
        super(name, TextFormatting.GOLD, 227, 179, 66, 209, 113, 10);

        this.castType = EnumCastType.CONTINUOUS;
        this.cooldown = 120;

        addCost(HerbRegistry.getHerbByName("stalicripe"), 0.5F);
        addCost(HerbRegistry.getHerbByName("wildewheet"), 0.25F);

        addIngredients(
                new ItemStack(ModItems.stalicripe),
                new ItemStack(ModBlocks.thatch),
                new ItemStack(ModItems.living_hoe),
                new ItemStack(epicsquid.mysticalworld.init.ModItems.copper_knife)
        );
    }

    @Override
    public boolean cast(EntityPlayer caster, List<SpellModule> modules) {
        caster.world.setBlockState(caster.getPosition().up(2), Blocks.GRASS.getDefaultState());

        boolean hadEffect = false;
        List<BlockPos> blocks =  Util.getBlocksWithinRadius(caster.world, caster.getPosition(), 10, 10, 10, blockPos -> isAffectedByFallSpell(caster.world, blockPos) );

        for (BlockPos pos : blocks)
        {

        }
        return false;
    }

    private boolean isAffectedByFallSpell(World world, BlockPos pos)
    {
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        if (block == Blocks.GRASS || block == Blocks.TALLGRASS)
            return true;
        else if (block == Blocks.LEAVES || block == Blocks.LEAVES2)
            return true;
        else if (block == Blocks.DIRT)
        {
            // TODO: 17/08/2019 Complete This condition
            return true;
        }
        else return false;
    }
}
