package teamroots.roots.spell;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;
import teamroots.roots.Constants;
import teamroots.roots.entity.EntityFireJet;
import teamroots.roots.entity.EntityThornTrap;
import teamroots.roots.network.PacketHandler;
import teamroots.roots.network.message.MessageLifeDrainAbsorbFX;

public class SpellPoppy extends SpellBase {

	public SpellPoppy(String name) {
		super(name,TextFormatting.DARK_RED,128f/255f,32f/255f,32f/255f,32f/255f,32f/255f,32f/255f);
		this.castType = SpellBase.EnumCastType.INSTANTANEOUS;
		this.cooldown = 80;
	}
	
	@Override
	public void cast(EntityPlayer player){
		if (!player.world.isRemote){
			boolean foundTarget = false;
			for (int i = 0; i < 4 && !foundTarget; i ++){
				double x = player.posX+player.getLookVec().x*3.0*(float)i;
				double y = player.posY+player.getEyeHeight()+player.getLookVec().y*3.0*(float)i;
				double z = player.posZ+player.getLookVec().z*3.0*(float)i;
				List<EntityLivingBase> entities = player.world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(x-2.0,y-2.0,z-2.0,x+2.0,y+2.0,z+2.0));
				for (EntityLivingBase e : entities){
					if (e.getUniqueID().compareTo(player.getUniqueID()) != 0 && !foundTarget){
						foundTarget = true;
						e.getEntityData().setInteger(Constants.MIND_WARD_TAG, 400);
					}
				}
			}
		}
	}

}
