package eu.clarin.cmdi.vlo.pages;

import eu.clarin.cmdi.vlo.FacetConstants;
import eu.clarin.cmdi.vlo.VloWebApplication;
import eu.clarin.cmdi.vlo.config.VloConfig;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Assert;
import org.junit.Test;


public class ResourceLinkPanelTest {
    
    static VloConfig testConfig;

    public static final String _SAME_STRING = "http://blabla";
    public static final String _RESOLVE_STRING = "hdl:1839/00-0000-0000-0004-3357-F";
    public static final String _RESOLVE_OUT = "http://corpus1.mpi.nl/qfs1/media-archive/dobes_data/Marquesan/North_Marquesas/Nuku_Hiva/narratives/Media/Expl-Anc-Et-NH.m4a";
    @Before
    public void setUp() {
        
        WicketTester wicketTester;

        String fileName = VloConfig.class.getResource("/VloConfig.xml").getFile();

        testConfig = VloConfig.readTestConfig(fileName);

        // optionally, modify the test configuration here

        wicketTester = new WicketTester(new VloWebApplication(testConfig));
    }

    @Test
    public void getNameFromLinkTest(){
        ResourceLinkPanel totest = new ResourceLinkPanel("a", FacetConstants.RESOURCE_TYPE_AUDIO, "a");
        String uit = totest.getNameFromLink(_SAME_STRING);
        Assert.assertTrue("Returns the same string if it does not start with the handle-prefix",uit.equals(_SAME_STRING));

        uit = totest.getNameFromLink(_RESOLVE_STRING);
        Assert.assertTrue("Returns the resolved url of the handle pid", uit.equals(_RESOLVE_OUT));
    }
}
