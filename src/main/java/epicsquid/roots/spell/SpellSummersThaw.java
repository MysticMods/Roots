package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.network.fx.MessageIcedTouchThawFX;
import epicsquid.roots.spell.modules.SpellModule;
import epicsquid.roots.util.RitualUtil;
import epicsquid.roots.util.types.Property;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFarmland;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

public class SpellSummersThaw extends SpellBase {

  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(20);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.CONTINUOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("infernal_bulb", 0.25));
  public static Property<Integer> PROP_RADIUS = new Property<>("radius", 5);

  public static String spellName = "spell_thaw";
  public static SpellSummersThaw instance = new SpellSummersThaw(spellName);

  private int radius;

  public SpellSummersThaw(String name) {
    super(name, TextFormatting.AQUA, 25F/255F, 1F, 235F/255F, 252F/255F, 166F/255F, 37F/255F);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_RADIUS);
  }

  @Override
  public void init() {
    addIngredients(
            new ItemStack(ModItems.bark_acacia),
            new ItemStack(Blocks.TORCH),
            new ItemStack(ModItems.infernal_bulb),
            new ItemStack(Items.GUNPOWDER),
            new ItemStack(Blocks.DOUBLE_PLANT, 1, BlockDoublePlant.EnumPlantType.SUNFLOWER.getMeta())
    );
  }

  @Override
  public boolean cast(EntityPlayer caster, List<SpellModule> modules) {

    BlockPos pos = RitualUtil.getRandomPosRadialXYZ(caster.getPosition(), radius, 2,  radius);
    boolean applied = false;

    if (!caster.world.isRemote) {
      if (caster.world.getBlockState(pos).getBlock() == Blocks.SNOW_LAYER) {
        caster.world.setBlockToAir(pos);
        applied = true;
      }

      if (caster.world.getBlockState(pos).getBlock() == Blocks.SNOW || caster.world.getBlockState(pos).getBlock() == Blocks.ICE) {
        caster.world.setBlockState(pos, Blocks.WATER.getDefaultState(), 3);
        applied = true;
      }

      if (caster.world.getBlockState(pos).getBlock() == Blocks.PACKED_ICE) {
        caster.world.setBlockState(pos, Blocks.ICE.getDefaultState(), 3);
        applied = true;
      }

      if ((caster.world.getBlockState(pos).getBlock() == Blocks.FARMLAND) && (caster.world.getBlockState(pos).getValue(BlockFarmland.MOISTURE) < 7)) {
        caster.world.setBlockState(pos, Blocks.FARMLAND.getDefaultState().withProperty(BlockFarmland.MOISTURE, 7), 3);
        applied = true;
      }

      if (applied) {
        PacketHandler.sendToAllTracking(new MessageIcedTouchThawFX(pos.getX(), pos.getY(), pos.getZ(), true), caster);
      }
    }

    return applied;
  }

  @Override
  public void doFinalise() {
    this.castType = properties.getProperty(PROP_CAST_TYPE);
    this.cooldown = properties.getProperty(PROP_COOLDOWN);
    this.radius = properties.getProperty(PROP_RADIUS);
  }

}
