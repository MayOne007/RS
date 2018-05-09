package com.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "t_dict")
public class Dict implements Serializable {
	
	private static final long serialVersionUID = 1951057563788686787L;
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "N_ID", unique = true, nullable = false)
	private Integer id;	
	@Column(name = "C_KEY", nullable = false, length = 50)
	private String key;
	@Column(name = "C_VALUE", nullable = false, length = 50)
	private String value;
	@Column(name = "C_PARENTKEY", length = 50)
	private String parentKey;
	@Column(name = "N_SORT")
	private Integer sort;
	
	@JsonIgnore
	@ManyToOne(targetEntity = Dict.class,cascade=CascadeType.REFRESH,fetch = FetchType.LAZY)  
	@JoinColumn(name="C_PARENTKEY",referencedColumnName="C_KEY",insertable=false,updatable=false)
	private Dict parentDict; 
	
	@JsonIgnore
	@OneToMany(targetEntity = Dict.class, cascade=CascadeType.REFRESH, mappedBy = "parentDict")
	private List<Dict> childDicts;
	
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public String getParentKey() {
		return this.parentKey;
	}

	public void setParentKey(String parentKey) {
		this.parentKey = parentKey;
	}

	public Integer getSort() {
		return this.sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	public Dict getParentDict() {
		return parentDict;
	}

	public void setParentDict(Dict parentDict) {
		this.parentDict = parentDict;
	}

	public List<Dict> getChildDicts() {
		return childDicts;
	}

	public void setChildDicts(List<Dict> childDicts) {
		this.childDicts = childDicts;
	}
	
}