package mysticmods.roots.block;

import mysticmods.roots.api.RootsAPI;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;

public class ElementalSoilBlock extends Block {
    public ElementalSoilBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction facing, IPlantable plantable) {
        PlantType type = plantable.getPlantType(world, pos);
        return type != PlantType.WATER || state.is(RootsAPI.Tags.Blocks.WATER_SOIL);
    }
}
