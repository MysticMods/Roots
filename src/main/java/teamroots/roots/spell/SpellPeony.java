package teamroots.roots.spell;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;
import teamroots.roots.entity.EntityFireJet;
import teamroots.roots.entity.EntityPetalShell;
import teamroots.roots.entity.EntityThornTrap;
import teamroots.roots.network.PacketHandler;
import teamroots.roots.network.message.MessagePetalShellBurstFX;

public class SpellPeony extends SpellBase {

	public SpellPeony(String name) {
		super(name,TextFormatting.LIGHT_PURPLE,255f/255f,192f/255f,240f/255f,255f/255f,255f/255f,255f/255f);
		this.castType = SpellBase.EnumCastType.INSTANTANEOUS;
		this.cooldown = 120;
	}
	
	@Override
	public void cast(EntityPlayer player){
		if (!player.world.isRemote){
			List<EntityPetalShell> shells = player.world.getEntitiesWithinAABB(EntityPetalShell.class, new AxisAlignedBB(player.posX-0.5,player.posY,player.posZ-0.5,player.posX+0.5,player.posY+2.0,player.posZ+0.5));
			boolean hasShell = false;
			for (EntityPetalShell s : shells){
				if (s.playerId.compareTo(player.getUniqueID()) == 0){
					hasShell = true;
					s.getDataManager().set(s.charge, Math.min(3,s.getDataManager().get(s.charge)+1));
					s.getDataManager().setDirty(s.charge);
				}
			}
			if (!hasShell){
				EntityPetalShell shell = new EntityPetalShell(player.world);
				shell.setPosition(player.posX, player.posY+1.0f, player.posZ);
				shell.setPlayer(player.getUniqueID());
				player.world.spawnEntity(shell);
			}
			PacketHandler.INSTANCE.sendToAll(new MessagePetalShellBurstFX(player.posX,player.posY+1.0f,player.posZ));
		}
	}

}
