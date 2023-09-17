package com.easymedia.service;


import com.easymedia.dto.EmfMap;

/**
 * <pre> 
 * FRAFrptService Service
 * </pre>
 * 
 * @ClassName		: FRAFrptService.java
 * @Description		: Flexible Report Service
 * @author 안진용
 * @since 2019.01.09
 * @version 1.0
 * @see
 * @Modification Information
 * <pre>
 * 		since			author				   description
 *   ===========    ==============    =============================
 *    2019.01.09		  안진용					     최초생성
 * </pre>
 */ 
public interface FRAFrptService {
	
    /**
     * Flexible Report HGSI List
     * 
     * @param emfMap
	 * @return
	 * @throws Exception
     */
    public EmfMap selectFrptHgsiListAjax(EmfMap emfMap) throws Exception;
    
    
    
    /**
     * Flexible Report RSC List
     * 
     * @param emfMap
	 * @return
	 * @throws Exception
     */
    public EmfMap selectFrptRscListAjax(EmfMap emfMap) throws Exception;     

    /**
     * Flexible Report Mystery Shopping List
     * 
     * @param emfMap
     * @return
     * @throws Exception
     */
    public EmfMap selectFrptMysListAjax(EmfMap emfMap) throws Exception;     
    
    /**
     * Flexible Report Dealer KPI List Ajax
     * 
     * @param emfMap
     * @return
     * @throws Exception
     */
    public EmfMap selectFrptKpiListAjax(EmfMap emfMap) throws Exception;     
    
    
    /**
     * Flexible Report FieldWork List
     * 
     * @param emfMap
	 * @return
	 * @throws Exception
     */
    public EmfMap selectFrptFieldWorkList(EmfMap emfMap) throws Exception;       
    
    /**
     * Flexible Report VinList List
     * 
     * @param emfMap
     * @return
     * @throws Exception
     */
    public EmfMap selectFrptVinList(EmfMap emfMap) throws Exception;       
    
    
	
}