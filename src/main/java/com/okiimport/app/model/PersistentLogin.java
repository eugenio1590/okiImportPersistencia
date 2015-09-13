package com.okiimport.app.model;

import java.io.Serializable;

import javax.persistence.*;

import com.okiimport.app.resource.model.AbstractEntity;

import java.sql.Timestamp;


/**
 * The persistent class for the persistent_logins database table.
 * 
 */
@Entity
@Table(name="persistent_logins")
@NamedQuery(name="PersistentLogin.findAll", query="SELECT p FROM PersistentLogin p")
public class PersistentLogin extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false, length=64)
	private String series;

	@Column(name="last_used", nullable=false)
	private Timestamp lastUsed;

	@Column(nullable=false, length=64)
	private String token;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="username", referencedColumnName="username", nullable=false)
	private Usuario usuario;

	public PersistentLogin() {
	}

	public String getSeries() {
		return this.series;
	}

	public void setSeries(String series) {
		this.series = series;
	}

	public Timestamp getLastUsed() {
		return this.lastUsed;
	}

	public void setLastUsed(Timestamp lastUsed) {
		this.lastUsed = lastUsed;
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}