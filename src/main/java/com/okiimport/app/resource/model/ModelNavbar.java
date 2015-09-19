package com.okiimport.app.resource.model;

import java.util.List;

public interface ModelNavbar {
	public void setParent(ModelNavbar parent);
	public int getIdNode();
	public String getLabel();
	public String getIcon();
	public String getUriLocation();
	public List<ModelNavbar> getChilds();
	public ModelNavbar getParent();
	public Boolean isRootParent();
	public List<ModelNavbar> getRootTree();
	public <T> T[] childToArray(Class<?> clazz);
	public <T> T[] childToArray(Class<?> clazz, int element);
}
