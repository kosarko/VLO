/*
 * Copyright (C) 2017 CLARIN
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
package eu.clarin.cmdi.vlo.wicket.panels.record;

import eu.clarin.cmdi.vlo.FacetConstants;
import eu.clarin.cmdi.vlo.service.solr.SimilarDocumentsService;
import eu.clarin.cmdi.vlo.wicket.components.SolrFieldLabel;
import java.util.Collections;
import java.util.List;
import org.apache.solr.common.SolrDocument;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author twagoo
 */
public class SimilarDocumentsPanel extends GenericPanel<SolrDocument> {

    private final static Logger logger = LoggerFactory.getLogger(SimilarDocumentsPanel.class);

    @SpringBean
    private SimilarDocumentsService similarDocumentsService;

    public SimilarDocumentsPanel(String id, final IModel<SolrDocument> model) {
        super(id, model);
        IModel<List<SolrDocument>> similarDocumentsModel = new LoadableDetachableModel<List<SolrDocument>>() {
            @Override
            public List<SolrDocument> load() {
                final Object docId = model.getObject().getFieldValue(FacetConstants.FIELD_ID);
                if (docId instanceof String) {
                    return similarDocumentsService.getDocuments((String) docId);
                } else {
                    logger.warn("No (usable) document id for document, could not query for similar documents");
                    return Collections.emptyList();
                }
            }
        };

        add(new ListView<SolrDocument>("document", similarDocumentsModel) {
            @Override
            protected void populateItem(ListItem<SolrDocument> item) {
                item.add(new SolrFieldLabel("name", item.getModel(), FacetConstants.FIELD_NAME, "Unnamed record"));
            }

        });
    }

}
