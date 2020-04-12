package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.Roots;
import epicsquid.roots.modifiers.instance.ModifierInstanceList;
import epicsquid.roots.network.fx.MessageScatterPlantFX;
import epicsquid.roots.util.types.Property;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.oredict.OreIngredient;

import java.util.ArrayList;
import java.util.List;

public class SpellScatter extends SpellBase {

  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(60);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("wildroot", 0.25));
  public static Property<Integer> PROP_RADIUS = new Property<>("radius", 7).setDescription("horizontal radius of the area the spell in which the spell takes effect");
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 3).setDescription("radius on the Y axis of the area the spell in which the spell takes effect");
  public static Property<Integer> PROP_MAX_SEEDS = new Property<>("max_seeds", 16).setDescription("maximum number of seeds planted each time the spell is cast");

  public static ResourceLocation spellName = new ResourceLocation(Roots.MODID, "spell_scatter");
  public static SpellScatter instance = new SpellScatter(spellName);

  private int radius, radius_y, max_seeds;

  public SpellScatter(ResourceLocation name) {
    super(name, TextFormatting.DARK_GREEN, 188F / 255F, 244F / 255F, 151F / 255F, 71F / 255F, 132F / 255F, 30F / 255F);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_RADIUS, PROP_RADIUS_Y, PROP_MAX_SEEDS);
  }

  @Override
  public void init() {
    addIngredients(
        new OreIngredient("cropWheat"),
        new OreIngredient("cropPotato"),
        new ItemStack(Items.GOLDEN_HOE),
        new ItemStack(Items.WHEAT_SEEDS),
        new OreIngredient("rootsBark")
    );
  }

  @Override
  public boolean cast(EntityPlayer caster, ModifierInstanceList modifiers, int ticks) {
    World world = caster.world;
    BlockPos pos = caster.getPosition();

    ItemStack offhand = caster.getHeldItemOffhand();
    if (offhand.isEmpty() || !(offhand.getItem() instanceof IPlantable)) {
      return false;
    }

    List<BlockPos> blocks = Util.getBlocksWithinRadius(world, pos.down(), radius, radius_y, radius, (p) -> {
      IBlockState at = world.getBlockState(p);
      if (at.getBlock().canSustainPlant(at, world, p, EnumFacing.UP, (IPlantable) offhand.getItem())) {
        return world.isAirBlock(p.up());
      } else {
        return false;
      }
    });
    if (blocks.isEmpty()) {
      return false;
    }

    List<BlockPos> affectedPositions = new ArrayList<>();

    for (BlockPos p : blocks) {
      IPlantable plantable = (IPlantable) offhand.getItem();
      IBlockState planted = plantable.getPlant(world, p);

      if (Blocks.FARMLAND.canSustainPlant(planted, world, p, EnumFacing.UP, plantable)) {
        if (!world.isRemote) {
          world.setBlockState(p.up(), planted);

          if (!caster.isCreative()) {
            offhand.shrink(1);
          }
        }
        affectedPositions.add(p);
      }

      if (affectedPositions.size() >= max_seeds || offhand.isEmpty()) {
        break;
      }
    }

    if (!world.isRemote) {
      caster.setHeldItem(EnumHand.OFF_HAND, offhand);

      if (!affectedPositions.isEmpty()) {
        MessageScatterPlantFX fx = new MessageScatterPlantFX(affectedPositions);
        PacketHandler.sendToAllTracking(fx, caster);
      }
    }

    return !affectedPositions.isEmpty();
  }

  @Override
  public void doFinalise() {
    this.castType = properties.get(PROP_CAST_TYPE);
    this.cooldown = properties.get(PROP_COOLDOWN);
    this.radius = properties.get(PROP_RADIUS);
    this.radius_y = properties.get(PROP_RADIUS_Y);
    this.max_seeds = properties.get(PROP_MAX_SEEDS);
  }

}
