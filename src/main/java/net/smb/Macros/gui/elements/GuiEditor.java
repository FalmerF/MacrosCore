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

public class GuiEditor extends GuiElement {
	private String text;
	public String description = "";
	
	public int limit = 0;
	private int cursorPos = 0;
	private int selectionPos = 0;
	private int timer;
	private int textAnchorX = 0, textAnchorY = 0;
	public int lineSpace = 2;
	public int lineHeight;
	private int maxWidthLine;
	
	private int cursorLine;
	private int cursorPosInLine;
	private int widthToCursor;
	
	private int firstCharLine;
	private int firstCharPosInLine;
	private int firstPos;
	private int firstSelectedWidth;
	
	private int secondCharLine;
	private int secondCharPosInLine;
	private int secondPos;
	
	private int pressedTimer = 10;
	
	private boolean displayEditPlace;
	private boolean moveToCursor, fastMove;
	public boolean saved = true;
	
	public float scale = 1.0F;
	
	public GuiSlider verticalSlider, horizontalSlider;
	
	public GuiEditor(int posX, int posY, int width, int height, String text, String description){
		super(-1, posX, posY, width, height);
		this.text = text;
		this.description = description;
		this.verticalSlider = new GuiSlider(-1, this.posX+this.width-7, this.posY+5, 5, this.height-10, 0.0F, 1);
		this.horizontalSlider = new GuiSlider(-1, this.posX+5, this.posY+this.height-7, this.width-15, 5, 0.0F, 1, true);
		this.lineHeight = RenderUtil.fontRenderer.FONT_HEIGHT+this.lineSpace;
	}
	
	public void setPos() {
		this.verticalSlider.posX = this.verticalSlider.relPosX + this.posX;
		this.verticalSlider.posY = this.verticalSlider.relPosY + this.posY;
		
		this.horizontalSlider.posX = this.horizontalSlider.relPosX + this.posX;
		this.horizontalSlider.posY = this.horizontalSlider.relPosY + this.posY;
	}
	
	public void update() {
		timer--;
		if(timer <= 0) {
			timer = 5;
			displayEditPlace = !displayEditPlace;
		}
		
		if(this.pressed && pressedTimer > 0) pressedTimer--;
	}
	
	public void draw(Minecraft mc, int positionX, int positionY) {
		if(this.visible) {
			this.hovered = positionX >= this.posX && positionY >= this.posY && positionX < this.posX + this.width && positionY < this.posY + this.height;
			
			mc.getTextureManager().bindTexture(ResourceLocations.guiAtlas);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL11.GL_BLEND);
			if(this.enabled) RenderUtil.setColor(Color.color_12);
			else RenderUtil.setColor(Color.color_7);
			
			RenderUtil.drawFromAtlas(this.posX, this.posY, 1, 1);
			RenderUtil.drawFromAtlas(this.width+this.posX-20, this.posY, 3, 1);
			RenderUtil.drawFromAtlas(this.posX, this.height+this.posY-20, 1, 3);
			RenderUtil.drawFromAtlas(this.width+this.posX-20, this.height+this.posY-20, 3, 3);
			
			RenderUtil.drawFromAtlas(this.posX+20, this.posY, 2, 1, this.width-40, 20);
			RenderUtil.drawFromAtlas(this.width+this.posX-20, this.posY+20, 3, 2, 20, this.height-40);
			RenderUtil.drawFromAtlas(this.posX, this.posY+20, 1, 2, 20, this.height-40);
			RenderUtil.drawFromAtlas(this.posX+20, this.height+this.posY-20, 2, 3, this.width-40, 20);
			
			RenderUtil.drawFromAtlas(this.posX+20, this.posY+20, 2, 2, this.width-40, this.height-40);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glDisable(GL11.GL_BLEND);
			Color color = new Color(Color.color_4, 1.0F);
			
			RenderUtil.glScissor(this.posX+3, this.posY+3, this.width-6, this.height-6);
			
			String[] lines = getLines(text);
			
			verticalSlider.steps = Math.max(lines.length-(this.height-6)/lineHeight, 1);
			horizontalSlider.steps = Math.max((maxWidthLine-this.width)/10, 1);
			
			if(moveToCursor) {
				int step = 1;
				if(fastMove) step = 10;
				int widthToCursor = RenderUtil.fontRenderer.getStringWidth(lines[cursorLine].substring(0, cursorPosInLine));
				if(this.posY+10+textAnchorY+(lineHeight*cursorLine) > this.height+this.posY-3) {
					verticalSlider.addStep(step);
				}
				else if(this.posY+textAnchorY+(lineHeight*cursorLine) < this.posY) {
					verticalSlider.addStep(-step);
				}
				else if(this.posX+10+widthToCursor+textAnchorX > this.posX+this.width-3) {
					horizontalSlider.addStep(step);
				}
				else if(this.posX+10+widthToCursor+textAnchorX < this.posX+3) {
					horizontalSlider.addStep(-step);
				}
				else moveToCursor = false;
			}
			
			this.textAnchorY = (int)(Math.min(-((lineHeight*lines.length)-this.height+10), 0)*verticalSlider.value);
			this.textAnchorX = (int)((maxWidthLine-this.width+20)*-horizontalSlider.value);
			
			if(this.verticalSlider.steps <= 1) this.verticalSlider.visible = false;
			else this.verticalSlider.visible = true;
			if(this.horizontalSlider.steps <= 1) this.horizontalSlider.visible = false;
			else this.horizontalSlider.visible = true;
			
			if(text.equals("") && !this.selected) {
				color = new Color(Color.color_8, 0.5f);
				RenderUtil.drawString(description, this.posX+3, this.posY+3, color, false, scale, false);
			}
			else {
				for(int i = 0; i < lines.length; i++) {
					int linePosY = this.posY+textAnchorY+3+lineHeight*i;
					if(linePosY>this.posY+10-this.lineHeight && linePosY < this.posY+this.height-10) {
						RenderUtil.drawString(CodeParser.setColor(lines[i]), this.posX+3+textAnchorX, linePosY, color, false, scale, false);
					}
				}
			}
			
			if(this.selected) {
				if(displayEditPlace) {
					int currentCharPosX = this.posX+3;
					int currentCharPosY = this.posY+3;
					if(!text.equals("")) {
						currentCharPosX = this.posX+3+widthToCursor+textAnchorX;
						currentCharPosY = this.posY+3+textAnchorY+lineHeight*cursorLine;
					}
					
					RenderUtil.drawString("|", currentCharPosX, currentCharPosY, Color.color_4, false, scale, false);
				}
				
				if(this.pressed && pressedTimer <= 0) {
					pressedTimer = 2;
					setCursorPos(getCharAtPos(positionX, positionY), false);
					this.fastMove = false;
				}
			
				if(cursorPos != selectionPos) {
					RenderUtil.setColor(Color.color_11);
					GL11.glEnable(GL11.GL_COLOR_LOGIC_OP);
			        GL11.glLogicOp(GL11.GL_OR_REVERSE);
					if(firstCharLine == secondCharLine) {
				        RenderUtil.drawRect(firstPos, lineHeight*firstCharLine-1+this.posY+3+textAnchorY, secondPos, lineHeight*(firstCharLine+1)-2+this.posY+3+textAnchorY);
				       
					}
					else {
						for(int i = firstCharLine; i <= secondCharLine; i++) {
							int linePosY = (this.lineHeight*i)+this.textAnchorY+this.posY;
							if(this.posY-10 < linePosY && this.posY+this.height-3 > linePosY) {
								if(i == firstCharLine) {
									RenderUtil.drawRect(firstPos, lineHeight*i-1+this.posY+3+textAnchorY, firstPos+firstSelectedWidth, lineHeight*(i+1)-2+this.posY+3+textAnchorY);
								}
								else if(i == secondCharLine) {
									RenderUtil.drawRect(this.posX+3, lineHeight*i-1+this.posY+3+textAnchorY, secondPos, lineHeight*(i+1)-2+this.posY+3+textAnchorY);
								}
								else {
									RenderUtil.drawRect(this.posX+3, lineHeight*i-1+this.posY+3+textAnchorY, this.posX+3+RenderUtil.fontRenderer.getStringWidth(lines[i]), lineHeight*(i+1)-2+this.posY+3+textAnchorY);
								}
							}
						}
					}
					GL11.glDisable(GL11.GL_COLOR_LOGIC_OP);
				}
			}
			
			RenderUtil.glScissorDisable();
			this.verticalSlider.draw(mc, positionX, positionY);
			this.horizontalSlider.draw(mc, positionX, positionY);
		}
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
		this.setSelected(false);
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		this.setSelected(false);
	}
	
	public void mouseScroll(int scroll) {
		if(this.visible && this.hovered) {
			if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
				if(scroll > 0) horizontalSlider.addStep(-2);
				else if(scroll < 0) horizontalSlider.addStep(2);
			}
			else {
				if(scroll > 0) verticalSlider.addStep(-2);
				else if(scroll < 0) verticalSlider.addStep(2);
			}
		}
	}
	
	public boolean clicked(int id, int posX, int posY, GuiScreenHeader gui) {
		if(this.verticalSlider.clicked(id, posX, posY, gui)) return true;
		else if(this.horizontalSlider.clicked(id, posX, posY, gui)) return true;
		else if(this.visible && this.enabled && pressed(posX, posY)) {
    		if(gui != null) {
    			gui.actionPerfomed(this, id);
    			gui.pressed = this;
    			gui.selected = this;
    		}
    		setSelected(true);
    		pressedTimer = 5;
    		this.pressed = true;
    		
    		setCursorPos(getCharAtPos(posX, posY), true);
    		return true;
    	}
    	else return false;
    }
	
	public void keyTyped(char key, int keyId) {
		if(selected && this.enabled) {
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
    		else if(keyId == Keyboard.KEY_UP) {
    			moveCursorVertical(-1);
    		}
    		else if(keyId == Keyboard.KEY_DOWN) {
    			moveCursorVertical(1);
    		}
    		else if(keyId == Keyboard.KEY_ESCAPE) setSelected(false);
    		else if(keyId == Keyboard.KEY_A && Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
    			this.selectionPos = 0;
    			setCursorPos(text.length(), false);
    		}
    		else if(keyId == Keyboard.KEY_RETURN) {
    			addText(String.valueOf((char)10));
    		}
    		else addText(String.valueOf(key));
    	}
	}
	
	public void addText(String add) {
		saved = false;
		add = filerAllowedCharacters(add);
		if(!add.equals("")) delete(true);
		String beforeText = text.substring(0, cursorPos);
		String afterText = text.substring(cursorPos, text.length());
		int addedSize = add.length();
		
		text = beforeText + add + afterText;
		if(addedSize != 0) moveCursor(addedSize);
		
		String[] lines = getLines(text);
		maxWidthLine = 0;
		for(String line : lines) {
			int lineWidth = RenderUtil.fontRenderer.getStringWidth(line);
			if(lineWidth > maxWidthLine) maxWidthLine = lineWidth;
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
    	}
    	else {
    		((GuiScreenHeader)Minecraft.getMinecraft().currentScreen).actionReleased(this);
    	}
    }
	
	public void moveCursorVertical(int move) {
		String[] lines = getLines(text);
		int cursorLine = getCharLine(lines, cursorPos);
		int cursorNumInLine = getCharNumInLine(lines, cursorPos);
		int widthToCursor = RenderUtil.fontRenderer.getStringWidth(lines[cursorLine].substring(0, cursorNumInLine));
		
		int newLine = Math.max(Math.min(cursorLine+move, lines.length-1), 0);
		int newCursorPos = 0;
		for(int i = 0; i < newLine; i++) newCursorPos += lines[i].length();
		String newLineString =  RenderUtil.fontRenderer.trimStringToWidth(lines[newLine], widthToCursor+2);
		newCursorPos += newLineString.length();
		
		if(newLineString.endsWith(String.valueOf((char)10))) newCursorPos--;
		
		setCursorPos(newCursorPos, true);
	}
	
	public void moveCursor(int move) {
		moveCursor(move, true);
	}
	
	public void moveCursor(int move, boolean moveSelected) {
		setCursorPos(cursorPos+move, moveSelected);
	}
	
	public void setCursorPos(int num, boolean moveSelected) {
		cursorPos = Math.max(Math.min(num, text.length()), 0);
		String[] lines = getLines(text);
		if(moveSelected) {
			this.selectionPos = cursorPos;
		}
		else {
			int pos1 = selectionPos;
			int pos2 = cursorPos;
			if(cursorPos < selectionPos) {
				pos1 = cursorPos;
				pos2 = selectionPos;
			}
			
			firstCharLine = getCharLine(lines, pos1);
			firstCharPosInLine = getCharNumInLine(lines, pos1);
			firstPos = (int)(RenderUtil.fontRenderer.getStringWidth(lines[firstCharLine].substring(0, firstCharPosInLine))*scale)+this.posX+3+textAnchorX;
			firstSelectedWidth = (int)(RenderUtil.fontRenderer.getStringWidth(lines[firstCharLine].substring(firstCharPosInLine, lines[firstCharLine].length()))*scale);
			
			secondCharLine = getCharLine(lines, pos2);
			secondCharPosInLine = getCharNumInLine(lines, pos2);
			secondPos = (int)(RenderUtil.fontRenderer.getStringWidth(lines[secondCharLine].substring(0, secondCharPosInLine))*scale)+this.posX+3+textAnchorX;
		}
		
		cursorLine = getCharLine(lines, cursorPos);
		cursorPosInLine = getCharNumInLine(lines, cursorPos);
		widthToCursor = RenderUtil.fontRenderer.getStringWidth(lines[cursorLine].substring(0, cursorPosInLine));

		if(this.posY+10+textAnchorY+(lineHeight*cursorLine) > this.height+this.posY-3) {
			this.fastMove = true;
			this.moveToCursor = true;
		}
		else if(this.posY+textAnchorY+(lineHeight*cursorLine) < this.posY) {
			this.fastMove = true;
			this.moveToCursor = true;
		}
		
		if(this.posX+10+widthToCursor+textAnchorX > this.posX+this.width-3) {
			this.fastMove = true;
			this.moveToCursor = true;
		}
		else if(this.posX+10+widthToCursor+textAnchorX < this.posX+3) {
			this.fastMove = true;
			this.moveToCursor = true;
		}
	}
	
	public int getCharAtPos(int posX, int posY) {
		int needWidth = posX-(this.posX+3)-textAnchorX;
		posY = posY-this.posY-3-this.textAnchorY;
		int charLine = Math.max(posY/lineHeight, 0);
		String[] lines = getLines(this.text);
		if(charLine > lines.length-1) return this.text.length();
		int numToLine = 0;
		for(int i = 0; i < charLine; i++) {
			numToLine += lines[i].length();
		}
		if(needWidth >= 0) {
			String text = RenderUtil.fontRenderer.trimStringToWidth(lines[charLine], needWidth, false, scale);
			int charPos = numToLine+text.length();
			if(text.endsWith(String.valueOf((char)10))) charPos--;
			return charPos;
		}
		else return numToLine;
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
	
	public String[] getLines(String text) {
		text += " ";
		String[] lines = text.split(String.valueOf((char)10));
		for(int i = 0; i < lines.length; i++) {
			lines[i] += (char)10;
		}
		return lines;
	}
	
	public int getCharLine(String[] lines, int num) {
		int t = 0;
		for(int i = 0; i < lines.length; i++) {
			t += lines[i].length();
			if(t > num) return i;
		}
		return Math.max(lines.length-1, 0);
	}
	
	public int getCharNumInLine(String[] lines, int num) {
		int t = 0;
		for(int i = 0; i < lines.length; i++) {
			int lineLength = lines[i].length();
			if(t + lineLength > num) return num-t;
			else t += lineLength;
		}
		return Math.max(lines[lines.length-1].length(), 0);
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
		this.setCursorPos(0, true);
		this.moveToCursor = false;
	}
	
	public static String filerAllowedCharacters(String text)
    {
        StringBuilder var1 = new StringBuilder();
        char[] var2 = text.toCharArray();
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4)
        {
            char var5 = var2[var4];

            if (isAllowedCharacter(var5))
            {
                var1.append(var5);
            }
        }

        return var1.toString();
    }
	
	public static boolean isAllowedCharacter(char c)
    {
        return c == 10 || (c != 167 && c >= 32 && c != 127);
    }
}
