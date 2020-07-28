package epicsquid.roots.client;

import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.roots.integration.IntegrationUtil;
import epicsquid.roots.properties.PropertyTable;
import epicsquid.roots.ritual.RitualBase;
import epicsquid.roots.ritual.RitualRegistry;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.SpellRegistry;
import net.minecraft.client.Minecraft;
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
import java.util.function.Function;

@SideOnly(Side.CLIENT)
public class PatchouliHack {
  public static void init() {
    Map<String, BookTextParser.CommandProcessor> COMMANDS = ObfuscationReflectionHelper.getPrivateValue(BookTextParser.class, null, "COMMANDS");
    Map<String, BookTextParser.FunctionProcessor> FUNCTIONS = ObfuscationReflectionHelper.getPrivateValue(BookTextParser.class, null, "FUNCTIONS");
    int spaceWidth = Minecraft.getMinecraft().fontRenderer.getStringWidth(" ");

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
    BookTextParser.CommandProcessor reset = (state) -> {
      state.color = state.prevColor;
      state.cluster = null;
      state.tooltip = "";
      state.onClick = null;
      state.isExternalLink = false;
      return "";
    };
    FUNCTIONS.put("cat", categoryFunction);
    COMMANDS.put("/cat", reset);

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
    COMMANDS.put("/uses", reset);

    Function<String, BookTextParser.FunctionProcessor> prop = (type) ->
        (parameter, state) -> {
          if (parameter.contains("/")) {
            String[] parts = parameter.split("/");
            RitualBase ritual = RitualRegistry.getRitual(parts[0]);
            SpellBase spell = SpellRegistry.getSpell(parts[0]);
            PropertyTable props;
            String propName = parts[1];
            Object value = null;
            if (type.equals("ritual") && ritual != null) {
              props = ritual.getProperties();
              if (props.hasProperty(propName)) {
                value = props.getValue(propName);
              } else {
                return "INVALID PROPERTY: " + propName;
              }
            } else if (type.equals("ritual")) {
              return "INVALID RITUAL";
            } else if (type.equals("spell") && spell != null) {
              props = spell.getProperties();
              if (props.hasProperty(propName)) {
                value = props.getValue(propName);
              } else {
                return "INVALID PROPERTY: " + propName;
              }
            } else if (type.equals("spell")) {
              return "INVALID SPELL";
            }
            if (value != null) {
              return value.toString();
            } else {
              return "INVALID COMMAND";
            }
          } else {
            return "INVALID " + (type.equals("spell") ? "SPELL" : "RITUAL") + "COMMAND, REQUIRES PROPERTY TOO";
          }
        };

    FUNCTIONS.put("spell", prop.apply("spell"));
    COMMANDS.put("/spell", reset);

    FUNCTIONS.put("ritual", prop.apply("ritual"));
    COMMANDS.put("/ritual", reset);

/*    BookTextParser.FunctionProcessor li_link = (parameter, state) -> {
      state.lineBreaks = 1;
      state.spacingLeft = 4;
      state.spacingRight = spaceWidth;

      state.cluster = new LinkedList<>();

      state.prevColor = state.color;
      state.color = state.book.linkColor;
      boolean isExternal = parameter.matches("^https?\\:.*");

      if (isExternal) {
        String url = parameter;
        state.tooltip = I18n.format("patchouli.gui.lexicon.external_link");
        state.isExternalLink = true;
        state.onClick = () -> GuiBook.openWebLink(url);
      } else {
        int hash = parameter.indexOf('#');
        String anchor = null;
        if (hash >= 0) {
          anchor = parameter.substring(hash + 1);
          parameter = parameter.substring(0, hash);
        }

        ResourceLocation href = new ResourceLocation(state.book.getModNamespace(), parameter);
        BookEntry entry = state.book.contents.entries.get(href);
        if (entry != null) {
          state.tooltip = entry.isLocked() ? (TextFormatting.GRAY + I18n.format("patchouli.gui.lexicon.locked")) : entry.getName();
          GuiBook gui = state.gui;
          Book book = state.book;
          int page = 0;
          if (anchor != null) {
            int anchorPage = entry.getPageFromAnchor(anchor);
            if (anchorPage >= 0)
              page = anchorPage / 2;
            else
              state.tooltip += " (INVALID ANCHOR:" + anchor + ")";
          }
          int finalPage = page;
          state.onClick = () -> {
            GuiBookEntry entryGui = new GuiBookEntry(book, entry, finalPage);
            gui.displayLexiconGui(entryGui, true);
            GuiBook.playBookFlipSound(book);
          };
        } else {
          state.tooltip = "BAD LINK: " + parameter;
        }
      }
      char bullet = '\u2022';
      return TextFormatting.BLACK.toString() + bullet + TextFormatting.RESET.toString();
    };

    FUNCTIONS.put("lil", li_link);
    COMMANDS.put("/lil", reset);*/
  }
}
