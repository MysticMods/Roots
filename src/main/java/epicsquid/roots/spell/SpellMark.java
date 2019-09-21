package epicsquid.roots.spell;

import epicsquid.roots.block.BlockMark;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.spell.modules.SpellModule;
import epicsquid.roots.util.types.Property;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

/*************************************************
 * Author: Davoleo
 * Date / Hour: 19/09/2019 / 21:48
 * Class: SpellMark
 * Project: Mystic Mods
 * Copyright - © - Davoleo - 2019
 **************************************************/

public class SpellMark extends SpellBase {

    public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(100);
    public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
    public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("moonglow_leaf", 0.25));
    public static Property.PropertyCost PROP_COST_2 = new Property.PropertyCost(1, new SpellCost("wildroot", 0.125));
    public static Property<Integer> PROP_COUNT = new Property<>("count", 15);

    public static String spellName = "spell_mark";
    public static SpellMark instance = new SpellMark(spellName);

    private int count;

    public SpellMark(String name) {
        super(name, TextFormatting.GREEN, 237F/255F, 199F/255F, 47F/255F, 161F/255F, 237F/255F, 47F/255F);
        properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_COST_2, PROP_COUNT);
    }

    @Override
    public void init() {
        addIngredients(
                new ItemStack(Blocks.TORCH),
                new ItemStack(ModItems.petals),
                new ItemStack(ModItems.moonglow_leaf),
                new ItemStack(ModItems.terra_moss)
        );
    }

    @Override
    public boolean cast(EntityPlayer player, List<SpellModule> modules) {
        World world = player.world;
        RayTraceResult result = SpellFeyLight.instance.rayTrace(player, player.isSneaking() ? 1 : 10);
        if (result != null && (!player.isSneaking() && result.typeOfHit == RayTraceResult.Type.BLOCK)) {
            BlockPos pos = result.getBlockPos().offset(result.sideHit);
            if (world.isAirBlock(pos)) {
                if (!world.isRemote) {
                    world.setBlockState(pos, ModBlocks.mark.getDefaultState().withProperty(BlockMark.FACING, result.sideHit));
                    world.playSound(null, pos, SoundEvents.BLOCK_CLOTH_PLACE, SoundCategory.PLAYERS, 0.50f, 1.25F);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void finalise() {
        this.castType = properties.getProperty(PROP_CAST_TYPE);
        this.cooldown = properties.getProperty(PROP_COOLDOWN);

        SpellCost cost1 = properties.getProperty(PROP_COST_1);
        SpellCost cost2 = properties.getProperty(PROP_COST_2);
        this.addCost(cost1.getHerb(), cost1.getCost());
        this.addCost(cost2.getHerb(), cost2.getCost());

        this.count = properties.getProperty(PROP_COUNT);
    }
}
