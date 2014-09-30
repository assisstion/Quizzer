package com.github.assisstion.Quizzer.gui;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class FileOutputConsumer implements StringConsumer, Closeable{

	protected BufferedWriter bos;

	protected FileOutputConsumer(){

	}

	public FileOutputConsumer(Writer out){
		bos = new BufferedWriter(out);
	}

	public FileOutputConsumer(OutputStream out){
		bos = new BufferedWriter(new OutputStreamWriter(out));
	}

	@Override
	public void accept(String s){
		try{
			bos.write(s + "\n");
		}
		catch(IOException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void close() throws IOException{
		bos.close();
	}

}
