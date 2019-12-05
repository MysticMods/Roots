package epicsquid.roots.spell;

import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModPotions;
import epicsquid.roots.spell.modules.SpellModule;
import epicsquid.roots.util.types.Property;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

public class SpellGeas extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(80);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("baffle_cap", 0.5));
  public static Property<Integer> PROP_DURATION = new Property<>("geas_duration", 400);

  public static String spellName = "spell_geas";
  public static SpellGeas instance = new SpellGeas(spellName);

  private int duration;

  public SpellGeas(String name) {
    super(name, TextFormatting.DARK_RED, 128f / 255f, 32f / 255f, 32f / 255f, 32f / 255f, 32f / 255f, 32f / 255f);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_DURATION);
  }

  @Override
  public void init() {
    addIngredients(
        new ItemStack(Item.getItemFromBlock(Blocks.WEB)),
        new ItemStack(Items.LEAD),
        new ItemStack(Items.CARROT_ON_A_STICK),
        new ItemStack(ModItems.terra_spores),
        new ItemStack(Item.getItemFromBlock(ModBlocks.baffle_cap_mushroom))
    );
  }

  @Override
  public boolean cast(PlayerEntity player, List<SpellModule> modules) {
    boolean foundTarget = false;
    for (int i = 0; i < 4 && !foundTarget; i++) {
      double x = player.posX + player.getLookVec().x * 3.0 * (float) i;
      double y = player.posY + player.getEyeHeight() + player.getLookVec().y * 3.0 * (float) i;
      double z = player.posZ + player.getLookVec().z * 3.0 * (float) i;
      List<LivingEntity> entities = player.world
          .getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB(x - 4.0, y - 4.0, z - 4.0, x + 4.0, y + 4.0, z + 4.0));
      for (LivingEntity e : entities) {
        if (e != player && e.getActivePotionEffect(ModPotions.geas) == null) {
          foundTarget = true;
          if (!player.world.isRemote) {
            e.addPotionEffect(new EffectInstance(ModPotions.geas, duration, 0, false, false));
            if (e instanceof MobEntity) {
              ((MobEntity) e).setAttackTarget(null);
            }
            break;
          }
        }
      }
    }
    return foundTarget;
  }

  @Override
  public void doFinalise() {
    this.castType = properties.getProperty(PROP_CAST_TYPE);
    this.cooldown = properties.getProperty(PROP_COOLDOWN);
    this.duration = properties.getProperty(PROP_DURATION);
  }
}
