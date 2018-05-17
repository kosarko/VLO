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

function createTour(restart, step) {
    var opts = {
        name: 'vlo-tour',
        redirect: true,
        steps: createTourSteps()
    };

    var tour = new Tour(opts);

    if (restart) {
        tour.restart();
    }

    // Initialize the tour
    tour.init();

    if (step !== undefined) {
        tour.goTo(step);
    }

    return tour;
}

function hideSimpleAndStart() {
    if (transitionFromSimple !== undefined) {
        transitionFromSimple(function () {
            createTour(true).start();
        });
    } else {
        createTour(true).start();
    }
}

function typeValue(input, value) {
    if (value.length > 0) {
        var current = $(input).val();
        $(input).val(current + value.substr(0, 1));
        if (value.length > 1) {
            setTimeout(function () {
                typeValue(input, value.substr(1));
            }, 30 + Math.floor(50 * Math.random()));
        }
    }
}

function createTourSteps() {

    //  Steps from CLARIN-PLUS screencast (https://b2drop.eudat.eu/s/FrrnfN1refArxFy)
    //  
    //    * text search
    //    * results
    //    * facets ('categories')
    //    * expand a few
    //    * filter by language (e.g. estonian)
    //        * one result remains
    //    * search result
    //        * expand for details
    //        * availability laundry tags
    //    * navigate to record page
    //        * tabs
    //        * original provider
    //        * goes into repository
    //        * back to VLO
    //        * back to search results
    //    * remove selection
    //    * adapt search
    //        * resource type
    //    * help page    

    return [
        {
            element: "#search-form",
            title: "Enter search terms",
            content: "Enter some keywords reflecting your interest in the search box to search through all records. You can use AND, OR and NOT to create more advanced queries. These and other options are explained in detail in the <a href='help#syntax'>Help page</a>.",
            placement: "auto top"
        }, {
            element: "#search-form button",
            title: "Search",
            content: "Press the button to search",
            placement: "auto bottom",
            onShow: function () {
                var searchInput = $('#search-form input.search-box')
                if (searchInput.val() === '') {
                    typeValue(searchInput, 'speech corpus');
                }
            },
            onShown: function (tour) {
                //listen to form submit - go to next if submitted
                $('#search-form .search-button').one('click', function () {
                    $('#search-form').data('tour-submitted', true);
                    tour.goTo(2);
                });
            }
        }, {
            element: "#searchresultitems",
            title: "Results",
            content: "Search results are shown here",
            placement: "auto top",
            orphan: true,
            onShow: function () {
                //submit search form if not submitted yet
                if (!$('#search-form').data('tour-submitted')) {
                    $('#search-form .search-button').click();
                }
            }
        }, {
            element: "#facets",
            title: "Facets",
            content: "Various <em>facets</em> provide a quick overview of the available content, and allow for narrowing down the search results.",
            placement: "auto right"
        }, {
            element: "#facets .facet:first",
            title: "Expand facet",
            content: "Click the name of a facet to expand it and see the values found within the current search results. Try selecting one or more values and see how the result listing changes.",
            placement: "auto right",
            onShown: function () {
                //expand
                if ($('#facets .facet:first').hasClass('collapsedfacet')) {
                    $('#facets .facet.collapsedfacet:first a.expandable-panel-toggle').click();
                }
            }
        }, {
            element: "#searchresultitems .searchresultitem:first .searchresultmoreless",
            title: "Search result",
            content: "Each search result represent a record that matches the search criteria. More information about the record can be displayed by expanding the description (click the '+' button).",
            placement: "auto left"
        }, {
            element: "#searchresultitems .searchresultitem:first .searchresult-licenseInfo",
            title: "Rights information",
            content: "Basic rights information, if available, is shown next to each search result.",
            placement: "auto left"
        }, {
            element: "#searchresultitems .searchresultitem:first h3 a",
            title: "Find out more",
            content: "Click the record title to find out everything about the described resources, including how to access the content. Press <strong>next</strong> to go to the record page for this result.",
            placement: "auto top"
        }, {
            element: ".record-tabpanel .nav-tabs li:last",
            title: "Record information",
            content: "Each of these tabs represents a different aspect of the record. The first tab lists the most important information about the described resource(s).",
            placement: "auto right",
            path: RegExp(/.*\/record.*/i),
            redirect: function () {
                var resultLink = $('#searchresultitems .searchresultitem:first h3 a');
                if (resultLink.length > 0) {
                    var newPath = resultLink.attr('href') + '#tour';
                    console.log("new path: " + newPath);

                    if (this._inited === true) {
                        document.location.href = newPath;
                        return (new jQuery.Deferred()).promise();
                    }
                }
            }
        }, {
            element: ".record-tabpanel .tab1",
            title: "Resources tab",
            content: "This tab lists the described resource(s). If present, you can click any of the links to click it. Note that not all records link directly to the described resources. In such cases, look for a landing page or search link.",
            placement: "auto top",
            path: RegExp(/.*\/record.*/i),
            onShow: function () {
                $(".tab1 a").click();
            }
        }, {
            element: ".record-tabpanel .tab2",
            title: "Availability tab",
            content: "Here you will find an indication of the known information regarding rights to using, accessing and/or distributing resources. Make sure to always check at the primary source before actually using or redistributing any of the resources!",
            placement: "auto top",
            path: RegExp(/.*\/record.*/i),
            onShow: function () {
                $(".tab2 a").click();
            }
        }, {
            element: ".record-tabpanel .tab3",
            title: "&quot;All metadata&quot; tab",
            content: "Any available metadata that is not shown in the details can be found here.",
            placement: "auto top",
            path: RegExp(/.*\/record.*/i),
            onShow: function () {
                $(".tab3 a").click();
            }
        }, {
            element: ".record-tabpanel .tab4",
            title: "&quot;Technical details&quot; tab",
            content: "Any available metadata that is not shown in the details can be found here.",
            placement: "auto top",
            path: RegExp(/.*\/record.*/i),
            onShow: function () {
                $(".tab4 a").click();
            }
        }, {
            element: "#recordprevnext .btn:first",
            title: "Search results navigation",
            content: "Use these buttons to navigate to the previous or next item from the result results without having to go back to the list.",
            placement: "auto bottom",
            path: RegExp(/.*\/record.*/i)
        }, {
            element: "#topnavigation",
            title: "&quot;Breadcrumbs&quot;",
            content: "These links seve as 'breadcrumbs' that show you where you are in your exploration and allow you to go back one or more levels .",
            placement: "auto bottom",
            path: RegExp(/.*\/record.*/i)
        }
    ];

}

function initTourSearchPage() {
    $(document).ready(function () {
        if (window.location.hash === '#tour') {
            hideSimpleAndStart();
        } else {
            $("#simple-jumbotron #learn-more").hide();
            $("#simple-jumbotron #take-tour").removeClass('hidden');
            $("#simple-jumbotron #take-tour").click(function () {
                hideSimpleAndStart();
            });
        }

    });
}

function initTourRecordPage() {
    $(document).ready(function () {
        if (window.location.hash === '#tour') {
            var tour = createTour(false);
            if (tour.getCurrentStep() !== null && !tour.ended()) {
                tour.start();
            }
        }
    });
}
