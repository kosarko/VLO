/*
 * Copyright (C) 2018 CLARIN
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

import eu.clarin.cmdi.vlo.FacetConstants;
import java.util.Locale;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;

/**
 *
 * @author Twan Goosen <twan@clarin.eu>
 */
public class PIDLabel extends GenericPanel<String> {

    private final static IConverter<String> labelConverter = new PidLabelLinkConverter(25);
    private final static IConverter<String> badgeConverter = new PidBadgeLinkConverter();

    /**
     *
     * @param id component id
     * @param model link model
     */
    public PIDLabel(String id, IModel<String> model) {
        super(id, model);
        add(new ExternalLink("link", model)
                .add(new Label("badge", model) {
                    @Override
                    public <C> IConverter<C> getConverter(Class<C> type) {
                        if (type.equals(String.class)) {
                            return (IConverter<C>) badgeConverter;
                        } else {
                            return super.getConverter(type);
                        }
                    }

                })
                .add(new Label("label", model) {
                    @Override
                    public <C> IConverter<C> getConverter(Class<C> type) {
                        if (type.equals(String.class)) {
                            return (IConverter<C>) labelConverter;
                        } else {
                            return super.getConverter(type);
                        }
                    }

                })
                .add(new AttributeModifier("title", model))
        );
    }

    private static class PidBadgeLinkConverter implements IConverter<String> {

        @Override
        public String convertToObject(String value, Locale locale) throws ConversionException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public String convertToString(String value, Locale locale) {
            final String lcValue = value.toLowerCase();
            if (lcValue.startsWith(FacetConstants.HANDLE_PREFIX)
                    || (lcValue.startsWith(FacetConstants.HANDLE_PROXY))
                    || (lcValue.startsWith(FacetConstants.HANDLE_PROXY_HTTPS))) {
                return "HDL";
            } else {
                return "WWW";
            }

        }
    }

    private static class PidLabelLinkConverter implements IConverter<String> {

        private final int maxLength;

        public PidLabelLinkConverter(int maxLength) {
            this.maxLength = maxLength;
        }

        @Override
        public String convertToObject(String value, Locale locale) throws ConversionException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public String convertToString(String value, Locale locale) {
            if (value == null) {
                return null;
            } else {
                final String converted = convertToString(value);
                if (maxLength >= 0 && maxLength < converted.length()) {
                    return converted.substring(0, maxLength - 3) + "...";
                } else {
                    return converted;
                }
            }
        }

        private String convertToString(String value) {
            final String lcValue = value.toLowerCase();
            if (lcValue.startsWith(FacetConstants.HANDLE_PREFIX)) {
                return value.substring(FacetConstants.HANDLE_PREFIX.length());
            }
            if (lcValue.startsWith(FacetConstants.HANDLE_PROXY)) {
                return value.substring(FacetConstants.HANDLE_PROXY.length());
            }
            if (lcValue.startsWith(FacetConstants.HANDLE_PROXY_HTTPS)) {
                return value.substring(FacetConstants.HANDLE_PROXY_HTTPS.length());
            }

            return value;
        }

    }
}
