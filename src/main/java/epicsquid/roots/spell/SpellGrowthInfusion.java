package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.mechanics.Growth;
import epicsquid.roots.network.fx.MessageLifeInfusionFX;
import epicsquid.roots.spell.modules.SpellModule;
import epicsquid.roots.util.types.Property;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreIngredient;

import java.util.List;
import java.util.Random;

public class SpellGrowthInfusion extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(0);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.CONTINUOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("terra_moss", 0.08));
  public static Property<Integer> PROP_TICK_COUNT = new Property<>("tick_count", 1);

  public static String spellName = "spell_growth_infusion";
  public static SpellGrowthInfusion instance = new SpellGrowthInfusion(spellName);

  private int tickCount;

  public SpellGrowthInfusion(String name) {
    super(name, TextFormatting.YELLOW, 48f / 255f, 255f / 255f, 48f / 255f, 192f / 255f, 255f / 255f, 192f / 255f);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_TICK_COUNT);

    addIngredients(
        new OreIngredient("treeSapling"),
        new OreIngredient("treeSapling"),
        new ItemStack(ModItems.terra_moss),
        new ItemStack(epicsquid.mysticalworld.init.ModItems.aubergine),
        new ItemStack(Items.WHEAT)
    );
  }

  @Override
  public boolean cast(EntityPlayer player, List<SpellModule> modules) {
    RayTraceResult result = player.world.rayTraceBlocks(player.getPositionVector().add(0, player.getEyeHeight(), 0), player.getLookVec().scale(8.0f).add(player.getPositionVector().add(0, player.getEyeHeight(), 0)));
    if (result != null) {
      if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
        BlockPos pos = result.getBlockPos();
        IBlockState state = player.world.getBlockState(pos);
        if (Growth.canGrow(player.world, pos, state)) {
          if (!player.world.isRemote) {
            for (int i = 0; i < tickCount; i++) {
              state.getBlock().randomTick(player.world, pos, state, new Random());
            }
            PacketHandler.sendToAllTracking(new MessageLifeInfusionFX(pos.getX(), pos.getY(), pos.getZ()), player);
            return true;
          }
        }
      }
    }
    return false;
  }

  @Override
  public void finalise() {
    this.castType = properties.getProperty(PROP_CAST_TYPE);
    this.cooldown = properties.getProperty(PROP_COOLDOWN);

    SpellCost cost = properties.getProperty(PROP_COST_1);
    addCost(cost.getHerb(), cost.getCost());

    this.tickCount = properties.getProperty(PROP_TICK_COUNT);
  }
}
