package com.vietek.taxioperation.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.common.Searchable;
import com.vietek.taxioperation.common.Validation;

@Entity
@Table(name = "lst_vehicletype", schema = "txm_tracking")
@Where(clause = "Active = 1")
public class TaxiType extends AbstractModel implements Serializable {

	/**
	 * Danh muc loai xe taxi
	 * 
	 * @author VuD
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@Column(name = "TypeID")
	private int id;
	@Searchable(placehoder = "Mã Loại Xe")
	@Column(name = "TypeCode")
	@Validation(title = CommonDefine.VehicleTypeDefine.CODE_TYPE, nullable = false, alowrepeat = false)
	private String value;
	@Searchable(placehoder = "Tên Loại Xe")
	@Column(name = "TypeName")
	@Validation(title = CommonDefine.VehicleTypeDefine.NAME_TYPE, nullable = false)
	private String name;
	@ManyToOne
	@JoinColumn(name = "SeatTypeID")
	private Seat seats;
	@ManyToOne
	@JoinColumn(name = "Color")
	private Color color;

	@ManyToOne
	@JoinColumn(name = "PriceListID")
	private PriceList priceList;

	@ManyToOne
	@JoinColumn(name = "VehicleMarkID", referencedColumnName = "CommonID", nullable = false)
	@Searchable(placehoder = "Tìm hiệu xe")
	@Validation(title = CommonDefine.VehicleTypeDefine.TRADEMARK, nullable = false)
	private TradeMark tradeMark;

	@ManyToOne
	@JoinColumn(name = "VehicleFirmID", referencedColumnName = "CommonID", nullable = false)
	@Validation(title = CommonDefine.VehicleTypeDefine.CARSUPPLIER, nullable = false)
	@Searchable(placehoder = "Tìm hãng xe")
	private CarSupplier carSupplier;

	@ManyToOne
	@JoinTable(name = "mapping_vehicletype_pricetype", joinColumns = {
			@JoinColumn(name = "vehicletype_id") }, inverseJoinColumns = { @JoinColumn(name = "pricetype_id") })
	private TypeTablePrice typeTablePrice;

	@Column(name = "Active")
	private Boolean isActive = true;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Seat getSeats() {
		return seats;
	}

	public void setSeats(Seat seats) {
		this.seats = seats;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public PriceList getPriceList() {
		return priceList;
	}

	public void setPriceList(PriceList priceList) {
		this.priceList = priceList;
	}

	public TradeMark getTradeMark() {
		return tradeMark;
	}

	public void setTradeMark(TradeMark tradeMark) {
		this.tradeMark = tradeMark;
	}

	public CarSupplier getCarSupplier() {
		return carSupplier;
	}

	public void setCarSupplier(CarSupplier carSupplier) {
		this.carSupplier = carSupplier;
	}

	public TypeTablePrice getTypeTablePrice() {
		return typeTablePrice;
	}

	public void setTypeTablePrice(TypeTablePrice typeTablePrice) {
		this.typeTablePrice = typeTablePrice;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public String toString() {
		return name;
	}

}
