package com.easymedia.service;

import com.easymedia.dto.EmfMap;
import com.easymedia.service.dao.COMailDAO;
import com.easymedia.service.mail.COMailDbDAO;
import com.easymedia.utility.EgovStringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService
{

    /** 메일 API 주소 **/
    @Value("${mail.api.url}")
	private String mailApiUrl;

    /** HTTP URL **/
    @Value("${globals.http-url}")
	private String httpUrl;

    /** HTTP ADMIN URL **/
    @Value("${globals.http-admin-url}")
	private String httpAdminUrl;

    /** HTTP ADMIN URL **/
    @Value("${globals.http-web-source-url}")
	private String httpWebSourceUrl;

    /** HTTP ADMIN URL **/
    @Value("${globals.server-ip}")
	private String ServerIp;

    /** SITENAME **/
    @Value("${globals.site-name}")
	private String globalsSiteName;

    /** 보내는 이메일 **/
    @Value("${globals.from-user}")
	private String fromUser;

    /** 이메일 서버 연동 identity **/
    @Value("${globals.my-mailer-identity}")
	private String myMailerIdentity;

	/** 이메일 서버 연동 identity **/
	@Value("${globals.mail-tmpl-file-path}")
	private String mailTmplFilePath;

    /** DAO 접속 **/
    private final COMailDAO cOMailDAO;

    /** EMAIL DB DAO 접속 **/
    private final COMailDbDAO cOMailDbDAO;

	private final JavaMailSender mailSender;
	/**
	 * 메일 발송
	 * @param emfMap
	 * @throws Exception
	 */
	public void sendMail(EmfMap emfMap)throws Exception
	{
		MimeMessage message = mailSender.createMimeMessage();

		try
		{
			MimeMessageHelper messageHelper = new MimeMessageHelper(mailSender.createMimeMessage(), true, "UTF-8");

			messageHelper.setSubject(emfMap.getString("subject"));
			messageHelper.setTo(emfMap.getString("toUser"));

			if (!"".equals(emfMap.getString("cc")))
			{
				messageHelper.setCc(emfMap.getString("cc"));
			}

			messageHelper.setFrom(emfMap.getString("fromUser"));
			messageHelper.setText(emfMap.getString("contents"), true);

			mailSender.send(message);
		}
		catch(Exception me)
		{
			me.printStackTrace();
		}
	}

    /**
     * 사용자 메일 발송 내용.
     *
     * @param emfMap
     * @param fileNm
     * @throws Exception
     */
    public void sendTempleteMail(EmfMap emfMap, String fileNm) throws Exception
    {
        BufferedReader br = null;

        emfMap.put("httpUrl", httpUrl);
        emfMap.put("httpAdminUrl", httpAdminUrl);
        emfMap.put("httpWebSourceUrl", httpWebSourceUrl);

        StringBuffer mailStr = new StringBuffer();

        EmfMap paramMap = null;
		String sendFromUser= fromUser;
        try
        {
            String line = null;

            String fileUrl = mailTmplFilePath + fileNm;

            br = new BufferedReader(new InputStreamReader(new FileInputStream(fileUrl),"UTF-8"));

            while ((line=br.readLine()) != null)
            {
            	mailStr.append(line).append("\n");
            }

            br.close();
            br = null;
            line = null;

            String contents = mailStr.toString();

            mailStr = null;

            Iterator iterator = emfMap.entrySet().iterator();

            while (iterator.hasNext())
            {
            	Entry entry = (Entry) iterator.next();

            	contents = contents.replaceAll("&&" + entry.getKey() + "&&", String.valueOf(entry.getValue()));
            }

            if (!"".equals(emfMap.getString("fromUser")))
            {
				sendFromUser = emfMap.getString("fromUser");
            }

            paramMap = new EmfMap();

            paramMap.put("subject", emfMap.getString("subject"));
            paramMap.put("toUser", emfMap.getString("toUser"));
        	paramMap.put("fromUser", sendFromUser);
            paramMap.put("contents", contents);

            sendMail(paramMap);

            paramMap = null;
        }
        catch (Exception e)
        {
            throw (Exception) e;
        }
        finally
        {
            if (br != null)
            {
            	br.close();
            }
        }
    }

    /**
     * 스케쥴에서 발송할 HGSI 설문 발송
     *
     * @param emfMap
     * @param fileNm
     * @throws Exception
     */
    public void sendHgsiTempleteMail(EmfMap emfMap, String fileNm) throws Exception
    {
        JSONObject rtnJson	 	= null;
    	String result_code 		= null;
    	String result_message	= null;
    	JSONObject resultBody   = null;
    	EmfMap tmpMap = null;
    	String surveyUrl = "";

        emfMap.put("httpUrl"			, httpUrl);
        emfMap.put("httpAdminUrl"		, httpAdminUrl);
        emfMap.put("httpWebSourceUrl"	, httpWebSourceUrl);

        //개발서버에서는 이메일 발송을 하지 않느다.
        //개발서버에서 알림 이메일이 나가는 경우가 생기네...devTest를 1로 2019-11-19 기획자 요청으로 개발서버에서 숨김처리
      /*  if(ServerIp.trim().indexOf("192.168.0.13") > -1 && !"1".equals(emfMap.getString("devTest")))
        {
        	return;
        }*/

        //타겟 추가
        EmfMap mailMap = new EmfMap();
        mailMap.setCamelYn("N");
        List<EmfMap> toMailList   = (List)emfMap.get("toMailList");
        mailMap.put("sender_email"	, fromUser);
    	mailMap.put("sender_name"	, globalsSiteName);
    	mailMap.put("hgsi_yn", emfMap.getString("hgsiYn"));
    	mailMap.put("toMailList"	, toMailList);
    	BufferedReader br = null;
		String contents = null;
		if(!"".equals(fileNm))
		{
			try
	        {
	            String line = null;
	            StringBuffer mailStr = new StringBuffer();
	            String fileUrl = mailTmplFilePath + fileNm;
	            br = new BufferedReader(new InputStreamReader(new FileInputStream(fileUrl),"UTF-8"));
	            while ((line=br.readLine()) != null)
	            {
	            	mailStr.append(line).append("\n");
	            }
	            br.close();
	            br = null;
	            line = null;
	            contents = mailStr.toString();
	        }
	        catch (Exception e)
	        {
	            throw (Exception) e;
	        }
	        finally
	        {
	            if (br != null)
	            {
	            	br.close();
	            }
	        }
		}
		else
		{
			contents = emfMap.getString("contents");
		}
		String timeStamp = EgovStringUtil.getTimeStamp();
		mailMap.put("identity", myMailerIdentity);
		mailMap.put("title", timeStamp);
		mailMap.put("description", mailMap.getString("title"));
		mailMap.put("fields", "email,name,first,second,third,fourth,fifth,sixth,seventh,eighth,ninth,tenth,eleventh,twelfth,thirteenth,fourteenth,fifteenth,sixteenth,seventeenth");
//		mailMap.put("fields", "email,name,first,second,third");
		cOMailDbDAO.setMakeTarget(mailMap);
		mailMap.put("title", emfMap.getString("title"));
//		mailMap.put("target_id", 26);
		if(!"".equals(mailMap.getString("target_id")) && !"0".equals(mailMap.getString("target_id")) && !"-1".equals(mailMap.getString("target_id")))
		{
			mailMap.put("result_code", "0000");
			mailMap.put("result_messagae", "타겟 추가 성공");
			cOMailDAO.setEmailSendMst(mailMap);
			if(toMailList != null && toMailList.size() > 0)
			{
				//주소록 등록
				int actCnt = cOMailDbDAO.setCsvImport(mailMap);
				if(actCnt > 0)
				{
					//주소록 수정
					mailMap.put("actCnt", actCnt);
					cOMailDbDAO.setTargetListCnt(mailMap);
					//캠페인 생성
					mailMap.put("campaignTitle", mailMap.getString("title"));
					mailMap.put("explanation", mailMap.getString("description"));
					mailMap.put("emailTitle", mailMap.getString("title"));
					mailMap.put("content", contents);
					mailMap.put("senderEail", fromUser);
					cOMailDbDAO.setMakeCampaign(mailMap);

					cOMailDAO.setEmailSendDtl(mailMap);
				}
			}
		}
		else
		{
			mailMap.put("target_id", mailMap.getString("target_id"));
			mailMap.put("result_code", "0");
			mailMap.put("result_messagae", "타겟 추가 실패");
			cOMailDAO.setEmailSendMst(mailMap);
		}
    }


    /**
     * 스케쥴에서 발송할 이벤트 메일
     *
     * @param emfMap
     * @param fileNm
     * @throws Exception
     */
    public void sendEventTempleteMail(EmfMap emfMap, String fileNm) throws Exception
    {
        //개발서버에서 알림 이메일이 나가는 경우가 생기네...devTest를 1로
        /*if(ServerIp.trim().indexOf("192.168.0.13") > -1 && !"1".equals(emfMap.getString("devTest")))
        {
        	return;
        }*/

        JSONObject rtnJson	 	= null;
    	String result_code 		= null;
    	String result_message	= null;
    	JSONObject resultBody   = null;
    	EmfMap tmpMap = null;
    	String surveyUrl = "";

        emfMap.put("httpUrl"			, httpUrl);
        emfMap.put("httpAdminUrl"		, httpAdminUrl);
        emfMap.put("httpWebSourceUrl"	, httpWebSourceUrl);

        //타겟 추가
        EmfMap mailMap = new EmfMap();
        List<EmfMap> toMailList   = (List)emfMap.get("toMailList");
        if(toMailList == null)
        {
        	return;
        }
        mailMap.put("senderEmail"	, fromUser);
    	mailMap.put("senderName"	, globalsSiteName);
    	mailMap.put("toMailList"	, toMailList);
    	mailMap.put("title"	, emfMap.getString("title"));
    	BufferedReader br = null;
		String contents = null;
		if(!"".equals(fileNm))
		{
			try
	        {
	            String line = null;
	            StringBuffer mailStr = new StringBuffer();
	            String fileUrl = mailTmplFilePath + fileNm;
	            br = new BufferedReader(new InputStreamReader(new FileInputStream(fileUrl),"UTF-8"));
	            while ((line=br.readLine()) != null)
	            {
	            	mailStr.append(line).append("\n");
	            }
	            br.close();
	            br = null;
	            line = null;
	            contents = mailStr.toString();
	        }
	        catch (Exception e)
	        {
	            throw (Exception) e;
	        }
	        finally
	        {
	            if (br != null)
	            {
	            	br.close();
	            }
	        }
		}
		else
		{
			contents = emfMap.getString("contents");
		}
		//데이터 처리
		//String userInfo = "";
		Iterator iterator = null;
		Entry entry = null;
		if(toMailList != null && toMailList.size() > 0)
		{
			JSONObject jsonObject = new JSONObject();
			JSONArray req_array = new JSONArray();
			for(int q = 0 ; q < toMailList.size() ; q++)
			{
				tmpMap = toMailList.get(q);
				/*if("".equals(userInfo))
				{
					userInfo = tmpMap.getString("email");
				}
				else
				{
					userInfo = userInfo + "þ" + tmpMap.getString("email");
				}*/
				iterator = tmpMap.entrySet().iterator();
				JSONObject data = new JSONObject();
				JSONObject alternates = new JSONObject();
	            while (iterator.hasNext())
	            {
	            	entry = (Entry) iterator.next();
	            	//userInfo = userInfo + "æ" + entry.getKey() + "Ð" + entry.getValue();
	            	
	            	// mymailer db수정으로 userinfo 수정 20220208
	            	if(entry.getValue().getClass().getName().indexOf("List") < 0){
	            		data.put(entry.getKey(), entry.getValue());
		    			alternates.put("alternates", data);
	            	} 
	            }
	            jsonObject.put(tmpMap.getString("email"),alternates);
	            req_array.add(jsonObject);
			}
			
			mailMap.put("contents", contents.replaceAll("&&contents&&", tmpMap.getString("body")));
			//mailMap.put("userInfo", userInfo);
			mailMap.put("userInfo", ""+jsonObject+"");
            mailMap.put("eventId", 2);
    		cOMailDbDAO.insertEventEmail(mailMap);
    		
		}else{
			mailMap.put("contents", contents);
			//mailMap.put("userInfo", userInfo);
			mailMap.put("userInfo", "");
			mailMap.put("eventId", 2);
			cOMailDbDAO.insertEventEmail(mailMap);
		}
    }

    public void setDeleteTargetList() throws Exception
    {
//    	EmfMap mailMap = new EmfMap();
//    	mailMap.setCamelYn("N");
//    	mailMap.put("target_id", 26);
//    	cOMailDbDAO.setDeleteTargetList(mailMap);
    }

    private JSONObject getPostData(EmfMap mailMap, URL url) throws Exception
    {
    	StringBuilder postData = new StringBuilder();
        for(String key : mailMap.keySet())
        {
            if(postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(key, "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(mailMap.getString(key), "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes); // POST 호출

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

    	JSONParser jsonParser = new JSONParser();
    	JSONObject jsonObject = (JSONObject)jsonParser.parse(in);
    	in.close();
    	return jsonObject;
    }
}