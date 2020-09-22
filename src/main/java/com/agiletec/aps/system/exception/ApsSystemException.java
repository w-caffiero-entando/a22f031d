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
package com.agiletec.aps.system.exception;

/**
 * Eccezione di di sistema
 * @author W.Ambu
 *
 * @deprecated use {@link org.entando.entando.ent.exception.EntException} instead
 */
@Deprecated
public class ApsSystemException extends ApsException {
	/**
	 * Costruttore con solo messaggio
	 * @param message Il messaggio associato all'eccezione
	 */
	public ApsSystemException(String message){
		super(message);
	}
	
	/**
	 * Costruttore con messaggio e causa (precedente eccezione).
	 * @param message Il messaggio associato all'eccezione
	 * @param cause L'eccezione che ha causato l'eccezione originale 
	 */
	public ApsSystemException(String message, Throwable cause){
		super(message, cause);
	}
}
