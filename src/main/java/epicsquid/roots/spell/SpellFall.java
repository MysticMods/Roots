package epicsquid.roots.spell;

import epicsquid.mysticallib.util.Util;
import epicsquid.mysticalworld.init.ModBlocks;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.spell.modules.SpellModule;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
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

    private int count;

    public SpellFall(String name) {
        super(name, TextFormatting.GOLD, 227/255F, 179/255F, 66/255F, 209/255F, 113/255F, 10/255F);

        this.castType = EnumCastType.CONTINUOUS;
        this.cooldown = 120;

        addCost(HerbRegistry.getHerbByName("stalicripe"), 0.05F);
        addCost(HerbRegistry.getHerbByName("wildewheet"), 0.025F);

        addIngredients(
                new ItemStack(ModItems.stalicripe),
                new ItemStack(ModBlocks.thatch),
                new ItemStack(ModItems.living_hoe),
                new ItemStack(epicsquid.mysticalworld.init.ModItems.copper_knife)
        );
    }

    @Override
    public boolean cast(EntityPlayer caster, List<SpellModule> modules) {
        //caster.world.setBlockState(caster.getPosition().up(2), Blocks.GRASS.getDefaultState());
        count++;
        boolean hadEffect = false;
        List<BlockPos> blocks = Util.getBlocksWithinRadius(caster.world, caster.getPosition(), 10, 10, 10, blockPos -> isAffectedByFallSpell(caster.world, blockPos));

        BlockPos pos;
        if (blocks.size() > 1)
            pos = blocks.get(Util.rand.nextInt(blocks.size() - 1));
        else if (!blocks.isEmpty())
            pos = blocks.get(0);
        else
            return false;

        IBlockState blockstate = caster.world.getBlockState(pos);
        Block block = blockstate.getBlock();

        if (count % 3 == 0)
        {
            if (block instanceof BlockLeaves || block instanceof BlockGrass) {
                block.dropBlockAsItemWithChance(caster.world, pos, blockstate, 0.75F, 0);
                caster.world.setBlockToAir(pos);
                caster.world.playSound(caster, pos, SoundEvents.BLOCK_GRASS_BREAK, SoundCategory.BLOCKS, 1F, 1F);
                return true;
            }
        }
        return false;
    }

    private boolean isAffectedByFallSpell(World world, BlockPos pos)
    {
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        return block instanceof BlockLeaves || block instanceof BlockGrass || block instanceof BlockTallGrass;
    }
}
