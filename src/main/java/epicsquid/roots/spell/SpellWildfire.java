package epicsquid.roots.spell;

import epicsquid.roots.entity.spell.EntityFireJet;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

public class SpellWildfire extends SpellBase {

  public SpellWildfire(String name) {
    super(name, TextFormatting.GOLD, 255f / 255f, 128f / 255f, 32f / 255f, 255f / 255f, 64f / 255f, 32f / 255f);
    this.castType = SpellBase.EnumCastType.INSTANTANEOUS;
    this.cooldown = 24;

    addCost(HerbRegistry.getHerbByName("infernal_bulb"), 0.125f);
    addIngredients(
        new ItemStack(Items.DYE, 1, 14),
        new ItemStack(Items.COAL, 1, 1),
        new ItemStack(Items.GUNPOWDER),
        new ItemStack(ModItems.infernal_bulb),
        new ItemStack(ModItems.infernal_bulb)
    );
  }

  @Override
  public void cast(EntityPlayer player) {
    if (!player.world.isRemote) {
      EntityFireJet fireJet = new EntityFireJet(player.world);
      fireJet.setPlayer(player.getUniqueID());
      fireJet.setPosition(player.posX, player.posY, player.posZ);
      player.world.spawnEntity(fireJet);
    }
  }

}
