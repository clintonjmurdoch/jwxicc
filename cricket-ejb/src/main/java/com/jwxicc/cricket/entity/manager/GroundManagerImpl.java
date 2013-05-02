package com.jwxicc.cricket.entity.manager;

import javax.ejb.Local;
import javax.ejb.Stateless;

import com.jwxicc.cricket.entity.Ground;
import com.jwxicc.cricket.interfaces.GroundManager;

@Stateless(name = "groundManager")
@Local(GroundManager.class)
public class GroundManagerImpl extends BaseManager<Ground> implements
		GroundManager {

	@Override
	public boolean validateRequiredFields(Ground obj) {
		// TODO Auto-generated method stub
		return false;
	}

}
