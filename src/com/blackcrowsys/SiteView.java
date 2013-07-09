package com.blackcrowsys;

public class SiteView {
	private Integer id;
	private String sitename;

	public SiteView(Integer id, String sitename) {
		this.id = id;
		this.sitename = sitename;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSitename() {
		return sitename;
	}

	public void setSitename(String sitename) {
		this.sitename = sitename;
	}

	public String toString() {
		return this.getId() + " - " + this.getSitename();
	}
}
