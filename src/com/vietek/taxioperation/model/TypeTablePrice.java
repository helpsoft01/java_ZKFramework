package com.vietek.taxioperation.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.vietek.taxioperation.common.Searchable;

@Entity
@Table(name = "lst_typetableprice")
public class TypeTablePrice extends AbstractModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "TypeID")
	private int id;
	@Column(name = "CodeType")
	@Searchable(placehoder = "Mã loại")
	private String codetype;
	@Column(name = "TablePriceName")
	@Searchable(placehoder = "Tên loại")
	private String tablepricename;
	@Column(name = "PricePerExtKm")
	private Double pricePerKm;
	@Column(name = "PricePerExtTime")
	private Double pricePerExtTime;
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "typeTablePrice")
	@Searchable(placehoder = "Tìm Loại xe")
	private Set<TaxiType> typevehicle = new HashSet<TaxiType>();
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "typeTablePrice")
	@Searchable(placehoder = "Tìm loại bảng giá")
	private Set<TablePrice> tablePrices = new HashSet<TablePrice>();
    @Column(name = "Active")
    private Boolean isActive = true;
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTablepricename() {
		return tablepricename;
	}

	public void setTablepricename(String tablepricename) {
		this.tablepricename = tablepricename;
	}

	public Set<TaxiType> getTypevehicle() {
		return typevehicle;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public void setTypevehicle(HashSet<TaxiType> typevehicles) {
		this.typevehicle = typevehicles;
	}

	public String getCodetype() {
		return codetype;
	}

	public void setCodetype(String codetype) {
		this.codetype = codetype;
	}

	public Set<TablePrice> getTablePrices() {
		return tablePrices;
	}

	public void setTablePrices(HashSet<TablePrice> tablePrices) {
		this.tablePrices = tablePrices;
	}

	public Double getPricePerKm() {
		return pricePerKm;
	}

	public void setPricePerKm(Double pricePerKm) {
		this.pricePerKm = pricePerKm;
	}

	public Double getPricePerExtTime() {
		return pricePerExtTime;
	}

	public void setPricePerExtTime(Double pricePerExtTime) {
		this.pricePerExtTime = pricePerExtTime;
	}

	@Override
	public String toString() {

		return tablepricename;

	}

}