package com.jade.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.collect.Lists;

@XmlRootElement
public class RouteGroup {
	@XmlAttribute
	public String type;
	
	@XmlAttribute
	public String pkge;
	
	@XmlElement(name = "route")
	public List<RouteItem> routeItems = Lists.newArrayList();

	public void addRoutegroup(RouteItem routeItem) {
		this.routeItems.add(routeItem);
	}
}
