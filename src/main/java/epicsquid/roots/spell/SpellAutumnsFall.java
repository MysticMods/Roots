package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.Util;
import epicsquid.mysticalworld.init.ModBlocks;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.network.fx.MessageFallBladesFX;
import epicsquid.roots.spell.modules.SpellModule;
import epicsquid.roots.util.types.Property;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class SpellAutumnsFall extends SpellBase {

    public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(120);
    public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.CONTINUOUS);
    public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(1, new SpellCost("wildewheet", 0.025));
    public static Property<Integer> PROP_RADIUS = new Property<>("radius", 10);

    public static String spellName = "spell_autumns_fall";
    public static SpellAutumnsFall instance = new SpellAutumnsFall(spellName);

    private int radius;

    public SpellAutumnsFall(String name) {
        super(name, TextFormatting.GOLD, 227/255F, 179/255F, 66/255F, 209/255F, 113/255F, 10/255F);
        properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_RADIUS);
    }

    @Override
    public void init() {
        addIngredients(
                new ItemStack(ModItems.stalicripe),
                new ItemStack(ModItems.stalicripe),
                new ItemStack(ModBlocks.thatch),
                new ItemStack(ModItems.living_hoe),
                new ItemStack(epicsquid.mysticalworld.init.ModItems.copper_knife)
        );
    }

    @Override
    public boolean cast(EntityPlayer caster, List<SpellModule> modules) {
        List<BlockPos> blocks = Util.getBlocksWithinRadius(caster.world, caster.getPosition(), radius, 2, radius, blockPos -> isAffectedByFallSpell(caster.world, blockPos));

        if (blocks.isEmpty()) {
            return false;
        }

        BlockPos pos = blocks.get(Util.rand.nextInt(blocks.size()));

        IBlockState blockstate = caster.world.getBlockState(pos);
        Block block = blockstate.getBlock();

        if (Util.rand.nextBoolean()) {
            if (block instanceof BlockLeaves || block instanceof BlockTallGrass) {
                // Check itemblock for block leaves, build an save a map per session
                if (!caster.world.isRemote) {
                    caster.world.destroyBlock(pos, true);
                    caster.world.notifyBlockUpdate(pos, blockstate, Blocks.AIR.getDefaultState(), 8);
                    PacketHandler.sendToAllTracking(new MessageFallBladesFX(pos.getX(), pos.getY(), pos.getZ(), true), caster.world, pos);
                }
                return true;
            } else if (block instanceof BlockGrass)
            {
                if (!caster.world.isRemote) {
                    caster.world.setBlockState(pos, Blocks.DIRT.getDefaultState(), 3);
                    caster.world.playSound(caster, pos, SoundEvents.BLOCK_GRAVEL_BREAK, SoundCategory.BLOCKS, 0.5F, 1F);
                    PacketHandler.sendToAllTracking(new MessageFallBladesFX(pos.getX(), pos.getY() + 1, pos.getZ(), true), caster.world, pos);
                }
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

    @Override
    public void doFinalise() {
        this.castType = properties.getProperty(PROP_CAST_TYPE);
        this.cooldown = properties.getProperty(PROP_COOLDOWN);
        this.radius = properties.getProperty(PROP_RADIUS);
    }
}
