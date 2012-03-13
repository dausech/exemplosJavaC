package br.com.javac.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class Log {

	public static void error(String log) {
		System.out.println("ERROR: " + log);
	}

	public static void info(String log) {
		System.out.println("INFO: " + log);
	}
	
	public static void stackTraceOf(String prefix, Exception e) {
		prefix = prefix == null ? "" : prefix;
		for (String linha : stackTraceOf(e)) {
			error(prefix + linha);
		}
	}
	
	public static List<String> stackTraceOf(Exception e) {
		List<String> stackError = new ArrayList<String>();
		StringWriter sw = null;
		StringBuffer sbStactTrace = null;
		StringBuffer sbOut = null;
		
		try {
			if (e != null) {
				sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw));
				sbStactTrace = sw.getBuffer();
				sbOut = new StringBuffer();
				for (int i = 0; i <= sbStactTrace.length() - 1; i++){
				    char ch = sbStactTrace.charAt(i);
			    	if ((ch != '\n') && (ch != '\r')) {
			        	sbOut.append(ch);
			    	} 
			    	if (ch == '\n') {
			    	    stackError.add(sbOut.toString());
			        	sbOut = new StringBuffer();
			    	}
				}
			}
		} finally {
			sw = null;
			sbStactTrace = null;
			sbOut = null;
		}
		return stackError;
	}

}
