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
package com.agiletec.aps.tags;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.entando.entando.ent.util.EntLogging.EntLogger;
import org.entando.entando.ent.util.EntLogging.EntLogFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.IPage;

/**
 * Includes the jsp associated to the widget as configured in the page frame
 * 
 */
public class WidgetTag extends TagSupport {

	private static final EntLogger _logger = EntLogFactory.getSanitizedLogger(WidgetTag.class);
	
	public int doEndTag() throws JspException {
		ServletRequest req =  this.pageContext.getRequest();
		RequestContext reqCtx = (RequestContext) req.getAttribute(RequestContext.REQCTX);
		IPage page = (IPage) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE);
		try {
			String showletOutput[] = (String[]) reqCtx.getExtraParam("ShowletOutput");
			if(_frame <0 || _frame >= showletOutput.length){
				_logger.error("Frame attribute {} exceeds the limit in the page {}", _frame, page.getCode());
				String msg = "Frame attribute =" + _frame + " exceeds the limit in the page " + page.getCode();
				throw new JspException(msg);
				//ApsSystemUtils.getLogger().error(msg);				
			}
			String showlet = showletOutput[_frame];
			if (null == showlet) 
				showlet = "";
			this.pageContext.getOut().print(showlet);
		} catch (Throwable t) {
			String msg = "Error detected in the inclusion of the output widget";
			_logger.error("Error detected in the inclusion of the output widget", t);
			//ApsSystemUtils.logThrowable(t, this, "doEndTag", msg);
			throw new JspException(msg, t);
		}
		
		return EVAL_PAGE;
	}
	
	/**
	 * Return the frame attribute
	 * @return The positional frame number
	 */
	public int getFrame() {
		return _frame;
	}

	/**
	 * Set the frame attribute
	 * @param frame The positional frame number
	 */
	public void setFrame(int frame) {
		this._frame = frame;
	}

	private int _frame;
}
