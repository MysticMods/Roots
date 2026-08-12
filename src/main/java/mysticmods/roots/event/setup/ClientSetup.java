package mysticmods.roots.event.setup;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.init.ModItems;
import mysticmods.roots.item.CastingItem;
import mysticmods.roots.item.util.DyeableWithDefault;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@EventBusSubscriber(modid = RootsAPI.MODID, value = Dist.CLIENT)
public class ClientSetup {
  public static final ResourceLocation SPELL_PREDICATE = RootsAPI.rl("spell_predicate");
  public static final ResourceLocation STRING_COLOR = RootsAPI.rl("string_color");
  public static final ResourceLocation UNDYED = RootsAPI.rl("undyed");

  @SuppressWarnings("deprecation")
  @SubscribeEvent
  public static void init(FMLCommonSetupEvent event) {
    event.enqueueWork(() -> {
      ItemPropertyFunction func = (stack, level, entity, seed) -> {
        DyeableWithDefault dyeable = stack.get(ModAttachments.DYEABLE);
        if (dyeable == DyeableWithDefault.DEFAULT) {
          return 0;
        }
        return 1;
      };
      ItemProperties.register(ModItems.APOTHECARY_POUCH.get(), UNDYED, func);
      ItemProperties.register(ModItems.COMPONENT_POUCH.get(), UNDYED, func);
      ItemProperties.register(ModItems.HERB_POUCH.get(), UNDYED, func);

      ItemPropertyFunction func2 = (stack, level, entity, seed) -> {
        DyeableWithDefault dyeable = stack.get(ModAttachments.DYEABLE);
        if (dyeable == DyeableWithDefault.DEFAULT || dyeable == null || dyeable.color() == null) {
          return -1;
        }
        return dyeable.color().getId() / 16f;
      };

      ItemProperties.register(ModItems.SYLVAN_POUCH.get(), STRING_COLOR, func2);

      ItemPropertyFunction func3 = (stack, level, entity, seed) -> {
        var spell = CastingItem.getCurrentSpell(level, entity, stack);
        if (spell != null) {
          return spell.getIconPredicate();
        }

        return -1;
      };

      ItemProperties.register(ModItems.SPELL_GROWTH_INFUSION.get(), SPELL_PREDICATE, func3);
    });
  }
}
