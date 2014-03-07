/*
 * Copyright (C) 2014 CLARIN
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package eu.clarin.cmdi.vlo.wicket.components;

import eu.clarin.cmdi.vlo.pojo.ResourceInfo;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * Panel that shows details for a selected resource (file name, mime type,
 * resource class, link to resource itself)
 *
 * @author twagoo
 */
public class ResourceLinkDetailsPanel extends Panel {

    public ResourceLinkDetailsPanel(String id, IModel<ResourceInfo> model) {
        super(id, model);
        setDefaultModel(new CompoundPropertyModel<ResourceInfo>(model));

        add(new Label("fileName"));
        add(new Label("mimeType"));
        add(new Label("resourceType"));

        final ExternalLink link = new ExternalLink("link", new PropertyModel<String>(model, "href"));
        link.add(new Label("href"));
        add(link);
    }

}
