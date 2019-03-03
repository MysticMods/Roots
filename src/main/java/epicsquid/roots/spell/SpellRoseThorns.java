package epicsquid.roots.spell;

import epicsquid.roots.entity.spell.EntityThornTrap;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

public class SpellRoseThorns extends SpellBase {

  public SpellRoseThorns(String name) {
    super(name, TextFormatting.RED, 255f / 255f, 32f / 255f, 64f / 255f, 32f / 255f, 255f / 255f, 96f / 255f);
    this.castType = SpellBase.EnumCastType.INSTANTANEOUS;
    this.cooldown = 24;

    addCost(HerbRegistry.getHerbByName("terra_moss"), 0.25f);
    addIngredients(
        new ItemStack(Blocks.CACTUS),
        new ItemStack(Blocks.DOUBLE_PLANT, 1, 4),
        new ItemStack(Items.BONE),
        new ItemStack(Items.FERMENTED_SPIDER_EYE),
        new ItemStack(ModItems.terra_moss)
    );
  }

  @Override
  public void cast(EntityPlayer player) {
    if (!player.world.isRemote) {
      EntityThornTrap trap = new EntityThornTrap(player.world);
      trap.setPlayer(player.getUniqueID());
      trap.setPosition(player.posX + player.getLookVec().x, player.posY + player.getEyeHeight() + player.getLookVec().y, player.posZ + player.getLookVec().z);
      trap.motionX = player.getLookVec().x * 0.75f;
      trap.motionY = player.getLookVec().y * 0.75f;
      trap.motionZ = player.getLookVec().z * 0.75f;
      player.world.spawnEntity(trap);
    }
  }

}
