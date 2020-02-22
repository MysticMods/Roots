package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.network.fx.MessageDisarmFX;
import epicsquid.roots.spell.modules.SpellModule;
import epicsquid.roots.util.types.Property;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreIngredient;

import java.util.List;

public class SpellDisarm extends SpellBase {

  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(350);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("moonglow_leaf", 0.50));
  public static Property.PropertyCost PROP_COST_2 = new Property.PropertyCost(1, new SpellCost("dewgonia", 0.25));
  public static Property<Integer> PROP_RADIUS_X = new Property<>("radius_x", 2);
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 2);
  public static Property<Integer> PROP_RADIUS_Z = new Property<>("radius_z", 2);

  public static String spellName = "spell_disarm";
  public static SpellDisarm instance = new SpellDisarm(spellName);

  private int radius_x, radius_y, radius_z;

  private SpellDisarm(String name) {
    super(name, TextFormatting.DARK_RED, 122F / 255F, 0F, 0F, 58F / 255F, 58F / 255F, 58F / 255F);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_COST_2, PROP_RADIUS_X, PROP_RADIUS_Y, PROP_RADIUS_Z);
  }

  @Override
  public void init() {
    addIngredients(
        new ItemStack(Items.SHIELD),
        new OreIngredient("gemDiamond"),
        new OreIngredient("bone"),
        new ItemStack(Item.getItemFromBlock(Blocks.TRIPWIRE_HOOK)),
        new ItemStack(ModItems.moonglow_leaf)
    );
  }

  @Override
  public boolean cast(EntityPlayer caster, List<SpellModule> modules) {
    BlockPos playerPos = caster.getPosition();
    World world = caster.world;

    List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(playerPos.getX() - radius_x, playerPos.getY() - radius_y, playerPos.getZ() - radius_z, playerPos.getX() + radius_x, playerPos.getY() + radius_y, playerPos.getZ() + radius_z));
    entities.remove(caster);

    if (entities.isEmpty()) {
      return false;
    }

    for (EntityLivingBase entity : entities) {
      int pieces = 0;
      for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
        ItemStack stack = entity.getItemStackFromSlot(slot);
        if (stack.isEmpty()) {
          continue;
        }
        pieces++;
        if (!world.isRemote) {
          entity.setItemStackToSlot(slot, ItemStack.EMPTY);
          ItemUtil.spawnItem(world, entity.getPosition(), stack);
        }
      }

      if (pieces != 0) {
        if (!world.isRemote) {
          PacketHandler.sendToAllTracking(new MessageDisarmFX(entity.getPosition().getX(), entity.getPosition().getY(), entity.getPosition().getZ()), caster);
        }
        return true;
      }
    }
    return false;
  }

  @Override
  public void doFinalise() {
    this.castType = properties.getProperty(PROP_CAST_TYPE);
    this.cooldown = properties.getProperty(PROP_COOLDOWN);
    this.radius_x = properties.getProperty(PROP_RADIUS_X);
    this.radius_y = properties.getProperty(PROP_RADIUS_Y);
    this.radius_z = properties.getProperty(PROP_RADIUS_Z);
  }
}
