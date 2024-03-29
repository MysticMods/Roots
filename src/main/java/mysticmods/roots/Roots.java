package mysticmods.roots;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.access.IPlayerAccessor;
import mysticmods.roots.api.access.IRecipeManagerAccessor;
import mysticmods.roots.api.capability.Capabilities;
import mysticmods.roots.api.capability.Grant;
import mysticmods.roots.api.modifier.Modifier;
import mysticmods.roots.api.registry.Registries;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.client.impl.ClientPlayerAccessor;
import mysticmods.roots.client.impl.ClientRecipeAccessor;
import mysticmods.roots.impl.ServerPlayerAccessor;
import mysticmods.roots.impl.ServerRecipeAccessor;
import mysticmods.roots.init.*;
import mysticmods.roots.network.Networking;
import mysticmods.roots.network.client.ClientBoundCapabilitySynchronization;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import noobanidus.libs.noobutil.data.generator.RecipeGenerator;
import noobanidus.libs.noobutil.reference.ModData;
import noobanidus.libs.noobutil.registrate.CustomRegistrate;
import noobanidus.libs.particleslib.config.ConfigManager;

import javax.annotation.Nullable;

@Mod(RootsAPI.MODID)
public class Roots {
  public static CustomRegistrate REGISTRATE;
  public static final RecipeGenerator RECIPES = new RecipeGenerator(RootsAPI.MODID);

  public static final CreativeModeTab ITEM_GROUP = new CreativeModeTab(RootsAPI.MODID) {
    @Override
    public ItemStack makeIcon() {
      return new ItemStack(ModItems.Herbs.WILDROOT.get());
    }
  };

  public Roots() {
    REGISTRATE = CustomRegistrate.create(RootsAPI.MODID);
    REGISTRATE.creativeModeTab(() -> ITEM_GROUP);
    ModData.setIdAndIdentifier(RootsAPI.MODID, RootsAPI.MOD_IDENTIFIERS);

    ConfigManager.loadConfig(ConfigManager.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve(RootsAPI.MODID + "-common.toml"));
    ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigManager.COMMON_CONFIG);
    IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

    RootsAPI.INSTANCE = new RootsAPI() {
      private final IRecipeManagerAccessor accessor = DistExecutor.safeRunForDist(() -> ClientRecipeAccessor::new, () -> ServerRecipeAccessor::new);
      private final IPlayerAccessor playerAccessor = DistExecutor.safeRunForDist(() -> ClientPlayerAccessor::new, () -> ServerPlayerAccessor::new);

      @Override
      public IRecipeManagerAccessor getRecipeAccessor() {
        return accessor;
      }

      @Nullable
      @Override
      public Player getPlayer() {
        return playerAccessor.getPlayer();
      }

      @Override
      public void grant(ServerPlayer player, Grant grant) {
        player.getCapability(Capabilities.GRANT_CAPABILITY).ifPresent(cap -> {
          if (grant.getType() == Grant.Type.SPELL) {
            Spell spell = Registries.SPELL_REGISTRY.get().getValue(grant.getId());
            if (spell != null) {
              cap.grantSpell(spell);
            }
          } else if (grant.getType() == Grant.Type.MODIFIER) {
            Modifier modifier = Registries.MODIFIER_REGISTRY.get().getValue(grant.getId());
            if (modifier != null) {
              cap.grantModifier(modifier);
            }
          }
          if (cap.isDirty()) {
            synchronizeCapability(player, RootsAPI.GRANT_CAPABILITY_ID);
          }
        });
      }

      @Override
      public void synchronizeCapability(ServerPlayer player, ResourceLocation capability) {
        Networking.sendTo(new ClientBoundCapabilitySynchronization(player, capability), player);
      }
    };

    ModBlocks.load();
    ModBlockEntities.load();
    ModItems.load();
    ModHerbs.load();
    ModRegistries.load();
    ModTags.load();
    ModRecipes.load();
    ModConditions.load();
    ModRituals.load();
    ModSpells.load();
    ModLang.load();
    ModRegistries.register(bus);
    ModRecipes.Types.register(bus);
  }
}
