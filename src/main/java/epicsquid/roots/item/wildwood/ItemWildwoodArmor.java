package epicsquid.roots.item.wildwood;

import epicsquid.mysticallib.model.IModeledObject;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.item.ILivingRepair;
import epicsquid.roots.model.armor.ModelWildwoodArmor;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class ItemWildwoodArmor extends ItemArmor implements IModeledObject, ILivingRepair {
  public ItemWildwoodArmor(ArmorMaterial material, EntityEquipmentSlot slot, String name) {
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
  public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
    return Roots.MODID + ":textures/models/armor/wildwood_armor.png";
  }

  @Nullable
  @Override
  @SideOnly(Side.CLIENT)
  public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
    return ModelWildwoodArmor.getInstance(armorSlot);
  }

  @Override
  public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
    // TODO: Make configurable?
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

  public static int piecesWorn(EntityPlayer player) {
    int count = 0;
    for (ItemStack stack : player.getArmorInventoryList()) {
      if (stack.getItem() instanceof ItemWildwoodArmor)
        count++;
    }
    return count;
  }

  @Override
  public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
    return toRepair.getItem() == this && repair.getItem() == ModItems.bark_wildwood;
  }
}
