package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.block.groves.GroveStoneBlock;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.network.fx.MessageGroveCompleteFX;
import epicsquid.roots.spell.modules.SpellModule;
import epicsquid.roots.util.types.Property;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreIngredient;

import java.util.ArrayList;
import java.util.List;

public class SpellFairySupplication extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(10);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("pereskia", 0.1));
  public static Property.PropertyCost PROP_COST_2 = new Property.PropertyCost(1, new SpellCost("terra_moss", 0.1));

  public static String spellName = "spell_fairy_supplication";
  public static SpellFairySupplication instance = new SpellFairySupplication(spellName);

  public SpellFairySupplication(String name) {
    super(name, TextFormatting.LIGHT_PURPLE, 66f / 255f, 40f / 255f, 7f / 255f, 218f / 255f, 106f / 255f, 24f / 255f);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_COST_2);
  }

  @Override
  public void init () {
    addIngredients(
        new OreIngredient("doorWood"),
        new ItemStack(ModBlocks.chiseled_runestone),
        new ItemStack(ModItems.pereskia_bulb),
        new ItemStack(ModItems.pereskia),
        new ItemStack(ModItems.petals)
    );
  }

  @Override
  public boolean cast(PlayerEntity player, List<SpellModule> modules) {
    List<BlockPos> positions = Util.getBlocksWithinRadius(player.world, player.getPosition(), 15, 10, 15, ModBlocks.fairy_grove_stone);
    if (positions.isEmpty()) return false;

    boolean didStuff = false;

    List<BlockPos> changed = new ArrayList<>();

    for (BlockPos pos : positions) {
      BlockState state = player.world.getBlockState(pos);
      if (state.getBlock() != ModBlocks.fairy_grove_stone) {
        continue;
      }

      if (state.getValue(GroveStoneBlock.VALID)) {
        continue;
      }

      didStuff = true;
      changed.add(pos);

      if (!player.world.isRemote) {
        player.world.setBlockState(pos, state.withProperty(GroveStoneBlock.VALID, true));
      }
    }

    if (didStuff && !changed.isEmpty() && !player.world.isRemote) {
      MessageGroveCompleteFX message = new MessageGroveCompleteFX(changed);
      PacketHandler.sendToAllTracking(message, player);
    }

    return didStuff;
  }

  @Override
  public void doFinalise() {
    this.castType = properties.getProperty(PROP_CAST_TYPE);
    this.cooldown = properties.getProperty(PROP_COOLDOWN);
  }
}
