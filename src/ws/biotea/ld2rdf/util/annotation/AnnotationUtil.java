package ws.biotea.ld2rdf.util.annotation;

import java.util.Vector;

import ws.biotea.ld2rdf.rdf.model.aoextended.AnnotationE;

public class AnnotationUtil {
	public String annotationListToCloudString(Vector<AnnotationE> list) {
		String str = "";
		int frequency = -1, header = 16;
        for (int line = 1, i = list.size()-1; i >= 0; line++, i--) {
        	AnnotationE pair = list.get(i);
        	
        	int freq = pair.getFrequency();
        	if (freq != frequency) {
	    		frequency = freq;
	    		header--;
	    	}
        	str += "<span class='ld_cloud_" + header + "'>" + pair.getBody() + "(" + freq + ")</span>";
        	
        	if (line % 3 == 0) {
        		str += "<br/>";
        	} else {
        		str += "&nbsp;";
        	}
        }
        return str;
	}
}
