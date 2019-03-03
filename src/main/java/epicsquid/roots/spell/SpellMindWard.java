package epicsquid.roots.spell;

import java.util.List;

import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.util.Constants;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;

public class SpellMindWard extends SpellBase {

  public SpellMindWard(String name) {
    super(name, TextFormatting.DARK_RED, 128f / 255f, 32f / 255f, 32f / 255f, 32f / 255f, 32f / 255f, 32f / 255f);
    this.castType = SpellBase.EnumCastType.INSTANTANEOUS;
    this.cooldown = 80;

    addCost(HerbRegistry.getHerbByName("wildewheet"), 0.5f);
    addIngredients(
        new ItemStack(Items.ROTTEN_FLESH),
        new ItemStack(ModItems.wildewheet),
        new ItemStack(ModItems.wildewheet_seed),
        new ItemStack(ModItems.aubergine_seed),
        new ItemStack(Items.ENDER_PEARL)
    );
  }

  @Override
  public void cast(EntityPlayer player) {
    if (!player.world.isRemote) {
      boolean foundTarget = false;
      for (int i = 0; i < 4 && !foundTarget; i++) {
        double x = player.posX + player.getLookVec().x * 3.0 * (float) i;
        double y = player.posY + player.getEyeHeight() + player.getLookVec().y * 3.0 * (float) i;
        double z = player.posZ + player.getLookVec().z * 3.0 * (float) i;
        List<EntityLivingBase> entities = player.world
            .getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(x - 2.0, y - 2.0, z - 2.0, x + 2.0, y + 2.0, z + 2.0));
        for (EntityLivingBase e : entities) {
          if (e.getUniqueID().compareTo(player.getUniqueID()) != 0 && !foundTarget) {
            foundTarget = true;
            e.getEntityData().setInteger(Constants.MIND_WARD_TAG, 400);
          }
        }
      }
    }
  }

}
