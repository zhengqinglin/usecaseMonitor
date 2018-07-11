package com.ruijie.util;

import org.apache.commons.lang3.StringUtils;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class XmlUtil {

	private static final Logger logger = LoggerFactory.getLogger(XmlUtil.class);
	/**
	 * xml转map
	 * @param xmlStr
	 * @return
	 * @throws org.jdom.JDOMException
	 * @throws IOException
	 */
    public static Map<String, String> xml2Map(String xmlStr)
            throws JDOMException, IOException {
    	if(StringUtils.isBlank(xmlStr)){
    		return null;
    	}
    	try{
    		  Map<String, String> rtnMap = new HashMap<String, String>();
    	        SAXBuilder builder = new SAXBuilder();
    	        Document doc = builder.build(new StringReader(xmlStr));
    	        // 得到根节点  
    	        Element root = doc.getRootElement();
    	        String rootName = root.getName();
    	        rtnMap.put("root.name", rootName);
    	        // 调用递归函数，得到所有最底层元素的名称和值，加入map中  
    	        convert(root, rtnMap, rootName);
    	        return rtnMap;
    	}  catch(Exception e){
    		logger.error("解析XML字符串{}失败,原因{}",xmlStr,e);
    	}
    	return null;
    }

    /**
     * 递归函数，找出最下层的节点并加入到map中，由xml2Map方法调用。
     * 
     * @param e
     *            xml节点，包括根节点
     * @param map
     *            目标map
     * @param lastname
     *            从根节点到上一级节点名称连接的字串
     */
    @SuppressWarnings("rawtypes")
	private static void convert(Element e, Map<String, String> map,
            String lastname) {
        if (e.getAttributes().size() > 0) {
            Iterator it_attr = e.getAttributes().iterator();
            while (it_attr.hasNext()) {
                Attribute attribute = (Attribute) it_attr.next();
                String attrname = attribute.getName();
                String attrvalue = e.getAttributeValue(attrname);
                map.put(lastname + "." + attrname, attrvalue);
            }
        }
        List children = e.getChildren();
        Iterator it = children.iterator();
        while (it.hasNext()) {
            Element child = (Element) it.next();
            String name = lastname + "." + child.getName();
            // 如果有子节点，则递归调用
            if (child.getChildren().size() > 0) {
                convert(child, map, name);
            } else {
                // 如果没有子节点，则把值加入map
                map.put(name, child.getText());
                // 如果该节点有属性，则把所有的属性值也加入map
                if (child.getAttributes().size() > 0) {
                    Iterator attr = child.getAttributes().iterator();
                    while (attr.hasNext()) {
                        Attribute attribute = (Attribute) attr.next();
                        String attrname = attribute.getName();
                        String attrvalue = child.getAttributeValue(attrname);
                        map.put(name + "." + attrname, attrvalue);
                    }
                }
            }
        }
    }

}

