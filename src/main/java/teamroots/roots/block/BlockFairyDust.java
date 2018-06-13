package teamroots.roots.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import teamroots.roots.network.PacketHandler;
import teamroots.roots.network.message.MessageFairyDustBurstFX;
import teamroots.roots.particle.ParticleUtil;
import teamroots.roots.tileentity.TileEntityImbuer;
import teamroots.roots.tileentity.TileEntityMortar;
import teamroots.roots.util.Misc;

public class BlockFairyDust extends BlockBase {

	public BlockFairyDust(Material material, SoundType type, String name, boolean addToTab) {
		super(material, type, name, addToTab);
		this.needsRandomTick = true;
	}

    @Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean b)
    {
    }
	
	@Override
	public boolean requiresUpdates(){
		return true;
	}
	
	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player){
		if (!world.isRemote){
			PacketHandler.INSTANCE.sendToAll(new MessageFairyDustBurstFX(pos.getX()+0.5,pos.getY()+0.5,pos.getZ()+0.5));
		}
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random random){
		List<Float> reds = new ArrayList<Float>();
		List<Float> greens = new ArrayList<Float>();
		List<Float> blues = new ArrayList<Float>();
		reds.add(177f);
		reds.add(255f);
		reds.add(255f);
		reds.add(219f);
		reds.add(122f);
		greens.add(255f);
		greens.add(223f);
		greens.add(163f);
		greens.add(179f);
		greens.add(144f);
		blues.add(117f);
		blues.add(163f);
		blues.add(255f);
		blues.add(255f);
		blues.add(255f);
		
		int ind = Misc.random.nextInt(5);
		
		float r = reds.get(ind);
		float g = greens.get(ind);
		float b = blues.get(ind);
		for (int i = 0; i < 2; i ++){
			ParticleUtil.spawnParticleGlow(world, pos.getX()+0.5f, pos.getY()+0.5f, pos.getZ()+0.5f, (random.nextFloat()-0.5f)*0.003f, (random.nextFloat())*0.015f, (random.nextFloat()-0.5f)*0.003f, r, g, b, 0.25f, 3.0f, 240);
		}
		for (int i = 0; i < 2; i ++){
			ParticleUtil.spawnParticleStar(world, pos.getX()+0.5f, pos.getY()+0.5f, pos.getZ()+0.5f, (random.nextFloat()-0.5f)*0.003f, (random.nextFloat())*0.003f, (random.nextFloat()-0.5f)*0.003f, r, g, b, 0.25f, 3.0f, 240);
		}
	}

}
