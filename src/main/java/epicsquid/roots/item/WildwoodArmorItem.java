package epicsquid.roots.item;

import epicsquid.roots.Roots;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.model.armor.WildwoodArmorModel;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class WildwoodArmorItem extends ArmorItem implements ILivingRepair {
  public WildwoodArmorItem(IArmorMaterial materialIn, EquipmentSlotType slot, Properties builder) {
    super(materialIn, slot, builder);
/*    setTranslationKey(name);
    setRegistryName(new ResourceLocation(Roots.MODID, name));
    setMaxDamage(750);
    setCreativeTab(Roots.tab);*/
  }

  @Nullable
  @Override
  public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
    return Roots.MODID + ":textures/models/armor/wildwood_armor.png";
  }

  @Nullable
  @Override
  @OnlyIn(Dist.CLIENT)
  public BipedModel getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, BipedModel _default) {
    return WildwoodArmorModel.getInstance(armorSlot);
  }

  @Override
  public void onArmorTick(ItemStack stack, World world, PlayerEntity player) {
    switch (piecesWorn(player)) {
      case 1:
        if (random.nextInt(12 * 20) == 0 && player.shouldHeal())
          player.heal(1);
        break;
      case 2:
        if (random.nextInt(10 * 20) == 0 && player.shouldHeal())
          player.heal(1);
        break;
      case 3:
        if (random.nextInt(8 * 20) == 0 && player.shouldHeal())
          player.heal(1);
        break;
      case 4:
        if (random.nextInt(6 * 20) == 0 && player.shouldHeal())
          player.heal(1);
        break;
      default:
    }
  }

  public static int piecesWorn(PlayerEntity player) {
    int count = 0;
    for (ItemStack stack : player.getArmorInventoryList()) {
      if (stack.getItem() instanceof WildwoodArmorItem)
        count++;
    }
    return count;
  }

  @Override
  public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
    return toRepair.getItem() == this && repair.getItem() == ModItems.bark_wildwood;
  }
}
