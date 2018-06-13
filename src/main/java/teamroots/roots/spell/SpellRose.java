package teamroots.roots.spell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;
import teamroots.roots.entity.EntityFireJet;
import teamroots.roots.entity.EntityThornTrap;

public class SpellRose extends SpellBase {

	public SpellRose(String name) {
		super(name,TextFormatting.RED,255f/255f,32f/255f,64f/255f,32f/255f,255f/255f,96f/255f);
		this.castType = SpellBase.EnumCastType.INSTANTANEOUS;
		this.cooldown = 24;
	}
	
	@Override
	public void cast(EntityPlayer player){
		if (!player.world.isRemote){
			EntityThornTrap trap = new EntityThornTrap(player.world);
			trap.setPlayer(player.getUniqueID());
			trap.setPosition(player.posX+player.getLookVec().x, player.posY+player.getEyeHeight()+player.getLookVec().y, player.posZ+player.getLookVec().z);
			trap.motionX = player.getLookVec().x*0.75f;
			trap.motionY = player.getLookVec().y*0.75f;
			trap.motionZ = player.getLookVec().z*0.75f;
			player.world.spawnEntity(trap);
		}
	}

}
