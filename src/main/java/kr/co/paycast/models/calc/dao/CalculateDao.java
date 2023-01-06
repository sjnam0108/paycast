package kr.co.paycast.models.calc.dao;

import java.util.Date;
import java.util.List;

import kr.co.paycast.models.calc.StoreCalcDay;
import kr.co.paycast.models.calc.StoreCalcMenu;
import kr.co.paycast.models.calc.StoreCalcStats;

public interface CalculateDao {

	public void saveOrUpdate(StoreCalcDay oneCalcDay);

	public void saveOrUpdate(StoreCalcStats oneCalcStats);

	public void saveOrUpdate(StoreCalcMenu oneCalcMenu);

	public List<StoreCalcDay> calcDayList(Date fromDate1, Date toDate2, int storeId);

	public void calcDayDelete(List<StoreCalcDay> calcDayList);

	public List<StoreCalcStats> calcMonthList(Date fromDate1, Date toDate2,	int storeId);

	public void calcMonthDelete(List<StoreCalcStats> calcMonthList);

	public List<StoreCalcMenu> calcMenuList(Date fromDate1, Date toDate2, int storeId);

	public void calcMenuDelete(List<StoreCalcMenu> calcMenuList);

}
