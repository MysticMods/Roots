package teamroots.roots.spell;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import teamroots.roots.entity.EntityFireJet;
import teamroots.roots.network.PacketHandler;
import teamroots.roots.network.message.MessageDandelionCastFX;
import teamroots.roots.network.message.MessageSanctuaryBurstFX;
import teamroots.roots.network.message.MessageSanctuaryRingFX;
import teamroots.roots.network.message.MessageShatterBurstFX;
import teamroots.roots.util.Misc;

public class SpellAzureBluet extends SpellBase {

	public SpellAzureBluet(String name) {
		super(name,TextFormatting.GRAY,96f/255f,96f/255f,96f/255f,192f/255f,192f/255f,192f/255f);
		this.castType = SpellBase.EnumCastType.INSTANTANEOUS;
		this.cooldown = 20;
	}
	
	@Override
	public void cast(EntityPlayer player){
		if (!player.world.isRemote){
			RayTraceResult result = player.world.rayTraceBlocks(player.getPositionVector().addVector(0, player.getEyeHeight(), 0),player.getLookVec().scale(8.0f).add(player.getPositionVector().addVector(0, player.getEyeHeight(), 0)));
			if (result != null){
				if (result.typeOfHit == RayTraceResult.Type.BLOCK){
					BlockPos pos = result.getBlockPos();
		    		IBlockState state = player.world.getBlockState(pos);
		    		boolean doParticles = false;
		    		if (state.getBlockHardness(player.world, pos) > 0){
		    			player.world.destroyBlock(pos, true);
		    			player.world.notifyBlockUpdate(pos, state, Blocks.AIR.getDefaultState(), 8);
		    			doParticles = true;
		    		}
					for (int i = 0; i < 4; i ++){
						pos = result.getBlockPos().offset(EnumFacing.getFront(Misc.random.nextInt(6)));
			    		state = player.world.getBlockState(pos);
			    		if (state.getBlockHardness(player.world, pos) > 0){
				    		player.world.destroyBlock(pos, true);
				    		player.world.notifyBlockUpdate(pos, state, Blocks.AIR.getDefaultState(), 8);
			    		}
					}
					if (doParticles){
						float offX = 0.5f*(float)Math.sin(Math.toRadians(-90.0f-player.rotationYaw));
						float offZ = 0.5f*(float)Math.cos(Math.toRadians(-90.0f-player.rotationYaw));
						PacketHandler.INSTANCE.sendToAll(new MessageShatterBurstFX(player.posX+offX, player.posY+player.getEyeHeight(), player.posZ+offZ, result.hitVec.x,result.hitVec.y,result.hitVec.z));
					}
				}
				else if (result.typeOfHit == RayTraceResult.Type.ENTITY){
					if (result.entityHit instanceof EntityLivingBase){
        				((EntityLivingBase)result.entityHit).attackEntityFrom(DamageSource.MAGIC.causeMobDamage(player), 3.0f);
        				((EntityLivingBase)result.entityHit).setLastAttackedEntity(player);
        				((EntityLivingBase)result.entityHit).setRevengeTarget(player);
    					float offX = 0.5f*(float)Math.sin(Math.toRadians(-90.0f-player.rotationYaw));
    					float offZ = 0.5f*(float)Math.cos(Math.toRadians(-90.0f-player.rotationYaw));
						PacketHandler.INSTANCE.sendToAll(new MessageShatterBurstFX(player.posX+offX, player.posY+player.getEyeHeight(), player.posZ+offZ, result.hitVec.x,result.hitVec.y,result.hitVec.z));
					}	
				}
			}
		}
	}

}
