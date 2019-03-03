package com.ubaid.app.model.q;

import com.ubaid.app.model.logger.Queue;

public class Q
{
	long[] array;
	
	private Queue q;
	
	public Q(Queue q)
	{
		array = new long[3];
		this.q = q;
	}

	//this will add from head, and remove from the tail
	public void add(long number)
	{
		array[0] = array[1];
		
		array[1] = array[2];
		
		array[2] = number;		
		
		print();
	}
	
	private void print()
	{
		String str = "";
		
		for(int j = 0; j < 3; j++)
		{
			str += array[j] + ", ";
		}
		
		str += "\n";
		
		q.setIndex(str);
	}
	
	public boolean isBottom()
	{
		if((array[0] == array[1]) && (array[1] == array[2]) && (array[2] == array[0]))
			return true;
		else
			return false;
	}
	
}
