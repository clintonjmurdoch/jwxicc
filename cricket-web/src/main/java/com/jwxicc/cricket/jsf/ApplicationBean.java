package com.jwxicc.cricket.jsf;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.jar.Manifest;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

@ManagedBean(name="app")
@ApplicationScoped
public class ApplicationBean implements Serializable {
	
	private static final String IMPLEMENTATION_VERSION = "Implementation-Version";
	private String version;

	@PostConstruct
	public void init() {
		Manifest manifest;
	    try {
	        InputStream is = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/META-INF/MANIFEST.MF");
	        manifest = new Manifest();
	        manifest.read(is);
	    } catch (IOException ioe) {
	    	System.out.println("Failed to read manifest");
	    	return;
	    }
	    version = manifest.getMainAttributes().getValue(IMPLEMENTATION_VERSION);
	}
	
	public String getVersion() {
		return version;
	}
}
