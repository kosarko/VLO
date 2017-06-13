/*
 * Copyright (C) 2016 CLARIN
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
package eu.clarin.cmdi.vlo.wicket;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.Behavior;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Behavior that sends a tracking request to the Piwik tracker on an Ajax event.
 * By default, it registers on the "click" event. It assumes the JavaScript
 * tracker to be loaded (usually by means of the tracking code included in the
 * parent page).
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class AjaxPiwikTrackingBehavior extends AjaxEventBehavior {

    public static final Logger log = LoggerFactory.getLogger(AjaxPiwikTrackingBehavior.class);

    public static final String DEFAULT_EVENT = "click";
    private final String trackerCommand;
    private boolean async = true;

    protected AjaxPiwikTrackingBehavior(String event, String trackerCommand) {
        super(event);
        this.trackerCommand = trackerCommand;
    }

    @Override
    protected void onEvent(AjaxRequestTarget target) {
        String js = "if(Piwik != null) { ";
        if (async) {
            js += "var tracker = Piwik.getAsyncTracker(); ";
        } else {
            js += "var tracker = Piwik.getTracker(); ";
        }
        js += "if(tracker != null) { tracker." + getTrackerCommand(target) + ";}}";
        log.debug("Calling Piwik API: {}", js);
        target.appendJavaScript(js);
    }

    protected String getTrackerCommand(AjaxRequestTarget target) {
        return trackerCommand;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    /**
     * Tracking of an action with a custom name
     *
     * @param event
     * @param pageTitle
     * @return
     */
    public static Behavior newEventTrackingBehavior(String event, final String pageTitle) {
        return new AjaxPiwikTrackingBehavior(event, "trackPageView('" + pageTitle + "')");
    }

    public static Behavior newEventTrackingBehavior(final String pageTitle) {
        return newEventTrackingBehavior(DEFAULT_EVENT, pageTitle);
    }

    public static abstract class SearchTrackingBehavior extends AjaxPiwikTrackingBehavior {

        public SearchTrackingBehavior(String event) {
            super(event, null);
        }

        @Override
        protected String getTrackerCommand(AjaxRequestTarget target) {
            return "trackSiteSearch('" + getKeywords(target) + "')";
        }

        protected abstract String getKeywords(AjaxRequestTarget target);

    }

    public static class EventTrackingBehavior extends AjaxPiwikTrackingBehavior {

        private final String category;
        private final String action;

        public EventTrackingBehavior(String event, String category, String action) {
            super(event, null);
            this.category = category;
            this.action = action;
        }

        @Override
        protected String getTrackerCommand(AjaxRequestTarget target) {
            //trackEvent(category, action, [name], [value])
            final String name = getName(target);
            final String value = getValue(target);
            final StringBuilder command
                    = new StringBuilder("trackEvent(")
                            .append("'").append(category).append("'")
                            .append(", '").append(action).append("'");
            if (name != null) {
                command.append(", '").append(name).append("'");
            }
            if (value != null) {
                command.append(", '").append(value).append("'");
            }
            command.append(")");
            return command.toString();
        }

        protected String getName(AjaxRequestTarget target) {
            return null;
        }

        protected String getValue(AjaxRequestTarget target) {
            return null;
        }

    }
}
