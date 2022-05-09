package cz.vutbr.fit.dip.services;

import org.eclipse.uml2.uml.Class;

public class OutputNode {
	
	private Class node;
	private Integer priority;
	private String tag;
	
	
	OutputNode(Class node, Integer priority, String tag) {
		this.node = node;
		this.priority = priority;
		this.tag = tag;
	}
	
	public Class getNode() {
		return node;
	}
	public void setNode(Class node) {
		this.node = node;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	

}
