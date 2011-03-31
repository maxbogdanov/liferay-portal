/**
 * Copyright (c) 2000-2011 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.repository.cmis;

import com.liferay.portal.InvalidRepositoryException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.repository.RepositoryException;
import com.liferay.portal.kernel.repository.cmis.CMISRepositoryWrapper;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.security.auth.PrincipalThreadLocal;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;

/**
 * @author Alexander Chow
 */
public class CMISAtomPubRepository extends CMISRepositoryWrapper {

	public Object getSession() throws PortalException, RepositoryException {
		Session session = null;

		Map<String, String> parameters = new HashMap<String, String>();

		String login = getLogin();
		String password = PrincipalThreadLocal.getPassword();
		Locale locale = LocaleUtil.getDefault();

		parameters.put(SessionParameter.USER, login);
		parameters.put(SessionParameter.PASSWORD, password);
		parameters.put(
			SessionParameter.LOCALE_ISO3166_COUNTRY,
			locale.getCountry());
		parameters.put(SessionParameter.LOCALE_ISO639_LANGUAGE,
			locale.getLanguage());
		
		parameters.put(
			SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());

		parameters.put(
			SessionParameter.ATOMPUB_URL, getTypeSettingsValue(_ATOMPUB_URL));

		CMISRepositoryUtil.checkRepository(
			getRepositoryId(), parameters, getTypeSettingsProperties(),
			_REPOSITORY_ID);

		session = CMISRepositoryUtil.getSessionFactory().createSession(
			parameters);

		session.setDefaultContext(CMISRepositoryUtil.getOperationContext());

		return session;
	}

	public String[] getSupportedConfigurations() {
		return _SUPPORTED_CONFIGURATIONS;
	}

	public String[][] getSupportedParameters() {
		return _SUPPORTED_PARAMETERS;
	}

	protected String getTypeSettingsValue(String typeSettingsKey)
		throws InvalidRepositoryException {

		UnicodeProperties typeSettingsProperties = getTypeSettingsProperties();

		return CMISRepositoryUtil.getTypeSettingsValue(
			typeSettingsProperties, typeSettingsKey);
	}

	private static final String _ATOMPUB_URL = "ATOMPUB_URL";

	private static final String _CONFIGURATION_ATOMPUB = "ATOMPUB";

	private static final String _REPOSITORY_ID = "REPOSITORY_ID";

	private static final String[] _SUPPORTED_CONFIGURATIONS = {
		_CONFIGURATION_ATOMPUB
	};

	private static final String[][] _SUPPORTED_PARAMETERS = {
		new String[] {
			_ATOMPUB_URL, _REPOSITORY_ID
		}
	};

}