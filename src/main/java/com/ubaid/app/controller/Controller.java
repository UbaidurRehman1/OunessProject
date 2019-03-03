package com.ubaid.app.controller;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.ubaid.app.model.DataSource;
import com.ubaid.app.model.logger.Queue;
import com.ubaid.app.model.object.NewProducts;
import com.ubaid.app.model.object.Products;

public class Controller
{
	private final Queue queue;
	private ExecutorService intiater_thread;
	private List<Products> products = new Vector<>();
	private static boolean isPause;
	private ExecutorService service = Executors.newFixedThreadPool(1);
	private Connection con;
	private static final String query = "INSERT INTO products(name, nameE, productLink, imageLink, price, type, brand) VALUES(?, ?, ?, ?, ?, ?, ?)";
	private PreparedStatement statement;
	//for test
	public Controller()
	{
		queue = new Queue();
	}
	
	public static void main(String [] args)
	{
		Controller controller = new Controller();
		List<Products> list = controller.getList();
		assert(list != null);
	}
	
	
	public Controller(Queue queue)
	{
		this.queue = queue;
		con = DataSource.getConnection();
		
		try
		{
			statement = con.prepareStatement(query);
		}
		catch (SQLException e)
		{
			queue.setText(e, queue.getErrorIndex());
		}
		
		runInitiater();
	}
	
	public List<Products> getList()
	{
		try
		{
			PreparedStatement statement = DataSource.getConnection().prepareStatement("SELECT * from products limit 2");
			ResultSet rs = statement.executeQuery();
			while(rs.next())
			{
				Products product = new NewProducts(rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8));
				products.add(product);
			}
		}
		catch(SQLException exp)
		{
			exp.printStackTrace();
		}
		
		
		return products;
		
		
	}
	
	private void runInitiater()
	{
		intiater_thread = Executors.newFixedThreadPool(1);
		intiater_thread.execute(new Initiater(this));
		intiater_thread.shutdown();
	}


	public Queue getQueue() {
		return queue;
	}
	
	public void setRecord(Products product)
	{
		
		service.execute(new Runnable()
		{
			
			@Override
			public void run()
			{
				try
				{
					statement.setString(1, product.getProductName());
					statement.setString(2, product.getProductNameInEnglish());
					statement.setString(3, product.getProductLink());
					statement.setString(4, product.getImageLink());
					statement.setString(5, product.getProductPrice());
					statement.setString(6, product.getProdctType());
					statement.setString(7, product.getBrand());
					statement.execute();
				}
				catch(SQLException exp)
				{
					getQueue().setText(exp, getQueue().getErrorIndex());
				}
				
			}
		});

	}

	
	public static boolean isPause() {
		return isPause;
	}

	public static void setPause(boolean isPause) {
		Controller.isPause = isPause;
	}
	
}
