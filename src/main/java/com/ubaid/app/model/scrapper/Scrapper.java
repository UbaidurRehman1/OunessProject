package com.ubaid.app.model.scrapper;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ubaid.app.controller.Controller;
import com.ubaid.app.model.object.NewProducts;
import com.ubaid.app.model.object.Products;

public class Scrapper implements ScrapperI
{

	int index = 0;
	String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36";
	String referral = "http://www.google.com";

	String img_link = "Not Available";
	
	String baseURL = "";
	
	
	public Scrapper(Document document, Controller controller) throws Exception
	{		
		try
		{
			
			Elements product_divs = document.select("li.s-item");
			String baseURl = document.baseUri();
			
			for(Element product : product_divs)
			{
				String name = "Not Available";
				String product_link = "Not Available";
				String product_price = "Not Available";
				String product_name_in_english = "Not Available";
				String brand = "Not Available";
				String img_link = "Not Avaiable";
				

				
				
				
				
				
				try
				{
					Element _product_link = product.select("a.s-item__link").first();
					product_link = _product_link.absUrl("href");
				}
				catch(Exception exp)
				{
					
				}

				
				
				try
				{
					Element imgElement = product.getElementsByTag("img").first();
					img_link = imgElement.absUrl("src");
					String newImageLink = getImageLink(product_link, 0);
					if(newImageLink != null)
						img_link = newImageLink;
				}
				catch(Exception exp)
				{
					
				}

							
				try
				{
					Element _product_price = product.select(".s-item__price").first();
					product_price = _product_price.text();
				}
				catch(Exception exp)
				{
					
				}
				
				try
				{
					
					Element _name = product.select("h3.s-item__title").first();

					name = _name.text();
				}
				catch(Exception exp)
				{
					exp.printStackTrace();
				}
				
				String type = "Cell-Phone-Cases-Covers-Skins";

				try
				{
					
					type = getType(baseURl);
				}
				catch(Exception exp)
				{
					
				}
				
					
				
				
				Products product_ = new NewProducts(name, product_name_in_english, product_link, img_link, product_price, type, brand);
				controller.getQueue().setIndex(product_.toString());
				controller.setRecord(product_);
			}
			
			
		}
		catch(Exception exp)
		{
			System.out.println(document.head());
			exp.printStackTrace();
			
		}

	}
	
		
	public static void main(String [] args)
	{
		
	}
	
	
	private String getImageLink(String url, int counter)
	{
		
		String imgLink = null;

		if(counter > 4)
			return imgLink;
		
		try
		{
			Document doc = Jsoup.connect(url).maxBodySize(0).userAgent(userAgent).referrer(referral).get();
						
			try
			{
				Elements images = doc.getElementsByAttributeValue("property", "og:image");
				Element img_link = images.first();
				imgLink = img_link.attr("content");
			}
			catch(NullPointerException exp)
			{
				
			}
		}
		catch(HttpStatusException e1)
		{
			e1.printStackTrace();
			try
			{
				Thread.sleep(10000);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			
			imgLink = getImageLink(url, ++counter);
			
		}
		catch(SocketTimeoutException | UnknownHostException exp)
		{
			exp.printStackTrace();
			try
			{
				Thread.sleep(1000);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			
			imgLink = getImageLink(url, ++counter);
		}
		catch(IOException exp)
		{
			exp.printStackTrace();
			try
			{
				Thread.sleep(1000);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			
			imgLink = getImageLink(url, ++counter);
			
		}
		catch(Exception exp)
		{
			exp.printStackTrace();
			return imgLink;
		}
		return imgLink;
	}

	
	private static String getType(String url)
	{
		try
		{
			String[] words = url.split("/");
			String actualWord = words[words.length - 3];
			return actualWord;
			
		}
		catch(Exception exp)
		{
			return "eBay";
		}
	}
		
}
