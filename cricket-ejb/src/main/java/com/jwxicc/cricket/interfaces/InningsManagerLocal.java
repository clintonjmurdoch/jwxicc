package com.jwxicc.cricket.interfaces;

import javax.ejb.Local;

import com.jwxicc.cricket.entity.Inning;

@Local
public interface InningsManagerLocal extends InningsFacade, CricketDataManager<Inning> {
}