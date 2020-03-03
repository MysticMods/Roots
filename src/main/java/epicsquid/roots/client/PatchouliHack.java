package epicsquid.roots.client;

import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.roots.integration.IntegrationUtil;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.patchouli.client.book.BookCategory;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.client.book.gui.GuiBookCategory;
import vazkii.patchouli.client.book.text.BookTextParser;
import vazkii.patchouli.common.book.Book;

import java.util.Map;

@SideOnly(Side.CLIENT)
public class PatchouliHack {
  public static void init() {
    Map<String, BookTextParser.CommandProcessor> COMMANDS = ObfuscationReflectionHelper.getPrivateValue(BookTextParser.class, null, "COMMANDS");
    Map<String, BookTextParser.FunctionProcessor> FUNCTIONS = ObfuscationReflectionHelper.getPrivateValue(BookTextParser.class, null, "FUNCTIONS");

    BookTextParser.FunctionProcessor categoryFunction = (parameter, state) -> {
      state.prevColor = state.color;
      state.color = state.book.linkColor;
      ResourceLocation cat = new ResourceLocation(state.book.getModNamespace(), parameter);
      BookCategory entry = state.book.contents.categories.get(cat);
      if (entry != null) {
        state.tooltip = entry.isLocked() ? (TextFormatting.GRAY + I18n.format("patchouli.gui.lexicon.locked")) : entry.getName();
        GuiBook gui = state.gui;
        Book book = state.book;
        state.onClick = () -> {
          GuiBookCategory entryGui = new GuiBookCategory(book, entry);
          gui.displayLexiconGui(entryGui, true);
          GuiBook.playBookFlipSound(book);
        };
      } else {
        state.tooltip = "BAD LINK: INVALID CATEGORY " + parameter;
      }

      return "";
    };
    BookTextParser.CommandProcessor categoryCommand = (state) -> {
      state.color = state.prevColor;
      state.cluster = null;
      state.tooltip = "";
      state.onClick = null;
      state.isExternalLink = false;
      return "";
    };
    FUNCTIONS.put("cat", categoryFunction);
    COMMANDS.put("/cat", categoryCommand);

    BookTextParser.FunctionProcessor jeiUses = (parameter, state) -> {
      state.prevColor = state.color;
      if (Loader.isModLoaded("jei")) {
        String[] parts = parameter.split(":");
        ItemStack stack = ItemUtil.stackFromString(parts);
        if (!stack.isEmpty()) {
          state.color = state.book.linkColor;
          state.tooltip = stack.getDisplayName();
          state.onClick = () -> {
            IntegrationUtil.showUses(stack);
          };
        }
      }

      return "";
    };

    FUNCTIONS.put("uses", jeiUses);
    COMMANDS.put("/uses", categoryCommand);
  }
}
