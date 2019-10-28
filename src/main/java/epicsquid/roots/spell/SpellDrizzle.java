package epicsquid.roots.spell;

import epicsquid.roots.init.ModItems;
import epicsquid.roots.spell.modules.SpellModule;
import epicsquid.roots.util.types.Property;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

public class SpellDrizzle extends SpellBase {

    public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(300);
    public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
    public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("dewgonia", 0.125));
    public static Property.PropertyCost PROP_COST_2 = new Property.PropertyCost(1, new SpellCost("wildroot", 0.125));
    public static Property<Integer> PROP_RADIUS = new Property<>("radius", 20);

    private int radius;

    public static String spellName = "spell_drizzle";
    public static SpellDrizzle instance = new SpellDrizzle(spellName);

    public SpellDrizzle(String name) {
        super(name, TextFormatting.BLUE, 34/255F, 133/255F, 245/255F, 23/255F, 44/255F, 89/255F);
        properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_COST_2);
    }

    @Override
    public void init() {
        addIngredients(
                new ItemStack(Items.BUCKET),
                new ItemStack(ModItems.dewgonia),
                new ItemStack(Items.DYE, 1, 0),
                new ItemStack(Item.getItemFromBlock(Blocks.VINE))
        );
    }

    @Override
    public boolean cast(EntityPlayer caster, List<SpellModule> modules) {
        return false;
    }

    @Override
    public void doFinalise() {
        this.castType = properties.getProperty(PROP_CAST_TYPE);
        this.cooldown = properties.getProperty(PROP_COOLDOWN);
        this.radius = properties.getProperty(PROP_RADIUS);
    }
}
