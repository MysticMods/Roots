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
import net.minecraftforge.fml.common.FMLCommonHandler;
import teamroots.roots.entity.EntityFireJet;
import teamroots.roots.entity.EntityTimeStop;
import teamroots.roots.network.PacketHandler;
import teamroots.roots.network.message.MessageRadianceBeamFX;
import teamroots.roots.network.message.MessageTimeStopStartFX;

public class SpellSunflower extends SpellBase {

	public SpellSunflower(String name) {
		super(name,TextFormatting.WHITE,255f/255f,255f/255f,64f/255f,255f/255f,255f/255f,192f/255f);
		this.castType = SpellBase.EnumCastType.CONTINUOUS;
		this.cooldown = 40;
	}
	
	@Override
	public void cast(EntityPlayer player){
		if (!player.world.isRemote && player.ticksExisted % 2 == 0){
			float distance = 32;
			RayTraceResult result = player.world.rayTraceBlocks(player.getPositionVector().addVector(0,player.getEyeHeight(),0), player.getPositionVector().addVector(0,player.getEyeHeight(),0).add(player.getLookVec().scale(distance)));
			Vec3d direction = player.getLookVec();
			ArrayList<Vec3d> positions = new ArrayList<Vec3d>();
			float offX = 0.5f*(float)Math.sin(Math.toRadians(-90.0f-player.rotationYaw));
			float offZ = 0.5f*(float)Math.cos(Math.toRadians(-90.0f-player.rotationYaw));
			positions.add(new Vec3d(player.posX+offX,player.posY+player.getEyeHeight(),player.posZ+offZ));
			PacketHandler.INSTANCE.sendToAll(new MessageRadianceBeamFX(player.getUniqueID(),player.posX,player.posY+1.0f,player.posZ));
			if (result != null){
				positions.add(result.hitVec);
				if (result.typeOfHit == RayTraceResult.Type.BLOCK){
					Vec3i hitSide = result.sideHit.getDirectionVec();
					float xCoeff = 1f;
					if (hitSide.getX() != 0){
						xCoeff = -1f;
					}
					float yCoeff = 1f;
					if (hitSide.getY() != 0){
						yCoeff = -1f;
					}
					float zCoeff = 1f;
					if (hitSide.getZ() != 0){
						zCoeff = -1f;
					}
					direction = new Vec3d(direction.x*xCoeff,direction.y*yCoeff,direction.z*zCoeff);
					distance -= result.hitVec.subtract(player.getPositionVector()).lengthVector();
					if (distance > 0){
						RayTraceResult result2 = player.world.rayTraceBlocks(result.hitVec, result.hitVec.add(direction.scale(distance)));
						if (result2 != null){
							positions.add(result2.hitVec);
							if (result2.typeOfHit == RayTraceResult.Type.BLOCK){
								hitSide = result2.sideHit.getDirectionVec();
		    					xCoeff = 1f;
		    					if (hitSide.getX() != 0){
		    						xCoeff = -1f;
		    					}
		    					yCoeff = 1f;
		    					if (hitSide.getY() != 0){
		    						yCoeff = -1f;
		    					}
		    					zCoeff = 1f;
		    					if (hitSide.getZ() != 0){
		    						zCoeff = -1f;
		    					}
								direction = new Vec3d(direction.x*xCoeff,direction.y*yCoeff,direction.z*zCoeff);
								distance -= result2.hitVec.subtract(player.getPositionVector()).lengthVector();
								if (distance > 0){
									RayTraceResult result3 = player.world.rayTraceBlocks(result2.hitVec, result2.hitVec.add(direction.scale(distance)));
									if (result3 != null){
										positions.add(result3.hitVec);
									}
									else {
										positions.add(result2.hitVec.add(direction.scale(distance)));
									}
								}
							}
						}
						else {
							positions.add(result.hitVec.add(direction.scale(distance)));
						}
					}
				}
			}
			else {
				positions.add(player.getPositionVector().addVector(0,player.getEyeHeight(),0).add(player.getLookVec().scale(distance)));
			}
			if (positions.size() > 1){
				for (int i = 0; i < positions.size()-1; i ++){
					double bx = Math.abs(positions.get(i+1).x-positions.get(i).x)*0.1f;
					double by = Math.abs(positions.get(i+1).y-positions.get(i).y)*0.1f;
					double bz = Math.abs(positions.get(i+1).z-positions.get(i).z)*0.1f;
					for (float j = 0; j < 1; j += 0.1f){
						double x = positions.get(i).x*(1.0f-j)+positions.get(i+1).x*j;
						double y = positions.get(i).y*(1.0f-j)+positions.get(i+1).y*j;
						double z = positions.get(i).z*(1.0f-j)+positions.get(i+1).z*j;
						List<EntityLivingBase> entities = player.world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(x-bx,y-by,z-bz,x+bx,y+by,z+bz));
						for (EntityLivingBase e : entities){
							if (!(e instanceof EntityPlayer && !FMLCommonHandler.instance().getMinecraftServerInstance().isPVPEnabled()) &&
									e.getUniqueID().compareTo(player.getUniqueID()) != 0){
								e.attackEntityFrom(DamageSource.MAGIC.causeMobDamage(player), 4.0f);
								if (e.isEntityUndead()){
									e.attackEntityFrom(DamageSource.MAGIC.causeMobDamage(player), 2.0f);
								}
								e.setRevengeTarget(player);
								e.setLastAttackedEntity(player);
							}
						}
					}
				}
			}
		}
	}

}
