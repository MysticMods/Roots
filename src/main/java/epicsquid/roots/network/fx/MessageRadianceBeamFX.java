package epicsquid.roots.network.fx;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.network.ClientMessageHandler;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellRadiance;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.UUID;

public class MessageRadianceBeamFX implements IMessage {
	private double posX = 0, posY = 0, posZ = 0;
	private boolean dim = false;
	private UUID id = null;
	
	public MessageRadianceBeamFX() {
		super();
	}
	
	public MessageRadianceBeamFX(UUID id, double x, double y, double z, boolean dim) {
		super();
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		this.id = id;
		this.dim = dim;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		posX = buf.readDouble();
		posY = buf.readDouble();
		posZ = buf.readDouble();
		id = new UUID(buf.readLong(), buf.readLong());
		dim = buf.readBoolean();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeDouble(posX);
		buf.writeDouble(posY);
		buf.writeDouble(posZ);
		buf.writeLong(id.getMostSignificantBits());
		buf.writeLong(id.getLeastSignificantBits());
		buf.writeBoolean(dim);
	}
	
	public static float getColorCycle(float ticks) {
		return (MathHelper.sin((float) Math.toRadians(ticks)) + 1.0f) / 2.0f;
	}
	
	public static class MessageHolder extends ClientMessageHandler<MessageRadianceBeamFX> {
		@SideOnly(Side.CLIENT)
		@Override
		protected void handleMessage(final MessageRadianceBeamFX message, final MessageContext ctx) {
			World world = Minecraft.getMinecraft().world;
			EntityPlayer player = world.getPlayerEntityByUUID(message.id);
			if (player != null) {
				float distance = SpellRadiance.instance.distance;
				RayTraceResult result = player.world.rayTraceBlocks(player.getPositionVector().add(0, player.getEyeHeight(), 0), player.getPositionVector().add(0, player.getEyeHeight(), 0).add(player.getLookVec().scale(distance)), false, true, true);
				Vec3d direction = player.getLookVec();
				ArrayList<Vec3d> positions = new ArrayList<>();
				float offX = 0.5f * (float) Math.sin(Math.toRadians(-90.0f - player.rotationYaw));
				float offZ = 0.5f * (float) Math.cos(Math.toRadians(-90.0f - player.rotationYaw));
				positions.add(new Vec3d(player.posX + offX, player.posY + player.getEyeHeight(), player.posZ + offZ));
				if (result != null) {
					positions.add(result.hitVec);
					if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
						Vec3i hitSide = result.sideHit.getDirectionVec();
						float xCoeff = 1f;
						if (hitSide.getX() != 0) {
							xCoeff = -1f;
						}
						float yCoeff = 1f;
						if (hitSide.getY() != 0) {
							yCoeff = -1f;
						}
						float zCoeff = 1f;
						if (hitSide.getZ() != 0) {
							zCoeff = -1f;
						}
						direction = new Vec3d(direction.x * xCoeff, direction.y * yCoeff, direction.z * zCoeff);
						distance -= result.hitVec.subtract(player.getPositionVector()).length();
						if (distance > 0) {
							RayTraceResult result2 = player.world.rayTraceBlocks(result.hitVec.add(direction.scale(0.1)), result.hitVec.add(direction.scale(distance)));
							if (result2 != null) {
								positions.add(result2.hitVec);
								if (result2.typeOfHit == RayTraceResult.Type.BLOCK) {
									hitSide = result2.sideHit.getDirectionVec();
									xCoeff = 1f;
									if (hitSide.getX() != 0) {
										xCoeff = -1f;
									}
									yCoeff = 1f;
									if (hitSide.getY() != 0) {
										yCoeff = -1f;
									}
									zCoeff = 1f;
									if (hitSide.getZ() != 0) {
										zCoeff = -1f;
									}
									direction = new Vec3d(direction.x * xCoeff, direction.y * yCoeff, direction.z * zCoeff);
									distance -= result2.hitVec.subtract(player.getPositionVector()).length();
									if (distance > 0) {
										RayTraceResult result3 = player.world
												.rayTraceBlocks(result2.hitVec.add(direction.scale(0.1)), result2.hitVec.add(direction.scale(distance)));
										if (result3 != null) {
											positions.add(result3.hitVec);
										} else {
											positions.add(result2.hitVec.add(direction.scale(distance)));
										}
									}
								}
							} else {
								positions.add(result.hitVec.add(direction.scale(distance)));
							}
						}
					}
				} else {
					positions.add(player.getPositionVector().add(0, player.getEyeHeight(), 0).add(player.getLookVec().scale(distance)));
				}
				if (positions.size() > 1) {
					double totalDist = 0;
					for (int i = 0; i < positions.size() - 1; i++) {
						totalDist += positions.get(i).subtract(positions.get(i + 1)).length();
					}
					double alphaDist = 0;
					float base = 0.05f;
					if (message.dim) {
						base = 0.75f;
					}
					for (int i = 0; i < positions.size() - 1; i++) {
						double dist = positions.get(i).subtract(positions.get(i + 1)).length();
						for (double j = 0; j < dist; j += 0.15) {
							double x = positions.get(i).x * (1.0 - j / dist) + positions.get(i + 1).x * (j / dist);
							double y = positions.get(i).y * (1.0 - j / dist) + positions.get(i + 1).y * (j / dist);
							double z = positions.get(i).z * (1.0 - j / dist) + positions.get(i + 1).z * (j / dist);
							alphaDist += 0.15;
							if (Util.rand.nextBoolean()) {
								ParticleUtil.spawnParticleStarNoGravity(world, (float) x, (float) y, (float) z, 0, 0, 0, SpellRadiance.instance.getRed1() * 255.0f, SpellRadiance.instance.getGreen1() * 255.0f, SpellRadiance.instance.getBlue1() * 255.0f, base * (float) (1.0f - alphaDist / totalDist), 3f + 3f * Util.rand.nextFloat(), 14);
							} else {
								ParticleUtil.spawnParticleStarNoGravity(world, (float) x, (float) y, (float) z, 0, 0, 0, SpellRadiance.instance.getRed2() * 255.0f, SpellRadiance.instance.getGreen2() * 255.0f, SpellRadiance.instance.getBlue2() * 255.0f, base * (float) (1.0f - alphaDist / totalDist), 3f + 3f * Util.rand.nextFloat(), 14);
							}
						}
					}
				}
			}
		}
	}
}