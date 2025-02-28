package mysticmods.roots.gen.client;

import mysticmods.roots.Roots;
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
    this.sprite(ModParticles.PYRE.get(), RootsAPI.rl("simple"));
    this.sprite(ModParticles.FEY_LIGHT.get(), RootsAPI.rl("simple"));
    this.sprite(ModParticles.METEOR.get(), RootsAPI.rl("simple"));
    this.sprite(ModParticles.WILDFIRE.get(), RootsAPI.rl("simple"));
    this.sprite(ModParticles.GROWTH.get(), RootsAPI.rl("simple"));
    this.sprite(ModParticles.CHANNEL.get(), RootsAPI.rl("simple"));

    this.spriteSet(ModParticles.GEAS.get(), RootsAPI.rl("line"), 3, false);
    this.spriteSet(ModParticles.PYRE_LEAF.get(), RootsAPI.rl("leaf"), 5, false);
  }

  @Override
  public String getName() {
    return "Roots Particle Provider";
  }
}