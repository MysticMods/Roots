package teamroots.roots.spell;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;
import teamroots.roots.entity.EntityFireJet;
import teamroots.roots.network.PacketHandler;
import teamroots.roots.network.message.MessageSanctuaryBurstFX;
import teamroots.roots.network.message.MessageSanctuaryRingFX;

public class SpellRedTulip extends SpellBase {

	public SpellRedTulip(String name) {
		super(name,TextFormatting.DARK_PURPLE,208f/255f,16f/255f,80f/255f,224f/255f,32f/255f,144f/255f);
		this.castType = SpellBase.EnumCastType.CONTINUOUS;
		this.cooldown = 60;
	}
	
	@Override
	public void cast(EntityPlayer player){
		List<Entity> entities = player.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(player.posX-4.0,player.posY-4.0,player.posZ-4.0,player.posX+4.0,player.posY+5.0,player.posZ+4.0));
		if (entities.size() > 0){
			for (Entity e : entities){
				if (e.getUniqueID().compareTo(player.getUniqueID()) != 0){
					if (Math.pow((e.posX-player.posX),2)+Math.pow((e.posY-player.posY),2)+Math.pow((e.posZ-player.posZ),2) < 9.0f){
						e.motionX = 0.125f*(e.posX-player.posX);
						e.motionY = 0.125f*(e.posY-player.posY);
						e.motionZ = 0.125f*(e.posZ-player.posZ);
						e.velocityChanged = true;
						if (!e.isInvisible()){
							PacketHandler.INSTANCE.sendToAll(new MessageSanctuaryBurstFX(e.posX,e.posY+0.6f*e.getEyeHeight(),e.posZ));
						}
					}
				}
			}
		}
		if (player.ticksExisted % 2 == 0){
			PacketHandler.INSTANCE.sendToAll(new MessageSanctuaryRingFX(player.posX,player.posY+0.875f,player.posZ));
		}
	}

}
