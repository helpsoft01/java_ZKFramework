package com.vietek.taxioperation.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.vietek.taxioperation.common.ApplyEditor;
import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.common.FixedCombobox;
import com.vietek.taxioperation.common.Validation;

/**
 *
 * @author VuD
 */
@Entity
@Table(name = "lst_pavilionarea", schema = "txm_tracking")
@Where(clause = "Active = 1")
public class MarketingPlace extends AbstractModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "PavilionID")
	private int id;
	@Validation(title = CommonDefine.MarketingPlace.CODE, nullable = false, alowrepeat = false)
	private String PavilionCode;
	@Validation(title = CommonDefine.MarketingPlace.ADDRESS, nullable = false)
	private String Address;
	@Validation(title = CommonDefine.MarketingPlace.NAME, nullable = false)
	private String PavilionName;
	private String PavilionCard; // The nhan vien tiep thi
	private Integer Quota = 0; // Giới hạn xe tại điểm
	private Double Lati = 0.0;
	private Double Longi = 0.0;
	private Integer Radius = 0; // bán kính điểm tiếp thị
	private String Decription; // Mô tả
	private Double BorderLatiMin;
	private Double BorderLatiMax;
	private Double BorderLongiMin;
	private Double BorderLongiMax;
	@ApplyEditor(classNameEditor = "IntCheckEditor")
	private Integer Active = 1;
	@Validation(title = CommonDefine.MarketingPlace.AGENT, nullable = false)
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "AgentID", referencedColumnName = "AgentID", nullable = false, foreignKey = @ForeignKey(name = "FK_lst_pavilionarea_lst_agent_AgentID") )
	private Agent agent;
	@Validation(title = CommonDefine.MarketingPlace.TYPEPOINT, nullable = false)
	@FixedCombobox(label = { "Điểm tiếp thị", "Điểm sắp tài" }, value = { 0, 1 })
	private Integer Paviliontype = 0;

	public String getPaviliontypename() {
		String result = "";
		if (Paviliontype.equals(0)) {
			result = "Điểm tiếp thị";
		} else {
			result = "Điểm sắp tài";
		}
		return result;
	}

	public Integer getPaviliontype() {
		return Paviliontype;
	}

	public void setPaviliontype(Integer paviliontype) {
		Paviliontype = paviliontype;
	}

	public Agent getAgent() {
		return agent;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPavilionCode() {
		return PavilionCode;
	}

	public void setPavilionCode(String pavilionCode) {
		PavilionCode = pavilionCode;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getPavilionName() {
		return PavilionName;
	}

	public void setPavilionName(String pavilionName) {
		PavilionName = pavilionName;
	}

	public String getPavilionCard() {
		return PavilionCard;
	}

	public void setPavilionCard(String pavilionCard) {
		PavilionCard = pavilionCard;
	}

	public Integer getQuota() {
		return Quota;
	}

	public void setQuota(Integer quota) {
		Quota = quota;
	}

	public Double getLati() {
		return Lati;
	}

	public void setLati(Double lati) {
		Lati = lati;
	}

	public Double getLongi() {
		return Longi;
	}

	public void setLongi(Double longi) {
		Longi = longi;
	}

	public Integer getRadius() {
		return Radius;
	}

	public void setRadius(Integer radius) {
		Radius = radius;
	}

	public String getDecription() {
		return Decription;
	}

	public void setDecription(String decription) {
		Decription = decription;
	}

	public Double getBorderLatiMin() {
		return BorderLatiMin;
	}

	public void setBorderLatiMin(Double borderLatiMin) {
		BorderLatiMin = borderLatiMin;
	}

	public Double getBorderLatiMax() {
		return BorderLatiMax;
	}

	public void setBorderLatiMax(Double borderLatiMax) {
		BorderLatiMax = borderLatiMax;
	}

	public Double getBorderLongiMin() {
		return BorderLongiMin;
	}

	public void setBorderLongiMin(Double borderLongiMin) {
		BorderLongiMin = borderLongiMin;
	}

	public Double getBorderLongiMax() {
		return BorderLongiMax;
	}

	public void setBorderLongiMax(Double borderLongiMax) {
		BorderLongiMax = borderLongiMax;
	}

	public Integer getActive() {
		return Active;
	}

	public void setActive(Integer active) {
		Active = active;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}
}