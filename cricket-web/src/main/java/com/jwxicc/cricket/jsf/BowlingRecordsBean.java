package com.jwxicc.cricket.jsf;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.jwxicc.cricket.entity.Batting;
import com.jwxicc.cricket.entity.Bowling;
import com.jwxicc.cricket.records.BattingRecordsManager;
import com.jwxicc.cricket.records.BowlingRecord;
import com.jwxicc.cricket.records.BowlingRecordsManager;

@ManagedBean(name = "bowlRecordsBean")
@ViewScoped
public class BowlingRecordsBean implements Serializable {

	@EJB(name = "bowlingRecordsManager")
	BowlingRecordsManager records;

	private List<Bowling> bowlingFigures;
	private List<BowlingRecord> aggregateWickets;
	private List<BowlingRecord> bowlingAverage;
	private boolean willowfestOnly;
	
	public void toggleWillowfestOnly() {
		System.out.println("toggle willowfest only from " + willowfestOnly);
		bowlingFigures = null;
		aggregateWickets = null;
		bowlingAverage = null;
		this.willowfestOnly = !this.willowfestOnly;
	}

	public List<Bowling> getBowlingFigures() {
		if (bowlingFigures == null) {
			bowlingFigures = records.getInningsBest(willowfestOnly);
		}
		return bowlingFigures;
	}

	public void setBowlingFigures(List<Bowling> bowlingFigures) {
		this.bowlingFigures = bowlingFigures;
	}

	public List<BowlingRecord> getAggregateWickets() {
		if (aggregateWickets == null) {
			aggregateWickets = records.getByAggregate(willowfestOnly);
		}
		return aggregateWickets;
	}

	public void setAggregateWickets(List<BowlingRecord> aggregateWickets) {
		this.aggregateWickets = aggregateWickets;
	}

	public List<BowlingRecord> getBowlingAverage() {
		if (bowlingAverage == null) {
			bowlingAverage = records.getByAverage(willowfestOnly);
		}
		return bowlingAverage;
	}

	public void setBowlingAverage(List<BowlingRecord> bowlingAverage) {
		this.bowlingAverage = bowlingAverage;
	}

	public boolean isWillowfestOnly() {
		return willowfestOnly;
	}

	public void setWillowfestOnly(boolean willowfestOnly) {
		this.willowfestOnly = willowfestOnly;
	}
}
