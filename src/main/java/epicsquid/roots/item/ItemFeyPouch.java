package epicsquid.roots.item;

import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModItems;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

import javax.annotation.Nonnull;

public class ItemFeyPouch extends ItemPouch {
  public ItemFeyPouch(@Nonnull String name) {
    super(name, PouchType.FEY);
  }

  private final Int2ObjectOpenHashMap<ModelResourceLocation> colorMap = new Int2ObjectOpenHashMap<>();

  @Override
  public void initModel() {
    for (EnumDyeColor dye : EnumDyeColor.values()) {
      colorMap.put(dye.getMetadata(), new ModelResourceLocation(new ResourceLocation(Roots.MODID, "fey_pouch/" + dye.getDyeColorName()), "inventory"));
    }
    ModelBakery.registerItemVariants(ModItems.fey_pouch, colorMap.values().toArray(new ModelResourceLocation[0]));

    final ModelResourceLocation res = new ModelResourceLocation(new ResourceLocation(Roots.MODID, "fey_pouch/blue"), "inventory");

    ModelLoader.setCustomMeshDefinition(ModItems.fey_pouch, (stack) -> {
          NBTTagCompound tag = ItemUtil.getOrCreateTag(stack);
          if (tag.hasKey("color")) {
            return colorMap.get(tag.getInteger("color"));
          }
          return res;
        }
    );
  }
}
