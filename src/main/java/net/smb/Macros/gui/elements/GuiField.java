package net.smb.Macros.gui.elements;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatAllowedCharacters;
import net.smb.Macros.CodeParser;
import net.smb.Macros.gui.screens.GuiScreenHeader;
import net.smb.Macros.util.Color;
import net.smb.Macros.util.Log;
import net.smb.Macros.util.RenderUtil;
import net.smb.Macros.util.ResourceLocations;

public class GuiField extends GuiElement {
	public String text;
	public String description = "";
	
	public int limit = 0;
	private int cursorPos = 0;
	private int selectionPos = 0;
	private int timer;
	private int textAnchorX = 0;
	
	private boolean displayEditPlace;
	
	public float scale = 1.0F;
	
	public boolean saved = true;
	public boolean code = false;
	public boolean numbers = false;
	
	public GuiField(int posX, int posY, int width, float scale, String text, String description){
		super(-1, posX, posY, width, 20);
		this.text = text;
		this.scale = scale;
		this.description = description;
	}
	
	public void update() {
		timer--;
		if(timer <= 0) {
			timer = 5;
			displayEditPlace = !displayEditPlace;
		}
	}
	
	public void draw(Minecraft mc, int positionX, int positionY) {
		if(this.visible) {
			mc.getTextureManager().bindTexture(ResourceLocations.guiAtlas);
			
			int outlineX = this.posX-1;
			int outlineY = this.posY-1;
			int outlineWidth = this.width+2;
			int outlineHeight = this.height+2;
			
			RenderUtil.setColor(Color.color_8);
			RenderUtil.drawFromAtlas(outlineX, outlineY, 5, 1, outlineHeight, outlineHeight);
			RenderUtil.drawFromAtlas(outlineX+outlineHeight, outlineY, 7, 1, outlineWidth-outlineHeight*2, outlineHeight);
			RenderUtil.drawFromAtlas(outlineX+outlineWidth-outlineHeight, outlineY, 9, 1, outlineHeight, outlineHeight);
			
			if(!this.selected) RenderUtil.setColor(Color.color_7);
			else RenderUtil.setColor(Color.color_10);
			RenderUtil.drawFromAtlas(this.posX, this.posY, 5, 1, this.height, this.height);
			RenderUtil.drawFromAtlas(this.posX+this.height, this.posY, 7, 1, this.width-this.height*2, this.height);
			RenderUtil.drawFromAtlas(this.posX+this.width-this.height, this.posY, 9, 1, this.height, this.height);
			
			String displayText = text;
			Color color = Color.color_4;
			if(text.equals("") && !this.selected) {
				displayText = description;
				color = new Color(Color.color_8, 0.5f);
			}
			else if(!text.equals("") && code) {
				displayText = CodeParser.setColor(text);
			}
			RenderUtil.glScissor(this.posX+1, this.posY, this.width-2, this.height);
			
			RenderUtil.drawString(displayText, this.posX+3+textAnchorX, this.posY+(int)Math.floor(this.height/4.0f), color, false, scale, false);
			
			if(this.selected) {
				if(displayEditPlace) {
					int currentCharPos = 0;
					if(!text.equals("")) currentCharPos = (int)(RenderUtil.fontRenderer.getStringWidth(text.substring(0, cursorPos))*scale)+textAnchorX;
					RenderUtil.drawString("|", this.posX+3+currentCharPos, this.posY+(int)Math.floor(this.height/4.0f), Color.color_4, false, scale, false);
				}
				
				if(this.pressed) {
					setCursorPos(getCharAtPos(positionX, positionY), false);
				}
			
				if(cursorPos != selectionPos) {
					String selectedText = getSelectedText();
					
					int pos1 = selectionPos;
					if(cursorPos < selectionPos) {
						pos1 = cursorPos;
					}
					
					int selectedWidth = (int)(RenderUtil.fontRenderer.getStringWidth(selectedText)*scale);
					int firstPos = (int)(RenderUtil.fontRenderer.getStringWidth(text.substring(0, pos1))*scale)+this.posX+3+textAnchorX;
					
					RenderUtil.setColor(Color.color_11);
					GL11.glEnable(GL11.GL_COLOR_LOGIC_OP);
			        GL11.glLogicOp(GL11.GL_OR_REVERSE);
					RenderUtil.drawRect(firstPos, this.posY+(int)Math.floor(this.height/4.0f), firstPos+selectedWidth, this.posY+6+RenderUtil.fontRenderer.FONT_HEIGHT);
					GL11.glDisable(GL11.GL_COLOR_LOGIC_OP);
				}
			}
			
			RenderUtil.glScissorDisable();
		}
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
		this.setSelected(false);
	}
	
	public boolean clicked(int id, int posX, int posY, GuiScreenHeader gui) {
    	if(this.visible && pressed(posX, posY)) {
    		if(gui != null) {
    			gui.actionPerfomed(this, id);
    			gui.pressed = this;
    			gui.selected = this;
    		}
    		setSelected(true);
    		this.pressed = true;
    		
    		setCursorPos(getCharAtPos(posX, posY), true);
    		return true;
    	}
    	else return false;
    }
	
	public void keyTyped(char key, int keyId) {
		if(selected) {
			if(keyId == Keyboard.KEY_BACK && text.length()>0) delete();
			else if(key == 3) {
				if(selectionPos != cursorPos) GuiScreen.setClipboardString(getSelectedText());
    		}
    		else if(key == 22) addText(GuiScreen.getClipboardString());
    		else if(key == 24) {
    			if(selectionPos != cursorPos) {
					GuiScreen.setClipboardString(getSelectedText());
					delete(true);
				}
    		}
    		else if(keyId == Keyboard.KEY_LEFT) {
    			if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) moveCursor(-1, false);
    			else if(selectionPos != cursorPos) {
    				int pos1 = selectionPos;
    				if(cursorPos < selectionPos) {
    					pos1 = cursorPos;
    				}
    				setCursorPos(pos1, true);
    			}
    			else moveCursor(-1);
    		}
    		else if(keyId == Keyboard.KEY_RIGHT) {
    			if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) moveCursor(1, false);
    			else if(selectionPos != cursorPos) {
    				int pos2 = cursorPos;
    				if(cursorPos < selectionPos) {
    					pos2 = selectionPos;
    				}
    				setCursorPos(pos2, true);
    			}
    			else moveCursor(1);
    		}
    		else if(keyId == Keyboard.KEY_ESCAPE) setSelected(false);
    		//else addText(key + "");
    		else if(keyId == Keyboard.KEY_A && Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
    			this.selectionPos = 0;
    			setCursorPos(text.length(), false);
    		}
    		else if(keyId == Keyboard.KEY_RETURN) {
    			setSelected(false);
    			GuiScreenHeader screen = (GuiScreenHeader)Minecraft.getMinecraft().currentScreen;
    			if(screen.selected == this) screen.selected = null;
    		}
    		else addText(String.valueOf(key));
    	}
	}
	
	public void addText(String add) {
		add = filerAllowedCharacters(add);
		if(!add.equals("")) {
			delete(true);
			saved = false;
			String beforeText = text.substring(0, cursorPos);
			String afterText = text.substring(cursorPos, text.length());
			int addedSize = 0;
			for(char c : add.toCharArray()) {
				if(this.limit != 0 && this.text.length() >= this.limit) {
					break;
				}
				else {
					beforeText += c;
					addedSize++;
				}
			}
			text = beforeText + afterText;
			if(addedSize != 0) moveCursor(addedSize);
		}
	}
	public void delete() {
		delete(false);
	}
	
	public void delete(boolean onlySelection) {
		if(selectionPos != cursorPos) {
			saved = false;
			int pos1 = selectionPos;
			int pos2 = cursorPos;
			if(cursorPos < selectionPos) {
				pos1 = cursorPos;
				pos2 = selectionPos;
			}
			
			String beforeText = text.substring(0, pos1);
			String afterText = text.substring(pos2, text.length());
			
			setCursorPos(pos1, true);
			text = beforeText + afterText;
		}
		else if(!onlySelection) {
			saved = false;
			String beforeText = text.substring(0, cursorPos);
			String afterText = text.substring(cursorPos, text.length());
			if(beforeText.length() > 0) {
				moveCursor(-1);
				beforeText = beforeText.substring(0, beforeText.length()-1);
			}
			text = beforeText + afterText;
		}
	}
	
	public void setSelected(boolean selected) {
    	this.selected = selected;
    	if(selected) {
    		setCursorPos(text.length(), true);
    	}
    	else {
    		((GuiScreenHeader)Minecraft.getMinecraft().currentScreen).actionReleased(this);
    	}
    }
	
	public void moveCursor(int move) {
		moveCursor(move, true);
	}
	
	public void moveCursor(int move, boolean moveSelected) {
		setCursorPos(cursorPos+move, moveSelected);
	}
	
	public void setCursorPos(int num, boolean moveSelected) {
		cursorPos = Math.max(Math.min(num, text.length()), 0);
		timer = 5;
		displayEditPlace = true;
		if(moveSelected) this.selectionPos = cursorPos;
		
		int widthToCursor = 0;
		if(!text.equals("")) widthToCursor = (int)(RenderUtil.fontRenderer.getStringWidth(text.substring(0, cursorPos))*scale);
		
		if(widthToCursor+textAnchorX > this.width-6) setAnchorX(-(widthToCursor-15));
		else if(widthToCursor + textAnchorX - 3 < 0) setAnchorX(-(widthToCursor-this.width+15));
	}
	
	private void setAnchorX(int anchor) {
		textAnchorX = Math.min(Math.max(anchor, -(RenderUtil.fontRenderer.getStringWidth(text)-this.width+6)), 0);
	}
	
	public int getCharAtPos(int posX, int posY) {
		int needWidth = posX-(this.posX+3)-textAnchorX;
		if(needWidth >= 0) {
			String text = RenderUtil.fontRenderer.trimStringToWidth(this.text, needWidth, false, scale);
			return text.length();
		}
		else return 0;
	}
	
	public String getSelectedText() {
		int pos1 = selectionPos;
		int pos2 = cursorPos;
		if(cursorPos < selectionPos) {
			pos1 = cursorPos;
			pos2 = selectionPos;
		}
		
		return text.substring(pos1, pos2);
	}
	
	public String filerAllowedCharacters(String text)
    {
        StringBuilder var1 = new StringBuilder();
        char[] var2 = text.toCharArray();
        int var3 = var2.length;

        if(numbers) {
        	 for (int var4 = 0; var4 < var3; ++var4)
             {
                 char var5 = var2[var4];

                 if (isAllowedCharacterNum(var5))
                 {
                     var1.append(var5);
                 }
             }
        }
        else {
	        for (int var4 = 0; var4 < var3; ++var4)
	        {
	            char var5 = var2[var4];
	
	            if (isAllowedCharacter(var5))
	            {
	                var1.append(var5);
	            }
	        }
        }

        return var1.toString();
    }
	
	public boolean isAllowedCharacter(char c)
    {
        return c == 10 || (c != 167 && c >= 32 && c != 127);
    }
	public boolean isAllowedCharacterNum(char c)
    {
        return "0123456789".indexOf(c) != -1;
    }
}
