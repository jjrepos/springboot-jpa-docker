package com.jjrepos.profileimage.api;


import com.jjrepos.atom.api.Link;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test base class.
 *
 * @author sjordan
 */
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@SpringBootTest
public abstract class AbstractResourceTest {

    protected static final String GATEWAY_HEADER = "X-AUTHENTICATION-Handshake";
    protected static final String GATEWAY_JSON = "{\"client_id\":\"zdd4zbfew7xzgx8nevc3w2u4\",\"access_token\":{\"Username\":\"employeeId\"}}";
    private static final Logger LOG = LoggerFactory.getLogger(AbstractResourceTest.class);
    @Autowired
    protected MockMvc mockMvc;

    protected String getGateWayHeader(String employeeId) {
        String json = GATEWAY_JSON.replace("employeeId", employeeId);
        json = Base64.getEncoder().encodeToString(json.getBytes());
        return json;
    }

    protected void assertLink(List<Link> links, String rel, String uri) {
        assertThat(links).isNotEmpty();
        assertThat(links).contains(Link.of(rel, uri));
    }

}
