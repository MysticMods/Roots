package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.config.SpellConfig;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.network.fx.MessageSanctuaryBurstFX;
import epicsquid.roots.network.fx.MessageSanctuaryRingFX;
import epicsquid.roots.spell.modules.SpellModule;
import epicsquid.roots.util.types.Property;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreIngredient;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SpellSanctuary extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(0);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.CONTINUOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("pereskia", 0.125));
  public static Property.PropertyCost PROP_COST_2 = new Property.PropertyCost(1, new SpellCost("wildroot", 0.125));
  public static Property<Float> PROP_VELOCITY = new Property<>("push_velocity", 0.125f);
  public static Property<Integer> PROP_RADIUS_X = new Property<>("radius_x", 4);
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 5);
  public static Property<Integer> PROP_RADIUS_Z = new Property<>("radius_z", 4);

  public static String spellName = "spell_sanctuary";
  public static SpellSanctuary instance = new SpellSanctuary(spellName);

  private float velocity;
  private int radius_x, radius_y, radius_z;

  private static Set<String> entitiesBlackList = new HashSet<>();

  public SpellSanctuary(String name) {
    super(name, TextFormatting.DARK_PURPLE, 208f / 255f, 16f / 255f, 80f / 255f, 224f / 255f, 32f / 255f, 144f / 255f);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_COST_2, PROP_VELOCITY, PROP_RADIUS_X, PROP_RADIUS_Y, PROP_RADIUS_Z);

    addIngredients(
        new ItemStack(Items.DYE, 1, 1),
        new ItemStack(ModItems.pereskia),
        new OreIngredient("vine"),
        new ItemStack(ModItems.pereskia),
        new ItemStack(ModItems.wildroot)
    );
  }

  static {
    entitiesBlackList.addAll(Arrays.asList(SpellConfig.spellFeaturesCategory.sanctuaryEntitiesBlacklist));
  }

  @Override
  public boolean cast(EntityPlayer player, List<SpellModule> modules) {

    List<Entity> entities = Util.getEntitiesWithinRadius(player.world, Entity.class, player.getPosition(), radius_x, radius_y, radius_z);

    if (entities.size() > 0) {
      for (Entity e : entities) {
        if (e.getUniqueID() != player.getUniqueID()) {
          if ((e instanceof IProjectile || e instanceof IMob || e.isCreatureType(EnumCreatureType.MONSTER, false)) && (!entitiesBlackList.contains(EntityList.getKey(e).toString()))) {
            if (Math.pow((e.posX - player.posX), 2) + Math.pow((e.posY - player.posY), 2) + Math.pow((e.posZ - player.posZ), 2) < 9.0f) {
              e.motionX = velocity * (e.posX - player.posX);
              e.motionY = velocity * (e.posY - player.posY);
              e.motionZ = velocity * (e.posZ - player.posZ);
              e.velocityChanged = true;
              if (!e.isInvisible()) {
                PacketHandler.sendToAllTracking(new MessageSanctuaryBurstFX(e.posX, e.posY + 0.6f * e.getEyeHeight(), e.posZ), e);
              }
            }
          }
        }
      }
    }
    if (player.ticksExisted % 2 == 0) {
      PacketHandler.sendToAllTracking(new MessageSanctuaryRingFX(player.posX, player.posY + 0.875f, player.posZ), player);
    }
    return true;
  }

  @Override
  public void finalise() {
    this.castType = properties.getProperty(PROP_CAST_TYPE);
    this.cooldown = properties.getProperty(PROP_COOLDOWN);

    SpellCost cost = properties.getProperty(PROP_COST_1);
    addCost(cost.getHerb(), cost.getCost());
    cost = properties.getProperty(PROP_COST_2);
    addCost(cost.getHerb(), cost.getCost());

    this.radius_x = properties.getProperty(PROP_RADIUS_X);
    this.radius_y = properties.getProperty(PROP_RADIUS_Y);
    this.radius_z = properties.getProperty(PROP_RADIUS_Z);
    this.velocity = properties.getProperty(PROP_VELOCITY);
  }


// THIS IS THE RESULT OF MY WORK, I'M GONNA KEEP IT AS A TROPHY - Davoleo
//  private boolean checkInterfaces(Entity e, Set<Class<?>> whiteList) {
//    Class<?> class1 = e.getClass();
//    for (Class<?> class2 : whiteList)
//    {
//      if (class1.isAssignableFrom(class2))
//        return true;
//    }
//    return false;
//  }
//
//  private static Set<Class<?>> readConfigWhitelist() {
//
//    for (String className : SpellConfig.spellFeaturesCategory.sanctuaryEntitiesBlacklist)
//    {
//      try {
//        entitiesBlackList.add(Class.forName(className));
//      }catch (ClassNotFoundException exception) {
//        Roots.logger.error("ERROR: One of the Sanctuary whitelist classes does not exist!");
//      }
//    }
//    return entitiesBlackList;
//  }

}

