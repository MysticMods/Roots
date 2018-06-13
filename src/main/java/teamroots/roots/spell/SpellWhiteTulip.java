package teamroots.roots.spell;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameType;
import teamroots.roots.Constants;
import teamroots.roots.entity.EntityFireJet;
import teamroots.roots.entity.EntityTimeStop;
import teamroots.roots.network.PacketHandler;
import teamroots.roots.network.message.MessageLightDrifterFX;
import teamroots.roots.network.message.MessageLightDrifterSync;
import teamroots.roots.network.message.MessageRadianceBeamFX;
import teamroots.roots.network.message.MessageTimeStopStartFX;

public class SpellWhiteTulip extends SpellBase {

	public SpellWhiteTulip(String name) {
		super(name,TextFormatting.AQUA,196f/255f,240f/255f,255f/255f,32f/255f,64f/255f,96f/255f);
		this.castType = SpellBase.EnumCastType.INSTANTANEOUS;
		this.cooldown = 200;
	}
	
	@Override
	public void cast(EntityPlayer player){
		if (!player.world.isRemote){
			player.capabilities.disableDamage = true;
			player.capabilities.allowFlying = true;
			player.noClip = true;
			player.getEntityData().setInteger(Constants.LIGHT_DRIFTER_TAG, 100);
			player.getEntityData().setDouble(Constants.LIGHT_DRIFTER_X, player.posX);
			player.getEntityData().setDouble(Constants.LIGHT_DRIFTER_Y, player.posY);
			player.getEntityData().setDouble(Constants.LIGHT_DRIFTER_Z, player.posZ);
			if (player.capabilities.isCreativeMode){
				player.getEntityData().setInteger(Constants.LIGHT_DRIFTER_MODE, GameType.CREATIVE.getID());
			}
			else {
				player.getEntityData().setInteger(Constants.LIGHT_DRIFTER_MODE, GameType.SURVIVAL.getID());
			}
			player.setGameType(GameType.SPECTATOR);
			PacketHandler.INSTANCE.sendToAll(new MessageLightDrifterSync(player.getUniqueID(),player.posX,player.posY,player.posZ,true,GameType.SPECTATOR.getID()));
			PacketHandler.INSTANCE.sendToAll(new MessageLightDrifterFX(player.posX,player.posY+1.0f,player.posZ));
		}
	}

}
