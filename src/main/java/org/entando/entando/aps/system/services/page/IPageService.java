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
package org.entando.entando.aps.system.services.page;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Collection;
import java.util.List;

import org.entando.entando.aps.system.services.IComponentExistsService;
import org.entando.entando.aps.system.services.IComponentUsageService;
import org.entando.entando.aps.system.services.page.model.PageConfigurationDto;
import org.entando.entando.aps.system.services.page.model.PageDto;
import org.entando.entando.aps.system.services.page.model.PagesStatusDto;
import org.entando.entando.aps.system.services.page.model.WidgetConfigurationDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.page.model.PagePositionRequest;
import org.entando.entando.web.page.model.PageRequest;
import org.entando.entando.web.page.model.PageSearchRequest;
import org.entando.entando.web.page.model.WidgetConfigurationRequest;
import org.springframework.lang.Nullable;

/**
 *
 * @author paddeo
 */
public interface IPageService extends IComponentExistsService, IComponentUsageService {

    String BEAN_NAME = "PageService";

    public static final String STATUS_ONLINE = "published";
    public static final String STATUS_DRAFT = "draft";
    public static final String STATUS_UNPUBLISHED = "unpublished";

    public PageDto getPage(String pageCode, String status);

    public PageDto addPage(PageRequest pageRequest);

    public void removePage(String pageName);

    public PageDto updatePage(String pageCode, PageRequest pageRequest);

    public PageDto getPatchedPage(String pageCode, JsonNode patch);

    default List<PageDto> getPages(String parentCode) {
        return getPages(parentCode, null, null);
    }

    public List<PageDto> getPages(String parentCode,
            @Nullable String forLinkingToOwnerGroup, @Nullable Collection<String> forLinkingToExtraGroups);

    public PagedMetadata<PageDto> searchPages(PageSearchRequest request, List<String> allowedGroups);

    /**
     * Search against online pages
     *
     * @param request
     * @return
     */
    public PagedMetadata<PageDto> searchOnlineFreePages(RestListRequest request);

    public PageDto movePage(String pageCode, PagePositionRequest pageRequest);

    public PageConfigurationDto getPageConfiguration(String pageCode, String status);

    public PageConfigurationDto restorePageConfiguration(String pageCode);

    public WidgetConfigurationDto getWidgetConfiguration(String pageCode, int frameId, String status);

    public WidgetConfigurationDto updateWidgetConfiguration(String pageCode, int frameId, WidgetConfigurationRequest widget);

    public void deleteWidgetConfiguration(String pageCode, int frameId);

    public PageDto updatePageStatus(String pageCode, String status);

    public PageConfigurationDto applyDefaultWidgets(String pageCode);

    public PagedMetadata<?> getPageReferences(String pageCode, String manager, RestListRequest requestList);

    public PagesStatusDto getPagesStatus();

    public List<PageDto> listViewPages();

}
