package teamroots.roots.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import teamroots.roots.book.BookRegistry;

public class GuiHandler implements IGuiHandler{

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch(ID){
		case 0:
			return new GuiBook(BookRegistry.herblore_book.name,player.getHeldItemMainhand());
		case 1:
			return new GuiBook(BookRegistry.herblore_book.name,player.getHeldItemOffhand());
		case 2:
			return new GuiBook(BookRegistry.spellcraft_book.name,player.getHeldItemMainhand());
		case 3:
			return new GuiBook(BookRegistry.spellcraft_book.name,player.getHeldItemOffhand());
		case 4:
			return new GuiBook(BookRegistry.ritual_book.name,player.getHeldItemMainhand());
		case 5:
			return new GuiBook(BookRegistry.ritual_book.name,player.getHeldItemOffhand());
		case 6:
			return new GuiBook(BookRegistry.forbidden_book.name,player.getHeldItemMainhand());
		case 7:
			return new GuiBook(BookRegistry.forbidden_book.name,player.getHeldItemOffhand());
		}
		return null;
	}

}
