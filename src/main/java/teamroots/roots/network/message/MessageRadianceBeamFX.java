package teamroots.roots.network.message;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import teamroots.roots.entity.EntityAuspiciousPoint;
import teamroots.roots.entity.RenderAuspiciousPoint;
import teamroots.roots.particle.ParticleUtil;
import teamroots.roots.spell.SpellBase;
import teamroots.roots.spell.SpellRegistry;

public class MessageRadianceBeamFX implements IMessage {
	public static Random random = new Random();
	double posX = 0, posY = 0, posZ = 0;
	UUID id = null;
	
	public MessageRadianceBeamFX(){
		super();
	}
	
	public MessageRadianceBeamFX(UUID id, double x, double y, double z){
		super();
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		this.id = id;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		posX = buf.readDouble();
		posY = buf.readDouble();
		posZ = buf.readDouble();
		id = new UUID(buf.readLong(),buf.readLong());
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeDouble(posX);
		buf.writeDouble(posY);
		buf.writeDouble(posZ);
		buf.writeLong(id.getMostSignificantBits());
		buf.writeLong(id.getLeastSignificantBits());
	}

	public static float getColorCycle(float ticks){
		return (MathHelper.sin((float)Math.toRadians(ticks))+1.0f)/2.0f;
	}

    public static class MessageHolder implements IMessageHandler<MessageRadianceBeamFX,IMessage>
    {
    	@SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(final MessageRadianceBeamFX message, final MessageContext ctx) {
    		World world = Minecraft.getMinecraft().world;
			EntityPlayer player = world.getPlayerEntityByUUID(message.id);
    		if (player != null){
    			float distance = 32;
    			RayTraceResult result = player.world.rayTraceBlocks(player.getPositionVector().addVector(0,player.getEyeHeight(),0), player.getPositionVector().addVector(0,player.getEyeHeight(),0).add(player.getLookVec().scale(distance)));
    			Vec3d direction = player.getLookVec();
    			ArrayList<Vec3d> positions = new ArrayList<Vec3d>();
    			float offX = 0.5f*(float)Math.sin(Math.toRadians(-90.0f-player.rotationYaw));
    			float offZ = 0.5f*(float)Math.cos(Math.toRadians(-90.0f-player.rotationYaw));
    			positions.add(new Vec3d(player.posX+offX,player.posY+player.getEyeHeight(),player.posZ+offZ));
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
    						RayTraceResult result2 = player.world.rayTraceBlocks(result.hitVec.add(direction.scale(0.1)), result.hitVec.add(direction.scale(distance)));
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
    									RayTraceResult result3 = player.world.rayTraceBlocks(result2.hitVec.add(direction.scale(0.1)), result2.hitVec.add(direction.scale(distance)));
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
    				double totalDist = 0;
    				for (int i = 0; i < positions.size()-1; i ++){
    					totalDist += positions.get(i).subtract(positions.get(i+1)).lengthVector();
    				}
    				double alphaDist = 0;
    				for (int i = 0; i < positions.size()-1; i ++){
    					double dist = positions.get(i).subtract(positions.get(i+1)).lengthVector();
    					for (double j = 0; j < dist; j += 0.15){
    						double x = positions.get(i).x*(1.0-j/dist)+positions.get(i+1).x*(j/dist);
    						double y = positions.get(i).y*(1.0-j/dist)+positions.get(i+1).y*(j/dist);
    						double z = positions.get(i).z*(1.0-j/dist)+positions.get(i+1).z*(j/dist);
    						alphaDist += 0.15;
    						
    						if (random.nextBoolean()){
    							ParticleUtil.spawnParticleStar(world, (float)x, (float)y, (float)z, 0, 0, 0, SpellRegistry.spell_sunflower.red1*255.0f,SpellRegistry.spell_sunflower.green1*255.0f,SpellRegistry.spell_sunflower.blue1*255.0f, 0.75f*(float)(1.0f-alphaDist/totalDist), 3f+3f*random.nextFloat(), 14);
    						}
    						else {
    							ParticleUtil.spawnParticleStar(world, (float)x, (float)y, (float)z, 0, 0, 0, SpellRegistry.spell_sunflower.red2*255.0f,SpellRegistry.spell_sunflower.green2*255.0f,SpellRegistry.spell_sunflower.blue2*255.0f, 0.75f*(float)(1.0f-alphaDist/totalDist), 3f+3f*random.nextFloat(), 14);
    						}
    					}
    				}
    			}
    		}
    		return null;
        }
    }

}
