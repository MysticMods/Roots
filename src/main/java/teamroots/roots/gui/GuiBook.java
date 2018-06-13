package teamroots.roots.gui;

import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import teamroots.roots.EventManager;
import teamroots.roots.book.Book;
import teamroots.roots.book.BookRegistry;
import teamroots.roots.book.Page;
import teamroots.roots.util.Misc;
import teamroots.roots.util.RenderUtil;

public class GuiBook extends GuiScreen {
	String bookName = "";
	ItemStack bookItem = ItemStack.EMPTY;
	public double mouseX = 0;
	public double mouseY = 0; 
	public double smoothMouseX = 0;
	public double smoothMouseY = 0; 
	public int contentsPage = -1;
	
	public boolean showLeftArrow = false, showRightArrow = false;
	
	public int tooltipX = 0, tooltipY = 0;
	ItemStack tooltipStack = null;
	public boolean renderTooltip = false;
	public int framesExisted = 0;
	
	public GuiBook(String name, ItemStack book){
		this.bookName = name;
		this.bookItem = book;
	}
	
	public void markTooltipForRender(ItemStack stack, int x, int y){
		renderTooltip = true;
		tooltipX = x;
		tooltipY = y;
		tooltipStack = stack;
	}
	
	public void doRenderTooltip(){
		if (renderTooltip){
			this.renderToolTip(tooltipStack, tooltipX, tooltipY);
			renderTooltip = false;
		}
	}
	
	public void renderItemStackAt(ItemStack stack, int x, int y, int mouseX, int mouseY){
		if (stack != ItemStack.EMPTY){
			RenderHelper.disableStandardItemLighting();
			RenderHelper.enableGUIStandardItemLighting();
			this.itemRender.renderItemIntoGUI(stack, x, y);
			this.itemRender.renderItemOverlayIntoGUI(this.fontRenderer, stack, x, y, stack.getCount() != 1 ? Integer.toString(stack.getCount()) : "");
			if (mouseX >= x && mouseY >= y && mouseX < x+16 && mouseY < y+16){
				this.markTooltipForRender(stack, mouseX, mouseY);
			}
			RenderHelper.enableStandardItemLighting();
		}
		GlStateManager.disableLighting();
	}
	
	public void renderItemStackMinusTooltipAt(ItemStack stack, int x, int y, int mouseX, int mouseY){
		if (stack != ItemStack.EMPTY){
			RenderHelper.disableStandardItemLighting();
			RenderHelper.enableGUIStandardItemLighting();
			this.itemRender.renderItemIntoGUI(stack, x, y);
			this.itemRender.renderItemOverlayIntoGUI(this.fontRenderer, stack, x, y, stack.getCount() != 1 ? Integer.toString(stack.getCount()) : "");
			RenderHelper.enableStandardItemLighting();
		}
		GlStateManager.disableLighting();
	}
	
	@Override
	public boolean doesGuiPauseGame(){
		return false;
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton){
		float basePosX = ((float)width/2.0f)-96;
		float basePosY = ((float)height/2.0f)-128;

		Book book = BookRegistry.books.get(bookName);
		Page page = book.pages.get(book.selectedPage);
		if (showLeftArrow){
			if (mouseX >= basePosX+16 && mouseY >= basePosY+224 && mouseX <= basePosX+48 && mouseY <= basePosY+240){
				book.selectedPage --;
			}
		}
		if (showRightArrow){
			if (mouseX >= basePosX+144 && mouseY >= basePosY+224 && mouseX <= basePosX+176 && mouseY <= basePosY+240){
				book.selectedPage ++;
			}
		}
		if (contentsPage != -1){
			if (mouseX >= basePosX+80 && mouseY >= basePosY+224 && mouseX <= basePosX+112 && mouseY <= basePosY+240){
				book.selectedPage = contentsPage;
			}
		}
		if (page.type == Page.EnumPageType.TABLE_OF_CONTENTS){
			if (book.selectedPage < book.pages.size()-1){
				int ic = 0;
				for (int i = book.selectedPage; i < book.pages.size(); i ++){
					if (book.pages.get(i).pageIcon != ItemStack.EMPTY){
						if (mouseX >= basePosX+17+(ic%4)*42 && mouseY >= basePosY+48+(ic/4)*28 && mouseX <= basePosX+49+(ic%4)*42 && mouseY <= basePosY+80+(ic/4)*28){
							book.selectedPage = i;
						}
						ic ++;
					}
				}
			}
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks){
		GlStateManager.color(1, 1, 1, 1);
		GlStateManager.disableLighting();
		GlStateManager.enableAlpha();
		
		Book book = BookRegistry.books.get(this.bookName);
		Page page = book.pages.get(book.selectedPage);
		boolean satisfiesReqs = true;
		if (page.doesReqKnowledge){
			satisfiesReqs = Book.getKnowledge(bookItem) >= page.reqKnowledge;
		}
		this.contentsPage = -1;
		for (int i = 0; i < book.pages.size(); i ++){
			if (book.pages.get(i).type == Page.EnumPageType.TABLE_OF_CONTENTS){
				this.contentsPage = i;
			}
		}
		
		if (book.selectedPage == 0){
			showLeftArrow = false;
		}
		else {
			showLeftArrow = true;
		}
		if (book.selectedPage >= book.pages.size() - 1){
			showRightArrow = false;
		}
		else {
			showRightArrow = true;
		}
		this.drawDefaultBackground();
		GlStateManager.color(1, 1, 1, 1);
		Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("roots:textures/gui/gui_book.png"));
		GlStateManager.color(1, 1, 1, 1);
		this.mouseX = mouseX;
		this.mouseY = mouseY;

		int basePosX = (int)((float)width/2.0f)-96;
		int basePosY = (int)((float)height/2.0f)-128;
		
		this.drawTexturedModalRect(basePosX, basePosY, 0, 0, 192, 256);
		
		if (satisfiesReqs){
			RenderUtil.drawCenteredString(this.fontRenderer, I18n.format("roots.book."+book.name+"."+page.name+".title"), basePosX+96, basePosY+20, 0x2b2b2a);
		}
		else {
			RenderUtil.drawCenteredString(this.fontRenderer, I18n.format("roots.book.unknown_title"), basePosX+96, basePosY+20, 0x2b2b2a);
		}
		GlStateManager.color(1f, 1f, 1f, 1f);
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("roots:textures/gui/gui_book.png"));
		if (page.type == Page.EnumPageType.TEXT && satisfiesReqs){
			List<String> strings = Misc.getLines(I18n.format("roots.book."+book.name+"."+page.name+".desc"), 160);
			for (int i = 0; i < strings.size(); i ++){
				fontRenderer.drawString(strings.get(i), basePosX+20, basePosY+48+12*i, 0x2b2b2a);
				GlStateManager.color(1, 1, 1, 1);
			}
		}
		if (page.type == Page.EnumPageType.DISPLAY && satisfiesReqs){
			drawTexturedModalRect(basePosX+84,basePosY+52,192,48,24,24);
			this.renderItemStackAt(page.displayStack, basePosX+88, basePosY+56, mouseX, mouseY);
			List<String> strings = Misc.getLines(I18n.format("roots.book."+book.name+"."+page.name+".desc"), 160);
			for (int i = 0; i < strings.size(); i ++){
				fontRenderer.drawString(strings.get(i), basePosX+20, basePosY+80+12*i, 0x2b2b2a);
				GlStateManager.color(1, 1, 1, 1);
			}
		}
		if (page.type == Page.EnumPageType.MORTAR && satisfiesReqs){
			drawTexturedModalRect(basePosX+84,basePosY+68,192,96,24,24);
			drawTexturedModalRect(basePosX+84,basePosY+116,224,72,24,24);
			drawTexturedModalRect(basePosX+84,basePosY+140,192,48,24,24);
			this.renderItemStackAt(page.mortarOutput, basePosX+88, basePosY+144, mouseX, mouseY);
			for (int i = 0; i < page.mortarInputs.length; i ++){
				double angle = (double)i/(double)page.mortarInputs.length*2.0*Math.PI;
				this.renderItemStackAt(page.mortarInputs[i], basePosX+88+(int)(24.0*Math.cos(angle)), basePosY+72+(int)(24.0*Math.sin(angle)), mouseX, mouseY);
			}
			
			List<String> strings = Misc.getLines(I18n.format("roots.book."+book.name+"."+page.name+".desc"), 160);
			for (int i = 0; i < strings.size(); i ++){
				fontRenderer.drawString(strings.get(i), basePosX+20, basePosY+166+12*i, 0x2b2b2a);
				GlStateManager.color(1, 1, 1, 1);
			}
		}
		if (page.type == Page.EnumPageType.TABLE_OF_CONTENTS){
			if (book.selectedPage < book.pages.size()-1){
				int ic = 0;
				for (int i = book.selectedPage; i < book.pages.size(); i ++){
					if (book.pages.get(i).pageIcon != ItemStack.EMPTY){
						if (mouseX >= basePosX+17+(ic%4)*42 && mouseY >= basePosY+52+(ic/4)*28 && mouseX <= basePosX+49+(ic%4)*42 && mouseY <= basePosY+80+(ic/4)*28){
							drawTexturedModalRect(basePosX+17+(ic%4)*42,basePosY+72+(ic/4)*28,192,40,32,8);
						}
						else {
							drawTexturedModalRect(basePosX+17+(ic%4)*42,basePosY+72+(ic/4)*28,192,32,32,8);
						}
						ic ++;
					}
				}
				ic = 0;
				for (int i = book.selectedPage; i < book.pages.size(); i ++){
					if (book.pages.get(i).pageIcon != ItemStack.EMPTY){
						boolean bookReqs = book.pages.get(i).enoughKnowledge(bookItem);
						if (bookReqs){
							this.renderItemStackMinusTooltipAt(book.pages.get(i).pageIcon.setStackDisplayName(I18n.format("roots.book."+book.name+"."+book.pages.get(i).name+".title")), basePosX+25+(ic%4)*42,basePosY+56+(ic/4)*28, mouseX, mouseY);
							if (mouseX >= basePosX+17+(ic%4)*42 && mouseY >= basePosY+52+(ic/4)*28 && mouseX <= basePosX+49+(ic%4)*42 && mouseY <= basePosY+80+(ic/4)*28){
								this.markTooltipForRender((new ItemStack(Items.STICK)).setStackDisplayName(I18n.format("roots.book."+book.name+"."+book.pages.get(i).name+".title")), mouseX, mouseY);
							}
						}
						else {
							Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("roots:textures/gui/gui_book.png"));
							drawTexturedModalRect(basePosX+17+(ic%4)*42,basePosY+56+(ic/4)*28,192,176,32,16);
							if (mouseX >= basePosX+17+(ic%4)*42 && mouseY >= basePosY+52+(ic/4)*28 && mouseX <= basePosX+49+(ic%4)*42 && mouseY <= basePosY+80+(ic/4)*28){
								this.markTooltipForRender((new ItemStack(Items.STICK)).setStackDisplayName(I18n.format("roots.book.more_knowledge")), mouseX, mouseY);
							}
						}
						ic ++;
					}
				}
			}
		}
		if (page.type == Page.EnumPageType.MOONLIGHT && satisfiesReqs){
			drawTexturedModalRect(basePosX+84,basePosY+68,192,48,24,24);
			drawTexturedModalRect(basePosX+132,basePosY+68,224,96,24,24);
			drawTexturedModalRect(basePosX+36,basePosY+68,224,96,24,24);
			drawTexturedModalRect(basePosX+84,basePosY+116,224,72,24,24);
			drawTexturedModalRect(basePosX+84,basePosY+140,192,48,24,24);
			this.renderItemStackAt(page.moonlightOutput, basePosX+88, basePosY+144, mouseX, mouseY);
			this.renderItemStackAt(page.moonlightInputs[0], basePosX+88, basePosY+72, mouseX, mouseY);
			for (int i = 1; i < page.moonlightInputs.length; i ++){
				double angle = (double)i/(double)(page.moonlightInputs.length-1)*2.0*Math.PI;
				this.renderItemStackAt(page.moonlightInputs[i], basePosX+88+(int)(24.0*Math.cos(angle)), basePosY+72+(int)(24.0*Math.sin(angle)), mouseX, mouseY);
			}
			
			List<String> strings = Misc.getLines(I18n.format("roots.book."+book.name+"."+page.name+".desc"), 160);
			for (int i = 0; i < strings.size(); i ++){
				fontRenderer.drawString(strings.get(i), basePosX+20, basePosY+166+12*i, 0x2b2b2a);
				GlStateManager.color(1, 1, 1, 1);
			}
		}
		if (page.type == Page.EnumPageType.RITUAL && satisfiesReqs){
			drawTexturedModalRect(basePosX+84,basePosY+68,192,192,24,24);
			for (int i = 0; i < page.ritualInputs.length; i ++){
				double angle = (double)i/(double)(page.ritualInputs.length)*2.0*Math.PI;
				this.renderItemStackAt(page.ritualInputs[i], basePosX+88+(int)(24.0*Math.cos(angle)), basePosY+72+(int)(24.0*Math.sin(angle)), mouseX, mouseY);
			}
			
			List<String> strings = Misc.getLines(I18n.format("roots.book."+book.name+"."+page.name+".desc"), 160);
			for (int i = 0; i < strings.size(); i ++){
				fontRenderer.drawString(strings.get(i), basePosX+20, basePosY+116+12*i, 0x2b2b2a);
				GlStateManager.color(1, 1, 1, 1);
			}
		}
		if (page.type == Page.EnumPageType.CRAFTING && satisfiesReqs){
			for (int i = 0; i < 3; i ++){
				for (int j = 0; j < 3; j ++){
					drawTexturedModalRect(basePosX+60+24*i,basePosY+48+24*j,192,48,24,24);
				}
			}
			drawTexturedModalRect(basePosX+84,basePosY+122,224,72,24,24);
			drawTexturedModalRect(basePosX+84,basePosY+146,192,48,24,24);
			this.renderItemStackAt(page.craftingOutput, basePosX+88, basePosY+150, mouseX, mouseY);

			for (int i = 0; i < 3; i ++){
				for (int j = 0; j < 3; j ++){
					this.renderItemStackAt(page.craftingInputs[j*3+i], basePosX+64+i*24, basePosY+52+j*24, mouseX, mouseY);
				}
			}
			
			List<String> strings = Misc.getLines(I18n.format("roots.book."+book.name+"."+page.name+".desc"), 160);
			for (int i = 0; i < strings.size(); i ++){
				fontRenderer.drawString(strings.get(i), basePosX+20, basePosY+176+12*i, 0x2b2b2a);
			}
		}
		if (page.type == Page.EnumPageType.FURNACE && satisfiesReqs){
			drawTexturedModalRect(basePosX+60,basePosY+52,192,48,24,24);
			drawTexturedModalRect(basePosX+84,basePosY+52,224,24,24,24);
			drawTexturedModalRect(basePosX+84,basePosY+76,224,48,24,24);
			drawTexturedModalRect(basePosX+108,basePosY+52,192,48,24,24);
			this.renderItemStackAt(page.furnaceInput, basePosX+64, basePosY+56, mouseX, mouseY);
			this.renderItemStackAt(page.furnaceOutput, basePosX+112, basePosY+56, mouseX, mouseY);
			List<String> strings = Misc.getLines(I18n.format("roots.book."+book.name+"."+page.name+".desc"), 160);
			for (int i = 0; i < strings.size(); i ++){
				fontRenderer.drawString(strings.get(i), basePosX+20, basePosY+104+12*i, 0x2b2b2a);
				GlStateManager.color(1, 1, 1, 1);
			}
		}
		Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("roots:textures/gui/gui_book.png"));
		GlStateManager.color(1f, 1f, 1f, 1f);
		if (showLeftArrow){
			if (mouseX >= basePosX+16 && mouseY >= basePosY+224 && mouseX <= basePosX+48 && mouseY <= basePosY+240){
				drawTexturedModalRect(basePosX+16,basePosY+224,224,144,32,16);
			}
			else {
				drawTexturedModalRect(basePosX+16,basePosY+224,224,128,32,16);
			}
		}
		if (showRightArrow){
			if (mouseX >= basePosX+144 && mouseY >= basePosY+224 && mouseX <= basePosX+176 && mouseY <= basePosY+240){
				drawTexturedModalRect(basePosX+144,basePosY+224,192,144,32,16);
			}
			else {
				drawTexturedModalRect(basePosX+144,basePosY+224,192,128,32,16);
			}
		}
		if (contentsPage != -1){
			if (mouseX >= basePosX+80 && mouseY >= basePosY+224 && mouseX <= basePosX+112 && mouseY <= basePosY+240){
				drawTexturedModalRect(basePosX+80,basePosY+224,224,160,32,16);
			}
			else {
				drawTexturedModalRect(basePosX+80,basePosY+224,192,160,32,16);
			}
		}
		
		doRenderTooltip();
		GlStateManager.color(1f, 1f, 1f, 1f);
		
	    GlStateManager.disableBlend();
		RenderHelper.enableStandardItemLighting();
		GlStateManager.enableLighting();
		GlStateManager.disableAlpha();
	}
}
