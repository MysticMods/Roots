package epicsquid.roots.spell;

import epicsquid.mysticalworld.init.ModItems;
import epicsquid.roots.entity.spell.EntityFireJet;
import epicsquid.roots.init.HerbRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
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
        new ItemStack(Blocks.RED_FLOWER, 1, 5),
        new ItemStack(Items.GUNPOWDER),
        new ItemStack(Items.COAL, 1, 1),
        new ItemStack(ModItems.wildroot)
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
