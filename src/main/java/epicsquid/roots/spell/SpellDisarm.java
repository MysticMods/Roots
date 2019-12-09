package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.network.fx.MessageDisarmFX;
import epicsquid.roots.spell.modules.SpellModule;
import epicsquid.roots.util.types.Property;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SpellDisarm extends SpellBase{

  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(100);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("moonglow_leaf", 0.25));
  public static Property<Integer> PROP_RADIUS = new Property<>("radius", 20);

  public static String spellName = "spell_disarm";
  public static SpellDisarm instance = new SpellDisarm(spellName);

  private int radius;

  private SpellDisarm(String name) {
    super(name, TextFormatting.DARK_RED, 122F/255F, 0F, 0F, 58F/255F, 58F/255F, 58F/255F);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1);
  }

  @Override
  public void init() {
    addIngredients(
            new ItemStack(Items.IRON_SWORD),
            new ItemStack(ModItems.bark_dark_oak),
            new ItemStack(ModItems.bark_spruce),
            new ItemStack(ModItems.petals)
    );
  }

  @Override
  public boolean cast(EntityPlayer caster, List<SpellModule> modules) {
    BlockPos playerPos =  caster.getPosition();
    UUID playerId = caster.getUniqueID();

    if (!caster.world.isRemote) {
      List<EntityLivingBase> entities = caster.world.getEntitiesWithinAABB(EntityLivingBase.class,
              new AxisAlignedBB(playerPos.getX() - radius, playerPos.getY() - 3, playerPos.getZ() - radius, playerPos.getX() + radius, playerPos.getY() + 3, playerPos.getZ() + radius));
      entities.remove(caster.world.getPlayerEntityByUUID(playerId));

      if (!entities.isEmpty()) {
        for (EntityLivingBase entity : entities) {

          List<ItemStack> inventory = new ArrayList<>();
          if (!entity.getHeldItem(EnumHand.MAIN_HAND).isEmpty())
            inventory.add(entity.getHeldItemMainhand());
          if (!entity.getHeldItem(EnumHand.OFF_HAND).isEmpty())
            inventory.add(entity.getHeldItemOffhand());

          entity.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
          entity.setHeldItem(EnumHand.OFF_HAND, ItemStack.EMPTY);

          if (!inventory.isEmpty())
          {
            for (ItemStack stack : inventory)
              if (Math.random() * 100 < 15)
                caster.world.spawnEntity(new EntityItem(caster.world, entity.posX, entity.posY, entity.posZ, stack));

            PacketHandler.sendToAllTracking(new MessageDisarmFX(entity.getPosition().getX(), entity.getPosition().getY(), entity.getPosition().getZ()), caster);
          }
          //Removes Armor
          //for (int i = 0; i < 4; i++)
          //  entity.replaceItemInInventory(i + 5, ItemStack.EMPTY);

        }
        return true;
      } else
        return false;
    }
    return false;
  }

  @Override
  public void doFinalise() {
    this.castType = properties.getProperty(PROP_CAST_TYPE);
    this.cooldown = properties.getProperty(PROP_COOLDOWN);
    this.radius = properties.getProperty(PROP_RADIUS);
  }

}
