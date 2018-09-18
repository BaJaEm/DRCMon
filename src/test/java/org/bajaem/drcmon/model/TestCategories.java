
package org.bajaem.drcmon.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.drcmon.DBGenerator;
import org.bajaem.drcmon.respository.ProbeCategoryRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

@WithMockUser(username = "admin", roles = { "USER", "ADMIN" })
public class TestCategories extends DBGenerator
{

    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    private ProbeCategoryRepository repo;

    @Test
    public void testRepo()
    {
        assertNotNull(repo);
    }

    @Test
    public void CRUD()
    {
        try
        {
            LOG.debug("Creating new category");
            final ProbeCategory cat = createCategory("TEST", "test_channel");
            repo.save(cat);
            final ProbeCategory newCat = repo.findByName("TEST");
            assertNotNull(newCat);
            assertEquals("TEST", newCat.getName());
            assertEquals("test_channel", newCat.getChannel());

            newCat.setChannel("foo");

            repo.save(newCat);

            final ProbeCategory cat3 = repo.findByName("TEST");

            assertEquals("foo", cat3.getChannel());

        }
        catch (final Throwable t)
        {
            LOG.fatal(t.getMessage(), t);
            fail(t.getMessage());
        }
    }

    private ProbeCategory createCategory(final String name, final String channel)
    {
        final ProbeCategory cat = new ProbeCategory();
        cat.setName(name);
        cat.setChannel(channel);
        return cat;
    }
}
