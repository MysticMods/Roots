package epicsquid.roots.spell;

import epicsquid.roots.init.ModItems;
import epicsquid.roots.spell.modules.SpellModule;
import epicsquid.roots.util.types.Property;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

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
    public boolean cast(EntityPlayer caster, List<SpellModule> modules) {
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
