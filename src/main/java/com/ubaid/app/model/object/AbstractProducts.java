package com.ubaid.app.model.object;

public abstract class AbstractProducts implements Products
{

	@Override
	public String toString() {
		return "AbstractProducts [productName=" + productName + ", productNameInEnglish=" + productNameInEnglish
				+ ", productLink=" + productLink + ", imageLink=" + imageLink + ", productPrice=" + productPrice + ", BASE URI=" + getBASEURI() + "]";
	}

	protected String productName;
	protected String productNameInEnglish;
	protected String productLink;
	protected String imageLink;
	protected String productPrice;
	protected String baseURI;
	protected String brand;
		
	public AbstractProducts(String name, String nameEnglish, String productLink, String imageLink, String productPrice)
	{
		this.productName = name;
		this.productNameInEnglish = nameEnglish;
		this.productLink = productLink;
		this.imageLink = imageLink;
		this.productPrice = productPrice;
	}
	
	@Override
	public String getProductName()
	{
		return productName;
	}

	@Override
	public String getProductNameInEnglish()
	{
		return productNameInEnglish;
	}

	@Override
	public String getProductLink()
	{
		return productLink;
	}

	@Override
	public String getImageLink()
	{
		return imageLink;
	}

	@Override
	public String getProductPrice()
	{
		return productPrice;
	}
	
	public abstract String getBASEURI();
	public abstract void setBASEURI(String uri);
	abstract void setBrand(String brand);
	public abstract String getBrand();
	
	
}
