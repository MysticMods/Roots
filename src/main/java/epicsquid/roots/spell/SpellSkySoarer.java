package epicsquid.roots.spell;

import epicsquid.mysticalworld.init.ModItems;
import epicsquid.roots.entity.spell.EntityBoost;
import epicsquid.roots.init.HerbRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

public class SpellSkySoarer extends SpellBase {

  public SpellSkySoarer(String name) {
    super(name, TextFormatting.BLUE, 32f / 255f, 200f / 255f, 255f / 255f, 32f / 255f, 64f / 255f, 255f / 255f);
    this.castType = SpellBase.EnumCastType.INSTANTANEOUS;
    this.cooldown = 56;

    addCost(HerbRegistry.getHerbByName("cloud_berry"), 0.125f);
    addIngredients(
        new ItemStack(Items.SUGAR),
        new ItemStack(Items.FEATHER),
        new ItemStack(Items.STRING),
        new ItemStack(ModItems.aubergine_seed),
        new ItemStack(ModItems.cloud_berry)
    );
  }

  @Override
  public void cast(EntityPlayer player) {
    if (!player.world.isRemote) {
      EntityBoost boost = new EntityBoost(player.world);
      boost.setPlayer(player.getUniqueID());
      boost.setPosition(player.posX, player.posY, player.posZ);
      player.world.spawnEntity(boost);
    }
  }

}
