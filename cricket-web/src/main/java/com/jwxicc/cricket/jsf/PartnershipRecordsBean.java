package com.jwxicc.cricket.jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.jwxicc.cricket.entity.Partnership;
import com.jwxicc.cricket.records.PartnershipRecordsManager;
import com.jwxicc.cricket.util.JwxiccUtils;

@ManagedBean(name = "pshipRecordsBean")
@ViewScoped
public class PartnershipRecordsBean implements Serializable {

	@EJB(name = "pshipRecordsManager")
	PartnershipRecordsManager recordsManager;

	private int jwTeamId = JwxiccUtils.JWXICC_TEAM_ID;

	private List<Partnership> allWickets;
	private List<PartnershipRecordsHolder> allWicketPartnerships;

	public int getJwTeamId() {
		return jwTeamId;
	}

	public void setJwTeamId(int jwTeamId) {
		this.jwTeamId = jwTeamId;
	}

	public List<Partnership> getAllWickets() {
		if (allWickets == null) {
			this.allWickets = recordsManager.getTopPartnerships();
		}
		return allWickets;
	}

	public void setAllWickets(List<Partnership> allWickets) {
		this.allWickets = allWickets;
	}

	public List<PartnershipRecordsHolder> getAllWicketPartnerships() {
		if (allWicketPartnerships == null) {
			allWicketPartnerships = new ArrayList<PartnershipRecordsHolder>(10);
			List<List<Partnership>> topPartnershipsByWicket = this.recordsManager
					.getAllTopPartnershipsByWicket();
			for (List<Partnership> pshipList : topPartnershipsByWicket) {
				if (pshipList != null) {
					allWicketPartnerships.add(new PartnershipRecordsHolder(pshipList));
				} else {
					allWicketPartnerships.add(null);
				}
			}
		}
		return allWicketPartnerships;
	}

	public void setAllWicketPartnerships(List<PartnershipRecordsHolder> allWicketPartnerships) {
		this.allWicketPartnerships = allWicketPartnerships;
	}

	public class PartnershipRecordsHolder {
		private List<Partnership> partnershipList;

		public PartnershipRecordsHolder(List<Partnership> partnershipList) {
			this.partnershipList = partnershipList;
		}

		public List<Partnership> getPartnershipList() {
			return partnershipList;
		}

		public void setPartnershipList(List<Partnership> partnershipList) {
			this.partnershipList = partnershipList;
		}

	}

}