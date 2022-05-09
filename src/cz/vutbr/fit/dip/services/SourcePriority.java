package cz.vutbr.fit.dip.services;

import org.eclipse.uml2.uml.Class;

public class SourcePriority {
	private Class source;
	private Integer priority;
	
	public Class getSource() {
		return source;
	}
	public void setSource(Class source) {
		this.source = source;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	
	public SourcePriority(Class source, Integer priority) {
		this.source = source;
		this.priority = priority;
		if (priority == 0) {
			this.priority = Integer.MAX_VALUE;
		}
	}
}
