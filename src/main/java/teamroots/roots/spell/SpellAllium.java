package teamroots.roots.spell;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;
import teamroots.roots.entity.EntityFireJet;
import teamroots.roots.entity.EntityThornTrap;
import teamroots.roots.network.PacketHandler;
import teamroots.roots.network.message.MessageAcidCloudFX;
import teamroots.roots.network.message.MessageLifeDrainAbsorbFX;

public class SpellAllium extends SpellBase {

	public SpellAllium(String name) {
		super(name,TextFormatting.DARK_GREEN,80f/255f,160f/255f,40f/255f,64f/255f,96f/255f,32f/255f);
		this.castType = SpellBase.EnumCastType.CONTINUOUS;
		this.cooldown = 24;
	}
	
	@Override
	public void cast(EntityPlayer player){
		if (!player.world.isRemote){
			List<EntityLivingBase> entities = player.world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(player.posX-4.0,player.posY-1.0,player.posZ-4.0,player.posX+4.0,player.posY+3.0,player.posZ+4.0));
			for (EntityLivingBase e : entities){
				if (!(e instanceof EntityPlayer && !FMLCommonHandler.instance().getMinecraftServerInstance().isPVPEnabled()) &&
					e.getUniqueID().compareTo(player.getUniqueID()) != 0){
					e.attackEntityFrom(DamageSource.GENERIC.causeMobDamage(player), 1.0f);
					e.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("poison"),80,0));
					e.setRevengeTarget(player);
					e.setLastAttackedEntity(player);
				}
			}
			PacketHandler.INSTANCE.sendToAll(new MessageAcidCloudFX(player.posX,player.posY+player.getEyeHeight(),player.posZ));
		}
	}

}
