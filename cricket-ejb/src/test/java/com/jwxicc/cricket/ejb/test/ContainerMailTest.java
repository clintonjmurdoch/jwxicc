package com.jwxicc.cricket.ejb.test;

import javax.mail.MessagingException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.jwxicc.cricket.mail.MailSender;

@RunWith(Arquillian.class)
public class ContainerMailTest {

	@Deployment
	public static Archive<?> createDeployment() {
		JavaArchive archive = ShrinkWrap.create(JavaArchive.class)
				.addPackages(true, "com.jwxicc.cricket.mail")
				.addPackages(true, "org.apache.commons.lang")
				.addPackages(true, "javax.mail", "javax.activation")
				.addAsResource(EmptyAsset.INSTANCE, "META-INF/beans.xml");

		return archive;
	}

	@Test
	public void testSendEmail() throws MessagingException {
		new MailSender().sendEmail("clintonjmurdoch@gmail.com", "Arquillian Test Mail",
				"Arquillian test.\nThe system works!");
	}

}
