package epicsquid.roots.spell;

import epicsquid.roots.entity.spell.FireJetEntity;
import epicsquid.roots.spell.modules.SpellModule;
import epicsquid.roots.util.types.Property;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

public class SpellWildfire extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(24);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("infernal_bulb", 0.125));

  public static String spellName = "spell_wild_fire";
  public static SpellWildfire instance = new SpellWildfire(spellName);

  public SpellWildfire(String name) {
    super(name, TextFormatting.GOLD, 255f / 255f, 128f / 255f, 32f / 255f, 255f / 255f, 64f / 255f, 32f / 255f);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1);
  }

  @Override
  public void init() {
    addIngredients(
/*        new ItemStack(Items.DYE, 1, 14),
        new ItemStack(Items.COAL, 1, 1),
        new OreIngredient("gunpowder"),
        new ItemStack(ModItems.infernal_bulb),
        new ItemStack(Item.getItemFromBlock(Blocks.TNT))*/
    );
  }

  @Override
  public boolean cast(PlayerEntity player, List<SpellModule> modules) {
    if (!player.world.isRemote) {
      // TODO: Do this properly
      FireJetEntity fireJet = new FireJetEntity(null, player.world);
      fireJet.setPlayer(player.getUniqueID());
      fireJet.setPosition(player.posX, player.posY, player.posZ);
      player.world.addEntity(fireJet);
    }
    return true;
  }

  @Override
  public void doFinalise() {
    this.castType = properties.getProperty(PROP_CAST_TYPE);
    this.cooldown = properties.getProperty(PROP_COOLDOWN);
  }
}
