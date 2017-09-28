package com.jade.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RouteItem {
	@XmlAttribute
	public String controllerkey;

	@XmlAttribute
	public String className;
}
