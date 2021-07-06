package net.smb.Macros.gui.screens;

import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.smb.Macros.Localisation;
import net.smb.Macros.MacrosSettings;
import net.smb.Macros.gui.Layout;
import net.smb.Macros.gui.LayoutManager;
import net.smb.Macros.gui.elements.GuiButton;
import net.smb.Macros.gui.elements.GuiButtonWithIcon;
import net.smb.Macros.gui.elements.GuiElement;
import net.smb.Macros.gui.elements.GuiField;
import net.smb.Macros.gui.elements.GuiPanel;
import net.smb.Macros.gui.elements.GuiScrollView;
import net.smb.Macros.gui.widgets.Widget;
import net.smb.Macros.util.GuiUtil;

public class GuiScreenGuiEditor extends GuiScreenHeader {
	private GuiScreen backMenu;
	public Layout layout;
	public GuiPanel editPanel;
	public GuiPanel editWidget;
	public Widget currentWidget;
	public GuiPanel addElementPanel;
	public GuiPanel layoutEdit;
	public GuiScrollView layoutsScroll;
	public GuiElement lastSelected;
	
	private int rotateState;

	public GuiScreenGuiEditor(Minecraft mc, Layout layout, GuiScreen backMenu) {
		super(mc);
		this.layout = layout;
		this.backMenu = backMenu;
	}
	
	public void initGui()
    {
		super.initGui();
		
		guiElements.addAll(layout.widgets);
		
		editPanel = new GuiPanel(-1, MacrosSettings.getInt("guiEditPanelX"), MacrosSettings.getInt("guiEditPanelY"), 16, 89);
		editPanel.canMove = true;
		editPanel.minSize = 6;
		editPanel.shadow = true;
		
		editPanel.addGuiElement(new GuiButtonWithIcon(1, 1, 1, 14, 3));
		editPanel.addGuiElement(new GuiButtonWithIcon(2, 1, 18, 14, 4));
		editPanel.addGuiElement(new GuiButtonWithIcon(3, 1, 35, 14, 2));
		editPanel.addGuiElement(new GuiButtonWithIcon(4, 1, 57, 14, 5));
		editPanel.addGuiElement(new GuiButtonWithIcon(5, 1, 74, 14, 6));
		
		setRotatePanel(MacrosSettings.getInt("guiEditPanelRotate"));
		
		addElementPanel = new GuiPanel(-1, 13, 2, 100, 58);
		addElementPanel.setVisible(false);
		addElementPanel.hover = true;
		addElementPanel.addGuiElement(new GuiButton(6, 2, 2, 96, 10, Localisation.getString("widget.name.button")));
		addElementPanel.addGuiElement(new GuiButton(7, 2, 13, 96, 10, Localisation.getString("widget.name.text")));
		addElementPanel.addGuiElement(new GuiButton(8, 2, 24, 96, 10, Localisation.getString("widget.name.slider")));
		addElementPanel.addGuiElement(new GuiButton(9, 2, 35, 96, 10, Localisation.getString("widget.name.indicator")));
		addElementPanel.addGuiElement(new GuiButton(10, 2, 46, 96, 10, Localisation.getString("widget.name.image")));
		
		editPanel.addGuiElement(addElementPanel);
		
		layoutEdit = new GuiPanel(-1, 13, 2, 100, 90);
		layoutEdit.hover = true;
		layoutEdit.visible = false;
		
		GuiField field = new GuiField(2, 2, 96, 1.0F, this.layout.getLayoutName(), Localisation.getString("field.description.name"));
		field.id = 16;
		
		layoutEdit.addGuiElement(field);
		
		layoutsScroll = new GuiScrollView(-1, 2, 32, 96, 56);
		layoutsScroll.elementHeight = 11;
		for(Layout layout : LayoutManager.layouts) {
			layoutsScroll.addGuiElement(new GuiButton(15, 2, layoutsScroll.guiElements.size()*11+2, 85, 10, layout.getLayoutName()));
		}
		
		layoutEdit.addGuiElement(layoutsScroll);
		
		editPanel.addGuiElement(layoutEdit);
	}
	
	public void actionPerfomed(GuiElement element, int mouseButton) {
		if(mouseButton == 0) {
			switch(element.id){
			case 1:
				openAddPanel();
				break;
			case 2:
				deleteWidget();
				break;
			case 3:
				openEditPanel();
				break;
			case 4:
				mc.displayGuiScreen((GuiScreen)null);
				break;
			case 5:
				setRotatePanel(this.rotateState+1);
				break;
			case 6:
				addWidget("Button");
				break;
			case 7:
				addWidget("Text");
				break;
			case 8:
				addWidget("Slider");
				break;
			case 9:
				addWidget("Indicator");
				break;
			case 10:
				addWidget("Image");
				break;
			case 15:
				mc.displayGuiScreen(new GuiScreenGuiEditor(mc, LayoutManager.getlayout(((GuiButton)element).displayString), this.backMenu));
				break;
			case 16:
				closeWidgetPanel(true);
				break;
			case 17:
				closeWidgetPanel(false);
				break;
			}
		}
		
	}
	
	public void actionReleased(GuiElement element) {
		switch (element.id)
        {
			case 16:
				if(((GuiField)element).text.equals("")) {
					((GuiField)element).text = layout.getLayoutName();
				}
				else this.layout.renameLayout(((GuiField)element).text);
				break;
        }
	}
	
	public void addWidget(String name) {
		Widget widget = LayoutManager.createWidget(name);
		this.layout.widgets.add(widget);
		this.guiElements.add(widget);
	}
	
	public void openAddPanel() {
		if(this.rotateState == 0) {
			addElementPanel.posY = editPanel.posY + 2;
			if(editPanel.posX+addElementPanel.width+20 >= this.width) {
				addElementPanel.posX = editPanel.posX - addElementPanel.width - 2;
			}
			else {
				addElementPanel.posX = editPanel.posX + 18;
			}
		}
		else if(this.rotateState == 1) {
			addElementPanel.posX = editPanel.posX + 2;
			if(editPanel.posY+addElementPanel.height+20 >= this.height) {
				addElementPanel.posY = editPanel.posY - addElementPanel.height - 2;
			}
			else {
				addElementPanel.posY = editPanel.posY + 18;
			}
		}
		GuiUtil.setPos(addElementPanel.guiElements, addElementPanel.posX, addElementPanel.posY);
		addElementPanel.visible = true;
		layoutEdit.visible = false;
		this.selected = addElementPanel;
	}
	
	public void openEditPanel() {
		if(this.rotateState == 0) {
			layoutEdit.posY = editPanel.posY + 2;
			if(editPanel.posX+layoutEdit.width+20 >= this.width) {
				layoutEdit.posX = editPanel.posX - layoutEdit.width - 2;
			}
			else {
				layoutEdit.posX = editPanel.posX + 18;
			}
		}
		else if(this.rotateState == 1) {
			layoutEdit.posX = editPanel.posX + 2;
			if(editPanel.posY+layoutEdit.height+20 >= this.height) {
				layoutEdit.posY = editPanel.posY - layoutEdit.height - 2;
			}
			else {
				layoutEdit.posY = editPanel.posY + 18;
			}
		}
		GuiUtil.setPos(layoutEdit.guiElements, layoutEdit.posX, layoutEdit.posY);
		layoutEdit.visible = true;
		addElementPanel.visible = false;
		this.selected = layoutEdit;
	}
	
	public void deleteWidget() {
		if(this.lastSelected != null && this.lastSelected instanceof Widget) {
			this.layout.widgets.remove(this.lastSelected);
			this.guiElements.remove(this.lastSelected);
			this.selected = null;
		}
	}
	
	public void setRotatePanel(int rotate) {
		if(rotate > 1) rotate = 0;
		else if(rotate < 0) rotate = 1;
		this.rotateState = rotate;
		
		if(rotate == 0) {
			editPanel.width = 16;
			editPanel.height = 89;
			
			editPanel.guiElements.get(0).setRelPos(1, 1);
			editPanel.guiElements.get(1).setRelPos(1, 18);
			editPanel.guiElements.get(2).setRelPos(1, 35);
			editPanel.guiElements.get(3).setRelPos(1, 57);
			editPanel.guiElements.get(4).setRelPos(1, 74);
		}
		else if(rotate == 1) {
			editPanel.width = 89;
			editPanel.height = 16;
			
			editPanel.guiElements.get(0).setRelPos(1, 1);
			editPanel.guiElements.get(1).setRelPos(18, 1);
			editPanel.guiElements.get(2).setRelPos(35, 1);
			editPanel.guiElements.get(3).setRelPos(57, 1);
			editPanel.guiElements.get(4).setRelPos(74, 1);
		}
		editPanel.limitPos();
		GuiUtil.setPos(editPanel.guiElements, editPanel.posX, editPanel.posY);
	}
	
	public void showWidgetPanel(GuiPanel panel, Widget widget) {
		currentWidget = widget;
		currentWidget.setParams();
		editWidget = panel;
		editWidget.posX = width/2-editWidget.width/2;
		editWidget.posY = height/2-editWidget.height/2;
		GuiUtil.setPos(editWidget.guiElements, editWidget.posX, editWidget.posY);
		editWidget.limitPos();
		editWidget.setVisible(true);
	}
	
	public void closeWidgetPanel(boolean save) {
		if(save) currentWidget.setParams();
		else currentWidget.updateParams();
		editWidget.setVisible(false);
		editWidget = null;
	}
	
	protected void keyTyped(char key, int keyId)
    {
		editPanel.keyTyped(key, keyId);
		if(editWidget != null) editWidget.keyTyped(key, keyId);
		for(GuiElement element : guiElements) element.keyTyped(key, keyId);
		if(keyId == Keyboard.KEY_DELETE) deleteWidget();
		if (keyId == 1)
        {
			guiClose();
        }
    }
	
	public void updateScreen() {
		super.updateScreen();
		editPanel.update();
		if(editWidget != null) editWidget.update();
		for(GuiElement element : guiElements) element.update();
	}
	
	public void drawScreen(int positionX, int positionY, float partialTicks)
    {
		this.drawDefaultBackground();
		
		if(editWidget != null) {
			for(GuiElement element : guiElements) {
				element.draw(mc, -1, -1);
			}
			editPanel.draw(mc, -1, -1);
		}
		else {
			for(GuiElement element : guiElements) {
				element.draw(mc, positionX, positionY);
			}
			editPanel.draw(mc, positionX, positionY);
		}
		
		
		if(editWidget != null) editWidget.draw(mc, positionX, positionY);
	}
	
	public void handleMouseInput()
    {
        int var1 = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int var2 = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
        int var3 = Mouse.getEventButton();
        int var4 = Math.max(Math.min(Mouse.getEventDWheel(),1),-1);

        if (var3 != -1)
        {
            this.mouseMovedOrUp(var1, var2, var3);
        }
        if (Mouse.getEventButtonState())
        {
            this.mouseClicked(var1, var2, var3);
        }
        
        
        if(var4 != 0) {
        	editPanel.mouseScroll(var4);
        	for(GuiElement element : guiElements) element.mouseScroll(var4);
        }
    }
	
	public void mouseClicked(int posX, int posY, int mouseButton)
    {
		if(selected != null) {
    		selected.setSelected(false, posX, posY);
    		if(!selected.selected) selected = null;
		}
    	if(editWidget != null) {
    		editWidget.clicked(mouseButton, posX, posY, this);
    	}
    	else {
	    	if(editPanel.clicked(mouseButton, posX, posY, this));
	    	else CheckPressElements(this.guiElements, posX, posY, mouseButton);
    	}
    	lastSelected = selected;
    }
	
	public boolean CheckPressElements(List<GuiElement> elements, int posX, int posY, int mouseButton) {
		try {
			for(GuiElement element : elements) {
				if(element.clicked(mouseButton, posX, posY, this)) {
					if(!(element instanceof GuiPanel) && !(element instanceof GuiScrollView)) {
						selected = element;
						pressed = element;
					}
	    			return true;
	    		}
	    	}
		} catch(Exception e) {}
		return false;
	}
	
	public void guiClose() {
		if(this.backMenu != null) {
			if(backMenu instanceof GuiScreenMenu) ((GuiScreenMenu)this.backMenu).mouseMovedOrUp(0, 0, 0);
			mc.displayGuiScreen(this.backMenu);
		}
		else {
			mc.displayGuiScreen((GuiScreen)null);
		}
	}
	
	public void onGuiClosed() {
		LayoutManager.saveToXML();
		MacrosSettings.setParam("guiEditPanelX", editPanel.posX);
		MacrosSettings.setParam("guiEditPanelY", editPanel.posY);
		MacrosSettings.setParam("guiEditPanelRotate", this.rotateState);
		Keyboard.enableRepeatEvents(false);
		mc.setIngameFocus();
	}
}
