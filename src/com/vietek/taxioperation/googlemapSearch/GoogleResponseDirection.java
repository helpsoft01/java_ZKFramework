package com.vietek.taxioperation.googlemapSearch;

import org.codehaus.jackson.annotate.JsonIgnore;

public class GoogleResponseDirection {

	private String status;
	@JsonIgnore
	private String[] destination_addresses;
	@JsonIgnore
	private String[] origin_addresses;
	private Item[] rows;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String[] getDestination_addresses() {
		return destination_addresses;
	}

	public void setDestination_addresses(String[] destination_addresses) {
		this.destination_addresses = destination_addresses;
	}

	public String[] getOrigin_addresses() {
		return origin_addresses;
	}

	public void setOrigin_addresses(String[] origin_addresses) {
		this.origin_addresses = origin_addresses;
	}

	public Item[] getRows() {
		return rows;
	}

	public void setRows(Item[] rows) {
		this.rows = rows;
	}

	class Item {
		public Element[] elements;

		public Element[] getElements() {
			return elements;
		}

		public void setElements(Element[] elements) {
			this.elements = elements;
		}
	}

	class Element {
		Duration duration;
		Distance distance;
		String status;

		public Duration getDuration() {
			return duration;
		}
		public void setDuration(Duration duration) {
			this.duration = duration;
		}
		public Distance getDistance() {
			return distance;
		}
		public void setDistance(Distance distance) {
			this.distance = distance;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		
	}

	class Distance {
		private String text;
		private String value;

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}

	class Duration {
		private String text;
		private String value;

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}
}
