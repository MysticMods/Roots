package epicsquid.roots.item;

import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModItems;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.item.DyeColor;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

import javax.annotation.Nonnull;

public class ItemFeyPouch extends ItemPouch {
  public ItemFeyPouch(@Nonnull String name) {
    super(name, PouchType.FEY);
  }

  private final Int2ObjectOpenHashMap<net.minecraft.client.renderer.model.ModelResourceLocation> colorMap = new Int2ObjectOpenHashMap<>();

  @Override
  public void initModel() {
    for (DyeColor dye : DyeColor.values()) {
      colorMap.put(dye.getMetadata(), new net.minecraft.client.renderer.model.ModelResourceLocation(new ResourceLocation(Roots.MODID, "fey_pouch/" + dye.getDyeColorName()), "inventory"));
    }
    ModelBakery.registerItemVariants(ModItems.fey_pouch, colorMap.values().toArray(new ModelResourceLocation[0]));

    final net.minecraft.client.renderer.model.ModelResourceLocation res = new net.minecraft.client.renderer.model.ModelResourceLocation(new ResourceLocation(Roots.MODID, "fey_pouch/blue"), "inventory");

    ModelLoader.setCustomMeshDefinition(ModItems.fey_pouch, (stack) -> {
          CompoundNBT tag = ItemUtil.getOrCreateTag(stack);
          if (tag.contains("color")) {
            return colorMap.get(tag.getInt("color"));
          }
          return res;
        }
    );
  }
}
