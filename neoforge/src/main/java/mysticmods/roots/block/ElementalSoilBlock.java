package mysticmods.roots.block;

import mysticmods.roots.api.RootsTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.TriState;


public class ElementalSoilBlock extends Block {
    public ElementalSoilBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public TriState canSustainPlant(BlockState state, BlockGetter level, BlockPos soilPosition, Direction facing, BlockState plant) {
        return super.canSustainPlant(state, level, soilPosition, facing, plant);
    }

/*    @Override
    public boolean canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction facing, IPlantable plantable) {
        PlantType type = plantable.getPlantType(world, pos);
        return type != PlantType.WATER || state.is(RootsTags.Blocks.WATER_SOIL);
    }*/
}
