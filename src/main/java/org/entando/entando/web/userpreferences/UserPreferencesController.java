/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.web.userpreferences;

import com.agiletec.aps.system.services.user.UserDetails;
import javax.validation.Valid;
import org.entando.entando.aps.system.services.userpreferences.IUserPreferencesService;
import org.entando.entando.ent.util.EntLogging.EntLogFactory;
import org.entando.entando.ent.util.EntLogging.EntLogger;
import org.entando.entando.web.common.model.SimpleRestResponse;
import org.entando.entando.web.userpreferences.model.UserPreferencesDto;
import org.entando.entando.web.userpreferences.model.UserPreferencesRequest;
import org.entando.entando.web.userpreferences.validator.UserPreferencesValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

@RestController
@SessionAttributes("user")
public class UserPreferencesController {

    private final EntLogger logger = EntLogFactory.getSanitizedLogger(this.getClass());

    @Autowired
    private IUserPreferencesService userPreferencesService;

    @Autowired
    private UserPreferencesValidator userPreferencesValidator;

    @GetMapping(value = "/userPreferences/{username:.+}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<UserPreferencesDto>> getUserPreferences(
            @ModelAttribute("user") UserDetails user, @PathVariable String username, BindingResult bindingResult) {
        logger.debug("Getting user '{}' preferences ", username);
        userPreferencesValidator.validate(user, username, bindingResult);
        UserPreferencesDto response = userPreferencesService.getUserPreferences(username);
        return new ResponseEntity<>(new SimpleRestResponse<>(response), HttpStatus.OK);
    }

    @PutMapping(value = "/userPreferences/{username:.+}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<UserPreferencesDto>> updateUserPreferences(
            @ModelAttribute("user") UserDetails user, @PathVariable String username,
            @Valid @RequestBody UserPreferencesRequest bodyRequest, BindingResult bindingResult) {
        logger.debug("Updating user '{}' preferences to -> {}", username, bodyRequest);
        userPreferencesValidator.validate(user, username, bindingResult);
        UserPreferencesDto response = userPreferencesService.updateUserPreferences(username, bodyRequest);
        return new ResponseEntity<>(new SimpleRestResponse<>(response), HttpStatus.OK);
    }
}
