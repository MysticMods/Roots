package mysticmods.roots.entity.other;

import mysticmods.roots.init.ModEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class WildwoodCart extends AbstractMinecart {
  public WildwoodCart(Level level) {
    super(ModEntities.WILDWOOD_CART.get(), level);
  }

  public WildwoodCart(Level level, double x, double y, double z) {
    super(ModEntities.WILDWOOD_CART.get(), level, x, y, z);
  }

  public WildwoodCart(EntityType<WildwoodCart> wildwoodCartEntityType, Level level) {
    super(wildwoodCartEntityType, level);
  }

  @Override
  protected Item getDropItem() {
    return Items.AIR;
    //return ModItems.WILDWOOD_CART.get();
  }

  @Override
  public Type getMinecartType() {
    return Type.RIDEABLE;
  }
}
