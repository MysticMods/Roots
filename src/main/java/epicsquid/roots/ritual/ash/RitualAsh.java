package epicsquid.roots.ritual.ash;

import epicsquid.roots.entity.ritual.EntityRitualBase;
import epicsquid.roots.entity.ritual.EntitySpawnItem;
import epicsquid.roots.ritual.RitualBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RitualAsh extends RitualBase {

    private final ItemStack dropStack;

    public RitualAsh(String name, int duration, ItemStack dropStack) {
        super(name, duration);
        this.dropStack = dropStack;
    }

    @Override
    public void doEffect(World world, BlockPos pos) {
        EntityRitualBase entity = new EntitySpawnItem(world, dropStack, 100);
        entity.setPosition(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
        world.spawnEntity(entity);
    }

}
