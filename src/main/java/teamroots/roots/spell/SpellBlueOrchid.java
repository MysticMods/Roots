package teamroots.roots.spell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;
import teamroots.roots.entity.EntityBoost;
import teamroots.roots.entity.EntityFireJet;
import teamroots.roots.entity.EntityTimeStop;
import teamroots.roots.network.PacketHandler;
import teamroots.roots.network.message.MessageTimeStopStartFX;

public class SpellBlueOrchid extends SpellBase {

	public SpellBlueOrchid(String name) {
		super(name,TextFormatting.BLUE,32f/255f,200f/255f,255f/255f,32f/255f,64f/255f,255f/255f);
		this.castType = SpellBase.EnumCastType.INSTANTANEOUS;
		this.cooldown = 56;
	}
	
	@Override
	public void cast(EntityPlayer player){
		if (!player.world.isRemote){
			EntityBoost boost = new EntityBoost(player.world);
			boost.setPlayer(player.getUniqueID());
			boost.setPosition(player.posX, player.posY, player.posZ);
			player.world.spawnEntity(boost);
		}
	}

}
