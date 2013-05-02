package com.jwxicc.cricket.interfaces;

import java.util.List;

import javax.ejb.Local;

import com.jwxicc.cricket.entity.Howout;
import com.jwxicc.cricket.entity.WicketDetail;

@Local
public interface DismissalsManager extends Manager {

	public List<Howout> getAllDismissalTypes();
	public Howout findHowout(int id);
	public void persistWicketDetail(WicketDetail wicket);
}
