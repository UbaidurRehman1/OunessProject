package com.ubaid.app.controller;



import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Initiater implements Runnable
{

	Controller controller;
	
	public Initiater(Controller controller)
	{
		this.controller = controller;
	}
	
	@Override
	public void run()
	{
		setSystemProperties();
		embedLogger();
		init();
	}
		
	private void init()
	{
		try
		{
			ExecutorService thread = Executors.newFixedThreadPool(1);
			thread.execute(new Handover());
			thread.shutdown();

		}
		catch(Exception exp)
		{
			controller.getQueue().setText(exp, controller.getQueue().getErrorIndex());
		}
	}
	
	
	private void embedLogger()
	{
		ExecutorService service = Executors.newFixedThreadPool(1);
		service.execute(new Runnable()
		{
			@Override
			public void run()
			{
				while(true)
				{
					System.out.println(controller.getQueue().getNext());
				}
			}
		});
	}
	
	private void setSystemProperties()
	{
		try
		{
			//changes
			System.setProperty("webdriver.chrome.driver", ".//Resource//chromedriver.exe");
			System.setProperty("webdriver.gecko.driver", "./Resource/geckodriver.exe");		
	
		}
		catch(Exception exp)
		{
			controller.getQueue().setText(exp, controller.getQueue().getErrorIndex());
		}
	}
	
	
	private class Handover implements Runnable
	{

		@Override
		public void run()
		{
			initializeApp();
		}
		
		private void initializeApp()
		{
			try
			{
				new AppHandler(controller);
			}
			catch(Exception exp)
			{
				controller.getQueue().setText(exp, controller.getQueue().getErrorIndex());
			}
		}
		
	}
}
