package com.pms.entity;

import java.io.Serializable;
import java.util.Properties;
import java.util.UUID;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

public class CustomIdGenerator implements IdentifierGenerator {
	public static final String GENERATOR_NAME = "customIdGenerator";
	public static final String PREFIX_PARAM = "prefix";

	private String prefix = "";

	@Override
	public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
		this.prefix = params.getProperty(PREFIX_PARAM, "");
	}

	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object entityObject)
			throws HibernateException {
		String uuid = UUID.randomUUID().toString().substring(0, 6);
		return (prefix + uuid).toUpperCase();
	}
}
