/*
 * IRIS(Integrated SW Repository for Information Sharing) version 1.0
 *
 * Copyright �� 2017 kt corp. All rights reserved.
 *
 * This is a proprietary software of kt corp, and you may not use this file except in
 * compliance with license agreement with kt corp. Any redistribution or use of this
 * software, with or without modification shall be strictly prohibited without prior written
 * approval of kt corp, and the copyright notice above does not evidence any actual or
 * intended publication of such software.
 *
 */

package kr.co.paycast.models.calc.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import kr.co.paycast.viewmodels.calc.CalcStatsItem;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface CalculateService {
	// Common
	public void flush();
	
	//
	// for Calculate Dao
	//
	public List<CalcStatsItem> getCalcDayRead(String fromDate, String toDate, int storeId);

	public List<CalcStatsItem> getCalcMonthRead(String fromDate, String toDate,	int storeId);

	public List<CalcStatsItem> getCalcMenuRead(String fromDate, String toDate, int storeId);

	public void updateTermly();

	public boolean operEnd(String storeId, HttpSession session);
	
}
