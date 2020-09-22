/*
 * Copyright 2015-Present Entando S.r.l. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.system.services.page;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.EntRuntimeException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.baseconfig.SystemParamsUtils;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.entando.entando.ent.exception.EntException;
import org.entando.entando.ent.util.EntLogging.EntLogger;
import org.entando.entando.ent.util.EntLogging.EntLogFactory;

public class PageTokenManager extends AbstractService implements IPageTokenManager {

	private static final EntLogger logger = EntLogFactory.getSanitizedLogger(PageTokenManager.class);

	private static final int SALT_LENGTH = 8;
	private static final int HASH_LENGTH = 20;
	public static final String KEYGEN_CIPHER = "AES/GCM/NoPadding";
	public static final String ENCRYPTION_CIPHER = "AES/GCM/NoPadding";

	private String salt;
	private String password;

	private ConfigInterface configManager;


	public String getSalt() {
		return salt;
	}

	public String getPassword() {
		return password;
	}

	protected ConfigInterface getConfigManager() {
		return configManager;
	}
	public void setConfigManager(ConfigInterface configManager) {
		this.configManager = configManager;
	}

	@Override
	public void init() throws Exception {
		String param = this.getConfigManager().getParam(PREVIEW_HASH);
		if (StringUtils.isBlank(param) || param.trim().length() < HASH_LENGTH) {
			param = this.generateRandomHash();
		}
		this.generateInternalSaltAndPassword(param);
	}

	@Override
	public String generateToken(String pageCode) {
		SecretKeyFactory keyFactory;
		try {
			keyFactory = SecretKeyFactory.getInstance(KEYGEN_CIPHER);
			SecretKey key = keyFactory.generateSecret(new PBEKeySpec(this.getPasswordCharArray()));
			Cipher pbeCipher = Cipher.getInstance(ENCRYPTION_CIPHER);
			pbeCipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(this.getSalt().getBytes(), 20));
			return base64Encode(pbeCipher.doFinal(pageCode.getBytes(StandardCharsets.UTF_8)));
		} catch (GeneralSecurityException e) {
			logger.error("Error during token generation for page code: {}", pageCode, e);
			throw new EntRuntimeException(
					String.format("Error during token generation for page code: \"%s\"", pageCode),
					e);
		}
	}

	protected void generateInternalSaltAndPassword(String param) {
		this.salt = StringUtils.left(param, SALT_LENGTH);
		this.password = StringUtils.substring(param, SALT_LENGTH);
	}

	protected String generateRandomHash() throws EntException {
		String param = "";
		try {
			String xmlParams = this.getConfigManager().getConfigItem(SystemConstants.CONFIG_ITEM_PARAMS);
			Map<String, String> params = SystemParamsUtils.getParams(xmlParams);
			if (!params.containsKey(IPageTokenManager.PREVIEW_HASH)) {
				param = RandomStringUtils.randomAlphanumeric(HASH_LENGTH);
				params.put(PREVIEW_HASH, param);
				String newXmlParams = SystemParamsUtils.getNewXmlParams(xmlParams, params);
				this.getConfigManager().updateConfigItem(SystemConstants.CONFIG_ITEM_PARAMS, newXmlParams);
			}
			logger.info("Successfully created a random page_preview_hash");
		} catch (Throwable t) {
			throw new EntException("Error occurred generating a random page_preview_hash", t);
		}
		return param;
	}

	protected char[] getPasswordCharArray() {
		return this.getPassword().toCharArray();
	}

	protected static byte[] base64Decode(String property) throws IOException {
		return Base64.getDecoder().decode(property);
	}

	protected static String base64Encode(byte[] bytes) {
		return Base64.getEncoder().encodeToString(bytes);
	}

}
