package org.bajaem.drcmon.model;

import static org.junit.Assert.assertNotNull;

import org.bajaem.drcmon.DBGenerator;
import org.bajaem.drcmon.respository.ProbeTypeRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

@WithMockUser(username = "admin", roles = { "USER", "ADMIN" })
public class TestTypes extends DBGenerator
{
    @Autowired
    private ProbeTypeRepository repo;

    @Test
    public void testTypeRepoExists()
    {
        assertNotNull(repo);
    }

    @Test
    public void testBasicTypes()
    {
        repo.findAll().forEach((p) -> assertNotNull(p.getDescription()));
        final ProbeType ping = repo.findByName("Ping");
        assertNotNull(ping);

        final ProbeType portMon = repo.findByName("PortMon");
        assertNotNull(portMon);

        final ProbeType sql = repo.findByName("SQLQuery");
        assertNotNull(sql);

        final ProbeType rest = repo.findByName("RESTGet");
        assertNotNull(rest);

    }
}
