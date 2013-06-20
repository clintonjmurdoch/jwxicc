package com.jwxicc.cricket.jsf;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.imageio.ImageIO;

import com.jwxicc.cricket.interfaces.PlayerManager;

@ManagedBean(name="imageBean")
@SessionScoped
public class ImageBean implements Serializable {
	
	@EJB
	PlayerManager pm;

	public void paintImage(OutputStream stream, Object obj) throws IOException {
		try {
			System.out.println("called paintImage");
			BufferedImage bi = ImageIO.read(new ByteArrayInputStream(pm.getPlayerForProfile((Integer) obj).getPlayerDetail()
					.getImage()));
			ImageIO.write(bi, "jpeg", stream);
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}
}
