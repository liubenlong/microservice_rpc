package com.freedom.rpc.thrift.common.annotation;

public class Model {
	private String group;
	private String service;
	private String edition;

	public Model(String g, String s, String e) {
		group = g;
		service = s;
		edition = e;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getEdition() {
		return edition;
	}

	public void setEdition(String edition) {
		this.edition = edition;
	}
	
	
}
