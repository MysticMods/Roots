package mysticmods.roots.gen.client;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.init.ModParticles;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.ParticleDescriptionProvider;

import java.util.List;

public class RootsParticleProvider extends ParticleDescriptionProvider {
  public RootsParticleProvider(PackOutput output, ExistingFileHelper fileHelper) {
    super(output, fileHelper);
  }

  @Override
  protected void addDescriptions() {
    this.sprite(ModParticles.PYRE.get(), RootsAPI.rl("simple"));
    this.sprite(ModParticles.SYLVAN_LIGHT.get(), RootsAPI.rl("simple"));
    this.sprite(ModParticles.METEOR.get(), RootsAPI.rl("simple"));
    this.sprite(ModParticles.WILDFIRE.get(), RootsAPI.rl("simple"));
    this.sprite(ModParticles.GROWTH.get(), RootsAPI.rl("simple_square"));
    this.sprite(ModParticles.CHANNEL_TARGET.get(), RootsAPI.rl("simple"));

    this.sprite(ModParticles.ANIMAL_HARVEST.get(), RootsAPI.rl("simple"));
    this.spriteSet(ModParticles.SPIRAL.get(), RootsAPI.rl("petal"), 4, false);

    this.spriteSet(ModParticles.PYRE_LEAF.get(), RootsAPI.rl("leaf"), 5, false);
    this.spriteSet(ModParticles.GROVE_STONE.get(), List.of(RootsAPI.rl("leaf_0"), RootsAPI.rl("leaf_1"), RootsAPI.rl("leaf_2"), RootsAPI.rl("leaf_3"), RootsAPI.rl("leaf_4"), RootsAPI.rl("petal_0"), RootsAPI.rl("petal_1"), RootsAPI.rl("petal_2"), RootsAPI.rl("petal_3")));

    this.sprite(ModParticles.SPROUT_PORTAL.get(), RootsAPI.rl("simple"));
  }

  @Override
  public String getName() {
    return "Roots Particle Provider";
  }
}