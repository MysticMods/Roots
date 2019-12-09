package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.network.fx.MessageFallBladesFX;
import epicsquid.roots.spell.modules.SpellModule;
import epicsquid.roots.util.types.Property;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

public class SpellBlades extends SpellBase {

    public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(160);
    public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.CONTINUOUS);
    public static Property.PropertyCost PROP_COST = new Property.PropertyCost(0, new SpellCost("wildroot", 0.05));
    public static Property<Integer> PROP_RADIUS = new Property<>("radius", 10);

    public static String spellName = "spell_blades";
    public static SpellBlades instance = new SpellBlades(spellName);

    private int radius;

    public SpellBlades(String name) {
        super(name, TextFormatting.DARK_GREEN, 64/255F, 240/255F, 24/255F, 26/255F, 110/255F, 13/255F);
        properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST, PROP_RADIUS);
    }

    @Override
    public void init() {
        addIngredients(
                new ItemStack(ModItems.wildroot),
                new ItemStack(epicsquid.mysticalworld.init.ModItems.aubergine),
                new ItemStack(ModItems.seeds),
                new ItemStack(Items.STONE_HOE)
        );
    }

    @Override
    public boolean cast(EntityPlayer caster, List<SpellModule> modules) {

        List<BlockPos> blocks = Util.getBlocksWithinRadius(caster.world, caster.getPosition(), radius, 2, radius,
                pos -> caster.world.isAirBlock(pos.up()) && caster.world.getBlockState(pos).getBlock() instanceof BlockDirt);

        BlockPos pos;

        if (blocks.size() > 1)
            pos = blocks.get(Util.rand.nextInt(blocks.size() - 1));
        else if (!blocks.isEmpty())
            pos = blocks.get(0);
        else
            return false;

        if (pos != null) {
            IBlockState blockstate = caster.world.getBlockState(pos);

            if (blockstate.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.DIRT) {
                if (!caster.world.isRemote) {
                    if (Util.rand.nextInt(30) == 0){
                        caster.world.setBlockState(pos, Blocks.GRASS.getDefaultState());
                        caster.world.playSound(caster, pos, SoundEvents.BLOCK_GRASS_BREAK, SoundCategory.BLOCKS, 0.5F, 2F);
                        PacketHandler.sendToAllTracking(new MessageFallBladesFX(pos.getX(), pos.getY(), pos.getZ(), false), caster.world, pos);
                    }
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void doFinalise() {
        this.castType = properties.getProperty(PROP_CAST_TYPE);
        this.cooldown = properties.getProperty(PROP_COOLDOWN);
        this.radius = properties.getProperty(PROP_RADIUS);
    }
}
