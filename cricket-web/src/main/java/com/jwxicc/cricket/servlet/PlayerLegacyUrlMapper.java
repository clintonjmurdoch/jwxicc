package com.jwxicc.cricket.servlet;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.jwxicc.cricket.entity.Player;
import com.jwxicc.cricket.interfaces.PlayerManager;

@WebServlet("*" + PlayerLegacyUrlMapper.PAGE_EXTENSION)
public class PlayerLegacyUrlMapper extends HttpServlet {

	public static final String PAGE_EXTENSION = ".php";

	@EJB
	PlayerManager playerManager;

	@Override
	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		process(arg0, arg1);
	}

	@Override
	protected void doPost(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		process(arg0, arg1);
	}

	private void process(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pageName = request.getServletPath();
		pageName = StringUtils.substringBefore(pageName, PAGE_EXTENSION);
		
		// servletPath always starts with /
		pageName = pageName.substring(1);
		
		if (pageName.length() == 0) {
			throw new LegacyUrlNotResolvedException(request.getRequestURI());
		}

		if (StringUtils.isBlank(pageName) || !StringUtils.isNumeric(pageName)) {
			throw new LegacyUrlNotResolvedException(request.getRequestURI());
		}

		Player player = playerManager.getPlayerForCapNumber(Integer.parseInt(pageName));

		if (player == null) {
			throw new LegacyUrlNotResolvedException(request.getRequestURI());
		} else {
			response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
			response.setHeader("Location",
					getBaseUrl(request) + "/player.xhtml?playerId=" + player.getPlayerId());
		}
	}

	private String getBaseUrl(HttpServletRequest request) {
		return request.getScheme() + "://" + request.getServerName() + ":"
				+ request.getServerPort();
	}
	
	public static class LegacyUrlNotResolvedException extends ServletException {
		
		public LegacyUrlNotResolvedException(String message) {
			super(message);
		}
	}
}
