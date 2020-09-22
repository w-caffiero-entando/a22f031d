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
package org.entando.entando.aps.system.services.dataobject.widget;

import java.util.List;

import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import org.entando.entando.aps.system.services.dataobject.helper.IDataTypeListBean;

/**
 * Il bean detentore dei parametri di ricerca di liste di dataObject.
 *
 * @author E.Santoboni
 */
public interface IDataObjectListTagBean extends IDataTypeListBean {

	/**
	 * Setta il codice dei tipi di dataObject da cercare.
	 *
	 * @param dataObjectType Il codice dei tipi di dataObject da cercare.
	 */
	public void setContentType(String dataObjectType);

	/**
	 * Restituisce la categoria dei dataObject da cercare.
	 *
	 * @return La categoria dei dataObject da cercare.
	 * @deprecated use getCategories
	 */
	public String getCategory();

	/**
	 * Setta la categoria dei dataObject da cercare.
	 *
	 * @param category La categoria dei dataObject da cercare.
	 * @deprecated use addCategory
	 */
	public void setCategory(String category);

	public void addCategory(String category);

	/**
	 * Aggiunge un filtro in coda alla lista di filtri definita nel bean.
	 *
	 * @param filter Il filtro da aggiungere.
	 */
	public void addFilter(EntitySearchFilter filter);

	/**
	 * Aggiunge una opzione filtro utente in coda alla lista di filtri definita
	 * nel bean.
	 *
	 * @param filter L'opzione filtro utente da aggiungere.
	 */
	public void addUserFilterOption(UserFilterOptionBean filter);

	/**
	 * Restituisce la lista di opzioni filtro utente definita.
	 *
	 * @return La lista di opzioni filtro utente definita nel bean.
	 */
	public List<UserFilterOptionBean> getUserFilterOptions();

}
