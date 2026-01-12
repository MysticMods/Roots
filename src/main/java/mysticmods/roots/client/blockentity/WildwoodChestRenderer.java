package mysticmods.roots.client.blockentity;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.blockentity.WildwoodChestBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.ChestType;

public class WildwoodChestRenderer extends ChestRenderer<WildwoodChestBlockEntity> {
  private static final ResourceLocation CHEST_SHEET = ResourceLocation.withDefaultNamespace("textures/atlas/chest.png");
  private static final Material WILDWOOD_CHEST_MATERIAL = new Material(
      CHEST_SHEET,
      RootsAPI.rl("entity/chest/wildwood_chest")
  );

  public WildwoodChestRenderer(BlockEntityRendererProvider.Context context) {
    super(context);
  }

  @Override
  protected Material getMaterial(WildwoodChestBlockEntity blockEntity, ChestType chestType) {
    return WILDWOOD_CHEST_MATERIAL;
  }
}
