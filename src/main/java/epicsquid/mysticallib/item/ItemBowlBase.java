package epicsquid.mysticallib.item;

import epicsquid.mysticallib.model.CustomModelItem;
import epicsquid.mysticallib.model.CustomModelLoader;
import epicsquid.mysticallib.util.ItemUtil;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

import javax.annotation.Nonnull;

public class ItemBowlBase extends ItemFoodBase {

  private boolean hasCustomModel;

  public ItemBowlBase(@Nonnull String name, int amount, float saturation, boolean isWolfFood) {
    super(name, amount, saturation, isWolfFood);
  }

  public ItemBowlBase(@Nonnull String name, int amount, boolean isWolfFood) {
    super(name, amount, isWolfFood);
  }

  public ItemBowlBase setModelCustom(boolean custom) {
    this.hasCustomModel = custom;
    return this;
  }

  @Override
  public void initModel() {
    ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "handlers"));
  }

  @Override
  public void initCustomModel() {
    if (this.hasCustomModel) {
      ResourceLocation texture = new ResourceLocation(getRegistryName().getNamespace() + ":items/" + getRegistryName().getPath());
      CustomModelItem item = new CustomModelItem(false, texture);
      item.addTexture("particle", texture);
      CustomModelLoader.itemmodels.put(getRegistryName(), item);
    }
  }

  @Override
  public ItemStack onItemUseFinish(ItemStack stack, World world, LivingEntity entity) {
    ItemStack result = super.onItemUseFinish(stack, world, entity);
    ItemStack returned = new ItemStack(Items.BOWL);
    if (result.isEmpty()) {
      return returned;
    } else if (entity instanceof PlayerEntity && !world.isRemote) {
      PlayerEntity player = (PlayerEntity) entity;
      if (!player.addItemStackToInventory(returned)) {
        ItemUtil.spawnItem(world, player.getPosition(), returned);
      }
    }
    return result;
  }
}
