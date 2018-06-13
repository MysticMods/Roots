package teamroots.roots.spell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;
import teamroots.roots.entity.EntityFireJet;
import teamroots.roots.entity.EntityTimeStop;
import teamroots.roots.network.PacketHandler;
import teamroots.roots.network.message.MessageTimeStopStartFX;

public class SpellOxeyeDaisy extends SpellBase {

	public SpellOxeyeDaisy(String name) {
		super(name,TextFormatting.DARK_BLUE,64f/255f,64f/255f,64f/255f,192f/255f,32f/255f,255f/255f);
		this.castType = SpellBase.EnumCastType.INSTANTANEOUS;
		this.cooldown = 320;
	}
	
	@Override
	public void cast(EntityPlayer player){
		if (!player.world.isRemote){
			EntityTimeStop timeStop = new EntityTimeStop(player.world);
			timeStop.setPlayer(player.getUniqueID());
			timeStop.setPosition(player.posX, player.posY, player.posZ);
			player.world.spawnEntity(timeStop);
			PacketHandler.INSTANCE.sendToAll(new MessageTimeStopStartFX(player.posX,player.posY+1.0f,player.posZ));
		}
	}

}
