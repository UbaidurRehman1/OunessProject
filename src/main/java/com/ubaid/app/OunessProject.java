package com.ubaid.app;


import com.ubaid.app.controller.Controller;
import com.ubaid.app.model.logger.Queue;



public class OunessProject
{
	public static void main(String [] args)
	{
		Queue queue = new Queue();
		new Controller(queue);
	}
}
