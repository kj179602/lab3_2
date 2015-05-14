package edu.iis.mto.staticmock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import edu.iis.mto.staticmock.reader.WebServiceNewsReader;

@RunWith(PowerMockRunner.class)
@PrepareForTest( {ConfigurationLoader.class, NewsReaderFactory.class} )
public class LoadNewsTest {

	@Test
	public void loadNews_onePublicContent_oneSubscribentContent() {
		
		IncomingNews incomingNews = new IncomingNews();
		IncomingInfo incomingInfo = new IncomingInfo("someContent",SubsciptionType.NONE);
		incomingNews.add(incomingInfo);
		
		Configuration configuration = mock(Configuration.class);
		when(configuration.getReaderType()).thenReturn("WS");
		
		mockStatic(ConfigurationLoader.class);
		ConfigurationLoader configurationLoader = mock( ConfigurationLoader.class );
		when(ConfigurationLoader.getInstance()).thenReturn(configurationLoader);
		when(configurationLoader.loadConfiguration()).thenReturn(configuration);
		
		WebServiceNewsReader wsnReader = mock(WebServiceNewsReader.class);
		when(wsnReader.read()).thenReturn(incomingNews);
		
		mockStatic(NewsReaderFactory.class);			
		when(NewsReaderFactory.getReader((String)Mockito.any())).thenReturn(wsnReader);
		
		
		NewsLoader newsLoader = new NewsLoader();
		PublishableNews publishableNews = newsLoader.loadNews();
		
		List<String> publicContent = (List< String >)Whitebox.getInternalState(publishableNews, "publicContent");
		List<String> expectedResult = new ArrayList<String>();
		expectedResult.add("someContent");
		assertEquals(expectedResult , publicContent);
		
		List<String> subscribentContent = (List< String >)Whitebox.getInternalState(publishableNews, "subscribentContent");
		expectedResult = new ArrayList<String>();
		expectedResult.add("someContent");
		assertEquals(expectedResult , publicContent);		
	}
}
