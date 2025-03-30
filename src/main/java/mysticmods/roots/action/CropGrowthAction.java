package mysticmods.roots.action;

import mysticmods.roots.api.action.GroveAction;
import mysticmods.roots.api.action.GroveContext;
import mysticmods.roots.growth.GrowthRecord;
import mysticmods.roots.util.GrowthUtil;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import java.util.Set;

public class CropGrowthAction implements GroveAction<CropGrowthContext> {
  @Override
  public boolean test(CropGrowthContext context) {
    GrowthRecord record = GrowthUtil.getGrowthRecord(context.blockState());
    if (record == null) {
      return false;
    }

    if (context.oldBlockState().isAir() && !context.blockState().isAir()) {
      return true;
    }

    IntegerProperty age = record.ageProperty().orElse(null);
    if (age == null) {
      return false;
    }

    if (!context.oldBlockState().hasProperty(age) || !context.blockState().hasProperty(age)) {
      return false;
    }

    int oldAge = context.oldBlockState().getValue(age);
    int newAge = context.blockState().getValue(age);

    return newAge > oldAge;
  }

  @Override
  public void reward(CropGrowthContext context) {
    // TODO: Actually do a reward

  }

  @Override
  public Set<GroveContext.Parameter> getUsedParameters() {
    return CropGrowthContext.PARAMTERS;
  }
}
