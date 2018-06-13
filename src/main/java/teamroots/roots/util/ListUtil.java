package teamroots.roots.util;

import java.util.Comparator;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

public class ListUtil {
	public static class StateComparator implements Comparator<IBlockState> {
		@Override
		public int compare(IBlockState arg0, IBlockState arg1) {
			return (arg0.getBlock().getRegistryName().toString()+"_"+arg0.getBlock().getMetaFromState(arg0)).compareTo(arg1.getBlock().getRegistryName().toString()+"_"+arg1.getBlock().getMetaFromState(arg1));
		}
	}
	public static class StackComparator implements Comparator<ItemStack> {
		@Override
		public int compare(ItemStack arg0, ItemStack arg1) {
			return (arg0.getItem().getRegistryName().toString()+"_"+arg0.getItemDamage()).compareTo(arg1.getItem().getRegistryName().toString()+"_"+arg1.getItemDamage());
		}
	}
	
	public static StateComparator stateComparator = new StateComparator();
	
	public static boolean stateListsMatch(List<IBlockState> list1, List<IBlockState> list2){
		list1.sort(stateComparator);
		list2.sort(stateComparator);
		boolean doMatch = list1.size() == list2.size();
		if (doMatch){
			for (int i = 0; i < list1.size(); i ++){
				if (list1.get(i).getBlock() != list2.get(i).getBlock() || list1.get(i).getBlock().getMetaFromState(list1.get(i)) != list2.get(i).getBlock().getMetaFromState(list2.get(i))){
					doMatch = false;
				}
			}
		}
		return doMatch;
	}
	
	public static StackComparator stackComparator = new StackComparator();
	
	public static class StackSizeComparator implements Comparator<ItemStack> {
		@Override
		public int compare(ItemStack arg0, ItemStack arg1) {
			return Math.max(-1, Math.min(1, arg0.getCount()-arg1.getCount()));
		}
	}
	
	public static StackSizeComparator stackSizeComparator = new StackSizeComparator();
	
	public static boolean stackListsMatch(List<ItemStack> list1, List<ItemStack> list2){
		list1.sort(stackComparator);
		list2.sort(stackComparator);
		boolean doMatch = list1.size() == list2.size();
		if (doMatch){
			for (int i = 0; i < list1.size(); i ++){
				if (list1.get(i).getItem() != list2.get(i).getItem() || list1.get(i).getItemDamage() != list2.get(i).getItemDamage()){
					doMatch = false;
				}
			}
		}
		return doMatch;
	}
}
