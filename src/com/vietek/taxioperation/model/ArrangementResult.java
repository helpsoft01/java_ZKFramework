package com.vietek.taxioperation.model;

/**
 * 
 * @author VuD
 * 
 *
 */
public class ArrangementResult {
	private ArrangementTaxi arrangamentTaxi;
	private int sequence;
	private int status;

	public ArrangementTaxi getArrangamentTaxi() {
		return arrangamentTaxi;
	}

	public void setArrangamentTaxi(ArrangementTaxi arrangamentTaxi) {
		this.arrangamentTaxi = arrangamentTaxi;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
