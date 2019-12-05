package epicsquid.roots.spell;

import epicsquid.roots.entity.spell.TimeStopEntity;
import epicsquid.roots.spell.modules.SpellModule;
import epicsquid.roots.util.types.Property;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TextFormatting;

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
  }

  @Override
  public void init() {
    addIngredients(
/*        new OreIngredient("enderpearl"),
        new ItemStack(ModItems.moonglow_leaf),
        PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), Potions.SLOWNESS),
        new ItemStack(ModItems.pereskia),
        new ItemStack(Items.CLOCK)*/
    );

  }

  @Override
  public boolean cast(PlayerEntity player, List<SpellModule> modules) {
    if (!player.world.isRemote) {
      // TODO: Properly create thje entities
      TimeStopEntity timeStop = new TimeStopEntity(player.world, duration);
      timeStop.setPlayer(player.getUniqueID());
      timeStop.setPosition(player.posX, player.posY, player.posZ);
      player.world.addEntity(timeStop);
      // TODO: PAckets
      //PacketHandler.sendToAllTracking(new MessageTimeStopStartFX(player.posX, player.posY + 1.0f, player.posZ), player);
    }
    return true;
  }

  @Override
  public void doFinalise() {
    this.castType = properties.getProperty(PROP_CAST_TYPE);
    this.cooldown = properties.getProperty(PROP_COOLDOWN);
    duration = properties.getProperty(PROP_DURATION);
  }
}
