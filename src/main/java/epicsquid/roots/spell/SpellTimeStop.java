package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticalworld.init.ModItems;
import epicsquid.roots.entity.spell.EntityTimeStop;
import epicsquid.roots.network.fx.MessageTimeStopStartFX;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

public class SpellTimeStop extends SpellBase {

  public SpellTimeStop(String name) {
    super(name, TextFormatting.DARK_BLUE, 64f / 255f, 64f / 255f, 64f / 255f, 192f / 255f, 32f / 255f, 255f / 255f);
    this.castType = SpellBase.EnumCastType.INSTANTANEOUS;
    this.cooldown = 320;

    addCost(ModItems.pereskia, 0.5f);
    addCost(ModItems.moonglow_leaf, 0.25f);
    addCost(ModItems.pereskia_bulb, 0.25f);
    addIngredients(
        new ItemStack(Items.NETHER_WART),
        new ItemStack(ModItems.moonglow_leaf),
        new ItemStack(ModItems.moonglow_leaf),
        new ItemStack(ModItems.pereskia),
        new ItemStack(Items.CLOCK)
    );
  }

  @Override
  public void cast(EntityPlayer player) {
    if (!player.world.isRemote) {
      EntityTimeStop timeStop = new EntityTimeStop(player.world);
      timeStop.setPlayer(player.getUniqueID());
      timeStop.setPosition(player.posX, player.posY, player.posZ);
      player.world.spawnEntity(timeStop);
      PacketHandler.INSTANCE.sendToAll(new MessageTimeStopStartFX(player.posX, player.posY + 1.0f, player.posZ));
    }
  }

}
