package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.advancements.Advancements;
import epicsquid.roots.block.BlockGroveStone;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.network.fx.MessageGroveCompleteFX;
import epicsquid.roots.spell.modules.SpellModule;
import epicsquid.roots.util.types.Property;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreIngredient;

import java.util.ArrayList;
import java.util.List;

public class SpellGroveSupplication extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(10);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("wildroot", 0.1));
  public static Property.PropertyCost PROP_COST_2 = new Property.PropertyCost(1, new SpellCost("terra_moss", 0.1));

  public static String spellName = "spell_supplication";
  public static SpellGroveSupplication instance = new SpellGroveSupplication(spellName);

  public SpellGroveSupplication(String name) {
    super(name, TextFormatting.GOLD, 66f / 255f, 40f / 255f, 7f / 255f, 218f / 255f, 106f / 255f, 24f / 255f);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_COST_2);

    addIngredients(
        new OreIngredient("doorWood"),
        new ItemStack(Blocks.MOSSY_COBBLESTONE),
        new ItemStack(Blocks.SAPLING),
        new ItemStack(ModItems.wildroot),
        new ItemStack(ModItems.petals)
    );
  }

  @Override
  public boolean cast(EntityPlayer player, List<SpellModule> modules) {
    List<BlockPos> positions = Util.getBlocksWithinRadius(player.world, player.getPosition(), 15, 10, 15, ModBlocks.grove_stone);
    if (positions.isEmpty()) return false;

    boolean didStuff = false;

    List<BlockPos> changed = new ArrayList<>();

    for (BlockPos pos : positions) {
      IBlockState state = player.world.getBlockState(pos);
      if (state.getBlock() != ModBlocks.grove_stone) {
        continue;
      }

      if (state.getValue(BlockGroveStone.VALID)) {
        continue;
      }

      didStuff = true;
      changed.add(pos);

      if (!player.world.isRemote) {
        player.world.setBlockState(pos, state.withProperty(BlockGroveStone.VALID, true));
        Advancements.GROVE_TRIGGER.trigger((EntityPlayerMP) player, null);
      }
    }

    if (didStuff && !changed.isEmpty() && !player.world.isRemote) {
      MessageGroveCompleteFX message = new MessageGroveCompleteFX(changed);
      PacketHandler.sendToAllTracking(message, player);
    }

    return didStuff;
  }

  @Override
  public void finalise() {
    this.castType = properties.getProperty(PROP_CAST_TYPE);
    this.cooldown = properties.getProperty(PROP_COOLDOWN);

    SpellCost cost = properties.getProperty(PROP_COST_1);
    addCost(cost.getHerb(), cost.getCost());
    cost = properties.getProperty(PROP_COST_2);
    addCost(cost.getHerb(), cost.getCost());
  }
}
