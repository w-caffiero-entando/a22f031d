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
package com.agiletec.aps.system.common.entity;

import java.util.Iterator;
import java.util.List;

import org.entando.entando.ent.util.EntLogging.EntLogger;
import org.entando.entando.ent.util.EntLogging.EntLogFactory;

/**
 * Thread Class used to reload all entity references. 
 * @author E.Santoboni
 */
public class ReloadingReferencesThread extends Thread {

	private static final EntLogger _logger = EntLogFactory.getSanitizedLogger(ReloadingReferencesThread.class);
	
	/**
	 * Setup the thread for the references reloading
	 * @param entityManager the service that handles the entities
	 * @param typeCode The type Code of entities to reload. If null, reload all entities.
	 */
	public ReloadingReferencesThread(ApsEntityManager entityManager, String typeCode) {
		this._entityManager = entityManager;
		this._typeCode = typeCode;
	}
	
	@Override
	public void run() {
		if (null != this._typeCode) {
			this.reloadEntityTypeReferences(this._typeCode);
		} else {
			List<String> typeCodes = this._entityManager.getEntityTypeCodes();
			Iterator<String> iter = typeCodes.iterator();
			while (iter.hasNext()) {
				String typeCode = (String) iter.next();
				this.reloadEntityTypeReferences(typeCode);
			}
		}
	}
	
	protected void reloadEntityTypeReferences(String typeCode) {
		try {
			this._entityManager.reloadEntitySearchReferencesByType(typeCode);
		} catch (Throwable t) {
			_logger.error("reloadEntityTypeReferences of type : {}", typeCode, t);
			//ApsSystemUtils.logThrowable(t, this, "reloadEntityTypeReferences of type : " + typeCode);
		}
	}
	
	private ApsEntityManager _entityManager;
	private String _typeCode;
	
}