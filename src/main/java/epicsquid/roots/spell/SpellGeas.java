package epicsquid.roots.spell;

import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModPotions;
import epicsquid.roots.spell.modules.SpellModule;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreIngredient;

import java.util.List;

public class SpellGeas extends SpellBase {
  public static String spellName = "spell_geas";
  public static SpellGeas instance = new SpellGeas(spellName);

  public SpellGeas(String name) {
    super(name, TextFormatting.DARK_RED, 128f / 255f, 32f / 255f, 32f / 255f, 32f / 255f, 32f / 255f, 32f / 255f);
    this.castType = SpellBase.EnumCastType.INSTANTANEOUS;
    this.cooldown = 80;

    addCost(HerbRegistry.getHerbByName("wildewheet"), 0.5f);
    addIngredients(
        new ItemStack(Items.ROTTEN_FLESH),
        new ItemStack(ModItems.wildewheet),
        new ItemStack(ModItems.wildewheet_seed),
        new ItemStack(epicsquid.mysticalworld.init.ModItems.aubergine_seed),
        new OreIngredient("enderpearl")
    );
  }

  @Override
  public boolean cast(EntityPlayer player, List<SpellModule> modules) {
    boolean foundTarget = false;
    for (int i = 0; i < 4 && !foundTarget; i++) {
      double x = player.posX + player.getLookVec().x * 3.0 * (float) i;
      double y = player.posY + player.getEyeHeight() + player.getLookVec().y * 3.0 * (float) i;
      double z = player.posZ + player.getLookVec().z * 3.0 * (float) i;
      List<EntityLivingBase> entities = player.world
          .getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(x - 4.0, y - 4.0, z - 4.0, x + 4.0, y + 4.0, z + 4.0));
      for (EntityLivingBase e : entities) {
        if (e != player && e.getActivePotionEffect(ModPotions.geas) == null) {
          foundTarget = true;
          if (!player.world.isRemote) {
            e.addPotionEffect(new PotionEffect(ModPotions.geas, 400));
            if (e instanceof EntityLiving) {
              ((EntityLiving) e).setAttackTarget(null);
            }
            break;
          }
        }
      }
    }
    return foundTarget;
  }
}
