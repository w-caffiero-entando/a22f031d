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

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.common.entity.helper.IEntityFilterBean;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import org.entando.entando.ent.exception.EntException;
import org.entando.entando.aps.system.services.dataobject.helper.IDataTypeListFilterBean;
import org.entando.entando.aps.system.services.dataobject.IDataObjectManager;

/**
 * Interfaccia base per l'implementazione del bean helper della showlet di
 * erogatore lista dataObject. La classe è a servizio sia delle funzioni
 * dell'area di amministrazione che di front-end.
 *
 * @author E.Santoboni
 */
public interface IDataObjectListWidgetHelper extends org.entando.entando.aps.system.services.dataobject.helper.IDataTypeListHelper {

	/**
	 * Restituisce la lista di identificativi di dataObject in base ai parametri
	 * di ricerca. I parametri utilizzati per la ricerca, per ciascuno di essi
	 * vengono estratti con questo ordine di importanza: hanno la precedenza i
	 * parametri specificati all'intrno del tag jsp, nel caso uno di essi sia
	 * nullo esso viene ricercato nei parametri di configurazione della showlet.
	 *
	 * @param bean Il contenitore delle informazioni base sulla interrogazione
	 * da eseguire.
	 * @param reqCtx Il contesto della richiesta.
	 * @return La lista di identificativi di dataObject in base ai parametri di
	 * ricerca.
	 * @throws Throwable In caso di errore.
	 */
	public List<String> getContentsId(IDataObjectListTagBean bean, RequestContext reqCtx) throws Throwable;

	/**
	 * Restituisce l'insieme dei filtri in base al parametro di configurazione
	 * della showlet detentore dei filtri. Il parametro è nella forma di:
	 * (key=KEY;value=VALUE;attributeFilter=TRUE|FALSE;start=START;end=END;like=TRUE|FALSE)+..<OTHER_FILTERS>
	 *
	 * @param dataType Il tipo di dataObject al quale i filtri vanno applicati.
	 * @param filtersShowletParam Il parametro della showlet nella forma
	 * corretta detentore dei filtri.
	 * @param reqCtx Il contesto della richiesta.
	 * @return L'insieme dei filtri dato dall'interpretazione del parametro.
	 */
	public EntitySearchFilter[] getFilters(String dataType, String filtersShowletParam, RequestContext reqCtx);

	/**
	 * Costruisce e restituisce un filtro in base ai parametri specificati. Il
	 * metodo è a servizio del sottoTag DataObjectListFilterTag di
	 * DataObjectListTag.
	 *
	 * @param dataObjectType Il Tipo di dataObject corrispondente al filtro da
	 * costruire.
	 * @param bean Il contenitore delle informazioni sul filtro da costruire.
	 * @param reqCtx Il contesto della richiesta corrente.
	 * @return Il nuovo filtro costruito in base ai parametri specificati.
	 * @deprecated From Entando 3.0 version 3.0.1. Use getFilter(String,
	 * IEntityFilterBean, RequestContext) method
	 */
	public EntitySearchFilter getFilter(String dataObjectType, IDataTypeListFilterBean bean, RequestContext reqCtx);

	public EntitySearchFilter getFilter(String dataObjectType, IEntityFilterBean bean, RequestContext reqCtx);

	/**
	 * @deprecated From Entando 3.0 version 3.0.1. Use
	 * getUserFilterOption(String, IEntityFilterBean, RequestContext) method
	 */
	public UserFilterOptionBean getUserFilterOption(String dataObjectType, IDataTypeListFilterBean bean, RequestContext reqCtx);

	public UserFilterOptionBean getUserFilterOption(String dataObjectType, IEntityFilterBean bean, RequestContext reqCtx);

	/**
	 * Restituisce il parametro da inserire nella configurazione della showlet.
	 * Il parametro è nella forma di:
	 * (key=KEY;value=VALUE;attributeFilter=TRUE|FALSE;start=START;end=END;like=TRUE|FALSE)+..<OTHER_FILTERS>
	 *
	 * @param filters I filtri tramite il quale ricavare il parametro.
	 * @return Il parametro da inserire nella configurazione della showlet.
	 * @deprecated From Entando 2.0 version 2.4.1. Use
	 * getFilterParam(EntitySearchFilter[]) method
	 */
	public String getShowletParam(EntitySearchFilter[] filters);

	/**
	 * Return tle list of the front-end user filter options configured into
	 * showlet parameters.
	 *
	 * @param bean The container of the base informations.
	 * @param reqCtx The request context.
	 * @return The list of the filter options.
	 * @throws EntException in case of error.
	 */
	public List<UserFilterOptionBean> getConfiguredUserFilters(IDataObjectListTagBean bean, RequestContext reqCtx) throws EntException;

	public static final String[] allowedMetadataFilterKeys
			= {IDataObjectManager.ENTITY_TYPE_CODE_FILTER_KEY, IDataObjectManager.DATA_OBJECT_DESCR_FILTER_KEY, IDataObjectManager.DATA_OBJECT_STATUS_FILTER_KEY,
				IDataObjectManager.DATA_OBJECT_CREATION_DATE_FILTER_KEY, IDataObjectManager.DATA_OBJECT_MODIFY_DATE_FILTER_KEY, IDataObjectManager.DATA_OBJECT_ONLINE_FILTER_KEY};

	/*public static final String[] allowedMetadataUserFilterOptionKeys
			= {UserFilterOptionBean.KEY_CATEGORY, UserFilterOptionBean.KEY_FULLTEXT};*/

	public static final String WIDGET_PARAM_CONTENT_TYPE = "contentType";
	public static final String WIDGET_PARAM_USER_FILTERS = "userFilters";
	public static final String WIDGET_PARAM_CATEGORIES = "categories";
	public static final String WIDGET_PARAM_OR_CLAUSE_CATEGORY_FILTER = "orClauseCategoryFilter";
	public static final String WIDGET_PARAM_FILTERS = "filters";
	public static final String WIDGET_PARAM_TITLE = "title";
	public static final String WIDGET_PARAM_PAGE_LINK = "pageLink";
	public static final String WIDGET_PARAM_PAGE_LINK_DESCR = "linkDescr";

	/**
	 * @deprecated Use {@link #WIDGET_PARAM_CONTENT_TYPE} instead
	 */
	public static final String SHOWLET_PARAM_CONTENT_TYPE = WIDGET_PARAM_CONTENT_TYPE;
	/**
	 * @deprecated Use {@link #WIDGET_PARAM_USER_FILTERS} instead
	 */
	public static final String SHOWLET_PARAM_USER_FILTERS = WIDGET_PARAM_USER_FILTERS;

	@Deprecated(/**
			 * to maintain compatibility with versions prior to 2.2.0.1
			 */
			)
	public static final String SHOWLET_PARAM_CATEGORY = "category";
	/**
	 * @deprecated Use {@link #WIDGET_PARAM_CATEGORIES} instead
	 */
	public static final String SHOWLET_PARAM_CATEGORIES = WIDGET_PARAM_CATEGORIES;
	/**
	 * @deprecated Use {@link #WIDGET_PARAM_OR_CLAUSE_CATEGORY_FILTER} instead
	 */
	public static final String SHOWLET_PARAM_OR_CLAUSE_CATEGORY_FILTER = WIDGET_PARAM_OR_CLAUSE_CATEGORY_FILTER;
	/**
	 * @deprecated Use {@link #WIDGET_PARAM_FILTERS} instead
	 */
	public static final String SHOWLET_PARAM_FILTERS = WIDGET_PARAM_FILTERS;
	/**
	 * @deprecated Use {@link #WIDGET_PARAM_TITLE} instead
	 */
	public static final String SHOWLET_PARAM_TITLE = WIDGET_PARAM_TITLE;
	/**
	 * @deprecated Use {@link #WIDGET_PARAM_PAGE_LINK} instead
	 */
	public static final String SHOWLET_PARAM_PAGE_LINK = WIDGET_PARAM_PAGE_LINK;
	/**
	 * @deprecated Use {@link #WIDGET_PARAM_PAGE_LINK_DESCR} instead
	 */
	public static final String SHOWLET_PARAM_PAGE_LINK_DESCR = WIDGET_PARAM_PAGE_LINK_DESCR;

}
