package com.easymedia.api.controller;

import com.easymedia.dto.EmfMap;
import com.easymedia.service.CODMenuService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;

/**
 * <pre> 
 * 메뉴 관리 Controller
 * </pre>
 * 
 * @ClassName		: CODAbsMenuController.java
 * @Description		: 메뉴 관리 Controller
 * @author 김대환
 * @since 2019.01.15
 * @version 1.0
 * @see
 * @Modification Information
 * <pre>
 * 		since			author				   description
 *   ===========    ==============    =============================
 *    2019.01.15		김대환					최초 생성
 * </pre>
 */
@Tag(name = "메뉴", description = "메뉴")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api/v1/co/cod")
public class CODAbsMenuController  {

	private final CODMenuService cODMenuService;
	
	/**
	 * 전체 메뉴 페이지
	 *
	 * @param emfMap
	 * @return String View URL
	 * @throws Exception
	 */
	@RequestMapping(value="/abs-index.do")
	public String getAllMenuPage(EmfMap emfMap, ModelMap modelMap) throws Exception
	{
		return "mngwserc/co/cod/CODAbsMenuWrite";
	}
	
	/**
	 * 메뉴 목록을 조회한다.
	 *
	 * @param emfMap
	 * @return JSON 데이터
	 * @throws Exception
	 */
	@RequestMapping(value="/menu-list.ajax")
	public void selectMenuList(EmfMap emfMap, HttpServletResponse response) throws Exception
	{
		response.setContentType("text/html;charset=UTF-8");

		PrintWriter out = response.getWriter();

		try
        {
			emfMap.put("isChkd", "N");
            List<EmfMap> menuList = cODMenuService.selectMenuList(emfMap);

            JSONArray jSONArray = new JSONArray();

            int paramSeq = Integer.parseInt(emfMap.getString("menuSeq"));
            int startNum = 0;
            jSONArray = cODMenuService.getJsonData(menuList, startNum, paramSeq);
            out.print(jSONArray);
            
            jSONArray = null;
        }
        catch (Exception he)
		{
			if (log.isDebugEnabled())
			{
				log.debug(he.getMessage());
            }
			throw he;
		}
        finally
        {
            out.close();
        }
	}

	/**
	 * 메뉴의 상세정보를 조회한다.
	 *
	 * @param emfMap
	 * @return JSON 데이터
	 * @throws Exception
	 */
	@RequestMapping(value="/menu-dtl.ajax")
	public EmfMap selectMenuDtl(EmfMap emfMap, ModelMap modelMap) throws Exception
	{
		EmfMap rtnMap = null;
        try
        {
			rtnMap = cODMenuService.selectMenuDtl(emfMap);
        }
		catch (Exception he)
		{
			if (log.isDebugEnabled())
			{
				log.debug(he.getMessage());
            }
			throw he;
        }

        return rtnMap;
	}

	/**
	 * 메뉴를 등록한다.
	 *
	 * @param emfMap
	 * @return JSON 데이터
	 * @throws Exception
	 */
	@RequestMapping(value="/insert.ajax", method=RequestMethod.POST)
	public String insertMenu(EmfMap emfMap, ModelMap modelMap) throws Exception
	{
		try
		{
			modelMap.addAttribute("actCnt", cODMenuService.insertMenu(emfMap));
		}
		catch (Exception he)
		{
			if (log.isDebugEnabled())
			{
				log.debug(he.getMessage());
            }
			throw he;
		}

		return "jsonView";
	}

	/**
	 * 메뉴명을 수정한다.
	 *
	 * @param emfMap
	 * @return JSON 데이터
	 * @throws Exception
	 */
	@RequestMapping(value ="/name-update.ajax", method=RequestMethod.POST)
	public String updateMenuNm(EmfMap emfMap, ModelMap modelMap) throws Exception
	{
		try
		{
			modelMap.addAttribute("actCnt", cODMenuService.updateMenuNm(emfMap));
		}
		catch (Exception he)
		{
			if (log.isDebugEnabled())
			{
				log.debug(he.getMessage());
            }
			throw he;
		}

		return "jsonView";
	}

	/**
	 * 메뉴정보를 수정한다.
	 *
	 * @param emfMap
	 * @return JSON 데이터
	 * @throws Exception
	 */
	@RequestMapping(value="/info-update.ajax", method=RequestMethod.POST)
	public String updateMenuInf(EmfMap emfMap, ModelMap modelMap) throws Exception
	{
		try
		{
			modelMap.addAttribute("actCnt", cODMenuService.updateMenuInf(emfMap));
		}
		catch (Exception he)
		{
			if (log.isDebugEnabled())
			{
				log.debug(he.getMessage());
            }
			throw he;
		}

		return "jsonView";
	}

	/**
	 * 사용자 노출 여부를 수정한다.
	 *
	 * @param emfMap
	 * @return JSON 데이터
	 * @throws Exception
	 */
	@RequestMapping(value="/use-update.ajax", method=RequestMethod.POST)
	public String updateUserUseYn(EmfMap emfMap, ModelMap modelMap) throws Exception
	{
		try
		{
			modelMap.addAttribute("actCnt", cODMenuService.updateUserUseYn(emfMap));
		}
		catch (Exception he)
		{
			if (log.isDebugEnabled())
			{
				log.debug(he.getMessage());
            }
			throw he;
		}

		return "jsonView";
	}

	/**
	 * 메뉴를 이동한다.
	 *
	 * @param emfMap
	 * @return JSON 데이터
	 * @throws Exception
	 */
	@RequestMapping(value="/pstn-update.ajax", method=RequestMethod.POST)
	public String updateMenuPstn(EmfMap emfMap, ModelMap modelMap) throws Exception
	{
		try
		{
			modelMap.addAttribute("actCnt", cODMenuService.updateMenuPstn(emfMap));
		}
		catch (Exception he)
		{
			if (log.isDebugEnabled())
			{
				log.debug(he.getMessage());
            }
			throw he;
		}

		return "jsonView";
	}

	/**
	 * 메뉴를 삭제한다.
	 *
	 * @param emfMap
	 * @return JSON 데이터
	 * @throws Exception
	 */
	@RequestMapping(value ="/delete.ajax", method=RequestMethod.POST)
	public String deleteMenu(EmfMap emfMap, ModelMap modelMap) throws Exception
	{
		try
		{
			modelMap.addAttribute("actCnt", cODMenuService.deleteMenu(emfMap));
		}
		catch (Exception he)
		{
			if (log.isDebugEnabled())
			{
				log.debug(he.getMessage());
            }
			throw he;
		}

		return "jsonView";
	}

	/**
     * 상위부모를 다 가져온다.
     *
	 * @param emfMap
	 * @return JSON 데이터
	 * @throws Exception
	 */
	@RequestMapping(value="/parnt-data.ajax", method=RequestMethod.POST)
	public void getParntData(EmfMap emfMap, ModelMap modelMap, HttpServletResponse response) throws Exception
	{
		response.setContentType("text/html;charset=UTF-8");

		PrintWriter out = response.getWriter();

        try
        {
            List<EmfMap> list = cODMenuService.getParntData(emfMap);

            JSONArray jSONArray = new JSONArray();

            for ( int i = 0; i < list.size(); )
            {
                JSONObject jSONObject = new JSONObject();

                jSONObject.put("data", list.get(i).getString("menuNm"));

                JSONObject jsonAttr = new JSONObject();

                jsonAttr.put("id", list.get(i).getString("menuSeq"));
                jsonAttr.put("rel", list.get(i).getString("menuGb"));
                jsonAttr.put("parent_treeid", list.get(i).getString("parntSeq"));
                jsonAttr.put("level", list.get(i).getString("dpth"));
                jsonAttr.put("status", list.get(i).getString("userUseYn"));
                jsonAttr.put("link", list.get(i).getString("userLink"));
                jsonAttr.put("treeid", list.get(i).getString("menuSeq"));
                jsonAttr.put("dpth", list.get(i).getString("dpth"));
                jSONObject.put("attr", jsonAttr);
                jsonAttr = null;
                i++;
                jSONArray.add(jSONObject);
                jSONObject = null;
            }

            out.print(jSONArray);
            jSONArray=null;
        }
        catch (Exception he)
        {
        	if (log.isDebugEnabled())
			{
				log.debug(he.getMessage());
            }
			throw he;
        }
        finally
        {
            out.close();
        }
	}

	/**
     * 하위노드를 다 가져온다.
     *
	 * @param emfMap
	 * @return JSON 데이터
	 * @throws Exception
	 */
	@RequestMapping(value="/child-data.ajax", method=RequestMethod.POST)
	public void getChildData(EmfMap emfMap, ModelMap modelMap, HttpServletResponse response) throws Exception
	{
		response.setContentType("text/html;charset=UTF-8");

		PrintWriter out = response.getWriter();

        try
        {
            List<EmfMap> list = cODMenuService.getChildData(emfMap);

            JSONArray jSONArray = new JSONArray();

            EmfMap jsonMap = null;

            for ( int i = 0; i < list.size(); )
            {
            	jsonMap = list.get(i);

                JSONObject jSONObject = new JSONObject();

                jSONObject.put("data", jsonMap.getString("menuNm"));

                JSONObject jsonAttr = new JSONObject();
                jsonAttr.put("id", jsonMap.getString("menuSeq"));
                jsonAttr.put("rel", jsonMap.getString("menuGb"));
                jsonAttr.put("parent_treeid", jsonMap.getString("parntSeq"));
                jsonAttr.put("level", jsonMap.getString("dpth"));
                jsonAttr.put("status", jsonMap.getString("userUseYn"));
                jsonAttr.put("link", jsonMap.getString("userLink"));
                jsonAttr.put("treeid", jsonMap.getString("menuSeq"));
                jsonAttr.put("type", jsonMap.getString("type"));
                jSONObject.put("attr", jsonAttr);

                jsonAttr = null;
                i++;
                jSONArray.add(jSONObject);
                jSONObject = null;
            }

            out.print(jSONArray);
            jSONArray=null;
        }
        catch (Exception he)
        {
        	if (log.isDebugEnabled())
			{
				log.debug(he.getMessage());
            }
			throw he;
        }
        finally
        {
            out.close();
        }
	}
}