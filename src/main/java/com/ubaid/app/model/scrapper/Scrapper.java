package com.ubaid.app.model.scrapper;


import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ubaid.app.controller.Controller;
import com.ubaid.app.model.object.NewProducts;
import com.ubaid.app.model.object.Products;

public class Scrapper implements ScrapperI
{	
	public Scrapper(String document, Controller controller) throws Exception
	{		
		try
		{

			String name = "Not Available";
			String product_link = "Not Available";
			String product_price = "Not Available";
			String product_name_in_english = "Not Available";
			String brand = "Not Available";
			String img_link = "Not Avaiable";
			String baseImageMediaURL = "http://ounass-prod2.atgcdn.ae/small_light(dw=586,of=webp,q=90)/pub/media/catalog/product";
			
			int price = -1;

			JSONObject object = new JSONObject(document);
			
			JSONArray array = object.getJSONArray("hits");

			Iterator<Object> _items = array.iterator();

			while(_items.hasNext())
			{
				
				try
				{
					Object object2 = _items.next();
					JSONObject obj = new JSONObject(object2.toString());
					
					price = obj.getInt("price");
					name = obj.getString("name");
					String _product_link = obj.getString("deepLink");
					int index = _product_link.indexOf("https://en-saudi.ounass.com/");
					int last_index = _product_link.lastIndexOf("&");
					try
					{
						product_link = _product_link.substring(index, last_index);						
					}
					catch(IndexOutOfBoundsException exp)
					{
						product_link = _product_link;
					}
				
					JSONArray media = obj.getJSONArray("media");
					JSONObject _src = media.getJSONObject(0);
					String partialURL = _src.getString("src");
					img_link = baseImageMediaURL + partialURL;
					
					JSONObject analytics = obj.getJSONObject("analytics");
					
					String type = analytics.getString("productClass");
					brand = analytics.getString("brand");

					product_price = Integer.toString(price);
					assert(img_link != null);
			
					Products product_ = new NewProducts(name, product_name_in_english, product_link, img_link, product_price, type, brand);
					controller.getQueue().setIndex(product_.toString());
					controller.setRecord(product_);

					
				}
				catch(JSONException | NullPointerException e)
				{
					e.printStackTrace();
				}
			}
			
			assert(array != null);			
		}
		catch(Exception exp)
		{
			exp.printStackTrace();
		}
	}
}
