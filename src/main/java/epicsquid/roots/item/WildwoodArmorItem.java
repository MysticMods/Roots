package epicsquid.roots.item;

import epicsquid.mysticallib.model.IModeledObject;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.model.armor.WildwoodArmorModel;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class WildwoodArmorItem extends ArmorItem implements IModeledObject, ILivingRepair {
  public WildwoodArmorItem(ArmorMaterial material, EquipmentSlotType slot, String name) {
    super(material, 0, slot);
    setTranslationKey(name);
    setRegistryName(new ResourceLocation(Roots.MODID, name));
    setMaxDamage(750);
    setCreativeTab(Roots.tab);
  }

  @Override
  public void initModel() {
    ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "handler"));
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
  public void onArmorTick(World world, PlayerEntity player, ItemStack itemStack) {
    switch (piecesWorn(player)) {
      case 1:
        if (itemRand.nextInt(12 * 20) == 0 && player.shouldHeal())
          player.heal(1);
        break;
      case 2:
        if (itemRand.nextInt(10 * 20) == 0 && player.shouldHeal())
          player.heal(1);
        break;
      case 3:
        if (itemRand.nextInt(8 * 20) == 0 && player.shouldHeal())
          player.heal(1);
        break;
      case 4:
        if (itemRand.nextInt(6 * 20) == 0 && player.shouldHeal())
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
