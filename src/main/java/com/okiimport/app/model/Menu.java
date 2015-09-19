package com.okiimport.app.model;

import java.io.Serializable;
import java.lang.reflect.Array;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.okiimport.app.resource.model.AbstractEntity;
import com.okiimport.app.resource.model.ModelNavbar;

import java.util.ArrayList;
import java.util.List;


/**
 * The persistent class for the menu database table.
 * 
 */
@Entity
@Table(name="menu")
@NamedQuery(name="Menu.findAll", query="SELECT m FROM Menu m")
@JsonIgnoreProperties({"hijos"})
public class Menu extends AbstractEntity implements  Serializable, ModelNavbar{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="menu_id_seq")
	@SequenceGenerator(name="menu_id_seq", sequenceName="menu_id_seq", initialValue=1, allocationSize=1)
	@Column(name="id_menu", unique=true, nullable=false)
	private Integer idMenu;

	@Column(length=255)
	private String actividad;

	@Column(length=255)
	private String icono;

	@Column(length=255)
	private String nombre;
	
	@Column
	private Integer tipo;

	@Column(length=255)
	private String ruta;

	//bi-directional many-to-one association to Menu
	@ManyToOne
	@JoinColumn(name="id_padre")
	private Menu padre;

	//bi-directional one-to-many association to Menu
	@OneToMany(mappedBy="padre")
	private List<Menu> hijos;

	public Menu() {
	}

	public Integer getIdMenu() {
		return this.idMenu;
	}

	public void setIdMenu(Integer idMenu) {
		this.idMenu = idMenu;
	}

	public String getActividad() {
		return this.actividad;
	}

	public void setActividad(String actividad) {
		this.actividad = actividad;
	}

	public String getIcono() {
		return this.icono;
	}

	public void setIcono(String icono) {
		this.icono = icono;
	}

	public String getNombre() {
		return this.nombre;
	}

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getRuta() {
		return this.ruta;
	}

	public void setRuta(String ruta) {
		this.ruta = ruta;
	}

	public Menu getPadre() {
		return this.padre;
	}

	public void setPadre(Menu menu) {
		this.padre = menu;
	}

	public List<Menu> getHijos() {
		return this.hijos;
	}

	public void setHijos(List<Menu> hijos) {
		this.hijos = hijos;
	}

	public Menu addHijo(Menu menu) {
		getHijos().add(menu);
		menu.setPadre(this);

		return menu;
	}

	public Menu removeHijo(Menu menu) {
		getHijos().remove(menu);
		menu.setPadre(null);

		return menu;
	}

	/**METODOS OVERRIDE*/
	//1. ModelNavbar
	public int getIdNode(){
		return this.getIdMenu();
	}
	
	public String getLabel() {
		return this.getNombre();
	}

	public String getIcon() {
		return this.getIcono();
	}

	public String getUriLocation() {
		return this.getRuta();
	}
	
	public List<ModelNavbar> getRootTree() {
		List<ModelNavbar> rootTree = new ArrayList<ModelNavbar>();
		calculateRootTree(rootTree, this);
		return rootTree;
	}

	public List<ModelNavbar> getChilds() {
		List<ModelNavbar> temp = new ArrayList<ModelNavbar>();
		if(this.hijos!=null)
			for(Menu menu : this.getHijos())
				temp.add(menu);
		return temp;
	}
	
	/**METODOS PROPIOS DE LA CLASE*/
	public Menu getParentRoot(){
		Menu rootParent=(Menu) padre;
		while(rootParent!=null)
			rootParent = (Menu) rootParent.padre;
		return rootParent;
	}
	
	//Hijos
	public Menu getParent(Integer id){
		Menu parent=(Menu) padre;
		if(parent!=null)
			while(parent.getIdMenu()!=id && parent.getPadre()!=null)
				parent = (Menu) parent.padre;
		else
			parent = this;
		return parent;
	}
	
	protected void calculateRootTree(List<ModelNavbar> root, ModelNavbar nodo){
		if(nodo.isRootParent())
			root.add(nodo);
		else {
			calculateRootTree(root, nodo.getParent());
			root.add(nodo);
		}
	}

	public void setParent(ModelNavbar parent) {
		this.setPadre((Menu) parent);
	}

	public ModelNavbar getParent() {
		return this.getPadre();
	}

	public Boolean isRootParent() {
		return (this.getPadre()==null);
	}

	public <T> T[] childToArray(Class<?> clazz) {
		return childToArray(clazz, this.hijos.size());
	}

	@SuppressWarnings("unchecked")
	public <T> T[] childToArray(Class<?> clazz, int element) {
		return getChilds().toArray( (T[]) Array.newInstance(clazz, element));
	}

}