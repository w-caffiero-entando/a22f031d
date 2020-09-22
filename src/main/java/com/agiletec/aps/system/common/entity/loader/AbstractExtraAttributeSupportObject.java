/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package com.agiletec.aps.system.common.entity.loader;

import java.io.InputStream;
import java.io.Serializable;

import javax.servlet.ServletContext;

import org.entando.entando.ent.util.EntLogging.EntLogger;
import org.entando.entando.ent.util.EntLogging.EntLogFactory;
import org.springframework.web.context.ServletContextAware;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.util.FileTextReader;

/**
 * @author E.Santoboni
 */
public abstract class AbstractExtraAttributeSupportObject implements ServletContextAware, Serializable {

	private static final EntLogger _logger =  EntLogFactory.getSanitizedLogger(AbstractExtraAttributeSupportObject.class);
	
	/**
	 * Extract the xml with the definition of Attribute support object.
	 * The xml will be extracted from the file path in the instance param.
	 * @return The xml with the definition.
	 * @throws Exception In case of error
	 */
	protected String extractXml() throws Exception {
		String xml = null;
		InputStream is = null;
		try {
			is = this._servletContext.getResourceAsStream(this.getDefsFilePath());
			if (null == is) {
				//ApsSystemUtils.getLogger().error("Null Input Stream - Definition file path " + this.getDefsFilePath());
				_logger.warn("Null Input Stream - Definition file path {}", this.getDefsFilePath());
				return null;
			}
			xml = FileTextReader.getText(is);
		} catch (Throwable t) {
			//String message = "Error detected while extracting extra Attribute Objects : file " + this.getDefsFilePath();
			//ApsSystemUtils.logThrowable(t, this, "extractXml", message);
			_logger.error("Error detected while extracting extra Attribute Objects : file {}", this.getDefsFilePath(), t);
		} finally {
			if (null != is) {
				is.close();
			}
		}
		return xml;
	}
	
	/**
	 * (**DEPRECATED since Entando 3.0.1** Use setEntityManagerNameDest) Set the entity manager destination.
	 * @param entityManagerDest The entity manager destination.
	 * @deprecated Since Entando 3.0.1. To avoid circolar references. Use setEntityManagerNameDest
	 */
	public void setEntityManagerDest(IEntityManager entityManagerDest) {
		String name = ((IManager) entityManagerDest).getName();
		this.setEntityManagerNameDest(name);
	}
	
	protected String getEntityManagerNameDest() {
		return _entityManagerNameDest;
	}
	public void setEntityManagerNameDest(String entityManagerNameDest) {
		this._entityManagerNameDest = entityManagerNameDest;
	}
	
	protected String getDefsFilePath() {
		return _defsFilePath;
	}
	public void setDefsFilePath(String defsFilePath) {
		this._defsFilePath = defsFilePath;
	}
	
	@Override
	public void setServletContext(ServletContext servletContext) {
		this._servletContext = servletContext;
	}
	
	private String _entityManagerNameDest;
	private String _defsFilePath;
	
	private ServletContext _servletContext;
	
}