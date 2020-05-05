package epicsquid.roots.mechanics;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.config.SpellConfig;
import epicsquid.roots.integration.botania.SolegnoliaHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Magnetize {
  public static <T extends Entity> List<T> collect(Class<? extends T> clazz, World world, BlockPos startPosition, int radiusX, int radiusY, int radiusZ) {
    List<T> entities = Util.getEntitiesWithinRadius(world, clazz, startPosition, radiusX, radiusY, radiusZ);
    entities.removeIf(Magnetize::skipPull);
    return entities;
  }

  public static <T extends Entity> int pull(Class<? extends T> clazz, World world, BlockPos startPosition, int radiusX, int radiusY, int radiusZ) {
    List<T> entities = collect(clazz, world, startPosition, radiusX, radiusY, radiusZ);
    if (entities.isEmpty()) return 0;
    for (Entity entity : entities) {
      if (!world.isRemote) {
        if (entity instanceof EntityItem && !entity.isDead) {
          ((EntityItem) entity).setPickupDelay(0);
        }
        entity.moveToBlockPosAndAngles(startPosition, 0f, 0f);
      }
    }
    return entities.size();
  }

  // Plz only call this on the remote
  public static List<BlockPos> store(World world, BlockPos startPosition, IItemHandler handler, int radiusX, int radiusY, int radiusZ) {
    List<EntityItem> items = collect(EntityItem.class, world, startPosition, radiusX, radiusY, radiusZ);
    Iterator<EntityItem> iterator = items.iterator();
    List<BlockPos> positions = new ArrayList<>();
    while (iterator.hasNext()) {
      EntityItem entity = iterator.next();
      ItemStack item = entity.getItem();
      ItemStack result = ItemHandlerHelper.insertItemStacked(handler, item, false);
      if (result.isEmpty()) {
        positions.add(entity.getPosition());
        entity.setDead();
      }
    }
    return positions;
  }

  public static boolean skipPull(Entity entity) {
    if (entity.isDead) {
      return true;
    }

    // Supporting Demagnetize
    // https://www.curseforge.com/minecraft/mc-mods/demagnetize
    if (entity instanceof EntityItem && entity.getEntityData().hasKey("PreventRemoteMovement")) {
      return true;
    }

    if (!SpellConfig.spellFeaturesCategory.shouldMagnetismAttractXP && entity instanceof EntityXPOrb) {
      return true;
    }

    return SolegnoliaHelper.hasBotania() && SolegnoliaHelper.hasSolegnoliaAround(entity);
  }
}
