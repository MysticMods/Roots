package epicsquid.roots.spell;

import java.util.List;

import epicsquid.roots.entity.spell.EntityPetalShell;
import epicsquid.roots.network.PacketHandler;
import epicsquid.roots.network.message.MessagePetalShellBurstFX;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;

public class SpellPetalShell extends SpellBase {

  public SpellPetalShell(String name) {
    super(name, TextFormatting.LIGHT_PURPLE, 255f / 255f, 192f / 255f, 240f / 255f, 255f / 255f, 255f / 255f, 255f / 255f);
    this.castType = SpellBase.EnumCastType.INSTANTANEOUS;
    this.cooldown = 120;
  }

  @Override
  public void cast(EntityPlayer player) {
    if (!player.world.isRemote) {
      List<EntityPetalShell> shells = player.world.getEntitiesWithinAABB(EntityPetalShell.class,
          new AxisAlignedBB(player.posX - 0.5, player.posY, player.posZ - 0.5, player.posX + 0.5, player.posY + 2.0, player.posZ + 0.5));
      boolean hasShell = false;
      for (EntityPetalShell s : shells) {
        if (s.playerId.compareTo(player.getUniqueID()) == 0) {
          hasShell = true;
          s.getDataManager().set(EntityPetalShell.getCharge(), Math.min(3, s.getDataManager().get(EntityPetalShell.getCharge()) + 1));
          s.getDataManager().setDirty(EntityPetalShell.getCharge());
        }
      }
      if (!hasShell) {
        EntityPetalShell shell = new EntityPetalShell(player.world);
        shell.setPosition(player.posX, player.posY + 1.0f, player.posZ);
        shell.setPlayer(player.getUniqueID());
        player.world.spawnEntity(shell);
      }
      PacketHandler.INSTANCE.sendToAll(new MessagePetalShellBurstFX(player.posX, player.posY + 1.0f, player.posZ));
    }
  }

}
