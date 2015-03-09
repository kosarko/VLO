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
package eu.clarin.cmdi.vlo.config;

import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Arrays;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author twagoo
 */
public class VloConfigMarshallerTest {
    
    private final static Logger logger = LoggerFactory.getLogger(VloConfigMarshallerTest.class);
    private VloConfigMarshaller instance;
    
    @Before
    public void setUp() throws Exception {
        instance = new VloConfigMarshaller();
    }

    /**
     * Test of marshal method, of class VloConfigMarshaller.
     */
    @Test
    public void testUnmarshal() throws Exception {
        InputStream configFile = getClass().getResourceAsStream("/VloConfig.xml");
        VloConfig config = instance.unmarshal(new StreamSource(configFile));
        configFile.close();
        
        assertNotNull(config);
        assertEquals("http://localhost:8080/vlo-solr/core0/", config.getSolrUrl());
        assertEquals(2, config.getDataRoots().size());
        assertEquals(14, config.getFacetFields().size());
    }

    /**
     * Test of marshal method, of class VloConfigMarshaller.
     */
    @Test
    public void testMarshal() throws Exception {
        final VloConfig config = new VloConfig();
        config.setSolrUrl("http://server/solr");
        config.setDataRoots(Arrays.asList(new DataRoot("originName", new File("rootFile"), "prefix", "toStrip", Boolean.FALSE)));
        config.setFacetFields(Arrays.asList("collection", "country", "continent"));
        final StringWriter sw = new StringWriter();
        instance.marshal(config, new StreamResult(sw));
        logger.debug(sw.toString());
    }
    
}
