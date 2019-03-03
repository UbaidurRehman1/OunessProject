package com.ubaid.app.model.document;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Random;

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
		
	}
	
	private String getSpecialURL(String url)
	{
		return url;
	}
	
	
	private int count(String url)
	{
		Document document = getDoucument(url);
		
		assert(document != null);
		
		return -1;
	}
	
	private List<String> getURLs(String url, int count)
	{
		return null;
	}
	
	
	@Override
	public List<Document> getDocuments()
	{
		return null;
	}
	
	private Document getDoucument(String url)
	{
		Random random = new Random();

		try
		{
			Document document = Jsoup.connect(url).maxBodySize(0).userAgent(userAgent).referrer(referral).get();			
			return document;
		
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
