package teamroots.roots.recipe;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import teamroots.roots.util.ListUtil;

public class MoonlightRecipe {
	public IBlockState coreState;
	public IBlockStateCondition coreCondition;
	public List<IBlockState> outerStates = new ArrayList<IBlockState>();
	public List<IBlockStateCondition> outerConditions = new ArrayList<IBlockStateCondition>();
	public IBlockState resultState;
	
	public static interface IBlockStateCondition {
		public boolean match(IBlockState state1, IBlockState state2);
	}
	
	public static class ConditionAlways implements IBlockStateCondition {
		@Override
		public boolean match(IBlockState state1, IBlockState state2) {
			return true;
		}
	}
	
	public static class ConditionPropertyMatch implements IBlockStateCondition {
		public IProperty property;
		public ConditionPropertyMatch(IProperty property){
			this.property = property;
		}
		@Override
		public boolean match(IBlockState state1, IBlockState state2) {
			return state1.getValue(property) == state2.getValue(property);
		}
	}
	
	public static class ConditionMetaMatch implements IBlockStateCondition {
		@Override
		public boolean match(IBlockState state1, IBlockState state2) {
			return state1.getBlock().getMetaFromState(state1) == state2.getBlock().getMetaFromState(state2);
		}
	}
	
	public MoonlightRecipe(IBlockState core, IBlockStateCondition coreCond,
			IBlockState outer1, IBlockStateCondition outer1Cond, 
			IBlockState outer2, IBlockStateCondition outer2Cond, 
			IBlockState outer3, IBlockStateCondition outer3Cond, 
			IBlockState outer4, IBlockStateCondition outer4Cond, 
			IBlockState result){
		this.coreState = core;
		this.coreCondition = coreCond;
		this.outerStates.add(outer1);
		this.outerStates.add(outer2);
		this.outerStates.add(outer3);
		this.outerStates.add(outer4);
		this.outerConditions.add(outer1Cond);
		this.outerConditions.add(outer2Cond);
		this.outerConditions.add(outer3Cond);
		this.outerConditions.add(outer4Cond);
		this.resultState = result;
	}
	
	public boolean matches(IBlockState core, IBlockState outer1, IBlockState outer2, IBlockState outer3, IBlockState outer4){
		List<IBlockState> temp = new ArrayList<IBlockState>();
		List<IBlockStateCondition> conditions = new ArrayList<IBlockStateCondition>();
		temp.add(coreState);
		for (IBlockState b : outerStates){
			if (b != null){
				temp.add(b);
			}
		}
		conditions.add(coreCondition);
		for (IBlockStateCondition b : outerConditions){
			if (b != null){
				conditions.add(b);
			}
		}
		List<IBlockState> inputs = new ArrayList<IBlockState>();
		inputs.add(core);
		inputs.add(outer1);
		inputs.add(outer2);
		inputs.add(outer3);
		inputs.add(outer4);
		if (inputs.size() == temp.size()){
			boolean[] matches1 = new boolean[temp.size()];
			boolean[] matches2 = new boolean[inputs.size()];
			for (int i = 0; i < temp.size(); i ++){
				for (int j = 0; j < inputs.size(); j ++){
					if (!matches1[i] && !matches2[j]){
						if (temp.get(i).getBlock() == inputs.get(j).getBlock()){
							if (conditions.get(i).match(temp.get(i), inputs.get(j))){
								matches1[i] = true;
								matches2[j] = true;
							}
						}
					}
				}
			}
			boolean doMatch = true;
			for (int i = 0; i < matches1.length; i ++){
				if (!matches1[i]){
					doMatch = false;
				}
			}
			for (int i = 0; i < matches2.length; i ++){
				if (!matches2[i]){
					doMatch = false;
				}
			}
			return doMatch;
		}
		return false;
	}
}
