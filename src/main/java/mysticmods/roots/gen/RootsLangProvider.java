package mysticmods.roots.gen;

import mysticmods.roots.api.RootsAPI;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;




public class RootsLangProvider extends LanguageProvider {
  public RootsLangProvider(PackOutput output) {
    super(output, RootsAPI.MODID, "en_us");
  }

  @Override
  protected void addTranslations() {
    // Tag translations
  }
}
