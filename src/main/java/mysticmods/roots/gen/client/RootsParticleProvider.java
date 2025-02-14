package mysticmods.roots.gen.client;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.init.ModParticles;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.ParticleDescriptionProvider;

public class RootsParticleProvider extends ParticleDescriptionProvider {
  public RootsParticleProvider(PackOutput output, ExistingFileHelper fileHelper) {
    super(output, fileHelper);
  }

  @Override
  protected void addDescriptions() {
    this.spriteSet(ModParticles.PYRE.get(), RootsAPI.rl("simple"), 1, false);
    this.spriteSet(ModParticles.FEY_LIGHT.get(), RootsAPI.rl("simple"), 1, false);
    this.spriteSet(ModParticles.PYRE_LEAF.get(), RootsAPI.rl("leaf"), 5, false);
    this.spriteSet(ModParticles.METEOR.get(), RootsAPI.rl("simple"), 1, false);
    this.spriteSet(ModParticles.WILDFIRE.get(), RootsAPI.rl("wildfire"), 1, false);
  }

  @Override
  public String getName() {
    return "Roots Particle Provider";
  }
}