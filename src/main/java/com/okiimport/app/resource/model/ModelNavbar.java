package com.okiimport.app.resource.model;

import java.util.List;

public interface ModelNavbar {
	int getIdNode();
	String getLabel();
	String getIcon();
	String getUriLocation();
	List<ModelNavbar> getRootTree();
	ModelNavbar getParent();
	void setParent(ModelNavbar parent);
	Boolean isRootParent();
	List<ModelNavbar> getChilds();
	<T> T[] childToArray(Class<?> clazz);
	<T> T[] childToArray(Class<?> clazz, int element);
}
