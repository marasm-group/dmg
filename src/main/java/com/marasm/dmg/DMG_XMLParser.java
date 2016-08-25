package com.marasm.dmg;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by vhq473 on 24.08.2016.
 */
public class DMG_XMLParser
{
    ArrayList<DMGObject> objects = new ArrayList<>();
    static String text_tag = "#text";
    public DMG_XMLParser(String data)
    {
        ParseXML(data);
    }

    private void ParseXML(String data)
    {
        InputStream in = IOUtils.toInputStream(data, Utils.getEncoding());
        ParseXML(in);
    }
    private void ParseXML(InputStream in)
    {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(in);
            ParseXML(doc);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(this,"Failed to parse XML");
            System.exit(-1);
        }
    }

    private void ParseXML(Document doc)
    {
        DMGObject obj = new DMGObject();
        obj.name = "DMG_XMLObject";
        visit(doc,0,obj);
        objects.add(obj);
    }

    public void visit(Node node, int level, DMGObject currentObject)
    {
        NodeList list = node.getChildNodes();
        ArrayList<Field> fields = new ArrayList<>();
        NamedNodeMap attributes = node.getAttributes();
        if(attributes != null)
        {
            for (int i = 0; i < attributes.getLength(); i++)
            {
                Node attribute = attributes.item(i);
                processNode(attribute,list,level,fields);
            }
        }
        for (int i = 0; i < list.getLength(); i++)
        {
            Node childNode = list.item(i);
            processNode(childNode,list,level,fields);
        }
        if(fields.size() != 0 && currentObject != null)
        {
            currentObject.fields = Utils.processDuplicates(fields);
        }
    }

    private void processNode(Node childNode, NodeList list, int level, Collection<Field> fields)
    {
        NodeList childNodes = childNode.getChildNodes();
        Field f = process(childNode, level + 1);
        if (!f.name.equals(text_tag))
        {
            fields.add(f);
        }
        visit(childNode, level + 1,f.object);
    }

    private Field process(Node node, int level)
    {
        Field f = new Field(node.getNodeName(),type(node));
        if(f.type == Type.Object)
        {
            f.object = new DMGObject();
            f.object.name = node.getNodeName();
        }
        return f;
    }

    private Type type(Node node)
    {
        if(node.getChildNodes() != null)
        {
            if (node.getChildNodes().getLength() > 0)
            {
                NodeList childNodes = node.getChildNodes();
                if(childNodes.getLength() == 1)
                {
                    Node firstNode = childNodes.item(0);
                    if(!firstNode.hasAttributes() && !firstNode.hasChildNodes())
                    {
                        if(firstNode.getNodeName().equals(text_tag))
                        {
                            return Type.fromString(firstNode.getNodeValue());
                        }
                    }
                }
                return Type.Object;
            }
        }
        if(node.getAttributes() != null)
        {
            if(node.getAttributes().getLength() > 0) {
                return Type.Object;
            }
        }
        return Type.fromString(node.getNodeValue());
    }
}
