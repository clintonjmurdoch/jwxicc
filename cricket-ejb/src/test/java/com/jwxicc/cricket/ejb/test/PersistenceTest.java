package com.jwxicc.cricket.ejb.test;

import java.io.File;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.logging.Logger;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.jwxicc.cricket.entity.Game;
import com.jwxicc.cricket.entity.manager.GameManagerImpl;
import com.jwxicc.cricket.interfaces.GameManager;

@RunWith(Arquillian.class)
public class PersistenceTest {
	
	private static final Logger logger = Logger.getLogger(PersistenceTest.class);
	
	@EJB
	GameManager manager;
	
    @Deployment
    public static Archive<?> createDeployment() {
    	
        JavaArchive archive = ShrinkWrap.create(JavaArchive.class)
            .addClasses(GameManager.class, GameManagerImpl.class).addPackage(Game.class.getPackage())
            .addAsResource("test-persistence.xml", "META-INF/persistence.xml");
        archive.as(ZipExporter.class).exportTo(new File("C:/test.jar"));
        logger.debug(archive.toString(true));
        
        return archive;
    }
    
    @Test
    public void testGetAGame() {
    	manager.findLazy(1);
    }
    
    @PersistenceContext
    EntityManager em;
    
    @Inject
    UserTransaction utx;
 
    // tests go here
}