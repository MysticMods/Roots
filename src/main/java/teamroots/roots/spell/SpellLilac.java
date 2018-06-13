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
import teamroots.roots.network.message.MessageLifeInfusionFX;
import teamroots.roots.network.message.MessageSanctuaryBurstFX;
import teamroots.roots.network.message.MessageSanctuaryRingFX;
import teamroots.roots.network.message.MessageShatterBurstFX;
import teamroots.roots.util.Misc;

public class SpellLilac extends SpellBase {

	public SpellLilac(String name) {
		super(name,TextFormatting.GREEN,48f/255f,255f/255f,48f/255f,192f/255f,255f/255f,192f/255f);
		this.castType = SpellBase.EnumCastType.CONTINUOUS;
		this.cooldown = 16;
	}
	
	@Override
	public void cast(EntityPlayer player){
		if (!player.world.isRemote){
			RayTraceResult result = player.world.rayTraceBlocks(player.getPositionVector().addVector(0, player.getEyeHeight(), 0),player.getLookVec().scale(8.0f).add(player.getPositionVector().addVector(0, player.getEyeHeight(), 0)));
			if (result != null){
				if (result.typeOfHit == RayTraceResult.Type.BLOCK){
					BlockPos pos = result.getBlockPos();
		    		IBlockState state = player.world.getBlockState(pos);
		    		for (int i = 0; i < 1; i ++){
		    			state.getBlock().randomTick(player.world, pos, state, Misc.random);
		    		}
		    		PacketHandler.INSTANCE.sendToAll(new MessageLifeInfusionFX(pos.getX(),pos.getY(),pos.getZ()));
				}
			}
		}
	}

}
