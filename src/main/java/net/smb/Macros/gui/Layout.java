package net.smb.Macros.gui;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import net.smb.Macros.gui.widgets.Widget;

public class Layout {
	public List<Widget> widgets;
	private String layoutName;
	
	public Layout(String name) {
		widgets = new ArrayList<Widget>();
		layoutName = name; 
	}
	
	public void saveToXML(Document document, Element gui) {
		Element layoutNode = document.createElement("layout");
		layoutNode.setAttribute("name", layoutName);
		for(Widget widget : widgets) widget.saveToXML(document, layoutNode);
		gui.appendChild(document.createComment("Config for layout " + layoutName));
		gui.appendChild(layoutNode);
	}
	
	public String getLayoutName() {
		return layoutName;
	}
	
	public void renameLayout(String name) {
		layoutName = name;
	}
}
