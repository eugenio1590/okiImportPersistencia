package com.okiimport.app.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.okiimport.app.model.enumerados.EEstatusFormaPago;
import com.okiimport.app.resource.model.AbstractEntity;

/**
 * The persistent class for the forma_pago database table.
 * 
 */
@Entity
@Table(name="forma_pago")
@NamedQuery(name="FormaPago.findAll", query="SELECT f FROM FormaPago f")
@JsonIgnoreProperties({"pago"})
public class FormaPago extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="forma_pago_id_seq")
	@SequenceGenerator(name="forma_pago_id_seq", sequenceName="forma_pago_id_seq", initialValue=1, allocationSize=1)
	@Column(name="id_forma_pago")
	private Integer idFormaPago;
	
	private String nombre;
	
	private String url;
	
	@Enumerated(EnumType.STRING)
	private EEstatusFormaPago estatus;
	
	//bi-directional one-to-many association to Pago
	@OneToMany(mappedBy="formaPago", fetch=FetchType.LAZY)
	private List<Pago> pago;
	
	public FormaPago() {
	}

	public Integer getIdFormaPago() {
		return idFormaPago;
	}

	public void setIdFormaPago(Integer idFormaPago) {
		this.idFormaPago = idFormaPago;
	}

	public String getNombre() {
		return nombre;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public EEstatusFormaPago getEstatus() {
		return estatus;
	}

	public void setEstatus(EEstatusFormaPago estatus) {
		this.estatus = estatus;
	}

	public List<Pago> getPago() {
		return pago;
	}

	public void setPago(List<Pago> pago) {
		this.pago = pago;
	}
	
	public Pago addPago(Pago pago){
		getPago().add(pago);
		pago.setFormaPago(this);
		
		return pago;
	}
	
	public Pago removePagoCompra(Pago pago){
		getPago().remove(pago);
		pago.setFormaPago(null);
		
		return pago;
	}
}
