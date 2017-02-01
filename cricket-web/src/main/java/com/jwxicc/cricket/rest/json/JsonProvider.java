package com.jwxicc.cricket.rest.json;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

@Provider
@Consumes({MediaType.APPLICATION_JSON, "text/json"})
@Produces({MediaType.APPLICATION_JSON, "text/json"})
public class JsonProvider extends JacksonJsonProvider {

	@Override
	public void writeTo(Object value, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String,Object> httpHeaders, OutputStream entityStream) throws IOException {
		
		ObjectMapper mapper = locateMapper(type, mediaType);
		mapper.registerModule(new Hibernate4Module());
		mapper.setSerializationInclusion(Include.NON_EMPTY);
		
		super.writeTo(value, type, genericType, annotations, mediaType, httpHeaders, entityStream);
	}
}
