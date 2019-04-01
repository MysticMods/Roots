package epicsquid.roots.spell;

import java.util.List;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.entity.spell.EntityPetalShell;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.network.fx.MessagePetalShellBurstFX;
import epicsquid.roots.spell.modules.SpellModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;

public class SpellPetalShell extends SpellBase {
  public static String spellName = "spell_petal_shell";
  public static SpellPetalShell instance = new SpellPetalShell(spellName);

  public SpellPetalShell(String name) {
    super(name, TextFormatting.LIGHT_PURPLE, 255f / 255f, 192f / 255f, 240f / 255f, 255f / 255f, 255f / 255f, 255f / 255f);
    this.castType = SpellBase.EnumCastType.INSTANTANEOUS;
    this.cooldown = 120;

    addCost(HerbRegistry.getHerbByName("spirit_herb"), 0.75f);
    addIngredients(
        new ItemStack(Items.MELON),
        new ItemStack(ModItems.aubergine),
        new ItemStack(ModItems.spirit_herb),
        new ItemStack(Items.SHIELD),
        new ItemStack(ModItems.pereskia_bulb)
    );
  }

  @Override
  public boolean cast(EntityPlayer player, List<SpellModule> modules) {
    if (!player.world.isRemote) {
      List<EntityPetalShell> shells = player.world.getEntitiesWithinAABB(EntityPetalShell.class,
          new AxisAlignedBB(player.posX - 0.5, player.posY, player.posZ - 0.5, player.posX + 0.5, player.posY + 2.0, player.posZ + 0.5));
      boolean hasShell = false;
      for (EntityPetalShell s : shells) {
        if (s.getPlayerId().compareTo(player.getUniqueID()) == 0) {
          hasShell = true;
          s.getDataManager().set(s.getCharge(), Math.min(3, s.getDataManager().get(s.getCharge()) + 1));
          s.getDataManager().setDirty(s.getCharge());
        }
      }
      if (!hasShell) {
        EntityPetalShell shell = new EntityPetalShell(player.world);
        shell.setPosition(player.posX, player.posY + 1.0f, player.posZ);
        shell.setPlayer(player.getUniqueID());
        player.world.spawnEntity(shell);
      }
      PacketHandler.sendToAllTracking(new MessagePetalShellBurstFX(player.posX, player.posY + 1.0f, player.posZ), player);
    }
    return true;
  }

}
