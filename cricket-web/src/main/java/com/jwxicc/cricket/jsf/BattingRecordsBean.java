package com.jwxicc.cricket.jsf;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.jwxicc.cricket.entity.Batting;
import com.jwxicc.cricket.records.BattingRecord;
import com.jwxicc.cricket.records.BattingRecordsManager;

@ManagedBean(name = "batRecordsBean")
@ViewScoped
public class BattingRecordsBean implements Serializable {

	@EJB(name = "battingRecordsManager")
	BattingRecordsManager records;

	private List<Batting> runsInInnings;
	private List<BattingRecord> aggregateRuns;
	private List<BattingRecord> averageRuns;
	private boolean willowfestOnly;
	
	public void toggleWillowfestOnly() {
		System.out.println("toggle willowfest only from " + willowfestOnly);
		runsInInnings = null;
		aggregateRuns = null;
		averageRuns = null;
		this.willowfestOnly = !this.willowfestOnly;
	}

	public List<Batting> getRunsInInnings() {
		if (runsInInnings == null) {
			runsInInnings = records.getInningsBest(willowfestOnly);
		}
		return runsInInnings;
	}

	public void setRunsInInnings(List<Batting> runsInInnings) {
		this.runsInInnings = runsInInnings;
	}

	public List<BattingRecord> getAggregateRuns() {
		if (aggregateRuns == null) {
			aggregateRuns = records.getByAggregate(willowfestOnly);
		}
		return aggregateRuns;
	}

	public void setAggregateRuns(List<BattingRecord> aggregateRuns) {
		this.aggregateRuns = aggregateRuns;
	}

	public List<BattingRecord> getAverageRuns() {
		if (averageRuns == null) {
			averageRuns = records.getByAverage(willowfestOnly);
		}
		return averageRuns;
	}

	public void setAverageRuns(List<BattingRecord> averageRuns) {
		this.averageRuns = averageRuns;
	}

	public boolean isWillowfestOnly() {
		return willowfestOnly;
	}

	public void setWillowfestOnly(boolean willowfestOnly) {
		this.willowfestOnly = willowfestOnly;
	}
}
