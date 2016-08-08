package com.vietek.taxioperation.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.vietek.taxioperation.common.Searchable;

@Entity
@Table(name = "lst_tableprice")
public class TablePrice extends AbstractModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private int id;
	@Column(name = "km", nullable = false)
	@Searchable(placehoder = "Km giá")
	private Integer km;
	@Column(name = "time_1c", nullable = false)
	private Integer time1c;
	@Column(name = "time_2c", nullable = false)
	private Integer time2c;
	@Column(name = "price_1c", nullable = false)
	private Double price1c;
	@Column(name = "price_2c", nullable = false)
	private Double price2c;
	@Column(name = "from_date", nullable = false)
	private Timestamp fromDate;
	@Column(name = "to_date", nullable = false)
	private Timestamp toDate;
	@ManyToOne
	@JoinColumn(name = "typetableprice_id", nullable = true)
	private TypeTablePrice typeTablePrice;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getKm() {
		return km;
	}

	public void setKm(Integer km) {
		this.km = km;
	}

	public Integer getTime1c() {
		return time1c;
	}

	public void setTime1c(Integer time1c) {
		this.time1c = time1c;
	}

	public Integer getTime2c() {
		return time2c;
	}

	public void setTime2c(Integer time2c) {
		this.time2c = time2c;
	}

	public Double getPrice1c() {
		return price1c;
	}

	public void setPrice1c(Double price1c) {
		this.price1c = price1c;
	}

	public Double getPrice2c() {
		return price2c;
	}

	public void setPrice2c(Double price2c) {
		this.price2c = price2c;
	}

	public Timestamp getFromDate() {
		return fromDate;
	}

	public void setFromDate(Timestamp fromDate) {
		this.fromDate = fromDate;
	}

	public Timestamp getToDate() {
		return toDate;
	}

	public void setToDate(Timestamp toDate) {
		this.toDate = toDate;
	}

	public TypeTablePrice getTypeTablePrice() {
		return typeTablePrice;
	}

	public void setTypeTablePrice(TypeTablePrice typeTablePrice) {
		this.typeTablePrice = typeTablePrice;
	}

}