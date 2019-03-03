package com.ubaid.app.model.document;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.ubaid.app.controller.Controller;
import com.ubaid.app.model.scrapper.Scrapper;


public class Documents implements IDocument
{

	
	protected Controller controller;
	String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36";
	String referral = "http://www.google.com";

	
	
	public Documents(String url, Controller controller)
	{
		this.controller = controller;
		url = getSpecialURL(url);
		controller.getQueue().setIndex("First URL: " + url);		

		int count = count(url);
		List<String> urls = getURLs(url, count);
		
	}
	
	private String getSpecialURL(String url)
	{
		return url;
	}
	
	/**
	 * 
	 * @param url of the requested page
	 * @return total number of pages of this url
	 */
	private int count(String url)
	{
		String document = getDoucument(url);
		try
		{
			JSONObject object = new JSONObject(document);
			JSONObject pagination = object.getJSONObject("pagination");
			int count = pagination.getInt("totalPages");
			return count;
		}
		catch(JSONException | NumberFormatException exp)
		{
			return -1;
		}		
	}
	
	/**
	 * 
	 * @param url of requested page
	 * @param count how many pages of this url
	 * @return a list of urls of different subpages 
	 */
	private List<String> getURLs(String url, int count)
	{
		List<String> list = new ArrayList<String>();
		url = url.substring(0, url.length() - 1);
		for(int i = 0; i < count; i++)
		{
			list.add(url + i);
		}
		return list;
	}
	
	
	@Override
	public List<Document> getDocuments()
	{
		return null;
	}
	
	/**
	 * 
	 * @param url of the page
	 * @return an stringly json got from the url
	 */
	private String getDoucument(String url)
	{
		Random random = new Random();

		try
		{
			//getting json string from the url
			String json = Jsoup.connect(url).ignoreContentType(true).execute().body();
			return json;		
		}
		catch(HttpStatusException e1)
		{
			try
			{
				Thread.sleep(random.nextInt(10000));
			}
			catch (InterruptedException e)
			{
				return null;
			}
			e1.printStackTrace();
			return getDoucument(url);

		}
		catch(SocketTimeoutException | UnknownHostException exp)
		{
			try
			{
				Thread.sleep(random.nextInt(10000));
			}
			catch (InterruptedException e)
			{

			}
			exp.printStackTrace();
			return getDoucument(url);

		}
		catch (IOException exp)
		{
			try
			{
				Thread.sleep(random.nextInt(10000));
			}
			catch (InterruptedException e)
			{
			}	
			exp.printStackTrace();
			return getDoucument(url);

		}
	}
	
	private void addDocuments(String url)
	{
		Random random = new Random();
		try
		{
			Document document = Jsoup.connect(url).maxBodySize(0).userAgent(userAgent).referrer(referral).get();
			
			try
			{
				if(document != null)
					new Scrapper(document, controller);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
			try
			{
				Thread.sleep(random.nextInt(10000));
			}
			catch(InterruptedException exp)
			{
				exp.printStackTrace();
			}
			
		}
		catch(HttpStatusException e1)
		{
			try
			{
				Thread.sleep(random.nextInt(10000));
			}
			catch (InterruptedException e)
			{
				addDocuments(url);
			}
		}
		catch(SocketTimeoutException | UnknownHostException exp)
		{
			try
			{
				Thread.sleep(random.nextInt(10000));
			}
			catch (InterruptedException e)
			{
				addDocuments(url);
			}
		}
		catch (IOException exp)
		{
			try
			{
				Thread.sleep(random.nextInt(10000));
			}
			catch (InterruptedException e)
			{
				addDocuments(url);
			}	
		}		
	}
	
}
