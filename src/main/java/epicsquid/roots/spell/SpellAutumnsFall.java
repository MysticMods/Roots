package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.Util;
import epicsquid.mysticalworld.init.ModBlocks;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.network.fx.MessageFallBladesFX;
import epicsquid.roots.spell.modules.SpellModule;
import epicsquid.roots.util.types.Property;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class SpellAutumnsFall extends SpellBase {

  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(120);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.CONTINUOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(1, new SpellCost("wildewheet", 0.250));
  public static Property<Integer> PROP_RADIUS = new Property<>("radius", 10).setDescription("the horizontal radius of the effect this spell");
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 3).setDescription("the radius on the Y axis of the effect of this spell");
  public static Property<Integer> PROP_MAX_AFFECTED = new Property<>("max_affected", 1).setDescription("maximum number of blocks affected each tick");

  public static String spellName = "spell_autumns_fall";
  public static SpellAutumnsFall instance = new SpellAutumnsFall(spellName);

  private int radius, max_affected, radius_y;

  public SpellAutumnsFall(String name) {
    super(name, TextFormatting.GOLD, 227 / 255F, 179 / 255F, 66 / 255F, 209 / 255F, 113 / 255F, 10 / 255F);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_RADIUS, PROP_RADIUS_Y, PROP_MAX_AFFECTED);
  }

  @Override
  public void init() {
    addIngredients(
        new ItemStack(ModItems.stalicripe),
        new ItemStack(ModItems.wildewheet),
        new ItemStack(ModBlocks.thatch),
        new ItemStack(Items.STONE_HOE),
        new ItemStack(Item.getItemFromBlock(Blocks.DEADBUSH))
    );
  }

  @Override
  public boolean cast(EntityPlayer caster, List<SpellModule> modules, int ticks) {
    List<BlockPos> blocks = Util.getBlocksWithinRadius(caster.world, caster.getPosition(), radius, radius_y, radius, blockPos -> isAffectedByFallSpell(caster.world, blockPos));

    if (blocks.isEmpty()) {
      return false;
    }

    int affected = 0;

    while (affected < max_affected && !blocks.isEmpty()) {

      BlockPos pos = blocks.remove(Util.rand.nextInt(blocks.size()));
      // TODO: Check itemblock for block leaves, build and save a map per session
      if (!caster.world.isRemote) {
        caster.world.destroyBlock(pos, true);
        //caster.world.notifyBlockUpdate(pos, blockstate, Blocks.AIR.getDefaultState(), 8); // TODO: Is this needed?
        PacketHandler.sendToAllTracking(new MessageFallBladesFX(pos.getX(), pos.getY(), pos.getZ(), true), caster.world, pos);
      }
      affected++;
    }
    return affected > 0;
  }

  private boolean isAffectedByFallSpell(World world, BlockPos pos) {
    IBlockState state = world.getBlockState(pos);
    Block block = state.getBlock();

    return block instanceof BlockLeaves || block instanceof BlockTallGrass;
  }

  @Override
  public void doFinalise() {
    this.castType = properties.get(PROP_CAST_TYPE);
    this.cooldown = properties.get(PROP_COOLDOWN);
    this.radius = properties.get(PROP_RADIUS);
    this.radius_y = properties.get(PROP_RADIUS_Y);
    this.max_affected = properties.get(PROP_MAX_AFFECTED);
  }
}
