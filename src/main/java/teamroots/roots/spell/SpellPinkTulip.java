package teamroots.roots.spell;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;
import teamroots.roots.entity.EntityFireJet;
import teamroots.roots.entity.EntityThornTrap;
import teamroots.roots.network.PacketHandler;
import teamroots.roots.network.message.MessageLifeDrainAbsorbFX;

public class SpellPinkTulip extends SpellBase {

	public SpellPinkTulip(String name) {
		super(name,TextFormatting.DARK_GRAY,144f/255f,32f/255f,64f/255f,255f/255f,196f/255f,240f/255f);
		this.castType = SpellBase.EnumCastType.CONTINUOUS;
		this.cooldown = 28;
	}
	
	@Override
	public void cast(EntityPlayer player){
		if (!player.world.isRemote){
			boolean foundTarget = false;
			PacketHandler.INSTANCE.sendToAll(new MessageLifeDrainAbsorbFX(player.getUniqueID(),player.posX,player.posY+player.getEyeHeight(),player.posZ));
			for (int i = 0; i < 4 && !foundTarget; i ++){
				double x = player.posX+player.getLookVec().x*3.0*(float)i;
				double y = player.posY+player.getEyeHeight()+player.getLookVec().y*3.0*(float)i;
				double z = player.posZ+player.getLookVec().z*3.0*(float)i;
				List<EntityLivingBase> entities = player.world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(x-2.0,y-2.0,z-2.0,x+2.0,y+2.0,z+2.0));
				for (EntityLivingBase e : entities){
					if (!(e instanceof EntityPlayer && !FMLCommonHandler.instance().getMinecraftServerInstance().isPVPEnabled()) &&
							e.getUniqueID().compareTo(player.getUniqueID()) != 0){
						foundTarget = true;
						if (e.hurtTime <= 0 && !e.isDead){
							e.attackEntityFrom(DamageSource.WITHER.causeMobDamage(player), 1.0f);
							e.setRevengeTarget(player);
							e.setLastAttackedEntity(player);
							player.heal(0.5f);
						}
					}
				}
			}
		}
	}

}
