package com.okiimport.app.model;

import java.io.Serializable;

import javax.persistence.*;

import com.okiimport.app.resource.model.AbstractEntity;

import java.sql.Timestamp;


/**
 * The persistent class for the history_logins database table.
 * 
 */
@Entity
@Table(name="history_logins")
@NamedQuery(name="HistoryLogin.findAll", query="SELECT h FROM HistoryLogin h")
public class HistoryLogin extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="history_logins_id_seq")
	@SequenceGenerator(name="history_logins_id_seq", sequenceName="history_logins_id_seq", initialValue=1, allocationSize=1)
	private Integer id;

	@Column(name="date_login")
	private Timestamp dateLogin;

	@Column(name="date_logout")
	private Timestamp dateLogout;
	
	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="username", referencedColumnName="username", nullable=false)
	private Usuario usuario;

	public HistoryLogin() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Timestamp getDateLogin() {
		return this.dateLogin;
	}

	public void setDateLogin(Timestamp dateLogin) {
		this.dateLogin = dateLogin;
	}

	public Timestamp getDateLogout() {
		return this.dateLogout;
	}

	public void setDateLogout(Timestamp dateLogout) {
		this.dateLogout = dateLogout;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}