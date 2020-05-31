package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.Util;
import epicsquid.mysticalworld.init.ModBlocks;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.modifiers.instance.ModifierInstanceList;
import epicsquid.roots.network.fx.MessageFallBladesFX;
import epicsquid.roots.util.types.Property;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SpellAutumnsFall extends SpellBase {

  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(120);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.CONTINUOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(1, new SpellCost("wildewheet", 0.250));
  public static Property<Integer> PROP_RADIUS = new Property<>("radius", 10).setDescription("the horizontal radius of the effect this spell");
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 5).setDescription("the radius on the Y axis of the effect of this spell");
  public static Property<Integer> PROP_MAX_AFFECTED = new Property<>("max_affected", 1).setDescription("maximum number of blocks affected each tick");

  public static ResourceLocation spellName = new ResourceLocation(Roots.MODID, "spell_autumns_fall");
  public static SpellAutumnsFall instance = new SpellAutumnsFall(spellName);

  private int radius, max_affected, radius_y;

  public SpellAutumnsFall(ResourceLocation name) {
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
  public boolean cast(EntityPlayer caster, ModifierInstanceList modifiers, int ticks, int amplifier) {
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
        PacketHandler.sendToAllTracking(new MessageFallBladesFX(pos.getX(), pos.getY(), pos.getZ(), true), caster.world, pos);
      }
      affected++;
    }
    return affected > 0;
  }

  private static Set<Block> contained = new HashSet<>();
  private static Set<Block> ignored = new HashSet<>();

  private boolean isAffectedByFallSpell(World world, BlockPos pos) {
    IBlockState state = world.getBlockState(pos);
    Block block = state.getBlock();
    if (ignored.contains(block)) {
      return false;
    }
    if (contained.contains(block)) {
      return true;
    }

    if (block instanceof BlockLeaves) {
      contained.add(block);
      return true;
    }

    ItemStack dropped = new ItemStack(block.getItemDropped(state, Util.rand, 0), 1, block.damageDropped(state));
    if (!dropped.isEmpty()) {
      int[] ores = OreDictionary.getOreIDs(dropped);
      for (int ore : ores) {
        if (OreDictionary.getOreName(ore).equals("treeLeaves")) {
          contained.add(block);
          return true;
        }
      }
    }

    ignored.add(block);
    return false;
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
