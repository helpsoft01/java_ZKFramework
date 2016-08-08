package com.vietek.taxioperation.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.vietek.taxioperation.common.ColumnInfor;
import com.vietek.taxioperation.common.Searchable;

@Entity
@Table(name = "cat_TelephoneExtension")
public class TelephoneExtensionTms extends AbstractModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7380618244075571187L;

	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	@ColumnInfor(name = "id", label = "id")
	private int id;

	@ColumnInfor(name = "extension", label = "Extension phone")
	@Searchable(placehoder = "Tìm số điện thoại")
	private String extension;

	@ManyToOne(optional = false)
	@JoinColumn(name = "telephoneTable_ID", nullable = false, updatable = true)
	@ColumnInfor(name = "telephoneTable", label = "Bàn điện thoại")
	@Searchable(placehoder = "Tìm bàn điện thoại")
	private TelephoneTableTms telephoneTable;

	@ManyToOne(optional = false)
	@JoinColumn(name = "channel_ID", nullable = false, updatable = true)
	@ColumnInfor(name = "channel", label = "Kênh")
	@Searchable(placehoder = "Tìm kênh")
	private ChannelTms channel;

	public ChannelTms getChannel() {
		return channel;
	}

	public void setChannel(ChannelTms channel) {
		this.channel = channel;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public TelephoneTableTms getTelephoneTable() {
		return telephoneTable;
	}

	public void setTelephoneTable(TelephoneTableTms telephoneTable) {
		this.telephoneTable = telephoneTable;
	}
}