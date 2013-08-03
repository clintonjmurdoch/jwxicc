package com.jwxicc.cricket.jsf;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.jwxicc.cricket.records.FieldingRecord;
import com.jwxicc.cricket.records.FieldingRecordsManager;

@ManagedBean(name = "fieldingRecords")
@ViewScoped
public class FieldingRecordsBean implements Serializable {

	@EJB
	FieldingRecordsManager fieldingRecords;

	private List<FieldingRecord> catchesByOutfielder;
	private List<FieldingRecord> mostDismissals;

	public List<FieldingRecord> getCatchesByOutfielder() {
		if (catchesByOutfielder == null) {
			catchesByOutfielder = fieldingRecords.getMostCatchesByOutfielder();
		}
		return catchesByOutfielder;
	}

	public void setCatchesByOutfielder(List<FieldingRecord> catchesByOutfielder) {
		this.catchesByOutfielder = catchesByOutfielder;
	}

	public List<FieldingRecord> getMostDismissals() {
		if (mostDismissals == null) {
			mostDismissals = fieldingRecords.getMostDismissals();
		}
		return mostDismissals;
	}

	public void setMostDismissals(List<FieldingRecord> mostDismissals) {
		this.mostDismissals = mostDismissals;
	}

}
