package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.entity.spell.EntityTimeStop;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.network.fx.MessageTimeStopStartFX;
import epicsquid.roots.spell.modules.SpellModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

public class SpellTimeStop extends SpellBase {
  public static String spellName = "spell_time_stop";
  public static SpellTimeStop instance = new SpellTimeStop(spellName);

  public SpellTimeStop(String name) {
    super(name, TextFormatting.DARK_BLUE, 64f / 255f, 64f / 255f, 64f / 255f, 192f / 255f, 32f / 255f, 255f / 255f);
    this.castType = SpellBase.EnumCastType.INSTANTANEOUS;
    this.cooldown = 320;

    addCost(HerbRegistry.getHerbByName("pereskia"), 0.5f);
    addCost(HerbRegistry.getHerbByName("moonglow_leaf"), 0.5f);
    addIngredients(
        new ItemStack(Items.ENDER_EYE),
        new ItemStack(ModItems.moonglow_leaf),
        new ItemStack(ModItems.moonglow_leaf),
        new ItemStack(ModItems.pereskia),
        new ItemStack(Items.CLOCK)
    );
  }

  @Override
  public boolean cast(EntityPlayer player, List<SpellModule> modules) {
    if (!player.world.isRemote) {
      EntityTimeStop timeStop = new EntityTimeStop(player.world);
      timeStop.setPlayer(player.getUniqueID());
      timeStop.setPosition(player.posX, player.posY, player.posZ);
      player.world.spawnEntity(timeStop);
      PacketHandler.INSTANCE.sendToAll(new MessageTimeStopStartFX(player.posX, player.posY + 1.0f, player.posZ));
    }
    return true;
  }

}
