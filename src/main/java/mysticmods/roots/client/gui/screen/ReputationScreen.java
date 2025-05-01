package mysticmods.roots.client.gui.screen;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.attachment.GrantStorage;
import mysticmods.roots.api.attachment.ReputationStorage;
import mysticmods.roots.api.datacomponent.SpellStorage;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.LibrarySpell;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.client.gui.buttons.LibrarySpellButton;
import mysticmods.roots.client.gui.buttons.StaffSpellButton;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.network.server.ServerboundSetSpellPacket;
import mysticmods.roots.network.server.ServerboundSwapSpellsPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ReputationScreen extends RootsScreen {
  protected ReputationScreen() {
    super(Component.translatable("roots.gui.reputation"));
    this.width = 256;
    this.height = 152;
  }

  @Override
  protected void init() {
    super.init();
  }

  @Nullable
  private ReputationStorage getStorage () {
    return this.minecraft.player.getData(ModAttachments.REPUTATION_STORAGE.get());
  }

  public static void open() {
    ReputationScreen newScreen = new ReputationScreen();
    Minecraft.getInstance().setScreen(newScreen);
  }

  @Override
  public void drawBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
    drawBackground(graphics, mouseX, mouseY, partialTicks, 256, 152, 256, 256);
  }

  private static final ResourceLocation background = RootsAPI.rl("textures/gui/reputation.png");

  @Override
  public ResourceLocation getBackground() {
    return background;
  }

  @Override
  public int getBackgroundWidth() {
    return 256;
  }

  @Override
  public int getBackgroundHeight() {
    return 152;
  }
}
