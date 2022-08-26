package thaumcraft.api.golems;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.golems.seals.ISeal;
import thaumcraft.api.golems.seals.ISealEntity;
import thaumcraft.api.golems.seals.SealPos;
import thaumcraft.api.golems.tasks.Task;

public class GolemHelper {

	/**
	 * Make sure to register your seals during the preInit phase before TC is loaded
	 * @param seal
	 */
	public static void registerSeal(ISeal seal) {
		ThaumcraftApi.internalMethods.registerSeal(seal);
	}

	public static ISeal getSeal(String key) {
		return ThaumcraftApi.internalMethods.getSeal(key);
	}
	
	public static ItemStack getSealStack(String key) {
		return ThaumcraftApi.internalMethods.getSealStack(key);
	}

	public static ISealEntity getSealEntity(int dim, SealPos pos) {
		return ThaumcraftApi.internalMethods.getSealEntity(dim, pos);
	}
	
	public static void addGolemTask(int dim, Task task) {
		ThaumcraftApi.internalMethods.addGolemTask(dim, task);
	}
	
	public static HashMap<Integer,ArrayList<ProvisionRequest>> provisionRequests = new HashMap<>();
	
	/**
	 * 
	 * @param world
	 * @param seal
	 * @param stack the stack requested. Can accept wildcard values.
	 */
	public static void requestProvisioning(World world, ISealEntity seal, ItemStack stack) {
		if (!provisionRequests.containsKey(world.provider.getDimension()))
			provisionRequests.put(world.provider.getDimension(), new ArrayList<ProvisionRequest>());
		ArrayList<ProvisionRequest> list = provisionRequests.get(world.provider.getDimension());
		ProvisionRequest pr = new ProvisionRequest(seal,stack.copy());
		if (!list.contains(pr)) {
			list.add(pr);
		}
	}
	
	/**
	 * 
	 * @param world
	 * @param pos
	 * @param side
	 * @param stack the stack requested. Can accept wildcard values.
	 */
	public static void requestProvisioning(World world, BlockPos pos, EnumFacing side, ItemStack stack) {
		if (!provisionRequests.containsKey(world.provider.getDimension()))
			provisionRequests.put(world.provider.getDimension(), new ArrayList<ProvisionRequest>());
		ArrayList<ProvisionRequest> list = provisionRequests.get(world.provider.getDimension());
		ProvisionRequest pr = new ProvisionRequest(pos, side, stack.copy());
		if (!list.contains(pr)) {
			list.add(pr);
		}
	}
	
	/**
	 * 
	 * @param world
	 * @param entity
	 * @param stack the stack requested. Can accept wildcard values.
	 */
	public static void requestProvisioning(World world, Entity entity, ItemStack stack) {
		if (!provisionRequests.containsKey(world.provider.getDimension()))
			provisionRequests.put(world.provider.getDimension(), new ArrayList<ProvisionRequest>());
		ArrayList<ProvisionRequest> list = provisionRequests.get(world.provider.getDimension());
		ProvisionRequest pr = new ProvisionRequest(entity, stack.copy());
		if (!list.contains(pr)) {
			list.add(pr);
		}
	}
	
	/**
	 * This method is used to get a single blockpos from within a designated seal area.
	 * This method is best used if you want to increment through the blocks in the area. 
	 * @param seal
	 * @param count a value used to derive a specific pos 
	 * @return
	 */
	public static BlockPos getPosInArea(ISealEntity seal, int count) {
		int xx = 1 + (seal.getArea().getX()-1) * (seal.getSealPos().face.getXOffset()==0?2:1);
		int yy = 1 + (seal.getArea().getY()-1) * (seal.getSealPos().face.getYOffset()==0?2:1);
		int zz = 1 + (seal.getArea().getZ()-1) * (seal.getSealPos().face.getZOffset()==0?2:1);
		
		int qx = seal.getSealPos().face.getXOffset()!=0?seal.getSealPos().face.getXOffset():1;
		int qy = seal.getSealPos().face.getYOffset()!=0?seal.getSealPos().face.getYOffset():1;
		int qz = seal.getSealPos().face.getZOffset()!=0?seal.getSealPos().face.getZOffset():1;
		
		int y = qy*((count/zz)/xx)%yy + seal.getSealPos().face.getYOffset();
		int x = qx*(count/zz)%xx + seal.getSealPos().face.getXOffset();
		int z = qz*count%zz + seal.getSealPos().face.getZOffset();
				
		BlockPos p = seal.getSealPos().pos.add(
				x - (seal.getSealPos().face.getXOffset()==0?xx/2:0),
				y - (seal.getSealPos().face.getYOffset()==0?yy/2:0),
				z - (seal.getSealPos().face.getZOffset()==0?zz/2:0));
		
		return p;
	}

	
	/**
	 * Returns the designated seal area as a AxisAlignedBB
	 * @param seal
	 * @return
	 */
	public static AxisAlignedBB getBoundsForArea(ISealEntity seal) {
		return new AxisAlignedBB(
				seal.getSealPos().pos.getX(), seal.getSealPos().pos.getY(), seal.getSealPos().pos.getZ(), 
				seal.getSealPos().pos.getX()+1, seal.getSealPos().pos.getY()+1, seal.getSealPos().pos.getZ()+1)
				.offset(
					seal.getSealPos().face.getXOffset(),
					seal.getSealPos().face.getYOffset(),
					seal.getSealPos().face.getZOffset())
				.expand(
					seal.getSealPos().face.getXOffset()!=0?(seal.getArea().getX()-1) * seal.getSealPos().face.getXOffset():0,
					seal.getSealPos().face.getYOffset()!=0?(seal.getArea().getY()-1) * seal.getSealPos().face.getYOffset():0,
					seal.getSealPos().face.getZOffset()!=0?(seal.getArea().getZ()-1) * seal.getSealPos().face.getZOffset():0)
				.grow(
					seal.getSealPos().face.getXOffset()==0?seal.getArea().getX()-1:0,
					seal.getSealPos().face.getYOffset()==0?seal.getArea().getY()-1:0,
					seal.getSealPos().face.getZOffset()==0?seal.getArea().getZ()-1:0 );
	}
	
	

}
