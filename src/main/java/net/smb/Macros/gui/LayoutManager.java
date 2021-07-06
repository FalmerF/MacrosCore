package net.smb.Macros.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import net.smb.Macros.MacrosSettings;
import net.smb.Macros.gui.widgets.Widget;
import net.smb.Macros.gui.widgets.WidgetButton;
import net.smb.Macros.gui.widgets.WidgetImage;
import net.smb.Macros.gui.widgets.WidgetIndicator;
import net.smb.Macros.gui.widgets.WidgetSlider;
import net.smb.Macros.gui.widgets.WidgetText;
import net.smb.Macros.util.Log;

public class LayoutManager {
	public static List<Layout> layouts = new ArrayList<Layout>();
	public static Map<String, Layout> bindedLayouts = new TreeMap<String, Layout>();
	public static File xmlFile = new File(MacrosSettings.macrosDir, ".gui.xml");
	
	public static void saveToXML() {
		try {
			if(!xmlFile.exists()) {
				MacrosSettings.macrosDir.mkdir();
				xmlFile.createNewFile();
			}
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
	        Document document = documentBuilder.newDocument();
	        
	        Element gui = document.createElement("gui");
	        
	        Element bindedGui = document.createElement("binded");
	        for(Map.Entry<String, Layout> layout : bindedLayouts.entrySet()) {
	        	Element propNode = document.createElement("bind");
	        	propNode.setAttribute("slot", layout.getKey());
	        	if(layout.getValue() != null) propNode.setTextContent(layout.getValue().getLayoutName());
				bindedGui.appendChild(propNode);
	        }
	        gui.appendChild(bindedGui);
	        
	        Element layoutsElement = document.createElement("layouts");
	        for(Layout layout : layouts) layout.saveToXML(document, layoutsElement);
	        gui.appendChild(layoutsElement);
	        
	        document.appendChild(gui);
	        
	        TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty("indent", "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.transform(new DOMSource(document), new StreamResult(xmlFile));
		} catch(Exception e) {e.printStackTrace();}
	}
	
	public static void loadLayout() {
		try {
			if(!xmlFile.exists()) {
				MacrosSettings.macrosDir.mkdir();
				xmlFile.createNewFile();
				
				Layout ingame = new Layout("ingame");
				Layout inchat = new Layout("inchat");
				Layout ininventory = new Layout("ininventory");
				
				layouts.add(ingame);
				layouts.add(inchat);
				layouts.add(ininventory);
				
				bindedLayouts.put("ingame", ingame);
				bindedLayouts.put("inchat", inchat);
				bindedLayouts.put("ininventory", ininventory);
				saveToXML();
			}
			else {
				DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
				documentBuilderFactory.setNamespaceAware(true);
		        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		        Document document = documentBuilder.parse(xmlFile);
		        
		        Node layoutsElement = document.getElementsByTagName("layouts").item(0);
		        NodeList layoutsList = getNodes(layoutsElement, "layout");
		        for(int i = 0; i < layoutsList.getLength(); i++) {
		        	Layout layout = new Layout(layoutsList.item(i).getAttributes().getNamedItem("name").getTextContent());
		        	NodeList widgets = getNodes(layoutsList.item(i), "widget");
		        	for(int n = 0; n < widgets.getLength(); n++) {
		        		Widget widget = createWidget(widgets.item(n).getAttributes().getNamedItem("name").getTextContent());
		        		NodeList properties = getNodes(widgets.item(n), "*");
		        		for(int t = 0; t < properties.getLength(); t++) {
		        			String propName = properties.item(t).getNodeName();
		        			String value = properties.item(t).getTextContent();
		        			widget.propeties.put(propName, value);
		        		}
		        		widget.updateParams();
		        		layout.widgets.add(widget);
		        	}
		        	layouts.add(layout);
		        }
		        
		        Node bindedGui = document.getElementsByTagName("binded").item(0);
		        NodeList bindsList = getNodes(bindedGui, "bind");
		        for(int i = 0; i < bindsList.getLength(); i++) {
		        	String slot = bindsList.item(i).getAttributes().getNamedItem("slot").getTextContent();
		        	Layout layout = getlayout(bindsList.item(i).getTextContent());
		        	bindedLayouts.put(slot, layout);
		        }
			}
		} catch(Exception e) {e.printStackTrace();}
	}
	
	public static NodeList getNodes(Node node,String xPath) {
		try {
			XPath xpath = XPathFactory.newInstance().newXPath();
	        return (NodeList)xpath.evaluate(xPath, node, XPathConstants.NODESET);
		} catch(Exception e) {e.printStackTrace(); return null;}
	}
	
	public static String createLayout(String name) {
		String layoutName = name;
		if(getlayout(layoutName) != null) {
			for(int i = 1; ; i++) {
				layoutName = name + "_" + i;
				if(getlayout(layoutName) == null) break;
			}
		}
		layouts.add(new Layout(layoutName));
		return layoutName;
	}
	
	public static String renameLayout(String name, String newName) {
		Layout layout = getlayout(name);
		if(layout != null) {
			String layoutName = newName;
			if(getlayout(layoutName) != null) {
				for(int i = 1; ; i++) {
					layoutName = newName + "_" + i;
					if(getlayout(layoutName) == null) break;
				}
			}
			layout.renameLayout(layoutName);
			return layoutName;
		}
		return "";
	}
	
	public static Layout getlayout(String name) {
		for(Layout layout : layouts) if(layout.getLayoutName().equals(name)) return layout;
		return null;
	}
	
	public static Widget createWidget(String name) {
		try {
			Widget widget = new Widget(name);
			if(name.equals("Button")) {
				widget = new WidgetButton(name);
			}
			else if(name.equals("Text")) {
				widget = new WidgetText(name);
			}
			else if(name.equals("Slider")) {
				widget = new WidgetSlider(name);
			}
			else if(name.equals("Indicator")) {
				widget = new WidgetIndicator(name);
			}
			else if(name.equals("Image")) {
				widget = new WidgetImage(name);
			}
			return widget;
		} catch(Exception e) {e.printStackTrace();}
		return null;
	}
}
