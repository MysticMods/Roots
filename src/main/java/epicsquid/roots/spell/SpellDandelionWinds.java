package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.network.fx.MessageDandelionCastFX;
import epicsquid.roots.spell.modules.SpellModule;
import epicsquid.roots.util.types.Property;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreIngredient;

import java.util.List;

public class SpellDandelionWinds extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(20);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("cloud_berry", 0.125));
  public static Property<Float> PROP_DISTANCE = new Property<>("distance", 0.75f).setDescription("the vertical component of the vector that determines the direction of the entity");

  public static String spellName = "spell_dandelion_winds";
  public static SpellDandelionWinds instance = new SpellDandelionWinds(spellName);

  private float distance;

  public SpellDandelionWinds(String name) {
    super(name, TextFormatting.YELLOW, 255f / 255f, 255f / 255f, 32f / 255f, 255f / 255f, 176f / 255f, 32f / 255f);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_DISTANCE);
  }

  @Override
  public void init () {
    addIngredients(
        new ItemStack(Blocks.YELLOW_FLOWER),
        new OreIngredient("treeLeaves"),
        new ItemStack(ModItems.runic_dust),
        new ItemStack(ModItems.cloud_berry),
        new ItemStack(ModItems.terra_spores)
    );
  }

  @Override
  public boolean cast(EntityPlayer player, List<SpellModule> modules, int ticks) {
    PacketHandler.sendToAllTracking(new MessageDandelionCastFX(player.getUniqueID(), player.posX, player.posY + player.getEyeHeight(), player.posZ), player);
    List<EntityLivingBase> entities = player.world.getEntitiesWithinAABB(EntityLivingBase.class,
        new AxisAlignedBB(player.posX + player.getLookVec().x * 6.0 - 6.0, player.posY + player.getLookVec().y * 6.0 - 6.0,
            player.posZ + player.getLookVec().z * 6.0 - 4.0, player.posX + player.getLookVec().x * 6.0 + 6.0, player.posY + player.getLookVec().y * 6.0 + 6.0,
            player.posZ + player.getLookVec().z * 6.0 + 6.0));
    if (entities.size() > 0) {
      for (EntityLivingBase e : entities) {
        if (e.getUniqueID().compareTo(player.getUniqueID()) != 0) {
          e.motionX += (player.getLookVec().x);
          e.motionY += (distance);
          e.motionZ += (player.getLookVec().z);
          e.velocityChanged = true;
        }
      }
    }
    return true;
  }

  @Override
  public void doFinalise() {
    this.castType = properties.get(PROP_CAST_TYPE);
    this.cooldown = properties.get(PROP_COOLDOWN);
    this.distance = properties.get(PROP_DISTANCE);
  }
}
