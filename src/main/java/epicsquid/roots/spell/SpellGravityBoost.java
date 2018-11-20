package epicsquid.roots.spell;

import epicsquid.mysticalworld.init.ModItems;
import epicsquid.roots.entity.spell.EntityBoost;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

public class SpellGravityBoost extends SpellBase {

  public SpellGravityBoost(String name) {
    super(name, TextFormatting.BLUE, 32f / 255f, 200f / 255f, 255f / 255f, 32f / 255f, 64f / 255f, 255f / 255f);
    this.castType = SpellBase.EnumCastType.INSTANTANEOUS;
    this.cooldown = 56;

    addCost(ModItems.pereskia, 0.125f);
    addIngredients(
        new ItemStack(Items.RABBIT_FOOT, 1),
        new ItemStack(Blocks.RED_FLOWER, 1, 1),
        new ItemStack(Items.SUGAR, 1),
        new ItemStack(ModItems.aubergine_seed, 1),
        new ItemStack(ModItems.moonglow_leaf, 1)
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
