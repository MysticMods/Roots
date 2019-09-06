package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.entity.spell.EntityTimeStop;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.network.fx.MessageTimeStopStartFX;
import epicsquid.roots.spell.modules.SpellModule;
import epicsquid.roots.util.types.Property;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreIngredient;

import java.util.List;

public class SpellTimeStop extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(320);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("pereskia", 0.5));
  public static Property.PropertyCost PROP_COST_2 = new Property.PropertyCost(1, new SpellCost("moonglow_leaf", 0.5));
  public static Property<Integer> PROP_DURATION = new Property<>("duration", 200);

  public static String spellName = "spell_time_stop";
  public static SpellTimeStop instance = new SpellTimeStop(spellName);

  public static int duration;

  public SpellTimeStop(String name) {
    super(name, TextFormatting.DARK_BLUE, 64f / 255f, 64f / 255f, 64f / 255f, 192f / 255f, 32f / 255f, 255f / 255f);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_COST_2, PROP_DURATION);

    addIngredients(
        new OreIngredient("enderpearl"),
        new ItemStack(ModItems.moonglow_leaf),
        new ItemStack(ModItems.moonglow_leaf),
        new ItemStack(ModItems.pereskia),
        new ItemStack(Items.CLOCK)
    );
  }

  @Override
  public boolean cast(EntityPlayer player, List<SpellModule> modules) {
    if (!player.world.isRemote) {
      EntityTimeStop timeStop = new EntityTimeStop(player.world, duration);
      timeStop.setPlayer(player.getUniqueID());
      timeStop.setPosition(player.posX, player.posY, player.posZ);
      player.world.spawnEntity(timeStop);
      PacketHandler.sendToAllTracking(new MessageTimeStopStartFX(player.posX, player.posY + 1.0f, player.posZ), player);
    }
    return true;
  }

  @Override
  public void finalise() {
    this.castType = properties.getProperty(PROP_CAST_TYPE);
    this.cooldown = properties.getProperty(PROP_COOLDOWN);

    SpellCost cost = properties.getProperty(PROP_COST_1);
    addCost(cost.getHerb(), cost.getCost());
    cost = properties.getProperty(PROP_COST_2);
    addCost(cost.getHerb(), cost.getCost());

    duration = properties.getProperty(PROP_DURATION);
  }
}
